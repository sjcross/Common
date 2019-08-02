package wbif.sjx.common.Process.HoughTransform.Transforms;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import wbif.sjx.common.MathFunc.MidpointCircle;
import wbif.sjx.common.Process.HoughTransform.Accumulators.CircleAccumulator;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by sc13967 on 12/01/2018.
 */
public class CircleHoughTransform extends GenericHoughTransform {
    private int[][] parameterRanges;

    public CircleHoughTransform(ImageProcessor ipr, int[][] parameterRanges) {
        super(ipr);

        this.parameterRanges = parameterRanges;
        this.accumulator = new CircleAccumulator(parameterRanges);

    }

    @Override
    public void run() {
        // Creating local variables for all parameter controls
        int minX = parameterRanges[0][0];
        int maxX = parameterRanges[0][1];
        int minY = parameterRanges[1][0];
        int maxY = parameterRanges[1][1];
        int minR = parameterRanges[2][0];
        int maxR = parameterRanges[2][1];

        // Setting up the threading system
        ThreadPoolExecutor pool = new ThreadPoolExecutor(nThreads,nThreads,0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>());

        // Iterating over all radii
        int nR = maxR-minR+1;
        for (int iR=0;iR<nR;iR++) {
            int finalIR = iR;
            Runnable task = () -> {
                // Getting the current radius value
                int R = minR + finalIR;

                // Generating coordinates for the points on the midpoint circle
                MidpointCircle midpointCircle = new MidpointCircle(R);
                int[] xCirc = midpointCircle.getXCircle();
                int[] yCirc = midpointCircle.getYCircle();

                // Iterating over X and Y
                int nX = maxX - minX + 1;
                int nY = maxY - minY + 1;
                for (int iX = 0; iX < nX; iX++) {
                    for (int iY = 0; iY < nY; iY++) {
                        // Getting current XY values
                        int X = minX + iX;
                        int Y = minY + iY;

                        double value = pixels.getPixelValue(new int[]{X, Y});
                        accumulator.addPoints(new int[]{X, Y, R}, value, xCirc, yCirc);

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
