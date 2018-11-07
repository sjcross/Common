// TODO: Change getProjectedArea to use HashSet for coordinate indices
// TODO: Should get calculateSurface methods to work for negative values too (not just ignore them)

package wbif.sjx.common.Object;

import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.stat.descriptive.rank.Min;
import wbif.sjx.common.MathFunc.ArrayFunc;
import wbif.sjx.common.MathFunc.CumStat;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by sc13967 on 28/07/2017.
 */
public class Volume {
    protected final double dppXY; //Calibration in xy (fixed once declared in constructor)
    protected final double dppZ; //Calibration in z (fixed once declared in constructor)
    protected final String calibratedUnits;
    protected final boolean twoD;

    protected TreeSet<Point<Integer>> points = new TreeSet<>();
    protected TreeSet<Point<Integer>> surface = null;

    /**
     * Mean coordinates (XYZ) stored as pixel values.  Additional public methods (e.g. getXMean) have the option for
     * pixel or calibrated distances.
     */
    private Point<Double> meanCentroid = null;

    /**
     * Median coordinates (XYZ) stored as pixel values.  Additional public methods (e.g. getXMean) have the option for
     * pixel or calibrated distances.
     */
    private Point<Double> medianCentroid = null;


    public Volume(double dppXY, double dppZ, String calibratedUnits, boolean twoD) {
        this.dppXY = dppXY;
        this.dppZ = dppZ;
        this.calibratedUnits = calibratedUnits;
        this.twoD = twoD;
    }


    public TreeSet<Point<Integer>> getPoints() {
        return points;

    }

    public Volume setPoints(TreeSet<Point<Integer>> points) {
        this.points = points;
        return this;
    }

    public void clearPoints() {
        points = new TreeSet<>();
    }

    public Volume addCoord(int xIn, int yIn, int zIn) {
        points.add(new Point<>(xIn,yIn,zIn));
        return this;
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

    public boolean is2D() {
        return twoD;
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

    public ArrayList<Integer> getSurfaceXCoords() {
        if (surface == null) calculateSurface();
        return surface.stream().map(Point::getX).collect(Collectors.toCollection(ArrayList::new));

    }

    public ArrayList<Integer> getSurfaceYCoords() {
        if (surface == null) calculateSurface();
        return surface.stream().map(Point::getY).collect(Collectors.toCollection(ArrayList::new));

    }

    public ArrayList<Integer> getSurfaceZCoords() {
        if (surface == null) calculateSurface();
        return surface.stream().map(Point::getZ).collect(Collectors.toCollection(ArrayList::new));

    }

    public double getXYScaledZ(double z) {
        return z*dppZ/dppXY;
    }

    public void calculateSurface() {
        if (twoD) {
            calculateSurface2D();
        } else {
            calculateSurface3D();
        }
    }

    public void calculateSurface2D() {
        surface = new TreeSet<>();

        // Iterating over each Point, adding it if it has fewer than 6 neighbours
        for (Point<Integer> point:points) {
            int count = 0;

            if (containsPoint(new Point<>(point.x-1,point.y,0))) count++;
            if (containsPoint(new Point<>(point.x+1,point.y,0))) count++;
            if (containsPoint(new Point<>(point.x,point.y-1,0))) count++;
            if (containsPoint(new Point<>(point.x,point.y+1,0))) count++;

            if (count < 4) surface.add(point);
        }
    }

    public void calculateSurface3D() {
        surface = new TreeSet<>();

        // Iterating over each Point, adding it if it has fewer than 6 neighbours
        for (Point<Integer> point:points) {
            int count = 0;

            if (containsPoint(new Point<>(point.x-1,point.y,point.z))) count++;
            if (containsPoint(new Point<>(point.x+1,point.y,point.z))) count++;
            if (containsPoint(new Point<>(point.x,point.y-1,point.z))) count++;
            if (containsPoint(new Point<>(point.x,point.y+1,point.z))) count++;
            if (containsPoint(new Point<>(point.x,point.y,point.z-1))) count++;
            if (containsPoint(new Point<>(point.x,point.y,point.z+1))) count++;

            if (count < 6) surface.add(point);
        }
    }

    public TreeSet<Point<Integer>> getSurface() {
        if (surface == null) calculateSurface();
        return surface;
    }

    public double calculatePointPointSeparation(Point<Integer> point1, Point<Integer> point2) {
        Volume volume1 = new Volume(dppXY,dppZ,calibratedUnits,is2D());
        volume1.addCoord(point1.getX(),point1.getY(),point1.getZ());

        Volume volume2 = new Volume(dppXY,dppZ,calibratedUnits,is2D());
        volume2.addCoord(point2.getX(),point2.getY(),point2.getZ());

        return volume1.getCentroidSeparation(volume2,true);

    }

//    public void shrinkObject(double shrinkLength) {
//        // Calculating the distance of each point to the edge of the object
//
//    }

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

    public double[] getSurfaceX(boolean pixelDistances) {
        if (surface == null) calculateSurface();
        if (pixelDistances)
            return surface.stream().map(Point::getX).mapToDouble(Integer::doubleValue).toArray();
        else
            return surface.stream().map(Point::getX).mapToDouble(Integer::doubleValue).map(v->v* dppXY).toArray();

    }

    public double[] getSurfaceY(boolean pixelDistances) {
        if (surface == null) calculateSurface();
        if (pixelDistances)
            return surface.stream().map(Point::getY).mapToDouble(Integer::doubleValue).toArray();
        else
            return surface.stream().map(Point::getY).mapToDouble(Integer::doubleValue).map(v->v* dppXY).toArray();

    }

    /**
     *
     * @param pixelDistances
     * @param matchXY Get Z-coordinates in equivalent pixel distances to XY (e.g. for Z-coordinates at twice the XY
     *                spacing, Z of 1 will be returned as 2).
     * @return
     */
    public double[] getSurfaceZ(boolean pixelDistances, boolean matchXY) {
        if (surface == null) calculateSurface();
        if (pixelDistances)
            if (matchXY)
                return surface.stream().map(Point::getZ).mapToDouble(Integer::doubleValue).map(v -> v* dppZ / dppXY).toArray();

            else
                return surface.stream().map(Point::getZ).mapToDouble(Integer::doubleValue).toArray();

        else
            return surface.stream().map(Point::getZ).mapToDouble(Integer::doubleValue).map(v->v* dppZ).toArray();

    }

    public void clearSurface() {
        surface = null;
    }

    public void calculateMeanCentroid() {
        CumStat csX = new CumStat();
        CumStat csY = new CumStat();
        CumStat csZ = new CumStat();

        for (double value:getXCoords()) csX.addMeasure(value);
        for (double value:getYCoords()) csY.addMeasure(value);
        for (double value:getZCoords()) csZ.addMeasure(value);

        meanCentroid = new Point<>(csX.getMean(),csY.getMean(),csZ.getMean());

    }

    private void calculateMedianCentroid() {
        // Getting coordinates
        ArrayList<Integer> xCoords = getXCoords();
        ArrayList<Integer> yCoords = getYCoords();
        ArrayList<Integer> zCoords = getZCoords();

        // Sorting values in ascending order
        Collections.sort(xCoords);
        Collections.sort(yCoords);
        Collections.sort(zCoords);

        // Taking the central value
        int nValues = xCoords.size();

        if (nValues%2==0) {
            double xCent = ((double)xCoords.get(nValues/2-1)+(double)xCoords.get(nValues/2))/2;
            double yCent = ((double)yCoords.get(nValues/2-1)+(double)yCoords.get(nValues/2))/2;
            double zCent = ((double)zCoords.get(nValues/2-1)+(double)zCoords.get(nValues/2))/2;

            medianCentroid = new Point<>(xCent,yCent,zCent);

        } else {
            double xCent =  xCoords.get(nValues/2);
            double yCent =  yCoords.get(nValues/2);
            double zCent =  zCoords.get(nValues/2);

            medianCentroid = new Point<>(xCent,yCent,zCent);

        }
    }

    /**
     * Returns the previously-calculated mean x centroid.  If no centroid was previously calculated, it is calculated.
     * @param pixelDistances
     * @return
     */
    public double getXMean(boolean pixelDistances) {
        // Checking if the centroid has previously been calculated
        if (meanCentroid == null) calculateMeanCentroid();

        if (pixelDistances) return meanCentroid.getX();

        return meanCentroid.getX()*dppXY;

    }

    public double getYMean(boolean pixelDistances) {
        // Checking if the centroid has previously been calculated
        if (meanCentroid == null) calculateMeanCentroid();

        if (pixelDistances) return meanCentroid.getY();

        return meanCentroid.getY()*dppXY;

    }

    public double getZMean(boolean pixelDistances, boolean matchXY) {
        // Checking if the centroid has previously been calculated
        if (meanCentroid == null) calculateMeanCentroid();

        // matchXY is ignored if using calibrated distances
        if (pixelDistances && !matchXY) return meanCentroid.getZ();
        if (pixelDistances && matchXY) return meanCentroid.getZ()*dppZ/dppXY;

        return meanCentroid.getZ()*dppZ;

    }

    public double getXMedian(boolean pixelDistances) {
        // Checking if the centroid has previously been calculated
        if (medianCentroid == null) calculateMedianCentroid();

        if (pixelDistances) return medianCentroid.getX();

        return medianCentroid.getX()*dppXY;

    }

    public double getYMedian(boolean pixelDistances) {
        // Checking if the centroid has previously been calculated
        if (medianCentroid == null) calculateMedianCentroid();

        if (pixelDistances) return medianCentroid.getY();

        return medianCentroid.getY()*dppXY;

    }

    public double getZMedian(boolean pixelDistances, boolean matchXY) {
        // Checking if the centroid has previously been calculated
        if (medianCentroid == null) calculateMedianCentroid();

        if (pixelDistances && !matchXY) return medianCentroid.getZ();
        if (pixelDistances && matchXY) return medianCentroid.getZ()*dppZ/dppXY;

        return medianCentroid.getZ()*dppZ;

    }

    public double getHeight(boolean pixelDistances, boolean matchXY) {
        double[] z = getZ(pixelDistances,matchXY);

        double minZ = new Min().evaluate(z);
        double maxZ = new Max().evaluate(z);

        return maxZ - minZ;

    }

    public double[][] getExtents(boolean pixelDistances, boolean matchXY) {
        //Minimum and maximum values for all dimensions [x_min, y_min, z_min; x_max, y_max, z_max]
        double[][] extents = new double[3][2];

        double[] x = getX(pixelDistances);
        double[] y = getY(pixelDistances);
        double[] z = getZ(pixelDistances,matchXY);

        extents[0][0] = new Min().evaluate(x);
        extents[0][1] = new Max().evaluate(x);
        extents[1][0] = new Min().evaluate(y);
        extents[1][1] = new Max().evaluate(y);
        extents[2][0] = new Min().evaluate(z);
        extents[2][1] = new Max().evaluate(z);

        return extents;

    }

    public double[][] getExtents2D(boolean pixelDistances) {
        //Minimum and maximum values for all dimensions [x_min, y_min, z_min; x_max, y_max]
        double[][] extents = new double[2][2];

        double[] x = getX(pixelDistances);
        double[] y = getY(pixelDistances);

        extents[0][0] = new Min().evaluate(x);
        extents[0][1] = new Max().evaluate(x);
        extents[1][0] = new Min().evaluate(y);
        extents[1][1] = new Max().evaluate(y);

        return extents;

    }

    public boolean hasVolume() {
        //True if all dimension (x,y,z) are > 0

        double[][] extents = getExtents(true,false);

        boolean hasvol = false;

        if (extents[0][1]-extents[0][0] > 0 & extents[1][1]-extents[1][0] > 0 & extents[2][1]-extents[2][0] > 0) {
            hasvol = true;
        }

        return hasvol;
    }

    public boolean hasArea() {
        //True if all dimensions (x,y) are > 0

        double[][] extents = getExtents(true,false);

        boolean hasarea = false;

        if (extents[0][1]-extents[0][0] > 0 & extents[1][1]-extents[1][0] > 0) {
            hasarea = true;
        }

        return hasarea;

    }

    public int getNVoxels() {
        return points.size();

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
        TreeSet<Point<Integer>> points1 = getPoints();
        TreeSet<Point<Integer>> points2 = volume2.getPoints();

        HashSet<Point<Integer>> overlapping = new HashSet<>();

        int count = 0;

        // Iterate over the object with the fewest points
        if (points1.size() < points2.size()) {
            for (Point<Integer> p1:points1) if (points2.contains(p1)) count++;
        } else {
            for (Point<Integer> p2:points2) if (points1.contains(p2)) count++;
        }

        return count;

    }

    public double getCentroidSeparation(Volume volume2, boolean pixelDistances) {
        double x1 = getXMean(pixelDistances);
        double y1 = getYMean(pixelDistances);
        double z1 = getZMean(pixelDistances,true);

        double x2 = volume2.getXMean(pixelDistances);
        double y2 = volume2.getYMean(pixelDistances);
        double z2 = volume2.getZMean(pixelDistances,true);

        return Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1)+(z2-z1)*(z2-z1));

    }

    public double getSurfaceSeparation(Volume volume2, boolean pixelDistances) {
        double minDist = Double.MAX_VALUE;

        // Getting coordinates for the surface points (6-way connectivity)
        double[] x1 = getSurfaceX(pixelDistances);
        double[] y1 = getSurfaceY(pixelDistances);
        double[] z1 = getSurfaceZ(pixelDistances, true);
        double[] z1Slice = getSurfaceZ(true, false);

        double[] x2 = volume2.getSurfaceX(pixelDistances);
        double[] y2 = volume2.getSurfaceY(pixelDistances);
        double[] z2 = volume2.getSurfaceZ(pixelDistances, true);
        double[] z2Slice = volume2.getSurfaceZ(true, false);

        // Measuring point-to-point distances on both object surfaces
        for (int j = 0; j < x2.length; j++) {
            Point<Integer> currentPoint2 = new Point<>((int) x2[j], (int) y2[j], (int) z2Slice[j]);
            boolean isInside = false;
            for (int i = 0; i < x1.length; i++) {
                double xDist = x2[j] - x1[i];
                double yDist = y2[j] - y1[i];
                double zDist = z2[j] - z1[i];
                double dist = Math.sqrt(xDist * xDist + yDist * yDist + zDist * zDist);

                if (dist < Math.abs(minDist)) {
                    minDist = dist;
                    isInside = getPoints().contains(currentPoint2);
                    if (!isInside) {
                        Point<Integer> currentPoint1 = new Point<>((int) x1[i], (int) y1[i], (int) z1Slice[i]);
                        isInside = volume2.getPoints().contains(currentPoint1);
                    }
                }
            }

            // If this point is inside the parent the distance should be negative
            if (isInside) minDist = -minDist;

        }

        return minDist;

    }

    public HashSet<Point<Integer>> getOverlappingPoints(Volume volume2) {
        TreeSet<Point<Integer>> points1 = getPoints();
        TreeSet<Point<Integer>> points2 = volume2.getPoints();

        HashSet<Point<Integer>> overlapping = new HashSet<>();
        if (points1.size() < points2.size()) {
            for (Point<Integer> p1 : points1) if (points2.contains(p1)) overlapping.add(p1);
        } else {
            for (Point<Integer> p2 : points2) if (points1.contains(p2)) overlapping.add(p2);
        }

        return overlapping;

    }

    /**
     * Calculates the angle of the trajectory from this volume to volume2.  Angle is in radians and is relative to the
     * positive x-axis.
     * @param volume2
     * @return
     */
    public double calculateAngle2D(Volume volume2) {
        Point<Double> p1 = new Point<>(getXMean(true),getYMean(true),0d);
        Point<Double> p2 = new Point<>(volume2.getXMean(true),volume2.getYMean(true),0d);

        return p1.calculateAngle2D(p2);

    }

    /**
     * Calculates the angle of the trajectory from this volume to a point.  Angle is in radians and is relative to the
     * positive x-axis.
     * @param point
     * @return
     */
    public double calculateAngle2D(Point<Double> point) {
        Point<Double> p1 = new Point<>(getXMean(true),getYMean(true),0d);

        return p1.calculateAngle2D(point);

    }

    public double getContainedVolume(boolean pixelDistances) {
        if (pixelDistances) {
            return points.size()*dppZ/dppXY;
        } else {
            return points.size()*dppXY*dppXY*dppZ;
        }
    }

    public boolean containsPoint(Point<Integer> point1) {
        return getPoints().contains(point1);
    }

    @Override
    public int hashCode() {
        int hash = 1;

        hash = 31*hash + ((Number) dppXY).hashCode();
        hash = 31*hash + ((Number) dppZ).hashCode();
        hash = 31*hash + calibratedUnits.toUpperCase().hashCode();

        for (Point<Integer> point:points) {
            hash = 31*hash + point.hashCode();
        }

        return hash;

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Volume)) return false;

        Volume volume2 = (Volume) obj;
        TreeSet<Point<Integer>> points1 = getPoints();
        TreeSet<Point<Integer>> points2 = volume2.getPoints();

        if (points1.size() != points2.size()) return false;

        if (dppXY != volume2.getDistPerPxXY()) return false;
        if (dppZ != volume2.getDistPerPxZ()) return false;
        if (!calibratedUnits.toUpperCase().equals(volume2.getCalibratedUnits().toUpperCase())) return false;

        Iterator<Point<Integer>> iterator1 = points1.iterator();
        Iterator<Point<Integer>> iterator2 = points2.iterator();

        while (iterator1.hasNext()) {
            Point<Integer> point1 = iterator1.next();
            Point<Integer> point2 = iterator2.next();

            if (!point1.equals(point2)) return false;

        }

        return true;

    }

}
