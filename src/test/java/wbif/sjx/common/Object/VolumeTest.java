package wbif.sjx.common.Object;

import org.junit.Test;

import static org.junit.Assert.*;

public class VolumeTest {
    private double tolerance = 1E-2;

    @Test
    public void testCalculateAngle2DTopRight() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(dppXY,dppZ,units);
        volume1.addCoord(0,0,0);

        Volume volume2 = new Volume(dppXY,dppZ,units);
        volume2.addCoord(10,10,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(45);

        assertEquals(expectedAngle,actualAngle,tolerance);

    }

    @Test
    public void testCalculateAngle2DTopLeftQ() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(dppXY,dppZ,units);
        volume1.addCoord(0,0,0);

        Volume volume2 = new Volume(dppXY,dppZ,units);
        volume2.addCoord(-5,5,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(135);

        assertEquals(expectedAngle,actualAngle,tolerance);

    }

    @Test
    public void testCalculateAngle2DBottomLeftQ() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(dppXY,dppZ,units);
        volume1.addCoord(0,0,0);

        Volume volume2 = new Volume(dppXY,dppZ,units);
        volume2.addCoord(-5,-5,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(-135);

        assertEquals(expectedAngle,actualAngle,tolerance);

    }

    @Test
    public void testCalculateAngle2DBottomRightQ() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(dppXY,dppZ,units);
        volume1.addCoord(0,0,0);

        Volume volume2 = new Volume(dppXY,dppZ,units);
        volume2.addCoord(5,-5,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(-45);

        assertEquals(expectedAngle,actualAngle,tolerance);

    }

    @Test
    public void testCalculateAngle2DPositiveXAxis() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(dppXY,dppZ,units);
        volume1.addCoord(0,0,0);

        Volume volume2 = new Volume(dppXY,dppZ,units);
        volume2.addCoord(5,0,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(0);

        assertEquals(expectedAngle,actualAngle,tolerance);

    }

    @Test
    public void testCalculateAngle2DPositiveYAxis() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(dppXY,dppZ,units);
        volume1.addCoord(0,0,0);

        Volume volume2 = new Volume(dppXY,dppZ,units);
        volume2.addCoord(0,5,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(90);

        assertEquals(expectedAngle,actualAngle,tolerance);

    }

    @Test
    public void testCalculateAngle2DNegativeXAxis() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(dppXY,dppZ,units);
        volume1.addCoord(0,0,0);

        Volume volume2 = new Volume(dppXY,dppZ,units);
        volume2.addCoord(-5,0,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(180);

        assertEquals(expectedAngle,actualAngle,tolerance);

    }

    @Test
    public void testCalculateAngle2DNegativeYAxis() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(dppXY,dppZ,units);
        volume1.addCoord(0,0,0);

        Volume volume2 = new Volume(dppXY,dppZ,units);
        volume2.addCoord(0,-5,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(-90);

        assertEquals(expectedAngle,actualAngle,tolerance);

    }

    @Test
    public void testContainsPoint() {
        Volume volume1 = new Volume(2.0,1.0,"PX");
        volume1.addCoord(1,2,3);
        volume1.addCoord(4,3,12);
        volume1.addCoord(2,1,2);
        volume1.addCoord(1,2,5);

        Point<Integer> point1 = new Point<>(1,2,3);
        Point<Integer> point2 = new Point<>(2,2,3);

        assertTrue(volume1.getPoints().contains(point1));
        assertFalse(volume1.getPoints().contains(point2));

    }


    // Testing hashcode generation

    @Test
    public void testHashCodeDifferentValue() {
        Volume volume1 = new Volume(1.0,1.0,"Test");
        volume1.addCoord(1,2,3);
        volume1.addCoord(4,3,12);
        volume1.addCoord(2,1,2);
        volume1.addCoord(1,2,5);

        Volume volume2 = new Volume(1.0,1.0,"Test");
        volume2.addCoord(1,2,3);
        volume2.addCoord(-4,3,12);
        volume2.addCoord(2,1,2);
        volume2.addCoord(1,2,5);

        assertNotEquals(volume1.hashCode(),volume2.hashCode());

    }

    @Test
    public void testHashCodeDifferentPointOrder() {
        // Verifying that the order of point placement doesn't matter
        Volume volume1 = new Volume(2.0,1.0,"Test");
        volume1.addCoord(1,2,3);
        volume1.addCoord(4,3,12);
        volume1.addCoord(2,1,2);
        volume1.addCoord(1,2,5);

        Volume volume2 = new Volume(2.0,1.0,"Test");
        volume2.addCoord(1,2,3);
        volume2.addCoord(4,3,12);
        volume2.addCoord(2,1,2);
        volume2.addCoord(1,2,5);

        Volume volume3 = new Volume(2.0,1.0,"Test");
        volume3.addCoord(2,1,2);
        volume3.addCoord(1,2,3);
        volume3.addCoord(4,3,12);
        volume3.addCoord(1,2,5);

        assertEquals(volume1.hashCode(),volume2.hashCode());
        assertEquals(volume1.hashCode(),volume3.hashCode());
        assertEquals(volume2.hashCode(),volume3.hashCode());

    }

    @Test
    public void testHashCodeMissingPoint() {
        // Verifying that all points need to be present for equality
        Volume volume1 = new Volume(1.0,1.0,"Test");
        volume1.addCoord(1,2,3);
        volume1.addCoord(4,3,12);
        volume1.addCoord(2,1,2);
        volume1.addCoord(1,2,5);

        Volume volume2 = new Volume(1.0,1.0,"Test");
        volume2.addCoord(2,1,2);
        volume2.addCoord(4,3,12);
        volume2.addCoord(1,2,5);

        assertNotEquals(volume1.hashCode(),volume2.hashCode());

    }

    @Test
    public void testHashCodeDifferentCalibration() {
        Volume volume1 = new Volume(2.0,1.0,"PX");
        volume1.addCoord(1,2,3);
        volume1.addCoord(4,3,12);
        volume1.addCoord(2,1,2);
        volume1.addCoord(1,2,5);

        Volume volume2 = new Volume(2.1,1.0,"PX");
        volume2.addCoord(1,2,3);
        volume2.addCoord(4,3,12);
        volume2.addCoord(2,1,2);
        volume2.addCoord(1,2,5);

        assertNotEquals(volume1.hashCode(),volume2.hashCode());

    }

    @Test
    public void testHashCodeDifferentUnitsCase() {
        Volume volume1 = new Volume(2.0,1.0,"PX");
        volume1.addCoord(1,2,3);
        volume1.addCoord(4,3,12);
        volume1.addCoord(2,1,2);
        volume1.addCoord(1,2,5);

        Volume volume2 = new Volume(2.0,1.0,"px");
        volume2.addCoord(1,2,3);
        volume2.addCoord(4,3,12);
        volume2.addCoord(2,1,2);
        volume2.addCoord(1,2,5);

        assertEquals(volume1.hashCode(),volume2.hashCode());

    }

    @Test
    public void testHashCodeDifferentUnits() {
        Volume volume1 = new Volume(2.0,1.0,"PX");
        volume1.addCoord(1,2,3);
        volume1.addCoord(4,3,12);
        volume1.addCoord(2,1,2);
        volume1.addCoord(1,2,5);

        Volume volume2 = new Volume(2.0,1.0,"um");
        volume2.addCoord(1,2,3);
        volume2.addCoord(4,3,12);
        volume2.addCoord(2,1,2);
        volume2.addCoord(1,2,5);

        assertNotEquals(volume1.hashCode(),volume2.hashCode());

    }


    // Testing equals

    @Test
    public void testEqualsDifferentValue() {
        Volume volume1 = new Volume(1.0,1.0,"Test");
        volume1.addCoord(1,2,3);
        volume1.addCoord(4,3,12);
        volume1.addCoord(2,1,2);
        volume1.addCoord(1,2,5);

        Volume volume2 = new Volume(1.0,1.0,"Test");
        volume2.addCoord(1,2,3);
        volume2.addCoord(-4,3,12);
        volume2.addCoord(2,1,2);
        volume2.addCoord(1,2,5);

        assertFalse(volume1.equals(volume2));
        assertFalse(volume2.equals(volume1));

    }

    @Test
    public void testEqualsDifferentPointOrder() {// Verifying that the order of point placement doesn't matter
        Volume volume1 = new Volume(2.0,1.0,"PX");
        volume1.addCoord(1,2,3);
        volume1.addCoord(4,3,12);
        volume1.addCoord(2,1,2);
        volume1.addCoord(1,2,5);

        Volume volume2 = new Volume(2.0,1.0,"PX");
        volume2.addCoord(1,2,3);
        volume2.addCoord(4,3,12);
        volume2.addCoord(2,1,2);
        volume2.addCoord(1,2,5);

        Volume volume3 = new Volume(2.0,1.0,"PX");
        volume3.addCoord(2,1,2);
        volume3.addCoord(1,2,3);
        volume3.addCoord(4,3,12);
        volume3.addCoord(1,2,5);

        assertTrue(volume1.equals(volume2));
        assertTrue(volume2.equals(volume1));
        assertTrue(volume1.equals(volume3));
        assertTrue(volume3.equals(volume1));
        assertTrue(volume2.equals(volume3));
        assertTrue(volume3.equals(volume2));

    }

    @Test
    public void testEqualsMissingPoint() {
        // Verifying that all points need to be present for equality
        Volume volume1 = new Volume(1.0,1.0,"Test");
        volume1.addCoord(1,2,3);
        volume1.addCoord(4,3,12);
        volume1.addCoord(2,1,2);
        volume1.addCoord(1,2,5);

        Volume volume2 = new Volume(1.0,1.0,"Test");
        volume2.addCoord(2,1,2);
        volume2.addCoord(4,3,12);
        volume2.addCoord(1,2,5);

        assertFalse(volume1.equals(volume2));
        assertFalse(volume2.equals(volume1));

    }

    @Test
    public void testEqualsDifferentCalibration() {
        Volume volume1 = new Volume(2.0,1.0,"PX");
        volume1.addCoord(1,2,3);
        volume1.addCoord(4,3,12);
        volume1.addCoord(2,1,2);
        volume1.addCoord(1,2,5);

        Volume volume2 = new Volume(2.1,1.0,"PX");
        volume2.addCoord(1,2,3);
        volume2.addCoord(4,3,12);
        volume2.addCoord(2,1,2);
        volume2.addCoord(1,2,5);

        assertFalse(volume1.equals(volume2));
        assertFalse(volume2.equals(volume1));

    }

    @Test
    public void testEqualsDifferentUnitsCase() {
        Volume volume1 = new Volume(2.0,1.0,"PX");
        volume1.addCoord(1,2,3);
        volume1.addCoord(4,3,12);
        volume1.addCoord(2,1,2);
        volume1.addCoord(1,2,5);

        Volume volume2 = new Volume(2.0,1.0,"px");
        volume2.addCoord(1,2,3);
        volume2.addCoord(4,3,12);
        volume2.addCoord(2,1,2);
        volume2.addCoord(1,2,5);

        assertTrue(volume1.equals(volume2));
        assertTrue(volume2.equals(volume1));

    }

    @Test
    public void testEqualsDifferentUnits() {
        Volume volume1 = new Volume(2.0,1.0,"PX");
        volume1.addCoord(1,2,3);
        volume1.addCoord(4,3,12);
        volume1.addCoord(2,1,2);
        volume1.addCoord(1,2,5);

        Volume volume2 = new Volume(2.0,1.0,"um");
        volume2.addCoord(1,2,3);
        volume2.addCoord(4,3,12);
        volume2.addCoord(2,1,2);
        volume2.addCoord(1,2,5);

        assertFalse(volume1.equals(volume2));
        assertFalse(volume2.equals(volume1));

    }

}