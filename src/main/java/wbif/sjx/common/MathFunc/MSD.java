package wbif.sjx.common.MathFunc;

import java.util.ArrayList;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.regression.SimpleRegression;

public class MSD
{
    private double[] posx;
    private double[] posy;
    private double[] post;

    public MSD(double[] posx_in, double[] posy_in, double[] post_in) {
        posx = posx_in;
        posy = posy_in;
        post = post_in;
    }

    public double[] getLinearFit(double inc_t, int dur) {
        double[] arr_x = getXValues(inc_t);
        double[] arr_y = getMSDValues(inc_t);

        return getLinearFit(arr_x,arr_y,dur);

    }

    public double[] getLinearFit(double[] arr_x, double[] arr_y, int dur) {
        arr_y[0] = 0; //There should be no values with zero time-gap, so this would otherwise be NaN

        if (arr_x.length < dur) {
            dur = arr_x.length;
        }

        double[][] data = new double[dur][2];
        for (int j=0;j<dur;j++) {
            data[j][0] = arr_x[j];
            data[j][1] = arr_y[j];
        }

        SimpleRegression sr = new SimpleRegression();
        sr.addData(data);
        return new double[]{sr.getSlope(),sr.getIntercept(),dur};

    }

    public double[] getMSDValues(double inc_t) {
        double dur = post[post.length-1] - post[0];
        double[] arr_y = new double[(int) dur+1];

        for (int i=0;i<=dur;i++) { //Incrementing over all time steps
            ArrayList<Double> disp = new ArrayList<Double>();
            int cnt = 0;
            for (int j=0;j<posx.length;j++) {//Incrementing over all frames with the possibility for that time step
                for (int k=j+1;k<posx.length;k++) {
                    //IJ.log("Testing "+String.valueOf(post[j])+" to "+String.valueOf(post[k]));
                    if (post[k]-post[j]==i*inc_t) {
                        double x = posx[k]-posx[j];
                        double y = posy[k]-posy[j];
                        disp.add(x*x+y*y);
                        cnt ++;
                    }
                }
            }

            double[] dbl_disp = new double[disp.size()];
            for (int j=0;j<dbl_disp.length;j++) {
                dbl_disp[j] = disp.get(j);
            }

            if (cnt > 0) {
                arr_y[i] = new Mean().evaluate(dbl_disp);
            }
        }

        return arr_y;
    }

    public double[] getXValues(double inc_t) {
        double dur = post[post.length-1] - post[0];
        double[] arr_x = new double[(int) dur+1];

        for (int i=0;i<=dur;i++) { //Incrementing over all time steps
            arr_x[i] = i*inc_t;
        }
        return arr_x;
    }

    public double[] getYFitValues(double inc_t, int dur) {
        double[] fit_vals = getLinearFit(inc_t, dur);
        double[] arr_x = getXValues(inc_t);
        double[] arr_y_lsq = new double[dur];
        for (int i=0;i<dur;i++) {
            arr_y_lsq[i] = arr_x[i]*fit_vals[0]+fit_vals[1];
        }
        return arr_y_lsq;
    }

    public double[] getYFitValues(double[] arr_x, double[] arr_y, int dur) {
        double[] fit_vals = getLinearFit(arr_x, arr_y, dur);
        double[] arr_y_lsq = new double[dur];
        for (int i=0;i<dur;i++) {
            arr_y_lsq[i] = arr_x[i]*fit_vals[0]+fit_vals[1];
        }
        return arr_y_lsq;
    }

    public double[] getNumber() {
        double dur = post[post.length-1] - post[0];
        double[] arr_n = new double[(int) dur+1];

        for (int i=0;i<=dur;i++) { //Incrementing over all time steps
            for (int j=0;j<posx.length;j++) {//Incrementing over all frames with the possibility for that time step
                for (int k=j+1;k<posx.length;k++) {
                    if (post[k]-post[j]==i) {
                        arr_n[i] ++;
                    }
                }
            }
        }
        return arr_n;
    }
}