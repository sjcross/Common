package wbif.sjx.common.Analysis;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import wbif.sjx.common.MathFunc.CumStat;

/**
 * Created by steph on 15/04/2017.
 */
public class DirectionalPersistenceCalculator {
    public static CumStat calculate(int[] f, double[] x, double[] y, double[] z) {
        int maxDf = f[f.length - 1] - f[0]; // Maximum frame separation
        CumStat cumStat = new CumStat(maxDf);

        for (int df = 0; df < maxDf; df++) {
            for (int i = 0; i < x.length - df - 1; i++) {
                if (f[i + 1] - f[i] == 1 & f[i + df + 1] - f[i + df] == 1 & f[i + df] - f[i] == df) {
                    Vector3D v1 = new Vector3D((x[i] - x[i + 1]), (y[i] - y[i + 1]), (z[i] - z[i + 1]));
                    Vector3D v2 = new Vector3D((x[i + df] - x[i + 1 + df]), (y[i + df] - y[i + 1 + df]), (z[i + df] - z[i + 1 + df]));

                    if (v1.getNorm() != 0 & v2.getNorm() != 0) {
                        cumStat.addSingleMeasure(df, Math.cos(Vector3D.angle(v1, v2)));
                    }
                }
            }
        }

        return cumStat;

    }
}
