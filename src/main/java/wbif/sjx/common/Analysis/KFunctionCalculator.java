package wbif.sjx.common.Analysis;

import wbif.sjx.common.Object.Point;

import java.util.ArrayList;
import java.util.TreeMap;

public class KFunctionCalculator {
    private final TreeMap<Double,Double> kFunction = new TreeMap<>();


    public KFunctionCalculator(ArrayList<Point<Double>> points, int nBins, boolean is2D) {
        int N = points.size();
        double regionSize = calculateRegionSize(points,is2D);
        double correctionFactor = regionSize/(N*N);
        double stepSize = calculateStepSize(regionSize,nBins,is2D);
        double maxSep = calculateMaximumPointSeparation(points);

        for (int i=0;i<nBins;i++) {
            double ts = stepSize*(i+1);
            int count = 0;
            for (Point<Double> point1 : points) {
                for (Point<Double> point2 : points) {
                    if (point1 == point2) continue;
                    double dist = point1.calculateDistanceToPoint(point2);

                    // If the other point is within ts of the central point, increment the relevant counter
                    if (dist < ts) count++;

                }
            }

            // Applying K-function correction
            double kVal = correctionFactor*count;
            kFunction.put(ts,kVal);

        }
    }

    public static double calculateMaximumPointSeparation(ArrayList<Point<Double>> points) {
        double maxDist = 0;
        for (Point<Double> point1:points) {
            for (Point<Double> point2:points) {
                maxDist = Math.max(maxDist,point1.calculateDistanceToPoint(point2));
            }
        }

        return maxDist;

    }

    public static double calculateRegionSize(ArrayList<Point<Double>> points, boolean is2D) {
        double minX = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;
        double minZ = Double.MAX_VALUE;
        double maxZ = -Double.MAX_VALUE;

        for (Point<Double> point:points) {
            minX = Math.min(minX,point.getX());
            maxX = Math.max(maxX,point.getX());
            minY = Math.min(minY,point.getY());
            maxY = Math.max(maxY,point.getY());
            minZ = Math.min(minZ,point.getZ());
            maxZ = Math.max(maxZ,point.getZ());
        }

        double dX = maxX-minX;
        double dY = maxY-minY;
        double dZ = maxZ-minZ;

        if (is2D) return dX*dY;
        else return dX*dY*dZ;

    }

    public static double calculateStepSize(double regionSize, int nBins, boolean is2D) {
        if (is2D) {
            return (Math.sqrt(regionSize)/3)/nBins;
        } else {
            return (Math.cbrt(regionSize)/3)/nBins;
        }
    }

    public TreeMap<Double,Double> getKFunction() {
        return kFunction;
    }

    public TreeMap<Double,Double> getLFunction() {
        TreeMap<Double,Double> lFunction = new TreeMap<>();

        for (double ts:kFunction.keySet()) {
            double kVal = kFunction.get(ts);
            double lVal = Math.sqrt(kVal/Math.PI)-ts;
            lFunction.put(ts,kVal);
        }

        return lFunction;

    }
}
