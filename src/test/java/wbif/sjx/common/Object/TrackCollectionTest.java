package wbif.sjx.common.Object;

import org.junit.jupiter.api.Test;
import wbif.sjx.common.ExpectedObjects.Tracks2D;
import wbif.sjx.common.MathFunc.CumStat;
import wbif.sjx.common.Object.Tracks.Track;
import wbif.sjx.common.Object.Tracks.TrackCollection;

import java.util.LinkedHashMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

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