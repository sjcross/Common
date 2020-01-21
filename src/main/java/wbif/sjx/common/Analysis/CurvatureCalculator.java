package wbif.sjx.common.Analysis;

import ij.ImagePlus;
import ij.gui.Line;
import ij.gui.OvalRoi;
import ij.gui.Overlay;
import ij.plugin.CalibrationBar;
import ij.process.ImageProcessor;
import org.apache.commons.math3.analysis.interpolation.*;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import wbif.sjx.common.Object.Vertex;

import java.awt.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.TreeMap;

/**
 * Created by sc13967 on 26/01/2018.
 */
public class CurvatureCalculator {
    private LinkedHashSet< Vertex > path;
    private PolynomialSplineFunction[] splines = null;
    private TreeMap<Double,Double> curvature = null;
    private FittingMethod fittingMethod = FittingMethod.STANDARD;

    public enum FittingMethod {STANDARD,LOESS}

    private int loessNNeighbours = 5;
    private double loessBandwidth = 0.04;
    private int loessIterations = 10;
    private double loessAccuracy = 100;


    public CurvatureCalculator(LinkedHashSet< Vertex > path) {
        this.path = path;

    }

    public void calculateCurvature() {
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

        // Storing splines
        splines = new PolynomialSplineFunction[6];

        // Fitting the spline pair
        switch (fittingMethod) {
            case LOESS:
                LoessInterpolator loessInterpolator = new LoessInterpolator(loessBandwidth,loessIterations,loessAccuracy);
                splines[0] = loessInterpolator.interpolate(t,x);
                splines[1] = loessInterpolator.interpolate(t,y);
                break;

            case STANDARD:
                SplineInterpolator splineInterpolator = new SplineInterpolator();
                splines[0] = splineInterpolator.interpolate(t,x);
                splines[1] = splineInterpolator.interpolate(t,y);
                break;
        }

        // Calculating curvature
        splines[2] = splines[0].polynomialSplineDerivative(); // dx
        splines[3] = splines[2].polynomialSplineDerivative(); // ddx
        splines[4] = splines[1].polynomialSplineDerivative(); // dy
        splines[5] = splines[4].polynomialSplineDerivative(); // ddy

        // Extracting the gradients as a function of position along the curve
        double[] knots = splines[0].getKnots();

        curvature = new TreeMap<>();
        double w = (double) loessNNeighbours/2d;
        for (int i=0;i<knots.length;i++) {
            double pos = knots[i];

            double minPos = Math.max(knots[0],pos-w);
            double maxPos = Math.min(knots[knots.length-1],pos+w);
            double width = maxPos-minPos;

            double dx = (splines[0].value(maxPos) - splines[0].value(minPos)) / width;
            double dy = (splines[1].value(maxPos) - splines[1].value(minPos)) / width;
            double ddx = (splines[2].value(maxPos) - splines[2].value(minPos)) / (0.5*width);
            double ddy = (splines[3].value(maxPos) - splines[3].value(minPos)) / (0.5*width);

            double k = (dx * ddy - dy * ddx) / Math.pow((dx * dx + dy * dy), 3d / 2d);

            curvature.put(knots[i],k);

        }
    }

    public TreeMap<Double,Double> getCurvature() {
        if (curvature == null) calculateCurvature();

        return  curvature;

    }

    public void showOverlay(ImagePlus ipl, int[] position, int lineWidth) {
        if (curvature == null) calculateCurvature();

        // Calculating maximum curvature
        double maxCurvature = Double.MIN_VALUE;
        for (double currentCurvature:curvature.values()) {
            maxCurvature = Math.max(Math.abs(currentCurvature),maxCurvature);
        }

        showOverlay(ipl,maxCurvature,position,lineWidth);

    }

    public void showOverlay(ImagePlus ipl, double maxCurvature, int[] position, double lineWidth) {
        if (curvature == null) calculateCurvature();

        double r = 2;
        Overlay ovl = ipl.getOverlay();
        if (ovl == null) ovl = new Overlay();

        if (curvature.size() < 2) {
            ipl.setOverlay(ovl);
            return;
        }

        Iterator<Double> iterator = curvature.keySet().iterator();
        double p2 = iterator.next();

        while (iterator.hasNext()) {
            double p1 = p2;
            p2 = iterator.next();

            double x1 = splines[0].value(p1)+0.5;
            double y1 = splines[1].value(p1)+0.5;
            double x2 = splines[0].value(p2)+0.5;
            double y2 = splines[1].value(p2)+0.5;

            double k1 = Math.abs(curvature.get(p1));
            double k2 = Math.abs(curvature.get(p2));
            double k = (k1+k2)/2;
            double b = k/(maxCurvature*1.5);
            Color color = Color.getHSBColor((float) b,1f,1f);

            Line line = new Line(x1,y1,x2,y2);
            line.setStrokeWidth(lineWidth);
            line.setStrokeColor(color);
            line.setPosition(position[2]);
            ovl.addElement(line);

        }



//        for (Double pos:curvature.keySet()) {
//            double x = splines[0].value(pos);
//            double y = splines[1].value(pos);
//
//            double b = curvature.get(pos)/(maxCurvature*1.5);
//            Color color = Color.getHSBColor((float) b,1f,1f);
//
//            OvalRoi ovr = new OvalRoi(x-r/2+0.5,y-r /2+0.5, r, r);
//            ovr.setStrokeWidth(1d);
//            ovr.setPosition(position[2]);
//            ovr.setFillColor(color);
//            ovl.addElement(ovr);
//
//        }

        ipl.setOverlay(ovl);

    }

    public int getLoessNNeighbours() {
        return loessNNeighbours;
    }

    public void setLoessNNeighbours(int loessNNeighbours) {
        this.loessNNeighbours = loessNNeighbours;

        // Calculating the bandwidth
        loessBandwidth = (double) loessNNeighbours/(double) path.size();

        // Keeping the bandwidth within limits
        if (loessBandwidth < 0) loessBandwidth = 0;
        if (loessBandwidth > 1) loessBandwidth = 1;

    }

    public double getLoessBandwidth() {
        return loessBandwidth;
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

    public FittingMethod getFittingMethod() {
        return fittingMethod;
    }

    public void setFittingMethod(FittingMethod fittingMethod) {
        this.fittingMethod = fittingMethod;
    }

    public LinkedHashSet<Vertex> getSpline() {
        if (splines == null) calculateCurvature();

        LinkedHashSet<Vertex> spline = new LinkedHashSet<>();

        for (double t:splines[0].getKnots()) {
            int x = (int) Math.round(splines[0].value(t));
            int y = (int) Math.round(splines[1].value(t));

            spline.add(new Vertex(x,y,0));

        }

        return spline;

    }
}
