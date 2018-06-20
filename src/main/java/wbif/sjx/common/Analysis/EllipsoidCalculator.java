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

    /**
     * This constructor is package-private.  As such, it's intended for testing only.
     * @param ell
     * @param volume
     */
    EllipsoidCalculator(Ellipsoid ell, Volume volume) {
        this.ell = ell;
        this.volume = volume;
    }

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

        Object[] yury = yuryPetrov(coords);
        double[] centre = (double[]) yury[0];
        double[] radii = (double[]) yury[1];
        double[][] eigenVectors = (double[][]) yury[2];

        ell = new Ellipsoid(radii[0],radii[1],radii[2],centre[0],centre[1],centre[2],eigenVectors);

    }

    public double[] getCentroid() {
        return ell.getCentre();

    }

    public double[] getRadii() {
        return ell.getRadii();

    }

    public double[][] getRotationMatrix() {
        return ell.getRotation();

    }

    public double[] getOrientationRads() {
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

    /**
     * Gives an approximation of the surface area, which has a relative error of 1.061% (Knud Thomsen's formula)
     * @return
     */
    public double getSurfaceArea() {
        double p = 1.6075;

        double[] r = getRadii();

        double t1 = Math.pow(r[0],p)*Math.pow(r[1],p);
        double t2 = Math.pow(r[0],p)*Math.pow(r[2],p);
        double t3 = Math.pow(r[1],p)*Math.pow(r[2],p);

        return 4*Math.PI*Math.pow(((t1+t2+t3)/3d),1d/p);

    }

    public double getVolume() {
        double[] r = getRadii();
        return (4d/3d)*Math.PI*r[0]*r[1]*r[2];

    }

    public double getSphericity() {
        double SA = getSurfaceArea();
        double V = getVolume();

        double t1 = Math.pow(Math.PI,1d/3d);
        double t2 = Math.pow((6*V),2d/3d);

        return (t1*t2)/SA;

    }

    public double[] getAspectRatios() {
        double[] AR = new double[3];
        double[] r = getRadii();

        AR[0] = r[1]/r[0];
        AR[1] = r[2]/r[0];
        AR[2] = r[2]/r[1];

        return AR;

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
