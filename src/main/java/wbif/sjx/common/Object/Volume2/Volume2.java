package wbif.sjx.common.Object.Volume2;

import wbif.sjx.common.Exceptions.IntegerOverflowException;
import wbif.sjx.common.Object.Point;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.stream.Collectors;

public abstract class Volume2 {
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

    public abstract Volume2 add(int x, int y, int z) throws IntegerOverflowException ;

    public abstract Volume2 add(Point<Integer> point) throws IntegerOverflowException ;

    public abstract TreeSet<Point<Integer>> getPoints();

    public abstract Volume2 setPoints(TreeSet<Point<Integer>> points);

    public abstract void clearPoints();

    public abstract TreeSet<Point<Integer>> getSurface();

    public abstract void clearSurface();

    public abstract boolean is2D();

    public abstract Point<Double> getMeanCentroid();

    public abstract Point<Double> getMedianCentroid();

    public abstract double getHeight(boolean pixelDistances, boolean matchXY);

    public abstract double[][] getExtents(boolean pixelDistances, boolean matchXY);

    public abstract boolean hasVolume();

    public abstract boolean hasArea();

    public abstract int getNVoxels();

    public abstract double getProjectedArea(boolean pixelDistances);

    public abstract int getOverlap(PointVolume volume2);

    /////// THIS RETURNS A DIFFERENT STRUCTURE TO ORIGINAL (VOLUME CLASS) METHOD
    public abstract Volume2 getOverlappingPoints(PointVolume volume2);

    public abstract boolean containsPoint(Point<Integer> point1);

    public abstract Volume2 createNewObject();


    // PUBLIC METHODS

    public double getXYScaledZ(double z) {
        return z*dppZ/dppXY;
    }

    public ArrayList<Integer> getXCoords() {
        return getPoints().stream().map(Point::getX).collect(Collectors.toCollection(ArrayList::new));

    }

    public ArrayList<Integer> getYCoords() {
        return getPoints().stream().map(Point::getY).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Integer> getZCoords() {
        return getPoints().stream().map(Point::getZ).collect(Collectors.toCollection(ArrayList::new));

    }

    public double[] getX(boolean pixelDistances) {
        if (pixelDistances)
            return getPoints().stream().map(Point::getX).mapToDouble(Integer::doubleValue).toArray();
        else
            return getPoints().stream().map(Point::getX).mapToDouble(Integer::doubleValue).map(v->v* dppXY).toArray();

    }

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
    public double[] getZ(boolean pixelDistances, boolean matchXY) {
        if (pixelDistances)
            if (matchXY)
                return getPoints().stream().map(Point::getZ).mapToDouble(Integer::doubleValue).map(v -> v* dppZ / dppXY).toArray();

            else
                return getPoints().stream().map(Point::getZ).mapToDouble(Integer::doubleValue).toArray();

        else
            return getPoints().stream().map(Point::getZ).mapToDouble(Integer::doubleValue).map(v->v* dppZ).toArray();

    }

    public ArrayList<Integer> getSurfaceXCoords() {
        return getSurface().stream().map(Point::getX).collect(Collectors.toCollection(ArrayList::new));

    }

    public ArrayList<Integer> getSurfaceYCoords() {
        return getSurface().stream().map(Point::getY).collect(Collectors.toCollection(ArrayList::new));

    }

    public ArrayList<Integer> getSurfaceZCoords() {
        return getSurface().stream().map(Point::getZ).collect(Collectors.toCollection(ArrayList::new));

    }

    public double[] getSurfaceX(boolean pixelDistances) {
        if (pixelDistances)
            return getSurface().stream().map(Point::getX).mapToDouble(Integer::doubleValue).toArray();
        else
            return getSurface().stream().map(Point::getX).mapToDouble(Integer::doubleValue).map(v->v* dppXY).toArray();

    }

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
    public double[] getSurfaceZ(boolean pixelDistances, boolean matchXY) {
        if (pixelDistances)
            if (matchXY)
                return getSurface().stream().map(Point::getZ).mapToDouble(Integer::doubleValue).map(v -> v* dppZ / dppXY).toArray();

            else
                return getSurface().stream().map(Point::getZ).mapToDouble(Integer::doubleValue).toArray();

        else
            return getSurface().stream().map(Point::getZ).mapToDouble(Integer::doubleValue).map(v->v* dppZ).toArray();

    }

//    public double calculatePointPointSeparation(Point<Integer> point1, Point<Integer> point2, boolean pixelDistances) {
//        try {
//            Volume volume1 = new Volume(dppXY,dppZ,calibratedUnits,is2D());
//            volume1.add(point1.getX(),point1.getY(),point1.getZ());
//
//            Volume volume2 = new Volume(dppXY,dppZ,calibratedUnits,is2D());
//            volume2.add(point2.getX(),point2.getY(),point2.getZ());
//
//            return volume1.getCentroidSeparation(volume2,pixelDistances);
//
//        } catch (IntegerOverflowException e) {
//            return Double.NaN;
//        }
//    }

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

    public double getXMedian(boolean pixelDistances) {
        if (pixelDistances) return getMedianCentroid().getX();

        return getMedianCentroid().getX()*dppXY;

    }

    public double getYMedian(boolean pixelDistances) {
        if (pixelDistances) return getMedianCentroid().getY();

        return getMedianCentroid().getY()*dppXY;

    }

    public double getZMedian(boolean pixelDistances, boolean matchXY) {
        if (pixelDistances && !matchXY) return getMedianCentroid().getZ();
        if (pixelDistances && matchXY) return getMedianCentroid().getZ()*dppZ/dppXY;

        return getMedianCentroid().getZ()*dppZ;

    }

    public double getCentroidSeparation(PointVolume volume2, boolean pixelDistances) {
        double x1 = getXMean(pixelDistances);
        double y1 = getYMean(pixelDistances);
        double z1 = getZMean(pixelDistances,true);

        double x2 = volume2.getXMean(pixelDistances);
        double y2 = volume2.getYMean(pixelDistances);
        double z2 = volume2.getZMean(pixelDistances,true);

        return Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1)+(z2-z1)*(z2-z1));

    }

    public double getSurfaceSeparation(PointVolume volume2, boolean pixelDistances) {
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
    public double calculateAngle2D(PointVolume volume2) {
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
            return getNVoxels()*dppZ/dppXY;
        } else {
            return getNVoxels()*dppXY*dppXY*dppZ;
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
}
