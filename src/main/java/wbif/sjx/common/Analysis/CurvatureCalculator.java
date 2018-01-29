package wbif.sjx.common.Analysis;

import ij.ImagePlus;
import ij.gui.Line;
import ij.gui.OvalRoi;
import ij.gui.Overlay;
import org.apache.commons.math3.analysis.interpolation.DividedDifferenceInterpolator;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.interpolation.LoessInterpolator;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import wbif.sjx.common.Object.Vertex;

import java.awt.*;
import java.util.LinkedHashSet;
import java.util.TreeMap;

/**
 * Created by sc13967 on 26/01/2018.
 */
public class CurvatureCalculator {
    PolynomialSplineFunction[] splines;
    TreeMap<Double,Double> curvature;

    private double loessBandwidth = 0.4;
    private int loessIterations = 10;
    private double loessAccuracy = 1;

    public CurvatureCalculator(LinkedHashSet< Vertex > path) {
        // Preparing line coordinates for spline fitting
        double[] t = new double[path.size()];
        double[] x = new double[path.size()];
        double[] y = new double[path.size()];

        int count=0;
        Vertex prevVertex = null;
        for (Vertex vertex:path) {
            t[count] = count==0 ? 0 : t[count-1] + vertex.getEdgeLength(prevVertex);
            x[count] = vertex.getX();
            y[count++] = vertex.getY();

            prevVertex = vertex;

        }

        // Fitting the spline pair
        LoessInterpolator interpolator = new LoessInterpolator(loessBandwidth,loessIterations,loessAccuracy);

        // Storing splines
        splines = new PolynomialSplineFunction[6];
        splines[0] = interpolator.interpolate(t,x);
        splines[1] = interpolator.interpolate(t,y);

        // Calculating curvature
        splines[2] = splines[0].polynomialSplineDerivative(); // dx
        splines[3] = splines[2].polynomialSplineDerivative(); // ddx
        splines[4] = splines[1].polynomialSplineDerivative(); // dy
        splines[5] = splines[4].polynomialSplineDerivative(); // ddy

        // Extracting the gradients as a function of position along the curve
        double[] knots = splines[0].getKnots();

        curvature = new TreeMap<>();
        for (int i=0;i<knots.length;i++) {
            double dx = splines[2].value(knots[i]);
            double ddx = splines[3].value(knots[i]);
            double dy = splines[4].value(knots[i]);
            double ddy = splines[5].value(knots[i]);

            double k = Math.abs(dx*ddy-dy*ddx)/Math.pow((dx*dx+dy*dy),3d/2d);

            curvature.put(knots[i],k);

        }
    }

    public TreeMap<Double,Double> getCurvature() {
        return  curvature;

    }

    public void showOverlay(ImagePlus ipl) {
        // Calculating maximum curvature
        double maxCurvature = Double.MIN_VALUE;
        for (double currentCurvature:curvature.values()) {
            maxCurvature = Math.max(currentCurvature,maxCurvature);
        }

        showOverlay(ipl,maxCurvature);

    }

    public void showOverlay(ImagePlus ipl, double maxCurvature) {
        double r = 2;
        Overlay ovl = new Overlay();
        double prevPos = curvature.keySet().iterator().next();
        for (Double pos:curvature.keySet()) {
            double x1 = splines[0].value(prevPos);
            double y1 = splines[1].value(prevPos);
            double x2 = splines[0].value(pos);
            double y2 = splines[1].value(pos);

            Line line = new Line(x1+0.5,y1+0.5,x2+0.5,y2+0.5);
            line.setStrokeWidth(3f);
//            OvalRoi ovr = new OvalRoi(x-r/2+0.5,y-r /2+0.5, r, r);
            double b = curvature.get(pos)/(maxCurvature*2);
            line.setStrokeColor(Color.getHSBColor((float) b,1f,1f));
            ovl.addElement(line);

            prevPos = pos;

        }

        ipl.setOverlay(ovl);

    }

    public double getLoessBandwidth() {
        return loessBandwidth;
    }

    public void setLoessBandwidth(double loessBandwidth) {
        this.loessBandwidth = loessBandwidth;
    }

    public int getLoessIterations() {
        return loessIterations;
    }

    public void setLoessIterations(int loessIterations) {
        this.loessIterations = loessIterations;
    }

    public double getLoessAccuracy() {
        return loessAccuracy;
    }

    public void setLoessAccuracy(double loessAccuracy) {
        this.loessAccuracy = loessAccuracy;
    }
}
