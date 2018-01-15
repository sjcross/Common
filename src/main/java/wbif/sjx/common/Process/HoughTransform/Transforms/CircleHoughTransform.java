package wbif.sjx.common.Process.HoughTransform.Transforms;

import ij.ImagePlus;
import ij.process.ImageProcessor;
import wbif.sjx.common.MathFunc.MidpointCircle;
import wbif.sjx.common.Process.HoughTransform.Accumulator;
import wbif.sjx.common.Process.HoughTransform.CircleAccumulator;

/**
 * Created by sc13967 on 12/01/2018.
 */
public class CircleHoughTransform extends GenericHoughTransform {
    private int[][] parameterRanges;

    public CircleHoughTransform(ImageProcessor ipr) {
        super(ipr);

    }

    public void setAccumulator(int[][] parameterRanges) {
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

        // Iterating over all radii
        int nR = maxR-minR+1;
        for (int iR=0;iR<nR;iR++) {
            // Getting the current radius value
            int R = minR+iR;

            // Generating coordinates for the points on the midpoint circle
            MidpointCircle midpointCircle = new MidpointCircle(R);
            int[] xCirc = midpointCircle.getXCircle();
            int[] yCirc = midpointCircle.getYCircle();

            // Iterating over X and Y
            int nX = maxX-minX+1;
            int nY = maxY-minY+1;
            for (int iX=0;iX<nX;iX++) {
                System.out.println(iX);
                for (int iY=0;iY<nY;iY++) {
                    // Getting current XY values
                    int X = minX+iX;
                    int Y = minY+iY;

                    double value = pixels.getPixelValue(new int[]{X,Y});
                    accumulator.addPoints(new int[]{X,Y,R},value,xCirc,yCirc);

                }
            }
        }
    }

}
