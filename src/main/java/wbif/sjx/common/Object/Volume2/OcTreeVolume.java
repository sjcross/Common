// TODO: Store a separate set of

package wbif.sjx.common.Object.Volume2;

import wbif.sjx.common.Exceptions.IntegerOverflowException;
import wbif.sjx.common.MathFunc.CumStat;
import wbif.sjx.common.Object.Point;
import wbif.sjx.common.Object.QuadTree.OTNode;
import wbif.sjx.common.Object.QuadTree.OcTree;
import wbif.sjx.common.Object.QuadTree.QTNode;
import wbif.sjx.common.Object.QuadTree.QuadTree;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.TreeSet;
import java.util.function.Consumer;

public class OcTreeVolume extends Volume2 {
    private final OcTree ocTree;


    public OcTreeVolume(Volume2 volume) {
        super(volume);

        ocTree = new OcTree(width, height, nSlices);
    }

    public OcTreeVolume(int width, int height, int nSlices, double dppXY, double dppZ, String calibratedUnits) {
        super(width, height, nSlices, dppXY, dppZ, calibratedUnits);

        ocTree = new OcTree(width, height, nSlices);
    }

    @Override
    public Volume2 add(int x, int y, int z) {
        // Adding this point
        ocTree.add(x, y, z);
        
//        ocTree.optimise();
        
        return this;

    }

    @Override
    public Volume2 add(Point<Integer> point) throws IntegerOverflowException {
        // Adding this point
        ocTree.add(point.getX(), point.getY(), point.getZ());
        
//        ocTree.optimise();
        
        return this;

    }

    @Override
    public void finalise() {
        ocTree.optimise();
    }

    @Override
    public TreeSet<Point<Integer>> getPoints() {
        return ocTree.getPoints();

    }

    @Override
    public Volume2 setPoints(TreeSet<Point<Integer>> points) {
        // Clearing all current points
        clearPoints();

        // Iterating over each point, adding it to the ocTree
        for (Point<Integer> point:points) {
            add(point.getX(),point.getY(),point.getZ());
        }

        return this;

    }

    @Override
    public void clearPoints() {
        ocTree.clear();
    }

    @Override
    public void calculateMeanCentroid()
    {
        CumStat csX = new CumStat();
        CumStat csY = new CumStat();
        CumStat csZ = new CumStat();

        meanOcTreeTraversal(ocTree.getRoot(), csX, csY, csZ, ocTree.getSize(), 0, 0, 0);

        meanCentroid = new Point<>(csX.getMean(),csY.getMean(),csZ.getMean());
    }

    private static void meanOcTreeTraversal(OTNode node, CumStat csX, CumStat csY, CumStat csZ, int size, int minX, int minY, int minZ)
    {
        if (node.isDivided())
        {
            final int halfSize = size / 2;
            final int midX = minX + halfSize;
            final int midY = minY + halfSize;
            final int midZ = minZ + halfSize;

            meanOcTreeTraversal(node.lnw, csX, csY, csZ, halfSize, minX, minY, minZ);
            meanOcTreeTraversal(node.lne, csX, csY, csZ, halfSize, midX, minY, minZ);
            meanOcTreeTraversal(node.lsw, csX, csY, csZ, halfSize, minX, midY, minZ);
            meanOcTreeTraversal(node.lse, csX, csY, csZ, halfSize, midX, midY, minZ);
            meanOcTreeTraversal(node.unw, csX, csY, csZ, halfSize, minX, minY, midZ);
            meanOcTreeTraversal(node.une, csX, csY, csZ, halfSize, midX, minY, midZ);
            meanOcTreeTraversal(node.usw, csX, csY, csZ, halfSize, minX, midY, midZ);
            meanOcTreeTraversal(node.use, csX, csY, csZ, halfSize, midX, midY, midZ);
        }
        else if (node.coloured)
        {
            final int halfSize = size / 2;

            csX.addMeasure(minX + halfSize, size * size);
            csY.addMeasure(minY + halfSize, size * size);
            csZ.addMeasure(minZ + halfSize, size * size);
        }
    }

    @Override
    public int size() {
        return ocTree.getPointCount();
    }

    @Override
    public double getProjectedArea(boolean pixelDistances) {
        TreeSet<Point> projectedPoints = new TreeSet<>();

        for (Point<Integer> point:this) {
            point.setZ(0);
            projectedPoints.add(point);
        }

        return pixelDistances ? projectedPoints.size() : projectedPoints.size()*dppXY*dppXY;
    }

    @Override
    public boolean contains(Point<Integer> point1) {
        return ocTree.contains(point1.x, point1.y, point1.z);
    }

    @Override
    public Volume2 createNewObject() {
        return new OcTreeVolume(this);
    }

    public void calculateSurface() {
        surface = ocTree.getEdgePoints();
        
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
        if (!(obj instanceof OcTreeVolume)) return false;

        OcTreeVolume volume2 = (OcTreeVolume) obj;
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

    @Override
    public Iterator<Point<Integer>> iterator() {
        return ocTree.iterator();
    }
}
