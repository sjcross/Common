package wbif.sjx.common.Analysis;

import java.util.TreeMap;

/**
 * Created by steph on 15/04/2017.
 */
public class DirectionalityRatioCalculator implements SpatialCalculator {
    public TreeMap<Integer,Double> calculate(int[] f, double[] x, double[] y, double[] z) {
        double[] euclideanDistance = EuclideanDistanceCalculator.calculate(x,y,z);
        double[] totalLength = CumulativePathLengthCalculator.calculate(f,x,y,z);

        TreeMap<Integer,Double> directionalPersistance = new TreeMap<>();
        directionalPersistance.put(f[0],0d);
        for (int i=0;i<x.length;i++) {
            int ff = f[i];
            if (totalLength[i] == 0) {
                directionalPersistance.put(ff,Double.NaN);
                continue;
            }

            if (euclideanDistance[i] == 0) {
                directionalPersistance.put(ff,0d);
                continue;
            }

            directionalPersistance.put(ff,euclideanDistance[i]/totalLength[i]);

        }

        return directionalPersistance;

    }
}
