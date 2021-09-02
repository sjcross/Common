package io.github.sjcross.common.object;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PointTest {
    private double tolerance = 1E-2;

    @Test
    public void testCalculateDistanceToPoint() {
        Point<Integer> p1 = new Point<>(1,2,3);
        Point<Integer> p2 = new Point<>(2,3,5);

        double expected = 2.45;
        double actual = p1.calculateDistanceToPoint(p2);
        assertEquals(expected,actual,tolerance);

        expected = 0;
        actual = p1.calculateDistanceToPoint(p1);
        assertEquals(expected,actual,tolerance);

    }

    @Test
    public void testHashCode() {
        Point<Integer> p1 = new Point<>(1,2,3);
        Point<Integer> p2 = new Point<>(1,2,3);
        assertEquals(p1.hashCode(),p2.hashCode());

        Point<Double> p3 = new Point<>(1.5,2.1,-3.3);
        Point<Double> p4 = new Point<>(1.5,2.1,-3.3);
        assertEquals(p3.hashCode(),p4.hashCode());

        Point<Float> p5 = new Point<>(1.5f,2.1f,-3.3f);
        Point<Float> p6 = new Point<>(1.6f,2.1f,-3.3f);
        assertNotEquals(p5.hashCode(),p6.hashCode());

    }

    @Test
    public void testEquals() {
        Point<Integer> p1 = new Point<>(1,2,3);
        Point<Integer> p2 = new Point<>(1,2,3);
        assertTrue(p1.equals(p2));
        assertTrue(p2.equals(p1));

        Point<Double> p3 = new Point<>(1.5,2.1,-3.3);
        Point<Double> p4 = new Point<>(1.5,2.1,-3.3);
        assertTrue(p3.equals(p4));
        assertTrue(p4.equals(p3));

        Point<Float> p5 = new Point<>(1.5f,2.1f,-3.3f);
        Point<Float> p6 = new Point<>(1.6f,2.1f,-3.3f);
        assertFalse(p5.equals(p6));
        assertFalse(p6.equals(p5));

    }
}