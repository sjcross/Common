package wbif.sjx.common.Object;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Stephen Cross on 09/08/2018.
 */
public class TrackTest {

    // HASHCODE TESTS

    @Test
    public void testHashCodeDifferentValue() {
        Track track1 = new Track("px");
        track1.addTimepoint(1,2,3,1);
        track1.addTimepoint(4,3,12,2);
        track1.addTimepoint(2,1,2,3);
        track1.addTimepoint(1,2,5,4);

        Track track2 = new Track("px");
        track2.addTimepoint(1,2,3,1);
        track2.addTimepoint(-4,3,12,2);
        track2.addTimepoint(2,1,2,3);
        track2.addTimepoint(1,2,5,4);

        assertNotEquals(track1.hashCode(),track2.hashCode());

    }

    @Test
    public void testHashCodeDifferentPointOrder() {
        // Verifying that the order of point placement doesn't matter
        Track track1 = new Track("px");
        track1.addTimepoint(1,2,3,1);
        track1.addTimepoint(4,3,12,2);
        track1.addTimepoint(2,1,2,3);
        track1.addTimepoint(1,2,5,4);

        Track track2 = new Track("px");
        track2.addTimepoint(1,2,3,1);
        track2.addTimepoint(4,3,12,2);
        track2.addTimepoint(2,1,2,3);
        track2.addTimepoint(1,2,5,4);

        Track track3 = new Track("px");
        track3.addTimepoint(2,1,2,3);
        track3.addTimepoint(1,2,3,1);
        track3.addTimepoint(4,3,12,2);
        track3.addTimepoint(1,2,5,4);

        assertEquals(track1.hashCode(),track2.hashCode());
        assertEquals(track1.hashCode(),track3.hashCode());
        assertEquals(track2.hashCode(),track3.hashCode());

    }

    @Test
    public void testHashCodeMissingPoint() {
        // Verifying that all points need to be present for equality
        Track track1 = new Track("px");
        track1.addTimepoint(1,2,3,1);
        track1.addTimepoint(4,3,12,2);
        track1.addTimepoint(2,1,2,3);
        track1.addTimepoint(1,2,5,4);

        Track track2 = new Track("px");
        track2.addTimepoint(2,1,2,3);
        track2.addTimepoint(4,3,12,2);
        track2.addTimepoint(1,2,5,4);

        assertNotEquals(track1.hashCode(),track2.hashCode());

    }

    @Test
    public void testHashCodeDifferentUnits() {
        Track track1 = new Track("px");
        track1.addTimepoint(1,2,3,1);
        track1.addTimepoint(4,3,12,2);
        track1.addTimepoint(2,1,2,3);
        track1.addTimepoint(1,2,5,4);

        Track track2 = new Track("um");
        track2.addTimepoint(1,2,3,1);
        track2.addTimepoint(4,3,12,2);
        track2.addTimepoint(2,1,2,3);
        track2.addTimepoint(1,2,5,4);

        assertNotEquals(track1.hashCode(),track2.hashCode());

    }

    @Test
    public void testHashCodeDifferentUnitsCase() {
        Track track1 = new Track("PX");
        track1.addTimepoint(1,2,3,1);
        track1.addTimepoint(4,3,12,2);
        track1.addTimepoint(2,1,2,3);
        track1.addTimepoint(1,2,5,4);

        Track track2 = new Track("px");
        track2.addTimepoint(1,2,3,1);
        track2.addTimepoint(4,3,12,2);
        track2.addTimepoint(2,1,2,3);
        track2.addTimepoint(1,2,5,4);

        assertEquals(track1.hashCode(),track2.hashCode());

    }


    // EQUALITY TESTS

    @Test
    public void testEqualsDifferentValue() {
        Track track1 = new Track("px");
        track1.addTimepoint(1,2,3,1);
        track1.addTimepoint(4,3,12,2);
        track1.addTimepoint(2,1,2,3);
        track1.addTimepoint(1,2,5,4);

        Track track2 = new Track("px");
        track2.addTimepoint(1,2,3,1);
        track2.addTimepoint(-4,3,12,2);
        track2.addTimepoint(2,1,2,3);
        track2.addTimepoint(1,2,5,4);

        assertFalse(track1.equals(track2));
        assertFalse(track2.equals(track1));

    }

    @Test
    public void testEqualsDifferentPointOrder() {// Verifying that the order of point placement doesn't matter
        Track track1 = new Track("px");
        track1.addTimepoint(1,2,3,1);
        track1.addTimepoint(4,3,12,2);
        track1.addTimepoint(2,1,2,3);
        track1.addTimepoint(1,2,5,4);

        Track track2 = new Track("px");
        track2.addTimepoint(1,2,3,1);
        track2.addTimepoint(4,3,12,2);
        track2.addTimepoint(2,1,2,3);
        track2.addTimepoint(1,2,5,4);

        Track track3 = new Track("px");
        track3.addTimepoint(2,1,2,3);
        track3.addTimepoint(1,2,3,1);
        track3.addTimepoint(4,3,12,2);
        track3.addTimepoint(1,2,5,4);

        assertTrue(track1.equals(track2));
        assertTrue(track2.equals(track1));
        assertTrue(track1.equals(track3));
        assertTrue(track3.equals(track1));
        assertTrue(track2.equals(track3));
        assertTrue(track3.equals(track2));

    }

    @Test
    public void testEqualsMissingPoint() {
        // Verifying that all points need to be present for equality
        Track track1 = new Track("px");
        track1.addTimepoint(1,2,3,1);
        track1.addTimepoint(4,3,12,2);
        track1.addTimepoint(2,1,2,3);
        track1.addTimepoint(1,2,5,4);

        Track track2 = new Track("px");
        track2.addTimepoint(2,1,2,3);
        track2.addTimepoint(4,3,12,2);
        track2.addTimepoint(1,2,5,4);

        assertFalse(track1.equals(track2));
        assertFalse(track2.equals(track1));

    }

    @Test
    public void testEqualsDifferentUnits() {
        Track track1 = new Track("px");
        track1.addTimepoint(1,2,3,1);
        track1.addTimepoint(4,3,12,2);
        track1.addTimepoint(2,1,2,3);
        track1.addTimepoint(1,2,5,4);

        Track track2 = new Track("um");
        track2.addTimepoint(1,2,3,1);
        track2.addTimepoint(4,3,12,2);
        track2.addTimepoint(2,1,2,3);
        track2.addTimepoint(1,2,5,4);

        assertFalse(track1.equals(track2));
        assertFalse(track2.equals(track1));

    }

    @Test
    public void testEqualsDifferentUnitsCase() {
        Track track1 = new Track("px");
        track1.addTimepoint(1,2,3,1);
        track1.addTimepoint(4,3,12,2);
        track1.addTimepoint(2,1,2,3);
        track1.addTimepoint(1,2,5,4);

        Track track2 = new Track("PX");
        track2.addTimepoint(1,2,3,1);
        track2.addTimepoint(4,3,12,2);
        track2.addTimepoint(2,1,2,3);
        track2.addTimepoint(1,2,5,4);

        assertTrue(track1.equals(track2));
        assertTrue(track2.equals(track1));

    }
}