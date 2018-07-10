package wbif.sjx.common.Analysis;

import org.bonej.geometry.FitEllipse;
import wbif.sjx.common.Object.Volume;

public class EllipseCalculator {
    private double[] e2d;

    public EllipseCalculator(Volume volume) throws RuntimeException {
        if (volume.getNVoxels() <= 2) return;

        //Uses FitEllipse class from BoneJ
        double[] x = volume.getX(true);
        double[] y = volume.getY(true);
        double[][] coords = new double[x.length][2];

        for (int i=0;i<x.length;i++) {
            coords[i][0] = x[i];
            coords[i][1] = y[i];
        }

        e2d = FitEllipse.direct(coords);

    }

    public double[] getEllipseFit() {
        return e2d;

    }

    public double getEllipseThetaRads() {
        if (e2d == null) return Double.NaN;

        if (e2d[1] == 0 & e2d[0] < e2d[2]) {
            return 0;

        } else if (e2d[1] == 0 & e2d[0] > e2d[2]) {
            return 90;

        } else if (e2d[1] != 0 & e2d[0] > 0) {
            return -Math.atan((e2d[2] - e2d[0] - Math.sqrt(Math.pow(e2d[0] - e2d[2], 2) + Math.pow(e2d[1], 2))) / e2d[1]);

        } else {
            double val = -Math.atan((e2d[2] - e2d[0] - Math.sqrt(Math.pow(e2d[0] - e2d[2], 2) + Math.pow(e2d[1], 2))) / e2d[1]) + Math.PI / 2;
            return ((val + Math.PI / 2) % Math.PI) - Math.PI / 2;

        }
    }

    public double getXCentre() {
        double a = e2d[0];
        double b = e2d[1];
        double c = e2d[2];
        double d = e2d[3];
        double f = e2d[4];
        double g = e2d[5];

        return (c*d-b*f)/(b*b-a*c);

    }

    public double getYCentre() {
        double a = e2d[0];
        double b = e2d[1];
        double c = e2d[2];
        double d = e2d[3];
        double f = e2d[4];
        double g = e2d[5];

        return (a*f-b*d)/(b*b-a*c);

    }

    public double getSemiMajorAxis() {
        double a = e2d[0];
        double b = e2d[1];
        double c = e2d[2];
        double d = e2d[3];
        double f = e2d[4];
        double g = e2d[5];

        double top = 2*(a*f*f + c*d*d + g*b*b - 2*b*d*f - a*c*g);
        double bottom = (b*b-a*c)*(Math.sqrt((a-c)*(a-c)+4*b*b)-(a+c));

        return Math.sqrt(top/bottom);

    }

    public double getSemiMinorAxis() {
        double a = e2d[0];
        double b = e2d[1];
        double c = e2d[2];
        double d = e2d[3];
        double f = e2d[4];
        double g = e2d[5];

        double top = 2*(a*f*f+c*d*d+g*b*b-2*b*d*f-a*c*g);
        double bottom = (b*b-a*c)*(-Math.sqrt((a-c)*(a-c)+4*b*b)-(a+c));

        return Math.sqrt(top/bottom);

    }
}
