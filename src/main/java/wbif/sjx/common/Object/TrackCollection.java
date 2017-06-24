package wbif.sjx.common.Object;

import ij.IJ;
import ij.measure.ResultsTable;
import wbif.sjx.common.MathFunc.CumStat;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by sc13967 on 13/06/2017.
 */
public class TrackCollection extends HashMap<Integer,Track> {

    // PUBLIC METHODS

    /**
     * Rolling Euclidean distance.  Values are stored per frame, relative to the start of that track.
     * @return
     */
    public double[][] getAverageRollingEuclideanDistance(boolean pixelDistances) {
        // Determining the longest duration
        int longestDuration = 0;
        for (Track track:values()) {
            if (track.getDuration() > longestDuration) {
                longestDuration = track.getDuration();
            }
        }

        // Creating the CumStat array
        CumStat[] cs = new CumStat[longestDuration+1];
        for (int i=0;i<cs.length;i++) {
            cs[i] = new CumStat();
        }

        for (Track track:values()) {
            double[] rollingEuclideanDistance = track.getRollingEuclideanDistance(pixelDistances);
            for (int i=0;i<rollingEuclideanDistance.length;i++) {
                cs[i].addMeasure(rollingEuclideanDistance[i]);
            }
        }

        // Getting the average and standard deviations
        double[] averageEuclideanDistance = Arrays.stream(cs).mapToDouble(CumStat::getMean).toArray();
        double[] stdevEuclideanDistance = Arrays.stream(cs).mapToDouble(CumStat::getStd).toArray();

        return new double[][]{averageEuclideanDistance,stdevEuclideanDistance};

    }

    /**
     * Rolling total path length.  Values are stored per frame, relative to the start of that track.
     * @return
     */
    public double[][] getAverageTotalPathLength(boolean pixelDistances) {
        // Determining the longest duration
        int longestDuration = 0;
        for (Track track:values()) {
            if (track.getDuration() > longestDuration) {
                longestDuration = track.getDuration();
            }
        }

        // Creating the CumStat array
        CumStat[] cs = new CumStat[longestDuration+1];
        for (int i=0;i<cs.length;i++) {
            cs[i] = new CumStat();
        }

        for (Track track:values()) {
            double[] rollingTotalPathLength = track.getRollingTotalPathLength(pixelDistances);
            for (int i=0;i<rollingTotalPathLength.length;i++) {
                cs[i].addMeasure(rollingTotalPathLength[i]);
            }
        }

        // Getting the average and standard deviations
        double[] averageTotalPathLength = Arrays.stream(cs).mapToDouble(CumStat::getMean).toArray();
        double[] stdevTotalPathLength = Arrays.stream(cs).mapToDouble(CumStat::getStd).toArray();

        return new double[][]{averageTotalPathLength,stdevTotalPathLength};

    }

    public int[][] getNumberOfObjects() {
        // Determining the first and last frames
        int firstFrame = Integer.MAX_VALUE;
        int lastFrame = 0;
        for (Track track:values()) {
            int[] f = track.getF();
            if (f[0] < firstFrame) {
                firstFrame = f[0];
            }
            if (f[f.length-1] > lastFrame) {
                lastFrame = f[f.length-1];
            }
        }

        // Counting the number of objects present in each frame
        int[] n = new int[lastFrame-firstFrame+1];
        for (Track track:values()) {
            int[] f = track.getF();
            for (int ff:f) {
                f[ff]++;
            }
        }

        // Creating an array of frame numbers
        int[] f = new int[lastFrame-firstFrame+1];
        for (int i=0;i<f.length;i++) {
            f[i] = i+firstFrame;
        }

        return new int[][]{f,n};

    }
}
