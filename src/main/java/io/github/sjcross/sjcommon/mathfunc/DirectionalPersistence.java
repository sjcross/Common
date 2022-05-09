package io.github.sjcross.sjcommon.mathfunc;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Created by sc13967 on 25/01/2017.
 */
public class DirectionalPersistence {
    private CumStat[] cosTheta;

    // CONSTRUCTORS
    public DirectionalPersistence(CumStat[] cosTheta) {
        this.cosTheta = cosTheta;
    }


    // PUBLIC METHODS

    public void execute(double[] x, double[] y, double[] z, int[] f) {
        int maxDf = f[f.length-1]-f[0]; // Maximum frame separation

        for (int df = 0;df<maxDf;df++) {
            for (int i = 0; i < x.length - df - 1; i++) {
                if (f[i+1]-f[i] == 1 & f[i+df+1]-f[i+df] == 1 & f[i+df]-f[i] == df) {
                    Vector3D v1 = new Vector3D((x[i] - x[i + 1]), (y[i] - y[i + 1]), (z[i] - z[i + 1]));
                    Vector3D v2 = new Vector3D((x[i + df] - x[i + 1 + df]), (y[i + df] - y[i + 1 + df]), (z[i + df] - z[i + 1 + df]));

                    if (v1.getNorm() != 0 & v2.getNorm() != 0) {
                        cosTheta[df].addMeasure(Math.cos(Vector3D.angle(v1, v2)));
                    }
                }
            }
        }
    }


    // GETTERS AND SETTERS

    public CumStat[] getCosTheta(){
        return cosTheta;

    }
}