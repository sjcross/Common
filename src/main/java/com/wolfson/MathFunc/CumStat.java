package com.wolfson.MathFunc;

import java.util.Arrays;

// THIS CLASS IS BASED ON THE INCREMENTAL CALCULATION OF WEIGHTED MEAN AND VARIANCE FROM http://www-uxsup.csx.cam.ac.uk/~fanf2/hermes/doc/antiforgery/stats.pdf (Accessed 30-06-2016)

public class CumStat {

	double[] x_mean;
	double[] x_var;
	double[] n;
	double[] w_sum;
	double[] S;
	double[] x_sum;
	double[] x_min;
	double[] x_max;

	public CumStat(int len) {
		x_mean = new double[len];
		x_var = new double[len];
		n = new double[len];
		w_sum = new double[len];
		S = new double[len];
		x_sum = new double[len];
		x_min = new double[len];
		Arrays.fill(x_min, Double.POSITIVE_INFINITY);
		x_max = new double[len];
		Arrays.fill(x_max, Double.NEGATIVE_INFINITY);
	}

	public void addMeasure(double[] x_in) { //Non-weighted vector
		for (int i=0;i<x_in.length;i++) {
			if (!Double.isNaN(x_in[i])) {
				n[i] ++;
				x_sum[i] = x_sum[i] + x_in[i];
				w_sum[i] = w_sum[i] + 1;
				double x_mean_prev = x_mean[i];
				x_mean[i] = x_mean[i] + (1/w_sum[i])*(x_in[i]-x_mean[i]);
				S[i] = S[i]+(x_in[i]-x_mean_prev)*(x_in[i]-x_mean[i]);
				x_var[i] = S[i]/w_sum[i];

				if (x_in[i] < x_min[i]) {
					x_min[i] = x_in[i];
				}

				if (x_in[i] > x_max[i]) {
					x_max[i] = x_in[i];
				}
			}
		}
	}

	public void addMeasure(double x_in) { //Non-weighted value
		if (!Double.isNaN(x_in)) {
			n[0] ++;
			x_sum[0] = x_sum[0] + x_in;
			w_sum[0] = w_sum[0] + 1;
			double x_mean_prev = x_mean[0];
			x_mean[0] = x_mean[0] + (1/w_sum[0])*(x_in-x_mean[0]);
			S[0] = S[0]+(x_in-x_mean_prev)*(x_in-x_mean[0]);
			x_var[0] = S[0]/w_sum[0];

			if (x_in < x_min[0]) {
				x_min[0] = x_in;
			}

			if (x_in > x_max[0]) {
				x_max[0] = x_in;
			}
		}
	}

	public void addMeasure(double[] x_in, double[] w) {//Weighted vector
		for (int i=0;i<x_mean.length;i++) {
			if (!Double.isNaN(x_in[i])) {
				if (w[i] != 0) {
					n[i] ++;
					x_sum[i] = x_sum[i] + x_in[i];
					w_sum[i] = w_sum[i] + w[i];
					double x_mean_prev = x_mean[i];
					x_mean[i] = x_mean[i] + (w[i]/w_sum[i])*(x_in[i]-x_mean[i]);
					S[i] = S[i]+w[i]*(x_in[i]-x_mean_prev)*(x_in[i]-x_mean[i]);
					x_var[i] = S[i]/w_sum[i];

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

	public void addMeasure(double x_in, double w) {//Weighted value
		if (!Double.isNaN(x_in)) {
			if (w != 0) {
				n[0] ++;
				x_sum[0] = x_sum[0] + x_in;
				w_sum[0] = w_sum[0] + w;
				double x_mean_prev = x_mean[0];
				x_mean[0] = x_mean[0] + (w/w_sum[0])*(x_in-x_mean[0]);
				S[0] = S[0]+w*(x_in-x_mean_prev)*(x_in-x_mean[0]);
				x_var[0] = S[0]/w_sum[0];

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
	public void addMeasures(double[] x_in) {
		for (int i=0;i<x_in.length;i++) {
			addMeasure(x_in[i]);
		}
	}

	/**
	 * Add multiple measurements with weighting to a single cumulative value.  Different from addMeasure, which adds a single value to each element of a cumulative array.
	 */
	public void addMeasures(double[] x_in, double[] w) {
		for (int i=0;i<x_in.length;i++) {
			addMeasure(x_in[i], w[i]);
		}
	}

	public double[] getMean() {
		return x_mean;
	}

	public double[] getSum() {
		return x_sum;
	}

	public double[] getVar() {
		return x_var;
	}

	public double[] getStd() {
		double[] x_std = new double[x_var.length];
		for (int i=0;i<x_var.length;i++) {
			x_std[i] = Math.sqrt(x_var[i]);
		}
		return x_std;
	}

	public double[] getN() {
		return n;
	}

	public double[] getMin() {
		return x_min;
	}

	public double[] getMax() {
		return x_max;
	}
}