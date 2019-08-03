package wbif.sjx.common.Object.Volume;

import wbif.sjx.common.MathFunc.CumStat;
import wbif.sjx.common.Object.Point;

import java.util.AbstractSet;

public abstract class CoordinateStore extends AbstractSet<Point<Integer>> {
    public abstract boolean add(int x, int y, int z);
    public abstract void finalise();
    public abstract long getNumberOfElements();
    public abstract VolumeType getVolumeType();
    protected abstract CoordinateStore calculateSurface2D();
    protected abstract CoordinateStore calculateSurface3D();

    public CoordinateStore calculateSurface(boolean is2D) {
        return is2D ? calculateSurface2D() : calculateSurface3D();

    }

    public CoordinateStore calculateProjected() {
        PointCoordinates projected = new PointCoordinates();

        for (Point<Integer> point:this) projected.add(point.x,point.y,0);

        return projected;

    }

    public Point<Double> calculateMeanCentroid() {
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
}
