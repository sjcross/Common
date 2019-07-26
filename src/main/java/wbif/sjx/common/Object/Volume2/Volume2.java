package wbif.sjx.common.Object.Volume2;

import wbif.sjx.common.Exceptions.IntegerOverflowException;
import wbif.sjx.common.Object.Point;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.stream.Collectors;

public abstract class Volume2 implements Iterable<Point<Integer>> {
    protected final double dppXY; //Calibration in xy
    protected final double dppZ; //Calibration in z
    protected final String calibratedUnits;

    protected final int width;
    protected final int height;
    protected final int nSlices;


    public Volume2(Volume2 volume) {
        this.width = volume.getWidth();
        this.height = volume.getHeight();
        this.nSlices = volume.nSlices;
        this.dppXY = volume.getDppXY();
        this.dppZ = volume.getDppZ();
        this.calibratedUnits = volume.getCalibratedUnits();

    }

    public Volume2(int width, int height, int nSlices, double dppXY, double dppZ, String calibratedUnits) {
        this.width = width;
        this.height = height;
        this.nSlices = nSlices;
        this.dppXY = dppXY;
        this.dppZ = dppZ;
        this.calibratedUnits = calibratedUnits;

    }


    // ABSTRACT METHODS

    public abstract Volume2 add(int x, int y, int z);

    public abstract Volume2 add(Point<Integer> point);

    public abstract void finalise();

    @Deprecated
    public abstract TreeSet<Point<Integer>> getPoints();

    public abstract Volume2 setPoints(TreeSet<Point<Integer>> points);

    public abstract void clearPoints();

    public abstract TreeSet<Point<Integer>> getSurface();

    public abstract void clearSurface();

    public abstract Point<Double> getMeanCentroid();

    public abstract int size();

    public abstract double getProjectedArea(boolean pixelDistances);

    public abstract boolean contains(Point<Integer> point1);

    public abstract Volume2 createNewObject();


    // PUBLIC METHODS

    public boolean is2D() {
        return nSlices == 1;

    }

    public double getXYScaledZ(double z) {
        return z*dppZ/dppXY;
    }

    @Deprecated
    public ArrayList<Integer> getXCoords() {
        return getPoints().stream().map(Point::getX).collect(Collectors.toCollection(ArrayList::new));

    }

    @Deprecated
    public ArrayList<Integer> getYCoords() {
        return getPoints().stream().map(Point::getY).collect(Collectors.toCollection(ArrayList::new));
    }

    @Deprecated
    public ArrayList<Integer> getZCoords() {
        return getPoints().stream().map(Point::getZ).collect(Collectors.toCollection(ArrayList::new));

    }

    @Deprecated
    public double[] getX(boolean pixelDistances) {
        if (pixelDistances)
            return getPoints().stream().map(Point::getX).mapToDouble(Integer::doubleValue).toArray();
        else
            return getPoints().stream().map(Point::getX).mapToDouble(Integer::doubleValue).map(v->v* dppXY).toArray();

    }

    @Deprecated
    public double[] getY(boolean pixelDistances) {
        if (pixelDistances)
            return getPoints().stream().map(Point::getY).mapToDouble(Integer::doubleValue).toArray();
        else
            return getPoints().stream().map(Point::getY).mapToDouble(Integer::doubleValue).map(v->v* dppXY).toArray();

    }

    /**
     *
     * @param pixelDistances
     * @param matchXY Get Z-coordinates in equivalent pixel distances to XY (e.g. for Z-coordinates at twice the XY
     *                spacing, Z of 1 will be returned as 2).
     * @return
     */
    @Deprecated
    public double[] getZ(boolean pixelDistances, boolean matchXY) {
        if (pixelDistances)
            if (matchXY)
                return getPoints().stream().map(Point::getZ).mapToDouble(Integer::doubleValue).map(v -> v* dppZ / dppXY).toArray();

            else
                return getPoints().stream().map(Point::getZ).mapToDouble(Integer::doubleValue).toArray();

        else
            return getPoints().stream().map(Point::getZ).mapToDouble(Integer::doubleValue).map(v->v* dppZ).toArray();

    }

    @Deprecated
    public ArrayList<Integer> getSurfaceXCoords() {
        return getSurface().stream().map(Point::getX).collect(Collectors.toCollection(ArrayList::new));

    }

    @Deprecated
    public ArrayList<Integer> getSurfaceYCoords() {
        return getSurface().stream().map(Point::getY).collect(Collectors.toCollection(ArrayList::new));

    }

    @Deprecated
    public ArrayList<Integer> getSurfaceZCoords() {
        return getSurface().stream().map(Point::getZ).collect(Collectors.toCollection(ArrayList::new));

    }

    @Deprecated
    public double[] getSurfaceX(boolean pixelDistances) {
        if (pixelDistances)
            return getSurface().stream().map(Point::getX).mapToDouble(Integer::doubleValue).toArray();
        else
            return getSurface().stream().map(Point::getX).mapToDouble(Integer::doubleValue).map(v->v* dppXY).toArray();

    }

    @Deprecated
    public double[] getSurfaceY(boolean pixelDistances) {
        if (pixelDistances)
            return getSurface().stream().map(Point::getY).mapToDouble(Integer::doubleValue).toArray();
        else
            return getSurface().stream().map(Point::getY).mapToDouble(Integer::doubleValue).map(v->v* dppXY).toArray();

    }

    /**
     *
     * @param pixelDistances
     * @param matchXY Get Z-coordinates in equivalent pixel distances to XY (e.g. for Z-coordinates at twice the XY
     *                spacing, Z of 1 will be returned as 2).
     * @return
     */
    @Deprecated
    public double[] getSurfaceZ(boolean pixelDistances, boolean matchXY) {
        if (pixelDistances)
            if (matchXY)
                return getSurface().stream().map(Point::getZ).mapToDouble(Integer::doubleValue).map(v -> v* dppZ / dppXY).toArray();

            else
                return getSurface().stream().map(Point::getZ).mapToDouble(Integer::doubleValue).toArray();

        else
            return getSurface().stream().map(Point::getZ).mapToDouble(Integer::doubleValue).map(v->v* dppZ).toArray();

    }

    public double calculatePointPointSeparation(Point<Integer> point1, Point<Integer> point2, boolean pixelDistances) {
        try {
            Volume2 volume1 = new PointVolume(width,height,nSlices,dppXY,dppZ,calibratedUnits);
            volume1.add(point1.getX(),point1.getY(),point1.getZ());

            Volume2 volume2 = new PointVolume(width,height,nSlices,dppXY,dppZ,calibratedUnits);
            volume2.add(point2.getX(),point2.getY(),point2.getZ());

            return volume1.getCentroidSeparation(volume2,pixelDistances);

        } catch (IntegerOverflowException e) {
            return Double.NaN;
        }
    }

    public double getXMean(boolean pixelDistances) {
        if (pixelDistances) return getMeanCentroid().getX();

        return getMeanCentroid().getX()*dppXY;

    }

    public double getYMean(boolean pixelDistances) {
        if (pixelDistances) return getMeanCentroid().getY();

        return getMeanCentroid().getY()*dppXY;

    }

    public double getZMean(boolean pixelDistances, boolean matchXY) {
        // matchXY is ignored if using calibrated distances
        if (pixelDistances && !matchXY) return getMeanCentroid().getZ();
        if (pixelDistances && matchXY) return getMeanCentroid().getZ()*dppZ/dppXY;

        return getMeanCentroid().getZ()*dppZ;

    }

    public double getHeight(boolean pixelDistances, boolean matchXY) {
        double minZ = Double.MAX_VALUE;
        double maxZ = Double.MIN_VALUE;

        // Getting XY ranges
        for (Point<Integer> point:this) {
            minZ = Math.min(minZ,point.z);
            maxZ = Math.max(maxZ,point.z);
        }

        double height = maxZ-minZ;

        if (pixelDistances) return matchXY ? getXYScaledZ(height) : height;
        return height*dppZ;

    }

    public double[][] getExtents(boolean pixelDistances, boolean matchXY) {
        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        double minZ = Double.MAX_VALUE;
        double maxZ = Double.MIN_VALUE;

        // Getting XY ranges
        for (Point<Integer> point:this) {
            minX = Math.min(minX,point.x);
            maxX = Math.max(maxX,point.x);
            minY = Math.min(minY,point.y);
            maxY = Math.max(maxY,point.y);
            minZ = Math.min(minZ,point.z);
            maxZ = Math.max(maxZ,point.z);
        }

        // Getting XY ranges
        if (pixelDistances) {
            if (matchXY) {
                minZ = getXYScaledZ(minZ);
                maxZ = getXYScaledZ(maxZ);
            }
        } else {
            minX = minX*dppXY;
            maxX = maxX*dppXY;
            minY = minY*dppXY;
            maxY = maxY*dppXY;
            minZ = minZ*dppZ;
            maxZ = maxZ*dppZ;
        }

        return new double[][]{{minX,maxX},{minY,maxY},{minZ,maxZ}};

    }

    public boolean hasVolume() {
        //True if all dimension (x,y,z) are > 0

        double[][] extents = getExtents(true,false);

        boolean hasvol = false;

        if (extents[0][1]-extents[0][0] > 0 & extents[1][1]-extents[1][0] > 0 & extents[2][1]-extents[2][0] > 0) {
            hasvol = true;
        }

        return hasvol;

    } // Copied

    public boolean hasArea() {
        //True if all dimensions (x,y) are > 0

        double[][] extents = getExtents(true,false);

        boolean hasarea = false;

        if (extents[0][1]-extents[0][0] > 0 & extents[1][1]-extents[1][0] > 0) {
            hasarea = true;
        }

        return hasarea;

    } // Copied

    public double getCentroidSeparation(Volume2 volume2, boolean pixelDistances) {
        double x1 = getXMean(pixelDistances);
        double y1 = getYMean(pixelDistances);
        double z1 = getZMean(pixelDistances,true);

        double x2 = volume2.getXMean(pixelDistances);
        double y2 = volume2.getYMean(pixelDistances);
        double z2 = volume2.getZMean(pixelDistances,true);

        return Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1)+(z2-z1)*(z2-z1));

    }

    public double getSurfaceSeparation(Volume2 volume2, boolean pixelDistances) {
        System.out.println("wbif.sjx.common.Object.Volume2 getSurfaceSeparation needs implementing.  Possibly make use of point iterator when implemented - rather than getting array of all coordiantes");
//        SurfaceSeparationCalculator calculator = new SurfaceSeparationCalculator(this,volume2,pixelDistances);
//
//        return calculator.getMinDist();
        return 0;
    }

    /**
     * Calculates the angle of the trajectory from this volume to volume2.  Angle is in radians and is relative to the
     * positive x-axis.
     * @param volume2
     * @return
     */
    public double calculateAngle2D(Volume2 volume2) {
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

    public Volume2 getOverlappingPoints(Volume2 volume2) {
        Volume2 overlapping = createNewObject();

        try {
            if (size() < volume2.size()) {
                for (Point<Integer> p1 : this) if (volume2.contains(p1)) overlapping.add(p1);
            } else {
                for (Point<Integer> p2 : volume2) if (contains(p2)) overlapping.add(p2);
            }
        } catch (IntegerOverflowException e) {
            // These points are a subset of the input PointVolume objects, so if they don't overflow these can't either
        }

        return overlapping;

    } // Copied

    public int getOverlap(Volume2 volume2) {
        int count = 0;

        if (size() < volume2.size()) {
            for (Point<Integer> p1 : this) if (volume2.contains(p1)) count++;
        } else {
            for (Point<Integer> p2 : volume2) if (contains(p2)) count++;
        }

        return count;

    } // Copied

    public double getContainedVolume(boolean pixelDistances) {
        if (pixelDistances) {
            return size()*dppZ/dppXY;
        } else {
            return size()*dppXY*dppXY*dppZ;
        }
    }


    // GETTERS AND SETTERS

    public double getDppXY() {
        return dppXY;
    }

    public double getDppZ() {
        return dppZ;
    }

    public String getCalibratedUnits() {
        return calibratedUnits;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getnSlices() {
        return nSlices;
    }


    // VOLUME FACTORY

    public static Volume2 newVolume(Volumes type, int width, int height, int nSlices, double dppXY, double dppZ, String calibratedUnits)
    {
        return type.factory.newVolume(width, height, nSlices, dppXY, dppZ, calibratedUnits);
    }

    private interface VolumeFactory
    {
        Volume2 newVolume(int width, int height, int nSlices, double dppXY, double dppZ, String calibratedUnits);
    }

    public enum Volumes
    {
        POINTSET(PointVolume::new),
        QUADTREE(QuadTreeVolume::new),
        OCTREE(OcTreeVolume::new),
        OPTIMAL((width, height, nSlices, dppXY, dppZ, calibratedUnits) ->
        {
            // ...

            return null;
        });

        private final VolumeFactory factory;

        Volumes(VolumeFactory factory)
        {
            this.factory = factory;
        }
    }
}
