// TODO: Store a separate set of

package wbif.sjx.common.Object.Volume2;

import wbif.sjx.common.Exceptions.IntegerOverflowException;
import wbif.sjx.common.Object.Point;
import wbif.sjx.common.Object.QuadTree.QuadTree;

import java.util.HashMap;
import java.util.TreeSet;

public class QuadTreeVolume extends Volume2 {
    private final HashMap<Integer, QuadTree> quadTrees = new HashMap<Integer, QuadTree>();
    private final HashMap<Integer, QuadTree> surfaceQuadTrees = new HashMap<Integer, QuadTree>();

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

    public QuadTreeVolume(Volume2 volume) {
        super(volume);
    }

    public QuadTreeVolume(int width, int height, int nSlices, double dppXY, double dppZ, String calibratedUnits) {
        super(width, height, nSlices, dppXY, dppZ, calibratedUnits);

    }

    @Override
    public Volume2 add(int x, int y, int z) {
        // Get relevant QuadTree
        quadTrees.putIfAbsent(z,new QuadTree(width,height));

        // Adding this point
        quadTrees.get(z).add(x,y);

        return this;

    }

    @Override
    public Volume2 add(Point<Integer> point) throws IntegerOverflowException {
        // Get relevant QuadTree
        quadTrees.putIfAbsent(point.z,new QuadTree(width,height));

        // Adding this point
        quadTrees.get(point.z).add(point.x,point.y);

        return this;

    }

    @Override
    public TreeSet<Point<Integer>> getPoints() {
        TreeSet<Point<Integer>> points = new TreeSet<>();

        // Iterating over each slice
        for (int z:quadTrees.keySet()) {
            // Getting the QuadTree for this slice
            QuadTree quadTree = quadTrees.get(z);

            // Adding each point
            for (Point<Integer> point:quadTree.getPoints()) {
                point.setZ(z);
                points.add(point);
            }

        }

        return points;

    }

    @Override
    public Volume2 setPoints(TreeSet<Point<Integer>> points) {
        // Clearing all current points
        clearPoints();

        // Iterating over each point, adding it to the quadtree
        for (Point<Integer> point:points) {
            add(point.getX(),point.getY(),point.getZ());
        }

        return this;

    }

    @Override
    public void clearPoints() {
        for (QuadTree quadTree:quadTrees.values()) quadTree.clear();
    }

    @Override
    public TreeSet<Point<Integer>> getSurface() {
        if (surfaceQuadTrees.size() == 0) calculateSurface();

        TreeSet<Point<Integer>> points = new TreeSet<>();

        // Iterating over each slice
        for (int z:surfaceQuadTrees.keySet()) {
            // Getting the QuadTree for this slice
            QuadTree quadTree = surfaceQuadTrees.get(z);

            // Adding each point
            for (Point<Integer> point:quadTree.getPoints()) {
                point.setZ(z);
                points.add(point);
            }

        }

        return points;

    }

    @Override
    public void clearSurface() {

    }

    public boolean is2D() {
        return nSlices == 1;
    }

    void calculateMeanCentroid() {
        System.out.println("wbif.sjx.common.Object.QuadTreeVolume calculateMeanCentroid needs implementing");
    }

    @Override
    public Point<Double> getMeanCentroid() {
        if (meanCentroid == null) calculateMeanCentroid();
        return meanCentroid;
    }

    void calculateMedianCentroid() {
        System.out.println("wbif.sjx.common.Object.QuadTreeVolume calculateMedianCentroid needs implementing");
    }

    @Override
    public Point<Double> getMedianCentroid() {
        if (medianCentroid == null) calculateMedianCentroid();
        return medianCentroid;
    }

    @Override
    public double getHeight(boolean pixelDistances, boolean matchXY) {
        System.out.println("wbif.sjx.common.Object.QuadTreeVolume getHeight needs implementing");
        return 0;
    }

    @Override
    public double[][] getExtents(boolean pixelDistances, boolean matchXY) {
        System.out.println("wbif.sjx.common.Object.QuadTreeVolume getExtents needs implementing");
        return new double[0][];
    }

    @Override
    public boolean hasVolume() {
        System.out.println("wbif.sjx.common.Object.QuadTreeVolume hasVolume needs implementing");
        return false;
    }

    @Override
    public boolean hasArea() {
        System.out.println("wbif.sjx.common.Object.QuadTreeVolume hasArea needs implementing");
        return false;
    }

    @Override
    public int getNVoxels() {
        System.out.println("wbif.sjx.common.Object.QuadTreeVolume getNVoxels needs implementing");
        return 0;
    }

    @Override
    public double getProjectedArea(boolean pixelDistances) {
        System.out.println("wbif.sjx.common.Object.QuadTreeVolume getProjectedArea needs implementing");
        return 0;
    }

    @Override
    public int getOverlap(PointVolume volume2) {
        System.out.println("wbif.sjx.common.Object.QuadTreeVolume getOverlap needs implementing");
        return 0;
    }

    @Override
    public Volume2 getOverlappingPoints(PointVolume volume2) {
        System.out.println("wbif.sjx.common.Object.QuadTreeVolume getOverlappingPoints needs implementing");
        return null;
    }

    @Override
    public boolean containsPoint(Point<Integer> point1) {
        System.out.println("wbif.sjx.common.Object.QuadTreeVolume containsPoint needs implementing");
        return false;
    }

    @Override
    public Volume2 createNewObject() {
        return new QuadTreeVolume(this);
    }

    public void calculateSurface() {
        System.out.println("wbif.sjx.common.Object.QuadTreeVolume calculateSurface need to handle 2D and 3D surfaces differently");
        // Iterating over each QuadTree, calculating the surface and adding it to the surface collection
        for (int z:quadTrees.keySet()) {
            // Creating a new QuadTree and getting a reference to it
            QuadTree surfaceQuadTree = surfaceQuadTrees.put(z,new QuadTree(width,height));
            if (surfaceQuadTree == null) continue;

            // Getting the QuadTree for this slice
            QuadTree quadTree = quadTrees.get(z);

            // Adding all edge points
            for (Point<Integer> point:quadTree.getEdgePoints()) surfaceQuadTree.add(point.getX(),point.getY());

        }
    }

    @Override
    public int hashCode() {
        System.out.println("wbif.sjx.common.Object.QuadTreeVolume hashCode needs implementing");

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
        System.out.println("wbif.sjx.common.Object.QuadTreeVolume equals needs implementing");

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
