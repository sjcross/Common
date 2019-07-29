// TODO: Change getProjectedArea to use HashSet for coordinate indices
// TODO: Should get calculateSurface methods to work for negative values too (not just ignore them)

package wbif.sjx.common.Object.Volume;

import wbif.sjx.common.Object.Point;
import wbif.sjx.common.Object.QuadTree.OcTree;

import java.util.*;

/**
 * Created by sc13967 on 28/07/2017.
 */
public class OctreeCoordinates implements CoordinateStore {
    private final OcTree ocTree;
    private final int width;
    private final int height;
    private final int nSlices;

    public OctreeCoordinates(int width, int height, int nSlices) {
        this.width = width;
        this.height = height;
        this.nSlices = nSlices;

        ocTree = new OcTree(width,height,nSlices);

    }

    // Adding and removing points

    @Override
    public boolean add(int x, int y, int z) {
        // Adding this point
        ocTree.add(x, y, z);

//        ocTree.optimise();

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

        return ocTree.contains(point.x, point.y, point.z);

    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        Point<Integer> point = (Point<Integer>) o;
        ocTree.remove(point.x,point.y,point.z);

        return true;

    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        ocTree.clear();
    }

    @Override
    public void finalise() {
        ocTree.optimise();
    }


    // Creating coordinate subsets

    @Override
    public CoordinateStore calculateSurface(boolean is2D) {
        if (is2D) return calculateSurface2D();
        else return calculateSurface3D();

    }

    CoordinateStore calculateSurface2D() {
        System.out.println("Need to implement 2D surface in OctreeVolume");
        return null;
    }

    CoordinateStore calculateSurface3D() {
        return ocTree.getEdgePoints();

    }


    // Volume properties

    @Override
    public int size() {
        return ocTree.getPointCount();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public long getNumberOfElements() {
        return ocTree.getNodeCount();
    }


    // Volume access

    @Override
    public Iterator<Point<Integer>> iterator() {
        return ocTree.iterator();
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
        if (!(obj instanceof OctreeCoordinates)) return false;

        OctreeCoordinates volume2 = (OctreeCoordinates) obj;
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
}
