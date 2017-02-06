package wolfson.common.Object;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import wolfson.common.MathFunc.CumStat;

import java.util.Arrays;

/**
 * Created by sc13967 on 03/02/2017.
 */
public class Track {
    double[] x;
    double[] y;
    double[] z;
    int[] f;


    // CONSTRUCTORS

    public Track(double[] x, double[] y, double[] z, int[] f) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.f = f;

    }

    public Track(double[] x, double[] y, int[] f) {
        this.x = x;
        this.y = y;
        this.z = new double[x.length];
        Arrays.fill(z, 1);
        this.f = f;

    }


    // PUBLIC METHODS

    public CumStat getDirectionalPersistence() {
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

    public CumStat getMSD() {
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

    public double[] getInstantaneousVelocity() {
        double[] velocity = new double[x.length - 1];

        for (int i = 1; i < x.length; i++) {
            double dx = x[i] - x[i - 1];
            double dy = y[i] - y[i - 1];
            double dz = z[i] - z[i - 1];

            velocity[i - 1] = Math.sqrt(dx * dx + dy * dy + dz * dz) / (f[i] - f[i - 1]);
        }

        return velocity;
    }

    public double[] getStepSizes() {
        double[] steps = new double[x.length - 1];

        for (int i = 1; i < x.length; i++) {
            double dx = x[i] - x[i - 1];
            double dy = y[i] - y[i - 1];
            double dz = z[i] - z[i - 1];

            steps[i-1] = Math.sqrt(dx * dx + dy * dy + dz * dz);
        }

        return steps;

    }

    public double getEuclideanDistance() {
        double dx = x[x.length-1]-x[0];
        double dy = y[x.length-1]-y[0];
        double dz = z[x.length-1]-z[0];

        return Math.sqrt(dx * dx + dy * dy + dz * dz);

    }

    public double getTotalPathLength() {
        double[] steps = getStepSizes();

        return new Sum().evaluate(steps);

    }

    public double getDirectionalityRatio() {
        return getEuclideanDistance()/getTotalPathLength();

    }

    /**
     * Returns a double[] containing the Euclidean distance at all time steps
     */
    public double[] getRollingEuclideanDistance() {
        double[] dist = new double[x.length];

        for (int i=0;i<dist.length;i++) {
            double dx = x[i]-x[0];
            double dy = y[i]-y[0];
            double dz = z[i]-z[0];

            dist[i] = Math.sqrt(dx*dx + dy*dy + dz*dz);
        }

        return dist;
    }

    /**
     * Returns a double[] containing the total path length up to each time step
     */
    public double[] getRollingTotalPathLength() {
        double[] steps = getStepSizes();
        double[] dist = new double[x.length];

        dist[0] = 0;
        for (int i=1;i<dist.length;i++) {
            dist[i] = dist[i-1]+steps[i-1];
        }

        return dist;
    }

    /**
     * Returns a double[] containing the directionality ratio at all time steps
     */
    public double[] getRollingDirectionalityRatio() {
        double[] eucl_dist = getRollingEuclideanDistance();
        double[] total_len = getRollingTotalPathLength();

        double[] dir_persist = new double[eucl_dist.length];
        for (int i=0;i<dir_persist.length;i++) {
            dir_persist[i] = eucl_dist[i]/total_len[i];
        }

        return dir_persist;
    }

    public double getDuration() {
        return f[f.length-1];
    }
}