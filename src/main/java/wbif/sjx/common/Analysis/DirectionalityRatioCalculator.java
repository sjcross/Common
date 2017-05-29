package wbif.sjx.common.Analysis;

/**
 * Created by steph on 15/04/2017.
 */
public class DirectionalityRatioCalculator {
    public static double[] calculate(double[] x, double[] y, double[] z) {
        double[] eucl_dist = EuclideanDistanceCalculator.calculate(x,y,z);
        double[] total_len = TotalPathLengthCalculator.calculate(x,y,z);

        double[] dir_persist = new double[eucl_dist.length];
        for (int i=0;i<dir_persist.length;i++) {
            if (total_len[i] == 0) {
                dir_persist[i] = Double.NaN;
                continue;
            }

            if (eucl_dist[i] == 0) {
                dir_persist[i] = 0;
                continue;
            }

            dir_persist[i] = eucl_dist[i]/total_len[i];

        }

        return dir_persist;

    }
}
