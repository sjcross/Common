package wbif.sjx.common.Process;

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

        double alphaOut = 0;
        if (r<=d1 && r<=d2 && r<=d3 && r<=d4) alphaOut = 0;
        if (r>d1 && r<=d2 && r<=d3 && r<=d4) alphaOut = 2*Math.acos(d1/r);
        if (r>d1 && r>d2 && r<=d3 && r<=d4) {
            if (r*r <= (d1*d1+d2*d2)) {
                alphaOut = 2*Math.acos(d1/r) + 2*Math.acos(d2/r);
            } else {
                alphaOut = Math.PI/2 + Math.acos(d1/r) + Math.acos(d2/r);
            }
        }
        if (r>d1 && r>d3 && r<=d2 && r<=d4) alphaOut = 2*Math.acos(d1/r)+2*Math.acos(d3/r);
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
}
