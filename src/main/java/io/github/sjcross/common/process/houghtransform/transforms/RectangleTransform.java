package io.github.sjcross.common.process.houghtransform.transforms;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import io.github.sjcross.common.object.voxels.BresenhamRectangle;
import io.github.sjcross.common.process.houghtransform.accumulators.RectangleAccumulator;

/**
 * Created by sc13967 on 12/01/2018.
 */
public class RectangleTransform extends GenericTransform {
    private int[][] parameterRanges;

    public static void main(String[] args) {
        new ImageJ();
        ImagePlus ipl = IJ.openImage("C:/Users/steph/Desktop/TEST_HoughRectangle.tif");
        ImageProcessor ipr = ipl.getProcessor();
        int[][] paramRanges = new int[][] { { 0, ipr.getWidth() - 1 }, { 0, ipr.getHeight() - 1 },
                { 68,72 }, { 130,150 }, {152,157} };
        double sz = 1;
        for (int i=0;i<paramRanges.length;i++) {
            sz = sz*((paramRanges[i][1]-paramRanges[i][0]+1)*4);
        }
        System.out.println(sz/1048576);
        RectangleTransform transform = new RectangleTransform(ipr, paramRanges);
        transform.setnThreads(4);
        transform.run();
        System.out.println("2");
        ArrayList<double[]> objects = transform.getObjects(0, 0);
        System.out.println("3");
        transform.addDetectedObjectsOverlay(ipl, objects);
        System.out.println("4");
        ipl.show();
        System.out.println("1");
        IJ.runMacro("waitForUser");
        // transform.getAccumulatorAsImage().show();
    }

    public RectangleTransform(ImageProcessor ipr, int[][] parameterRanges) {
        super(ipr);

        this.parameterRanges = parameterRanges;
        this.accumulator = new RectangleAccumulator(parameterRanges);

    }

    @Override
    public void run() {
        // Creating local variables for all parameter controls
        int minX = parameterRanges[0][0];
        int maxX = parameterRanges[0][1];
        int minY = parameterRanges[1][0];
        int maxY = parameterRanges[1][1];
        int minWidth = parameterRanges[2][0];
        int maxWidth = parameterRanges[2][1];
        int minLength = parameterRanges[3][0];
        int maxLength = parameterRanges[3][1];
        int minTheta = parameterRanges[4][0];
        int maxTheta = parameterRanges[4][1];

        // Setting up the threading system
        ThreadPoolExecutor pool = new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());

        // Iterating over all widths, heights and orientations
        int nWidth = maxWidth - minWidth + 1;
        int nLength = maxLength - minLength + 1;
        int nTheta = maxTheta - minTheta + 1;
        for (int iWidth = 0; iWidth < nWidth; iWidth++) {            
            for (int iLength = 0; iLength < nLength; iLength++) {
                for (int iTheta = 0; iTheta < nTheta; iTheta++) {
                    int finalIWidth = iWidth;
                    int finalILength = iLength;
                    int finalITheta = iTheta;

                    Runnable task = () -> {
                        // Getting the current values
                        int width = minWidth + finalIWidth;
                        int length = minLength + finalILength;
                        int theta = minTheta + finalITheta;

                        // Generating coordinates for the points on the rectangle
                        int[][] rect = BresenhamRectangle.getRectangle(width,length,theta);

                        // Iterating over X and Y
                        int nX = maxX - minX + 1;
                        int nY = maxY - minY + 1;
                        for (int iX = 0; iX < nX; iX++) {
                            for (int iY = 0; iY < nY; iY++) {
                                // Getting current XY values
                                int X = minX + iX;
                                int Y = minY + iY;

                                double value = pixels.getPixelValue(new int[] { X, Y });
                                accumulator.addPoints(new int[] { X, Y, width, length, theta }, value, rect);

                            }
                        }
                        System.out.println(finalIWidth+"_"+finalILength+"_"+finalITheta);
                    };
                    pool.submit(task);
                }
            }
        }

        pool.shutdown();
        try {
            pool.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS); // i.e. never terminate early
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
