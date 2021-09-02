package io.github.sjcross.common.process.houghtransform.transforms;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.ImageStack;
import io.github.sjcross.common.object.voxels.SphereShell;
import io.github.sjcross.common.object.voxels.SphereShell.Connectivity;
import io.github.sjcross.common.process.houghtransform.accumulators.SphereAccumulator;

/**
 * Created by sc13967 on 12/01/2018.
 */
public class SphereHoughTransform extends GenericHoughTransform {
    private int[][] parameterRanges;

    public static void main(String[] args) {
        new ImageJ();
        ImagePlus ipl = IJ.openImage("C:/Users/steph/Desktop/SphereBinary.tif");
        ImageStack ist = ipl.getStack();
        int[][] paramRanges = new int[][] { { 0, ist.getWidth() - 1 }, { 0, ist.getHeight() - 1 },
                { 0, ipl.getNSlices() - 1 }, { 15, 25 } };
        SphereHoughTransform sht = new SphereHoughTransform(ist, paramRanges);
        sht.run();
        sht.getAccumulatorAsImage().show();
    }

    public SphereHoughTransform(ImageStack ist, int[][] parameterRanges) {
        super(ist);

        this.parameterRanges = parameterRanges;
        this.accumulator = new SphereAccumulator(parameterRanges);

    }

    @Override
    public void run() {
        // Creating local variables for all parameter controls
        int minX = parameterRanges[0][0];
        int maxX = parameterRanges[0][1];
        int minY = parameterRanges[1][0];
        int maxY = parameterRanges[1][1];
        int minZ = parameterRanges[2][0];
        int maxZ = parameterRanges[2][1];
        int minR = parameterRanges[3][0];
        int maxR = parameterRanges[3][1];

        // Setting up the threading system
        ThreadPoolExecutor pool = new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());

        // Iterating over all radii
        int nR = maxR - minR + 1;
        for (int iR = 0; iR < nR; iR++) {
            int finalIR = iR;
            Runnable task = () -> {
                // Getting the current radius value
                int R = minR + finalIR;

                // // Generating coordinates for the points on the midpoint circle
                SphereShell sphereShell = new SphereShell(R,Connectivity.SIX);
                int[][] sph = sphereShell.getSphere();

                // Iterating over X and Y
                int nX = maxX - minX + 1;
                int nY = maxY - minY + 1;
                int nZ = maxZ - minZ + 1;
                for (int iX = 0; iX < nX; iX++) {
                    for (int iY = 0; iY < nY; iY++) {
                        for (int iZ = 0; iZ < nZ; iZ++) {
                            // Getting current XY values
                            int X = minX + iX;
                            int Y = minY + iY;
                            int Z = minZ + iZ;

                            double value = pixels.getPixelValue(new int[] { X, Y, Z });
                            accumulator.addPoints(new int[] { X, Y, Z, R }, value, sph);

                        }
                    }
                }
            };
            pool.submit(task);
        }

        pool.shutdown();
        try {
            pool.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS); // i.e. never terminate early
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
