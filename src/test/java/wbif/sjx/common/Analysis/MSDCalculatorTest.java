package wbif.sjx.common.Analysis;

import org.junit.Ignore;
import org.junit.Test;
import wbif.sjx.common.ExpectedObjects.SingleTrack2D;
import wbif.sjx.common.ExpectedObjects.Tracks2D;
import wbif.sjx.common.MathFunc.CumStat;
import wbif.sjx.common.Object.Track;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by steph on 09/07/2017.
 */
public class MSDCalculatorTest {
    private double tolerance = 1;

    @Test
    public void testCalculateNoProvidedCumStatSingleTrack() throws Exception {
        double dppXY = 1;
        double dppZ = 1;
        String units = "px";

        Track track = SingleTrack2D.getTrack(dppXY,dppZ,units);

        TreeMap<Integer,CumStat> cs = MSDCalculator.calculate(track.getF(),track.getX(true),track.getY(true),track.getZ(true));
        TreeMap<Integer,Double> actual = new TreeMap<>();
        for (int df:cs.keySet()) actual.put(df,cs.get(df).getMean());

        TreeMap<Integer,Double> expected = SingleTrack2D.getMSD();

        for (int frame:expected.keySet()) {
            assertEquals(expected.get(frame),actual.get(frame),expected.get(frame)*1E-4);
        }
    }

    @Test
    public void testCalculateProvidedCumStatManyTracks() throws Exception {
        double dppXY = 1;
        double dppZ = 1;
        String units = "px";

        LinkedHashMap<Integer,Track> tracks = new Tracks2D().getTracks(dppXY,dppZ,units);

        int maxF = 0;
        for (Track track:tracks.values()) {
            double[][] limits = track.getLimits(true);
            if (limits[3][1] > maxF) maxF = (int) limits[3][1];
        }

        TreeMap<Integer,CumStat> cs = new TreeMap<>();
        for (Track track:tracks.values()) {
            MSDCalculator.calculate(cs, track.getF(),track.getX(true),track.getY(true),track.getZ(true));
        }

        TreeMap<Integer,Double> actual = new TreeMap<>();
        for (int df:cs.keySet()) actual.put(df,cs.get(df).getMean());
        TreeMap<Integer,Double> expected = Tracks2D.getMeanMSD();

        for (int frame:expected.keySet()) {
            assertEquals(expected.get(frame),actual.get(frame),expected.get(frame)*1E-4);
        }
    }

    @Test @Ignore
    public void testGetLinearFitSingleTrack() throws Exception {
        double dppXY = 1;
        double dppZ = 1;
        String units = "px";

        Track track = SingleTrack2D.getTrack(dppXY,dppZ,units);

        TreeMap<Integer,CumStat> msd = MSDCalculator.calculate(track.getF(),track.getX(true),track.getY(true),track.getZ(true));
        double[] actual = MSDCalculator.getLinearFit(msd,81);
        double[] expected = new double[]{6.622E2,0,81};

        System.out.println(actual[0]+"_"+actual[1]);

        assertArrayEquals(expected,actual,tolerance);

    }
}