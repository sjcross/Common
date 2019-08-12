// TODO: Change getProjectedArea to use HashSet for coordinate indices

package wbif.sjx.common.Object.Volume;

import wbif.sjx.common.Object.Point;
import wbif.sjx.common.Object.QuadTree.OcTree;

import java.util.*;

/**
 * Created by sc13967 on 28/07/2017.
 */
public class OctreeCoordinates extends CoordinateSet {
    private final OcTree ocTree;

    public OctreeCoordinates() {
        ocTree = new OcTree();
    }

    @Override
    public VolumeType getVolumeType() {
        return VolumeType.OCTREE;
    }

    // Adding and removing points

    @Override
    public boolean add(int x, int y, int z) {
        ocTree.add(x, y, z);

        return true;
    }

    @Override
    public boolean add(Point<Integer> point) {
        return add(point.x,point.y,point.z);
    }

    @Override
    public boolean contains(Object o) {
        Point<Integer> point = (Point<Integer>) o;

        return ocTree.contains(point.x, point.y, point.z);
    }

    @Override
    public boolean remove(Object o) {
        Point<Integer> point = (Point<Integer>) o;
        ocTree.remove(point.x,point.y,point.z);

        return true;
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

    protected CoordinateSet calculateSurface2D() {
        return ocTree.getEdgePoints(true);
    }

    protected CoordinateSet calculateSurface3D() {
        return ocTree.getEdgePoints(false);
    }


    // Volume properties

    @Override
    public int size() {
        return ocTree.size();
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
}
