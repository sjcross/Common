package io.github.sjcross.common.process.houghtransform.accumulators;

import ij.ImagePlus;
import io.github.sjcross.common.mathfunc.Indexer;

import java.util.ArrayList;

/**
 * Created by sc13967 on 12/01/2018.
 */
public abstract class Accumulator {
    protected Indexer indexer;
    protected double[] accumulator;
    protected int[] counts;
    protected int[][] parameterRanges;


    // CONSTRUCTOR

    /**
     * Constructor for Accumulator object.
     * @param parameterRanges 2D Integer array containing the dimensions over which the accumulator exists.
     */
    public Accumulator(int[][] parameterRanges) {
        // Keeping track of the parameter ranges.  These will be needed to access the correct index
        this.parameterRanges = parameterRanges;

        // Calculating the number of elements in each dimension
        int[] dims = new int[parameterRanges.length];
        int i = 0;
        for (int[] range:parameterRanges) {
            int n = range[1]-range[0]+1;
            dims[i++] = n;

        }

        // Initialising the indexer
        indexer = new Indexer(dims);

        // Creating the accumulator to occupy the whole space
        int size = indexer.getLength();
        accumulator = new double[size];
        counts = new int[size];

    }

    public void normaliseScores() {
        for (int i=0;i<counts.length;i++) {
            accumulator[i] = accumulator[i]/counts[i];
        }
    }


    // ABSTRACT METHODS

    public abstract void addDetectedObjectsOverlay(ImagePlus ipl, ArrayList<double[]> objects);

    public abstract void addPoints(int[] parameters, double value, int[][] points);

    public abstract ArrayList<double[]> getObjects(double minScore, int exclusionR);

    public abstract ImagePlus getAccumulatorAsImage();

}
