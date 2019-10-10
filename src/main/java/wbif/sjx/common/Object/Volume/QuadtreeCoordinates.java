// TODO: Change getProjectedArea to use HashSet for coordinate indices

package wbif.sjx.common.Object.Volume;

import wbif.sjx.common.Object.Point;
import wbif.sjx.common.Object.QuadTree.QuadTree;

import java.util.*;

/**
 * Created by sc13967 on 28/07/2017.
 */
public class QuadtreeCoordinates extends CoordinateSet {
    private final Map<Integer, QuadTree> quadTrees;

    public QuadtreeCoordinates() {
        quadTrees = new TreeMap<>();
    }

    @Override
    public VolumeType getVolumeType() {
        return VolumeType.QUADTREE;
    }

    // Adding and removing points

    @Override
    public boolean add(int x, int y, int z) {
        quadTrees.putIfAbsent(z,new QuadTree());

        // Get relevant QuadTree
        QuadTree quadTree = quadTrees.get(z);

        // Adding this point
        quadTree.add(x, y);

        return true;
    }

    @Override
    public boolean add(Point<Integer> point) {
        return add(point.x,point.y,point.z);
    }

    @Override
    public boolean contains(Object o) {
        Point<Integer> point = (Point<Integer>) o;

        if (!quadTrees.containsKey(point.z)) return false;
        return quadTrees.get(point.z).contains(point.x,point.y);
    }

    @Override
    public boolean remove(Object o) {
        Point<Integer> point = (Point<Integer>) o;

        if (!quadTrees.containsKey(point.z)) return false;
        quadTrees.get(point.z).remove(point.x,point.y);

        return true;
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

    protected CoordinateSet calculateProjected() {
        CoordinateSet projectedCoordinates = new QuadtreeCoordinates();

        for (Point<Integer> point:this) projectedCoordinates.add(point.x,point.y,0);

        return projectedCoordinates;

    }


    // Volume properties

    @Override
    public int size() {
        int nVoxels = 0;

        for (QuadTree quadTree:quadTrees.values()) nVoxels += quadTree.size();

        return nVoxels;
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


    // Miscellaneous

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
