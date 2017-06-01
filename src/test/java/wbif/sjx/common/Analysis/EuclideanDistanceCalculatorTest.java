package wbif.sjx.common.Analysis;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by steph on 29/05/2017.
 */
public class EuclideanDistanceCalculatorTest {
    @Test
    public void testCalculate() throws Exception {
        double[] x = new double[]{3,-56,23.3,-16.2,62.4,23.8,55.3,-76.3};
        double[] y = new double[]{12,54.2,43.7,99.6,34.6,12.2,-21,-12};
        double[] z = new double[]{-2.2,45.8,-2.4,24,12.1,44.5,76.6,34.6};

        double[] measuredValues = EuclideanDistanceCalculator.calculate(x,y,z);
        double[] expectedValues = new double[]{0,86.98,37.64,93.43,65.14,51.12,100.17,90.66};

        assertArrayEquals(expectedValues,measuredValues,0.01);

    }
}