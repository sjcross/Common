package wbif.sjx.common.Analysis;

import java.util.LinkedHashMap;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import wbif.sjx.common.MathFunc.CumStat;
import wbif.sjx.common.MathFunc.Indexer;
import wbif.sjx.common.Object.Point;
import wbif.sjx.common.Object.Volume.Volume;
import wbif.sjx.common.Process.IntensityMinMax;

/**
 * Texture measures, largely from  Robert M. Haralick, K. Shanmugam, and Its'hak Dinstein, "Textural Features for Image
 * Classification", IEEE Transactions on Systems, Man, and Cybernetics, 1973, SMC-3 (6): 610–621
 */
public class TextureCalculator {
    private LinkedHashMap<Integer,Double> matrix;
    private Indexer indexer;
    private final int xOffs;
    private final int yOffs;
    private final int zOffs;


    // CONSTRUCTOR

    public TextureCalculator(int xOffs, int yOffs, int zOffs) {
        this.xOffs = xOffs;
        this.yOffs = yOffs;
        this.zOffs = zOffs;

    }


    // PUBLIC METHODS

    /**
     * Calculates the co-occurance matrix on the specified pixels, using the specified offset
     * @param image
     * @param volume
     */
    public void calculate(ImageStack image, Volume volume) {
        if (image.getBitDepth() != 8) image = convertTo8Bit(image);

        // Initialising new HashMap (acting as a sparse matrix) to store the co-occurrence matrix
        matrix = new LinkedHashMap<>();

        // Indexer to getPixelValue index for addressing HashMap
        indexer = new Indexer(256,256);

        // Running through all specified positions,
        int count = 0;
        for (Point<Integer> point:volume.getCoordinateSet()) {
            int x = point.getX();
            int y = point.getY();
            int z = point.getZ();

            if (volume.contains(new Point<>(x+xOffs,y+yOffs,z+zOffs))) {
                addValueToConfusionMatrix(image,x,y,z);
                count = count + 2;
            }
        }

        // Applying normalisation
        int finalCount = count;
        matrix.replaceAll((k, v) -> v/ finalCount);

    }

    /**
     * Calculates the co-occurance matrix on the entire image using the specified offset
     * @param image
     */
    public void calculate(ImageStack image) {
        if (image.getBitDepth() != 8) image = convertTo8Bit(image);

        // Getting image size
        int height = image.getHeight();
        int width = image.getWidth();
        int nSlices = image.size();

        // Initialising new HashMap (acting as a sparse matrix) to store the co-occurrence matrix
        matrix = new LinkedHashMap<>();

        // Indexer to getPixelValue index for addressing HashMap
        indexer = new Indexer(256,256);

        // Running through all specified positions,
        int count = 0;
        for (int z = 0; z < image.size(); z++) {
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    if (x+xOffs >= 0 & x+xOffs < width & y+yOffs >= 0 & y+yOffs < height & z+zOffs >= 0 & z+zOffs < nSlices) {
                        addValueToConfusionMatrix(image,x,y,z);
                        count = count + 2;
                    }
                }
            }
        }

        // Applying normalisation
        int finalCount = count;
        matrix.replaceAll((k, v) -> v/ finalCount);

    }

    ImageStack convertTo8Bit(ImageStack image) {
        // Duplicating the image, so the original isn't affected
        ImagePlus ipl = new ImagePlus("Temp",image.duplicate());

        IntensityMinMax.run(ipl,true);
        IJ.run(ipl, "8-bit", null);

        return ipl.getImageStack();

    }

    void addValueToConfusionMatrix(ImageStack image, int x, int y, int z) {
        int v1 = 0;
        int v2 = 0;
        // Getting current pixel value
        try {
            v1 = (int) image.getVoxel(x, y, z);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Out of bounds at "+x+","+y+","+z);
        }

        // Getting tested pixel value
        try {
            v2 = (int) image.getVoxel(x + xOffs, y + yOffs, z + zOffs);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Out of bounds at "+(x+ xOffs)+","+(y+yOffs)+","+(z+zOffs));
        }

        // Storing in the HashMap
        int index1 = indexer.getIndex(new int[]{v1, v2});
        int index2 = indexer.getIndex(new int[]{v2, v1});
        if (!matrix.containsKey(index1)) {
            matrix.put(index1,0d);
            matrix.put(indexer.getIndex(new int[]{v2, v1}),0d);
        }
        matrix.put(index1, matrix.get(index1) + 1);
        matrix.put(index2, matrix.get(index2) + 1);

    }

    /**
     * Calculates the angular second moment from the co-occurance matrix
     * @return
     */
    public double getASM() {
        double ASM = 0;

        for (double val:matrix.values()) {
            ASM = ASM + val*val;

        }

        return ASM;

    }

    /**
     * Calculates the contrast from the co-occurance matrix
     * @return
     */
    public double getContrast() {
        double contrast = 0;

        Indexer indexer = new Indexer(256,256);
        for (Integer index:matrix.keySet()) {
            int[] pos = indexer.getCoord(index);

            contrast = contrast + (pos[1]-pos[0])*(pos[1]-pos[0])*matrix.get(index);

        }

        return contrast;

    }

    /**
     * Calculates the correlation from the co-occurance matrix
     * @return
     */
    public double getCorrelation() {
        double correlation = 0;

        // Getting partial probability density functions
        CumStat px = new CumStat();
        CumStat py = new CumStat();

        Indexer indexer = new Indexer(256,256);
        for (Integer index:matrix.keySet()) {
            int[] pos = indexer.getCoord(index);

            px.addMeasure(pos[0],matrix.get(index));
            py.addMeasure(pos[1],matrix.get(index));

        }

        // Calculating the mean and standard deviations for the partial probability density functions
        double xMean = px.getMean();
        double yMean = py.getMean();

        double xStd = px.getStd(CumStat.POPULATION);
        double yStd = py.getStd(CumStat.POPULATION);

        // Calculating the correlation
        for (Integer index:matrix.keySet()) {
            int[] pos = indexer.getCoord(index);
            correlation = correlation + (pos[0]-xMean)*(pos[1]-yMean)*matrix.get(index);

        }

        correlation = correlation/(xStd*yStd);

        return correlation;

    }

    /**
     * Calculates the entropy from the co-occurance matrix
     * @return
     */
    public double getEntropy() {
        double entropy = 0;

        for (double val:matrix.values()) {
            entropy = entropy + val*Math.log(val)/Math.log(2);
        }

        return -entropy;

    }


    // GETTERS

    public LinkedHashMap<Integer, Double> getCoOccurrenceMatrix() {
        return matrix;
    }

    public Indexer getIndexer() {
        return indexer;
    }

    public int getXOffs() {
        return xOffs;
    }

    public int getYOffs() {
        return yOffs;
    }

    public int getZOffs() {
        return zOffs;
    }
}
