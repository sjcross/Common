package wbif.sjx.common.MathFunc;

import org.junit.Ignore;
import org.junit.Test;
import wbif.sjx.common.ExpectedObjects.Blobs2D;
import wbif.sjx.common.Object.Point;

import java.io.FileNotFoundException;
import java.util.HashMap;

import static org.junit.Assert.*;

public class GoreaudEdgeCorrectionTest {
    private double tolerance = 1E-2;

    @Test
    public void testGetFractionInsideRectangleR100() throws Exception {
        HashMap<Point<Double>,Double> expected = Blobs2D.getExpectedCorrections();

        GoreaudEdgeCorrection goreaudEdgeCorrection = new GoreaudEdgeCorrection(39.5,479.5,20.5,480.6446);

        for (Point<Double> point:expected.keySet()) {
            double actual= goreaudEdgeCorrection.getFractionInsideRectangle(point.getX(),point.getY(),100);

            assertEquals(expected.get(point),actual,tolerance);

        }
    }

    @Test
    public void testGetDistancesCloserToTopTopLeft() {
        double minX = 12;
        double maxX = 34;
        double minY = 16;
        double maxY = 32;
        GoreaudEdgeCorrection correction = new GoreaudEdgeCorrection(minX,maxX,minY,maxY);

        double[] actual = correction.getDistances(20,28);

        double dxLeft = 8;
        double dxTop = 4;
        double dxRight = 14;
        double dxBottom = 12;
        double[] expected = new double[]{dxTop,dxLeft,dxBottom,dxRight};

        assertArrayEquals(expected,actual,tolerance);

    }

    @Test
    public void testGetDistancesCloserToLeftTopLeft() {
        double minX = 12;
        double maxX = 34;
        double minY = 16;
        double maxY = 32;
        GoreaudEdgeCorrection correction = new GoreaudEdgeCorrection(minX,maxX,minY,maxY);

        double[] actual = correction.getDistances(16,22);

        double dxLeft = 4;
        double dxTop = 6;
        double dxRight = 18;
        double dxBottom = 10;
        double[] expected = new double[]{dxLeft,dxTop,dxRight,dxBottom};

        assertArrayEquals(expected,actual,tolerance);

    }

    @Test
    public void testGetDistancesCloserToTopTopRight() {
        double minX = 12;
        double maxX = 34;
        double minY = 16;
        double maxY = 32;
        GoreaudEdgeCorrection correction = new GoreaudEdgeCorrection(minX,maxX,minY,maxY);

        double[] actual = correction.getDistances(26,30);

        double dxLeft = 14;
        double dxTop = 2;
        double dxRight = 8;
        double dxBottom = 14;
        double[] expected = new double[]{dxTop,dxRight,dxBottom,dxLeft};

        assertArrayEquals(expected,actual,tolerance);

    }

    @Test
    public void testGetDistancesCloserToRightTopRight() {
        double minX = 12;
        double maxX = 34;
        double minY = 16;
        double maxY = 32;
        GoreaudEdgeCorrection correction = new GoreaudEdgeCorrection(minX,maxX,minY,maxY);

        double[] actual = correction.getDistances(30,23);

        double dxLeft = 18;
        double dxTop = 7;
        double dxRight = 4;
        double dxBottom = 9;
        double[] expected = new double[]{dxRight,dxTop,dxLeft,dxBottom};

        assertArrayEquals(expected,actual,tolerance);

    }

    @Test
    public void testGetDistancesCloserToBottomBottomLeft() {
        double minX = 12;
        double maxX = 34;
        double minY = 16;
        double maxY = 32;
        GoreaudEdgeCorrection correction = new GoreaudEdgeCorrection(minX,maxX,minY,maxY);

        double[] actual = correction.getDistances(17,18);

        double dxLeft = 5;
        double dxTop = 14;
        double dxRight = 17;
        double dxBottom = 2;
        double[] expected = new double[]{dxBottom,dxLeft,dxTop,dxRight};

        assertArrayEquals(expected,actual,tolerance);

    }

    @Test
    public void testGetDistancesCloserToLeftBottomLeft() {
        double minX = 12;
        double maxX = 34;
        double minY = 16;
        double maxY = 32;
        GoreaudEdgeCorrection correction = new GoreaudEdgeCorrection(minX,maxX,minY,maxY);

        double[] actual = correction.getDistances(14,18);

        double dxLeft = 2;
        double dxTop = 14;
        double dxRight = 20;
        double dxBottom = 2;
        double[] expected = new double[]{dxLeft,dxBottom,dxRight,dxTop};

        assertArrayEquals(expected,actual,tolerance);

    }

    @Test
    public void testGetDistancesCloserToBottomBottomRight() {
        double minX = 12;
        double maxX = 34;
        double minY = 16;
        double maxY = 32;
        GoreaudEdgeCorrection correction = new GoreaudEdgeCorrection(minX,maxX,minY,maxY);

        double[] actual = correction.getDistances(26,18);

        double dxLeft = 14;
        double dxTop = 14;
        double dxRight = 8;
        double dxBottom = 2;
        double[] expected = new double[]{dxBottom,dxRight,dxTop,dxLeft};

        assertArrayEquals(expected,actual,tolerance);

    }

    @Test
    public void testGetDistancesCloserToRightBottomRight() {
        double minX = 12;
        double maxX = 34;
        double minY = 16;
        double maxY = 32;
        GoreaudEdgeCorrection correction = new GoreaudEdgeCorrection(minX,maxX,minY,maxY);

        double[] actual = correction.getDistances(34,18);

        double dxLeft = 22;
        double dxTop = 14;
        double dxRight = 0;
        double dxBottom = 2;
        double[] expected = new double[]{dxRight,dxBottom,dxLeft,dxTop};

        assertArrayEquals(expected,actual,tolerance);

    }
}