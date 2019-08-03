// TODO: Change getProjectedArea to use HashSet for coordinate indices

package wbif.sjx.common.Object.Volume;

import wbif.sjx.common.Object.Point;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created by sc13967 on 28/07/2017.
 */
public class PointCoordinates extends CoordinateStore {
    private final TreeSet<Point<Integer>> points;

    @Override
    public VolumeType getVolumeType() {
        return VolumeType.POINTLIST;
    }

    public PointCoordinates() {
        points = new TreeSet<>();
    }

    // Adding and removing points

    @Override
    public boolean add(int x, int y, int z) {
        if (points.size() == Integer.MAX_VALUE) return false; //throw new IntegerOverflowException("Object too large (Integer overflow).");
        points.add(new Point<>(x,y,z));
        return true;
    }

    @Override
    public boolean add(Point<Integer> point) {
        return add(point.x,point.y,point.z);
    }

    @Override
    public boolean contains(Object o) {
        return points.contains(o);
    }

    @Override
    public boolean remove(Object o) {
        return points.remove(o);
    }

    @Override
    public void clear() {
        points.clear();
    }

    @Override
    public void finalise() {}

    // Creating coordinate subsets


    @Override
    protected CoordinateStore calculateSurface2D() {
        PointCoordinates surface = new PointCoordinates();

        // Iterating over each Point, adding it if it has fewer than 4 neighbours
        for (Point<Integer> point:points) {
            int count = 0;

            if (contains(new Point<>(point.x-1,point.y,0))) count++;
            if (contains(new Point<>(point.x+1,point.y,0))) count++;
            if (contains(new Point<>(point.x,point.y-1,0))) count++;
            if (contains(new Point<>(point.x,point.y+1,0))) count++;

            if (count < 4) surface.add(point);

        }

        return surface;
    }

    @Override
    protected CoordinateStore calculateSurface3D() {
        PointCoordinates surface = new PointCoordinates();

        // Iterating over each Point, adding it if it has fewer than 6 neighbours
        for (Point<Integer> point:points) {
            int count = 0;

            if (contains(new Point<>(point.x-1,point.y,point.z))) count++;
            if (contains(new Point<>(point.x+1,point.y,point.z))) count++;
            if (contains(new Point<>(point.x,point.y-1,point.z))) count++;
            if (contains(new Point<>(point.x,point.y+1,point.z))) count++;
            if (contains(new Point<>(point.x,point.y,point.z-1))) count++;
            if (contains(new Point<>(point.x,point.y,point.z+1))) count++;

            if (count < 6) surface.add(point);
        }

        return surface;
    }


    // Volume properties

    @Override
    public int size() {
        return points.size();
    }

    @Override
    public long getNumberOfElements() {
        return points.size();
    }


    // Volume access

    @Override
    public Iterator<Point<Integer>> iterator() {
        return points.iterator();
    }

    @Override
    public void forEach(Consumer<? super Point<Integer>> action) {
        points.forEach(action);
    }

    @Override
    public Spliterator<Point<Integer>> spliterator() {
        return points.spliterator();
    }
}