package wbif.sjx.common.Analysis;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import wbif.sjx.common.MathFunc.CumStat;

import java.util.Arrays;

/**
 * Created by steph on 15/04/2017.
 */
public class MSDCalculator {
    public static CumStat[] calculate(CumStat[] cumStat, int[] f, double[] x, double[] y, double[] z) {
        for (int i = 0; i < f[f.length - 1]; i++) { //Incrementing over all time steps
            for (int j = 0; j < x.length; j++) {//Incrementing over all frames with the possibility for that time step
                for (int k = j + 1; k < x.length; k++) {
                    if (f[k] - f[j] == i) {
                        double dx = x[k] - x[j];
                        double dy = y[k] - y[j];
                        double dz = z[k] - z[j];

                        double val = dx * dx + dy * dy + dz * dz;

                        cumStat[i].addMeasure(val);
                    }
                }
            }
        }

        return cumStat;
    }

    public static CumStat[] calculate(int[] f, double[] x, double[] y, double[] z) {
        CumStat[] cumStat = new CumStat[f[f.length - 1]+1];
        for (int i=0;i<cumStat.length;i++) {
            cumStat[i] = new CumStat();
        }

        return calculate(cumStat,f,x,y,z);

    }

    /**
     * Fits a straight line to the first nPoints of the provided MSD curve
     * @param nPoints
     * @return double[]{slope,intercept,nPoints}
     */
    public static double[] getLinearFit(double[] df, double[] MSD, int nPoints) {
        MSD[0] = 0; //There should be no values with zero time-gap, so this would otherwise be NaN

        if (df.length < nPoints) nPoints = df.length;

        double[][] data = new double[nPoints][2];
        for (int j=0;j<nPoints;j++) {
            data[j][0] = df[j];
            data[j][1] = MSD[j];
        }

        SimpleRegression sr = new SimpleRegression();
        sr.addData(data);

        return new double[]{sr.getSlope(),sr.getIntercept(),nPoints};

    }

}
