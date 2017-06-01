package wbif.sjx.common.MathFunc;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by steph on 29/05/2017.
 */
public class CumStatTest {
    /**
     * Tests addMeasure(double[] xIn)
     * @throws Exception
     */
    @Test
    public void testAddMeasure() throws Exception {
        // Initialising the CumStat object
        CumStat cs = new CumStat(5);

        // Creating four new 5-element arrays to add to the CumStat object covering positives, negatives, fractions and
        // NaNs.  These are added to object
        cs.addMeasure(new double[]{12,45.4,56,67,98.32});
        cs.addMeasure(new double[]{21,43,-65,34.2,Double.NaN});
        cs.addMeasure(new double[]{45.1,-23.23,45,65,-78.1});
        cs.addMeasure(new double[]{45.1,45.4,12,-3,78.1});

        // Correct answers
        double[] expectedMean = new double[]{30.8,27.6425,12,40.8,32.77};
        double[] expectedMin = new double[]{12,-23.23,-65,-3,-78.1};
        double[] expectedMax = new double[]{45.1,45.4,56,67,98.32};
        double[] expectedSum = new double[]{123.20,110.57,48.00,163.20,98.32};
        double[] expectedN = new double[]{4,4,4,4,3};
        double[] expectedWeightSum = new double[]{4,4,4,4,3};
        double[] expectedStdSamp = new double[]{16.92,33.93,54.63,32.83,96.55};
        double[] expectedStdPop = new double[]{14.65,29.39,47.31,28.43,78.83};
        double[] expectedVarSamp = new double[]{286.15,1151.51,2984.67,1078.03,9321.88};
        double[] expectedVarPop = new double[]{214.62,863.63,2238.50,808.52,6214.59};

        // Checking results (where relevant to 2 decimal places)
        assertArrayEquals("Calculation of mean",expectedMean,cs.getMean(),0.01);
        assertArrayEquals("Calculation of minimum",expectedMin,cs.getMin(),0);
        assertArrayEquals("Calculation of maximum",expectedMax,cs.getMax(),0);
        assertArrayEquals("Calculation of sum",expectedSum,cs.getSum(),0.01);
        assertArrayEquals("Calculation of N",expectedN,cs.getN(),0);
        assertArrayEquals("Calculation of weight sum",expectedWeightSum,cs.getWeight(),0);
        assertArrayEquals("Calculation of sample standard deviation (default)",expectedStdSamp,cs.getStd(),0.01);
        assertArrayEquals("Calculation of sample standard deviation",expectedStdSamp,cs.getStd(CumStat.SAMPLE),0.01);
        assertArrayEquals("Calculation of population standard deviation",expectedStdPop,cs.getStd(CumStat.POPULATION),0.01);
        assertArrayEquals("Calculation of sample variance (default)",expectedVarSamp,cs.getVar(),0.01);
        assertArrayEquals("Calculation of sample variance",expectedVarSamp,cs.getVar(CumStat.SAMPLE),0.01);
        assertArrayEquals("Calculation of population variance",expectedVarPop,cs.getVar(CumStat.POPULATION),0.01);

    }

    /**
     * Tests addMeasure(double[] xIn, int opt), where the only current option is to ignore zeroes (i.e set them to NaN)
     * @throws Exception
     */
    @Test
    public void testAddMeasure1() throws Exception {
        // Initialising the CumStat object
        CumStat cs = new CumStat(5);

        // Creating four new 5-element arrays to add to the CumStat object covering positives, negatives, fractions and
        // NaNs.  These are added to object
        cs.addMeasure(new double[]{12,45.4,56,0,98.32},CumStat.IGNOREZEROS);
        cs.addMeasure(new double[]{21,43,-65,34.2,Double.NaN},CumStat.IGNOREZEROS);
        cs.addMeasure(new double[]{45.1,0,45,65,-78.1},CumStat.IGNOREZEROS);
        cs.addMeasure(new double[]{45.1,45.4,0,-3,78.1});

        // Correct answers
        double[] expectedMean = new double[]{30.8,44.60,9,32.07,32.77};
        double[] expectedMin = new double[]{12,43,-65,-3,-78.1};
        double[] expectedMax = new double[]{45.1,45.4,56,65,98.32};
        double[] expectedSum = new double[]{123.20,133.8,36,96.20,98.32};
        double[] expectedN = new double[]{4,3,4,3,3};
        double[] expectedWeightSum = new double[]{4,3,4,3,3};
        double[] expectedStdSamp = new double[]{16.92,1.39,54.96,34.05,96.55};
        double[] expectedStdPop = new double[]{14.65,1.13,47.6,27.80,78.83};
        double[] expectedVarSamp = new double[]{286.15,1.92,3020.67,1159.41,9321.88};
        double[] expectedVarPop = new double[]{214.62,1.28,2265.50,772.94,6214.59};

        // Checking results (where relevant to 2 decimal places)
        assertArrayEquals("Calculation of mean",expectedMean,cs.getMean(),0.01);
        assertArrayEquals("Calculation of minimum",expectedMin,cs.getMin(),0);
        assertArrayEquals("Calculation of maximum",expectedMax,cs.getMax(),0);
        assertArrayEquals("Calculation of sum",expectedSum,cs.getSum(),0.01);
        assertArrayEquals("Calculation of N",expectedN,cs.getN(),0);
        assertArrayEquals("Calculation of weight sum",expectedWeightSum,cs.getWeight(),0);
        assertArrayEquals("Calculation of sample standard deviation (default)",expectedStdSamp,cs.getStd(),0.01);
        assertArrayEquals("Calculation of sample standard deviation",expectedStdSamp,cs.getStd(CumStat.SAMPLE),0.01);
        assertArrayEquals("Calculation of population standard deviation",expectedStdPop,cs.getStd(CumStat.POPULATION),0.01);
        assertArrayEquals("Calculation of sample variance (default)",expectedVarSamp,cs.getVar(),0.01);
        assertArrayEquals("Calculation of sample variance",expectedVarSamp,cs.getVar(CumStat.SAMPLE),0.01);
        assertArrayEquals("Calculation of population variance",expectedVarPop,cs.getVar(CumStat.POPULATION),0.01);

    }

    /**
     * Tests addSingleMeasure(int i, double xIn).  A typical CumStat is initialised and this adds a couple of new values
     * @throws Exception
     */
    @Test
    public void testAddSingleMeasure() throws Exception {
        // Initialising the CumStat object
        CumStat cs = new CumStat(5);

        // Creating four new 5-element arrays to add to the CumStat object covering positives, negatives, fractions and
        // NaNs.  These are added to object
        cs.addMeasure(new double[]{12,45.4,56,67,98.32});
        cs.addMeasure(new double[]{21,43,-65,34.2,Double.NaN});
        cs.addMeasure(new double[]{45.1,-23.23,45,65,-78.1});
        cs.addMeasure(new double[]{45.1,45.4,12,-3,78.1});

        cs.addSingleMeasure(1,15.9);
        cs.addSingleMeasure(3,-5);
        cs.addSingleMeasure(3,6.7);

        // Correct answers
        double[] expectedMean = new double[]{30.80,25.29,12.00,27.48,32.77};
        double[] expectedMin = new double[]{12.00,-23.23,-65.00,-5.00,-78.10};
        double[] expectedMax = new double[]{45.10,45.40,56.00,67.00,98.32};
        double[] expectedSum = new double[]{123.20,126.47,48.00,164.90,98.32};
        double[] expectedN = new double[]{4,5,4,6,3};
        double[] expectedWeightSum = new double[]{4,5,4,6,3};
        double[] expectedStdSamp = new double[]{16.92,29.85,54.63,32.96,96.55};
        double[] expectedStdPop = new double[]{14.65,26.70,47.31,30.08,78.83};
        double[] expectedVarSamp = new double[]{286.15,891.21,2984.67,1086.11,9321.88};
        double[] expectedVarPop = new double[]{214.62,712.97,2238.50,905.09,6214.59};

        // Checking results (where relevant to 2 decimal places)
        assertArrayEquals("Calculation of mean",expectedMean,cs.getMean(),0.01);
        assertArrayEquals("Calculation of minimum",expectedMin,cs.getMin(),0);
        assertArrayEquals("Calculation of maximum",expectedMax,cs.getMax(),0);
        assertArrayEquals("Calculation of sum",expectedSum,cs.getSum(),0.01);
        assertArrayEquals("Calculation of N",expectedN,cs.getN(),0);
        assertArrayEquals("Calculation of weight sum",expectedWeightSum,cs.getWeight(),0);
        assertArrayEquals("Calculation of sample standard deviation (default)",expectedStdSamp,cs.getStd(),0.01);
        assertArrayEquals("Calculation of sample standard deviation",expectedStdSamp,cs.getStd(CumStat.SAMPLE),0.01);
        assertArrayEquals("Calculation of population standard deviation",expectedStdPop,cs.getStd(CumStat.POPULATION),0.01);
        assertArrayEquals("Calculation of sample variance (default)",expectedVarSamp,cs.getVar(),0.01);
        assertArrayEquals("Calculation of sample variance",expectedVarSamp,cs.getVar(CumStat.SAMPLE),0.01);
        assertArrayEquals("Calculation of population variance",expectedVarPop,cs.getVar(CumStat.POPULATION),0.01);

    }

    /**
     * Tests addMeasure(double xIn), which works for a single-element CumStat object.
     * @throws Exception
     */
    @Test
    public void testAddMeasure2() throws Exception {
        // Initialising the CumStat object
        CumStat cs = new CumStat(1);

        // Creating four new single-element arrays to add to the CumStat object covering positives, negatives, fractions
        // and NaNs.  These are added to object.
        cs.addMeasure(12);
        cs.addMeasure(21);
        cs.addMeasure(45.1);
        cs.addMeasure(45.1);

        // Correct answers
        double[] expectedMean = new double[]{30.8};
        double[] expectedMin = new double[]{12};
        double[] expectedMax = new double[]{45.1};
        double[] expectedSum = new double[]{123.20};
        double[] expectedN = new double[]{4};
        double[] expectedWeightSum = new double[]{4};
        double[] expectedStdSamp = new double[]{16.92};
        double[] expectedStdPop = new double[]{14.65};
        double[] expectedVarSamp = new double[]{286.15};
        double[] expectedVarPop = new double[]{214.62};

        // Checking results (where relevant to 2 decimal places)
        assertArrayEquals("Calculation of mean",expectedMean,cs.getMean(),0.01);
        assertArrayEquals("Calculation of minimum",expectedMin,cs.getMin(),0);
        assertArrayEquals("Calculation of maximum",expectedMax,cs.getMax(),0);
        assertArrayEquals("Calculation of sum",expectedSum,cs.getSum(),0.01);
        assertArrayEquals("Calculation of N",expectedN,cs.getN(),0);
        assertArrayEquals("Calculation of weight sum",expectedWeightSum,cs.getWeight(),0);
        assertArrayEquals("Calculation of sample standard deviation (default)",expectedStdSamp,cs.getStd(),0.01);
        assertArrayEquals("Calculation of sample standard deviation",expectedStdSamp,cs.getStd(CumStat.SAMPLE),0.01);
        assertArrayEquals("Calculation of population standard deviation",expectedStdPop,cs.getStd(CumStat.POPULATION),0.01);
        assertArrayEquals("Calculation of sample variance (default)",expectedVarSamp,cs.getVar(),0.01);
        assertArrayEquals("Calculation of sample variance",expectedVarSamp,cs.getVar(CumStat.SAMPLE),0.01);
        assertArrayEquals("Calculation of population variance",expectedVarPop,cs.getVar(CumStat.POPULATION),0.01);

    }

    /**
     * Tests addMeasure(double[] xIn, double[] w), which is a vector with weightSum.  The weightSum doesn't increase the
     * effective number of measurements (i.e. w=2 doesn't equal 2 objects)
     * @throws Exception
     */
    @Test
    public void testAddMeasure3() throws Exception {
        // Initialising the CumStat object
        CumStat cs = new CumStat(5);

        // Creating four new 5-element arrays to add to the CumStat object covering positives, negatives, fractions and
        // NaNs.  These are added to object
        cs.addMeasure(new double[]{12,45.4,56,67,98.32},new double[]{3,2,1,2,3});
        cs.addMeasure(new double[]{21,43,-65,34.2,Double.NaN},new double[]{2,1,4,3,2});
        cs.addMeasure(new double[]{45.1,-23.23,45,65,-78.1},new double[]{4,2,1,4,2});
        cs.addMeasure(new double[]{45.1,45.4,12,-3,78.1},new double[]{2,4,3,1,2});

        // Correct answers
        double[] expectedMean = new double[]{31.69,29.88,-13.67,49.36,42.14};
        double[] expectedMin = new double[]{12.00,-23.23,-65.00,-3.00,-78.10};
        double[] expectedMax = new double[]{45.10,45.40,56.00,67.00,98.32};
        double[] expectedSum = new double[]{123.20,110.57,48.00,163.20,98.32};
        double[] expectedN = new double[]{4,4,4,4,3};
        double[] expectedWeightSum = new double[]{11,9,9,10,7};
        double[] expectedStdSamp = new double[]{15.72,30.12,51.00,23.65,82.63};
        double[] expectedStdPop = new double[]{14.99,28.40,48.09,22.43,76.50};
        double[] expectedVarSamp = new double[]{247.06,907.34,2601.50,559.09,6828.36};
        double[] expectedVarPop = new double[]{224.60,806.52,2312.44,503.18,5852.88};

        // Checking results (where relevant to 2 decimal places)
        assertArrayEquals("Calculation of mean",expectedMean,cs.getMean(),0.01);
        assertArrayEquals("Calculation of minimum",expectedMin,cs.getMin(),0);
        assertArrayEquals("Calculation of maximum",expectedMax,cs.getMax(),0);
        assertArrayEquals("Calculation of sum",expectedSum,cs.getSum(),0.01);
        assertArrayEquals("Calculation of N",expectedN,cs.getN(),0);
        assertArrayEquals("Calculation of weight sum",expectedWeightSum,cs.getWeight(),0);
        assertArrayEquals("Calculation of sample standard deviation (default)",expectedStdSamp,cs.getStd(),0.01);
        assertArrayEquals("Calculation of sample standard deviation",expectedStdSamp,cs.getStd(CumStat.SAMPLE),0.01);
        assertArrayEquals("Calculation of population standard deviation",expectedStdPop,cs.getStd(CumStat.POPULATION),0.01);
        assertArrayEquals("Calculation of sample variance (default)",expectedVarSamp,cs.getVar(),0.01);
        assertArrayEquals("Calculation of sample variance",expectedVarSamp,cs.getVar(CumStat.SAMPLE),0.01);
        assertArrayEquals("Calculation of population variance",expectedVarPop,cs.getVar(CumStat.POPULATION),0.01);

    }

    /**
     * Tests addMeasure(double[] xIn, double[] w, int opt), which is a vector with weightSum.  The weightSum doesn't
     * increase the effective number of measurements (i.e. w=2 doesn't equal 2 objects).  This also sets all zero-valued
     * elements to Double.NaN (not included in calculation).
     * @throws Exception
     */
    @Test
    public void testAddMeasure4() throws Exception {
        // Initialising the CumStat object
        CumStat cs = new CumStat(5);

        // Creating four new 5-element arrays to add to the CumStat object covering positives, negatives, fractions and
        // NaNs.  These are added to object
        cs.addMeasure(new double[]{12,45.4,56,0,98.32},new double[]{3,2,1,2,3},CumStat.IGNOREZEROS);
        cs.addMeasure(new double[]{21,43,-65,34.2,Double.NaN},new double[]{2,1,4,3,2},CumStat.IGNOREZEROS);
        cs.addMeasure(new double[]{45.1,0,45,65,-78.1},new double[]{4,2,1,4,2},CumStat.IGNOREZEROS);
        cs.addMeasure(new double[]{0,45.4,12,-3,78.1},new double[]{2,4,3,1,2},CumStat.IGNOREZEROS);

        // Correct answers
        double[] expectedMean = new double[]{28.71,45.06,-13.67,44.95,42.14};
        double[] expectedMin = new double[]{12.00,43.00,-65.00,-3.00,-78.10};
        double[] expectedMax = new double[]{45.10,45.40,56.00,65.00,98.32};
        double[] expectedSum = new double[]{78.10,133.80,48.00,96.20,98.32};
        double[] expectedN = new double[]{3,3,4,3,3};
        double[] expectedWeightSum = new double[]{9,7,9,8,7};
        double[] expectedStdSamp = new double[]{15.93,.91,51.00,24.65,82.63};
        double[] expectedStdPop = new double[]{15.02,.84,48.09,23.06,76.50};
        double[] expectedVarSamp = new double[]{253.89,.82,2601.50,607.70,6828.36};
        double[] expectedVarPop = new double[]{225.68,.71,2312.44,531.74,5852.88};

        // Checking results (where relevant to 2 decimal places)
        assertArrayEquals("Calculation of mean",expectedMean,cs.getMean(),0.01);
        assertArrayEquals("Calculation of minimum",expectedMin,cs.getMin(),0);
        assertArrayEquals("Calculation of maximum",expectedMax,cs.getMax(),0);
        assertArrayEquals("Calculation of sum",expectedSum,cs.getSum(),0.01);
        assertArrayEquals("Calculation of N",expectedN,cs.getN(),0);
        assertArrayEquals("Calculation of weight sum",expectedWeightSum,cs.getWeight(),0);
        assertArrayEquals("Calculation of sample standard deviation (default)",expectedStdSamp,cs.getStd(),0.01);
        assertArrayEquals("Calculation of sample standard deviation",expectedStdSamp,cs.getStd(CumStat.SAMPLE),0.01);
        assertArrayEquals("Calculation of population standard deviation",expectedStdPop,cs.getStd(CumStat.POPULATION),0.01);
        assertArrayEquals("Calculation of sample variance (default)",expectedVarSamp,cs.getVar(),0.01);
        assertArrayEquals("Calculation of sample variance",expectedVarSamp,cs.getVar(CumStat.SAMPLE),0.01);
        assertArrayEquals("Calculation of population variance",expectedVarPop,cs.getVar(CumStat.POPULATION),0.01);
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
        CumStat cs = new CumStat(1);

        // Creating four new 5-element arrays to add to the CumStat object covering positives, negatives, fractions and
        // NaNs.  These are added to object
        cs.addMeasure(12,3);
        cs.addMeasure(21,2);
        cs.addMeasure(45.1,4);
        cs.addMeasure(45.1,2);

        // Correct answers
        double[] expectedMean = new double[]{31.69};
        double[] expectedMin = new double[]{12.00};
        double[] expectedMax = new double[]{45.10};
        double[] expectedSum = new double[]{123.20};
        double[] expectedN = new double[]{4};
        double[] expectedWeightSum = new double[]{11};
        double[] expectedStdSamp = new double[]{15.72};
        double[] expectedStdPop = new double[]{14.99};
        double[] expectedVarSamp = new double[]{247.06};
        double[] expectedVarPop = new double[]{224.60};

        // Checking results (where relevant to 2 decimal places)
        assertArrayEquals("Calculation of mean",expectedMean,cs.getMean(),0.01);
        assertArrayEquals("Calculation of minimum",expectedMin,cs.getMin(),0);
        assertArrayEquals("Calculation of maximum",expectedMax,cs.getMax(),0);
        assertArrayEquals("Calculation of sum",expectedSum,cs.getSum(),0.01);
        assertArrayEquals("Calculation of N",expectedN,cs.getN(),0);
        assertArrayEquals("Calculation of weight sum",expectedWeightSum,cs.getWeight(),0);
        assertArrayEquals("Calculation of sample standard deviation (default)",expectedStdSamp,cs.getStd(),0.01);
        assertArrayEquals("Calculation of sample standard deviation",expectedStdSamp,cs.getStd(CumStat.SAMPLE),0.01);
        assertArrayEquals("Calculation of population standard deviation",expectedStdPop,cs.getStd(CumStat.POPULATION),0.01);
        assertArrayEquals("Calculation of sample variance (default)",expectedVarSamp,cs.getVar(),0.01);
        assertArrayEquals("Calculation of sample variance",expectedVarSamp,cs.getVar(CumStat.SAMPLE),0.01);
        assertArrayEquals("Calculation of population variance",expectedVarPop,cs.getVar(CumStat.POPULATION),0.01);

    }

    /**
     * Tests addMeasures(double[] xIn).  This method adds multiple values to a single-element CumStat
     * @throws Exception
     */
    @Test
    public void testAddMeasures() throws Exception {
        // Initialising the CumStat object
        CumStat cs = new CumStat(1);

        // Creating four new 5-element arrays to add to the CumStat object covering positives, negatives, fractions and
        // NaNs.  These are added to object
        cs.addMeasures(new double[]{12,21,45.1,45.1});

        // Correct answers
        double[] expectedMean = new double[]{30.8};
        double[] expectedMin = new double[]{12};
        double[] expectedMax = new double[]{45.1};
        double[] expectedSum = new double[]{123.20};
        double[] expectedN = new double[]{4};
        double[] expectedWeightSum = new double[]{4};
        double[] expectedStdSamp = new double[]{16.92};
        double[] expectedStdPop = new double[]{14.65};
        double[] expectedVarSamp = new double[]{286.15};
        double[] expectedVarPop = new double[]{214.62};

        // Checking results (where relevant to 2 decimal places)
        assertArrayEquals("Calculation of mean",expectedMean,cs.getMean(),0.01);
        assertArrayEquals("Calculation of minimum",expectedMin,cs.getMin(),0);
        assertArrayEquals("Calculation of maximum",expectedMax,cs.getMax(),0);
        assertArrayEquals("Calculation of sum",expectedSum,cs.getSum(),0.01);
        assertArrayEquals("Calculation of N",expectedN,cs.getN(),0);
        assertArrayEquals("Calculation of weight sum",expectedWeightSum,cs.getWeight(),0);
        assertArrayEquals("Calculation of sample standard deviation (default)",expectedStdSamp,cs.getStd(),0.01);
        assertArrayEquals("Calculation of sample standard deviation",expectedStdSamp,cs.getStd(CumStat.SAMPLE),0.01);
        assertArrayEquals("Calculation of population standard deviation",expectedStdPop,cs.getStd(CumStat.POPULATION),0.01);
        assertArrayEquals("Calculation of sample variance (default)",expectedVarSamp,cs.getVar(),0.01);
        assertArrayEquals("Calculation of sample variance",expectedVarSamp,cs.getVar(CumStat.SAMPLE),0.01);
        assertArrayEquals("Calculation of population variance",expectedVarPop,cs.getVar(CumStat.POPULATION),0.01);

    }

    /**
     * Tests addMeasures(double[] xIn, double[] w), which adds multiple values to a single-element CumStat, but with
     * weightSum.
     * @throws Exception
     */
    @Test
    public void testAddMeasures1() throws Exception {
        // Initialising the CumStat object
        CumStat cs = new CumStat(1);

        // Creating four new 5-element arrays to add to the CumStat object covering positives, negatives, fractions and
        // NaNs.  These are added to object
        cs.addMeasures(new double[]{12,21,45.1,45.1}, new double[]{3,2,4,2});

        // Correct answers
        double[] expectedMean = new double[]{31.69};
        double[] expectedMin = new double[]{12.00};
        double[] expectedMax = new double[]{45.10};
        double[] expectedSum = new double[]{123.20};
        double[] expectedN = new double[]{4};
        double[] expectedWeightSum = new double[]{11};
        double[] expectedStdSamp = new double[]{15.72};
        double[] expectedStdPop = new double[]{14.99};
        double[] expectedVarSamp = new double[]{247.06};
        double[] expectedVarPop = new double[]{224.60};

        // Checking results (where relevant to 2 decimal places)
        assertArrayEquals("Calculation of mean",expectedMean,cs.getMean(),0.01);
        assertArrayEquals("Calculation of minimum",expectedMin,cs.getMin(),0);
        assertArrayEquals("Calculation of maximum",expectedMax,cs.getMax(),0);
        assertArrayEquals("Calculation of sum",expectedSum,cs.getSum(),0.01);
        assertArrayEquals("Calculation of N",expectedN,cs.getN(),0);
        assertArrayEquals("Calculation of weight sum",expectedWeightSum,cs.getWeight(),0);
        assertArrayEquals("Calculation of sample standard deviation (default)",expectedStdSamp,cs.getStd(),0.01);
        assertArrayEquals("Calculation of sample standard deviation",expectedStdSamp,cs.getStd(CumStat.SAMPLE),0.01);
        assertArrayEquals("Calculation of population standard deviation",expectedStdPop,cs.getStd(CumStat.POPULATION),0.01);
        assertArrayEquals("Calculation of sample variance (default)",expectedVarSamp,cs.getVar(),0.01);
        assertArrayEquals("Calculation of sample variance",expectedVarSamp,cs.getVar(CumStat.SAMPLE),0.01);
        assertArrayEquals("Calculation of population variance",expectedVarPop,cs.getVar(CumStat.POPULATION),0.01);

    }

}