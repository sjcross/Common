package io.github.sjcross.common.process.houghtransform.accumulators;

import java.util.ArrayList;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.OvalRoi;
import ij.gui.Overlay;
import io.github.sjcross.common.object.voxels.MidpointCircle;

/**
 * Created by sc13967 on 13/01/2018.
 */
public class CircleAccumulator extends AbstractAccumulator {
    /**
     * Constructor for Accumulator object.
     *
     * @param parameterRanges 2D Integer array containing the dimensions over which
     *                        the accumulator exists.
     */
    public CircleAccumulator(int[][] parameters) {
        super(parameters);
    }

    @Override
    public void addDetectedObjectsOverlay(ImagePlus ipl, ArrayList<double[]> objects) {
        // Adding overlay showing detected circles
        Overlay overlay = ipl.getOverlay();
        if (overlay == null)
            overlay = new Overlay();

        for (int i = 0; i < objects.size(); i++) {
            double x = objects.get(i)[0];
            double y = objects.get(i)[1];
            double r = objects.get(i)[2];

            OvalRoi ovalRoi = new OvalRoi(x - r, y - r, 2 * r, 2 * r);
            overlay.add(ovalRoi);

        }

        ipl.setOverlay(overlay);

    }

    @Override
    public void addPoints(int[] indices, double value, int[][] points) {
        for (int i = 0; i < points.length; i++) {
            int iX = parameters[0][indices[0]] + points[i][0] - parameters[0][0];
            int iY = parameters[1][indices[1]] + points[i][1] - parameters[1][0];

            int idx = indexer.getIndex(new int[] { iX, iY, indices[2] });

            if (idx == -1)
                continue;

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
        if (maxIdx == -1)
            return objects;

        double maxVal = accumulator[maxIdx];

        // Extracting all points
        while (maxVal >= minScore) {
            addBrightestObject(maxIdx, maxVal, objects, x, y);

            // Updating the brightest point
            maxIdx = getLargestScorePixelIndex();
            if (maxIdx == -1)
                break;

            maxVal = accumulator[maxIdx];

        }

        return objects;

    }

    @Override
    public ArrayList<double[]> getNObjects(int nObjects, int exclusionR) {
        // Creating an ArrayList to store the points
        ArrayList<double[]> objects = new ArrayList<>();

        // Getting relative coordinates for exclusion zone
        MidpointCircle midpointCircle = new MidpointCircle(exclusionR);
        int[] x = midpointCircle.getXCircleFill();
        int[] y = midpointCircle.getYCircleFill();

        // Identifying the brightest point in the accumulator
        int maxIdx = getLargestScorePixelIndex();
        if (maxIdx == -1)
            return objects;

        double maxVal = accumulator[maxIdx];

        // Extracting all points
        for (int count = 0; count < nObjects; count++) {
            // Updating the brightest point
            maxIdx = getLargestScorePixelIndex();

            if (maxIdx == -1)
                break;

            maxVal = accumulator[maxIdx];

            addBrightestObject(maxIdx, maxVal, objects, x, y);

        }

        return objects;

    }

    void addBrightestObject(int maxIdx, double maxVal, ArrayList<double[]> objects, int[] x, int[] y) {
        // Getting parameters for brightest current spot and adding to ArrayList
        int[] currIndices = indexer.getCoord(maxIdx);
        int xx = parameters[0][currIndices[0]];
        int yy = parameters[1][currIndices[1]];
        int rr = parameters[2][currIndices[2]];

        objects.add(new double[] { xx, yy, rr, maxVal });

        // Setting all pixels within exclusionR to zero. This is repeated for all
        // slices.
        for (int iR = 0; iR < indexer.getDim()[2]; iR++) {
            for (int i = 0; i < x.length; i++) {
                int iX = xx + x[i] - parameters[0][0];
                int iY = yy + y[i] - parameters[1][0];
                int idx = indexer.getIndex(new int[] { iX, iY, iR });

                if (idx == -1)
                    continue;

                accumulator[idx] = 0;
            }
        }
    }

    @Override
    public ImagePlus getAccumulatorAsImage() {
        int[] dim = indexer.getDim();
        ImagePlus ipl = IJ.createImage("Circle_Accumulator", dim[0], dim[1], dim[2], 32);

        for (int idx = 0; idx < indexer.getLength(); idx++) {
            int[] coord = indexer.getCoord(idx);

            double value = accumulator[idx];

            ipl.setSlice(coord[2] + 1);
            ipl.getProcessor().putPixelValue(coord[0], coord[1], value);

        }

        return ipl;

    }
}
