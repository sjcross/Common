package io.github.sjcross.common.analysis;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

/**
 * Created by sc13967 on 21/05/2018.
 */
public class PeriodogramCalculator {
    public static double[] padSignal(double[] signalIn) {
        // Calculating the next power of 2
        double powIn = Math.log(signalIn.length)/Math.log(2);
        double powOut = Math.ceil(powIn);
        int lengthOut = (int) Math.pow(2,powOut);

        // Moving data to the new array
        double[] signalOut = new double[lengthOut];
        System.arraycopy(signalIn, 0, signalOut, 0, signalIn.length);

        return signalOut;

    }

    public static Complex[] calculateFFT(double[] signal) {
        // Calculate FFT for signal (complex output)
        FastFourierTransformer fastFourierTransformer = new FastFourierTransformer(DftNormalization.STANDARD);

        return fastFourierTransformer.transform(signal, TransformType.FORWARD);

    }

    public static double[] calculatePSD(Complex[] fft, double sampF) {
        int N = fft.length;
        int psdN = N/2+1;
        double[] psd = new double[psdN];

        for (int i=0;i<psdN;i++) {
            psd[i] = 1/(sampF*N) * fft[i].abs() * fft[i].abs();
        }

        // Doubling all values except the zero frequency and Nyquist frequency
        for (int i=1;i<psd.length-1;i++) {
            psd[i] *= 2;
        }

        return psd;

    }

    public static double[] calculateFrequency(double sampF, int N) {
        double[] freq = new double[N];

        for (int i=0;i<N;i++) {
            freq[i] = i*sampF/(2*(N-1));
        }

        return freq;

    }

    public static TreeMap<Double,Double> calculate(double[] signal, double sampF) {
        // Padding signal so the array length is a power of 2
        signal = padSignal(signal);

        // Calculating FFT using Apache Commons Math
        Complex[] fft = calculateFFT(signal);

        // Calculating the power spectral density and frequency values
        double[] psd = calculatePSD(fft,sampF);
        double[] freq = calculateFrequency(sampF,psd.length);

        // Converting to TreeMap
        TreeMap<Double,Double> periodogram = new TreeMap<>();
        for (int i=0;i<psd.length;i++) {
            periodogram.put(freq[i],psd[i]);
        }

        return periodogram;

    }

    public static void main(String[] args) {
        TreeMap<Double,Double> psd = new TreeMap<>();
        psd.put(0.0,0.0016214310049436605);
        psd.put(0.0625,2.68716758119196E-4);
        psd.put(0.125,1.5263724725207836E-4);
        psd.put(0.1875,7.81074956169333E-5);
        psd.put(0.25,7.212855869321588E-6);
        psd.put(0.3125,2.0692582111237256E-5);
        psd.put(0.375,3.736835918830827E-5);
        psd.put(0.4375,2.740466866844013E-5);
        psd.put(0.5,3.042702786115774E-6);

    }

    public static double[][] getKeyFrequencies(TreeMap<Double,Double> psd, int nPeaks) {
        double[][] peaks = new double[nPeaks][2];

        // Checking the first point is NaN
        if (Double.isNaN(psd.values().iterator().next())) return null;

        // For the number of desired peaks, determining the peak frequency and removing that peak before repeating
        for (int i=0;i<nPeaks;i++) {
            // Getting the corresponding frequency
            double corrFreq = getMaxPowerFrequency(psd);

            // Storing those values
            peaks[i][0] = corrFreq;

            if (Double.isNaN(corrFreq)) {
                peaks[i][1] = Double.NaN;
            } else {
                peaks[i][1] = psd.get(corrFreq);

                // Removing that peak from the spectrum
                flattenPeak(psd,corrFreq);
            }
        }

        return peaks;

    }

    /**
     * Determines the frequency corresponding to the maximum power
     * @param psd
     * @return
     */
    public static double getMaxPowerFrequency(TreeMap<Double,Double> psd) {
        double maxPower = -Double.MAX_VALUE;
        double corrFreq = Double.NaN; // Frequency corresponding to maximum power

        // Checking the first point is NaN
        if (Double.isNaN(psd.values().iterator().next())) return Double.NaN;

        // Iterating over all powers to see which has the largest value
        for (double freq:psd.keySet()) {
            double power = psd.get(freq);

            if (power > maxPower) {
                maxPower = power;
                corrFreq = freq;
            }
        }

        return corrFreq;

    }

    public static void flattenPeak(TreeMap<Double,Double> psd, double freq) {
        // Remove the central point from the peak
        double orig = psd.get(freq);
        psd.put(freq,-Double.MAX_VALUE);

        // Checking neighbours to see if they should be flattened too
        flattenNeighbours(psd,freq,orig);

    }

    public static void flattenNeighbours(TreeMap<Double,Double> psd, double freq, double orig) {
        // Testing the frequency to the left (lower frequency)
        Map.Entry<Double,Double> left = psd.lowerEntry(freq);
        if (left != null && left.getValue() < orig && left.getValue() != -Double.MAX_VALUE) flattenPeak(psd,left.getKey());

        // Testing the frequency to the right (higher frequency)
        Map.Entry<Double,Double> right = psd.higherEntry(freq);
        if (right != null && right.getValue() < orig && right.getValue() != -Double.MAX_VALUE) flattenPeak(psd,right.getKey());

    }

 }
