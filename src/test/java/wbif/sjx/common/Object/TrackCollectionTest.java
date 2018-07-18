package wbif.sjx.common.Object;

import org.junit.Test;
import wbif.sjx.common.Analysis.MSDCalculator;
import wbif.sjx.common.ExpectedObjects.Tracks2D;
import wbif.sjx.common.MathFunc.CumStat;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class TrackCollectionTest {

    @Test
    public void testGetAverageMSD() {
        double dppXY = 1;
        double dppZ = 1;
        String units = "px";

        LinkedHashMap<Integer,Track> tracks = new Tracks2D().getTracks(dppXY,dppZ,units);

        TrackCollection collection = new TrackCollection();
        collection.putAll(tracks);

        double[][] actual = collection.getAverageMSD(true);
        TreeMap<Integer,Double> expected = Tracks2D.getMeanMSD();

        for (int frame:expected.keySet()) {
            assertEquals(expected.get(frame),actual[1][frame],expected.get(frame)*1E-4);
        }
    }
}