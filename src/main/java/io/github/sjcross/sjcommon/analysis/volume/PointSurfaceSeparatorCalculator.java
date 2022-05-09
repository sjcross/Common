package io.github.sjcross.sjcommon.analysis.volume;

import java.util.Iterator;

import io.github.sjcross.sjcommon.object.Point;
import io.github.sjcross.sjcommon.object.volume.Volume;

public class PointSurfaceSeparatorCalculator {
    private final double dppXY;
    private final double minDist;
    private final Point<Integer> p1;

    public PointSurfaceSeparatorCalculator(Volume v1, Point<Double> p2) {
        this.dppXY = v1.getDppXY();

        double minDist = Double.MAX_VALUE;
        Point<Integer> p1 = null;
        boolean isInside = false;

        // If the volume is 2D, only calculate separation in XY
        boolean only2D = v1.is2D();

        Iterator<Point<Integer>> iterator1 = v1.getSurface().getCoordinateIterator();
        while (iterator1.hasNext()) {
            Point<Integer> pp1 = iterator1.next();

            double dx = pp1.x - p2.x;
            double dy = pp1.y - p2.y;

            if (only2D) {
                double dist = Math.sqrt(dx * dx + dy * dy);
                if (dist < Math.abs(minDist)) {
                    minDist = dist;
                    p1 = pp1;
    
                    // Checking if the closest point is inside the volume
                    Point<Integer> pp2 = new Point<>((int) Math.floor(p2.x), (int) Math.floor(p2.y), 0);
                    isInside = v1.contains(pp2);
                }
            } else {
                double dz = v1.getXYScaledZ(pp1.z - p2.z);
                double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                if (dist < Math.abs(minDist)) {
                    minDist = dist;
                    p1 = pp1;
    
                    // Checking if the closest point is inside the volume
                    Point<Integer> pp2 = new Point<>((int) Math.floor(p2.x), (int) Math.floor(p2.y),
                            (int) Math.floor(p2.z));
                    isInside = v1.contains(pp2);
                }
            }            
        }

        // If this point is inside the parent the distance should be negative
        if (isInside)
            minDist = -minDist;

        // Setting the optimal values
        this.minDist = minDist;
        this.p1 = p1;

    }

    public double getMinDist(boolean pixelDistances) {
        return pixelDistances ? minDist : minDist * dppXY;
    }

    public Point<Integer> getP1() {
        return p1;
    }

}
