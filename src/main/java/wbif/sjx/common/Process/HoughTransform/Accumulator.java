package wbif.sjx.common.Process.HoughTransform;

import wbif.sjx.common.MathFunc.Indexer;

/**
 * Created by sc13967 on 12/01/2018.
 */
public class Accumulator {
    private Indexer indexer;
    private double[] accumulator;

    /**
     * Constructor for Accumulator object.
     * @param dimensions Integer array containing the dimensions over which the accumulator exists.
     */
    public Accumulator(int[] dimensions) {
        indexer = new Indexer(dimensions);

        // Creating the accumulator to occupy the whole space
        int size = 1;
        for (int dimension:dimensions) size *= dimension;
        accumulator = new double[size];

    }
}
