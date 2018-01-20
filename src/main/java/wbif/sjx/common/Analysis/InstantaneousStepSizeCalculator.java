package wbif.sjx.common.Analysis;

import java.util.TreeMap;

/**
 * Created by steph on 15/04/2017.
 */
public class InstantaneousStepSizeCalculator implements SpatialCalculator {
    public TreeMap<Integer,Double> calculate(int[] f, double[] x, double[] y, double[] z) {
        TreeMap<Integer,Double> steps = new TreeMap<>();

        steps.put(f[0],0d);
        for (int i = 1; i < x.length; i++) {
            double dx = x[i] - x[i - 1];
            double dy = y[i] - y[i - 1];
            double dz = z[i] - z[i - 1];

            double currentStep = Math.sqrt(dx * dx + dy * dy + dz * dz);
            steps.put(f[i],currentStep);
        }

        return steps;
    }
}
