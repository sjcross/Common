package wbif.sjx.common.Process.SkeletonTools;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.OvalRoi;
import ij.gui.Overlay;
import org.apache.commons.math3.analysis.BivariateFunction;
import org.apache.commons.math3.analysis.interpolation.*;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.fitting.CurveFitter;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.optimization.DifferentiableMultivariateVectorMultiStartOptimizer;
import org.apache.commons.math3.optimization.DifferentiableMultivariateVectorOptimizer;
import wbif.sjx.common.Object.Point;
import wbif.sjx.common.Object.Vertex;
import wbif.sjx.common.Object.VertexCollection;
import wbif.sjx.common.Object.Volume;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by sc13967 on 24/01/2018.
 */
public class Skeleton extends VertexCollection {
    private double longestDistance;
    private LinkedHashSet<Vertex> longestPath;


    public static void main(String[] args) {
        new ImageJ();

        ImagePlus ipl = IJ.openImage("Y:\\Stephen\\People\\H\\Chrissy Hammond\\2018-01-16 Fish tracking\\Line.tif");
        ipl.show();

        Skeleton skeleton = new Skeleton(ipl);

        LinkedHashSet<Vertex> longestPath = skeleton.getLongestPath();
        PolynomialSplineFunction[] splines = skeleton.getSplines(longestPath,2,10);

        // Extracting the gradients as a function of position along the curve
        PolynomialSplineFunction dxSpline = splines[0].polynomialSplineDerivative();
        PolynomialSplineFunction ddxSpline = dxSpline.polynomialSplineDerivative();
        PolynomialSplineFunction dySpline = splines[1].polynomialSplineDerivative();
        PolynomialSplineFunction ddySpline = dySpline.polynomialSplineDerivative();

        double[] knots = splines[0].getKnots();
        double endPoint = knots[knots.length-1];

//        for (int i=0;i<endPoint;i++) {
//            double dx = dxSpline.value(i);
//            double ddx = ddxSpline.value(i);
//            double dy = dySpline.value(i);
//            double ddy = ddySpline.value(i);
//
//            kk[i] = Math.abs(dx*ddy-dy*ddx)/Math.pow((dx*dx+dy*dy),3d/2d);
//
//        }

        double pos = 0;
        double r = 2;
        Overlay ovl = new Overlay();
        for (int j=0;j<100000;j++) {
            if (!splines[0].isValidPoint(pos)) break;
            double xxx = splines[0].value(pos);
            double yyx = splines[1].value(pos);

            double dx = dxSpline.value(pos);
            double ddx = ddxSpline.value(pos);
            double dy = dySpline.value(pos);
            double ddy = ddySpline.value(pos);

            OvalRoi ovr = new OvalRoi(xxx-r/2+0.5,yyx-r /2+0.5, r, r);
            double b = Math.abs(dx*ddy-dy*ddx)/Math.pow((dx*dx+dy*dy),3d/2d)/0.15;
            ovr.setStrokeColor(Color.getHSBColor((float) b,1f,1f));
            ovl.addElement(ovr);
            ipl.setOverlay(ovl);

            pos = pos + 0.1;
        }
    }

    public Skeleton(ImagePlus ipl) {
        for (int x=0;x<ipl.getWidth();x++) {
            for (int y=0;y<ipl.getHeight();y++) {
                for (int z=0;z<ipl.getNSlices();z++) {
                    ipl.setPosition(1,z+1,1);
                    if (ipl.getProcessor().getPixel(x,y) == 0) {
                        add(new Vertex(x,y,z));
                    }
                }
            }
        }

        assignNeighbours();

    }

    public Skeleton(int[] x, int[] y, int[] z) {
        // Creating an array of neighbours
        for (int i=0;i<x.length;i++) {
            add(new Vertex(x[i],y[i],z[i]));

        }

        assignNeighbours();

    }

    public LinkedHashSet<Vertex> getLongestPath() {
        longestDistance = 0;
        longestPath = new LinkedHashSet<>();

        // Getting the vertices with one neighbour (those at branch ends)
        HashSet<Vertex> endPoints = getEndPoints();

        for (Vertex vertex : endPoints) {
            LinkedHashSet<Vertex> currentPath = new LinkedHashSet<>();
            addDistanceToNextVertex(vertex, 0, currentPath);
        }

        return longestPath;

    }

    public HashSet<Vertex> getEndPoints() {
        HashSet<Vertex> endPoints = new HashSet<>();

        // End points only have one neighbour
        for (Vertex vertex:this) {
            if (vertex.getNumberOfNeighbours() == 1) endPoints.add(vertex);
        }

        return endPoints;

    }

    public HashSet<Vertex> getBranchPoints() {
        HashSet<Vertex> branchPoints = new HashSet<>();

        // Branch points have 3 or more neighbours
        for (Vertex vertex:this) {
            if (vertex.getNumberOfNeighbours() >= 3) branchPoints.add(vertex);
        }

        return branchPoints;

    }

    public int[] getX() {
        return stream().mapToInt(Vertex::getX).toArray();
    }

    public int[] getY() {
        return stream().mapToInt(Vertex::getY).toArray();
    }

    public int[] getZ() {
        return stream().mapToInt(Vertex::getZ).toArray();
    }

    private void assignNeighbours() {
        // Assigning neighbours
        for (Vertex vertex1:this) {
            for (Vertex vertex2:this) {
                if (vertex1 == vertex2) continue;
                double distance = vertex1.getEdgeLength(vertex2);

                // The longest distance for 26-way connectivity is 1.73
                if (distance<1.75) vertex1.addNeighbour(vertex2);

            }
        }

        // There may be junctions with excessive linkages.  Identifying these and removing them.
        ArrayList<Vertex[]> linksToRemove = new ArrayList<>();
        for (Vertex vertex1:this) {
            if (vertex1.getNumberOfNeighbours() > 2) {
                // If the current vertex and a neighbour are connected to the same Vertex, keep the links with the
                // shortest length
                for (Vertex vertex2:vertex1.getNeighbours()) {
                    for (Vertex vertex3:vertex2.getNeighbours()) {
                        // Removing the longest link
                        if (vertex1.getNeighbours().contains(vertex3)) {
                            double v1v3 = vertex1.getEdgeLength(vertex3);
                            double v1v2 = vertex1.getEdgeLength(vertex2);
                            double v2v3 = vertex2.getEdgeLength(vertex3);

                            if (v1v2 > v1v3 && v1v2 > v2v3) {
                                linksToRemove.add(new Vertex[]{vertex1,vertex2});
                            } else if (v1v3 > v1v2 && v1v3 > v2v3) {
                                linksToRemove.add(new Vertex[]{vertex1, vertex3});
                            } else if (v2v3 > v1v2 && v2v3 > v1v3) {
                                linksToRemove.add(new Vertex[]{vertex2, vertex3});
                            }
                        }
                    }
                }
            }
        }

        for (Vertex[] link:linksToRemove) {
            link[0].removeNeighbour(link[1]);
            link[1].removeNeighbour(link[0]);
        }
    }

    private void addDistanceToNextVertex(Vertex currentVertex, double distance, LinkedHashSet<Vertex> currentPath) {
        // Making a new path for this point onwards
        LinkedHashSet<Vertex> newCurrentPath = new LinkedHashSet<>();
        newCurrentPath.addAll(currentPath);
        newCurrentPath.add(currentVertex);

        for (Vertex neighbourVertex:currentVertex.getNeighbours()) {
            // If this Vertex has already been traversed, skip it
            if (newCurrentPath.contains(neighbourVertex)) continue;

            // Calculating the distance to the new Vertex
            distance = distance + currentVertex.getEdgeLength(neighbourVertex);

            addDistanceToNextVertex(neighbourVertex,distance,newCurrentPath);

        }

        // If we've ended up at an end-point Vertex and the distance travelled is longer than the previously-assigned
        // longest distance assign this as the longest path
        if (currentVertex.getNumberOfNeighbours() == 1 && distance > longestDistance) {
            longestDistance = distance;
            longestPath = newCurrentPath;
        }
    }

    public PolynomialSplineFunction[] getSplines(LinkedHashSet<Vertex> path, int offset, int interval) {
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

        // Getting every nth point
        double[] tt = new double[(int) Math.ceil((double)(t.length-offset)/(double) interval)];
        double[] xx = new double[(int) Math.ceil((double)(t.length-offset)/(double) interval)];
        double[] yy = new double[(int) Math.ceil((double)(t.length-offset)/(double) interval)];
        double[] kk = new double[(int) Math.ceil((double)(t.length-offset)/(double) interval)];

        for (int i=0;i<tt.length;i++) {
            tt[i] = t[(i+offset)*interval];
            xx[i] = x[(i+offset)*interval];
            yy[i] = y[(i+offset)*interval];
        }

        // Fitting the spline pair
        SplineInterpolator interpolator = new SplineInterpolator();
        PolynomialSplineFunction[] splineFunctions = new PolynomialSplineFunction[2];
        splineFunctions[0] = interpolator.interpolate(tt,xx);
        splineFunctions[1] = interpolator.interpolate(tt,yy);

        return splineFunctions;

    }
}
