// TODO: Change getProjectedArea to use HashSet for coordinate indices

package wbif.sjx.common.Object;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.stat.descriptive.rank.Min;
import wbif.sjx.common.MathFunc.ArrayFunc;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by sc13967 on 28/07/2017.
 */
public class Volume {
    protected final double dppXY; //Calibration in xy (fixed once declared in constructor)
    protected final double dppZ; //Calibration in z (fixed once declared in constructor)
    protected final String calibratedUnits;

    protected ArrayList<Point<Integer>> points = new ArrayList<>() ;

    public ArrayList<Point<Integer>> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point<Integer>> points) {
        this.points = points;
    }

    public Volume(double dppXY, double dppZ, String calibratedUnits) {
        this.dppXY = dppXY;
        this.dppZ = dppZ;
        this.calibratedUnits = calibratedUnits;

    }

    public void addCoord(int xIn, int yIn, int zIn) {
        points.add(new Point<>(xIn,yIn,zIn,0));

    }

    public double getDistPerPxXY() {
        return dppXY;

    }

    public double getDistPerPxZ() {
        return dppZ;

    }

    public String getCalibratedUnits() {
        return calibratedUnits;
    }

    public ArrayList<Integer> getXCoords() {
        return points.stream().map(Point::getX).collect(Collectors.toCollection(ArrayList::new));

    }

    public ArrayList<Integer> getYCoords() {
        return points.stream().map(Point::getY).collect(Collectors.toCollection(ArrayList::new));

    }

    public ArrayList<Integer> getZCoords() {
        return points.stream().map(Point::getZ).collect(Collectors.toCollection(ArrayList::new));

    }

    public double[] getX(boolean pixelDistances) {
        if (pixelDistances)
            return points.stream().map(Point::getX).mapToDouble(Integer::doubleValue).toArray();
        else
            return points.stream().map(Point::getX).mapToDouble(Integer::doubleValue).map(v->v* dppXY).toArray();

    }

    public double[] getY(boolean pixelDistances) {
        if (pixelDistances)
            return points.stream().map(Point::getY).mapToDouble(Integer::doubleValue).toArray();
        else
            return points.stream().map(Point::getY).mapToDouble(Integer::doubleValue).map(v->v* dppXY).toArray();

    }

    /**
     *
     * @param pixelDistances
     * @param matchXY Get Z-coordinates in equivalent pixel distances to XY (e.g. for Z-coordinates at twice the XY
     *                spacing, Z of 1 will be returned as 2).
     * @return
     */
    public double[] getZ(boolean pixelDistances, boolean matchXY) {
        if (pixelDistances)
            if (matchXY)
                return points.stream().map(Point::getZ).mapToDouble(Integer::doubleValue).map(v -> v* dppZ / dppXY).toArray();

            else
                return points.stream().map(Point::getZ).mapToDouble(Integer::doubleValue).toArray();

        else
            return points.stream().map(Point::getZ).mapToDouble(Integer::doubleValue).map(v->v* dppZ).toArray();

    }

    public double getXMean(boolean pixelDistances) {
        return new Mean().evaluate(getX(pixelDistances));

    }

    public double getYMean(boolean pixelDistances) {
        return new Mean().evaluate(getY(pixelDistances));

    }

    public double getZMean(boolean pixelDistances, boolean matchXY) {
        return new Mean().evaluate(getZ(pixelDistances,matchXY));

    }

    public double getHeight(boolean pixelDistances, boolean matchXY) {
        double[] z = getZ(pixelDistances,matchXY);

        double min_z = new Min().evaluate(z);
        double max_z = new Max().evaluate(z);

        return max_z - min_z;

    }

    public double[] getExtents(boolean pixelDistances, boolean matchXY) {
        //Minimum and maximum values for all dimensions [x_min, y_min, z_min; x_max, y_max, z_max]
        double[] extents = new double[6];

        double[] x = getX(pixelDistances);
        double[] y = getY(pixelDistances);
        double[] z = getZ(pixelDistances,matchXY);

        extents[0] = new Min().evaluate(x);
        extents[1] = new Max().evaluate(x);
        extents[2] = new Min().evaluate(y);
        extents[3] = new Max().evaluate(y);
        extents[4] = new Min().evaluate(z);
        extents[5] = new Max().evaluate(z);

        return extents;

    }

    public boolean hasVolume() {
        //True if all dimension (x,y,z) are > 0

        double[] extents = getExtents(true,false);

        boolean hasvol = false;

        if (extents[1]-extents[0] > 0 & extents[3]-extents[2] > 0 & extents[5]-extents[4] > 0) {
            hasvol = true;
        }

        return hasvol;
    }

    public boolean hasArea() {
        //True if all dimensions (x,y) are > 0

        double[] extents = getExtents(true,false);

        boolean hasarea = false;

        if (extents[1]-extents[0] > 0 & extents[3]-extents[2] > 0) {
            hasarea = true;
        }

        return hasarea;

    }

    public double getVoxelVolume(boolean pixelDistances) {
        return getX(pixelDistances).length;

    }

    public int getNVoxels() {
        return getX(true).length;

    }

    public double getProjectedArea(boolean pixelDistances) {
        double[] x = getX(true);
        double[] y = getY(true);
        double[][] coords = new double[x.length][2];

        for (int i=0;i<x.length;i++) {
            coords[i][0] = x[i];
            coords[i][1] = y[i];
        }

        coords = ArrayFunc.uniqueRows(coords);

        return pixelDistances ? coords.length : coords.length*dppXY*dppXY;

    }

    public int getOverlap(Volume volume2) {
        double[] x1 = getX(true);
        double[] y1 = getY(true);
        double[] z1 = getZ(true,false);

        double[] x2 = volume2.getX(true);
        double[] y2 = volume2.getY(true);
        double[] z2 = volume2.getZ(true,false);
        int ovl = 0;

        for(int i = 0; i < x1.length; ++i) {
            for(int j = 0; j < x2.length; ++j) {
                if(x1[i] == x2[j] & y1[i] == y2[j] & z1[i] == z2[j]) {
                    ovl++;
                }
            }
        }

        return ovl;
    }

    public double getCentroidDistanceToPoint(Spot point,boolean pixelDistances) {
        double x_cent = getXMean(pixelDistances);
        double y_cent = getYMean(pixelDistances);
        double z_cent = getZMean(pixelDistances,true);

        return Math.sqrt(Math.pow(x_cent-point.getX(),2) + Math.pow(y_cent-point.getY(),2)
                + Math.pow(z_cent-point.getZ(),2))-point.getRadius();

    }

    public double getNearestDistanceToPoint(Spot point,boolean pixelDistances) {
        double[] x = getX(pixelDistances);
        double[] y = getY(pixelDistances);
        double[] z = getZ(pixelDistances,true);

        double dist = Double.POSITIVE_INFINITY;
        for (int i=0;i<x.length;i++) {
            double temp_dist = Math.sqrt(Math.pow(x[i] - point.getX(), 2) + Math.pow(y[i] - point.getY(), 2)
                    + Math.pow(z[i] - point.getZ(), 2))-point.getRadius();

            if (temp_dist < dist) {
                dist = temp_dist;
            }
        }

        return dist;

    }


    // DEPRECATED METHODS

//    @Deprecated
//    public void addCoord(double xIn, double yIn, double zIn) {
//        points.add(new Point<>((int) Math.round(xIn),(int) Math.round(yIn),(int) Math.round(zIn),0));
//
//    }
//
//    @Deprecated
//    public double getCalXY() {
//        return dppXY;
//
//    }
//
//    @Deprecated
//    public double getCalZ() {
//        return dppZ;
//
//    }
//
//    @Deprecated
//    public double[] getX() {
//        return getX(true);
//    }
//
//    @Deprecated
//    public double[] getY() {
//        return getY(true);
//    }
//
//    @Deprecated
//    public double[] getZ() {
//        return getZ(true,false);
//    }
//
//    @Deprecated
//    public double getXMean() {
//        return new Mean().evaluate(getX(true));
//
//    }
//
//    @Deprecated
//    public double getYMean() {
//        return new Mean().evaluate(getY(true));
//
//    }
//
//    @Deprecated
//    public double getZMean() {
//        return new Mean().evaluate(getZ(true,false));
//
//    }
//
//    @Deprecated
//    public double getHeight() {
//        double[] z = getZ();
//
//        double min_z = new Min().evaluate(z);
//        double max_z = new Max().evaluate(z);
//
//        double height = max_z - min_z;
//
//        return height;
//
//    }
//
//    @Deprecated
//    public double[] getExtents() {
//        //Minimum and maximum values for all dimensions [x_min, y_min, z_min; x_max, y_max, z_max]
//        double[] extents = new double[6];
//
//        double[] x = getX();
//        double[] y = getY();
//        double[] z = getZ();
//
//        extents[0] = new Min().evaluate(x);
//        extents[1] = new Max().evaluate(x);
//        extents[2] = new Min().evaluate(y);
//        extents[3] = new Max().evaluate(y);
//        extents[4] = new Min().evaluate(z);
//        extents[5] = new Max().evaluate(z);
//
//        return extents;
//
//    }
//
//    @Deprecated
//    public double getVoxelVolume() {
//        return getX(true).length* dppXY * dppXY * dppZ;
//
//    }
//
//    @Deprecated
//    public double getProjectedArea() {
//        double[] x = getX();
//        double[] y = getY();
//        double[][] coords = new double[x.length][2];
//
//        for (int i=0;i<x.length;i++) {
//            coords[i][0] = x[i];
//            coords[i][1] = y[i];
//        }
//
//        coords = ArrayFunc.uniqueRows(coords);
//
//        return coords.length* dppXY * dppXY;
//
//    }
//
//    @Deprecated
//    public double getCentroidDistanceToPoint(Spot point) {
//        return getCentroidDistanceToPoint(point,true);
//
//    }
//
//    @Deprecated
//    public double getNearestDistanceToPoint(Spot point) {
//        return getNearestDistanceToPoint(point,true);
//
//    }
}
