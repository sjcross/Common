package io.github.sjcross.sjcommon.object;

import org.junit.jupiter.api.Test;

import io.github.sjcross.sjcommon.object.tracks.Timepoint;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Stephen Cross on 09/08/2018.
 */
public class TimepointTest {

    @Test
    public void testHashCode() {
        Timepoint<Integer> p1 = new Timepoint<>(1,2,3,1);
        Timepoint<Integer> p2 = new Timepoint<>(1,2,3,1);
        assertEquals(p1.hashCode(),p2.hashCode());

        Timepoint<Double> p3 = new Timepoint<>(1.5,2.1,-3.3,0);
        Timepoint<Double> p4 = new Timepoint<>(1.5,2.1,-3.3,0);
        assertEquals(p3.hashCode(),p4.hashCode());

        Timepoint<Double> p5 = new Timepoint<>(1.5,2.1,-3.3,-1);
        Timepoint<Double> p6 = new Timepoint<>(1.5,2.1,-3.3,-1);
        assertEquals(p5.hashCode(),p6.hashCode());

        Timepoint<Float> p7 = new Timepoint<>(1.5f,2.1f,-3.3f,2);
        Timepoint<Float> p8 = new Timepoint<>(1.6f,2.1f,-3.3f,2);
        assertNotEquals(p7.hashCode(),p8.hashCode());

        Timepoint<Float> p9 = new Timepoint<>(1.5f,2.1f,-3.3f,2);
        Timepoint<Float> p10 = new Timepoint<>(1.5f,2.1f,-3.3f,3);
        assertNotEquals(p9.hashCode(),p10.hashCode());

    }

    @Test
    public void testEquals() {
        Timepoint<Integer> p1 = new Timepoint<>(1,2,3,1);
        Timepoint<Integer> p2 = new Timepoint<>(1,2,3,1);
        assertTrue(p1.equals(p2));
        assertTrue(p2.equals(p1));

        Timepoint<Double> p3 = new Timepoint<>(1.5,2.1,-3.3,0);
        Timepoint<Double> p4 = new Timepoint<>(1.5,2.1,-3.3,0);
        assertTrue(p3.equals(p4));
        assertTrue(p4.equals(p3));

        Timepoint<Double> p5 = new Timepoint<>(1.5,2.1,-3.3,-1);
        Timepoint<Double> p6 = new Timepoint<>(1.5,2.1,-3.3,-1);
        assertTrue(p5.equals(p6));
        assertTrue(p6.equals(p5));

        Timepoint<Float> p7 = new Timepoint<>(1.5f,2.1f,-3.3f,2);
        Timepoint<Float> p8 = new Timepoint<>(1.6f,2.1f,-3.3f,2);
        assertFalse(p7.equals(p8));
        assertFalse(p8.equals(p7));

        Timepoint<Float> p9 = new Timepoint<>(1.5f,2.1f,-3.3f,2);
        Timepoint<Float> p10 = new Timepoint<>(1.5f,2.1f,-3.3f,3);
        assertFalse(p9.equals(p10));
        assertFalse(p10.equals(p9));

    }
}