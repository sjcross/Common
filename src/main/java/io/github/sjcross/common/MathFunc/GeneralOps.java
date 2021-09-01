package io.github.sjcross.common.MathFunc;

/**
 * Created by sc13967 on 16/08/2016.
 */
public class GeneralOps {
    public static double ppdist(double[] pos1, double[] pos2) {
        double val = 0;

        for (int i=0;i<pos1.length;i++) {
            val += ((pos1[i]-pos2[i])*(pos1[i]-pos2[i]));
        }

        val = Math.sqrt(val);

        return val;
    }
}