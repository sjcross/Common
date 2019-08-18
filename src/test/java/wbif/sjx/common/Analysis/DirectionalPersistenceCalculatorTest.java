package wbif.sjx.common.Analysis;

import org.junit.jupiter.api.Test;
import wbif.sjx.common.MathFunc.CumStat;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Created by steph on 04/06/2017.
 */
public class DirectionalPersistenceCalculatorTest {
    @Test
    public void testCalculateContinuous2D() throws Exception {
        double[] x = new double[]{3,-56,23.3,-16.2,62.4,23.8,55.3,-76.3,34,23.8};
        double[] y = new double[]{12,54.2,43.7,99.6,34.6,12.2,-21,-12,45,6.3};
        double[] z = new double[10];
        Arrays.fill(z,1);

        CumStat[] cs = DirectionalPersistenceCalculator.calculateContinuous(x,y,z);
        double[] measuredMean = Arrays.stream(cs).map(CumStat::getMean).mapToDouble(Double::doubleValue).toArray();
        double[] expectedMean = new double[]{1.0, -0.67, 0.60, -0.68, 0.58, -0.42, 0.34, -0.29, -0.36};

        assertArrayEquals(expectedMean,measuredMean,0.01);

    }

    @Test
    public void testCalculateContinuous3D() throws Exception {
        double[] x = new double[]{3,-56,23.3,-16.2,62.4,23.8,55.3,-76.3,34,23.8};
        double[] y = new double[]{12,54.2,43.7,99.6,34.6,12.2,-21,-12,45,6.3};
        double[] z = new double[]{-2.2,45.8,-2.4,24,12.1,44.5,76.6,34.6,-23.8,42.3};

        CumStat[] cs = DirectionalPersistenceCalculator.calculateContinuous(x,y,z);
        double[] measuredMean = Arrays.stream(cs).map(CumStat::getMean).mapToDouble(Double::doubleValue).toArray();
        double[] expectedMean = new double[]{1.0, -0.60, 0.43, -0.55, 0.50, -0.29, 0.45, -0.54, 0.32,};

        assertArrayEquals(expectedMean,measuredMean,0.01);
    }
}