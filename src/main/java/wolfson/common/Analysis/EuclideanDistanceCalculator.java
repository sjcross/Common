package wolfson.common.Analysis;

/**
 * Created by steph on 15/04/2017.
 */
public class EuclideanDistanceCalculator {
    public static double[] calculate(double[] x, double[] y, double[] z) {
        double[] dist = new double[x.length];

        for (int i=0;i<dist.length;i++) {
            double dx = x[i]-x[0];
            double dy = y[i]-y[0];
            double dz = z[i]-z[0];

            dist[i] = Math.sqrt(dx*dx + dy*dy + dz*dz);
        }

        return dist;

    }
}
