package wbif.sjx.common.Analysis;

import ij.ImagePlus;
import ij.process.ImageConverter;
import wbif.sjx.common.MathFunc.CumStat;
import wbif.sjx.common.MathFunc.Indexer;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Texture measures, largely from  Robert M. Haralick, K. Shanmugam, and Its'hak Dinstein, "Textural Features for Image
 * Classification", IEEE Transactions on Systems, Man, and Cybernetics, 1973, SMC-3 (6): 610–621
 */
public class TextureCalculator {
    private LinkedHashMap<Integer,Double> matrix = new LinkedHashMap<>();
    private int xOffs = 1;
    private int yOffs = 0;
    private int zOffs = 0;


    // PUBLIC METHODS

    /**
     * Calculates the co-occurance matrix on the specified pixels, using the specified offset
     * @param image
     * @param xOffs
     * @param yOffs
     * @param zOffs
     * @param positions ArrayList containing x,y,z positions of pixels in region of interest
     */
    public void calculate(ImagePlus image, int xOffs, int yOffs, int zOffs, ArrayList<int[]> positions) {
        if (image.getBitDepth() != 8) {
            // The analysis requires discrete pixels values.  Therefore, 32-bit images are converted to 8-bit
            CumStat cs = IntensityCalculator.calculate(image);
            double min = cs.getMin();
            double max = cs.getMax();

            image.getProcessor().setMinAndMax(min, max);
            new ImageConverter(image).convertToGray8();
        }

        // Setting the local values of xOffs and yOffs
        this.xOffs = xOffs;
        this.yOffs = yOffs;
        this.zOffs = zOffs;

        // Getting image size
        int height = image.getHeight();
        int width = image.getWidth();
        int nSlices = image.getNSlices();

        // Initialising new HashMap (acting as a sparse matrix) to store the co-occurrence matrix
        matrix = new LinkedHashMap<>();

        // Indexer to get index for addressing HashMap
        Indexer indexer = new Indexer(256,256);

        // Running through all specified positions,
        for (int[] pos:positions) {
            if (pos[0]>=0 & pos[0]<width & pos[1]>=0 & pos[1]<height & pos[2]>=0 & pos[2]<nSlices) {
                // Getting current pixel value
                image.setPosition(1, pos[2], 1);
                int v1 = image.getProcessor().getPixel(pos[0], pos[1]);

                // Getting tested pixel value
                image.setPosition(1, pos[2] + zOffs, 1);
                int v2 = image.getProcessor().getPixel(pos[0] + xOffs, pos[1] + yOffs);

                // Storing in the HashMap
                int index1 = indexer.getIndex(new int[]{v1, v2});
                matrix.computeIfAbsent(index1, k -> matrix.put(index1, 0d));
                matrix.put(index1, matrix.get(index1) + 1);

            }
        }

      // Applying normalisation
        int nPixels = positions.size();
        matrix.replaceAll((k,v) -> v/nPixels);

    }

    /**
     * Calculates the co-occurance matrix on the entire image using the specified offset
     * @param image
     * @param xOffs
     * @param yOffs
     * @param zOffs
     */
    public void calculate(ImagePlus image, int xOffs, int yOffs, int zOffs) {
        // Creating an ArrayList of all pixel coordinates in the image.  These are the coordinates to be tested.  This
        // isn't the most efficient way to do it, but it retains compatibility with the general method used to
        // calculate for small regions
        int height = image.getHeight();
        int width = image.getWidth();
        int nSlices = image.getNSlices();

        ArrayList<int[]> positions = new ArrayList<>(height*width*nSlices);

        int iter = 0;
        for (int z = 0; z < image.getNSlices(); z++) {
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    positions.add(iter++,new int[]{x,y,z});

                }
            }
        }

        calculate(image,xOffs,yOffs,zOffs,positions);

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
            entropy = entropy + val*Math.log(val);
        }

        return -entropy;

    }


    // GETTERS


    public LinkedHashMap<Integer, Double> getCoOccurrenceMatrix() {
        return matrix;
    }

    public int getxOffs() {
        return xOffs;
    }

    public int getyOffs() {
        return yOffs;
    }

    public int getzOffs() {
        return zOffs;
    }
}
