package wbif.sjx.common.MathFunc;

import java.util.Arrays;

// THIS CLASS IS BASED ON THE INCREMENTAL CALCULATION OF WEIGHTED MEAN AND VARIANCE FROM http://www-uxsup.csx.cam.ac.uk/~fanf2/hermes/doc/antiforgery/stats.pdf (Accessed 30-06-2016)
// It gives the population standard deviation (normalised by N rather than N-1 as you get with the sample standard deviation)

public class CumStat {
    public static final int IGNOREZEROS = 0;
    public static final int POPULATION = 1;
    public static final int SAMPLE = 2;

    private double[] xMean;
    private double[] xVarPop; // Population variance
    private double[] xVarSamp; // Sample variance
    private double[] n;
    private double[] wSum;
    private double[] S;
    private double[] xSum;
    private double[] xMin;
    private double[] xMax;

    public CumStat(int len) {
        xMean = new double[len];
        xVarPop = new double[len];
        xVarSamp = new double[len];
        n = new double[len];
        wSum = new double[len];
        S = new double[len];
        xSum = new double[len];
        xMin = new double[len];
        Arrays.fill(xMin, Double.POSITIVE_INFINITY);
        xMax = new double[len];
        Arrays.fill(xMax, Double.NEGATIVE_INFINITY);
    }

    public synchronized void addMeasure(double[] xIn) { //Non-weighted vector
        for (int i=0;i<xIn.length;i++) {
            if (!Double.isNaN(xIn[i])) {
                n[i] ++;
                xSum[i] = xSum[i] + xIn[i];
                wSum[i] = wSum[i] + 1;
                double x_mean_prev = xMean[i];
                xMean[i] = xMean[i] + (1/ wSum[i])*(xIn[i]- xMean[i]);
                S[i] = S[i]+(xIn[i]-x_mean_prev)*(xIn[i]- xMean[i]);
                xVarPop[i] = S[i]/ wSum[i];
                xVarSamp[i] = S[i]/(wSum[i]-1);

                if (xIn[i] < xMin[i]) {
                    xMin[i] = xIn[i];
                }

                if (xIn[i] > xMax[i]) {
                    xMax[i] = xIn[i];
                }
            }
        }
    }

    public synchronized void addMeasure(double[] xIn, int opt) {
        for (int i = 0; i< xMean.length; i++) {
            if (opt == IGNOREZEROS & xIn[i] == 0) {
                xIn[i] = Double.NaN;
            }
        }
        addMeasure(xIn);
    }

    public synchronized void addSingleMeasure(int i, double xIn) {
        if (!Double.isNaN(xIn)) {
            n[i] ++;
            xSum[i] = xSum[i] + xIn;
            wSum[i] = wSum[i] + 1;
            double x_mean_prev = xMean[i];
            xMean[i] = xMean[i] + (1/ wSum[i])*(xIn- xMean[i]);
            S[i] = S[i]+(xIn-x_mean_prev)*(xIn- xMean[i]);
            xVarPop[i] = S[i]/ wSum[i];
            xVarSamp[i] = S[i]/(wSum[i]-1);

            if (xIn < xMin[i]) {
                xMin[i] = xIn;
            }

            if (xIn > xMax[i]) {
                xMax[i] = xIn;
            }
        }
    }

    public synchronized void addMeasure(double xIn) { //Non-weighted value
        if (!Double.isNaN(xIn)) {
            n[0] ++;
            xSum[0] = xSum[0] + xIn;
            wSum[0] = wSum[0] + 1;
            double x_mean_prev = xMean[0];
            xMean[0] = xMean[0] + (1/ wSum[0])*(xIn- xMean[0]);
            S[0] = S[0]+(xIn-x_mean_prev)*(xIn- xMean[0]);
            xVarPop[0] = S[0]/ wSum[0];
            xVarSamp[0] = S[0]/(wSum[0]-1);

            if (xIn < xMin[0]) {
                xMin[0] = xIn;
            }

            if (xIn > xMax[0]) {
                xMax[0] = xIn;
            }
        }
    }

    public synchronized void addMeasure(double[] xIn, double[] w) {//Weighted vector
        for (int i = 0; i< xMean.length; i++) {
            if (!Double.isNaN(xIn[i])) {
                if (w[i] != 0) {
                    n[i] ++;
                    xSum[i] = xSum[i] + xIn[i];
                    wSum[i] = wSum[i] + w[i];
                    double x_mean_prev = xMean[i];
                    xMean[i] = xMean[i] + (w[i]/ wSum[i])*(xIn[i]- xMean[i]);
                    S[i] = S[i]+w[i]*(xIn[i]-x_mean_prev)*(xIn[i]- xMean[i]);
                    xVarPop[i] = S[i]/ wSum[i];
                    xVarSamp[i] = S[i]/(wSum[i]-1);

                    if (xIn[i] < xMin[i]) {
                        xMin[i] = xIn[i];
                    }

                    if (xIn[i] > xMax[i]) {
                        xMax[i] = xIn[i];
                    }
                }
            }
        }
    }

    public synchronized void addMeasure(double[] xIn, double[] w, int opt) {
        for (int i = 0; i< xMean.length; i++) {
            if (opt == IGNOREZEROS & xIn[i] == 0) {
                xIn[i] = Double.NaN;
            }
        }
        addMeasure(xIn,w);
    }

    public synchronized void addMeasure(double xIn, double w) {//Weighted value
        if (!Double.isNaN(xIn)) {
            if (w != 0) {
                n[0] ++;
                xSum[0] = xSum[0] + xIn;
                wSum[0] = wSum[0] + w;
                double x_mean_prev = xMean[0];
                xMean[0] = xMean[0] + (w/ wSum[0])*(xIn- xMean[0]);
                S[0] = S[0]+w*(xIn-x_mean_prev)*(xIn- xMean[0]);
                xVarPop[0] = S[0]/ wSum[0];
                xVarSamp[0] = S[0]/(wSum[0]-1);

                if (xIn < xMin[0]) {
                    xMin[0] = xIn;
                }

                if (xIn > xMax[0]) {
                    xMax[0] = xIn;
                }
            }
        }
    }

    /**
     * Add multiple measurements to a single cumulative value.  Different from addMeasure, which adds a single value to
     * each element of a cumulative array.
     */
    public synchronized void addMeasures(double[] xIn) {
        for (int i=0;i<xIn.length;i++) {
            addMeasure(xIn[i]);
        }
    }

    /**
     * Add multiple measurements with weighting to a single cumulative value.  Different from addMeasure, which adds a single value to each element of a cumulative array.
     */
    public synchronized void addMeasures(double[] xIn, double[] w) {
        for (int i=0;i<xIn.length;i++) {
            addMeasure(xIn[i], w[i]);
        }
    }

    public synchronized double[] getMean() {
        return xMean;
    }

    public synchronized double[] getSum() {
        return xSum;
    }

    /**
     * Returns the sample variance
     * @return
     */
    public synchronized double[] getVar() {
        return xVarSamp;
    }

    /**
     * Returns the variance specified by the argument
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
      * @return
     */
    public synchronized double[] getStd() {
        return getStd(SAMPLE);
    }

    /**
     * Returns the standard deviation specified by the argument.
     * @param mode
     * @return
     */
    public synchronized double[] getStd(int mode) {
        if (mode == SAMPLE) {
            double[] x_std = new double[xVarSamp.length];
            for (int i = 0; i< xVarSamp.length; i++) {
                x_std[i] = Math.sqrt(xVarSamp[i]);
            }
            return x_std;

        } else if (mode == POPULATION) {
            double[] x_std = new double[xVarPop.length];
            for (int i = 0; i< xVarPop.length; i++) {
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