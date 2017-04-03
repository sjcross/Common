package wolfson.common.MathFunc;

import java.util.Arrays;

// THIS CLASS IS BASED ON THE INCREMENTAL CALCULATION OF WEIGHTED MEAN AND VARIANCE FROM http://www-uxsup.csx.cam.ac.uk/~fanf2/hermes/doc/antiforgery/stats.pdf (Accessed 30-06-2016)
// It gives the population standard deviation (normalised by N rather than N-1 as you get with the sample standard deviation)

public class CumStat {
    public static final int IGNOREZEROS = 0;
    public static final int POPULATION = 1;
    public static final int SAMPLE = 2;

    private double[] x_mean;
    private double[] x_var_pop; // Population variance
    private double[] x_var_samp; // Sample variance
    private double[] n;
    private double[] w_sum;
    private double[] S;
    private double[] x_sum;
    private double[] x_min;
    private double[] x_max;

    public CumStat(int len) {
        x_mean = new double[len];
        x_var_pop = new double[len];
        x_var_samp = new double[len];
        n = new double[len];
        w_sum = new double[len];
        S = new double[len];
        x_sum = new double[len];
        x_min = new double[len];
        Arrays.fill(x_min, Double.POSITIVE_INFINITY);
        x_max = new double[len];
        Arrays.fill(x_max, Double.NEGATIVE_INFINITY);
    }

    public synchronized void addMeasure(double[] x_in) { //Non-weighted vector
        for (int i=0;i<x_in.length;i++) {
            if (!Double.isNaN(x_in[i])) {
                n[i] ++;
                x_sum[i] = x_sum[i] + x_in[i];
                w_sum[i] = w_sum[i] + 1;
                double x_mean_prev = x_mean[i];
                x_mean[i] = x_mean[i] + (1/w_sum[i])*(x_in[i]-x_mean[i]);
                S[i] = S[i]+(x_in[i]-x_mean_prev)*(x_in[i]-x_mean[i]);
                x_var_pop[i] = S[i]/w_sum[i];
                x_var_samp[i] = S[i]/(w_sum[i]-1);

                if (x_in[i] < x_min[i]) {
                    x_min[i] = x_in[i];
                }

                if (x_in[i] > x_max[i]) {
                    x_max[i] = x_in[i];
                }
            }
        }
    }

    public synchronized void addMeasure(double[] x_in, int opt) {
        for (int i=0;i<x_mean.length;i++) {
            if (opt == IGNOREZEROS & x_in[i] == 0) {
                x_in[i] = Double.NaN;
            }
        }
        addMeasure(x_in);
    }

    public synchronized void addSingleMeasure(int i, double x_in) {
        if (!Double.isNaN(x_in)) {
            n[i] ++;
            x_sum[i] = x_sum[i] + x_in;
            w_sum[i] = w_sum[i] + 1;
            double x_mean_prev = x_mean[i];
            x_mean[i] = x_mean[i] + (1/w_sum[i])*(x_in-x_mean[i]);
            S[i] = S[i]+(x_in-x_mean_prev)*(x_in-x_mean[i]);
            x_var_pop[i] = S[i]/w_sum[i];
            x_var_samp[i] = S[i]/(w_sum[i]-1);

            if (x_in < x_min[i]) {
                x_min[i] = x_in;
            }

            if (x_in > x_max[i]) {
                x_max[i] = x_in;
            }
        }
    }

    public synchronized void addMeasure(double[] x_in, double[] w, int opt) {
        for (int i=0;i<x_mean.length;i++) {
            if (opt == IGNOREZEROS & x_in[i] == 0) {
                x_in[i] = Double.NaN;
            }
        }
        addMeasure(x_in,w);
    }

    public synchronized void addMeasure(double x_in) { //Non-weighted value
        if (!Double.isNaN(x_in)) {
            n[0] ++;
            x_sum[0] = x_sum[0] + x_in;
            w_sum[0] = w_sum[0] + 1;
            double x_mean_prev = x_mean[0];
            x_mean[0] = x_mean[0] + (1/w_sum[0])*(x_in-x_mean[0]);
            S[0] = S[0]+(x_in-x_mean_prev)*(x_in-x_mean[0]);
            x_var_pop[0] = S[0]/w_sum[0];
            x_var_samp[0] = S[0]/(w_sum[0]-1);

            if (x_in < x_min[0]) {
                x_min[0] = x_in;
            }

            if (x_in > x_max[0]) {
                x_max[0] = x_in;
            }
        }
    }

    public synchronized void addMeasure(double[] x_in, double[] w) {//Weighted vector
        for (int i=0;i<x_mean.length;i++) {
            if (!Double.isNaN(x_in[i])) {
                if (w[i] != 0) {
                    n[i] ++;
                    x_sum[i] = x_sum[i] + x_in[i];
                    w_sum[i] = w_sum[i] + w[i];
                    double x_mean_prev = x_mean[i];
                    x_mean[i] = x_mean[i] + (w[i]/w_sum[i])*(x_in[i]-x_mean[i]);
                    S[i] = S[i]+w[i]*(x_in[i]-x_mean_prev)*(x_in[i]-x_mean[i]);
                    x_var_pop[i] = S[i]/w_sum[i];
                    x_var_samp[i] = S[i]/(w_sum[i]-1);

                    if (x_in[i] < x_min[i]) {
                        x_min[i] = x_in[i];
                    }

                    if (x_in[i] > x_max[i]) {
                        x_max[i] = x_in[i];
                    }
                }
            }
        }
    }

    public synchronized void addMeasure(double x_in, double w) {//Weighted value
        if (!Double.isNaN(x_in)) {
            if (w != 0) {
                n[0] ++;
                x_sum[0] = x_sum[0] + x_in;
                w_sum[0] = w_sum[0] + w;
                double x_mean_prev = x_mean[0];
                x_mean[0] = x_mean[0] + (w/w_sum[0])*(x_in-x_mean[0]);
                S[0] = S[0]+w*(x_in-x_mean_prev)*(x_in-x_mean[0]);
                x_var_pop[0] = S[0]/w_sum[0];
                x_var_samp[0] = S[0]/(w_sum[0]-1);

                if (x_in < x_min[0]) {
                    x_min[0] = x_in;
                }

                if (x_in > x_max[0]) {
                    x_max[0] = x_in;
                }
            }
        }
    }

    /**
     * Add multiple measurements to a single cumulative value.  Different from addMeasure, which adds a single value to each element of a cumulative array.
     */
    public synchronized void addMeasures(double[] x_in) {
        for (int i=0;i<x_in.length;i++) {
            addMeasure(x_in[i]);
        }
    }

    /**
     * Add multiple measurements with weighting to a single cumulative value.  Different from addMeasure, which adds a single value to each element of a cumulative array.
     */
    public synchronized void addMeasures(double[] x_in, double[] w) {
        for (int i=0;i<x_in.length;i++) {
            addMeasure(x_in[i], w[i]);
        }
    }

    public synchronized double[] getMean() {
        return x_mean;
    }

    public synchronized double[] getSum() {
        return x_sum;
    }

    /**
     * Returns the sample variance
     * @return
     */
    public synchronized double[] getVar() {
        return x_var_samp;
    }

    /**
     * Returns the variance specified by the argument
     * @param mode
     * @return
     */
    public synchronized double[] getVar(int mode) {
        if (mode == SAMPLE) {
            return x_var_samp;

        } else if (mode == POPULATION) {
            return x_var_pop;

        }

        return new double[]{0};

    }

    /**
     * Returns the sample standard deviation.
      * @return
     */
    public synchronized double[] getStd() {
        double[] x_std = new double[x_var_samp.length];
        for (int i = 0; i< x_var_samp.length; i++) {
            x_std[i] = Math.sqrt(x_var_samp[i]);
        }
        return x_std;
    }

    /**
     * Returns the standard deviation specified by the argument.
     * @param mode
     * @return
     */
    public synchronized double[] getStd(int mode) {
        if (mode == SAMPLE) {
            double[] x_std = new double[x_var_samp.length];
            for (int i = 0; i< x_var_samp.length; i++) {
                x_std[i] = Math.sqrt(x_var_samp[i]);
            }
            return x_std;

        } else if (mode == POPULATION) {
            double[] x_std = new double[x_var_pop.length];
            for (int i = 0; i< x_var_pop.length; i++) {
                x_std[i] = Math.sqrt(x_var_pop[i]);
            }
            return x_std;

        }

        return new double[]{0};

    }

    public synchronized double[] getN() {
        return n;
    }

    public synchronized double[] getMin() {
        return x_min;
    }

    public synchronized double[] getMax() {
        return x_max;
    }

    public synchronized double[] getWeight() {
        return w_sum;

    }
}