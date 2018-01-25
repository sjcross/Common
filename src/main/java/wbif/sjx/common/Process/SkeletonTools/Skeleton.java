package wbif.sjx.common.Process.SkeletonTools;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import wbif.sjx.common.Object.Vertex;
import wbif.sjx.common.Object.VertexCollection;

import java.util.*;

/**
 * Created by sc13967 on 24/01/2018.
 */
public class Skeleton extends VertexCollection {
    private double longestDistance;
    private LinkedHashSet<Vertex> longestPath;


    public static void main(String[] args) {
        new ImageJ();

        ImagePlus ipl = IJ.openImage("Y:\\Stephen\\People\\H\\Chrissy Hammond\\2018-01-16 Fish tracking\\FakeSkeleton.tif");
        int[] xx = new int[1024];
        int[] yy = new int[1024];
        int[] zz = new int[1024];

        int i = 0;
        for (int x=0;x<ipl.getWidth();x++) {
            for (int y=0;y<ipl.getHeight();y++) {
                if (ipl.getProcessor().getPixel(x,y) == 255) {
                    xx[i] = x;
                    yy[i] = y;
                    zz[i] = 0;
                    i++;
                }
            }
        }

        Skeleton skeleton = new Skeleton(xx,yy,zz);

        LinkedHashSet<Vertex> longestPath = skeleton.getLongestPath();

        SkeletonVisualiser skeletonVisualiser = new SkeletonVisualiser();
        skeletonVisualiser.drawPath(longestPath,ipl);
        ipl.show();

    }

    public Skeleton(int[] x, int[] y, int[] z) {
        // Creating an array of neighbours
        for (int i=0;i<x.length;i++) {
            add(new Vertex(x[i],y[i],z[i]));

        }

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

    public void addDistanceToNextVertex(Vertex currentVertex, double distance, LinkedHashSet<Vertex> currentPath) {
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
}
