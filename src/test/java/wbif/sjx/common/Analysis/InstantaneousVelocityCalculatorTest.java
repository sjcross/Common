package wbif.sjx.common.Analysis;

import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.*;

/**
 * Created by steph on 09/07/2017.
 */
public class InstantaneousVelocityCalculatorTest {
    @Test
    public void testCalculateIncreasing() throws Exception {
        int[] f = new int[]{1,2,3,4,5,6,7,9,10};
        double[] x = new double[]{1,2,1,1.2,3.1,-1,1,-0.4,1.6};
        double[] y = new double[]{1,2,4.2,-3,5,3.2,11.4,-4.2,2.5};
        double[] z = new double[]{1,2,-5.3,-1,1.4,4.11,3.5,-2.1,7.9};

        TreeMap<Integer,Double> measuredValues = new InstantaneousVelocityCalculator().calculate(f,x,y,z);
        TreeMap<Integer,Double> expectedValues = new TreeMap<>();
        expectedValues.put(1,0d);
        expectedValues.put(2,1.73);
        expectedValues.put(3,7.69);
        expectedValues.put(4,8.39);
        expectedValues.put(5,8.57);
        expectedValues.put(6,5.23);
        expectedValues.put(7,8.46);
        expectedValues.put(9,8.32);
        expectedValues.put(10,12.20);

        for (int key:expectedValues.keySet()) {
            assertEquals(expectedValues.get(key),measuredValues.get(key),0.01);
        }
    }
}