package io.github.sjcross.common.process.houghtransform.transforms;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ij.process.ImageProcessor;
import io.github.sjcross.common.object.voxels.MidpointCircle;
import io.github.sjcross.common.process.CommaSeparatedStringInterpreter;
import io.github.sjcross.common.process.houghtransform.accumulators.CircleAccumulator;

/**
 * Created by sc13967 on 12/01/2018.
 */
public class CircleTransform extends AbstractTransform {
    // public static void main(String[] args) {
    //     new ImageJ();
    //     ImagePlus ipl = IJ.openImage("C:/Users/steph/Desktop/TEST_HoughCircle.tif");
    //     ImageProcessor ipr = ipl.getProcessor();

    //     CircleTransform transform = new CircleTransform(ipr, new String[]{"150-300-5","100-200","60-70-5"});
    //     transform.setnThreads(4);
    //     transform.run();

    //     ArrayList<double[]> objects = transform.getObjects(10000, 100);
    //     transform.addDetectedObjectsOverlay(ipl, objects);

    //     ipl.show();
    //     IJ.runMacro("waitForUser");

    // }

    public CircleTransform(ImageProcessor ipr, String[] parameterRanges) {
        super(ipr);

        String xRange = CommaSeparatedStringInterpreter.removeInterval(parameterRanges[0]);
        String yRange = CommaSeparatedStringInterpreter.removeInterval(parameterRanges[1]);

        int[][] parameters = new int[parameterRanges.length][];
        parameters[0] = CommaSeparatedStringInterpreter.interpretIntegers(xRange, true, ipr.getWidth()-2);
        parameters[1] = CommaSeparatedStringInterpreter.interpretIntegers(yRange, true, ipr.getHeight()-2);
        parameters[2] = CommaSeparatedStringInterpreter.interpretIntegers(parameterRanges[2], true, ipr.getWidth() - 1);
        
        this.accumulator = new CircleAccumulator(parameters);

    }

    @Override
    public void run() {
        int[][] parameters = accumulator.getParameters();

        // Setting up the threading system
        ThreadPoolExecutor pool = new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());

        // Iterating over all radii
        int nX = parameters[0].length;
        int nY = parameters[1].length;
        int nR = parameters[2].length;

        for (int iR = 0; iR < nR; iR++) {
            int finalIR = iR;
            int R = parameters[2][iR];
            Runnable task = () -> {
                // Generating coordinates for the points on the midpoint circle
                MidpointCircle midpointCircle = new MidpointCircle(R);
                int[][] circ = midpointCircle.getCircle();

                // Iterating over X and Y
                for (int iX = 0; iX < nX; iX++) {
                    for (int iY = 0; iY < nY; iY++) {
                        // Getting current XY values
                        int X = parameters[0][iX];
                        int Y = parameters[1][iY];

                        double value = pixels.getPixelValue(new int[] { X, Y });
                        accumulator.addPoints(new int[] { iX, iY, finalIR }, value, circ);

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
