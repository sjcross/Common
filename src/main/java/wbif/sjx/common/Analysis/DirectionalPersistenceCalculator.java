//TODO: Once unit test is working, swap Vector3D calculation for angle calculation from SkeletonTools.FitSkeletonBreaks

package wbif.sjx.common.Analysis;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import wbif.sjx.common.MathFunc.CumStat;

/**
 * Calculates the direction autocorrelation.  Tested against results from DiPer.
 */
public class DirectionalPersistenceCalculator {
    public static CumStat calculate(int[] f, double[] x, double[] y, double[] z) {
        int maxDf = f[f.length - 1] - f[0]; // Maximum frame separation
        CumStat cumStat = new CumStat(maxDf);

        for (int i = 0; i < f.length-1; i++) {
            for (int j = i; j < f.length-1; j++) {
                // Vectors must each take a single time step
                if (f[i+1]-f[i] == 1 & f[j+1]-f[j] == 1) {
                    int df = f[j] - f[i];
                    Vector3D v1 = new Vector3D((x[i+1] - x[i]), (y[i+1] - y[i]), (z[i+1] - z[i]));
                    Vector3D v2 = new Vector3D((x[j+1] - x[j]), (y[j+1] - y[j]), (z[j+1] - z[j]));

                    if (v1.getNorm() != 0 & v2.getNorm() != 0) {
                        cumStat.addSingleMeasure(df, Math.cos(Vector3D.angle(v1, v2)));
                    }
                }
            }
        }

        return cumStat;

    }

    public static CumStat calculateContinuous(double[] x, double[] y, double[] z) {
        int[] f = new int[x.length];
        for (int i=0;i<f.length;i++) {
            f[i] = i;
        }

        return calculate(f,x,y,z);

    }
}
