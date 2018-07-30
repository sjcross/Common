package wbif.sjx.common.MathFunc;

import org.junit.Ignore;
import org.junit.Test;
import wbif.sjx.common.Analysis.KFunctionCalculator;
import wbif.sjx.common.ExpectedObjects.Blobs2D;
import wbif.sjx.common.Object.Point;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class KFunctionTest {
    private double tolerance = 1E-2;

    @Test
    public void testCalculateMaximumPointSeparation2D() throws Exception {
        ArrayList<Point<Double>> centroids = Blobs2D.getCentroids();
        assertNotNull(centroids);

        double actual = KFunctionCalculator.calculateMaximumPointSeparation(centroids);
        double expected = 593.26;

        assertEquals(expected,actual,tolerance);

    }

    @Test
    public void testCalculateRegionSize2D() throws Exception {
        ArrayList<Point<Double>> centroids = Blobs2D.getCentroids();
        assertNotNull(centroids);

        double actual = KFunctionCalculator.calculateRegionSize(centroids,true);
        double expected = 202463.61;

        assertEquals(expected,actual,tolerance);

    }

    @Test
    public void testCalculateStepSize2D() throws Exception {
        double regionSize = 202463.61;
        int nBins = 100;

        double actual = KFunctionCalculator.calculateStepSize(202463.61,100,true);
        double expected = 1.4999;

        assertEquals(expected,actual,1E-4);

    }

    @Test
    public void testGetLFunction2D() throws Exception {
        ArrayList<Point<Double>> centroids = Blobs2D.getCentroids();
        assertNotNull(centroids);

        int nBins = 100;

        KFunctionCalculator calculator = new KFunctionCalculator(centroids,nBins,true);
        TreeMap<Double,Double> actual = calculator.getLFunction();
        TreeMap<Double,Double> expected = Blobs2D.getLFunction();

        assertNotNull(expected);

        Iterator<Double> actualIterator = actual.keySet().iterator();

        for (Double expectedTs : expected.keySet()) {
            double actualTs = actualIterator.next();

            assertEquals(expectedTs, actualTs, tolerance);
            assertEquals(expected.get(expectedTs), actual.get(actualTs), tolerance);

        }
    }
}