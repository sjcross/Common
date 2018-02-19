package wbif.sjx.common.Object;

import org.junit.Test;

import static org.junit.Assert.*;

public class VolumeTest {
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