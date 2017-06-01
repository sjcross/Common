package wbif.sjx.common.Analysis;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by steph on 29/05/2017.
 */
public class StepSizeCalculatorTest {
    @Test
    public void testCalculator() throws Exception {
        double[] x = new double[]{3,-56,23.3,-16.2,62.4,23.8,55.3,-76.3};
        double[] y = new double[]{12,54.2,43.7,99.6,34.6,12.2,-21,-12};
        double[] z = new double[]{-2.2,45.8,-2.4,24,12.1,44.5,76.6,34.6};

        double[] measuredValues = StepSizeCalculator.calculate(x,y,z);
        double[] expectedValues = new double[]{86.98,93.39,73.36,102.69,55.15,55.90,138.43};

        assertArrayEquals(expectedValues,measuredValues,0.01);

    }
}