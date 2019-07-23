// TODO: Store a separate set of

package wbif.sjx.common.Object.Volume2;

import wbif.sjx.common.Exceptions.IntegerOverflowException;
import wbif.sjx.common.Object.Point;
import wbif.sjx.common.Object.QuadTree.QuadTree;

import java.util.*;
import java.util.function.Consumer;

public class QuadTreeVolume extends Volume2 {
    private final TreeMap<Integer, QuadTree> quadTrees = new TreeMap<>();


    public QuadTreeVolume(Volume2 volume) {
        super(volume);
    }

    public QuadTreeVolume(int width, int height, int nSlices, double dppXY, double dppZ, String calibratedUnits) {
        super(width, height, nSlices, dppXY, dppZ, calibratedUnits);

    }

    @Override
    public Volume2 add(int x, int y, int z) {
        quadTrees.putIfAbsent(z,new QuadTree(width,height));

        // Get relevant QuadTree
        QuadTree quadTree = quadTrees.get(z);

        // Adding this point
        quadTree.add(x, y);

//        quadTree.optimise();

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
    public void calculateSurface() {
        if (is2D()) {
            // Get the sole QuadTree
            QuadTree quadTree = quadTrees.values().iterator().next();

            // Set the surface list to the edge points of the QuadTree
            surface = quadTree.getEdgePoints();
        } else {
            clearSurface();

            // For each slice
            for (int z:quadTrees.keySet()) {
                QuadTree silceQT       = quadTrees.get(z);
                QuadTree belowSliceQT  = quadTrees.get(z-1);
                QuadTree aboveSliceQT  = quadTrees.get(z+1);

                // Add all the edge points for the slices QuadTree factoring in the neighbouring slices
                silceQT.getEdgePoints3D(surface, belowSliceQT, aboveSliceQT);
            }
        }
    }

    @Override
    public void calculateMeanCentroid() {
        System.out.println("wbif.sjx.common.Object.QuadTreeVolume calculateMeanCentroid needs implementing");
    }

    @Override
    public void calculateMedianCentroid() {
        System.out.println("wbif.sjx.common.Object.QuadTreeVolume calculateMedianCentroid needs implementing");
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
    public int getOverlap(Volume2 volume2) {
        System.out.println("wbif.sjx.common.Object.QuadTreeVolume getOverlap needs implementing");
        return 0;
    }

    @Override
    public Volume2 getOverlappingPoints(Volume2 volume2) {
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

    @Override
    public Iterator<Point<Integer>> iterator() {
        return new QuadTreeVolumeIterator();
    }

    @Override
    public void forEach(Consumer<? super Point<Integer>> action) {
        System.out.println("wbif.sjx.common.Object.QuadTreeVolume forEach needs implementing");
    }

    @Override
    public Spliterator<Point<Integer>> spliterator() {
        System.out.println("wbif.sjx.common.Object.QuadTreeVolume spliterator needs implementing");
        return null;
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

    private class QuadTreeVolumeIterator implements Iterator<Point<Integer>> {
        private Iterator<Integer> sliceIterator = quadTrees.keySet().iterator();
        private Iterator<Point<Integer>> quadTreeIterator = null;
        private int z = 0;

        public QuadTreeVolumeIterator() {
            if (!sliceIterator.hasNext()) return;
            this.z = sliceIterator.next();
            quadTreeIterator = quadTrees.get(z).iterator();

        }

        @Override
        public boolean hasNext() {
            // Check if this current slice has another point
            if (quadTreeIterator.hasNext()) return true;

            // If the current slice doesn't have another point, check if there is another slice
            if (!sliceIterator.hasNext()) return false;

            // Now we've got to access the next slice without incrementing the slice iterator
            Iterator<Integer> tempZIterator = quadTrees.keySet().iterator();
            while (tempZIterator.hasNext()) {
                int tempZ = tempZIterator.next();
                if (tempZ != z) continue;

                tempZ = tempZIterator.next();
                return quadTrees.get(tempZ).iterator().hasNext();

            }

            // Shouldn't get this far
            return false;

        }

        @Override
        public Point<Integer> next() {
            // If the current slice has another point, return this with the appropriate slice index
            if (quadTreeIterator.hasNext()) {
                Point<Integer> slicePoint = quadTreeIterator.next();
                return new Point<>(slicePoint.getX(),slicePoint.getY(),z);
            }

            if (sliceIterator.hasNext()) {
                this.z = sliceIterator.next();
                this.quadTreeIterator = quadTrees.get(z).iterator();
                Point<Integer> slicePoint = quadTreeIterator.next();
                return new Point<>(slicePoint.getX(),slicePoint.getY(),z);

            }

            return null;

        }
    }
}
