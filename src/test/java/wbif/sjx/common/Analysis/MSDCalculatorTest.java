package wbif.sjx.common.Analysis;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import wbif.sjx.common.ExpectedObjects.SingleTrack2D;
import wbif.sjx.common.ExpectedObjects.Tracks2D;
import wbif.sjx.common.MathFunc.CumStat;
import wbif.sjx.common.Object.Track;

import java.util.LinkedHashMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Created by steph on 09/07/2017.
 */
public class MSDCalculatorTest {
    private double tolerance = 1;

    @Test
    public void testCalculateNoProvidedCumStatSingleTrack() throws Exception {
        Track track = SingleTrack2D.getTrack();

        TreeMap<Integer,CumStat> cs = MSDCalculator.calculate(track.getF(),track.getX(),track.getY(),track.getZ());
        TreeMap<Integer,Double> actual = new TreeMap<>();
        for (int df:cs.keySet()) actual.put(df,cs.get(df).getMean());

        TreeMap<Integer,Double> expected = SingleTrack2D.getMSD();

        for (int frame:expected.keySet()) {
            assertEquals(expected.get(frame),actual.get(frame),expected.get(frame)*1E-4);
        }
    }

    @Test
    public void testCalculateProvidedCumStatManyTracks() throws Exception {
        new Tracks2D();
        LinkedHashMap<Integer,Track> tracks = Tracks2D.getTracks();

        int maxF = 0;
        for (Track track:tracks.values()) {
            double[][] limits = track.getLimits();
            if (limits[3][1] > maxF) maxF = (int) limits[3][1];
        }

        TreeMap<Integer,CumStat> cs = new TreeMap<>();
        for (Track track:tracks.values()) {
            MSDCalculator.calculate(cs, track.getF(),track.getX(),track.getY(),track.getZ());
        }

        TreeMap<Integer,Double> actual = new TreeMap<>();
        for (int df:cs.keySet()) actual.put(df,cs.get(df).getMean());
        TreeMap<Integer,Double> expected = Tracks2D.getMeanMSD();

        for (int frame:expected.keySet()) {
            assertEquals(expected.get(frame),actual.get(frame),expected.get(frame)*1E-4);
        }
    }

    @Test
    @Disabled
    @Ignore
    public void testGetLinearFitSingleTrack() throws Exception {
//        Track track = SingleTrack2D.getTrack();
//
//        TreeMap<Integer,CumStat> msd = MSDCalculator.calculate(track.getF(),track.getX(),track.getY(),track.getZ());
//        double[] actual = MSDCalculator.getLinearFit(msd,81);
//        double[] expected = new double[]{6.622E2,0,81};
//
//        assertArrayEquals(expected,actual,tolerance);

    }
}