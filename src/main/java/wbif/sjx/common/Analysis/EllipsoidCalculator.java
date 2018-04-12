package wbif.sjx.common.Analysis;

import org.bonej.geometry.Ellipsoid;
import wbif.sjx.common.Object.Volume;

import static org.bonej.geometry.FitEllipsoid.inertia;

public class EllipsoidCalculator {
    Ellipsoid ell;

    public EllipsoidCalculator(Volume volume) {
        //Fitting an ellipsoid using method from BoneJ

        double[] x = volume.getX(true);
        double[] y = volume.getY(true);
        double[] z = volume.getZ(true,true);
        double[][] coords = new double[x.length][3];

        for (int i=0;i<x.length;i++) {
            coords[i][0] = x[i];
            coords[i][1] = y[i];
            coords[i][2] = z[i];
        }

        ell = inertia(coords);

    }

    public double[] getEllipsoidCentre() {
        return ell.getCentre();

    }

    public double[] getEllipsoidRadii() {
        return ell.getRadii();

    }

    public double[][] getEllipsoidRotationMatrix() {
        return ell.getRotation();

    }

    public double[] getEllipsoidOrientationRads() {
        double[][] rot = ell.getRotation();
        double[] orien = new double[2];

        orien[0] = -Math.atan(rot[1][0]/rot[0][0]); //Orientation relative to x axis
        double xy = Math.sqrt(Math.pow(rot[0][0],2)+Math.pow(rot[1][0],2));
        orien[1] = Math.atan(rot[2][0]/xy); //Orientation relative to xy plane

        if (rot[0][0] <= 0) {
            orien[1] = -orien[1];
        }

        return orien;

    }
}
