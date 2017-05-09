package wbif.sjx.common.Analysis;

import ij.ImagePlus;
import wbif.sjx.common.MathFunc.Indexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by sc13967 on 09/05/2017.
 */
public class TextureCalculator {
    private HashMap<Integer,AtomicInteger> matrix = new HashMap<>();
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
        HashMap<Integer,AtomicInteger> matrix = new HashMap<>();

        // Indexer to get index for addressing HashMap
        Indexer indexer = new Indexer(256,256);

        // Running through all specified positions,
        for (int[] pos:positions) {
            // Getting current pixel value
            image.setPosition(1,pos[2],1);
            int v1 = image.getProcessor().getPixel(pos[0],pos[1]);

            // Getting tested pixel value
            image.setPosition(1,pos[2]+zOffs,1);
            int v2 = image.getProcessor().getPixel(pos[0]+xOffs,pos[1]+yOffs);

            // Storing in the HashMap
            int index = indexer.getIndex(new int[]{v1,v2});
            matrix.get(index).getAndIncrement();

        }
    }

    /**
     * Calculates the co-occurance matrix on the entire image using the specified offset
     * @param image
     * @param xOffs
     * @param yOffs
     * @param zOffs
     */
    public void calculate(ImagePlus image, int xOffs, int yOffs, int zOffs) {
        // Setting the local values of xOffs and yOffs
        this.xOffs = xOffs;
        this.yOffs = yOffs;
        this.zOffs = zOffs;

        // Initialising new HashMap (acting as a sparse matrix) to store the co-occurance matrix
        HashMap<Integer, AtomicInteger> matrix = new HashMap<>();

        // Indexer to get index for addressing HashMap
        Indexer indexer = new Indexer(256, 256);

        // Running through all specified positions,
        for (int z = 0; z < image.getNSlices(); z++) {
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {

                    // Getting current pixel value
                    image.setPosition(1, z, 1);
                    int v1 = image.getProcessor().getPixel(x, y);

                    // Getting tested pixel value
                    image.setPosition(1, z + zOffs, 1);
                    int v2 = image.getProcessor().getPixel(x + xOffs, y + yOffs);

                    // Storing in the HashMap
                    int index = indexer.getIndex(new int[]{v1, v2});
                    matrix.get(index).getAndIncrement();

                }
            }
        }
    }

    /**
     * Calculates the angular second moment
     * @return
     */
    public int getASM() {
        int ASM = 0;

        for (AtomicInteger val:matrix.values()) {
            ASM =+ val.get()*val.get();

        }

        return ASM;

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
