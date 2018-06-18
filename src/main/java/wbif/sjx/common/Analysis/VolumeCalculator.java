package wbif.sjx.common.Analysis;

import com.github.quickhull3d.Point3d;
import com.github.quickhull3d.QuickHull3D;
import org.apache.commons.math3.geometry.euclidean.threed.Euclidean3D;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.PolyhedronsSet;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.RegionFactory;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wbif.sjx.common.MathFunc.GeneralOps;
import wbif.sjx.common.Object.Volume;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

public class VolumeCalculator {
    public static final int CENTROID = 0; //Fit hull around voxel centroids (hull volume < voxel volume)
    public static final int CORNER = 1; //Fit hull around voxel corners (hull volume > voxel volume)

    private Volume volume;
    private Point3d[] pts;
    private QuickHull3D hull;
    private double hullSurfaceArea = Double.NaN;
    private double hullVolume = Double.NaN;

    public VolumeCalculator(Volume volume, int fitMode) {
        this.volume = volume;

        switch (fitMode) {
            case CENTROID:
                fitConvexHull(CENTROID);
                break;

            case CORNER:
                fitConvexHull(CORNER);
                break;
        }
    }

    public boolean canFitHull() {
        return (volume.hasVolume() & volume.getNVoxels() > 4);
    }

    private void fitConvexHull(int fitMode) {
        if (!canFitHull()) return;

        double[] x = volume.getX(true);
        double[] y = volume.getY(true);
        double[] z = volume.getZ(true,true);

        switch (fitMode) {
            case CORNER:
                //Adding coordinates to Point3d structure
                pts = new Point3d[x.length * 8]; //Works on corners of each voxel
                for (int i=0;i<x.length;i++) {
                    pts[i * 8] = new Point3d(x[i]-0.5, y[i] - 0.5, z[i] - 0.5);
                    pts[i * 8 + 1] = new Point3d(x[i] + 0.5, y[i] - 0.5, z[i] - 0.5);
                    pts[i * 8 + 2] = new Point3d(x[i] - 0.5, y[i] + 0.5, z[i] - 0.5);
                    pts[i * 8 + 3] = new Point3d(x[i] + 0.5, y[i] + 0.5, z[i] - 0.5);
                    pts[i * 8 + 4] = new Point3d(x[i] - 0.5, y[i] - 0.5, z[i] + 0.5);
                    pts[i * 8 + 5] = new Point3d(x[i] + 0.5, y[i] - 0.5, z[i] + 0.5);
                    pts[i * 8 + 6] = new Point3d(x[i] - 0.5, y[i] + 0.5, z[i] + 0.5);
                    pts[i * 8 + 7] = new Point3d(x[i] + 0.5, y[i] + 0.5, z[i] + 0.5);

                }
                break;

            case CENTROID:
                pts = new Point3d[x.length]; //Works on corners of each voxel
                for (int i=0;i<x.length;i++) {
                    pts[i] = new Point3d(x[i], y[i], z[i]);
                }
                break;
        }

        hull = new QuickHull3D();

        // Certain point configurations (e.g. points in a line) will cause the fitting to fail
        try {
            hull.build(pts);
        } catch (IllegalArgumentException e) {
            return;
        }

        hull.triangulate(); //Converts all faces to triangles

    }

    public Point3d[] getHullVertices() {
        return hull.getVertices();

    }

    public int[][] getHullFaces() {
        return hull.getFaces();
    }

    public double getHullSurfaceArea(boolean pixelDistances) {
        if (!canFitHull()) return hullVolume; // Will return Double.NaN
        if (!Double.isNaN(hullSurfaceArea)) return hullSurfaceArea;

        Point3d[] verts = hull.getVertices();
        int[][] faces = hull.getFaces();

        //Using Heron's formula
        hullSurfaceArea = 0;

        for (int i = 0; i < faces.length; i++) {
            double[] a = {verts[faces[i][0]].get(0), verts[faces[i][0]].get(1), verts[faces[i][0]].get(2)};
            double[] b = {verts[faces[i][1]].get(0), verts[faces[i][1]].get(1), verts[faces[i][1]].get(2)};
            double[] c = {verts[faces[i][2]].get(0), verts[faces[i][2]].get(1), verts[faces[i][2]].get(2)};

            double l1 = GeneralOps.ppdist(a, b);
            double l2 = GeneralOps.ppdist(a, c);
            double l3 = GeneralOps.ppdist(b, c);

            double s = (l1 + l2 + l3) / 2; //The semiperimeter of the polygon

            hullSurfaceArea += Math.sqrt(s * (s - l1) * (s - l2) * (s - l3));
        }

        if (pixelDistances) {
            return hullSurfaceArea;
        } else {
            double dppXY = volume.getDistPerPxXY();
            return hullSurfaceArea*dppXY*dppXY;
        }
    }

    public double getHullVolume(boolean pixelDistances) {
        if (!canFitHull()) return hullVolume; // Will return Double.NaN
        if (!Double.isNaN(hullVolume)) return hullVolume;

        Point3d[] verts = hull.getVertices();
        int[][] faces = hull.getFaces();

        //The volume of each polyhedron (coordinate origin to each vertex of the current face) is calculated as the one
        //sixth the volume of a parallelepiped.  These are summed together.  As long as the face vertices are
        //counted in the same manner the positive and negative volumes should cancel out.
        hullVolume = 0; //Keeping track of the object volume
        for (int i = 0; i < faces.length; i++) {
            double[] a = {verts[faces[i][0]].get(0), verts[faces[i][0]].get(1), verts[faces[i][0]].get(2)};
            double[] b = {verts[faces[i][1]].get(0), verts[faces[i][1]].get(1), verts[faces[i][1]].get(2)};
            double[] c = {verts[faces[i][2]].get(0), verts[faces[i][2]].get(1), verts[faces[i][2]].get(2)};

            double[][] matrixData = {{a[0], b[0], c[0]}, {a[1], b[1], c[1]}, {a[2], b[2], c[2]}};
            RealMatrix rm = MatrixUtils.createRealMatrix(matrixData);
            LUDecomposition lud = new LUDecomposition(rm);

            hullVolume += lud.getDeterminant();
        }

        if (pixelDistances) {
            return hullVolume/6;
        } else {
            double dppXY = volume.getDistPerPxXY();
            return (dppXY*dppXY*dppXY*hullVolume/6);
        }
    }

    public double getSphericity() {
        if (!canFitHull()) return Double.NaN;
        if (Double.isNaN(hullVolume)) getHullVolume(true);
        if (Double.isNaN(hullSurfaceArea)) getHullSurfaceArea(true);

        return Math.pow(Math.PI, (double) 1 / 3) * Math.pow(6 * hullVolume, (double) 2 / 3) / hullSurfaceArea;

    }

    public double getSolidity() {
        if (!canFitHull()) return Double.NaN;
        if (Double.isNaN(hullVolume)) getHullVolume(true);

        return volume.getContainedVolume(true)/getHullVolume(true);

    }

    public Volume getContainedPoints() {
        if (!canFitHull()) return null;

        // Getting a list of vertices
        Point3d[] vertices = hull.getVertices();
        LinkedList<Vector3D> vectorList = new LinkedList<>();
        for (Point3d point3d:vertices) {
            vectorList.add(new Vector3D(point3d.x,point3d.y,(point3d.z/5)));
            System.out.println("    point: "+point3d.x+"_"+point3d.y+"_"+(point3d.z/5));
        }

        // Getting a list of facets
        int[][] facets = hull.getFaces(QuickHull3D.CLOCKWISE);
        LinkedList<int[]> facetsList = new LinkedList<>();
        for (int[] facet:facets) {
            facetsList.add(facet);
            System.out.println("    facet: "+facet[0]+"_"+facet[1]+"_"+facet[2]);
        }
//        facetsList.addAll(Arrays.asList(facets));

        // Creating the PolyhedronsSet
        PolyhedronsSet polyhedronsSet = new PolyhedronsSet(vectorList,facetsList,1E-2);

        // Testing which points are within the convex hull
        double[] extents = volume.getExtents(true,false);
        Volume insideHull = new Volume(volume.getDistPerPxXY(),volume.getDistPerPxZ(),volume.getCalibratedUnits(),volume.is2D());
        for (int x=(int) extents[0];x<=extents[1]+1;x++) {
            for (int y=(int) extents[2];y<=extents[3];y++) {
                for (int z=(int) extents[4];z<=extents[5];z++) {
                    Region.Location location = polyhedronsSet.checkPoint(new Vector3D(1,1,1));
                    if (location.equals(Region.Location.INSIDE)) {
                        insideHull.addCoord(x,y,z);
                        System.out.println("    Inside "+x+"_"+y+"_"+z);
                    } else {
                        System.out.println("    Outside "+x+"_"+y+"_"+z);
                    }
                }
            }
        }

        RegionFactory<Euclidean3D> regionFactory = new RegionFactory<>();
        ArrayList<Plane> planes = new ArrayList<>();
        

        PolyhedronsSet tree =
                (PolyhedronsSet) new RegionFactory<Euclidean3D>().buildConvex(
                        new Plane(vertex3, vertex2, vertex1, 1.0e-10),
                        new Plane(vertex2, vertex3, vertex4, 1.0e-10),
                        new Plane(vertex4, vertex3, vertex1, 1.0e-10),
                        new Plane(vertex1, vertex2, vertex4, 1.0e-10));

        return insideHull;

    }
}
