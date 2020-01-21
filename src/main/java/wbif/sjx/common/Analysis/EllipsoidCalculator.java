//package wbif.sjx.common.Analysis;
//
//import ij.IJ;
//import ij.ImageJ;
//import ij.ImageStack;
//import org.bonej.geometry.Ellipsoid;
//import wbif.sjx.common.Exceptions.IntegerOverflowException;
//import wbif.sjx.common.Object.Point;
//import wbif.sjx.common.Object.Volume.PointOutOfRangeException;
//import wbif.sjx.common.Object.Volume.Volume;
//
//import static org.bonej.geometry.FitEllipsoid.yuryPetrov;
//
//public class EllipsoidCalculator {
//    private final Volume volume;
//    private final Ellipsoid ell;
//
//
//    /**
//     * This constructor is package-private.  As such, it's intended for testing only.
//     * @param ell
//     * @param volume
//     */
//    EllipsoidCalculator(Ellipsoid ell, Volume volume) {
//        this.ell = ell;
//        this.volume = volume;
//    }
//
//    public EllipsoidCalculator(Volume volume, double maxAxisLength) {
//        this.volume = volume;
//
//        //Fitting an ellipsoid using method from BoneJ
//        int i = 0;
//        double[][] coords = new double[volume.size()][3];
//        for (Point<Integer> point:volume.getCoordinateSet()) {
//            coords[i][0] = point.getX();
//            coords[i][1] = point.getY();
//            coords[i][2] = volume.getXYScaledZ(point);
//            i++;
//        }
//
//        Object[] yury = yuryPetrov(coords);
//        double[] centre = (double[]) yury[0];
//        double[] radii = (double[]) yury[1];
//        double[][] eigenVectors = (double[][]) yury[2];
//
//        if (radii[0] > maxAxisLength || radii[1] > maxAxisLength || radii[2] > maxAxisLength) {
//            ell = null;
//        } else {
//            ell = new Ellipsoid(radii[0],radii[1],radii[2],centre[0],centre[1],centre[2],eigenVectors);
//        }
//    }
//
//    /**
//     * Adds a proportional number of points to each pixel depending on its intensity
//     * @param volume
//     * @param imageStack
//     */
//    public EllipsoidCalculator(Volume volume, double maxAxisLength, ImageStack imageStack) {
//        this.volume = volume;
//
//        // Determining the number of coordinates to add
//        int count = 0;
//        for (Point<Integer> point:volume.getCoordinateSet()) {
//            count = count + (int) imageStack.getVoxel(point.getX(),point.getY(),point.getZ());
//        }
//
//        double[][] coords = new double[count][3];
//        int i = 0;
//        for (Point<Integer> point:volume.getCoordinateSet()) {
//            int val = (int) imageStack.getVoxel(point.getX(),point.getY(),point.getZ());
//
//            for (int j=0; j<val; j++) {
//                coords[count][0] = point.getX();
//                coords[count][1] = point.getY();
//                coords[count++][2] = volume.getXYScaledZ(point);
//            }
//        }
//
//        Object[] yury = yuryPetrov(coords);
//        double[] centre = (double[]) yury[0];
//        double[] radii = (double[]) yury[1];
//        double[][] eigenVectors = (double[][]) yury[2];
//
//        if (radii[0] > maxAxisLength || radii[1] > maxAxisLength || radii[2] > maxAxisLength) {
//            ell = null;
//        } else {
//            ell = new Ellipsoid(radii[0],radii[1],radii[2],centre[0],centre[1],centre[2],eigenVectors);
//        }
//    }
//
//    public double[] getCentroid() {
//        if (ell == null) return null;
//        return ell.getCentre();
//
//    }
//
//    public double[] getRadii() {
//        if (ell == null) return null;
//        return ell.getRadii();
//
//    }
//
//    public double[][] getRotationMatrix() {
//        if (ell == null) return null;
//        return ell.getRotation();
//
//    }
//
//    public double[] getOrientationRads() {
//        if (ell == null) return null;
//        double[][] rot = ell.getRotation();
//        double[] orien = new double[2];
//
//        orien[0] = -Math.atan(rot[1][0]/rot[0][0]); //Orientation relative to x axis
//        double xy = Math.sqrt(Math.pow(rot[0][0],2)+Math.pow(rot[1][0],2));
//        orien[1] = Math.atan(rot[2][0]/xy); //Orientation relative to xy plane
//
//        if (rot[0][0] <= 0) {
//            orien[1] = -orien[1];
//        }
//
//        return orien;
//
//    }
//
//    /**
//     * Gives an approximation of the surface area, which has a relative error of 1.061% (Knud Thomsen's formula)
//     * @return
//     */
//    public double getSurfaceArea() {
//        if (ell == null) return Double.NaN;
//
//        double p = 1.6075;
//
//        double[] r = getRadii();
//
//        double t1 = Math.pow(r[0],p)*Math.pow(r[1],p);
//        double t2 = Math.pow(r[0],p)*Math.pow(r[2],p);
//        double t3 = Math.pow(r[1],p)*Math.pow(r[2],p);
//
//        return 4*Math.PI*Math.pow(((t1+t2+t3)/3d),1d/p);
//
//    }
//
//    public double getVolume() {
//        if (ell == null) return Double.NaN;
//        double[] r = getRadii();
//        return (4d/3d)*Math.PI*r[0]*r[1]*r[2];
//
//    }
//
//    public double getSphericity() {
//        if (ell == null) return Double.NaN;
//
//        double SA = getSurfaceArea();
//        double V = getVolume();
//
//        double t1 = Math.pow(Math.PI,1d/3d);
//        double t2 = Math.pow((6*V),2d/3d);
//
//        return (t1*t2)/SA;
//
//    }
//
//    public Volume getContainedPoints() throws IntegerOverflowException {
//        if (ell == null) return null;
//
//        double cal = volume.getDppXY()/volume.getDppZ();
//
//        Volume insideEllipsoid = new Volume(volume);
//
//        // Testing which points are within the convex hull
//        double[] xRange = ell.getXMinAndMax();
//        double[] yRange = ell.getYMinAndMax();
//        double[] zRange = ell.getZMinAndMax();
//
//        for (int x=(int) xRange[0];x<=xRange[1];x++) {
//            for (int y=(int) yRange[0];y<=yRange[1];y++) {
//                for (int z=(int) zRange[0];z<=zRange[1];z++) {
//                    if (ell.contains(x,y,z/cal)) {
//                        try {
//                            insideEllipsoid.add(x, y, z);
//                        } catch (PointOutOfRangeException e) {
//                            // If a point is outside the range, we just ignore it
//                        }
//                    }
//                }
//            }
//        }
//
//        return insideEllipsoid;
//
//    }
//
//}
//

package wbif.sjx.common.Analysis;

import ij.IJ;
import ij.ImageJ;
import ij.ImageStack;
import org.bonej.geometry.Ellipsoid;
import wbif.sjx.common.Exceptions.IntegerOverflowException;
import wbif.sjx.common.Object.Volume.PointOutOfRangeException;
import wbif.sjx.common.Object.Volume.Volume;

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

    public EllipsoidCalculator(Volume volume, double maxAxisLength) {
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

        if (radii[0] > maxAxisLength || radii[1] > maxAxisLength || radii[2] > maxAxisLength) {
            ell = null;
        } else {
            ell = new Ellipsoid(radii[0],radii[1],radii[2],centre[0],centre[1],centre[2],eigenVectors);
        }
    }

    /**
     * Adds a proportional number of points to each pixel depending on its intensity
     * @param volume
     * @param imageStack
     */
    public EllipsoidCalculator(Volume volume, double maxAxisLength, ImageStack imageStack) {
        this.volume = volume;

        //Fitting an ellipsoid using method from BoneJ
        double[] x = volume.getX(true);
        double[] y = volume.getY(true);
        double[] z = volume.getZ(true,true);
        double[] zSlice = volume.getZ(true,false);

        // Determining the number of coordinates to add
        int count = 0;
        for (int i=0;i<x.length;i++) {
            count = count + (int) imageStack.getVoxel((int) x[i], (int) y[i], (int) zSlice[i]);
        }

        double[][] coords = new double[count][3];
        count = 0;
        for (int i=0;i<x.length;i++) {
            int val = (int) imageStack.getVoxel((int) x[i], (int) y[i], (int) zSlice[i]);

            for (int j=0; j<val; j++) {
                coords[count][0] = x[i];
                coords[count][1] = y[i];
                coords[count++][2] = z[i];
            }
        }

        Object[] yury = yuryPetrov(coords);
        double[] centre = (double[]) yury[0];
        double[] radii = (double[]) yury[1];
        double[][] eigenVectors = (double[][]) yury[2];

        if (radii[0] > maxAxisLength || radii[1] > maxAxisLength || radii[2] > maxAxisLength) {
            ell = null;
        } else {
            ell = new Ellipsoid(radii[0],radii[1],radii[2],centre[0],centre[1],centre[2],eigenVectors);
        }
    }

    public double[] getCentroid() {
        if (ell == null) return null;
        return ell.getCentre();

    }

    public double[] getRadii() {
        if (ell == null) return null;
        return ell.getRadii();

    }

    public double[][] getRotationMatrix() {
        if (ell == null) return null;
        return ell.getRotation();

    }

    public double[] getOrientationRads() {
        if (ell == null) return null;
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
        if (ell == null) return Double.NaN;

        double p = 1.6075;

        double[] r = getRadii();

        double t1 = Math.pow(r[0],p)*Math.pow(r[1],p);
        double t2 = Math.pow(r[0],p)*Math.pow(r[2],p);
        double t3 = Math.pow(r[1],p)*Math.pow(r[2],p);

        return 4*Math.PI*Math.pow(((t1+t2+t3)/3d),1d/p);

    }

    public double getVolume() {
        if (ell == null) return Double.NaN;
        double[] r = getRadii();
        return (4d/3d)*Math.PI*r[0]*r[1]*r[2];

    }

    public double getSphericity() {
        if (ell == null) return Double.NaN;

        double SA = getSurfaceArea();
        double V = getVolume();

        double t1 = Math.pow(Math.PI,1d/3d);
        double t2 = Math.pow((6*V),2d/3d);

        return (t1*t2)/SA;

    }

    public Volume getContainedPoints() throws IntegerOverflowException {
        if (ell == null) return null;

        double cal = volume.getDppXY()/volume.getDppZ();

        Volume insideEllipsoid = new Volume(volume);

        // Testing which points are within the convex hull
        double[] xRange = ell.getXMinAndMax();
        double[] yRange = ell.getYMinAndMax();
        double[] zRange = ell.getZMinAndMax();

        for (int x=(int) xRange[0];x<=xRange[1];x++) {
            for (int y=(int) yRange[0];y<=yRange[1];y++) {
                for (int z=(int) zRange[0];z<=zRange[1];z++) {
                    if (ell.contains(x,y,z)) {
                        try {
                            insideEllipsoid.add(x, y, (int) Math.round(z*cal));
                        } catch (PointOutOfRangeException e) {
                            // If a point is outside the range, we just ignore it
                        }
                    }
                }
            }
        }

        return insideEllipsoid;

    }

}

