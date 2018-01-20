package wbif.sjx.common.Analysis;

import java.util.TreeMap;

/**
 * Created by steph on 15/04/2017.
 */
public class DirectionalityRatioCalculator implements SpatialCalculator {
    public TreeMap<Integer,Double> calculate(int[] f, double[] x, double[] y, double[] z) {
        TreeMap<Integer,Double> euclideanDistance = new EuclideanDistanceCalculator().calculate(f,x,y,z);
        TreeMap<Integer,Double> totalLength = new CumulativePathLengthCalculator().calculate(f,x,y,z);

        TreeMap<Integer,Double> directionalPersistance = new TreeMap<>();
        directionalPersistance.put(f[0],0d);
        for (int i=0;i<x.length;i++) {
            int ff = f[i];
            if (totalLength.get(ff) == 0) {
                directionalPersistance.put(ff,Double.NaN);
                continue;
            }

            if (euclideanDistance.get(ff) == 0) {
                directionalPersistance.put(ff,0d);
                continue;
            }

            directionalPersistance.put(ff,euclideanDistance.get(ff)/totalLength.get(ff));

        }

        return directionalPersistance;

    }
}
