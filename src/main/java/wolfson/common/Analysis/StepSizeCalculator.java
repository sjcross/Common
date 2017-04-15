package wolfson.common.Analysis;

/**
 * Created by steph on 15/04/2017.
 */
public class StepSizeCalculator {
    public static double[] calculator(double[] x, double[] y, double[] z) {
        double[] steps = new double[x.length - 1];

        for (int i = 1; i < x.length; i++) {
            double dx = x[i] - x[i - 1];
            double dy = y[i] - y[i - 1];
            double dz = z[i] - z[i - 1];

            steps[i-1] = Math.sqrt(dx * dx + dy * dy + dz * dz);
        }

        return steps;
    }
}
