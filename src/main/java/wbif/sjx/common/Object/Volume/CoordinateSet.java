package wbif.sjx.common.Object.Volume;

import wbif.sjx.common.MathFunc.CumStat;
import wbif.sjx.common.Object.Point;

import java.util.AbstractSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class CoordinateSet extends AbstractSet<Point<Integer>> {
    public abstract boolean add(int x, int y, int z);

    public abstract void finalise();

    public abstract long getNumberOfElements();

    public abstract VolumeType getVolumeType();

    protected abstract CoordinateSet calculateProjected();

    public CoordinateSet calculateSurface(boolean is2D) {
        return is2D ? calculateSurface2D() : calculateSurface3D();
    }

    protected CoordinateSet calculateSurface2D() {
        CoordinateSet surface = new PointCoordinates();

        // Iterating over each Point, adding it if it has fewer than 4 neighbours
        for (Point<Integer> point : this) {
                if (!contains(new Point<>(point.x - 1, point.y, 0))) {
                    surface.add(point);
                    continue;
                }
                    
                if (!contains(new Point<>(point.x + 1, point.y, 0))) {
                    surface.add(point);
                    continue;
                }
                    
                if (!contains(new Point<>(point.x, point.y - 1, 0))) {
                    surface.add(point);
                    continue;
                }
                    
                if (!contains(new Point<>(point.x, point.y + 1, 0))) {
                    surface.add(point);
                    continue;
                }
                
        }

        return surface;

    }

    protected CoordinateSet calculateSurface3D() {
        CoordinateSet surface = new PointCoordinates();

        // Iterating over each Point, adding it if it has fewer than 6 neighbours
        for (Point<Integer> point : this) {
                if (!contains(new Point<>(point.x - 1, point.y, point.z))) {
                    surface.add(point);
                    continue;
                }

                if (!contains(new Point<>(point.x + 1, point.y, point.z))) {
                    surface.add(point);
                    continue;
                }
                 
                if (!contains(new Point<>(point.x, point.y - 1, point.z))) {
                    surface.add(point);
                    continue;
                }
                 
                if (!contains(new Point<>(point.x, point.y + 1, point.z))) {
                    surface.add(point);
                    continue;
                }
                 
                if (!contains(new Point<>(point.x, point.y, point.z - 1))) {
                    surface.add(point);
                    continue;
                }
                 
                if (!contains(new Point<>(point.x, point.y, point.z + 1))) {
                    surface.add(point);
                    continue;
                }
                    
        }

        return surface;

    }

    public Point<Double> calculateMeanCentroid() {
        CumStat csX = new CumStat();
        CumStat csY = new CumStat();
        CumStat csZ = new CumStat();

        for (Point<Integer> point : this) {
            csX.addMeasure(point.x);
            csY.addMeasure(point.y);
            csZ.addMeasure(point.z);
        }

        return new Point<>(csX.getMean(), csY.getMean(), csZ.getMean());

    }
}
