package wbif.sjx.common.Analysis.Volume;

import wbif.sjx.common.Object.Point;
import wbif.sjx.common.Object.Volume;

public class SurfaceSeparationCalculator {
    private final double minDist;
    private final Point<Integer> p1;
    private final Point<Integer> p2;

    public SurfaceSeparationCalculator(Volume v1, Volume v2, boolean pixelDistances) {
        double minDist = Double.MAX_VALUE;
        Point<Integer> p1 = null;
        Point<Integer> p2 = null;

        // Getting coordinates for the surface points (6-way connectivity)
        double[] x1 = v1.getSurfaceX(pixelDistances);
        double[] y1 = v1.getSurfaceY(pixelDistances);
        double[] z1 = v1.getSurfaceZ(pixelDistances, true);
        double[] z1Slice = v1.getSurfaceZ(true, false);

        double[] x2 = v2.getSurfaceX(pixelDistances);
        double[] y2 = v2.getSurfaceY(pixelDistances);
        double[] z2 = v2.getSurfaceZ(pixelDistances, true);
        double[] z2Slice = v2.getSurfaceZ(true, false);

        // Measuring point-to-point distances on both object surfaces
        for (int j = 0; j < x2.length; j++) {
            Point<Integer> currentPoint2 = new Point<>((int) x2[j], (int) y2[j], (int) z2Slice[j]);
            boolean isInside = false;
            for (int i = 0; i < x1.length; i++) {
                double xDist = x2[j] - x1[i];
                double yDist = y2[j] - y1[i];
                double zDist = z2[j] - z1[i];
                double dist = Math.sqrt(xDist * xDist + yDist * yDist + zDist * zDist);

                if (dist < Math.abs(minDist)) {
                    minDist = dist;
                    p1 = new Point<Integer>((int) x1[i],(int) y1[i],(int) z1Slice[i]);
                    p2 = new Point<Integer>((int) x2[j],(int) y2[j],(int) z2Slice[j]);

                    isInside = v1.getPoints().contains(currentPoint2);
                    if (!isInside) {
                        Point<Integer> currentPoint1 = new Point<>((int) x1[i], (int) y1[i], (int) z1Slice[i]);
                        isInside = v2.getPoints().contains(currentPoint1);
                    }
                }
            }

            // If this point is inside the parent the distance should be negative
            if (isInside) minDist = -minDist;

        }

        // Setting the optimal values
        this.minDist = minDist;
        this.p1 = p1;
        this.p2 = p2;

    }

    public double getMinDist() {
        return minDist;
    }

    public Point<Integer> getP1() {
        return p1;
    }

    public Point<Integer> getP2() {
        return p2;
    }
}
