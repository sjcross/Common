package wbif.sjx.common.Analysis;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by steph on 29/05/2017.
 */
public class DirectionalityRatioCalculatorTest {
    @Test
    public void testCalculate() throws Exception {
        double[] x = new double[]{3,-56,23.3,-16.2,62.4,23.8,55.3,-76.3};
        double[] y = new double[]{12,54.2,43.7,99.6,34.6,12.2,-21,-12};
        double[] z = new double[]{-2.2,45.8,-2.4,24,12.1,44.5,76.6,34.6};

        double[] measuredValues = DirectionalityRatioCalculator.calculate(x,y,z);
        double[] expectedValues = new double[]{Double.NaN,1,0.21,0.37,0.18,0.12,0.21,0.15};

        assertArrayEquals(expectedValues,measuredValues,0.01);

    }
}