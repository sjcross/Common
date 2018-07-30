package wbif.sjx.common.MathFunc;

/**
 * Created by sc13967 on 17/01/2018.
 * This class calculates the edge correction from "On explicit formulas of edge effect correction for Ripley's
 * K-function" Goreaud, F. and Pelissier, R., Journal of Vegetation Science, 10 (1999) 433-438.
 */
public class GoreaudEdgeCorrection {
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    public GoreaudEdgeCorrection(double minX, double maxX, double minY, double maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public double getFractionInsideRectangle(double x, double y, double r) {
        // d1 and d3 correspond to the shorter axis, d2 and d4 correspond to the longer axis.  In each of those pairs,
        // the first value is the shorter distance (i.e. d1 < d3 and d2 < d4).
        double[] d = getDistances(x,y);
        double d1 = d[0];
        double d2 = d[1];
        double d3 = d[2];
        double d4 = d[3];

        double alphaOut = 0;

        // The sample circle is entirely within the sample area
        if (r<=d1 && r<=d2 && r<=d3 && r<=d4) alphaOut = 0;

        // The sample circle is outside one side
        if (r>d1 && r<=d2 && r<=d3 && r<=d4) alphaOut = 2*Math.acos(d1/r);

        // The sample circle extends beyond opposite sides of the sample area only
        if (r>d1 && r>d2 && r<=d3 && r<=d4) {
            if (r*r <= (d1*d1+d2*d2)) {
                alphaOut = 2*Math.acos(d1/r) + 2*Math.acos(d2/r);
            } else {
                alphaOut = Math.PI/2 + Math.acos(d1/r) + Math.acos(d2/r);
            }
        }

        // The sample circle extends beyond a corner of the sample area only
        if (r>d1 && r>d3 && r<=d2 && r<=d4) alphaOut = 2*Math.acos(d1/r)+2*Math.acos(d3/r);

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

        return alphaOut;

    }

    public double[] getDistances(double x, double y) {
        // d1 and d3 correspond to the shorter axis, d2 and d4 correspond to the longer axis
        double d1; double d2; double d3; double d4;

        if ((maxX-minX) > (maxY-minY)) {
            // X is the longer rectangle axis
            d1 = y-minY; // Distance to bottom
            d2 = x-minX; // Distance to left
            d3 = maxY-y; // Distance to top
            d4 = maxX-x; // Distance to right

        } else {
            // Y is the longer rectangle axis
            d1 = x-minX; // Distance to left
            d2 = y-minY; // Distance to bottom
            d3 = maxX-x; // Distance to right
            d4 = maxY-y; // Distance to top

        }

        // Ensuring d1 < d3 and d2 < d4
        double[] d13 = checkVariableOrder(d1,d3);
        double[] d24 = checkVariableOrder(d2,d4);

        return new double[]{d13[0],d24[0],d13[1],d24[1]};

    }

    /**
     * Takes two variables and outputs them to an array, where the smaller is in element 0 and the larger in element 1
     * @param v1
     * @param v2
     * @return
     */
    double[] checkVariableOrder(double v1, double v2) {
        if (v1 <= v2) return new double[]{v1,v2};
        System.out.println("Swapping variables");
        return new double[]{v2,v1};
    }
}
