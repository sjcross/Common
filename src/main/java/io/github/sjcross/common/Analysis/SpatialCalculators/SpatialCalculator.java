package io.github.sjcross.common.Analysis.SpatialCalculators;

import java.util.TreeMap;

import io.github.sjcross.common.Object.Tracks.Track;

/**
 * Created by sc13967 on 20/01/2018.
 */
public interface SpatialCalculator {
    public default TreeMap<Integer,Double> calculate(Track track) {
        return calculate(track.getX(),track.getY(),track.getZ(),track.getF());
    }

    public TreeMap<Integer,Double> calculate(double[] x, double[] y, double[] z, int[] f);
}
