// TODO: Change getProjectedArea to use HashSet for coordinate indices
// TODO: Should get calculateSurface methods to work for negative values too (not just ignore them)

package wbif.sjx.common.Object.Volume2;

import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.stat.descriptive.rank.Min;
import wbif.sjx.common.Analysis.Volume.SurfaceSeparationCalculator;
import wbif.sjx.common.Exceptions.IntegerOverflowException;
import wbif.sjx.common.MathFunc.ArrayFunc;
import wbif.sjx.common.MathFunc.CumStat;
import wbif.sjx.common.Object.Point;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by sc13967 on 28/07/2017.
 */
public class PointVolume extends Volume2 {
    protected TreeSet<Point<Integer>> points = new TreeSet<>();

    public PointVolume(Volume2 volume) {
        super(volume);
    }

    public PointVolume(int width, int height, int nSlices, double dppXY, double dppZ, String calibratedUnits) {
        super(width, height, nSlices, dppXY, dppZ, calibratedUnits);

    }


    @Override
    public TreeSet<Point<Integer>> getPoints() {
        return points;

    } // Copied

    @Override
    public PointVolume setPoints(TreeSet<Point<Integer>> points) {
        this.points = points;
        return this;
    } // Copied

    @Override
    public void clearPoints() {
        points = new TreeSet<>();
    } // Copied

    @Override
    public PointVolume add(int xIn, int yIn, int zIn) throws IntegerOverflowException {
        points.add(new Point<>(xIn,yIn,zIn));
        if (points.size() == Integer.MAX_VALUE) throw new IntegerOverflowException("Object too large (Integer overflow).");
        return this;
    } // Copied

    @Override
    public Volume2 add(Point<Integer> point) throws IntegerOverflowException {
        points.add(point);
        if (points.size() == Integer.MAX_VALUE) throw new IntegerOverflowException("Object too large (Integer overflow).");
        return this;
    }

    @Override
    public Point<Double> getMeanCentroid() {
        return null;
    }

    @Override
    public Point<Double> getMedianCentroid() {
        return null;
    }

    @Override
    public void calculateSurface() {
        if (is2D()) {
            calculateSurface2D();
        } else {
            calculateSurface3D();
        }
    } // Copied

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
    } // Not needed

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
    } // Not needed

    @Override
    public void calculateMeanCentroid() {
        CumStat csX = new CumStat();
        CumStat csY = new CumStat();
        CumStat csZ = new CumStat();

        for (double value:getXCoords()) csX.addMeasure(value);
        for (double value:getYCoords()) csY.addMeasure(value);
        for (double value:getZCoords()) csZ.addMeasure(value);

        meanCentroid = new Point<>(csX.getMean(),csY.getMean(),csZ.getMean());

    } // Copied

    @Override
    public void calculateMedianCentroid() {
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
    } // Copied

    @Override
    public double getHeight(boolean pixelDistances, boolean matchXY) {
        double[] z = getZ(pixelDistances,matchXY);

        double minZ = new Min().evaluate(z);
        double maxZ = new Max().evaluate(z);

        return maxZ - minZ;

    } // Copied

    @Override
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

    } // Copied

    @Override
    public boolean hasVolume() {
        //True if all dimension (x,y,z) are > 0

        double[][] extents = getExtents(true,false);

        boolean hasvol = false;

        if (extents[0][1]-extents[0][0] > 0 & extents[1][1]-extents[1][0] > 0 & extents[2][1]-extents[2][0] > 0) {
            hasvol = true;
        }

        return hasvol;
    } // Copied

    @Override
    public boolean hasArea() {
        //True if all dimensions (x,y) are > 0

        double[][] extents = getExtents(true,false);

        boolean hasarea = false;

        if (extents[0][1]-extents[0][0] > 0 & extents[1][1]-extents[1][0] > 0) {
            hasarea = true;
        }

        return hasarea;

    } // Copied

    @Override
    public int getNVoxels() {
        return points.size();

    } // Copied

    @Override
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

    } // Copied

    @Override
    public int getOverlap(Volume2 volume2) {
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

    } // Copied

    @Override
    public Volume2 getOverlappingPoints(Volume2 volume2) {
        TreeSet<Point<Integer>> points1 = getPoints();
        TreeSet<Point<Integer>> points2 = volume2.getPoints();

        Volume2 overlapping = new PointVolume(width,height,nSlices,dppXY,dppZ,calibratedUnits);

        try {
            if (points1.size() < points2.size()) {
                for (Point<Integer> p1 : points1) if (points2.contains(p1)) overlapping.add(p1);
            } else {
                for (Point<Integer> p2 : points2) if (points1.contains(p2)) overlapping.add(p2);
            }
        } catch (IntegerOverflowException e) {
            // These points are a subset of the input PointVolume objects, so if they don't overflow these can't either
        }

        return overlapping;

    } // Copied

    @Override
    public boolean containsPoint(Point<Integer> point1) {
        return getPoints().contains(point1);
    } // Copied

    @Override
    public Volume2 createNewObject() {
        return new PointVolume(this);
    }

    @Override
    public Iterator<Point<Integer>> iterator() {
        return points.iterator();
    }

    @Override
    public void forEach(Consumer<? super Point<Integer>> action) {
        points.forEach(action);
    }

    @Override
    public Spliterator<Point<Integer>> spliterator() {
        return points.spliterator();
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
        if (!(obj instanceof PointVolume)) return false;

        PointVolume volume2 = (PointVolume) obj;
        TreeSet<Point<Integer>> points1 = getPoints();
        TreeSet<Point<Integer>> points2 = volume2.getPoints();

        if (points1.size() != points2.size()) return false;

        if (dppXY != volume2.getDppXY()) return false;
        if (dppZ != volume2.getDppZ()) return false;
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
