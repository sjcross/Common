// TODO: Store a separate set of

package wbif.sjx.common.Object.Volume2;

import wbif.sjx.common.Exceptions.IntegerOverflowException;
import wbif.sjx.common.MathFunc.CumStat;
import wbif.sjx.common.Object.Point;
import wbif.sjx.common.Object.QuadTree.QTNode;
import wbif.sjx.common.Object.QuadTree.QuadTree;

import java.util.*;
import java.util.function.Consumer;

public class QuadTreeVolume extends Volume2 {
    private final TreeMap<Integer, QuadTree> quadTrees = new TreeMap<>();


    public QuadTreeVolume(Volume2 volume) {
        super(volume);
    }

    public QuadTreeVolume(int width, int height, int nSlices, double dppXY, double dppZ, String calibratedUnits) {
        super(width, height, nSlices, dppXY, dppZ, calibratedUnits);

    }

    @Override
    public Volume2 add(int x, int y, int z) {
        if (z >= nSlices) throw new IndexOutOfBoundsException("Coordinate out of bounds! (z: " + z + ")");

        quadTrees.putIfAbsent(z,new QuadTree(width,height));

        // Get relevant QuadTree
        QuadTree quadTree = quadTrees.get(z);

        // Adding this point
        quadTree.add(x, y);

//        quadTree.optimise();

        return this;

    }

    @Override
    public Volume2 add(Point<Integer> point) throws IntegerOverflowException {
        int z = point.z;
        if (z >= nSlices) throw new IndexOutOfBoundsException("Coordinate out of bounds! (z: " + z + ")");

        // Get relevant QuadTree
        quadTrees.putIfAbsent(point.z,new QuadTree(width,height));

        // Adding this point
        quadTrees.get(point.z).add(point.x,point.y);

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
            for (Point<Integer> point:quadTree.getPoints()) {
                point.setZ(z);
                points.add(point);
            }

        }

        return points;

    }

    @Override
    public Volume2 setPoints(TreeSet<Point<Integer>> points) {
        // Clearing all current points
        clearPoints();

        // Iterating over each point, adding it to the quadtree
        for (Point<Integer> point:points) {
            add(point.getX(),point.getY(),point.getZ());
        }

        return this;

    }

    @Override
    public void clearPoints() {
        for (QuadTree quadTree:quadTrees.values()) quadTree.clear();
    }

    @Override
    public void calculateSurface() {
        if (is2D()) {
            // Get the sole QuadTree
            QuadTree quadTree = quadTrees.values().iterator().next();

            // Set the surface list to the edge points of the QuadTree
            surface = quadTree.getEdgePoints();
        } else {
            clearSurface();

            // For each slice
            for (int z:quadTrees.keySet()) {
                QuadTree silceQT       = quadTrees.get(z);
                QuadTree belowSliceQT  = quadTrees.get(z-1);
                QuadTree aboveSliceQT  = quadTrees.get(z+1);

                // Add all the edge points for the slices QuadTree factoring in the neighbouring slices
                silceQT.getEdgePoints3D(surface, belowSliceQT, aboveSliceQT);
            }
        }
    }

    @Override
    public void calculateMeanCentroid()
    {
        CumStat csX = new CumStat();
        CumStat csY = new CumStat();
        CumStat csZ = new CumStat();

        for (int z:quadTrees.keySet())
        {
            QuadTree quadTree = quadTrees.get(z);

            meanQuadTreeTraversal(quadTree.getRoot(), csX, csY, quadTree.getSize(), 0, 0);

            csZ.addMeasure(z, quadTree.getPointCount());
        }

        meanCentroid = new Point<>(csX.getMean(),csY.getMean(),csZ.getMean());
    }

    private static void meanQuadTreeTraversal(QTNode node, CumStat csX, CumStat csY, int size, int minX, int minY)
    {
        if (node.isDivided())
        {
            final int halfSize = size / 2;
            final int midX = minX + halfSize;
            final int midY = minY + halfSize;

            meanQuadTreeTraversal(node.nw, csX, csY, halfSize, minX, minY);
            meanQuadTreeTraversal(node.ne, csX, csY, halfSize, midX, minY);
            meanQuadTreeTraversal(node.sw, csX, csY, halfSize, minX, midY);
            meanQuadTreeTraversal(node.se, csX, csY, halfSize, midX, midY);
        }
        else if (node.coloured)
        {
            final int halfSize = size / 2;

            csX.addMeasure(minX + halfSize, size);
            csY.addMeasure(minY + halfSize, size);
        }
    }

    @Override
    public double getHeight(boolean pixelDistances, boolean matchXY) {
        // With QuadTrees we can get the height more efficiently by just looking at the slices used
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (int z:quadTrees.keySet()) {
            if (quadTrees.get(z).getPointCount() == 0) continue;
            min = Math.min(min,z);
            max = Math.max(max,z);
        }

        int height = max-min;

        if (pixelDistances) return matchXY ? getXYScaledZ(height) : height;
        return height*dppZ;

    }

    @Override
    public int size() {
        int nVoxels = 0;

        for (QuadTree quadTree:quadTrees.values()) nVoxels += quadTree.getPointCount();

        return nVoxels;

    }

    @Override
    public double getProjectedArea(boolean pixelDistances) {
        System.out.println("wbif.sjx.common.Object.QuadTreeVolume getProjectedArea needs implementing");
        return 0;
    }

    @Override
    public boolean containsPoint(Point<Integer> point1) {
        if (!quadTrees.containsKey(point1.z)) return false;
        return quadTrees.get(point1.z).contains(point1.x,point1.y);

    }

    @Override
    public Volume2 createNewObject() {
        return new QuadTreeVolume(this);
    }

    @Override
    public Iterator<Point<Integer>> iterator() {
        return new QuadTreeVolumeIterator();
    }

    @Override
    public void forEach(Consumer<? super Point<Integer>> action) {
        System.out.println("wbif.sjx.common.Object.QuadTreeVolume forEach needs implementing");
    }

    @Override
    public Spliterator<Point<Integer>> spliterator() {
        System.out.println("wbif.sjx.common.Object.QuadTreeVolume spliterator needs implementing");
        return null;
    }

    @Override
    public int hashCode() {
        int hash = 1;

        hash = 31*hash + ((Number) dppXY).hashCode();
        hash = 31*hash + ((Number) dppZ).hashCode();
        hash = 31*hash + calibratedUnits.toUpperCase().hashCode();

        for (Point<Integer> point:this) hash = 31*hash + point.hashCode();

        return hash;

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof QuadTreeVolume)) return false;

        QuadTreeVolume volume2 = (QuadTreeVolume) obj;
        if (size() != volume2.size()) return false;

        if (dppXY != volume2.getDppXY()) return false;
        if (dppZ != volume2.getDppZ()) return false;
        if (!calibratedUnits.toUpperCase().equals(volume2.getCalibratedUnits().toUpperCase())) return false;

        Iterator<Point<Integer>> iterator2 = volume2.iterator();

        for (Point<Integer> point1:this) {
            if (!point1.equals(iterator2.next())) return false;
        }

        return true;

    }

    private class QuadTreeVolumeIterator implements Iterator<Point<Integer>> {
        private Iterator<Integer> sliceIterator = quadTrees.keySet().iterator();
        private Iterator<Point<Integer>> quadTreeIterator = null;
        private int z = 0;

        public QuadTreeVolumeIterator() {
            if (!sliceIterator.hasNext()) return;
            this.z = sliceIterator.next();
            quadTreeIterator = quadTrees.get(z).iterator();

        }

        @Override
        public boolean hasNext() {
            // Check we have an iterator
            if (quadTreeIterator == null) return false;

            // Check if this current slice has another point
            if (quadTreeIterator.hasNext()) return true;

            // If the current slice doesn't have another point, check if there is another slice
            if (!sliceIterator.hasNext()) return false;

            // Now we've got to access the next slice without incrementing the slice iterator
            Iterator<Integer> tempZIterator = quadTrees.keySet().iterator();
            while (tempZIterator.hasNext()) {
                int tempZ = tempZIterator.next();
                if (tempZ != z) continue;

                tempZ = tempZIterator.next();
                return quadTrees.get(tempZ).iterator().hasNext();

            }

            // Shouldn't get this far
            return false;

        }

        @Override
        public Point<Integer> next() {
            // If the current slice has another point, return this with the appropriate slice index
            if (quadTreeIterator.hasNext()) {
                Point<Integer> slicePoint = quadTreeIterator.next();
                return new Point<>(slicePoint.getX(),slicePoint.getY(),z);
            }

            if (sliceIterator.hasNext()) {
                this.z = sliceIterator.next();
                this.quadTreeIterator = quadTrees.get(z).iterator();
                Point<Integer> slicePoint = quadTreeIterator.next();
                return new Point<>(slicePoint.getX(),slicePoint.getY(),z);

            }

            return null;

        }
    }
}
