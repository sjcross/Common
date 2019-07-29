// TODO: Change getProjectedArea to use HashSet for coordinate indices
// TODO: Should get calculateSurface methods to work for negative values too (not just ignore them)

package wbif.sjx.common.Object.Volume;

import wbif.sjx.common.Object.Point;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created by sc13967 on 28/07/2017.
 */
public class PointCoordinates implements CoordinateStore {
    protected TreeSet<Point<Integer>> points = new TreeSet<>();

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
    public boolean addAll(Collection<? extends Point<Integer>> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return points.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return points.remove(o);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        points = new TreeSet<>();
    }

    @Override
    public void finalise() {

    }


    // Creating coordinate subsets

    @Override
    public CoordinateStore calculateSurface(boolean is2D) {
        if (is2D) return calculateSurface2D();
        else return calculateSurface3D();

    }

    @Override
    public VolumeType getVolumeType() {
        return VolumeType.POINTLIST;
    }

    CoordinateStore calculateSurface2D() {
        PointCoordinates surface = new PointCoordinates();

        // Iterating over each Point, adding it if it has fewer than 6 neighbours
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

    CoordinateStore calculateSurface3D() {
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

    } // Copied

    @Override
    public boolean isEmpty() {
        return size() == 0;
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
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public void forEach(Consumer<? super Point<Integer>> action) {
        points.forEach(action);
    }

    @Override
    public Spliterator<Point<Integer>> spliterator() {
        return points.spliterator();
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
        if (!(obj instanceof PointCoordinates)) return false;

        PointCoordinates volume2 = (PointCoordinates) obj;
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