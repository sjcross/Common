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

    public CoordinateSet calculateSurface(boolean is2D, int nThreads) {
        return is2D ? calculateSurface2D(nThreads) : calculateSurface3D(nThreads);
    }

    public CoordinateSet calculateSurface(boolean is2D) {
        return is2D ? calculateSurface2D(1) : calculateSurface3D(1);
    }

    protected CoordinateSet calculateSurface2D(int nThreads) {
        CoordinateSet surface = new PointCoordinates();

        // ThreadPoolExecutor pool = new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
        //         new LinkedBlockingQueue<>());

        // Iterating over each Point, adding it if it has fewer than 4 neighbours
        for (Point<Integer> point : this) {
            // Runnable task = () -> {
                int count = 0;

                if (contains(new Point<>(point.x - 1, point.y, 0)))
                    count++;
                if (contains(new Point<>(point.x + 1, point.y, 0)))
                    count++;
                if (contains(new Point<>(point.x, point.y - 1, 0)))
                    count++;
                if (contains(new Point<>(point.x, point.y + 1, 0)))
                    count++;

                if (count < 4)
                    surface.add(point);
            // };
            // pool.submit(task);
        }

        // pool.shutdown();
        // try {
        //     pool.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS); // i.e. never terminate early
        // } catch (InterruptedException e) {
        //     e.printStackTrace(System.err);
        // }

        return surface;

    }

    protected CoordinateSet calculateSurface3D(int nThreads) {
        CoordinateSet surface = new PointCoordinates();

        ThreadPoolExecutor pool = new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());

        // Iterating over each Point, adding it if it has fewer than 6 neighbours
        for (Point<Integer> point : this) {
            Runnable task = () -> {
                int count = 0;

                if (contains(new Point<>(point.x - 1, point.y, point.z)))
                    count++;
                if (contains(new Point<>(point.x + 1, point.y, point.z)))
                    count++;
                if (contains(new Point<>(point.x, point.y - 1, point.z)))
                    count++;
                if (contains(new Point<>(point.x, point.y + 1, point.z)))
                    count++;
                if (contains(new Point<>(point.x, point.y, point.z - 1)))
                    count++;
                if (contains(new Point<>(point.x, point.y, point.z + 1)))
                    count++;

                if (count < 6)
                    surface.add(point);
            };
            pool.submit(task);
        }

        pool.shutdown();

        try {
            pool.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS); // i.e. never terminate early
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);
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
