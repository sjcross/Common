package wbif.sjx.common.Object.Volume;

import wbif.sjx.common.MathFunc.CumStat;
import wbif.sjx.common.Object.Point;

import java.util.AbstractSet;

public abstract class CoordinateSet extends AbstractSet<Point<Integer>> {
    public abstract boolean add(int x, int y, int z);
    public abstract void finalise();
    public abstract long getNumberOfElements();
    public abstract VolumeType getVolumeType();
    protected abstract CoordinateSet calculateProjected();
    protected abstract CoordinateSet calculateSurface2D();
    protected abstract CoordinateSet calculateSurface3D();

    public CoordinateSet calculateSurface(boolean is2D) {
        return is2D ? calculateSurface2D() : calculateSurface3D();

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
