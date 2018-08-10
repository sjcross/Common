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
        LinkedHashMap<Integer,Track> tracks = Tracks2D.getTracks();

        TrackCollection collection = new TrackCollection();
        collection.putAll(tracks);

        TreeMap<Integer,CumStat> actual = collection.getAverageMSD();
        TreeMap<Integer,Double> expected = Tracks2D.getMeanMSD();

        for (int df:expected.keySet()) {
            assertEquals(expected.get(df),actual.get(df).getMean(),expected.get(df)*1E-4);
        }
    }
}