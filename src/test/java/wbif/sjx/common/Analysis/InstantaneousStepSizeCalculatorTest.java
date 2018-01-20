package wbif.sjx.common.Analysis;

import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.*;

/**
 * Created by steph on 29/05/2017.
 */
public class InstantaneousStepSizeCalculatorTest {
    @Test
    public void testCalculator() throws Exception {
        int[] f = new int[]{0,1,2,3,4,5,6,7};
        double[] x = new double[]{3,-56,23.3,-16.2,62.4,23.8,55.3,-76.3};
        double[] y = new double[]{12,54.2,43.7,99.6,34.6,12.2,-21,-12};
        double[] z = new double[]{-2.2,45.8,-2.4,24,12.1,44.5,76.6,34.6};

        TreeMap<Integer,Double> measuredValues = new InstantaneousStepSizeCalculator().calculate(f,x,y,z);
        TreeMap<Integer,Double> expectedValues = new TreeMap<>();
        expectedValues.put(0,0d);
        expectedValues.put(1,86.98);
        expectedValues.put(2,93.39);
        expectedValues.put(3,73.36);
        expectedValues.put(4,102.69);
        expectedValues.put(5,55.15);
        expectedValues.put(6,55.90);
        expectedValues.put(7,138.43);

        for (int key:expectedValues.keySet()) {
            assertEquals(expectedValues.get(key),measuredValues.get(key),0.01);
        }
    }
}