package wbif.sjx.common.Object;

import ij.ImagePlus;
import wbif.sjx.common.Analysis.*;
import wbif.sjx.common.MathFunc.CumStat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.stream.IntStream;

/**
 * Created by sc13967 on 03/02/2017.
 */
public class Track extends TreeMap<Integer,Point> {
    private double distXY = 1;
    private double distZ = 1;
    private String units = "px";

    // CONSTRUCTORS
    public Track() {

    }

    public Track(double distXY, double distZ, String units) {
        this.distXY = distXY;
        this.distZ = distZ;
        this.units = units;

    }

    public Track(double[] x, double[] y, double[] z, int[] f) {
        for (int i=0;i<x.length;i++) {
            put(f[i],new Point(x[i],y[i],z[i],f[i]));

        }
    }

    public Track(double[] x, double[] y, double[] z, int[] f, double distXY, double distZ, String unitsXY) {
        this.distXY = distXY;
        this.distZ = distZ;
        this.units = unitsXY;

        for (int i=0;i<x.length;i++) {
            put(f[i],new Point(x[i],y[i],z[i],f[i]));

        }
    }

    public Track(ArrayList<Double> x, ArrayList<Double> y, ArrayList<Double> z, ArrayList<Integer> f) {
        for (int i=0;i<x.size();i++) {
            put(f.get(i),new Point(x.get(i),y.get(i),z.get(i),f.get(i)));

        }
    }

    public Track(ArrayList<Double> x, ArrayList<Double> y, ArrayList<Double> z, ArrayList<Integer> f, double distXY, double distZ, String unitsXY) {
        this.distXY = distXY;
        this.distZ = distZ;
        this.units = unitsXY;

        for (int i=0;i<x.size();i++) {
            put(f.get(i),new Point(x.get(i),y.get(i),z.get(i),f.get(i)));

        }
    }


    // PUBLIC METHODS

    /**
     *
     * @return mean position as double[][]{meanX,meanY,meanZ}{stdevX,stdevY,stdevZ}
     */
    public double[][] getMeanPosition(boolean pixelDistances) {
        CumStat csX = new CumStat(getX(pixelDistances));
        CumStat csY = new CumStat(getY(pixelDistances));
        CumStat csZ = new CumStat(getZ(pixelDistances));

        return new double[][]{{csX.getMean(),csY.getMean(),csZ.getMean()},
                {csX.getStd(),csY.getStd(),csZ.getStd()}};

    }

    public CumStat[] getDirectionalPersistence(boolean pixelDistances) {
        return DirectionalPersistenceCalculator.calculate(getF(),getX(pixelDistances),getY(pixelDistances),getZ(pixelDistances));

    }

    public CumStat[] getMSD(boolean pixelDistances) {
        return MSDCalculator.calculate(getF(),getX(pixelDistances),getY(pixelDistances),getZ(pixelDistances));

    }

    public double[] getMSDLinearFit(boolean pixelDistances, int nPoints) {
        CumStat[] cs = MSDCalculator.calculate(getF(),getX(pixelDistances),getY(pixelDistances),getZ(pixelDistances));

        double[] df = new double[cs.length];
        for (int i=0;i<cs.length;i++) {
            df[i] = i;
        }
        double[] MSD = Arrays.stream(cs).mapToDouble(CumStat::getMean).toArray();

        return MSDCalculator.getLinearFit(df,MSD,nPoints);

    }

    public double[] getInstantaneousVelocity(boolean pixelDistances) {
        return InstantaneousVelocityCalculator.calculate(getF(),getX(pixelDistances),getY(pixelDistances),getZ(pixelDistances));

    }

    public double[] getStepSizes(boolean pixelDistances) {
        return StepSizeCalculator.calculate(getX(pixelDistances),getY(pixelDistances),getZ(pixelDistances));

    }

    public double getEuclideanDistance(boolean pixelDistances) {
        double[] x = getX(pixelDistances);
        double[] y = getY(pixelDistances);
        double[] z = getZ(pixelDistances);

        double dx = x[x.length-1]-x[0];
        double dy = y[x.length-1]-y[0];
        double dz = z[x.length-1]-z[0];

        return Math.sqrt(dx * dx + dy * dy + dz * dz);

    }

    public double getTotalPathLength(boolean pixelDistances) {
        double[] steps = getStepSizes(pixelDistances);

        return Arrays.stream(steps).sum();

    }

    public double getDirectionalityRatio(boolean pixelDistances) {
        return getEuclideanDistance(pixelDistances)/getTotalPathLength(pixelDistances);

    }

    /**
     * Returns a double[] containing the Euclidean distance at all time steps
     */
    public double[] getRollingEuclideanDistance(boolean pixelDistances) {
        return EuclideanDistanceCalculator.calculate(getX(pixelDistances),getY(pixelDistances),getZ(pixelDistances));

    }

    /**
     * Returns a double[] containing the total path length up to each time step
     */
    public double[] getRollingTotalPathLength(boolean pixelDistances) {
        return TotalPathLengthCalculator.calculate(getX(pixelDistances),getY(pixelDistances),getZ(pixelDistances));

    }

    public double[] getRollingDirectionalityRatio(boolean pixelDistances) {
        return DirectionalityRatioCalculator.calculate(getX(pixelDistances),getY(pixelDistances),getZ(pixelDistances));

    }

    public int getDuration() {
        int[] f = getF();

        return f[f.length-1]-f[0];

    }

    public double[][] getLimits(boolean pixelDistances){
        double[] x = getX(pixelDistances);
        double[] y = getY(pixelDistances);
        double[] z = getZ(pixelDistances);
        int[] f = getF();

        double[][] limits = new double[4][2];
        for (double[] row:limits) {
            row[0] = Double.MAX_VALUE;
            row[1] = Double.MIN_VALUE;

        }

        for (int i=0;i<x.length;i++) {
            if (x[i] < limits[0][0]) limits[0][0] = x[i];
            if (x[i] > limits[0][1]) limits[0][1] = x[i];
            if (y[i] < limits[1][0]) limits[1][0] = y[i];
            if (y[i] > limits[1][1]) limits[1][1] = y[i];
            if (z[i] < limits[2][0]) limits[2][0] = z[i];
            if (z[i] > limits[2][1]) limits[2][1] = z[i];
            if (f[i] < limits[3][0]) limits[3][0] = f[i];
            if (f[i] > limits[3][1]) limits[3][1] = f[i];

        }

        return limits;

    }

    public double[] getRollingIntensity(ImagePlus ipl, int radius) {
        double[] x = getX(true);
        double[] y = getY(true);
        double[] z = getZ(true);

        int[] f = getF();
        double[] intensity = new double[x.length];

        for (int i=0;i<x.length;i++) {
            ipl.setPosition(0,(int) Math.round(z[i])+1,f[i]+1);

            SpotIntensity spotIntensity = new SpotIntensity(ipl.getProcessor(),x[i],y[i],radius);

            intensity[i] = spotIntensity.getMeanPointIntensity();

        }

        return intensity;

    }


    // GETTERS AND SETTERS

    public double[] getX(boolean pixelDistances) {
        double[] x = values().stream().mapToDouble(Point::getX).toArray();

        if (pixelDistances) IntStream.range(0,x.length).forEach(i -> x[i] = x[i]/distXY);

        return x;

    }

    public double[] getY(boolean pixelDistances) {
        double[] y = values().stream().mapToDouble(Point::getY).toArray();

        if (pixelDistances) IntStream.range(0,y.length).forEach(i -> y[i] = y[i]/distXY);

        return y;

    }

    public double[] getZ(boolean pixelDistances) {
        double[] z = values().stream().mapToDouble(Point::getZ).toArray();

        if (pixelDistances) IntStream.range(0,z.length).forEach(i -> z[i] = z[i]/distZ);

        return z;

    }

    public int[] getF() {
        return values().stream().mapToInt(Point::getF).toArray();

    }

    public Point getPointAtFrame(int frame) {
        return get(frame);

    }

    public double[] getFAsDouble() {
        return values().stream().mapToDouble(Point::getF).toArray();
    }

    public double getDistXY() {
        return distXY;
    }

    public double getDistZ() {
        return distZ;
    }

    public String getUnits(boolean pixelDistances) {
        return pixelDistances ? "px" : units;
    }
}