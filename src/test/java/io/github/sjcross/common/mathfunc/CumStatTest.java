package io.github.sjcross.common.mathfunc;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by steph on 29/05/2017.
 */
public class CumStatTest {
    /**
     * Tests addMeasure(double xIn)
     * @throws Exception
     */
    @Test
    public void testAddMeasure() throws Exception {
        // Initialising the CumStat object
        CumStat cs = new CumStat();

        // Creating four new 5-element arrays to add to the CumStat object covering positives, negatives, fractions and
        // NaNs.  These are added to object
        cs.addMeasure(98.32);
        cs.addMeasure(Double.NaN);
        cs.addMeasure(-78.1);
        cs.addMeasure(78.1);

        // Correct answers
        double expectedMean = 32.77;
        double expectedMin = -78.1;
        double expectedMax = 98.32;
        double expectedSum = 98.32;
        double expectedN = 3;
        double expectedWeightSum = 3;
        double expectedStdSamp = 96.55;
        double expectedStdPop = 78.83;
        double expectedVarSamp = 9321.88;
        double expectedVarPop = 6214.59;

        // Checking results (where relevant to 2 decimal places)
        assertEquals(expectedMean,cs.getMean(),0.01);
        assertEquals(expectedMin,cs.getMin(),0);
        assertEquals(expectedMax,cs.getMax(),0);
        assertEquals(expectedSum,cs.getSum(),0.01);
        assertEquals(expectedN,cs.getN(),0);
        assertEquals(expectedWeightSum,cs.getWeight(),0);
        assertEquals(expectedStdSamp,cs.getStd(),0.01);
        assertEquals(expectedStdSamp,cs.getStd(CumStat.SAMPLE),0.01);
        assertEquals(expectedStdPop,cs.getStd(CumStat.POPULATION),0.01);
        assertEquals(expectedVarSamp,cs.getVar(),0.01);
        assertEquals(expectedVarSamp,cs.getVar(CumStat.SAMPLE),0.01);
        assertEquals(expectedVarPop,cs.getVar(CumStat.POPULATION),0.01);

    }

    /**
     * Tests addMeasure(double xIn, boolean ignoreZeroes), where the only current option is to ignore zeroes (i.e set them to NaN)
     * @throws Exception
     */
    @Test
    public void testAddMeasure1() throws Exception {
        // Initialising the CumStat object
        CumStat cs = new CumStat();

        // Creating four new 5-element arrays to add to the CumStat object covering positives, negatives, fractions and
        // NaNs.  These are added to object
        cs.addMeasure(0,true);
        cs.addMeasure(34.2,true);
        cs.addMeasure(65,true);
        cs.addMeasure(-3,true);

        // Correct answers
        double expectedMean = 32.07;
        double expectedMin = -3;
        double expectedMax = 65;
        double expectedSum = 96.20;
        double expectedN = 3;
        double expectedWeightSum = 3;
        double expectedStdSamp = 34.05;
        double expectedStdPop = 27.80;
        double expectedVarSamp = 1159.41;
        double expectedVarPop = 772.94;

        // Checking results (where relevant to 2 decimal places)
        assertEquals(expectedMean,cs.getMean(),0.01);
        assertEquals(expectedMin,cs.getMin(),0);
        assertEquals(expectedMax,cs.getMax(),0);
        assertEquals(expectedSum,cs.getSum(),0.01);
        assertEquals(expectedN,cs.getN(),0);
        assertEquals(expectedWeightSum,cs.getWeight(),0);
        assertEquals(expectedStdSamp,cs.getStd(),0.01);
        assertEquals(expectedStdSamp,cs.getStd(CumStat.SAMPLE),0.01);
        assertEquals(expectedStdPop,cs.getStd(CumStat.POPULATION),0.01);
        assertEquals(expectedVarSamp,cs.getVar(),0.01);
        assertEquals(expectedVarSamp,cs.getVar(CumStat.SAMPLE),0.01);
        assertEquals(expectedVarPop,cs.getVar(CumStat.POPULATION),0.01);

    }


    /**
     * Tests addMeasure(double xIn, double w), which is a vector with weightSum.  The weightSum doesn't increase the
     * effective number of measurements (i.e. w=2 doesn't equal 2 objects)
     * @throws Exception
     */
    @Test
    public void testAddMeasure3() throws Exception {
        // Initialising the CumStat object
        CumStat cs = new CumStat();

        // Creating four new 5-element arrays to add to the CumStat object covering positives, negatives, fractions and
        // NaNs.  These are added to object
        cs.addMeasure(98.32,3);
        cs.addMeasure(Double.NaN,2);
        cs.addMeasure(-78.1,2);
        cs.addMeasure(78.1,2);

        // Correct answers
        double expectedMean = 42.14;
        double expectedMin = -78.10;
        double expectedMax = 98.32;
        double expectedSum = 98.32;
        double expectedN = 3;
        double expectedWeightSum = 7;
        double expectedStdSamp = 82.63;
        double expectedStdPop = 76.50;
        double expectedVarSamp = 6828.36;
        double expectedVarPop = 5852.88;

        // Checking results (where relevant to 2 decimal places)
        assertEquals(expectedMean,cs.getMean(),0.01);
        assertEquals(expectedMin,cs.getMin(),0);
        assertEquals(expectedMax,cs.getMax(),0);
        assertEquals(expectedSum,cs.getSum(),0.01);
        assertEquals(expectedN,cs.getN(),0);
        assertEquals(expectedWeightSum,cs.getWeight(),0);
        assertEquals(expectedStdSamp,cs.getStd(),0.01);
        assertEquals(expectedStdSamp,cs.getStd(CumStat.SAMPLE),0.01);
        assertEquals(expectedStdPop,cs.getStd(CumStat.POPULATION),0.01);
        assertEquals(expectedVarSamp,cs.getVar(),0.01);
        assertEquals(expectedVarSamp,cs.getVar(CumStat.SAMPLE),0.01);
        assertEquals(expectedVarPop,cs.getVar(CumStat.POPULATION),0.01);

    }

    /**
     * Tests addMeasure(double xIn, double w, boolean ignoreZeroes), which is a vector with weightSum.  The weightSum doesn't
     * increase the effective number of measurements (i.e. w=2 doesn't equal 2 objects).  This also sets all zero-valued
     * elements to Double.NaN (not included in calculation).
     * @throws Exception
     */
    @Test
    public void testAddMeasure4() throws Exception {
        // Initialising the CumStat object
        CumStat cs = new CumStat();

        // Creating four new 5-element arrays to add to the CumStat object covering positives, negatives, fractions and
        // NaNs.  These are added to object
        cs.addMeasure(0,2,true);
        cs.addMeasure(34.2,3,true);
        cs.addMeasure(65,4,true);
        cs.addMeasure(-3,1,true);

        // Correct answers
        double expectedMean = 44.95;
        double expectedMin = -3.00;
        double expectedMax = 65.00;
        double expectedSum = 96.20;
        double expectedN = 3;
        double expectedWeightSum = 8;
        double expectedStdSamp = 24.65;
        double expectedStdPop = 23.06;
        double expectedVarSamp = 607.70;
        double expectedVarPop = 531.74;

        // Checking results (where relevant to 2 decimal places)
        assertEquals(expectedMean,cs.getMean(),0.01);
        assertEquals(expectedMin,cs.getMin(),0);
        assertEquals(expectedMax,cs.getMax(),0);
        assertEquals(expectedSum,cs.getSum(),0.01);
        assertEquals(expectedN,cs.getN(),0);
        assertEquals(expectedWeightSum,cs.getWeight(),0);
        assertEquals(expectedStdSamp,cs.getStd(),0.01);
        assertEquals(expectedStdSamp,cs.getStd(CumStat.SAMPLE),0.01);
        assertEquals(expectedStdPop,cs.getStd(CumStat.POPULATION),0.01);
        assertEquals(expectedVarSamp,cs.getVar(),0.01);
        assertEquals(expectedVarSamp,cs.getVar(CumStat.SAMPLE),0.01);
        assertEquals(expectedVarPop,cs.getVar(CumStat.POPULATION),0.01);
    }

    /**
     * Tests addMeasure(double xIn, double w), which is a single element with weightSum.  The weightSum doesn't
     * increase the effective number of measurements (i.e. w=2 doesn't equal 2 objects).  This also sets all zero-valued
     * elements to Double.NaN (not included in calculation).
     * @throws Exception
     */
    @Test
    public void testAddMeasure5() throws Exception {
        // Initialising the CumStat object
        CumStat cs = new CumStat();

        // Creating four new 5-element arrays to add to the CumStat object covering positives, negatives, fractions and
        // NaNs.  These are added to object
        cs.addMeasure(12,3);
        cs.addMeasure(21,2);
        cs.addMeasure(45.1,4);
        cs.addMeasure(45.1,2);

        // Correct answers
        double expectedMean = 31.69;
        double expectedMin = 12.00;
        double expectedMax = 45.10;
        double expectedSum = 123.20;
        double expectedN = 4;
        double expectedWeightSum = 11;
        double expectedStdSamp = 15.72;
        double expectedStdPop = 14.99;
        double expectedVarSamp = 247.06;
        double expectedVarPop = 224.60;

        // Checking results (where relevant to 2 decimal places)
        assertEquals(expectedMean,cs.getMean(),0.01);
        assertEquals(expectedMin,cs.getMin(),0);
        assertEquals(expectedMax,cs.getMax(),0);
        assertEquals(expectedSum,cs.getSum(),0.01);
        assertEquals(expectedN,cs.getN(),0);
        assertEquals(expectedWeightSum,cs.getWeight(),0);
        assertEquals(expectedStdSamp,cs.getStd(),0.01);
        assertEquals(expectedStdSamp,cs.getStd(CumStat.SAMPLE),0.01);
        assertEquals(expectedStdPop,cs.getStd(CumStat.POPULATION),0.01);
        assertEquals(expectedVarSamp,cs.getVar(),0.01);
        assertEquals(expectedVarSamp,cs.getVar(CumStat.SAMPLE),0.01);
        assertEquals(expectedVarPop,cs.getVar(CumStat.POPULATION),0.01);

    }

    /**
     * Tests addMeasures(double[] xIn).  This method adds multiple values to a single-element CumStat
     * @throws Exception
     */
    @Test
    public void testAddMeasures() throws Exception {
        // Initialising the CumStat object
        CumStat cs = new CumStat();

        // Creating four new 5-element arrays to add to the CumStat object covering positives, negatives, fractions and
        // NaNs.  These are added to object
        cs.addMeasures(new double[]{12,21,45.1,45.1});

        // Correct answers
        double expectedMean = 30.8;
        double expectedMin = 12;
        double expectedMax = 45.1;
        double expectedSum = 123.20;
        double expectedN = 4;
        double expectedWeightSum = 4;
        double expectedStdSamp = 16.92;
        double expectedStdPop = 14.65;
        double expectedVarSamp = 286.15;
        double expectedVarPop = 214.62;

        // Checking results (where relevant to 2 decimal places)
        assertEquals(expectedMean,cs.getMean(),0.01);
        assertEquals(expectedMin,cs.getMin(),0);
        assertEquals(expectedMax,cs.getMax(),0);
        assertEquals(expectedSum,cs.getSum(),0.01);
        assertEquals(expectedN,cs.getN(),0);
        assertEquals(expectedWeightSum,cs.getWeight(),0);
        assertEquals(expectedStdSamp,cs.getStd(),0.01);
        assertEquals(expectedStdSamp,cs.getStd(CumStat.SAMPLE),0.01);
        assertEquals(expectedStdPop,cs.getStd(CumStat.POPULATION),0.01);
        assertEquals(expectedVarSamp,cs.getVar(),0.01);
        assertEquals(expectedVarSamp,cs.getVar(CumStat.SAMPLE),0.01);
        assertEquals(expectedVarPop,cs.getVar(CumStat.POPULATION),0.01);

    }

    /**
     * Tests addMeasures(double[] xIn, double[] w), which adds multiple values to a single-element CumStat, but with
     * weightSum.
     * @throws Exception
     */
    @Test
    public void testAddMeasures1() throws Exception {
        // Initialising the CumStat object
        CumStat cs = new CumStat();

        // Creating four new 5-element arrays to add to the CumStat object covering positives, negatives, fractions and
        // NaNs.  These are added to object
        cs.addMeasures(new double[]{12,21,45.1,45.1}, new double[]{3,2,4,2});

        // Correct answers
        double expectedMean = 31.69;
        double expectedMin = 12.00;
        double expectedMax = 45.10;
        double expectedSum = 123.20;
        double expectedN = 4;
        double expectedWeightSum = 11;
        double expectedStdSamp = 15.72;
        double expectedStdPop = 14.99;
        double expectedVarSamp = 247.06;
        double expectedVarPop = 224.60;

        // Checking results (where relevant to 2 decimal places)
        assertEquals(expectedMean,cs.getMean(),0.01);
        assertEquals(expectedMin,cs.getMin(),0);
        assertEquals(expectedMax,cs.getMax(),0);
        assertEquals(expectedSum,cs.getSum(),0.01);
        assertEquals(expectedN,cs.getN(),0);
        assertEquals(expectedWeightSum,cs.getWeight(),0);
        assertEquals(expectedStdSamp,cs.getStd(),0.01);
        assertEquals(expectedStdSamp,cs.getStd(CumStat.SAMPLE),0.01);
        assertEquals(expectedStdPop,cs.getStd(CumStat.POPULATION),0.01);
        assertEquals(expectedVarSamp,cs.getVar(),0.01);
        assertEquals(expectedVarSamp,cs.getVar(CumStat.SAMPLE),0.01);
        assertEquals(expectedVarPop,cs.getVar(CumStat.POPULATION),0.01);

    }

}