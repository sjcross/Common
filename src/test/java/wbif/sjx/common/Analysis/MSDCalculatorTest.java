package wbif.sjx.common.Analysis;

import org.junit.Ignore;
import org.junit.Test;
import wbif.sjx.common.ExpectedObjects.Tracks2D;
import wbif.sjx.common.MathFunc.CumStat;
import wbif.sjx.common.Object.Track;

import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

/**
 * Created by steph on 09/07/2017.
 */
public class MSDCalculatorTest {
    private double tolerance = 1E-2;

    @Test @Ignore
    public void testCalculateProvidedCumStat() throws Exception {
    }

    @Test
    public void testCalculateNoProvidedCumStat() throws Exception {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        HashMap<Integer,Track> tracks = new Tracks2D().getTracks(dppXY,dppZ,units);

        int maxF = 0;
        for (Track track:tracks.values()) {
            double[][] limits = track.getLimits(true);
            if (limits[3][1] > maxF) maxF = (int) limits[3][1];
        }

        CumStat[] cs = new CumStat[maxF];
        for (int i=0;i<cs.length;i++) cs[i] = new CumStat();

        for (Track track:tracks.values()) {
            System.out.println(track.getF().length+"_"+track.values().size());
            MSDCalculator.calculate(cs, track.getF(),track.getX(true),track.getY(true),track.getZ(true));
        }

        TreeMap<Integer,Double> actual = new TreeMap<>();
        for (int i=0;i<cs.length;i++) actual.put(i,cs[i].getMean());
        TreeMap<Integer,Double> expected = Tracks2D.getMeanMSD();

        for (int frame:expected.keySet()) {
            System.out.println(expected.get(frame)+"_"+actual.get(frame));
//            assertEquals(expected.get(frame),actual.get(frame),tolerance);
        }
    }

    @Test @Ignore
    public void testGetLinearFit() throws Exception {
    }
}