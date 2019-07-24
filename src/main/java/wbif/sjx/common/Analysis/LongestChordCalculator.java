package wbif.sjx.common.Analysis;

import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import wbif.sjx.common.MathFunc.CumStat;
import wbif.sjx.common.MathFunc.GeneralOps;
import wbif.sjx.common.Object.Volume2.PointVolume;
import wbif.sjx.common.Object.Volume2.Volume2;

/**
 * Created by sc13967 on 20/06/2018.
 */
public class LongestChordCalculator {
    private double tolerance = 1E-10;
    private final Volume2 volume;

    private final double[][] LC; //Longest chord

    public LongestChordCalculator(Volume2 volume) {
        this.volume = volume;

        LC = calculateLC();

    }

    double[][] calculateLC() {
        //Reference for use as orientation descriptor: "Computer-Assisted Microscopy: The Measurement and Analysis of
        //Images", John C. Russ, Springer, 6 Dec 2012

        // Iterating over all point combinations on the Volume surface
        double[] x = volume.getSurfaceX(true);
        double[] y = volume.getSurfaceY(true);
        double[] z = volume.getSurfaceZ(true,false);

        double[][] lc = new double[2][3];

        double len = 0;
        for (int i = 0; i < x.length; i++) {
            for (int j = i + 1; j < x.length; j++) {
                double[] a = {x[i], y[i], volume.getXYScaledZ(z[i])};
                double[] b = {x[j], y[j], volume.getXYScaledZ(z[j])};

                double pp = GeneralOps.ppdist(a, b);

                if (pp > len) {
                    len = pp;
                    lc[0][0] = x[i];
                    lc[0][1] = y[i];
                    lc[0][2] = z[i];
                    lc[1][0] = x[j];
                    lc[1][1] = y[j];
                    lc[1][2] = z[j];
                }
            }
        }

        return lc;

    }

    public CumStat calculateAverageDistanceFromLC() {
        CumStat cumStat = new CumStat();

        // Creating a vector between the two end points of the longest chord
        Vector3D v1 = new Vector3D(LC[0][0],LC[0][1],volume.getXYScaledZ(LC[0][2]));
        Vector3D v2 = new Vector3D(LC[1][0],LC[1][1],volume.getXYScaledZ(LC[1][2]));
        Line line = new Line(v1,v2,tolerance);

        // Iterating over all points on the surface, calculating the closest distance to the longest chord
        double[] x = volume.getSurfaceX(true);
        double[] y = volume.getSurfaceY(true);
        double[] z = volume.getSurfaceZ(true,false);

        for (int i=0;i<x.length;i++) {
            Vector3D p1 = new Vector3D(x[i],y[i],volume.getXYScaledZ(z[i]));
            cumStat.addMeasure(line.distance(p1));
        }

        return cumStat;

    }

    public double[][] getLC() {
        return LC;
    }

    public double getLCLength() {
        double[] a = {LC[0][0], LC[0][1], volume.getXYScaledZ(LC[0][2])};
        double[] b = {LC[1][0], LC[1][1], volume.getXYScaledZ(LC[1][2])};

        return GeneralOps.ppdist(a, b);

    }
}
