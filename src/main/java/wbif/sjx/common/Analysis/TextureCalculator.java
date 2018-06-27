package wbif.sjx.common.Analysis;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.plugin.Duplicator;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;
import wbif.sjx.common.MathFunc.CumStat;
import wbif.sjx.common.MathFunc.Indexer;
import wbif.sjx.common.Object.Point;
import wbif.sjx.common.Object.Volume;

import java.util.ArrayList;
import java.util.LinkedHashMap;

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

        // Getting image size
        int height = image.getHeight();
        int width = image.getWidth();
        int nSlices = image.size();

        // Initialising new HashMap (acting as a sparse matrix) to store the co-occurrence matrix
        matrix = new LinkedHashMap<>();

        // Indexer to getPixelValue index for addressing HashMap
        indexer = new Indexer(256,256);

        // Normalising to the intensity range within the object
        image = normaliseIntensity(image,volume);

        // Running through all specified positions,
        int count = 0;
        for (Point<Integer> point:volume.getPoints()) {
            int x = point.getX();
            int y = point.getY();
            int z = point.getZ();

            if (x+xOffs >= 0 & x+xOffs < width & y+yOffs >= 0 & y+yOffs < height & z+zOffs >= 0 & z+zOffs < nSlices) {
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
        image = image.duplicate();

        // The analysis requires discrete pixels values.  Therefore, 32-bit images are converted to 8-bit
        CumStat cs = IntensityCalculator.calculate(image);
        double min = cs.getMin();
        double max = cs.getMax();

        for (int z = 1; z <= image.size(); z++) {
            ImageProcessor processor = image.getProcessor(z);
            processor.setMinAndMax(min, max);
            processor.convertToByte(false);
            image.setProcessor(processor,z);

        }

        return image;

    }

    ImageStack normaliseIntensity(ImageStack image, Volume volume) {
        // Duplicating the image, so the original isn't affected
        image = image.duplicate();

        // The analysis requires discrete pixels values.  Therefore, 32-bit images are converted to 8-bit
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;

        for (Point<Integer> point:volume.getPoints()) {
            double val = image.getVoxel(point.getX(),point.getY(),point.getZ());
            min = Math.min(min,val);
            max = Math.max(max,val);
        }

        for (int z = 0; z < image.size(); z++) {
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    double val = image.getVoxel(x, y, z);
                    val = (val-min)/(max-min)*255;
                    image.setVoxel(x, y, z, val);
                }
            }
        }

        return image;

    }

    void addValueToConfusionMatrix(ImageStack image, int x, int y, int z) {
        // Getting current pixel value
        int v1 = (int) image.getVoxel(x, y, z);

        // Getting tested pixel value
        int v2 = (int) image.getVoxel(x + xOffs, y + yOffs, z + zOffs);

        // Storing in the HashMap
        int index1 = indexer.getIndex(new int[]{v1, v2});
        matrix.computeIfAbsent(index1, k -> matrix.put(index1, 0d));
        matrix.put(index1, matrix.get(index1) + 1);

        int index2 = indexer.getIndex(new int[]{v2, v1});
        matrix.computeIfAbsent(index2, k -> matrix.put(index2, 0d));
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
}
