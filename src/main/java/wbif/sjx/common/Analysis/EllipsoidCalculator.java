package wbif.sjx.common.Analysis;

import org.apache.commons.math3.geometry.euclidean.threed.PolyhedronsSet;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.bonej.geometry.Ellipsoid;
import wbif.sjx.common.Object.Volume;

import java.util.Arrays;

import static org.bonej.geometry.FitEllipsoid.inertia;
import static org.bonej.geometry.FitEllipsoid.yuryPetrov;

public class EllipsoidCalculator {
    private final Volume volume;
    private final Ellipsoid ell;

    public EllipsoidCalculator(Volume volume) {
        this.volume = volume;

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

//        ell = inertia(coords);
        Object[] yuri = yuryPetrov(coords);
        double[] centre = (double[]) yuri[0];
        double[] radii = (double[]) yuri[1];
        double[][] eigenVectors = (double[][]) yuri[2];

        ell = new Ellipsoid(radii[0],radii[1],radii[2],centre[0],centre[1],centre[2],eigenVectors);

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

    public Volume getContainedPoints() {
        double dppXY = volume.getDistPerPxXY();
        double dppZ = volume.getDistPerPxZ();
        double cal = dppXY/dppZ;
        String units = volume.getCalibratedUnits();
        boolean is2D = volume.is2D();

        Volume insideEllipsoid = new Volume(dppXY,dppZ,units,is2D);

        // Testing which points are within the convex hull
        double[] xRange = ell.getXMinAndMax();
        double[] yRange = ell.getYMinAndMax();
        double[] zRange = ell.getZMinAndMax();

        for (int x=(int) xRange[0];x<=xRange[1];x++) {
            for (int y=(int) yRange[0];y<=yRange[1];y++) {
                for (int z=(int) zRange[0];z<=zRange[1];z++) {
                    if (ell.contains(x,y,z/cal)) {
                        insideEllipsoid.addCoord(x, y, z);
                    }
                }
            }
        }

        return insideEllipsoid;

    }
}
