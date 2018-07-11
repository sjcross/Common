package wbif.sjx.common.Analysis;

import org.bonej.geometry.FitEllipse;
import wbif.sjx.common.Object.Volume;

public class EllipseCalculator {
    private final Volume volume;
    private final double[] e2d;

    public EllipseCalculator(Volume volume) throws RuntimeException {
        this.volume = volume;

        if (volume.getNVoxels() <= 2) {
            e2d = null;
            return;
        }

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
        double a = e2d[0];
        double b = e2d[1];
        double c = e2d[2];

        if (e2d == null) return Double.NaN;

        if (b == 0 & a < c) {
            return 0;

        } else if (b == 0 & a > c) {
            return Math.PI/2;

        } else if (b != 0 & a > 0) {
            return -Math.atan((c-a - Math.sqrt((a-c)*(a-c) + b*b)) / b);

        } else {
            double val = -Math.atan((c-a - Math.sqrt((a-c)*(a-c) + b*b)) / b) + Math.PI / 2;
            return ((val + Math.PI / 2) % Math.PI) - Math.PI / 2;

        }
    }

    public double getXCentre() {
        double a = e2d[0];
        double b = e2d[1];
        double c = e2d[2];
        double d = e2d[3];
        double e = e2d[4];
        double f = e2d[5];

        return (2*c*d-b*e)/(b*b-4*a*c);

    }

    public double getYCentre() {
        double a = e2d[0];
        double b = e2d[1];
        double c = e2d[2];
        double d = e2d[3];
        double e = e2d[4];
        double f = e2d[5];

        return (2*a*e-b*d)/(b*b-4*a*c);

    }

    public double getSemiMajorAxis() {
        double a = e2d[0];
        double b = e2d[1];
        double c = e2d[2];
        double d = e2d[3];
        double e = e2d[4];
        double f = e2d[5];

        double part1 = 2*(a*e*e+c*d*d-b*d*e+(b*b-4*a*c)*f);
        double part2 = a+c+Math.sqrt((a-c)*(a-c)+b*b);
        double part3 = b*b-4*a*c;

        return -Math.sqrt(part1*part2)/part3;

    }

    public double getSemiMinorAxis() {
        double a = e2d[0];
        double b = e2d[1];
        double c = e2d[2];
        double d = e2d[3];
        double e = e2d[4];
        double f = e2d[5];

        double part1 = 2*(a*e*e+c*d*d-b*d*e+(b*b-4*a*c)*f);
        double part2 = a+c-Math.sqrt((a-c)*(a-c)+b*b);
        double part3 = b*b-4*a*c;

        return -Math.sqrt(part1*part2)/part3;

    }

    public Volume getContainedPoints() {
        double dppXY = volume.getDistPerPxXY();
        double dppZ = volume.getDistPerPxZ();
        double cal = dppXY/dppZ;
        String units = volume.getCalibratedUnits();
        boolean is2D = volume.is2D();

        Volume insideEllipse = new Volume(dppXY,dppZ,units,is2D);

        double xCent = getXCentre();
        double yCent = getYCentre();
        double semiMajor = getSemiMajorAxis();
        double[] xRange = new double[]{xCent-semiMajor,xCent+semiMajor};

        for (int x=(int) Math.floor(xCent-semiMajor);x<=xCent+semiMajor;x++) {
            for (int y=(int) Math.floor(yCent-semiMajor);y<=yCent+semiMajor;y++) {
                if (e2d[0]*x*x + e2d[1]*x*y + e2d[2]*y*y +e2d[3]*x + e2d[4]*y + e2d[5] <= 0) {
                    insideEllipse.addCoord(x,y,0);
                }
            }
        }

        return insideEllipse;

    }
}
