package wbif.sjx.common.Analysis;

import org.junit.Test;

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

        double[] measuredValues = InstantaneousVelocityCalculator.calculate(f,x,y,z);
        double[] expectedValues = new double[]{1.73,7.69,8.39,8.57,5.23,8.46,8.32,12.20};

        assertArrayEquals(expectedValues,measuredValues,0.01);

    }

    @Test
    public void testCalculateIncreasingDecreasing() throws Exception {
        int[] f = new int[]{1,2,3,2,5,3,7,9,10};
        double[] x = new double[]{1,2,1,1.2,3.1,-1,1,-0.4,1.6};
        double[] y = new double[]{1,2,4.2,-3,5,3.2,11.4,-4.2,2.5};
        double[] z = new double[]{1,2,-5.3,-1,1.4,4.11,3.5,-2.1,7.9};

        double[] measuredValues = InstantaneousVelocityCalculator.calculate(f,x,y,z);
        double[] expectedValues = new double[]{1.73,7.69,-8.39,2.86,-2.62,2.11,8.32,12.20};

        assertArrayEquals(expectedValues,measuredValues,0.01);

    }
}