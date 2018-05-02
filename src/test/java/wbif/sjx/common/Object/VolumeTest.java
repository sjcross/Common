package wbif.sjx.common.Object;

import org.junit.Test;

import static org.junit.Assert.*;

public class VolumeTest {
    private double tolerance = 1E-10;


    // ADDING COORDINATES

    @Test
    public void testAddCoordAlreadyExists() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(0,0,0);
        volume.addCoord(0,0,0);
        volume.addCoord(1,0,0);

        assertEquals(2,volume.getPoints().size());

    }


    // COORDINATE TESTS

    @Test
    public void testGetXNoVolume() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);

        double[] actualX = volume.getX(true);
        double[] expectedX = new double[]{};

        assertArrayEquals(expectedX,actualX,tolerance);
        assertEquals(0,volume.getPoints().size());

    }

    @Test
    public void testGetXPixelDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        double[] actualX = volume.getX(true);
        double[] expectedX = new double[]{10,10,11,13};

        assertArrayEquals(expectedX,actualX,tolerance);

    }

    @Test
    public void testGetXCalibratedDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        double[] actualX = volume.getX(false);
        double[] expectedX = new double[]{0.2,0.2,0.22,0.26};

        assertArrayEquals(expectedX,actualX,tolerance);

    }

    @Test
    public void testGetYPixelDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        double[] actualY = volume.getY(true);
        double[] expectedY = new double[]{5,5,5,7};

        assertArrayEquals(expectedY,actualY,tolerance);

    }

    @Test
    public void testGetYCalibratedDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        double[] actualY = volume.getY(false);
        double[] expectedY = new double[]{0.1,0.1,0.1,0.14};

        assertArrayEquals(expectedY,actualY,tolerance);

    }

    @Test
    public void testGetZPixelDistancesDoesntMatchXY() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        double[] actualZ = volume.getZ(true,false);
        double[] expectedZ = new double[]{1,2,2,1};

        assertArrayEquals(expectedZ,actualZ,tolerance);

    }

    @Test
    public void testGetZPixelDistancesDoesMatchXY() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        double[] actualZ = volume.getZ(true,true);
        double[] expectedZ = new double[]{5,10,10,5};

        assertArrayEquals(expectedZ,actualZ,tolerance);

    }

    @Test
    public void testGetZCalibratedDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        double[] actualZ = volume.getZ(false,true);
        double[] expectedZ = new double[]{0.1,0.2,0.2,0.1};
        assertArrayEquals(expectedZ,actualZ,tolerance);

        // The result shouldn't be affected by the XY matching when using calibrated distances
        actualZ = volume.getZ(false,false);
        assertArrayEquals(expectedZ,actualZ,tolerance);

    }


    // MEAN POSITION

    @Test
    public void testGetXMeanPixelDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertEquals(11,volume.getXMean(true),tolerance);

    }

    @Test
    public void testGetXMeanCalibratedDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertEquals(0.22,volume.getXMean(false),tolerance);

    }

    @Test
    public void testGetYMeanPixelDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertEquals(5.5,volume.getYMean(true),tolerance);

    }

    @Test
    public void testGetYMeanCalibratedDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertEquals(0.11,volume.getYMean(false),tolerance);

    }

    @Test
    public void testGetZMeanPixelDistancesDoesntMatchXY() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertEquals(1.5,volume.getZMean(true,false),tolerance);

    }

    @Test
    public void testGetZMeanPixelDistancesDoesMatchXY() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertEquals(7.5,volume.getZMean(true,true),tolerance);

    }

    @Test
    public void testGetZMeanCalibratedDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertEquals(0.15,volume.getZMean(false,false),tolerance);
        assertEquals(0.15,volume.getZMean(false,true),tolerance);

    }


    // ANGLE BETWEEN TWO VOLUMES

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


    // HEIGHT
    @Test
    public void testGetHeightPixelDistancesDoesntMatchXY() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,3);
        volume.addCoord(13,7,1);

        assertEquals(2,volume.getHeight(true,false),tolerance);

    }

    @Test
    public void testGetHeightPixelDistancesDoesMatchXY() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,3);
        volume.addCoord(13,7,1);

        assertEquals(10,volume.getHeight(true,true),tolerance);

    }

    @Test
    public void testGetHeightCalibratedDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,3);
        volume.addCoord(13,7,1);

        assertEquals(0.2,volume.getHeight(false,true),tolerance);
        assertEquals(0.2,volume.getHeight(false,false),tolerance);

    }


    // EXTENTS
    @Test
    public void testGetExtentsPixelDistancesDoesntMatchXY() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,3);
        volume.addCoord(13,7,1);

        double[] actualExtents = volume.getExtents(true,false);
        double[] expectedExtents = new double[]{10,13,5,7,1,3};

        assertArrayEquals(expectedExtents,actualExtents,tolerance);

    }

    @Test
    public void testGetExtentsPixelDistancesDoesMatchXY() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,3);
        volume.addCoord(13,7,1);

        double[] actualExtents = volume.getExtents(true,true);
        double[] expectedExtents = new double[]{10,13,5,7,5,15};

        assertArrayEquals(expectedExtents,actualExtents,tolerance);

    }

    @Test
    public void testGetExtentscalibratedDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,3);
        volume.addCoord(13,7,1);

        double[] expectedExtents = new double[]{0.2,0.26,0.1,0.14,0.1,0.3};
        double[] actualExtents = volume.getExtents(false,true);
        assertArrayEquals(expectedExtents,actualExtents,tolerance);

        actualExtents = volume.getExtents(false,false);
        assertArrayEquals(expectedExtents,actualExtents,tolerance);

    }

    @Test
    public void testGetExtents2DPixelDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,3);
        volume.addCoord(13,7,1);

        double[] expectedExtents = new double[]{10,13,5,7};
        double[] actualExtents = volume.getExtents2D(true);
        assertArrayEquals(expectedExtents,actualExtents,tolerance);

    }

    @Test
    public void testGetExtents2DDistancesCalibratedDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,3);
        volume.addCoord(13,7,1);

        double[] expectedExtents = new double[]{0.2,0.26,0.1,0.14};
        double[] actualExtents = volume.getExtents2D(false);
        assertArrayEquals(expectedExtents,actualExtents,tolerance);

    }


    // AREA VOLUME CHECKS

    @Test
    public void testHasVolumeNoVolume() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);

        assertFalse(volume.hasVolume());

    }

    @Test
    public void testHasVolume2D() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,1);
        volume.addCoord(11,5,1);
        volume.addCoord(13,7,1);

        assertFalse(volume.hasVolume());

    }

    @Test
    public void testHasVolume3D() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertTrue(volume.hasVolume());

    }

    @Test
    public void testHasAreaNoVolume() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);

        assertFalse(volume.hasArea());

    }

    @Test
    public void testHasArea2D() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,1);
        volume.addCoord(11,5,1);
        volume.addCoord(13,7,1);

        assertTrue(volume.hasArea());

    }

    @Test
    public void testHasArea3D() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertTrue(volume.hasArea());

    }


    // VOLUME

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

    @Test
    public void testGetNVoxelsNoVolume() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);

        assertEquals(0,volume.getNVoxels());

    }

    @Test
    public void testGetNVoxelsHasVolume() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertEquals(4,volume.getNVoxels());

    }

    @Test
    public void testGetContainedVolumePixelDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertEquals(20,volume.getContainedVolume(true),tolerance);

    }

    @Test
    public void testGetContainedVolumeCalibratedDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertEquals(0.00016,volume.getContainedVolume(false),tolerance);

    }

    @Test
    public void testGetContainedVolumePixelDistancesFlatObject() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,6,1);
        volume.addCoord(11,5,1);
        volume.addCoord(13,7,1);

        assertEquals(20,volume.getContainedVolume(true),tolerance);

    }

    @Test
    public void testGetContainedVolumeCalibratedDistancesFlatObject() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units);
        volume.addCoord(10,5,1);
        volume.addCoord(10,6,1);
        volume.addCoord(11,5,1);
        volume.addCoord(13,7,1);

        assertEquals(0.00016,volume.getContainedVolume(false),tolerance);

    }

    // HASHCODE TESTS

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


    // EQUALITY TESTS

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