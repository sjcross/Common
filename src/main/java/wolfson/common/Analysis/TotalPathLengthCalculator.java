package wolfson.common.Analysis;

/**
 * Created by steph on 15/04/2017.
 */
public class TotalPathLengthCalculator {
    public static double[] calculate(double[] x, double[] y, double[] z) {
        double[] steps = StepSizeCalculator.calculator(x,y,z);
        double[] dist = new double[x.length];

        dist[0] = 0;
        for (int i=1;i<dist.length;i++) {
            dist[i] = dist[i-1]+steps[i-1];
        }

        return dist;

    }
}
