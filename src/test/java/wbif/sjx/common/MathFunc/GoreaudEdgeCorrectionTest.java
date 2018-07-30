package wbif.sjx.common.MathFunc;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class GoreaudEdgeCorrectionTest {
    private double tolerance = 1E-2;

    @Test @Ignore
    public void testGetFractionInsideRectangle() {
    }

    @Test
    public void testGetDistancesCloserToTopLeftWideRect() {
        double minX = 12;
        double maxX = 34;
        double minY = 16;
        double maxY = 32;
        GoreaudEdgeCorrection correction = new GoreaudEdgeCorrection(minX,maxX,minY,maxY);

        double[] actual = correction.getDistances(20,25);
        double[] expected = new double[]{7,8,9,14};

        assertArrayEquals(expected,actual,tolerance);

    }

    @Test @Ignore
    public void testGetDistancesCloserToTopRightWideRect() {
    }

    @Test @Ignore
    public void testGetDistancesCloserToBottomLeftWideRect() {
    }

    @Test @Ignore
    public void testGetDistancesCloserToBottomRightWideRect() {
    }

    @Test @Ignore
    public void testGetDistancesCloserToTopLeftTallRect() {
    }

    @Test @Ignore
    public void testGetDistancesCloserToTopRightTallRect() {
    }

    @Test @Ignore
    public void testGetDistancesCloserToBottomLeftTallRect() {
    }

    @Test @Ignore
    public void testGetDistancesCloserToBottomRightTallRect() {
    }

    @Test @Ignore
    public void testCheckVariableOrderNoSwap() {
    }

    @Test @Ignore
    public void testCheckVariableOrderEqual() {
    }

    @Test @Ignore
    public void testCheckVariableOrderSwap() {
    }
}