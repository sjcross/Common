package wbif.sjx.common.Analysis;

/**
 * Created by steph on 15/04/2017.
 */
public class InstantaneousVelocityCalculator {
    public static double[] calculate(int[] f, double[] x, double[] y, double[] z) {
        double[] velocity = new double[x.length-1];

        for (int i = 1; i < x.length; i++) {
            double dx = x[i] - x[i - 1];
            double dy = y[i] - y[i - 1];
            double dz = z[i] - z[i - 1];

            velocity[i-1] = Math.sqrt(dx * dx + dy * dy + dz * dz) / (f[i] - f[i - 1]);

        }

        return velocity;

    }
}
