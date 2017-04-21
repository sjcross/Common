package wbif.sjx.common.Analysis;

import wbif.sjx.common.MathFunc.CumStat;

/**
 * Created by steph on 15/04/2017.
 */
public class MSDCalculator {
    public static CumStat calculate(int[] f, double[] x, double[] y, double[] z) {
        CumStat cumStat = new CumStat(f[f.length - 1]);

        for (int i = 0; i <= f[f.length - 1]; i++) { //Incrementing over all time steps
            for (int j = 0; j < x.length; j++) {//Incrementing over all frames with the possibility for that time step
                for (int k = j + 1; k < x.length; k++) {
                    //IJ.log("Testing "+String.valueOf(post[j])+" to "+String.valueOf(post[k]));
                    if (f[k] - f[j] == i) {
                        double dx = x[k] - x[j];
                        double dy = y[k] - y[j];
                        double dz = z[k] - z[j];

                        double val = dx * dx + dy * dy + dz * dz;

                        cumStat.addMeasure(i, val);
                    }
                }
            }
        }

        return cumStat;

    }
}
