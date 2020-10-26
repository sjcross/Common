package wbif.sjx.common.Analysis.SpatialCalculators;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.TreeMap;

import org.junit.jupiter.api.Test;

/**
 * Created by steph on 29/05/2017.
 */
public class EuclideanDistanceCalculatorTest {
    @Test
    public void testCalculate() throws Exception {
        int[] f = new int[]{0,1,2,3,4,5,6,7};
        double[] x = new double[]{3,-56,23.3,-16.2,62.4,23.8,55.3,-76.3};
        double[] y = new double[]{12,54.2,43.7,99.6,34.6,12.2,-21,-12};
        double[] z = new double[]{-2.2,45.8,-2.4,24,12.1,44.5,76.6,34.6};

        TreeMap<Integer,Double> measuredValues = new EuclideanDistanceCalculator().calculate(x,y,z,f);
        TreeMap<Integer,Double> expectedValues = new TreeMap<>();
        expectedValues.put(0,0d);
        expectedValues.put(1,86.98);
        expectedValues.put(2,37.64);
        expectedValues.put(3,93.43);
        expectedValues.put(4,65.14);
        expectedValues.put(5,51.12);
        expectedValues.put(6,100.17);
        expectedValues.put(7,90.66);

        for (int key:expectedValues.keySet()) {
            assertEquals(expectedValues.get(key),measuredValues.get(key),0.01);
        }
    }
}