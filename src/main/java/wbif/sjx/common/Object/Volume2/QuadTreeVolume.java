// TODO: Store a separate set of

package wbif.sjx.common.Object.Volume2;

import jdjf.quadTree.QuadTree;
import wbif.sjx.common.Object.Point;

import java.util.HashMap;
import java.util.TreeSet;

public class QuadTreeVolume extends Volume2 {
    private final HashMap<Integer, QuadTree> quadTrees;
    private final HashMap<Integer, QuadTree> surfaceQuadTrees;

    public QuadTreeVolume(double dppXY, double dppZ, String calibratedUnits, int width, int height, int nSlices) {
        super(dppXY, dppZ, calibratedUnits, width, height, nSlices);

        this.quadTrees = new HashMap<Integer, QuadTree>();
        this.surfaceQuadTrees = new HashMap<Integer, QuadTree>();

    }

    @Override
    public Volume2 addCoord(int x, int y, int z) {
        // Get relevant QuadTree
        quadTrees.putIfAbsent(z,new QuadTree(width,height));

        // Adding this point
        quadTrees.get(z).addPoint(x,y);

        return this;

    }

    @Override
    public TreeSet<Point<Integer>> getPoints() {
        TreeSet<Point<Integer>> points = new TreeSet<>();

        // Iterating over each slice
        for (int z:quadTrees.keySet()) {
            // Getting the QuadTree for this slice
            QuadTree quadTree = quadTrees.get(z);

            // Adding each point
            for (java.awt.Point point:quadTree.getPoints()) points.add(new Point<Integer>(point.x,point.y,z));

        }

        return points;

    }

    @Override
    public Volume2 setPoints(TreeSet<Point<Integer>> points) {
        // Clearing all current points
        clearPoints();

        // Iterating over each point, adding it to the quadtree
        for (Point<Integer> point:points) {
            addCoord(point.getX(),point.getY(),point.getZ());
        }

        return this;

    }

    @Override
    public void clearPoints() {
        for (QuadTree quadTree:quadTrees.values()) quadTree.clear();
    }

    @Override
    public TreeSet<Point<Integer>> getSurface() {
        if (surfaceQuadTrees.size() == 0) calculateSurface();

        TreeSet<Point<Integer>> points = new TreeSet<>();

        // Iterating over each slice
        for (int z:surfaceQuadTrees.keySet()) {
            // Getting the QuadTree for this slice
            QuadTree quadTree = surfaceQuadTrees.get(z);

            // Adding each point
            for (java.awt.Point point:quadTree.getPoints()) points.add(new Point<Integer>(point.x,point.y,z));

        }

        return points;

    }

    @Override
    public boolean is2D() {
        return nSlices == 1;
    }

    @Override
    public void calculateSurface() {
        System.err.println("QuadTreeVolume.calculateSurface needs writing");
    }
}
