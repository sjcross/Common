package wbif.sjx.common.Analysis;

import java.util.TreeMap;

/**
 * Created by steph on 15/04/2017.
 */
public class InstantaneousVelocityCalculator {
    public static double[] calculate(int[] f, double[] x, double[] y, double[] z) {
        double[] velocity = new double[x.length];
        velocity[0] = 0;

        for (int i = 1; i < x.length; i++) {
            double dx = x[i] - x[i - 1];
            double dy = y[i] - y[i - 1];
            double dz = z[i] - z[i - 1];

            velocity[i] = Math.sqrt(dx * dx + dy * dy + dz * dz) / (f[i] - f[i - 1]);

        }

        return velocity;

    }

    public static TreeMap<Integer,Double> calculateToTree(int[] f, double[] x, double[] y, double[] z) {
        TreeMap<Integer,Double> velocity = new TreeMap<>();

        for (int i = 1; i < x.length; i++) {
            double dx = x[i] - x[i - 1];
            double dy = y[i] - y[i - 1];
            double dz = z[i] - z[i - 1];
            double instVelocity = Math.sqrt(dx * dx + dy * dy + dz * dz) / (f[i] - f[i - 1]);

            velocity.put(f[i],instVelocity);

        }

        return velocity;

    }
}
