package io.github.sjcross.common.object;

import org.junit.jupiter.api.Test;
import io.github.sjcross.common.expectedobjects.Tracks2D;
import io.github.sjcross.common.mathfunc.CumStat;
import io.github.sjcross.common.object.tracks.Track;
import io.github.sjcross.common.object.tracks.TrackCollection;

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