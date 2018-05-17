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

    public TreeSet<Point<Integer>> getPoints() {
        return points;

    }

    public void setPoints(TreeSet<Point<Integer>> points) {
        this.points = points;
    }

    public void clearPoints() {
        points = new TreeSet<>();
    }

    public Volume(double dppXY, double dppZ, String calibratedUnits) {
        this.dppXY = dppXY;
        this.dppZ = dppZ;
        this.calibratedUnits = calibratedUnits;

    }

    public void addCoord(int xIn, int yIn, int zIn) {
        points.add(new Point<>(xIn,yIn,zIn));

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

    public void calculateSurface() {
        calculateSurface3D();
    }

    public void calculateSurface2D() {
        surface = new TreeSet<>();

        double[] extents = getExtents2D(true);
        int[][] coords = new int[(int) extents[1]+1][(int) extents[3]+1];

        // Adding pixels to a 2D array
        for (Point<Integer> point:points) {
            int x = point.getX();
            int y = point.getY();

            coords[x][y] = 1;

        }

        // Checking for neighbours
        for (Point<Integer> point:points) {
            int x = point.getX();
            int y = point.getY();

            // Points at the edge of the image are automatically classed as being edge pixels
            // THIS FIRST CONDITION CAN PROBABLY GO?
            if (x == 0 | x == extents[1] | y == 0 | y == extents[3]) {
                surface.add(new Point<>(x, y, 0));
                continue;
            }

            if (coords[x-1][y] + coords[x+1][y] + coords[x][y-1] + coords[x][y+1] < 4) {
                surface.add(new Point<>(x,y,0));
            }
        }
    }

    public void calculateSurface3D() {
        surface = new TreeSet<>();

        double[] extents = getExtents(true,false);
        int[][][] coords = new int[(int) extents[1]+1][(int) extents[3]+1][(int) extents[5]+1];

        // Adding pixels to a 3D array
        for (Point<Integer> point:points) {
            int x = point.getX();
            int y = point.getY();
            int z = point.getZ();

            // Ignore points outside smaller than zero
            if (x >= 0 && y >= 0 && z>= 0) coords[x][y][z] = 1;

        }

        // Checking for neighbours
        for (Point<Integer> point:points) {
            int x = point.getX();
            int y = point.getY();
            int z = point.getZ();

            if (x >= 0 && y >= 0 && z >= 0) {
                // Points at the edge of the image are automatically classed as being edge pixels
                if (x == 0 | x == extents[1] | y == 0 | y == extents[3] | z == 0 | z == extents[5]) {
                    surface.add(new Point<>(x, y, z));
                    continue;
                }

                if (coords[x - 1][y][z] + coords[x + 1][y][z] + coords[x][y - 1][z] + coords[x][y + 1][z] + coords[x][y][z - 1] + coords[x][y][z + 1] < 6) {
                    surface.add(new Point<>(x, y, z));
                }
            }
        }
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

    public double[] getExtents2D(boolean pixelDistances) {
        //Minimum and maximum values for all dimensions [x_min, y_min, z_min; x_max, y_max]
        double[] extents = new double[4];

        double[] x = getX(pixelDistances);
        double[] y = getY(pixelDistances);

        extents[0] = new Min().evaluate(x);
        extents[1] = new Max().evaluate(x);
        extents[2] = new Min().evaluate(y);
        extents[3] = new Max().evaluate(y);

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

    public double getCentroidSeparation(Volume volume2, boolean pixelDistances) {
        double x1 = getXMean(pixelDistances);
        double y1 = getYMean(pixelDistances);
        double z1 = getZMean(pixelDistances,true);

        double x2 = volume2.getXMean(pixelDistances);
        double y2 = volume2.getYMean(pixelDistances);
        double z2 = volume2.getZMean(pixelDistances,true);

        return Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1)+(z2-z1)*(z2-z1));

    }

    public ArrayList<Point<Integer>> getOverlappingPoints(Volume volume2) {
        TreeSet<Point<Integer>> points1 = getPoints();
        TreeSet<Point<Integer>> points2 = volume2.getPoints();

        ArrayList<Point<Integer>> overlapping = new ArrayList<>();

        for (Point<Integer> p1:points1) {
            for (Point<Integer> p2:points2) {
                if(p1.getX().equals(p2.getX()) && p1.getY().equals(p2.getY()) && p1.getZ().equals(p2.getZ())) {
                    overlapping.add(p2);
                }
            }
        }

        return overlapping;

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

    /**
     * Calculates the angle of the trajectory from this volume to volume2.  Angle is in radians and is relative to the
     * positive x-axis.
     * @param volume2
     * @return
     */
    public double calculateAngle2D(Volume volume2) {
        double x1 = getXMean(true);
        double y1 = getYMean(true);
        double x2 = volume2.getXMean(true);
        double y2 = volume2.getYMean(true);

        return Math.atan2((y2-y1),(x2-x1));


    }

    public double getContainedVolume(boolean pixelDistances) {
        if (pixelDistances) {
            return points.size()*dppZ/dppXY;
        } else {
            return points.size()*dppXY*dppXY*dppZ;
        }
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
