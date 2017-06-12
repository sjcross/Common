package wbif.sjx.common.MathFunc;

import java.util.HashMap;
import java.util.LinkedHashMap;

// THIS CLASS IS BASED ON THE INCREMENTAL CALCULATION OF WEIGHTED MEAN AND VARIANCE FROM http://www-uxsup.csx.cam.ac.uk/~fanf2/hermes/doc/antiforgery/stats.pdf (Accessed 30-06-2016)
// It gives the population standard deviation (normalised by N rather than N-1 as you get with the sample standard deviation)

public class CumStat {
    public static final int IGNOREZEROS = 0;
    public static final int POPULATION = 1;
    public static final int SAMPLE = 2;

    private LinkedHashMap<Integer, CSPoint> points;

    public CumStat(int len) {
        points = new HashMap<>(len);

    }

    /**
     * Adding values to each element
     * @param xIn
     */
    public synchronized void addMeasure(double[] xIn) { //Non-weighted vector
        for (int i = 0; i < xIn.length; i++) {
            CSPoint point = points.get(i);
            if (!Double.isNaN(xIn[i])) {
                point.n++;
                point.xSum = point.xSum + xIn[i];
                point.wSum = point.wSum + 1;
                double x_mean_prev = point.xMean;
                point.xMean = point.xMean + (1 / point.wSum) * (xIn[i] - point.xMean);
                point.S = point.S + (xIn[i] - x_mean_prev) * (xIn[i] - point.xMean);
                point.xVarPop = point.S / point.wSum;
                point.xVarSamp = point.S / (point.wSum - 1);

                if (xIn[i] < point.xMin) {
                    point.xMin = xIn[i];
                }

                if (xIn[i] > point.xMax) {
                    point.xMax = xIn[i];
                }
            }
        }
    }

    public synchronized void addMeasure(double[] xIn, int opt) {
        for (int i = 0; i < xIn.length; i++) {
            if (opt == IGNOREZEROS & xIn[i] == 0) {
                xIn[i] = Double.NaN;
            }
        }
        addMeasure(xIn);

    }

    public synchronized void addSingleMeasure(int i, double xIn) {
        CSPoint point = points.get(i);
        if (!Double.isNaN(xIn)) {
            point.n++;
            point.xSum = point.xSum + xIn;
            point.wSum = point.wSum + 1;
            double x_mean_prev = point.xMean;
            point.xMean = point.xMean + (1 / point.wSum) * (xIn - point.xMean);
            point.S = point.S + (xIn - x_mean_prev) * (xIn - point.xMean);
            point.xVarPop = point.S / point.wSum;
            point.xVarSamp = point.S / (point.wSum - 1);

            if (xIn < point.xMin) {
                point.xMin = xIn;
            }

            if (xIn > point.xMax) {
                point.xMax = xIn;
            }
        }
    }

    public synchronized void addMeasure(double xIn) { //Non-weighted value
        addSingleMeasure(0,xIn);

    }

    public synchronized void addMeasure(double[] xIn, double[] w) {//Weighted vector
        for (int i = 0; i < xIn.length; i++) {
            CSPoint point = points.get(i);

            if (!Double.isNaN(xIn[i])) {
                if (w[i] != 0) {
                    point.n++;
                    point.xSum = point.xSum + xIn[i];
                    point.wSum = point.wSum + w[i];
                    double x_mean_prev = point.xMean;
                    point.xMean = point.xMean + (w[i] / point.wSum) * (xIn[i] - point.xMean);
                    point.S = point.S + w[i] * (xIn[i] - x_mean_prev) * (xIn[i] - point.xMean);
                    point.xVarPop = point.S / point.wSum;
                    point.xVarSamp = point.S / (point.wSum - 1);

                    if (xIn[i] < point.xMin) {
                        point.xMin = xIn[i];
                    }

                    if (xIn[i] > point.xMax) {
                        point.xMax = xIn[i];
                    }
                }
            }
        }
    }

    public synchronized void addMeasure(double[] xIn, double[] w, int opt) {
        for (int i = 0; i < xIn.length; i++) {
            if (opt == IGNOREZEROS & xIn[i] == 0) {
                xIn[i] = Double.NaN;
            }
        }
        addMeasure(xIn, w);
    }

    public synchronized void addMeasure(double xIn, double w) {//Weighted value
        addMeasure(new double[]{xIn}, new double[]{w});

    }

    /**
     * Add multiple measurements to a single cumulative value.  Different from addMeasure, which adds a single value to
     * each element of a cumulative array.
     */
    public synchronized void addMeasures(double[] xIn) {
        for (int i = 0; i < xIn.length; i++) {
            addMeasure(xIn[i]);
        }
    }

    /**
     * Add multiple measurements with weighting to a single cumulative value.  Different from addMeasure, which adds a single value to each element of a cumulative array.
     */
    public synchronized void addMeasures(double[] xIn, double[] w) {
        for (int i = 0; i < xIn.length; i++) {
            addMeasure(xIn[i], w[i]);
        }
    }

    public synchronized int[] getKeys() {
        int[] keys = new int[points.size()];
        int i = 0;
        for (int key:points.keySet()) {
            keys[i++] = key;
        }

        return keys;

    }

    public synchronized double[] getMean() {
        double[] xMean = new double[points.size()];
        int i = 0;
        for (int key:points.keySet()) {
            xMean[i++] = points.get(key).xMean;
        }

        return xMean;
    }

    public synchronized double[] getSum() {
        double[] xSum = new double[points.size()];
        int i = 0;
        for (int key:points.keySet()) {
            xSum[i++] = points.get(key).xSum;
        }

        return xSum;
    }

    /**
     * Returns the sample variance
     *
     * @return
     */
    public synchronized double[] getVar() {
        return xVarSamp;
    }

    /**
     * Returns the variance specified by the argument
     *
     * @param mode
     * @return
     */
    public synchronized double[] getVar(int mode) {
        if (mode == SAMPLE) {
            return xVarSamp;

        } else if (mode == POPULATION) {
            return xVarPop;

        }

        return new double[]{0};

    }

    /**
     * Returns the sample standard deviation.
     *
     * @return
     */
    public synchronized double[] getStd() {
        return getStd(SAMPLE);
    }

    /**
     * Returns the standard deviation specified by the argument.
     *
     * @param mode
     * @return
     */
    public synchronized double[] getStd(int mode) {
        if (mode == SAMPLE) {
            double[] x_std = new double[xVarSamp.length];
            for (int i = 0; i < xVarSamp.length; i++) {
                x_std[i] = Math.sqrt(xVarSamp[i]);
            }
            return x_std;

        } else if (mode == POPULATION) {
            double[] x_std = new double[xVarPop.length];
            for (int i = 0; i < xVarPop.length; i++) {
                x_std[i] = Math.sqrt(xVarPop[i]);
            }
            return x_std;

        }

        return new double[]{0};

    }

    public synchronized double[] getN() {
        return n;
    }

    public synchronized double[] getMin() {
        return xMin;
    }

    public synchronized double[] getMax() {
        return xMax;
    }

    public synchronized double[] getWeight() {
        return wSum;

    }

}
class CSPoint {
    double xMean = 0;
    double xVarPop = 0; // Population variance
    double xVarSamp = 0; // Sample variance
    double n = 0;
    double wSum = 0;
    double S = 0;
    double xSum = 0;
    double xMin = 0;
    double xMax = 0;

    CSPoint() {
        xMin = Double.POSITIVE_INFINITY;
        xMax = Double.NEGATIVE_INFINITY;

    }

}
