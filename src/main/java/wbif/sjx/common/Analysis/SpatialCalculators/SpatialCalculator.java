package wbif.sjx.common.Analysis.SpatialCalculators;

import java.util.TreeMap;

/**
 * Created by sc13967 on 20/01/2018.
 */
public interface SpatialCalculator {
    public TreeMap<Integer,Double> calculate(int[] f, double[] x, double[] y, double[] z);
}
