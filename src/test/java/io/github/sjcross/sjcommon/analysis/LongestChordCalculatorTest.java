package io.github.sjcross.sjcommon.analysis;

import org.junit.jupiter.api.Test;

import io.github.sjcross.sjcommon.expectedobjects.HorizontalCylinderR22;
import io.github.sjcross.sjcommon.expectedobjects.VerticalCylinderR5;
import io.github.sjcross.sjcommon.exceptions.IntegerOverflowException;
import io.github.sjcross.sjcommon.mathfunc.CumStat;
import io.github.sjcross.sjcommon.object.volume.Volume;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LongestChordCalculatorTest {
    private double tolerance = 1E-2;

    @Test
    public void calculateAverageDistanceFromLCHorizontalCylinderR22() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new HorizontalCylinderR22().getObject(dppXY,dppZ,units);

        LongestChordCalculator calculator = new LongestChordCalculator(volume);

        CumStat actual = calculator.calculateAverageDistanceFromLC();
        double expected = 22d;

        // This measurement is quite imprecise, so tolerance of 20% the expected value is used
        assertEquals(expected,actual.getMean(),expected*0.2);

        // The maximum should be a bit closer, so a 10% tolerance is allowed
        assertEquals(expected,actual.getMax(),expected*0.1);

    }

    @Test
    public void calculateAverageDistanceFromLCVerticalCylinderR5Calibrated() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new VerticalCylinderR5().getObject(dppXY,dppZ,units);

        LongestChordCalculator calculator = new LongestChordCalculator(volume);

        CumStat actual = calculator.calculateAverageDistanceFromLC();
        double expected = 5d;

        // This measurement is quite imprecise, so tolerance of 20% the expected value is used
        assertEquals(expected,actual.getMean(),expected*0.2);

        // The maximum should be a bit closer, so a 10% tolerance is allowed
        assertEquals(expected,actual.getMax(),expected*0.1);

    }

    @Test
    public void calculateAverageDistanceFromLCVerticalCylinderR5Uncalibrated() throws IntegerOverflowException {
        double dppXY = 1;
        double dppZ = 1;
        String units = "um";

        Volume volume = new VerticalCylinderR5().getObject(dppXY,dppZ,units);

        LongestChordCalculator calculator = new LongestChordCalculator(volume);

        CumStat actual = calculator.calculateAverageDistanceFromLC();
        double expected = 5d;

        // This measurement is quite imprecise, so tolerance of 20% the expected value is used
        assertEquals(expected,actual.getMean(),expected*0.2);

        // The maximum should be a bit closer, so a 10% tolerance is allowed
        assertEquals(expected,actual.getMax(),expected*0.1);

    }

    @Test
    public void testCalculateLCHorizontalCylinderR22() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new HorizontalCylinderR22().getObject(dppXY,dppZ,units);

        LongestChordCalculator calculator = new LongestChordCalculator(volume);

        double[][] actual = calculator.calculateLC();
        double[] actual1 = new double[]{actual[0][0],actual[0][1],actual[0][2]};
        double[] actual2 = new double[]{actual[1][0],actual[1][1],actual[1][2]};

        double[] expected1 = new double[]{29,4,5};
        double[] expected2 = new double[]{29,71,5};

        boolean same = Arrays.equals(actual1,expected1) && Arrays.equals(actual2,expected2);
        boolean other = Arrays.equals(actual1,expected2) && Arrays.equals(actual2,expected1);

        assertTrue(same || other);

    }

    @Test
    public void testCalculateLCVerticalCylinderR5Calibrated() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new VerticalCylinderR5().getObject(dppXY,dppZ,units);

        LongestChordCalculator calculator = new LongestChordCalculator(volume);

        double[][] actual = calculator.calculateLC();
        double[] actual1 = new double[]{actual[0][0],actual[0][1],actual[0][2]};
        double[] actual2 = new double[]{actual[1][0],actual[1][1],actual[1][2]};

        double[] expected1 = new double[]{15,35,0};
        double[] expected2 = new double[]{15,35,14};

        boolean same = Arrays.equals(actual1,expected1) && Arrays.equals(actual2,expected2);
        boolean other = Arrays.equals(actual1,expected2) && Arrays.equals(actual2,expected1);

        assertTrue(same || other);

    }

    @Test
    public void testCalculateLCVerticalCylinderR5Uncalibrated() throws IntegerOverflowException {
        double dppXY = 1;
        double dppZ = 1;
        String units = "um";

        Volume volume = new VerticalCylinderR5().getObject(dppXY,dppZ,units);

        LongestChordCalculator calculator = new LongestChordCalculator(volume);

        double[][] actual = calculator.calculateLC();
        double[] actual1 = new double[]{actual[0][0],actual[0][1],actual[0][2]};
        double[] actual2 = new double[]{actual[1][0],actual[1][1],actual[1][2]};

        double[] expected1 = new double[]{15,35,0};
        double[] expected2 = new double[]{15,35,14};

        boolean same = Arrays.equals(actual1,expected1) && Arrays.equals(actual2,expected2);
        boolean other = Arrays.equals(actual1,expected2) && Arrays.equals(actual2,expected1);

        assertTrue(same || other);

    }

    @Test
    public void testGetLCLengthHorizontalCylinderR22() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new HorizontalCylinderR22().getObject(dppXY,dppZ,units);
        LongestChordCalculator calculator = new LongestChordCalculator(volume);

        double actual = calculator.getLCLength();
        double expected = 67d;

        assertEquals(expected,actual,tolerance);

    }

    @Test
    public void testGetLCLengthVerticalCylinderR5Calibrated() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new VerticalCylinderR5().getObject(dppXY,dppZ,units);

        LongestChordCalculator calculator = new LongestChordCalculator(volume);

        double actual = calculator.getLCLength();
        double expected = 70d;

        assertEquals(expected,actual,tolerance);

    }

    @Test
    public void testGetLCLengthVerticalCylinderR5Uncalibrated() throws IntegerOverflowException {
        double dppXY = 1;
        double dppZ = 1;
        String units = "um";

        Volume volume = new VerticalCylinderR5().getObject(dppXY,dppZ,units);

        LongestChordCalculator calculator = new LongestChordCalculator(volume);

        double actual = calculator.getLCLength();
        double expected = 14d;

        assertEquals(expected,actual,tolerance);

    }
}