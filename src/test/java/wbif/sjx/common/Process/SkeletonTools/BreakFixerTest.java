package wbif.sjx.common.Process.SkeletonTools;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BreakFixerTest {
    private double tolerance = 1E-10;

    @Test @Disabled
    public void getEndPoints() {
    }

    @Test
    public void getEndAngle135() {
        ArrayList<int[]> c = new ArrayList<>();
        c.add(new int[]{3,7});
        c.add(new int[]{4,6});
        c.add(new int[]{5,5});
        c.add(new int[]{6,4});
        c.add(new int[]{7,3});

        double actual = Math.toDegrees(BreakFixer.getEndAngleRads(c));
        assertEquals(135,actual,tolerance);

    }

    @Test
    public void getEndAngle90() {
        ArrayList<int[]> c = new ArrayList<>();
        c.add(new int[]{7,7});
        c.add(new int[]{7,6});
        c.add(new int[]{7,5});
        c.add(new int[]{7,4});
        c.add(new int[]{7,3});

        double actual = Math.toDegrees(BreakFixer.getEndAngleRads(c));
        assertEquals(90,actual,tolerance);

    }

    @Test
    public void getEndAngle45() {
        ArrayList<int[]> c = new ArrayList<>();
        c.add(new int[]{7,7});
        c.add(new int[]{6,6});
        c.add(new int[]{5,5});
        c.add(new int[]{4,4});
        c.add(new int[]{3,3});

        double actual = Math.toDegrees(BreakFixer.getEndAngleRads(c));
        assertEquals(45,actual,tolerance);

    }

    @Test
    public void getEndAngle0() {
        ArrayList<int[]> c = new ArrayList<>();
        c.add(new int[]{7,7});
        c.add(new int[]{6,7});
        c.add(new int[]{5,7});
        c.add(new int[]{4,7});
        c.add(new int[]{3,7});

        double actual = Math.toDegrees(BreakFixer.getEndAngleRads(c));
        assertEquals(0,actual,tolerance);

    }

    @Test
    public void getEndAnglem45() {
        ArrayList<int[]> c = new ArrayList<>();
        c.add(new int[]{7,3});
        c.add(new int[]{6,4});
        c.add(new int[]{5,5});
        c.add(new int[]{4,6});
        c.add(new int[]{3,7});

        double actual = Math.toDegrees(BreakFixer.getEndAngleRads(c));
        assertEquals(-45,actual,tolerance);

    }

    @Test
    public void getEndAnglem90() {
        ArrayList<int[]> c = new ArrayList<>();
        c.add(new int[]{7,3});
        c.add(new int[]{7,4});
        c.add(new int[]{7,5});
        c.add(new int[]{7,6});
        c.add(new int[]{7,7});

        double actual = Math.toDegrees(BreakFixer.getEndAngleRads(c));
        assertEquals(-90,actual,tolerance);

    }

    @Test
    public void getEndAnglem135() {
        ArrayList<int[]> c = new ArrayList<>();
        c.add(new int[]{3,3});
        c.add(new int[]{4,4});
        c.add(new int[]{5,5});
        c.add(new int[]{6,6});
        c.add(new int[]{7,7});

        double actual = Math.toDegrees(BreakFixer.getEndAngleRads(c));
        assertEquals(-135,actual,tolerance);

    }

    @Test
    public void getEndAnglem180() {
        ArrayList<int[]> c = new ArrayList<>();
        c.add(new int[]{3,7});
        c.add(new int[]{4,7});
        c.add(new int[]{5,7});
        c.add(new int[]{6,7});
        c.add(new int[]{7,7});

        double actual = Math.toDegrees(BreakFixer.getEndAngleRads(c));
        assertEquals(-180,actual,tolerance);

    }

    @Test @Disabled
    public void getDist() {
    }

    @Test @Disabled
    public void process() {
    }
}