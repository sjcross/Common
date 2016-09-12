//TODO: Add voxel-based surface area.
//TODO: Add ability to fit 3D ellipsoid (partially completed already using BoneJ library)

package com.wolfson.common.Object;

import org.apache.commons.math3.geometry.euclidean.threed.*;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.stat.descriptive.rank.Min;
import org.doube.geometry.Ellipsoid;
import org.doube.geometry.FitEllipse;
import quickhull3d.Point3d;
import quickhull3d.QuickHull3D;
import java.util.ArrayList;

import static com.wolfson.common.MathFunc.ArrayFunc.uniqueRows;
import static com.wolfson.common.MathFunc.GeneralOps.ppdist;
import static org.doube.geometry.FitEllipsoid.inertia;

/**
 * Created by sc13967 on 15/08/2016.
 */
public class Blob{
    public static final String OBJ_ID = "Object ID";
    public static final String VOX_VOL = "Voxel volume";
    public static final String PROJ_AREA = "Projected area (2D)";
    public static final String HEIGHT = "Height (z-axis range)";
    public static final String ELLIPSE_THETA = "Ellipse theta (rel. x-axis)";
    public static final String HULL_VOL = "Hull volume";
    public static final String HULL_SURF = "Hull surface area";
    public static final String SPHERICITY = "Sphericity";
    public static final String SOLIDITY = "Solidity";
    public static final String LC_LENGTH = "Longest chord length";
    public static final String LMAPC_LENGTH = "Longest major perp. chord length";
    public static final String LMIPC_LENGTH = "Longest minor perp. chord length";
    public static final String ECCENTRICITY1 = "Eccentricity1 (LC/AV[LMaPC,LMiPC])";
    public static final String ECCENTRICITY2 = "Eccentricity2 (AV[LC,LMaPC]/LMiPC)";
    public static final String LC_THETA = "Longest chord theta (rel. x-axis)";
    public static final String LC_PHI = "Longest chord phi (rel. xy-plane)";
    public static final String LMAPC_THETA = "Major perp. chord theta (rel. x-axis)";
    public static final String LMAPC_PHI = "Major perp. chord theta (rel. xy-plane)";
    public static final String LMIPC_THETA = "Minor perp. chord theta (rel. x-axis)";
    public static final String LMIPC_PHI = "Minor perp. chord theta (rel. xy-plane)";
    public static final String EULER_ALPHA = "Euler angle alpha";
    public static final String EULER_BETA = "Euler angle beta";
    public static final String EULER_GAMMA = "Euler angle gamma";
    public static final int MAX_RANGE = 0; //Use full intensity range
    public static final int ZERO_180 = 1; //Use range 0-180 (for orientations)
    public static final int DEGREES = 2; //Use degrees
    public static final int RADIANS = 3; //Use degrees

    int ID;
    double V;
    double SA;
    double[][] LC = new double[2][3]; //Longest chord
    double[][] LMaPC = new double[2][3]; //Longest major perpendicular chord
    double[][] LMiPC = new double[2][3]; //Longest minor perpendicular chord
    double[] e2d = new double[6]; //Coefficients of ellipse fitting equation
    boolean hull_cnd = false; //True when convex hull built
    boolean vol_cnd = false; //True when volume calculated
    boolean surf_cnd = false; //True when surface area calculated
    boolean LC_cnd = false; //True when longest chord calculated
    boolean ellipse_cnd = false; //True when an ellipse has been calculated
    boolean LMaPC_cnd = false; //True when longest major perpendicular chord calculated
    boolean LMiPC_cnd = false; //True when longest minor perpendicular chord calculated
    static int ang_unit = RADIANS; //Angular unit to use

    Point3d[] pts;
    QuickHull3D hull;

    ArrayList<Double> x = new ArrayList<Double>();
    ArrayList<Double> y = new ArrayList<Double>();
    ArrayList<Double> z = new ArrayList<Double>();

    public Blob(int ID) {
        this.ID = ID;

    }

    public static void setAngleMode(int mode) {
        ang_unit = mode;

    }

    public void addCoord(double x_in, double y_in, double z_in) {
        x.add(x_in);
        y.add(y_in);
        z.add(z_in);

    }

    public int getID() {
        return ID;
    }

    public double[] getX() {
        double[] x_coords = new double[x.size()];

        for (int i=0;i<x.size();i++) {
            x_coords[i] = x.get(i);
        }

        return x_coords;
    }

    public double[] getY() {
        double[] y_coords = new double[y.size()];

        for (int i=0;i<y.size();i++) {
            y_coords[i] = y.get(i);
        }

        return y_coords;
    }

    public double[] getZ() {
        double[] z_coords = new double[z.size()];

        for (int i=0;i<z.size();i++) {
            z_coords[i] = z.get(i);
        }

        return z_coords;
    }

    public double getXMean() {
        double x_mean = new Mean().evaluate(getX());

        return x_mean;

    }

    public double getYMean() {
        double y_mean = new Mean().evaluate(getY());

        return y_mean;

    }

    public double getZMean() {
        double z_mean = new Mean().evaluate(getZ());

        return z_mean;

    }

    public double getHeight() {
        double[] z = getZ();

        double min_z = new Min().evaluate(z);
        double max_z = new Max().evaluate(z);

        double height = max_z - min_z;

        return height;

    }

    public double[] getExtents() {
        //Minimum and maximum values for all dimensions [x_min, y_min, z_min; x_max, y_max, z_max]
        double[] extents = new double[6];

        double[] x = getX();
        double[] y = getY();
        double[] z = getZ();

        extents[0] = new Min().evaluate(x);
        extents[1] = new Max().evaluate(x);
        extents[2] = new Min().evaluate(y);
        extents[3] = new Max().evaluate(y);
        extents[4] = new Min().evaluate(z);
        extents[5] = new Max().evaluate(z);

        return extents;

    }

    public boolean hasVolume() {
        //True if all dimension (x,y,z) are > 0

        double[] extents = getExtents();

        boolean hasvol = false;

        if (extents[1]-extents[0] > 0 & extents[3]-extents[2] > 0 & extents[5]-extents[4] > 0) {
            hasvol = true;
        }

        return hasvol;
    }

    public boolean hasArea() {
            //True if all dimension (x,y) are > 0

            double[] extents = getExtents();

            boolean hasarea = false;

            if (extents[1]-extents[0] > 0 & extents[3]-extents[2] > 0) {
                hasarea = true;
            }

            return hasarea;

    }

    public double getVoxelVolume() {
        return x.size();

    }

    public double getProjectedPixels() {
        double[] x = getX();
        double[] y = getY();
        double[][] coords = new double[x.length][2];

        for (int i=0;i<x.length;i++) {
            coords[i][0] = x[i];
            coords[i][1] = y[i];
        }

        coords = uniqueRows(coords);

        return coords.length;

    }

    public void fitEllipse() {
        //Uses FitEllipse class from BoneJ
        double[] x = getX();
        double[] y = getY();
        double[][] coords = new double[x.length][2];

        for (int i=0;i<x.length;i++) {
            coords[i][0] = x[i];
            coords[i][1] = y[i];
        }

        coords = uniqueRows(coords);
        e2d = FitEllipse.direct(coords);

        ellipse_cnd = true;

    }

    public double[] getEllipseFit() {
        return e2d;

    }

    public double getEllipseTheta() {
        if (!ellipse_cnd) {
            fitEllipse();
        }

        double th = 0;
        if (e2d[1]==0 & e2d[0]<e2d[2]) {
            th = 0;

        } else if (e2d[1]==0 & e2d[0]>e2d[2]) {
            th = 90;

        } else if (e2d[1]!=0 & e2d[0]>0) {
            th = Math.toDegrees(-Math.atan((e2d[2]-e2d[0]-Math.sqrt(Math.pow(e2d[0]-e2d[2],2)+Math.pow(e2d[1],2)))/e2d[1]));

        }  else {
            double val = -Math.atan((e2d[2]-e2d[0]-Math.sqrt(Math.pow(e2d[0]-e2d[2],2)+Math.pow(e2d[1],2)))/e2d[1]) + Math.PI/2;
            th = Math.toDegrees(((val+Math.PI/2)%Math.PI)-Math.PI/2);

        }

        return th;

    }

    public boolean fitConvexHull() {
        boolean hasvol = hasVolume();
        double vol = getVoxelVolume();

        if (hasvol & vol > 4) {
            //Adding coordinates to Point3d structure
            pts = new Point3d[x.size()];
            for (int i = 0; i < x.size(); i++) {
                pts[i] = new Point3d(x.get(i), y.get(i), z.get(i));
            }

            hull = new QuickHull3D();
            hull.build(pts);
            hull.triangulate(); //Converts all faces to triangles

            hull_cnd = true;
        }

        return hasvol;

    }

    public Point3d[] getHullVertices() {

        return hull.getVertices();

    }

    public int[][] getHullFaces() {
        return hull.getFaces()
                ;
    }

    public double getHullSurfaceArea() {
        if (!surf_cnd) {
            calculateHullSurfaceArea();
        }

        return SA;
    }

    public void calculateHullSurfaceArea() {
        if (!hull_cnd) {
            fitConvexHull();
        }

        Point3d[] verts = hull.getVertices();
        int[][] faces = hull.getFaces();

        //Using Heron's formula
        SA = 0;

        for (int i=0;i<faces.length;i++) {
            double[] a = {verts[faces[i][0]].get(0),verts[faces[i][0]].get(1),verts[faces[i][0]].get(2)};
            double[] b = {verts[faces[i][1]].get(0),verts[faces[i][1]].get(1),verts[faces[i][1]].get(2)};
            double[] c = {verts[faces[i][2]].get(0),verts[faces[i][2]].get(1),verts[faces[i][2]].get(2)};

            double l1 = ppdist(a,b);
            double l2 = ppdist(a,c);
            double l3 = ppdist(b,c);

            double s = (l1+l2+l3)/2; //The semiperimeter of the polygon

            SA += Math.sqrt(s*(s-l1)*(s-l2)*(s-l3));
        }

        surf_cnd = true;
    }

    public double getHullVolume() {
        if (!vol_cnd) {
            calculateHullVolume();
        }

        return V;
    }

    public void calculateHullVolume() {
        if (!hull_cnd) {
            fitConvexHull();
        }

        Point3d[] verts = hull.getVertices();
        int[][] faces = hull.getFaces();

        //The volume of each polyhedron (coordinate origin to each vertex of the current face) is calculated as the one
        //sixth the volume of a parallelepiped.  These are summed together.  As long as the face vertices are
        //counted in the same manner the positive and negative volumes should cancel out.

        V = 0; //Keeping track of the object volume
        for (int i=0;i<faces.length;i++) {
            double[] a = {verts[faces[i][0]].get(0),verts[faces[i][0]].get(1),verts[faces[i][0]].get(2)};
            double[] b = {verts[faces[i][1]].get(0),verts[faces[i][1]].get(1),verts[faces[i][1]].get(2)};
            double[] c = {verts[faces[i][2]].get(0),verts[faces[i][2]].get(1),verts[faces[i][2]].get(2)};

            double[][] matrix_data = {{a[0],b[0],c[0]} , {a[1],b[1],c[1]} , {a[2],b[2],c[2]}};
            RealMatrix rm = MatrixUtils.createRealMatrix(matrix_data);
            LUDecomposition lud = new LUDecomposition(rm);

            V += lud.getDeterminant();
        }

        V /= 6;
        vol_cnd = true;
    }

    public double getSphericity() {
        if (!vol_cnd) {
            calculateHullVolume();
        }
        if (!surf_cnd) {
            calculateHullSurfaceArea();
        }

        double sph = Math.pow(Math.PI,(double) 1/3)*Math.pow(6*V,(double) 2/3)/SA;

        return sph;
    }

    public double getSolidity() {
        double solidity = getVoxelVolume()/getHullVolume();

        return solidity;

    }

    public void calculateLC() {
        //Reference for use as orientation descriptor: "Computer-Assisted Microscopy: The Measurement and Analysis of
        //Images", John C. Russ, Springer, 6 Dec 2012

        if (!hull_cnd) {
            fitConvexHull();
        }

        Point3d[] verts = hull.getVertices();

        double len = 0;
        for (int i=0;i<verts.length;i++) {
            for (int j=i+1;j<verts.length;j++) {
                double[] a = {verts[i].get(0),verts[i].get(1),verts[i].get(2)};
                double[] b = {verts[j].get(0),verts[j].get(1),verts[j].get(2)};
                double pp = ppdist(a,b);

                if (pp > len) {
                    len = pp;
                    LC[0][0] = a[0];
                    LC[0][1] = a[1];
                    LC[0][2] = a[2];
                    LC[1][0] = b[0];
                    LC[1][1] = b[1];
                    LC[1][2] = b[2];
                }
            }
        }

        LC_cnd = true;
    }

    public double[][] getLCCoords() {
        if (!LC_cnd) {
            calculateLC();
        }

        return LC;
    }

    public double getLCLength() {
        if (!LC_cnd) {
            calculateLC();
        }

        double pp = ppdist(new double[]{LC[0][0], LC[0][1], LC[0][2]},new double[]{LC[1][0], LC[1][1], LC[1][2]});

        return pp;
    }

    public void calculateLMaPC() {
        if (!LC_cnd) {
            calculateLC();
        }

        Point3d[] verts = hull.getVertices();
        int[][] faces = hull.getFaces();

        double len = 0;
        for (int i=0;i<verts.length;i++) {
            //The current point being tested (runs through all points in verts list)
            double[] a = {verts[i].get(0), verts[i].get(1), verts[i].get(2)};
            Vector3D va = new Vector3D(a[0], a[1], a[2]);

            //The longest chord line, offset so the current point being tested is at the origin
            Vector3D v1 = new Vector3D(LC[0][0], LC[0][1], LC[0][2]);
            Vector3D v2 = new Vector3D(LC[1][0], LC[1][1], LC[1][2]);
            Line chord = new Line(v1, v2, 0);

            //Plane with normal coincident to longest chord and encompassing test point
            Vector3D vt = new Vector3D(a[0], a[1], a[2]);
            Vector3D norm = chord.getDirection();
            Plane plane = new Plane(vt, norm, 0);

            for (int j = 0; j < faces.length; j++) {
                //Plane encompassing the current face being tested
                Vector3D vt1 = new Vector3D(verts[faces[j][0]].get(0), verts[faces[j][0]].get(1), verts[faces[j][0]].get(2));
                Vector3D vt2 = new Vector3D(verts[faces[j][1]].get(0), verts[faces[j][1]].get(1), verts[faces[j][1]].get(2));
                Vector3D vt3 = new Vector3D(verts[faces[j][2]].get(0), verts[faces[j][2]].get(1), verts[faces[j][2]].get(2));
                Plane face = new Plane(vt1, vt2, vt3, 1E-5);

                //Intersection of possible positions plane and face
                Line inter = face.intersection(plane);
                ArrayList<Vector3D> i_pts = new ArrayList<Vector3D>(); //Real intersection points

                //Intersection points of plane and face edges
                Line l1 = new Line(vt1, vt2, 1E-5);
                Vector3D i1 = inter.intersection(l1);
                if (i1 != null) { //It lies somewhere on the line
                    if (Vector3D.distance(i1, vt1) < Vector3D.distance(vt1, vt2) & Vector3D.distance(i1, vt2) < Vector3D.distance(vt1, vt2)) {
                        i_pts.add(i1);
                    }
                }

                Line l2 = new Line(vt1, vt3, 1E-5);
                Vector3D i2 = inter.intersection(l2);
                if (i2 != null) { //It lies somewhere on the line
                    if (Vector3D.distance(i2, vt1) < Vector3D.distance(vt1, vt3) & Vector3D.distance(i2, vt3) < Vector3D.distance(vt1, vt3)) {
                        i_pts.add(i2);
                    }
                }

                Line l3 = new Line(vt2, vt3, 1E-5);
                Vector3D i3 = inter.intersection(l3);
                if (i3 != null) { //It lies somewhere on the line
                    if (Vector3D.distance(i3, vt2) < Vector3D.distance(vt2, vt3) & Vector3D.distance(i3, vt3) < Vector3D.distance(vt2, vt3)) {
                        i_pts.add(i3);
                    }
                }

                if (i_pts.size() > 1) {
                    double d1 = Vector3D.distance(va, i_pts.get(0));
                    double d2 = Vector3D.distance(va, i_pts.get(1));

                    if (d1 > d2 & d1 > len) {
                        len = d1;
                        LMaPC[0][0] = a[0];
                        LMaPC[0][1] = a[1];
                        LMaPC[0][2] = a[2];
                        LMaPC[1][0] = i_pts.get(0).getX();
                        LMaPC[1][1] = i_pts.get(0).getY();
                        LMaPC[1][2] = i_pts.get(0).getZ();

                    } else if (d2 >= d1 & d2 > len) {
                        len = d2;
                        LMaPC[0][0] = a[0];
                        LMaPC[0][1] = a[1];
                        LMaPC[0][2] = a[2];
                        LMaPC[1][0] = i_pts.get(1).getX();
                        LMaPC[1][1] = i_pts.get(1).getY();
                        LMaPC[1][2] = i_pts.get(1).getZ();

                    }
                }
            }
        }

        if (len == 0) {
            LMaPC[0][0] = Double.NaN;
            LMaPC[0][1] = Double.NaN;
            LMaPC[0][2] = Double.NaN;
            LMaPC[1][0] = Double.NaN;
            LMaPC[1][1] = Double.NaN;
            LMaPC[1][2] = Double.NaN;

        }

        LMaPC_cnd = true;
    }

    public double[][] getLMaPCCoords() {
        if (!LMaPC_cnd) {
            calculateLMaPC();
        }

        return LMaPC;
    }

    public double getLMaPCLength() {
        if (!LMaPC_cnd) {
            calculateLMaPC();
        }

        double pp = ppdist(new double[]{LMaPC[0][0], LMaPC[0][1], LMaPC[0][2]},new double[]{LMaPC[1][0], LMaPC[1][1], LMaPC[1][2]});

        return pp;
    }

    public void calculateLMiPC() {
        if (!LMaPC_cnd) {
            calculateLMaPC();
        }

        Point3d[] verts = hull.getVertices();
        int[][] faces = hull.getFaces();

        double len = 0;
        for (int i=0;i<verts.length;i++) {
            //The current point being tested (runs through all points in verts list)
            double[] a = {verts[i].get(0), verts[i].get(1), verts[i].get(2)};

            //The plane encompassing longest chord (LC) and longest major perpendicular chord (LMaPC)
            Vector3D v1 = new Vector3D(LC[0][0] - a[0], LC[0][1] - a[1], LC[0][2] - a[2]);
            Vector3D v2 = new Vector3D(LC[1][0] - a[0], LC[1][1] - a[1], LC[1][2] - a[2]);
            Vector3D v3 = new Vector3D(LMaPC[1][0] - LMaPC[0][0] + LC[0][0] - a[0], LMaPC[1][1] - LMaPC[0][1] + LC[0][1]  - a[1], LMaPC[1][2] - LMaPC[0][2] + LC[0][2] - a[2]);
            Plane plane = new Plane(v1, v2, v3, 0);

            //Coordinates of the point on the longest chord closest to the test point (reset to correct origin)
            double[] ori = {plane.getOrigin().getX() + a[0], plane.getOrigin().getY() + a[1], plane.getOrigin().getZ() + a[2]};

            //A line passing from the test point, through the closest point on the plane
            Vector3D lv1 = new Vector3D(a[0], a[1], a[2]);
            Vector3D lv2 = new Vector3D(ori[0], ori[1], ori[2]);

            //Following condition prevents error if testing point on longest chord
            if (!lv1.equals(lv2)) {
                Line testchord = new Line(lv1, lv2, 0);

                for (int j = 0; j < faces.length; j++) {
                    //Plane encompassing the current face being tested
                    Vector3D vt1 = new Vector3D(verts[faces[j][0]].get(0), verts[faces[j][0]].get(1), verts[faces[j][0]].get(2));
                    Vector3D vt2 = new Vector3D(verts[faces[j][1]].get(0), verts[faces[j][1]].get(1), verts[faces[j][1]].get(2));
                    Vector3D vt3 = new Vector3D(verts[faces[j][2]].get(0), verts[faces[j][2]].get(1), verts[faces[j][2]].get(2));
                    Plane face = new Plane(vt1, vt2, vt3, 1E-5);

                    //Intersection of face plane and line from test point through origin
                    Vector3D inter = face.intersection(testchord);

                    //Checking the line intersects the face
                    if (inter != null) {
                        boolean run_test = false;
                        if (inter.distance(vt1) == 0 | inter.distance(vt2) == 0 | inter.distance(vt3) == 0 ) {
                            run_test = true;

                        } else {
                            //Shifting coordinates of face vertices such that test point is at the origin
                            Vector3D vts1 = new Vector3D(verts[faces[j][0]].get(0) - inter.getX(), verts[faces[j][0]].get(1) - inter.getY(), verts[faces[j][0]].get(2) - inter.getZ());
                            Vector3D vts2 = new Vector3D(verts[faces[j][1]].get(0) - inter.getX(), verts[faces[j][1]].get(1) - inter.getY(), verts[faces[j][1]].get(2) - inter.getZ());
                            Vector3D vts3 = new Vector3D(verts[faces[j][2]].get(0) - inter.getX(), verts[faces[j][2]].get(1) - inter.getY(), verts[faces[j][2]].get(2) - inter.getZ());

                            //Vertex lies within the face if the sum of angles between vertices adds to 2*pi
                            double theta1 = Vector3D.angle(vts1, vts2);
                            double theta2 = Vector3D.angle(vts2, vts3);
                            double theta3 = Vector3D.angle(vts3, vts1);

                            if (Math.abs(theta1 + theta2 + theta3 - 2 * Math.PI) < 1E-5) {
                                run_test = true;
                            }
                        }

                        if (run_test) {
                            double pp = Vector3D.distance(lv1, inter);
                            if (pp > len) {
                                len = pp;
                                LMiPC[0][0] = a[0];
                                LMiPC[0][1] = a[1];
                                LMiPC[0][2] = a[2];
                                LMiPC[1][0] = inter.getX();
                                LMiPC[1][1] = inter.getY();
                                LMiPC[1][2] = inter.getZ();
                            }
                        }
                    }
                }
            }
        }

        if (len == 0) {
            LMiPC[0][0] = Double.NaN;
            LMiPC[0][1] = Double.NaN;
            LMiPC[0][2] = Double.NaN;
            LMiPC[1][0] = Double.NaN;
            LMiPC[1][1] = Double.NaN;
            LMiPC[1][2] = Double.NaN;

        }

        LMiPC_cnd = true;
    }

    public double[][] getLMiPCCoords() {
        if (!LMiPC_cnd) {
            calculateLMiPC();
        }

        return LMiPC;
    }

    public double getLMiPCLength() {
        if (!LMiPC_cnd) {
            calculateLMiPC();
        }

        double pp = ppdist(new double[]{LMiPC[0][0], LMiPC[0][1], LMiPC[0][2]},new double[]{LMiPC[1][0], LMiPC[1][1], LMiPC[1][2]});

        return pp;
    }

    public boolean fixHandedness() {
        boolean fixed = false; //True if handedness needed to be fixed

        Vector3D xnorm = new Vector3D(LC[0][0]- LC[1][0],LC[0][1]- LC[1][1],LC[0][2]- LC[1][2]).normalize();
        Vector3D ynorm = new Vector3D(LMaPC[0][0]- LMaPC[1][0],LMaPC[0][1]- LMaPC[1][1],LMaPC[0][2]- LMaPC[1][2]).normalize();
        Vector3D znorm = new Vector3D(LMiPC[0][0]- LMiPC[1][0],LMiPC[0][1]- LMiPC[1][1],LMiPC[0][2]- LMiPC[1][2]).normalize();

        double dot = Vector3D.dotProduct(Vector3D.crossProduct(ynorm,znorm),xnorm);
        if (dot < 0) { //Antiparallel, so flip xnorm
            LC = new double[][]{{LC[1][0],LC[1][1],LC[1][2]},{LC[0][0],LC[0][1],LC[0][2]}};

            fixed = true;

        }

        return fixed;
    }

    public double getEccentricity1() {
        //Equation from http://www.cs.uu.nl/docs/vakken/ibv/reader/chapter8.pdf.
        //Averaging LMaPC and LMiPC axes
        double lc_dist = getLCLength();
        double lmapc_dist = getLMaPCLength();
        double lmipc_dist = getLMaPCLength();
        double mean_lpc_dist = new Mean().evaluate(new double[]{lmapc_dist,lmipc_dist});
        double eccentricity = lc_dist/mean_lpc_dist;

        return eccentricity;
    }

    public double getEccentricity2() {
        //Equation from http://www.cs.uu.nl/docs/vakken/ibv/reader/chapter8.pdf.
        //Averaging LC and LMaPC axes
        double lc_dist = getLCLength();
        double lmapc_dist = getLMaPCLength();
        double lmipc_dist = getLMaPCLength();
        double mean_lc_dist = new Mean().evaluate(new double[]{lc_dist,lmapc_dist});
        double eccentricity = mean_lc_dist/lmipc_dist;

        return eccentricity;
    }

    public double[] getLCOrientation() {
        //Orientation of the longest chord with respect to the x-axis

        if (!LC_cnd) {
            calculateLC();
        }

        double x = LC[0][0]- LC[1][0];
        double y = LC[0][1]- LC[1][1];
        double z = LC[0][2]- LC[1][2];
        double xy = ppdist(new double[]{LC[0][0], LC[0][1]},new double[]{LC[1][0], LC[1][1]});

        double[] orien = new double[2]; //Theta and phi
        orien[0] = -Math.atan(y/x); //Orientation relative to x axis
        orien[1] = Math.atan(z/xy); //Orientation relative to xy plane

        if (x <= 0) {
            orien[1] = -orien[1];
        }

        if (ang_unit == DEGREES) {
            orien[0] = orien[0]*180/Math.PI;
            orien[1] = orien[0]*180/Math.PI;
        }

        return orien;
    }

    public double[] getLMaPCOrientation() {
        //Orientation of the longest major perpendicular chord with respect to the y-axis

        if (!LMaPC_cnd) {
            calculateLMaPC();
        }

        double x = LMaPC[0][0]- LMaPC[1][0];
        double y = LMaPC[0][1]- LMaPC[1][1];
        double z = LMaPC[0][2]- LMaPC[1][2];
        double xy = ppdist(new double[]{LMaPC[0][0], LMaPC[0][1]},new double[]{LMaPC[1][0], LMaPC[1][1]});

        double[] orien = new double[2]; //Theta and phi
        orien[0] = -Math.atan(y/x); //Orientation relative to x axis
        orien[1] = Math.atan(z/xy); //Orientation relative to xy plane

        if (x <= 0) {
            orien[1] = -orien[1];
        }

        if (ang_unit == DEGREES) {
            orien[0] = orien[0]*180/Math.PI;
            orien[1] = orien[0]*180/Math.PI;
        }

        return orien;
    }

    public double[] getLMiPCOrientation() {
        //Orientation of the longest major perpendicular chord with respect to the y-axis

        if (!LMiPC_cnd) {
            calculateLMiPC();
        }

        double x = LMiPC[0][0]- LMiPC[1][0];
        double y = LMiPC[0][1]- LMiPC[1][1];
        double z = LMiPC[0][2]- LMiPC[1][2];
        double xy = ppdist(new double[]{LMiPC[0][0], LMiPC[0][1]},new double[]{LMiPC[1][0], LMiPC[1][1]});

        double[] orien = new double[2]; //Theta and phi
        orien[0] = -Math.atan(y/x); //Orientation relative to x axis
        orien[1] = Math.atan(z/xy); //Orientation relative to xy plane

        if (x <= 0) {
            orien[1] = -orien[1];
        }

        if (ang_unit == DEGREES) {
            orien[0] = orien[0]*180/Math.PI;
            orien[1] = orien[0]*180/Math.PI;
        }

        return orien;
    }

    public double[] getEulerAngles() {
        //Angles required to transform the object so LC aligns with the x-axis, LMaPC the y-axis and LMiPC the z-axis
        //Equations from https://en.wikipedia.org/wiki/Euler_angles (Accessed 2016-08-18)
        //These values haven't been checked to ensure they are correct

        Vector3D xnorm = new Vector3D(LC[0][0]- LC[1][0],LC[0][1]- LC[1][1],LC[0][2]- LC[1][2]).normalize();
        Vector3D ynorm = new Vector3D(LMaPC[0][0]- LMaPC[1][0],LMaPC[0][1]- LMaPC[1][1],LMaPC[0][2]- LMaPC[1][2]).normalize();
        Vector3D znorm = new Vector3D(LMiPC[0][0]- LMiPC[1][0],LMiPC[0][1]- LMiPC[1][1],LMiPC[0][2]- LMiPC[1][2]).normalize();

        double X[] = new double[]{xnorm.getX(), xnorm.getY(), xnorm.getZ()};
        double Y[] = new double[]{ynorm.getX(), ynorm.getY(), ynorm.getZ()};
        double Z[] = new double[]{znorm.getX(), znorm.getY(), znorm.getZ()};

        double[] euler = new double[3];
        euler[0] = Math.acos(Z[1]/Math.sqrt(1-Math.pow(Z[2],2)));
        euler[1] = Math.acos(Z[2]);
        euler[2] = Math.acos(Y[2]/Math.sqrt(1-Math.pow(Z[2],2)));

        return euler;
    }

    public void fitEllipsoid() {
        //Fitting an ellipsoid using method from BoneJ

        //Will look into this at a later date

        double[] x = getX();
        double[] y = getY();
        double[] z = getZ();
        double[][] coords = new double[x.length][3];

        for (int i=0;i<x.length;i++) {
            coords[i][0] = x[i];
            coords[i][1] = y[i];
            coords[i][2] = z[i];
        }

        Ellipsoid ell = inertia(coords);
        System.out.println("Volume: "+String.valueOf(ell.getVolume()));
        double[] cent = ell.getCentre();
        System.out.println("Centre: "+String.valueOf(cent[0])+", "+String.valueOf(cent[1])+", "+String.valueOf(cent[2]));
        double[] rad = ell.getRadii();
        System.out.println("Radii: "+String.valueOf(rad[0])+", "+String.valueOf(rad[1])+", "+String.valueOf(rad[2]));
        double[][] rot = ell.getRotation();
        System.out.println("Rotation matrix: "+String.valueOf(rot[0][0]*180/Math.PI)+", "+String.valueOf(rot[1][0]*180/Math.PI)+", "+String.valueOf(rot[2][0]*180/Math.PI)+String.valueOf(rot[0][1]*180/Math.PI)+", "+String.valueOf(rot[1][1]*180/Math.PI)+", "+String.valueOf(rot[2][1]*180/Math.PI)+String.valueOf(rot[0][2]*180/Math.PI)+", "+String.valueOf(rot[1][2]*180/Math.PI)+", "+String.valueOf(rot[2][2]*180/Math.PI));




    }

    /**
     * Returns parameter as a single double value.
     * @param type
     * @return
     */
    public double getGenericValue(String type){
        double val = Double.NaN;

        if (type.equals(OBJ_ID)) {
            val = getID();

        } else if (type.equals(VOX_VOL)) {
                val = getVoxelVolume();

        } else if (type.equals(PROJ_AREA)) {
            if (hasVolume()) {
                val = getProjectedPixels();
            }

        } else if (type.equals(HEIGHT)) {
            if (hasVolume()) {
                val = getHeight();
            }

        } else if (type.equals(HULL_SURF)) {
            if (hasVolume()) {
                val = getHullSurfaceArea();
            }

        } else if (type.equals(SPHERICITY)) {
            if (hasVolume()) {
                val = getSphericity();
            }

        } else if (type.equals(SOLIDITY)) {
            if (hasVolume()) {
                val = getSolidity();
            }

        } else if (type.equals(LC_LENGTH)) {
            if (hasVolume()) {
                val = getLCLength();
            }

        } else if (type.equals(LMAPC_LENGTH)) {
            if (hasVolume()) {
                val = getLMaPCLength();
            }

        } else if (type.equals(LMIPC_LENGTH)) {
            if (hasVolume()) {
                val = getLMiPCLength();
            }

        } else if (type.equals(ECCENTRICITY1)) {
            if (hasVolume()) {
                val = getEccentricity1();
            }

        } else if (type.equals(ECCENTRICITY2)) {
            if (hasVolume()) {
                val = getEccentricity2();
            }

        } else if (type.equals(LC_THETA)) {
            if (hasVolume()) {
                double[] orien = getLCOrientation();
                val = orien[0];
            }

        } else if (type.equals(LC_PHI)) {
            if (hasVolume()) {
                double[] orien = getLCOrientation();
                val = orien[1];
            }

        } else if (type.equals(LMAPC_THETA)) {
            if (hasVolume()) {
                double[] orien = getLMaPCOrientation();
                val = orien[0];
            }

        } else if (type.equals(LMAPC_PHI)) {
            if (hasVolume()) {
                double[] orien = getLMaPCOrientation();
                val = orien[1];
            }

        } else if (type.equals(LMIPC_THETA)) {
            if (hasVolume()) {
                double[] orien = getLMiPCOrientation();
                val = orien[0];
            }

        } else if (type.equals(LMIPC_PHI)) {
            if (hasVolume()) {
                double[] orien = getLMiPCOrientation();
                val = orien[1];
            }

        } else if (type.equals(EULER_ALPHA)) {
            if (hasVolume()) {
                double[] euler = getEulerAngles();
                val = euler[0];
            }

        } else if (type.equals(EULER_BETA)) {
            if (hasVolume()) {
                double[] euler = getEulerAngles();
                val = euler[1];
            }

        } else if (type.equals(EULER_GAMMA)) {
            if (hasVolume()) {
                double[] euler = getEulerAngles();
                val = euler[2];
            }

        } else if (type.equals(ELLIPSE_THETA)) {
            if (hasArea()) {
                val = getEllipseTheta();
            }

        }

        return val;
    }

    public static int getRangeType(String type){
        int val = 0;

        if (type.equals(OBJ_ID)) {
            val = MAX_RANGE;

        } else if (type.equals(VOX_VOL)) {
            val = MAX_RANGE;

        } else if (type.equals(PROJ_AREA)) {
            val = MAX_RANGE;

        } else if (type.equals(HEIGHT)) {
            val = MAX_RANGE;

        } else if (type.equals(HULL_VOL)) {
            val = MAX_RANGE;

        } else if (type.equals(HULL_SURF)) {
            val = MAX_RANGE;

        } else if (type.equals(SPHERICITY)) {
            val = MAX_RANGE;

        } else if (type.equals(SOLIDITY)) {
            val = MAX_RANGE;

        } else if (type.equals(LC_LENGTH)) {
            val = MAX_RANGE;

        } else if (type.equals(LMAPC_LENGTH)) {
            val = MAX_RANGE;

        } else if (type.equals(LMIPC_LENGTH)) {
            val = MAX_RANGE;

        } else if (type.equals(ECCENTRICITY1)) {
            val = MAX_RANGE;

        } else if (type.equals(ECCENTRICITY2)) {
            val = MAX_RANGE;

        } else if (type.equals(LC_THETA)) {
            val = ZERO_180;

        } else if (type.equals(LC_PHI)) {
            val = ZERO_180;

        } else if (type.equals(LMAPC_THETA)) {
            val = ZERO_180;

        } else if (type.equals(LMAPC_PHI)) {
            val = ZERO_180;

        } else if (type.equals(LMIPC_THETA)) {
            val = ZERO_180;

        } else if (type.equals(LMIPC_PHI)) {
            val = ZERO_180;

        } else if (type.equals(EULER_ALPHA)) {
            val = MAX_RANGE;

        } else if (type.equals(EULER_BETA)) {
            val = MAX_RANGE;

        } else if (type.equals(EULER_GAMMA)) {
            val = MAX_RANGE;

        } else if (type.equals(ELLIPSE_THETA)) {
            val = ZERO_180;

        }

        return val;

    }
}