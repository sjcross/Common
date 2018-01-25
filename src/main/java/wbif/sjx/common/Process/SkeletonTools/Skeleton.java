package wbif.sjx.common.Process.SkeletonTools;

import wbif.sjx.common.Object.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by sc13967 on 24/01/2018.
 */
public class Skeleton extends HashSet<Vertex> {
    public Skeleton(int[] x, int[] y, int[] z) {
        // Creating an array of neighbours
        for (int i=0;i<x.length;i++) {
            add(new Vertex(x[i],y[i],z[i]));
        }
    }

    public HashSet<Vertex> getLongestPath() {
        HashSet<Vertex> longestPath = new HashSet<>();

        // Getting the vertices with one neighbour (those at branch ends)
        HashSet<Vertex> endPoints = getEndPoints();

        // For each end-to-end pair calculate the distance.  Only allows a vertex to be used once (to avoid loops).
//        HashMap<Vertex,Double> distances = new HashMap<>();
//        for (Vertex endPoint:endPoints) {
//            HashMap<Vertex,Double> currentDistances = distanceToAllVertices(endPoint);
//
//            // Calculating the longest path length
//            double longest = 0;
//            for (double distance:currentDistances.values()) {
//                longest = Math.max(distance,longest);
//            }
//
//            // Adding the longest path length to the distances map
//            distances.put(endPoint,longest);
//
//        }
//
//        // Getting the vertex with the longest node (there should be two with the same score - i.e. either ends of the same path)
//        double longest = 0;
//        Vertex startingPoint = null;
//        for (Vertex vertex:distances.keySet()) {
//            double distance = distances.get(vertex);
//            if (distance>longest) {
//                longest = distance;
//                startingPoint = vertex;
//            }
//        }
//
//        // Getting the path corresponding to the longest distance

        return null;

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

    public HashSet<Vertex> longestPathBetweenVertices(Vertex startingVertex, Vertex endingVertex) {
        HashSet<Vertex> neighbours = startingVertex.getNeighbours();
        for (Vertex neighbour:neighbours) {

        }
        return null;

    }
}
