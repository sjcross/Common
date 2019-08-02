package wbif.sjx.common.Object.Volume;

import wbif.sjx.common.MathFunc.CumStat;
import wbif.sjx.common.Object.Point;

import java.util.Collection;

public interface CoordinateStore extends Collection<Point<Integer>> {
    public boolean add(int x, int y, int z);
    public void finalise();
    public long getNumberOfElements();
    public CoordinateStore calculateSurface(boolean is2D);
    public VolumeType getVolumeType();

    public default boolean removeAll(Collection<?> c) {
        Collection<Point<Integer>> points = (Collection<Point<Integer>>) c;
        for (Point<Integer> point:points) remove(point);
        return true;
    }

    public default CoordinateStore calculateProjected() {
        PointCoordinates projected = new PointCoordinates();

        for (Point<Integer> point:this) projected.add(point.x,point.y,0);

        return projected;

    }

    public default Point<Double> calculateMeanCentroid() {
        CumStat csX = new CumStat();
        CumStat csY = new CumStat();
        CumStat csZ = new CumStat();

        for (Point<Integer> point:this) {
            csX.addMeasure(point.x);
            csY.addMeasure(point.y);
            csZ.addMeasure(point.z);
        }

        return new Point<>(csX.getMean(),csY.getMean(),csZ.getMean());

    }

    @Override
    public default Object[] toArray() {
        Point[] array = new Point[size()];

        int i=0;
        for (Point<Integer> point:this) array[i++] = point;

        return array;

    }
}
