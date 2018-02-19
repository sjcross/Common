package wbif.sjx.common.Analysis.SpatialCalculators;

import java.util.TreeMap;

/**
 * Created by steph on 15/04/2017.
 */
public class CumulativePathLengthCalculator implements SpatialCalculator {
    public TreeMap<Integer,Double> calculate(int[] f, double[] x, double[] y, double[] z) {
        TreeMap<Integer,Double> steps = new InstantaneousStepSizeCalculator().calculate(f,x,y,z);
        TreeMap<Integer,Double> dist = new TreeMap<>();

        dist.put(f[0],0d);
        for (int i=1;i<x.length;i++) {
            double previousValue = dist.get(f[i-1])+steps.get(f[i]);
            dist.put(f[i],previousValue);
        }

        return dist;

    }
}
