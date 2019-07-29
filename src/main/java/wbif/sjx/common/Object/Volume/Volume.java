package wbif.sjx.common.Object.Volume;

import wbif.sjx.common.Exceptions.IntegerOverflowException;
import wbif.sjx.common.Object.Point;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Volume {
    protected final double dppXY; //Calibration in xy
    protected final double dppZ; //Calibration in z
    protected final String calibratedUnits;

    protected final int width;
    protected final int height;
    protected final int nSlices;

    protected CoordinateStore coordinateStore;
    protected Volume surface = null;
    protected Volume projected = null;
    protected Point<Double> meanCentroid = null;

    public enum VolumeType {POINTLIST, QUADTREE, OCTREE};
    private VolumeType volumeType;


    public Volume(Volume volume) {
        this.width = volume.getWidth();
        this.height = volume.getHeight();
        this.nSlices = volume.nSlices;
        this.dppXY = volume.getDppXY();
        this.dppZ = volume.getDppZ();
        this.calibratedUnits = volume.getCalibratedUnits();
        this.volumeType = volume.volumeType;

        coordinateStore = createCoordinateStore(volumeType);

    }

    public Volume(VolumeType volumeType, Volume volume) {
        this.width = volume.getWidth();
        this.height = volume.getHeight();
        this.nSlices = volume.nSlices;
        this.dppXY = volume.getDppXY();
        this.dppZ = volume.getDppZ();
        this.calibratedUnits = volume.getCalibratedUnits();

        coordinateStore = createCoordinateStore(volumeType);

    }

    public Volume(VolumeType volumeType, int width, int height, int nSlices, double dppXY, double dppZ, String calibratedUnits) {
        this.width = width;
        this.height = height;
        this.nSlices = nSlices;
        this.dppXY = dppXY;
        this.dppZ = dppZ;
        this.calibratedUnits = calibratedUnits;
        this.volumeType = volumeType;

        coordinateStore = createCoordinateStore(this.volumeType);

    }

    CoordinateStore createCoordinateStore(VolumeType type) {
        switch (type) {
            case OCTREE:
                return new OctreeCoordinates(width,height,nSlices);
            case POINTLIST:
            default:
                return new PointCoordinates();
            case QUADTREE:
                return new QuadtreeCoordinates(width,height);
        }
    }


    public void clearSurface() {
        surface = null;
    }


    // ABSTRACT METHODS

    public void add(int x, int y, int z) {
        if (x < 0 || x >= width)  throw new IndexOutOfBoundsException("Coordinate out of bounds! (x: " + x + ")");
        if (y < 0 || y >= height) throw new IndexOutOfBoundsException("Coordinate out of bounds! (y: " + y + ")");
        if (z >= nSlices) throw new IndexOutOfBoundsException("Coordinate out of bounds! (z: " + z + ")");

        coordinateStore.add(x,y,z);

    }

    public void add(Point<Integer> point) {
        add(point.x,point.y,point.z);

    }

    public void finalise() {
        coordinateStore.finalise();
    }

    @Deprecated
    public TreeSet<Point<Integer>> getPoints() {
        return new TreeSet<>(coordinateStore);

    }

    public Volume getSurface() {
        if (surface == null) {
            surface = new Volume(VolumeType.POINTLIST,this);
            surface.setCoordinateStore(coordinateStore.calculateSurface(is2D()));
        }

        return surface;

    }

    public Volume getProjected() {
        if (projected == null) {
            projected = new Volume(VolumeType.POINTLIST,width,height,1,dppXY,dppZ,calibratedUnits);
            projected.setCoordinateStore(coordinateStore.calculateProjected());
        }

        return projected;

    }

    public double getProjectedArea(boolean pixelDistances) {
        int size = getProjected().size();
        return pixelDistances ? size : size*dppXY*dppXY;

    }

    public void setPoints(TreeSet<Point<Integer>> points) {
        for (Point<Integer> point:points) add(point);
    }

    public void clearPoints() {
        coordinateStore.clear();
    }

    public Point<Double> getMeanCentroid() {
        if (meanCentroid == null) meanCentroid = coordinateStore.calculateMeanCentroid();
        return meanCentroid;
    }

    public int size() {
        return coordinateStore.size();
    }

    public boolean contains(Point<Integer> point1) {
        return coordinateStore.contains(point1);
    }

    public long getNumberOfElements() {
        return coordinateStore.getNumberOfElements();
    }


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
        return getSurface().getPoints().stream().map(Point::getX).collect(Collectors.toCollection(ArrayList::new));

    }

    @Deprecated
    public ArrayList<Integer> getSurfaceYCoords() {
        return getSurface().getPoints().stream().map(Point::getY).collect(Collectors.toCollection(ArrayList::new));

    }

    @Deprecated
    public ArrayList<Integer> getSurfaceZCoords() {
        return getSurface().getPoints().stream().map(Point::getZ).collect(Collectors.toCollection(ArrayList::new));

    }

    @Deprecated
    public double[] getSurfaceX(boolean pixelDistances) {
        if (pixelDistances)
            return getSurface().getPoints().stream().map(Point::getX).mapToDouble(Integer::doubleValue).toArray();
        else
            return getSurface().getPoints().stream().map(Point::getX).mapToDouble(Integer::doubleValue).map(v->v* dppXY).toArray();

    }

    @Deprecated
    public double[] getSurfaceY(boolean pixelDistances) {
        if (pixelDistances)
            return getSurface().getPoints().stream().map(Point::getY).mapToDouble(Integer::doubleValue).toArray();
        else
            return getSurface().getPoints().stream().map(Point::getY).mapToDouble(Integer::doubleValue).map(v->v* dppXY).toArray();
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
                return getSurface().getPoints().stream().map(Point::getZ).mapToDouble(Integer::doubleValue).map(v -> v* dppZ / dppXY).toArray();

            else
                return getSurface().getPoints().stream().map(Point::getZ).mapToDouble(Integer::doubleValue).toArray();

        else
            return getSurface().getPoints().stream().map(Point::getZ).mapToDouble(Integer::doubleValue).map(v->v* dppZ).toArray();

    }

    public double calculatePointPointSeparation(Point<Integer> point1, Point<Integer> point2, boolean pixelDistances) {
        try {
            Volume volume1 = new Volume(VolumeType.POINTLIST,width,height,nSlices,dppXY,dppZ,calibratedUnits);
            volume1.add(point1.getX(),point1.getY(),point1.getZ());

            Volume volume2 = new Volume(VolumeType.POINTLIST,width,height,nSlices,dppXY,dppZ,calibratedUnits);
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
        for (Point<Integer> point:coordinateStore) {
            minZ = Math.min(minZ,point.z);
            maxZ = Math.max(maxZ,point.z);
        }

        double height = maxZ-minZ;

        if (pixelDistances) return matchXY ? getXYScaledZ(height) : height;
        return height*dppZ;

    }

    public double[][] getExtents(boolean pixelDistances, boolean matchXY) {
        if (size() == 0) return new double[][]{{0,0},{0,0},{0,0}};

        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        double minZ = Double.MAX_VALUE;
        double maxZ = Double.MIN_VALUE;

        // Getting XY ranges
        for (Point<Integer> point:coordinateStore) {
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
        System.out.println("wbif.sjx.common.Object.Volume getSurfaceSeparation needs implementing.  Possibly make use of point iterator when implemented - rather than getting array of all coordiantes");
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

    public Volume getOverlappingPoints(Volume volume2) {
        Volume overlapping = new Volume(Volume.VolumeType.POINTLIST,this);

        try {
            if (size() < volume2.size()) {
                for (Point<Integer> p1 : coordinateStore) if (volume2.contains(p1)) overlapping.add(p1);
            } else {
                for (Point<Integer> p2 : volume2.coordinateStore) if (contains(p2)) overlapping.add(p2);
            }
        } catch (IntegerOverflowException e) {
            // These points are a subset of the input Volume objects, so if they don't overflow these can't either
        }

        return overlapping;

    } // Copied

    public int getOverlap(Volume volume2) {
        int count = 0;

        if (size() < volume2.size()) {
            for (Point<Integer> p1 : coordinateStore) if (volume2.contains(p1)) count++;
        } else {
            for (Point<Integer> p2 : volume2.coordinateStore) if (contains(p2)) count++;
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

    @Override
    public int hashCode() {
        int hash = coordinateStore.hashCode();

        hash = 31*hash + ((Number) dppXY).hashCode();
        hash = 31*hash + ((Number) dppZ).hashCode();
        hash = 31*hash + calibratedUnits.toUpperCase().hashCode();

        return hash;

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Volume)) return false;

        Volume volume = (Volume) obj;

        if (dppXY != volume.getDppXY()) return false;
        if (dppZ != volume.getDppZ()) return false;
        if (!calibratedUnits.toUpperCase().equals(volume.getCalibratedUnits().toUpperCase())) return false;

        return coordinateStore.equals(volume.coordinateStore);

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

    public CoordinateStore getCoordinateStore() {
        return coordinateStore;
    }

    public void setCoordinateStore(CoordinateStore coordinateStore) {
        this.coordinateStore = coordinateStore;
    }

}
