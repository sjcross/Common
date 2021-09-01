package io.github.sjcross.common.MathFunc.EdgeCorrection;

/**
 * Created by sc13967 on 17/01/2018. This class calculates the edge correction
 * from "On explicit formulas of edge effect correction for Ripley's K-function"
 * Goreaud, F. and Pelissier, R., Journal of Vegetation Science, 10 (1999)
 * 433-438.
 */
public class GoreaudEdgeCorrection extends EdgeCorrection {
    public GoreaudEdgeCorrection(double minX, double maxX, double minY, double maxY) {
        super(minX, maxX, minY, maxY);
    }

    public GoreaudEdgeCorrection(double minX, double maxX, double minY, double maxY, double minZ, double maxZ, boolean is2D) {
        super(minX, maxX, minY, maxY, minZ, maxZ, is2D);
    }


    public double getCorrection(double x, double y, double r) {
        // d1 and d3 correspond to the shorter axis, d2 and d4 correspond to the longer axis.  In each of those pairs,
        // the first value is the shorter distance (i.e. d1 < d3 and d2 < d4).
        double[] d = getDistances(x,y);
        double d1 = d[0];
        double d2 = d[1];
        double d3 = d[2];
        double d4 = d[3];

        double alphaOut = 0;

        // The sample circle is entirely within the sample area
        if (r<=d1 && r<=d2 && r<=d3 && r<=d4) {
            alphaOut = 0;
        }

        // The sample circle is outside one side
        if (r>d1 && r<=d2 && r<=d3 && r<=d4) {
            alphaOut = 2*Math.acos(d1/r);
        }

        // The sample circle extends beyond opposite sides of the sample area only
        if (r>d1 && r>d2 && r<=d3 && r<=d4) {
            if (r*r <= (d1*d1+d2*d2)) {
                alphaOut = 2*Math.acos(d1/r) + 2*Math.acos(d2/r);
            } else {
                alphaOut = Math.PI/2 + Math.acos(d1/r) + Math.acos(d2/r);
            }
        }

        // The sample circle extends beyond a corner of the sample area only
        if (r>d1 && r>d3 && r<=d2 && r<=d4) {
            alphaOut = 2*Math.acos(d1/r)+2*Math.acos(d3/r);
        }

        // The sample circle extends beyond three sides of the sample area
        if (r>d1 && r>d2 && r>d3 && r<=d4) {
            if (r*r <= (d1*d1+d2*d2) && r*r <= (d2*d2+d3*d3)) {
                alphaOut = 2*Math.acos(d1/r) + 2*Math.acos(d2/r) + 2*Math.acos(d3/r);
            } else if (r*r <= (d1*d1+d2*d2) && r*r > (d2*d2+d3*d3)) {
                alphaOut = Math.PI/2 + 2*Math.acos(d1/r) + Math.acos(d2/r) + Math.acos(d3/r);
            } else if (r*r > (d1*d1+d2*d2) && r*r > (d2*d2+d3*d3)) {
                alphaOut = Math.PI + Math.acos(d1/r) + Math.acos(d3/r);
            }
        }

        return Math.log((2*Math.PI)/(2*Math.PI-alphaOut))+1;

    }

    @Override
    public double getCorrection(double x, double y, double z, double r) {
        System.err.println("Goreaud correction doesn't currently work in 3D");
        return 0;
    }

    public double[] getDistances(double x, double y) {
        double d1 = y-minY; // Distance to bottom
        double d2 = x-minX; // Distance to left
        double d3 = maxY-y; // Distance to top
        double d4 = maxX-x; // Distance to right

        // Ensuring d1 < d3 and d2 < d4
        double[] d13 = checkVariableOrder(d1,d3);
        double[] d24 = checkVariableOrder(d2,d4);

        // Returning the coordinates in the correct order
        if (d13[0] < d24[0]) return new double[]{d13[0],d24[0],d13[1],d24[1]};
        return new double[]{d24[0],d13[0],d24[1],d13[1]};

    }

    /**
     * Takes two variables and outputs them to an array, where the smaller is in element 0 and the larger in element 1
     * @param v1
     * @param v2
     * @return
     */
    static double[] checkVariableOrder(double v1, double v2) {
        if (v1 <= v2) return new double[]{v1,v2};
        return new double[]{v2,v1};
    }
}
