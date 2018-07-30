package wbif.sjx.common.Process.HoughTransform.Accumulators;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.OvalRoi;
import ij.gui.Overlay;
import wbif.sjx.common.MathFunc.GoreaudEdgeCorrection;
import wbif.sjx.common.MathFunc.MidpointCircle;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sc13967 on 13/01/2018.
 */
public class CircleAccumulator extends Accumulator {
    /**
     * Constructor for Accumulator object.
     *
     * @param parameterRanges 2D Integer array containing the dimensions over which the accumulator exists.
     */
    public CircleAccumulator(int[][] parameterRanges) {
        super(parameterRanges);
    }


    @Override
    public void addDetectedObjectsOverlay(ImagePlus ipl, ArrayList<double[]> objects) {
        // Adding overlay showing detected circles
        Overlay overlay = ipl.getOverlay();
        if (overlay == null) overlay = new Overlay();

        for (int i=0;i<objects.size();i++) {
            double x = objects.get(i)[0];
            double y = objects.get(i)[1];
            double r = objects.get(i)[2];

            OvalRoi ovalRoi = new OvalRoi(x-r,y-r,2*r,2*r);
            overlay.add(ovalRoi);

        }

        ipl.setOverlay(overlay);

    }

    @Override
    public void addPoints(int[] parameters, double value, int[] x, int[] y) {
        for (int i = 0; i < x.length; i++) {
            int xx = parameters[0]+x[i]-parameterRanges[0][0];
            int yy = parameters[1]+y[i]-parameterRanges[1][0];
            int rr = parameters[2]-parameterRanges[2][0];

            int idx = indexer.getIndex(new int[]{xx,yy,rr});

            if (idx == -1) continue;

            // Adding the current value and incrementing the count
            accumulator[idx] = accumulator[idx] + value;
            counts[idx]++;

        }
    }

    @Override
    public ArrayList<double[]> getObjects(double minScore, int exclusionR) {
        // Creating an ArrayList to store the points
        ArrayList<double[]> objects = new ArrayList<>();

        // Getting relative coordinates for exclusion zone
        MidpointCircle midpointCircle = new MidpointCircle(exclusionR);
        int[] x = midpointCircle.getXCircleFill();
        int[] y = midpointCircle.getYCircleFill();

        // Identifying the brightest point in the accumulator
        int maxIdx = getLargestScorePixelIndex();
        double maxVal = accumulator[maxIdx];

        // Extracting all points
        while (maxVal >= minScore) {
            // Getting parameters for brightest current spot and adding to ArrayList
            int[] parameters = indexer.getCoord(maxIdx);
            parameters[0] = parameters[0] + parameterRanges[0][0];
            parameters[1] = parameters[1] + parameterRanges[1][0];
            parameters[2] = parameters[2] + parameterRanges[2][0];

            objects.add(new double[]{parameters[0],parameters[1],parameters[2],maxVal});

            // Setting all pixels within exclusionR to zero.  This is repeated for all slices.
            for (int rr = 0; rr< indexer.getDim()[2]; rr++) {
                for (int i = 0; i < x.length; i++) {
                    int xx = parameters[0] + x[i] - parameterRanges[0][0];
                    int yy = parameters[1] + y[i] - parameterRanges[1][0];

                    int idx = indexer.getIndex(new int[]{xx, yy, rr});

                    if (idx == -1) continue;

                    accumulator[idx] = 0;
                }
            }

            // Updating the brightest point
            maxIdx = getLargestScorePixelIndex();

            if (maxIdx == -1) break;

            maxVal = accumulator[maxIdx];

        }

        return objects;

    }

    @Override
    public ImagePlus getAccumulatorAsImage() {
        int[] dim = indexer.getDim();
        ImagePlus ipl = IJ.createImage("Circle_Accumulator",dim[0],dim[1],dim[2],32);

        for (int idx = 0; idx< indexer.getLength(); idx++) {
            int[] coord = indexer.getCoord(idx);

            double value = accumulator[idx];

            ipl.setSlice(coord[2]+1);
            ipl.getProcessor().putPixelValue(coord[0],coord[1],value);

        }

        return ipl;

    }

    /**
     * Returns the Indexer index for the pixel in the Accumulator with the largest score.
     * @return
     */
    public int getLargestScorePixelIndex() {
        double maxVal = Double.MIN_VALUE;
        int maxIdx = -1;

        // Iterating over all pixels in the Accumulator.  Identifying the brightest.
        for (int i=0;i<accumulator.length;i++) {
            if (accumulator[i] > maxVal) {
                maxVal = accumulator[i];
                maxIdx = i;
            }
        }

        return maxIdx;

    }


}
