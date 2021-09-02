package io.github.sjcross.common.object.volume;

import io.github.sjcross.common.analysis.volume.PointSurfaceSeparatorCalculator;
import io.github.sjcross.common.analysis.volume.SurfaceSeparationCalculator;
import io.github.sjcross.common.exceptions.IntegerOverflowException;
import io.github.sjcross.common.object.Point;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Volume {
    protected SpatCal spatCal;
    protected CoordinateSet coordinateSet;
    protected Volume surface = null;
    protected Volume projection = null;
    protected Point<Double> meanCentroidPx = null;

    public Volume(VolumeType volumeType, SpatCal spatCal) {
        this.spatCal = spatCal;
        coordinateSet = createCoordinateStore(volumeType);
    }

    public Volume(VolumeType volumeType, int width, int height, int nSlices, double dppXY, double dppZ, String units) {
        this.spatCal = new SpatCal(dppXY, dppZ, units, width, height, nSlices);
        coordinateSet = createCoordinateStore(volumeType);
    }

    CoordinateSet createCoordinateStore(VolumeType type) {
        switch (type) {
            case OCTREE:
                return new OctreeCoordinates();
            case QUADTREE:
                return new QuadtreeCoordinates();
            case POINTLIST:
            default:
                return new PointCoordinates();
        }
    }

    public void add(int x, int y, int z) throws PointOutOfRangeException {
        if (x < 0 || x >= spatCal.width)
            throw new PointOutOfRangeException("Coordinate out of bounds! (x: " + x + ")");
        if (y < 0 || y >= spatCal.height)
            throw new PointOutOfRangeException("Coordinate out of bounds! (y: " + y + ")");
        if (z < 0 || z >= spatCal.nSlices)
            throw new PointOutOfRangeException("Coordinate out of bounds! (z: " + z + ")");

        coordinateSet.add(x, y, z);

    }

    public void add(Point<Integer> point) throws PointOutOfRangeException {
        add(point.x, point.y, point.z);

    }

    public void translateCoords(int xOffs, int yOffs, int zOffs) {
        Volume newVol = new Volume(coordinateSet.getVolumeType(), spatCal);

        // CoordinateSet newCoordinateSet = coordinateSet.createEmptyCoordinateSet();
        for (Point<Integer> point : coordinateSet) {
            try {
                newVol.add(new Point<>(point.getX() + xOffs, point.getY() + yOffs, point.getZ() + zOffs));
            } catch (PointOutOfRangeException e) {
                // Do nothing
            }
        }
        newVol.finalise();

        // Replacing old coordinate set with the transposed one
        this.coordinateSet = newVol.getCoordinateSet();

    }

    public void finalise() {
        coordinateSet.finalise();
    }

    public void finalise(int z) {
        coordinateSet.finalise(z);
    }

    @Deprecated
    public TreeSet<Point<Integer>> getPoints() {
        return new TreeSet<>(coordinateSet);

    }

    public boolean hasCalculatedSurface() {
        return surface != null;
    }

    synchronized public Volume getSurface() {
        if (surface == null) {
            surface = new Volume(VolumeType.POINTLIST, getSpatialCalibration());
            surface.setCoordinateSet(coordinateSet.calculateSurface(is2D()));
        }

        return surface;

    }

    public Volume getProjected() {
        if (projection == null) {
            VolumeType outputType;
            // Octree is best represented by quadtree. Pointlist can stay as pointlist.
            switch (getVolumeType()) {
                case OCTREE:
                case QUADTREE:
                    outputType = VolumeType.QUADTREE;
                    break;
                case POINTLIST:
                default:
                    outputType = VolumeType.POINTLIST;
                    break;
            }

            projection = new Volume(outputType, spatCal.width, spatCal.height, 1, spatCal.dppXY, spatCal.dppZ,
                    spatCal.units);
            projection.setCoordinateSet(coordinateSet.calculateProjected());

        }

        return projection;

    }

    public boolean hasCalculatedProjection() {
        return projection != null;
    }

    public double getProjectedArea(boolean pixelDistances) {
        int size = getProjected().size();
        return pixelDistances ? size : size * spatCal.dppXY * spatCal.dppXY;

    }

    public Volume getSlice(int slice) {
        VolumeType outputType;
        // Octree is best represented by quadtree. Pointlist can stay as pointlist.
        switch (getVolumeType()) {
            case OCTREE:
            case QUADTREE:
                outputType = VolumeType.QUADTREE;
                break;
            case POINTLIST:
            default:
                outputType = VolumeType.POINTLIST;
                break;
        }

        Volume sliceVol = new Volume(outputType, spatCal.width, spatCal.height, 1, spatCal.dppXY, spatCal.dppZ,
                spatCal.units);
        sliceVol.setCoordinateSet(coordinateSet.getSlice(slice));

        return sliceVol;

    }

    public void setPoints(TreeSet<Point<Integer>> points) throws PointOutOfRangeException {
        for (Point<Integer> point : points)
            add(point);
    }

    public boolean hasCalculatedCentroid() {
        return meanCentroidPx != null;
    }

    public Point<Double> getMeanCentroid(boolean pixelDistances, boolean matchXY) {
        if (meanCentroidPx == null)
            meanCentroidPx = coordinateSet.calculateMeanCentroid();

        if (pixelDistances) {
            if (matchXY) {
                // Keeping X and Y, but changing Z to match
                double xCent = meanCentroidPx.getX();
                double yCent = meanCentroidPx.getY();
                double zCent = getXYScaledZ(meanCentroidPx.getZ());

                return new Point<Double>(xCent, yCent, zCent);

            } else {
                // Using raw X,Y,Z units
                return meanCentroidPx.duplicate();
            }
        }

        // Converting to calibrated units
        double xCent = meanCentroidPx.getX() * spatCal.dppXY;
        double yCent = meanCentroidPx.getY() * spatCal.dppXY;
        double zCent = meanCentroidPx.getZ() * spatCal.dppZ;

        return new Point<Double>(xCent, yCent, zCent);

    }

    public Point<Double> getMeanCentroid() {
        return getMeanCentroid(true, false);

    }

    public int size() {
        return coordinateSet.size();
    }

    public boolean contains(Point<Integer> point1) {
        return coordinateSet.contains(point1);
    }

    public long getNumberOfElements() {
        return coordinateSet.getNumberOfElements();
    }

    // PUBLIC METHODS

    public boolean is2D() {
        return spatCal.nSlices == 1;

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
            return getPoints().stream().map(Point::getX).mapToDouble(Integer::doubleValue).map(v -> v * spatCal.dppXY)
                    .toArray();

    }

    @Deprecated
    public double[] getY(boolean pixelDistances) {
        if (pixelDistances)
            return getPoints().stream().map(Point::getY).mapToDouble(Integer::doubleValue).toArray();
        else
            return getPoints().stream().map(Point::getY).mapToDouble(Integer::doubleValue).map(v -> v * spatCal.dppXY)
                    .toArray();

    }

    /**
     *
     * @param pixelDistances
     * @param matchXY        Get Z-coordinates in equivalent pixel distances to XY
     *                       (e.g. for Z-coordinates at twice the XY spacing, Z of 1
     *                       will be returned as 2).
     * @return
     */
    @Deprecated
    public double[] getZ(boolean pixelDistances, boolean matchXY) {
        if (pixelDistances)
            if (matchXY)
                return getPoints().stream().map(Point::getZ).mapToDouble(Integer::doubleValue)
                        .map(v -> v * spatCal.dppZ / spatCal.dppXY).toArray();

            else
                return getPoints().stream().map(Point::getZ).mapToDouble(Integer::doubleValue).toArray();

        else
            return getPoints().stream().map(Point::getZ).mapToDouble(Integer::doubleValue).map(v -> v * spatCal.dppZ)
                    .toArray();

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
            return getSurface().getPoints().stream().map(Point::getX).mapToDouble(Integer::doubleValue)
                    .map(v -> v * spatCal.dppXY).toArray();

    }

    @Deprecated
    public double[] getSurfaceY(boolean pixelDistances) {
        if (pixelDistances)
            return getSurface().getPoints().stream().map(Point::getY).mapToDouble(Integer::doubleValue).toArray();
        else
            return getSurface().getPoints().stream().map(Point::getY).mapToDouble(Integer::doubleValue)
                    .map(v -> v * spatCal.dppXY).toArray();
    }

    /**
     *
     * @param pixelDistances
     * @param matchXY        Get Z-coordinates in equivalent pixel distances to XY
     *                       (e.g. for Z-coordinates at twice the XY spacing, Z of 1
     *                       will be returned as 2).
     * @return
     */
    @Deprecated
    public double[] getSurfaceZ(boolean pixelDistances, boolean matchXY) {
        if (pixelDistances)
            if (matchXY)
                return getSurface().getPoints().stream().map(Point::getZ).mapToDouble(Integer::doubleValue)
                        .map(v -> v * spatCal.dppZ / spatCal.dppXY).toArray();

            else
                return getSurface().getPoints().stream().map(Point::getZ).mapToDouble(Integer::doubleValue).toArray();

        else
            return getSurface().getPoints().stream().map(Point::getZ).mapToDouble(Integer::doubleValue)
                    .map(v -> v * spatCal.dppZ).toArray();

    }

    public double getCalibratedX(Point<Integer> point) {
        return point.getX() * spatCal.dppXY;
    }

    public double getCalibratedY(Point<Integer> point) {
        return point.getY() * spatCal.dppXY;
    }

    public double getXYScaledZ(double z) {
        return z * spatCal.dppZ / spatCal.dppXY;
    }

    public double getXYScaledZ(Point<Integer> point) {
        return point.getZ() * spatCal.dppZ / spatCal.dppXY;
    }

    public double getCalibratedZ(Point<Integer> point, boolean matchXY) {
        if (matchXY)
            return point.getZ() * spatCal.dppZ / spatCal.dppXY;
        else
            return point.getZ() * spatCal.dppZ;
    }

    public double calculatePointPointSeparation(Point<Integer> point1, Point<Integer> point2, boolean pixelDistances) {
        try {
            Volume volume1 = new Volume(VolumeType.POINTLIST, spatCal.duplicate());
            volume1.add(point1.getX(), point1.getY(), point1.getZ());

            Volume volume2 = new Volume(VolumeType.POINTLIST, spatCal.duplicate());
            volume2.add(point2.getX(), point2.getY(), point2.getZ());

            return volume1.getCentroidSeparation(volume2, pixelDistances);

        } catch (IntegerOverflowException | PointOutOfRangeException e) {
            return Double.NaN;
        }
    }

    public double getXMean(boolean pixelDistances) {
        return getMeanCentroid(pixelDistances, true).getX();

    }

    public double getYMean(boolean pixelDistances) {
        return getMeanCentroid(pixelDistances, true).getY();

    }

    public double getZMean(boolean pixelDistances, boolean matchXY) {
        return getMeanCentroid(pixelDistances, matchXY).getZ();

    }

    public double getHeight(boolean pixelDistances, boolean matchXY) {
        double minZ = Double.MAX_VALUE;
        double maxZ = Double.MIN_VALUE;

        // Getting XY ranges
        for (Point<Integer> point : coordinateSet) {
            minZ = Math.min(minZ, point.z);
            maxZ = Math.max(maxZ, point.z);
        }

        double height = maxZ - minZ;

        if (pixelDistances)
            return matchXY ? getXYScaledZ(height) : height;
        return height * spatCal.dppZ;

    }

    public double[][] getExtents(boolean pixelDistances, boolean matchXY) {
        if (size() == 0)
            return new double[][] { { 0, 0 }, { 0, 0 }, { 0, 0 } };

        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        double minZ = Double.MAX_VALUE;
        double maxZ = Double.MIN_VALUE;

        // Getting XY ranges
        for (Point<Integer> point : coordinateSet) {
            minX = Math.min(minX, point.x);
            maxX = Math.max(maxX, point.x);
            minY = Math.min(minY, point.y);
            maxY = Math.max(maxY, point.y);
            minZ = Math.min(minZ, point.z);
            maxZ = Math.max(maxZ, point.z);
        }

        // Getting XY ranges
        if (pixelDistances) {
            if (matchXY) {
                minZ = getXYScaledZ(minZ);
                maxZ = getXYScaledZ(maxZ);
            }
        } else {
            minX = minX * spatCal.dppXY;
            maxX = maxX * spatCal.dppXY;
            minY = minY * spatCal.dppXY;
            maxY = maxY * spatCal.dppXY;
            minZ = minZ * spatCal.dppZ;
            maxZ = maxZ * spatCal.dppZ;
        }

        if (is2D()) {
            minZ = 0;
            maxZ = 0;
        }

        return new double[][] { { minX, maxX }, { minY, maxY }, { minZ, maxZ } };

    }

    public boolean hasVolume() {
        // True if all dimension (x,y,z) are > 0

        double[][] extents = getExtents(true, false);

        boolean hasvol = false;

        if (extents[0][1] - extents[0][0] > 0 & extents[1][1] - extents[1][0] > 0 & extents[2][1] - extents[2][0] > 0) {
            hasvol = true;
        }

        return hasvol;

    } // Copied

    public boolean hasArea() {
        // True if all dimensions (x,y) are > 0

        double[][] extents = getExtents(true, false);

        boolean hasarea = false;

        if (extents[0][1] - extents[0][0] > 0 & extents[1][1] - extents[1][0] > 0) {
            hasarea = true;
        }

        return hasarea;

    } // Copied

    public double getCentroidSeparation(Volume volume2, boolean pixelDistances) {
        return getCentroidSeparation(volume2, pixelDistances, false);
    }

    public double getCentroidSeparation(Volume volume2, boolean pixelDistances, boolean force2D) {
        double x1 = getXMean(pixelDistances);
        double y1 = getYMean(pixelDistances);
        double x2 = volume2.getXMean(pixelDistances);
        double y2 = volume2.getYMean(pixelDistances);

        // If one or both of the volumes are 2D, only calculate separation in XY
        if (is2D() || volume2.is2D() || force2D) {
            return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));

        } else {
            double z1 = getZMean(pixelDistances, true);
            double z2 = volume2.getZMean(pixelDistances, true);

            return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1) * (z2 - z1));

        }
    }

    public double getSurfaceSeparation(Volume volume2, boolean pixelDistances) {
        SurfaceSeparationCalculator calculator = new SurfaceSeparationCalculator(this, volume2, false);
        return calculator.getMinDist(pixelDistances);
    }

    public double getSurfaceSeparation(Volume volume2, boolean pixelDistances, boolean force2D) {
        SurfaceSeparationCalculator calculator = new SurfaceSeparationCalculator(this, volume2, force2D);
        return calculator.getMinDist(pixelDistances);
    }

    public double getPointSurfaceSeparation(Point<Double> point, boolean pixelDistances) {
        return getPointSurfaceSeparation(point, pixelDistances, false);
    }

    public double getPointSurfaceSeparation(Point<Double> point, boolean pixelDistances, boolean force2D) {
        // If this object is only 2D, ensure the Z-position of the point is also zero
        if (is2D() || force2D) 
            point = new Point<>(point.x, point.y, 0d);
        
        PointSurfaceSeparatorCalculator calculator = new PointSurfaceSeparatorCalculator(this, point);
        return calculator.getMinDist(pixelDistances);
    }

    /**
     * Calculates the angle of the trajectory from this volume to volume2. Angle is
     * in radians and is relative to the positive x-axis.
     * 
     * @param volume2
     * @return
     */
    public double calculateAngle2D(Volume volume2) {
        Point<Double> p1 = new Point<>(getXMean(true), getYMean(true), 0d);
        Point<Double> p2 = new Point<>(volume2.getXMean(true), volume2.getYMean(true), 0d);

        return p1.calculateAngle2D(p2);

    }

    /**
     * Calculates the angle of the trajectory from this volume to a point. Angle is
     * in radians and is relative to the positive x-axis.
     * 
     * @param point
     * @return
     */
    public double calculateAngle2D(Point<Double> point) {
        Point<Double> p1 = new Point<>(getXMean(true), getYMean(true), 0d);

        return p1.calculateAngle2D(point);

    }

    public Volume getOverlappingPoints(Volume volume2) {
        Volume overlapping = new Volume(getVolumeType(), getSpatialCalibration());

        try {
            if (size() < volume2.size()) {
                for (Point<Integer> p1 : coordinateSet)
                    if (volume2.contains(p1))
                        overlapping.add(p1);
            } else {
                for (Point<Integer> p2 : volume2.coordinateSet)
                    if (contains(p2))
                        overlapping.add(p2);
            }
        } catch (IntegerOverflowException | PointOutOfRangeException e) {
            // These points are a subset of the input Volume objects, so if they don't
            // overflow these can't either.
            // Similarly, they can't be out of range.
        }

        return overlapping;

    } // Copied

    public int getOverlap(Volume volume2) {
        int count = 0;

        if (size() < volume2.size()) {
            for (Point<Integer> p1 : coordinateSet)
                if (volume2.contains(p1))
                    count++;
        } else {
            for (Point<Integer> p2 : volume2.coordinateSet)
                if (contains(p2))
                    count++;
        }

        return count;

    } // Copied

    public double getContainedVolume(boolean pixelDistances) {
        if (pixelDistances) {
            return size() * spatCal.dppZ / spatCal.dppXY;
        } else {
            return size() * spatCal.dppXY * spatCal.dppXY * spatCal.dppZ;
        }
    }

    public void clearAllCoordinates() {
        coordinateSet.clear();
        surface = null;
        projection = null;
        meanCentroidPx = null;
    }

    public void clearSurface() {
        surface = null;
    }

    public void clearPoints() {
        coordinateSet.clear();
    }

    public void clearProjected() {
        projection = null;
    }

    public void clearCentroid() {
        meanCentroidPx = null;
    }

    @Override
    public int hashCode() {
        int hash = coordinateSet.hashCode();

        hash = 31 * hash + ((Number) spatCal.dppXY).hashCode();
        hash = 31 * hash + ((Number) spatCal.dppZ).hashCode();
        hash = 31 * hash + spatCal.units.toUpperCase().hashCode();

        return hash;

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Volume))
            return false;

        Volume volume = (Volume) obj;

        if (spatCal.dppXY != volume.getDppXY())
            return false;
        if (spatCal.dppZ != volume.getDppZ())
            return false;
        if (!spatCal.units.toUpperCase().equals(volume.getUnits().toUpperCase()))
            return false;

        return coordinateSet.equals(volume.coordinateSet);

    }

    // GETTERS AND SETTERS

    public SpatCal getSpatialCalibration() {
        return spatCal;
    }

    public void setSpatialCalibration(SpatCal spatCal) {
        this.spatCal = spatCal;
        if (surface != null) surface.setSpatialCalibration(spatCal);
        if (projection != null) projection.setSpatialCalibration(spatCal);
    }

    public double getDppXY() {
        return spatCal.dppXY;
    }

    public double getDppZ() {
        return spatCal.dppZ;
    }

    public String getUnits() {
        return spatCal.units;
    }

    public int getWidth() {
        return spatCal.width;
    }

    public int getHeight() {
        return spatCal.height;
    }

    public int getNSlices() {
        return spatCal.nSlices;
    }

    public CoordinateSet getCoordinateSet() {
        return coordinateSet;
    }

    public VolumeType getVolumeType() {
        return coordinateSet.getVolumeType();
    }

    public void setCoordinateSet(CoordinateSet coordinateSet)

    {
        this.coordinateSet = coordinateSet;
    }

    public Iterator<Point<Double>> getCalibratedIterator(boolean pixelDistances, boolean matchXY) {
        return new VolumeIterator(pixelDistances, matchXY);
    }

    public Iterator<Point<Integer>> getCoordinateIterator() {
        return coordinateSet.iterator();
    }

    private class VolumeIterator implements Iterator<Point<Double>> {
        private Iterator<Point<Integer>> iterator;
        private boolean pixelDistances;
        private boolean matchXY;

        public VolumeIterator(boolean pixelDistances, boolean matchXY) {
            this.pixelDistances = pixelDistances;
            this.iterator = coordinateSet.iterator();
            this.matchXY = matchXY;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Point<Double> next() {
            Point<Integer> nextPoint = iterator.next();
            int x = nextPoint.x;
            int y = nextPoint.y;
            int z = nextPoint.z;

            if (pixelDistances && matchXY) {
                return new Point<>((double) x, (double) y, (double) z * spatCal.dppZ / spatCal.dppXY);
            } else if (pixelDistances & !matchXY) {
                return new Point<>((double) x, (double) y, (double) z);
            } else {
                return new Point<>((double) x * spatCal.dppXY, (double) y * spatCal.dppXY, (double) z * spatCal.dppZ);
            }
        }
    }
}
