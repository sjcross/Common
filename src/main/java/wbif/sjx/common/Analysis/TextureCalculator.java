package wbif.sjx.common.Analysis;

import ij.ImagePlus;
import wbif.sjx.common.MathFunc.CumStat;
import wbif.sjx.common.MathFunc.Indexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by sc13967 on 09/05/2017.
 */
public class TextureCalculator {
    private HashMap<Integer,Double> matrix = new HashMap<>();
    private int nLevels = 0;
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
        // Setting the local values of xOffs and yOffs
        this.xOffs = xOffs;
        this.yOffs = yOffs;
        this.zOffs = zOffs;

        // Initialising new HashMap (acting as a sparse matrix) to store the co-occurance matrix
        matrix = new HashMap<>();

//        // Indexer to get index for addressing HashMap
//        nLevels = (int) Math.pow(2,image.getBitDepth());
//        Indexer indexer = new Indexer(nLevels,nLevels);
//
//        // Running through all specified positions,
//        for (int[] pos:positions) {
//            // Getting current pixel value
//            image.setPosition(1,pos[2],1);
//            int v1 = image.getProcessor().getPixel(pos[0],pos[1]);
//
//            // Getting tested pixel value
//            image.setPosition(1,pos[2]+zOffs,1);
//            int v2 = image.getProcessor().getPixel(pos[0]+xOffs,pos[1]+yOffs);
//
//            // Storing in the HashMap
//            int index1 = indexer.getIndex(new int[]{v1, v2});
//            matrix.computeIfAbsent(index1,k -> matrix.put(index1,0d));
//            matrix.put(index1,matrix.get(index1)+1);
//
//        }
//
//      // Applying normalisation
//        int nPixels = positions.size();
//        matrix.replaceAll((k,v) -> v/nPixels);
        nLevels = 4;
        Indexer indexer = new Indexer(nLevels,nLevels);

        matrix.put(indexer.getIndex(new int[]{0,0}),0.1818);
        matrix.put(indexer.getIndex(new int[]{0,1}),0.0727);
        matrix.put(indexer.getIndex(new int[]{0,2}),0.1091);
        matrix.put(indexer.getIndex(new int[]{0,3}),0d);

        matrix.put(indexer.getIndex(new int[]{1,0}),0.1636);
        matrix.put(indexer.getIndex(new int[]{1,1}),0.0727);
        matrix.put(indexer.getIndex(new int[]{1,2}),0.0364);
        matrix.put(indexer.getIndex(new int[]{1,3}),0.0182);

        matrix.put(indexer.getIndex(new int[]{2,0}),0.1091);
        matrix.put(indexer.getIndex(new int[]{2,1}),0.0909);
        matrix.put(indexer.getIndex(new int[]{2,2}),0.0545);
        matrix.put(indexer.getIndex(new int[]{2,3}),0d);

        matrix.put(indexer.getIndex(new int[]{3,0}),0.0545);
        matrix.put(indexer.getIndex(new int[]{3,1}),0.0364);
        matrix.put(indexer.getIndex(new int[]{3,2}),0d);
        matrix.put(indexer.getIndex(new int[]{3,3}),0d);

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

        Indexer indexer = new Indexer(nLevels,nLevels);
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

//        double[] sumx = new double[nLevels];
//        double[] sumy = new double[nLevels];

//         Getting partial probability density functions
        CumStat px = new CumStat(1);
        CumStat py = new CumStat(1);

        Indexer indexer = new Indexer(nLevels,nLevels);
        for (Integer index:matrix.keySet()) {
            int[] pos = indexer.getCoord(index);

            px.addMeasure((1+pos[0])*matrix.get(index));
            py.addMeasure((1+pos[1])*matrix.get(index));

//            sumx[pos[0]] = sumx[pos[0]] + matrix.get(index);
//            sumy[pos[1]] = sumy[pos[1]] + matrix.get(index);

        }

        // Calculating the mean and standard deviations for the partial probability density functions
        double xMean = px.getMean()[0];
        double yMean = py.getMean()[0];

        double xStd = px.getStd(CumStat.SAMPLE)[0];
        double yStd = py.getStd(CumStat.SAMPLE)[0];
//
//        double xMean = 0;
//        double yMean = 0;
//
//        double xStd = 0;
//        double yStd = 0;

//        for (int i=0;i<sumx.length;i++) {
//            xMean = xMean + i*sumx[i];
//            yMean = yMean + i*sumx[i];
//
//            xStd = xStd + (i-xMean)*(i-xMean)*
//
//        }

        System.out.println(xMean+"_"+xStd+"_"+yMean+"_"+yStd);

        // Calculating the correlation
        for (Integer index:matrix.keySet()) {
            int[] pos = indexer.getCoord(index);

            correlation = correlation + (pos[0]-xMean)*(pos[1]-yMean)*matrix.get(index);

        }

        correlation = correlation/(xStd*yStd);

        return correlation;

    }


    // GETTERS

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
