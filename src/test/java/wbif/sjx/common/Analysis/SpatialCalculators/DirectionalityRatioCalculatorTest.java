package wbif.sjx.common.Analysis.SpatialCalculators;

import org.junit.jupiter.api.Test;
import wbif.sjx.common.Analysis.SpatialCalculators.DirectionalityRatioCalculator;
import wbif.sjx.common.Object.Track;

import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by steph on 29/05/2017.
 */
public class DirectionalityRatioCalculatorTest {
    @Test
    public void testCalculate() throws Exception {
        int[] f = new int[]{0,1,2,3,4,5,6,7,8};
        double[] x = new double[]{3,-56,23.3,-16.2,62.4,23.8,55.3,-76.3};
        double[] y = new double[]{12,54.2,43.7,99.6,34.6,12.2,-21,-12};
        double[] z = new double[]{-2.2,45.8,-2.4,24,12.1,44.5,76.6,34.6};

        TreeMap<Integer,Double> measuredValues = new DirectionalityRatioCalculator().calculate(x,y,z,f);
        TreeMap<Integer,Double> expectedValues = new TreeMap<>();
        expectedValues.put(0,Double.NaN);
        expectedValues.put(1,1d);
        expectedValues.put(2,0.21);
        expectedValues.put(3,0.37);
        expectedValues.put(4,0.18);
        expectedValues.put(5,0.12);
        expectedValues.put(6,0.21);
        expectedValues.put(7,0.15);

        for (int key:expectedValues.keySet()) {
            assertEquals(expectedValues.get(key),measuredValues.get(key),0.01);
        }
    }

    @Test
    public void testCalculateWithTrack() throws Exception {
        int[] f = new int[]{0,1,2,3,4,5,6,7,8};
        double[] x = new double[]{3,-56,23.3,-16.2,62.4,23.8,55.3,-76.3};
        double[] y = new double[]{12,54.2,43.7,99.6,34.6,12.2,-21,-12};
        double[] z = new double[]{-2.2,45.8,-2.4,24,12.1,44.5,76.6,34.6};

        Track track = new Track(x,y,z,f,"px");

        TreeMap<Integer,Double> measuredValues = new DirectionalityRatioCalculator().calculate(track);
        TreeMap<Integer,Double> expectedValues = new TreeMap<>();
        expectedValues.put(0,Double.NaN);
        expectedValues.put(1,1d);
        expectedValues.put(2,0.21);
        expectedValues.put(3,0.37);
        expectedValues.put(4,0.18);
        expectedValues.put(5,0.12);
        expectedValues.put(6,0.21);
        expectedValues.put(7,0.15);

        for (int key:expectedValues.keySet()) {
            assertEquals(expectedValues.get(key),measuredValues.get(key),0.01);
        }
    }
}