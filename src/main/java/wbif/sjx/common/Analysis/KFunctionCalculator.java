package wbif.sjx.common.Analysis;

import wbif.sjx.common.MathFunc.GoreaudEdgeCorrection;
import wbif.sjx.common.Object.Point;

import java.util.ArrayList;
import java.util.TreeMap;

public class KFunctionCalculator {
    private final TreeMap<Double,Double> kFunction = new TreeMap<>();

    public KFunctionCalculator(ArrayList<Point<Double>> points, int nBins, boolean is2D, boolean edgeCorrection) {
        int N = points.size();
        double regionSize = calculateRegionSize(points,is2D);
        double areaFactor = regionSize/(N*N);
        double stepSize = calculateStepSize(regionSize,nBins,is2D);
        double maxSep = calculateMaximumPointSeparation(points);

        GoreaudEdgeCorrection correctionCalculator = null;
        if (edgeCorrection) {
            double[][] limits = calculateRegionLimits(points);
            correctionCalculator = new GoreaudEdgeCorrection(limits[0][0],limits[0][1],limits[1][0],limits[1][1]);
        }

        for (int i=0;i<nBins;i++) {
            double ts = stepSize*(i+1);
            double score = 0;
            for (Point<Double> point1 : points) {
                double correction = edgeCorrection ? correctionCalculator.getFractionInsideRectangle(point1.getX(),point1.getY(),ts) : 1;

                for (Point<Double> point2 : points) {
                    if (point1 == point2) continue;
                    double dist = point1.calculateDistanceToPoint(point2);

                    // If the other point is within ts of the central point, increment the relevant counter
                    if (dist < ts) score = score + correction;

                }
            }

            // Applying K-function correction
            double kVal = areaFactor*score;
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

    public static double[][] calculateRegionLimits(ArrayList<Point<Double>> points) {
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

        return new double[][]{{minX,maxX},{minY,maxY},{minZ,maxZ}};

    }

    public static double calculateRegionSize(ArrayList<Point<Double>> points, boolean is2D) {
        double[][] limits = calculateRegionLimits(points);

        double dX = limits[0][1]-limits[0][0];
        double dY = limits[1][1]-limits[1][0];
        double dZ = limits[2][1]-limits[2][0];

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
            lFunction.put(ts,lVal);
        }

        return lFunction;

    }
}
