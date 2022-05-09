package io.github.sjcross.sjcommon.analysis.spatialcalculators;

import org.junit.jupiter.api.Test;

import io.github.sjcross.sjcommon.analysis.spatialcalculators.AngularPersistenceCalculator;

import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AngularPersistenceCalculatorTest {
    @Test
    public void testCalculateXY() throws Exception {
        int[] f = new int[]{0,1,2,3,4,5,6,7,8};
        double[] x = new double[]{3   ,-56 ,23.3,-16.2,62.4,23.8,55.3,-76.3,-80.2};
        double[] y = new double[]{12  ,54.2,43.7,99.6 ,34.6,12.2,-21 ,-12  ,-20};
        double[] z = new double[]{0,0,0,0,0,0,0,0,0,0};

        TreeMap<Integer,Double> measuredValues = new AngularPersistenceCalculator().calculate(x,y,z,f);
        TreeMap<Integer,Double> expectedValues = new TreeMap<>();

        expectedValues.put(0,Double.NaN);
        expectedValues.put(1,0.1557);
        expectedValues.put(2,0.2623);
        expectedValues.put(3,0.0842);
        expectedValues.put(4,0.3873);
        expectedValues.put(5,0.4257);
        expectedValues.put(6,0.2366);
        expectedValues.put(7,0.6227);
        expectedValues.put(8,Double.NaN);

        for (int key:expectedValues.keySet()) {
            assertEquals(expectedValues.get(key),measuredValues.get(key),0.0001);
        }
    }
}
