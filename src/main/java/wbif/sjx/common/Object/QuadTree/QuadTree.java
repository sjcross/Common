package wbif.sjx.common.Object.QuadTree;

import com.drew.lang.annotations.Nullable;
import wbif.sjx.common.Object.Point;

import java.awt.*;
import java.util.TreeSet;
import java.util.Iterator;

/**
 * Created by JDJFisher on 9/07/2019.
 */
public class QuadTree implements Iterable<Point<Integer>>
{
    private static final int CHUNK_SIZE = 2048;

    private int width;
    private int height;
    private int size;
    private int points;
    private int nodes;
    private QTNode root;

    // empty constructor
    public QuadTree(int width, int height)
    {
        this.width = width;
        this.height = height;
        points = 0;
        nodes = 1;
        size = 1;

        // set the size to the first power of 2 that is greater than both dimensions
        while (size < Math.max(width, height)) size <<= 1;

        root = new QTNode();
    }

    // pixel constructor
    public QuadTree(boolean[] pixels, int width, int height)
    {
        this(width, height);

        if (pixels.length != width * height) throw new IllegalArgumentException("Invalid array size");

        for (int i = 0; i < height; i+= CHUNK_SIZE)
        {
            for (int j = 0; j < width; j+= CHUNK_SIZE)
            {
                // iterate over chunk index range
                for (int y = i; y < i + CHUNK_SIZE && y < height; y++)
                {
                    for (int x = j; x < j + CHUNK_SIZE && x < width; x++)
                    {
                        if (pixels[x + y * width]) add(x, y);
                    }
                }

                // optimise chunk
                optimise();
            }
        }

        optimise();
    }

    // point constructor
    public QuadTree(TreeSet<Point<Integer>> points, int width, int height)
    {
        this(width, height);

        for (Point<Integer> p : points)
        {
            add(p.getX(), p.getY());
        }

        optimise();
    }

    // copy constructor
    public QuadTree(QuadTree qt)
    {
        width = qt.width;
        height = qt.height;
        size = qt.size;
        points = qt.points;
        nodes = qt.nodes;
        root = new QTNode(qt.root);
    }

    // determine whether there is a point stored at the specified coordinates
    public boolean contains(int x, int y)
    {
        if (x < 0 || x >= width)  throw new IndexOutOfBoundsException("Coordinate out of bounds! (x: " + x + ")");
        if (y < 0 || y >= height) throw new IndexOutOfBoundsException("Coordinate out of bounds! (y: " + y + ")");

        return contains(root, x, y, size, 0, 0);
    }

    private boolean contains(QTNode node, int x, int y, int size, int minX, int minY)
    {
        // recursively select the sub-quadrant that contains the coordinates until a leaf node is found
        if (node.isDivided())
        {
            final int halfSize = size / 2;
            final int midX = minX + halfSize;
            final int midY = minY + halfSize;

            if (x < midX && y < midY)
            {
                return contains(node.nw, x, y, halfSize, minX, minY);
            }
            else if (x >= midX && y < midY)
            {
                return contains(node.ne, x, y, halfSize, midX, minY);
            }
            else if (x < midX && y >= midY)
            {
                return contains(node.sw, x, y, halfSize, minX, midY);
            }
            else
            {
                return contains(node.se, x, y, halfSize, midX, midY);
            }
        }

        // return the value of the leaf
        return node.coloured;
    }

    // add a point to the structure
    public void add(int x, int y)
    {
        set(x, y, true);
    }

    // remove a point from the structure
    public void remove(int x, int y)
    {
        set(x, y, false);
    }

    // set the point state for a given coordinate pair
    public void set(int x, int y, boolean b)
    {
        if (x < 0 || x >= width)  throw new IndexOutOfBoundsException("Coordinate out of bounds! (x: " + x + ")");
        if (y < 0 || y >= height) throw new IndexOutOfBoundsException("Coordinate out of bounds! (y: " + y + ")");

        set(root, x, y, b, size, 0, 0);
    }

    private void set(QTNode node, int x, int y, boolean b, int size, int minX, int minY)
    {
        if (node.isLeaf())
        {
            if (node.coloured == b) return;

            if (size == 1)
            {
                node.coloured = b;
                points += b ? 1 : -1;
                return;
            }

            node.subDivide();
            nodes += 4;
        }

        final int halfSize = size / 2;
        final int midX = minX + halfSize;
        final int midY = minY + halfSize;

        if (x < midX && y < midY)
        {
            set(node.nw, x, y, b, halfSize, minX, minY);
        }
        else if (x >= midX && y < midY)
        {
            set(node.ne, x, y, b, halfSize, midX, minY);
        }
        else if (x < midX && y >= midY)
        {
            set(node.sw, x, y, b, halfSize, minX, midY);
        }
        else
        {
            set(node.se, x, y, b, halfSize, midX, midY);
        }
    }

    // optimise the QuadTree by merging sub-quadrants encoding a uniform value
    public void optimise()
    {
        optimise(root);
    }

    private void optimise(QTNode node)
    {
        if (node.isDivided())
        {
            // attempt to optimise sub-quadrants first
            optimise(node.nw);
            optimise(node.ne);
            optimise(node.sw);
            optimise(node.se);

            // if all the sub-quadrants are equivalent, dispose them
            if (node.nw.equals(node.ne) && node.ne.equals(node.sw) && node.sw.equals(node.se))
            {
                node.coloured = node.nw.coloured;

                // destroy the redundant sub-quadrants
                node.nw = node.ne = node.sw = node.se = null;
                nodes -= 4;
            }
        }
    }

    // export the QuadTress points into an list of points
    public TreeSet<Point<Integer>> getPoints()
    {
        TreeSet<Point<Integer>> points = new TreeSet<>();

        getPoints(root, points, size, 0, 0);

        return points;
    }

    private void getPoints(QTNode node, TreeSet<Point<Integer>> points, int size, int minX, int minY)
    {
        // if this quadrant encodes no data, search the sub-quadrants for data
        if (node.isDivided())
        {
            final int halfSize = size / 2;
            final int midX = minX + halfSize;
            final int midY = minY + halfSize;

            getPoints(node.nw, points, halfSize, minX, minY);
            getPoints(node.ne, points, halfSize, midX, minY);
            getPoints(node.sw, points, halfSize, minX, midY);
            getPoints(node.se, points, halfSize, midX, midY);
        }
        // if the leaf is coloured, add all the points in the quadrant to the list
        else if (node.coloured)
        {
            for (int y = minY; y < minY + size && y < height; y++)
            {
                for (int x = minX; x < minX + size && x < width; x++)
                {
                    points.add(new Point<>(x, y, 0));
                }
            }
        }
    }

    // export the QuadTress points into an array of pixels
    public boolean[] getPixels()
    {
        boolean[] pixels = new boolean[width * height];

        getPixels(root, pixels, size, 0, 0);

        return pixels;
    }

    private void getPixels(QTNode node, boolean[] pixels, int size, int minX, int minY)
    {
        // if this quadrant encodes no data, search the sub-quadrants for data
        if (node.isDivided())
        {
            final int halfSize = size / 2;
            final int midX = minX + halfSize;
            final int midY = minY + halfSize;

            getPixels(node.nw, pixels, halfSize, minX, minY);
            getPixels(node.ne, pixels, halfSize, midX, minY);
            getPixels(node.sw, pixels, halfSize, minX, midY);
            getPixels(node.se, pixels, halfSize, midX, midY);
        }
        // if the leaf is coloured, colour the pixel array across the index range spanned by the quadrant
        else if (node.coloured)
        {
            for (int y = minY; y < minY + size && y < height; y++)
            {
                for (int x = minX; x < minX + size && x < width; x++)
                {
                    pixels[x + y * width] = true;
                }
            }
        }
    }

    public TreeSet<Point<Integer>> getEdgePoints()
    {
        TreeSet<Point<Integer>> points = new TreeSet<>();

        getEdgePoints(root, points, size, 0, 0);

        return points;
    }

    private void getEdgePoints(QTNode node, TreeSet<Point<Integer>> points, int size, int minX, int minY)
    {
        if (node.isDivided())
        {
            final int halfSize = size / 2;
            final int midX = minX + halfSize;
            final int midY = minY + halfSize;

            getEdgePoints(node.nw, points, halfSize, minX, minY);
            getEdgePoints(node.ne, points, halfSize, midX, minY);
            getEdgePoints(node.sw, points, halfSize, minX, midY);
            getEdgePoints(node.se, points, halfSize, midX, midY);
        }
        else if (node.coloured)
        {
            final int maxX = minX + size - 1;
            final int maxY = minY + size - 1;

            for (int x = minX; x <= maxX; x++)
            {
                if (minY - 1 <= 0 || !contains(x, minY - 1))
                {
                    points.add(new Point<>(x, minY, 0));
                }

                if (maxY + 1 >= height || !contains(x, maxY + 1))
                {
                    points.add(new Point<>(x, maxY, 0));
                }
            }

            for (int y = minY; y <= maxY; y++)
            {
                if (minX - 1 <= 0 || !contains(minX - 1, y))
                {
                    points.add(new Point<>(minX, y, 0));
                }

                if (maxX + 1 >= width || !contains(maxX + 1, y))
                {
                    points.add(new Point<>(maxX, y, 0));
                }
            }
        }
    }

    public void getEdgePoints3D(TreeSet<Point<Integer>> points, @Nullable QuadTree below, @Nullable QuadTree above, int z)
    {
        getEdgePoints3D(root, points, below, above, z, size, 0, 0);
    }

    private void getEdgePoints3D(QTNode node, TreeSet<Point<Integer>> points, QuadTree a, QuadTree b, int z, int size, int minX, int minY)
    {
        if (node.isDivided())
        {
            final int halfSize = size / 2;
            final int midX = minX + halfSize;
            final int midY = minY + halfSize;

            getEdgePoints3D(node.nw, points, a, b, z, halfSize, minX, minY);
            getEdgePoints3D(node.ne, points, a, b, z, halfSize, midX, minY);
            getEdgePoints3D(node.sw, points, a, b, z, halfSize, minX, midY);
            getEdgePoints3D(node.se, points, a, b, z, halfSize, midX, midY);
        }
        else if (node.coloured)
        {
            final int maxX = minX + size - 1;
            final int maxY = minY + size - 1;

            if(a == null || b == null)
            {
                for (int y = minY; y <= maxY; y++)
                {
                    for (int x = minX; x <= maxX; x++)
                    {
                        points.add(new Point<>(x, y, z));
                    }
                }
            }
            else
            {
                for (int x = minX; x <= maxX; x++)
                {
                    if (minY - 1 <= 0 || !contains(x, minY - 1) || !a.contains(x, maxY) || !b.contains(x, minY))
                    {
                        points.add(new Point<>(x, minY, z));
                    }

                    if (maxY + 1 >= height || !contains(x, maxY + 1) || !a.contains(x, maxY) || !b.contains(x, maxY))
                    {
                        points.add(new Point<>(x, maxY, z));
                    }
                }

                for (int y = minY; y <= maxY; y++)
                {
                    if (minX - 1 <= 0 || !contains(minX - 1, y) || !a.contains(minX, y) || !b.contains(minX, y))
                    {
                        points.add(new Point<>(minX, y, z));
                    }

                    if (maxX + 1 >= width || !contains(maxX + 1, y) || !a.contains(maxX, y) || !b.contains(maxX, y))
                    {
                        points.add(new Point<>(maxX, y, z));
                    }
                }
            }
        }
    }

    // merge points from another Quad Tree into this Quad Tree
    public void merge(QuadTree qt)
    {
        QuadTree merged = merge(qt, this);

        width = merged.width;
        height = merged.height;
        size = merged.size;
        points = merged.points;
        nodes = merged.nodes;
        root = merged.root;
    }

    public static QuadTree merge(QuadTree l, QuadTree s)
    {
        if (s.size > l.size) return merge(s, l);

        if (l == s) return l;

        QuadTree result = new QuadTree(l);

        if (result.size == s.size)
        {
            result.root = mergeNodes(result.root, s.root);
        }
        else
        {
            QTNode prevNode = result.root;
            QTNode currNode = prevNode;
            int size = result.size;

            while (size > s.size)
            {
                if (currNode.isLeaf())
                {
                    if (currNode.coloured) break;

                    currNode.subDivide();
                }

                prevNode = currNode;
                currNode = currNode.nw;
                size /= 2;
            }

            prevNode.nw = mergeNodes(currNode, s.root);
        }

        result.width = Math.max(l.width, s.width);
        result.height = Math.max(l.height, s.height);
        result.recountPoints();
        result.recountNodes();

        return result;
    }

    private static QTNode mergeNodes(QTNode a, QTNode b)
    {
        if (a.isLeaf()) return new QTNode(a.coloured ? a : b);
        if (b.isLeaf()) return new QTNode(b.coloured ? b : a);

        QTNode merged = new QTNode();
        merged.nw = mergeNodes(a.nw, b.nw);
        merged.ne = mergeNodes(a.ne, b.ne);
        merged.sw = mergeNodes(a.sw, b.sw);
        merged.se = mergeNodes(a.se, b.se);

        return merged;
    }

    private void recountNodes()
    {
        nodes = 1;
        recountNodes(root);
    }

    private void recountNodes(QTNode node)
    {
        if (node.isDivided())
        {
            nodes += 4;
            recountNodes(node.nw);
            recountNodes(node.ne);
            recountNodes(node.sw);
            recountNodes(node.se);
        }
    }

    private void recountPoints()
    {
        points = 0;
        recountPoints(root, size);
    }

    private void recountPoints(QTNode node, int size)
    {
        if (node.isDivided())
        {
            final int halfSize = size / 2;

            recountPoints(node.nw, halfSize);
            recountPoints(node.ne, halfSize);
            recountPoints(node.sw, halfSize);
            recountPoints(node.se, halfSize);
        }
        else if (node.coloured)
        {
            points += size * size;
        }
    }

    public boolean isEmpty()
    {
        return points == 0;
    }

    public void clear()
    {
        points = 0;
        nodes = 1;
        root = new QTNode();
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getSize()
    {
        return size;
    }

    public int getPointCount()
    {
        return points;
    }

    public int getNodeCount()
    {
        return nodes;
    }

    public QTNode getRoot()
    {
        return root;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuadTree qt = (QuadTree) o;

        return width == qt.width &&
                height == qt.height &&
                points == qt.points &&
                nodes == qt.nodes &&
                root.equals(qt.root);
    }

    @Override
    public int hashCode()
    {
        int result = size;
        result = 31 * result + width;
        result = 31 * result + height;
        result = 31 * result + points;
        result = 31 * result + nodes;
        result = 31 * result + (root != null ? root.hashCode() : 0);
        return result;
    }

    @Override
    public Iterator<Point<Integer>> iterator()
    {
        return new QuadTreeIterator();
    }

    private class QuadTreeIterator implements Iterator<Point<Integer>>
    {

        private Point<Integer> nextPoint = new Point<>(0,0,0);

        public QuadTreeIterator()
        {
            findNextPoint();
        }

        @Override
        public boolean hasNext()
        {
            return nextPoint != null;
        }

        @Override
        public Point<Integer> next()
        {
            // Create a copy of the next point, which will be output at the end of this method
            Point<Integer> outputPoint = new Point<>(nextPoint.x,nextPoint.y,0);

            // Prepare the next point
            findNextPoint();

            return outputPoint;

        }

        private void findNextPoint()
        {
            // Picking up where we left off, but moving to the next pixel
            int x = nextPoint.x+1;
            int y = nextPoint.y;

            while (y < height) {
                while (x < width) {
                    if (contains(x,y)) {
                        nextPoint = new Point<>(x,y,0);
                        return;
                    }
                    x++;
                }
                x = 0;
                y++;
            }

            // If we get to the end it means there are no more points, so set nextPoint to null
            nextPoint = null;

        }
    }
}