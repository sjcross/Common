// TODO: Change getProjectedArea to use HashSet for coordinate indices
// TODO: Should get calculateSurface methods to work for negative values too (not just ignore them)

package wbif.sjx.common.Object.Volume;

import wbif.sjx.common.Object.Point;
import wbif.sjx.common.Object.QuadTree.QuadTree;

import java.util.*;

/**
 * Created by sc13967 on 28/07/2017.
 */
public class QuadtreeCoordinates implements CoordinateStore {
    private final TreeMap<Integer, QuadTree> quadTrees = new TreeMap<>();
    private final int width;
    private final int height;

    public QuadtreeCoordinates(int width, int height) {
        this.width = width;
        this.height = height;
    }


    // Adding and removing points

    @Override
    public boolean add(int x, int y, int z) {
        quadTrees.putIfAbsent(z,new QuadTree(width,height));

        // Get relevant QuadTree
        QuadTree quadTree = quadTrees.get(z);

        // Adding this point
        quadTree.add(x, y);

//        quadTree.optimise();

        return true;

    }

    @Override
    public boolean add(Point<Integer> point) {
        return add(point.x,point.y,point.z);
    }

    @Override
    public boolean addAll(Collection<? extends Point<Integer>> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        Point<Integer> point = (Point<Integer>) o;

        if (!quadTrees.containsKey(point.z)) return false;
        return quadTrees.get(point.z).contains(point.x,point.y);

    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        Point<Integer> point = (Point<Integer>) o;

        if (!quadTrees.containsKey(point.z)) return false;
        quadTrees.get(point.z).remove(point.x,point.y);

        return true;

    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        for (QuadTree quadTree:quadTrees.values()) quadTree.clear();
    }

    @Override
    public void finalise() {
        for (QuadTree quadTree:quadTrees.values()) quadTree.optimise();
    }


    // Creating coordinate subsets

    @Override
    public CoordinateStore calculateSurface(boolean is2D) {
        if (is2D) return calculateSurface2D();
        else return calculateSurface3D();

    }

    CoordinateStore calculateSurface2D() {
        // Get the sole QuadTree
        QuadTree quadTree = quadTrees.values().iterator().next();

        // Set the surface list to the edge points of the QuadTree
        return quadTree.getEdgePoints();

    }

    CoordinateStore calculateSurface3D() {
        PointCoordinates surface = new PointCoordinates();

        // For each slice
        for (int z:quadTrees.keySet()) {
            QuadTree silceQT       = quadTrees.get(z);
            QuadTree belowSliceQT  = quadTrees.get(z-1);
            QuadTree aboveSliceQT  = quadTrees.get(z+1);

            // Add all the edge points for the slices QuadTree factoring in the neighbouring slices
            silceQT.getEdgePoints3D(surface, belowSliceQT, aboveSliceQT, z);

        }

        return surface;

    }


    // Volume properties

    @Override
    public int size() {
        int nVoxels = 0;

        for (QuadTree quadTree:quadTrees.values()) nVoxels += quadTree.getPointCount();

        return nVoxels;

    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public long getNumberOfElements() {
        long nElements = 0;

        for (QuadTree quadTree:quadTrees.values()) nElements += quadTree.getNodeCount();

        return nElements;

    }


    // Volume access

    @Override
    public Iterator<Point<Integer>> iterator() {
        return new QuadTreeVolumeIterator();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }


    // Miscellaneous

    @Override
    public int hashCode() {
        int hash = 1;

        for (Point<Integer> point:this) hash = 31*hash + point.hashCode();

        return hash;

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof QuadtreeCoordinates)) return false;

        QuadtreeCoordinates volume2 = (QuadtreeCoordinates) obj;
        if (size() != volume2.size()) return false;

        Iterator<Point<Integer>> iterator1 = iterator();
        Iterator<Point<Integer>> iterator2 = volume2.iterator();

        while (iterator1.hasNext()) {
            Point<Integer> point1 = iterator1.next();
            Point<Integer> point2 = iterator2.next();

            if (!point1.equals(point2)) return false;

        }

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
            // Check we have an iterator
            if (quadTreeIterator == null) return false;

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
