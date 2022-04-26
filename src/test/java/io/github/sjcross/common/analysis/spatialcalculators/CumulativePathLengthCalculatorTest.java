package io.github.sjcross.common.analysis.spatialcalculators;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.TreeMap;

import org.junit.jupiter.api.Test;

import io.github.sjcross.common.object.tracks.Track;

/**
 * Created by steph on 29/05/2017.
 */
public class CumulativePathLengthCalculatorTest {
    @Test
    public void testCalculate() throws Exception {
        int[] f = new int[]{0,1,2,3,4,5,6,7};
        double[] x = new double[]{3,-56,23.3,-16.2,62.4,23.8,55.3,-76.3};
        double[] y = new double[]{12,54.2,43.7,99.6,34.6,12.2,-21,-12};
        double[] z = new double[]{-2.2,45.8,-2.4,24,12.1,44.5,76.6,34.6};

        TreeMap<Integer,Double> measuredValues = new CumulativePathLengthCalculator().calculate(x,y,z,f);
        TreeMap<Integer,Double> expectedValues = new TreeMap<>();
        expectedValues.put(0,0d);
        expectedValues.put(1,86.98);
        expectedValues.put(2,180.37);
        expectedValues.put(3,253.74);
        expectedValues.put(4,356.42);
        expectedValues.put(5,411.57);
        expectedValues.put(6,467.47);
        expectedValues.put(7,605.91);

        for (int key:expectedValues.keySet()) {
            assertEquals(expectedValues.get(key),measuredValues.get(key),0.01);
        }
    }

    @Test
    public void testCalculateWithTrack() throws Exception {
        int[] f = new int[]{0,1,2,3,4,5,6,7};
        double[] x = new double[]{3,-56,23.3,-16.2,62.4,23.8,55.3,-76.3};
        double[] y = new double[]{12,54.2,43.7,99.6,34.6,12.2,-21,-12};
        double[] z = new double[]{-2.2,45.8,-2.4,24,12.1,44.5,76.6,34.6};

        Track track = new Track(x,y,z,f,"px");

        TreeMap<Integer,Double> measuredValues = new CumulativePathLengthCalculator().calculate(track);
        TreeMap<Integer,Double> expectedValues = new TreeMap<>();
        expectedValues.put(0,0d);
        expectedValues.put(1,86.98);
        expectedValues.put(2,180.37);
        expectedValues.put(3,253.74);
        expectedValues.put(4,356.42);
        expectedValues.put(5,411.57);
        expectedValues.put(6,467.47);
        expectedValues.put(7,605.91);

        for (int key:expectedValues.keySet()) {
            assertEquals(expectedValues.get(key),measuredValues.get(key),0.01);
        }
    }
}