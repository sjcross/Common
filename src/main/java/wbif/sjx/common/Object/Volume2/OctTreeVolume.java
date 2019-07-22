// TODO: Store a separate set of

package wbif.sjx.common.Object.Volume2;

import wbif.sjx.common.Exceptions.IntegerOverflowException;
import wbif.sjx.common.Object.Point;
import wbif.sjx.common.Object.QuadTree.OctTree;

import java.util.TreeSet;

public class OctTreeVolume extends Volume2 {
    private final OctTree octTree;

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

    public OctTreeVolume(Volume2 volume) {
        super(volume);

        octTree = new OctTree(width, height, nSlices);
    }

    public OctTreeVolume(int width, int height, int nSlices, double dppXY, double dppZ, String calibratedUnits) {
        super(width, height, nSlices, dppXY, dppZ, calibratedUnits);

        octTree = new OctTree(width, height, nSlices);
    }

    @Override
    public Volume2 add(int x, int y, int z) {
        // Adding this point
        octTree.add(x, y, z);
        
//        octTree.optimise();
        
        return this;

    }

    @Override
    public Volume2 add(Point<Integer> point) throws IntegerOverflowException {
        // Adding this point
        octTree.add(point.getX(), point.getY(), point.getZ());
        
//        octTree.optimise();
        
        return this;

    }

    @Override
    public TreeSet<Point<Integer>> getPoints() {
        return octTree.getPoints();

    }

    @Override
    public Volume2 setPoints(TreeSet<Point<Integer>> points) {
        // Clearing all current points
        clearPoints();

        // Iterating over each point, adding it to the octtree
        for (Point<Integer> point:points) {
            add(point.getX(),point.getY(),point.getZ());
        }

        return this;

    }

    @Override
    public void clearPoints() {
        octTree.clear();
    }
    
    void calculateMeanCentroid() {
        System.out.println("wbif.sjx.common.Object.OctTreeVolume calculateMeanCentroid needs implementing");
    }

    @Override
    public Point<Double> getMeanCentroid() {
        if (meanCentroid == null) calculateMeanCentroid();
        return meanCentroid;
    }

    void calculateMedianCentroid() {
        System.out.println("wbif.sjx.common.Object.OctTreeVolume calculateMedianCentroid needs implementing");
    }

    @Override
    public Point<Double> getMedianCentroid() {
        if (medianCentroid == null) calculateMedianCentroid();
        return medianCentroid;
    }

    @Override
    public double getHeight(boolean pixelDistances, boolean matchXY) {
        System.out.println("wbif.sjx.common.Object.OctTreeVolume getHeight needs implementing");
        return 0;
    }

    @Override
    public double[][] getExtents(boolean pixelDistances, boolean matchXY) {
        System.out.println("wbif.sjx.common.Object.OctTreeVolume getExtents needs implementing");
        return new double[0][];
    }

    @Override
    public boolean hasVolume() {
        System.out.println("wbif.sjx.common.Object.OctTreeVolume hasVolume needs implementing");
        return false;
    }

    @Override
    public boolean hasArea() {
        System.out.println("wbif.sjx.common.Object.OctTreeVolume hasArea needs implementing");
        return false;
    }

    @Override
    public int getNVoxels() {
        System.out.println("wbif.sjx.common.Object.OctTreeVolume getNVoxels needs implementing");
        return 0;
    }

    @Override
    public double getProjectedArea(boolean pixelDistances) {
        System.out.println("wbif.sjx.common.Object.OctTreeVolume getProjectedArea needs implementing");
        return 0;
    }

    @Override
    public int getOverlap(PointVolume volume2) {
        System.out.println("wbif.sjx.common.Object.OctTreeVolume getOverlap needs implementing");
        return 0;
    }

    @Override
    public Volume2 getOverlappingPoints(PointVolume volume2) {
        System.out.println("wbif.sjx.common.Object.OctTreeVolume getOverlappingPoints needs implementing");
        return null;
    }

    @Override
    public boolean containsPoint(Point<Integer> point1) {
        System.out.println("wbif.sjx.common.Object.OctTreeVolume containsPoint needs implementing");
        return false;
    }

    @Override
    public Volume2 createNewObject() {
        return new OctTreeVolume(this);
    }

    public void calculateSurface() {
        surface = octTree.getEdgePoints();
        
    }

    @Override
    public int hashCode() {
        System.out.println("wbif.sjx.common.Object.OctTreeVolume hashCode needs implementing");

        int hash = 1;

//        hash = 31*hash + ((Number) dppXY).hashCode();
//        hash = 31*hash + ((Number) dppZ).hashCode();
//        hash = 31*hash + calibratedUnits.toUpperCase().hashCode();
//
//        for (Point<Integer> point:points) {
//            hash = 31*hash + point.hashCode();
//        }

        return hash;

    }

    @Override
    public boolean equals(Object obj) {
        System.out.println("wbif.sjx.common.Object.OctTreeVolume equals needs implementing");

//        if (obj == this) return true;
//        if (!(obj instanceof Volume)) return false;
//
//        Volume volume2 = (Volume) obj;
//        TreeSet<Point<Integer>> points1 = getPoints();
//        TreeSet<Point<Integer>> points2 = volume2.getPoints();
//
//        if (points1.size() != points2.size()) return false;
//
//        if (dppXY != volume2.getDistPerPxXY()) return false;
//        if (dppZ != volume2.getDistPerPxZ()) return false;
//        if (!calibratedUnits.toUpperCase().equals(volume2.getCalibratedUnits().toUpperCase())) return false;
//
//        Iterator<Point<Integer>> iterator1 = points1.iterator();
//        Iterator<Point<Integer>> iterator2 = points2.iterator();
//
//        while (iterator1.hasNext()) {
//            Point<Integer> point1 = iterator1.next();
//            Point<Integer> point2 = iterator2.next();
//
//            if (!point1.equals(point2)) return false;
//
//        }

        return true;

    }
}
