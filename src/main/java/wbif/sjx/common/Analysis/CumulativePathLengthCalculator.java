package wbif.sjx.common.Analysis;

import java.util.TreeMap;

/**
 * Created by steph on 15/04/2017.
 */
public class CumulativePathLengthCalculator {
    public static double[] calculate(int[] f, double[] x, double[] y, double[] z) {
        TreeMap<Integer,Double> steps = new InstantaneousStepSizeCalculator().calculate(f,x,y,z);
        double[] dist = new double[x.length];

        dist[0] = 0;
        for (int i=1;i<dist.length;i++) {
            dist[i] = dist[i-1]+steps.get(f[i]);
        }

        return dist;

    }
}
