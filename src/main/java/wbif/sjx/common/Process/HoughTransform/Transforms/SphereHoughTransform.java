package wbif.sjx.common.Process.HoughTransform.Transforms;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ij.process.ImageProcessor;
import wbif.sjx.common.MathFunc.MidpointSphere;
import wbif.sjx.common.Process.HoughTransform.Accumulators.SphereAccumulator;

/**
 * Created by sc13967 on 12/01/2018.
 */
public class SphereHoughTransform extends GenericHoughTransform {
    private int[][] parameterRanges;

    public SphereHoughTransform(ImageProcessor ipr, int[][] parameterRanges) {
        super(ipr);

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
                MidpointSphere midpointSphere = new MidpointSphere(R);
                int[][] sph = midpointSphere.getSphere();

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
