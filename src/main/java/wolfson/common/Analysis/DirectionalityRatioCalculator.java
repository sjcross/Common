package wolfson.common.Analysis;

/**
 * Created by steph on 15/04/2017.
 */
public class DirectionalityRatioCalculator {
    public static double[] calculate(double[] x, double[] y, double[] z) {
        double[] eucl_dist = EuclideanDistanceCalculator.calculate(x,y,z);
        double[] total_len = TotalPathLengthCalculator.calculate(x,y,z);

        double[] dir_persist = new double[eucl_dist.length];
        for (int i=0;i<dir_persist.length;i++) {
            dir_persist[i] = eucl_dist[i]/total_len[i];
        }

        return dir_persist;

    }
}
