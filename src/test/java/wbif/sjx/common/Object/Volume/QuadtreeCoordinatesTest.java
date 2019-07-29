package wbif.sjx.common.Object.Volume;

import org.junit.Ignore;
import org.junit.Test;
import wbif.sjx.common.Exceptions.IntegerOverflowException;
import wbif.sjx.common.Object.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.*;

public class QuadtreeCoordinatesTest {
    private double tolerance10 = 1E-10;
    private double tolerance2 = 1E-2;


    // ADDING COORDINATES

    @Test
    public void testAddCoordAlreadyExists() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,10,10,1,dppXY,dppZ,units);
        volume.add(0,0,0);
        volume.add(0,0,0);
        volume.add(1,0,0);

        assertEquals(2,volume.size());

    }


    // COORDINATE TESTS

    @Test
    public void testGetXCoords() throws IntegerOverflowException {
        Volume volume = new Volume(VolumeType.QUADTREE,10,10,13,2.0,1.0,"PX");
        volume.add(1,2,3);
        volume.add(4,3,12);
        volume.add(2,1,2);
        volume.add(1,2,5);
        volume.add(1,2,8);
        volume.add(1,4,8);
        volume.add(2,4,8);
        volume.add(3,4,8);
        volume.add(2,4,2);
        volume.add(2,6,9);

        ArrayList<Integer> actual = volume.getXCoords();
        Collections.sort(actual);

        ArrayList<Integer> expected = new ArrayList<Integer>(Arrays.asList(1,4,2,1,1,1,2,3,2,2));
        Collections.sort(expected);

        assertEquals(10,actual.size());
        assertEquals(expected,actual);

    }

    @Test
    public void testGetYCoords() throws IntegerOverflowException {
        Volume volume = new Volume(VolumeType.QUADTREE,10,10,13,2.0,1.0,"PX");
        volume.add(1,2,3);
        volume.add(4,3,12);
        volume.add(2,1,2);
        volume.add(1,2,5);
        volume.add(1,2,8);
        volume.add(1,4,8);
        volume.add(2,4,8);
        volume.add(3,4,8);
        volume.add(2,4,2);
        volume.add(2,6,9);

        ArrayList<Integer> actual = volume.getYCoords();
        Collections.sort(actual);

        ArrayList<Integer> expected = new ArrayList<Integer>(Arrays.asList(2,3,1,2,2,4,4,4,4,6));
        Collections.sort(expected);

        assertEquals(10,actual.size());
        assertEquals(expected,actual);

    }

    @Test
    public void testGetZCoords() throws IntegerOverflowException {
        Volume volume = new Volume(VolumeType.QUADTREE,10,10,13,2.0,1.0,"PX");
        volume.add(1,2,3);
        volume.add(4,3,12);
        volume.add(2,1,2);
        volume.add(1,2,5);
        volume.add(1,2,8);
        volume.add(1,4,8);
        volume.add(2,4,8);
        volume.add(3,4,8);
        volume.add(2,4,2);
        volume.add(2,6,9);

        ArrayList<Integer> actual = volume.getZCoords();
        Collections.sort(actual);

        ArrayList<Integer> expected = new ArrayList<Integer>(Arrays.asList(3,12,2,5,8,8,8,8,2,9));
        Collections.sort(expected);

        assertEquals(10,actual.size());
        assertEquals(expected,actual);

    }

    @Test
    public void testGetXNoVolume2() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,10,10,13,dppXY,dppZ,units);

        double[] actualX = volume.getX(true);
        double[] expectedX = new double[]{};

        assertArrayEquals(expectedX,actualX, tolerance10);
        assertEquals(0,volume.size());

    }

    @Test
    public void testGetXPixelDistances() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        double[] actualX = volume.getX(true);
        double[] expectedX = new double[]{10,10,11,13};

        assertArrayEquals(expectedX,actualX, tolerance10);

    }

    @Test
    public void testGetXCalibratedDistances() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        double[] actualX = volume.getX(false);
        double[] expectedX = new double[]{0.2,0.2,0.22,0.26};

        assertArrayEquals(expectedX,actualX, tolerance10);

    }

    @Test
    public void testGetYPixelDistances() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        double[] actualY = volume.getY(true);
        double[] expectedY = new double[]{5,5,5,7};

        assertArrayEquals(expectedY,actualY, tolerance10);

    }

    @Test
    public void testGetYCalibratedDistances() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        double[] actualY = volume.getY(false);
        double[] expectedY = new double[]{0.1,0.1,0.1,0.14};

        assertArrayEquals(expectedY,actualY, tolerance10);

    }

    @Test
    public void testGetZPixelDistancesDoesntMatchXY() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        double[] actualZ = volume.getZ(true,false);
        double[] expectedZ = new double[]{1,2,2,1};

        assertArrayEquals(expectedZ,actualZ, tolerance10);

    }

    @Test
    public void testGetZPixelDistancesDoesMatchXY() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        double[] actualZ = volume.getZ(true,true);
        double[] expectedZ = new double[]{5,10,10,5};

        assertArrayEquals(expectedZ,actualZ, tolerance10);

    }

    @Test
    public void testGetZCalibratedDistances() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        double[] actualZ = volume.getZ(false,true);
        double[] expectedZ = new double[]{0.1,0.2,0.2,0.1};
        assertArrayEquals(expectedZ,actualZ, tolerance10);

        // The result shouldn't be affected by the XY matching when using calibrated distances
        actualZ = volume.getZ(false,false);
        assertArrayEquals(expectedZ,actualZ, tolerance10);

    }

    @Test
    public void testGetXYScaledZ() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);

        double actual = volume.getXYScaledZ(1);
        double expected = 5d;

        assertEquals(expected,actual, tolerance10);

    }


    // MEAN POSITION

    @Test
    public void testGetXMeanPixelDistances() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(11,volume.getXMean(true), tolerance10);

    }

    @Test
    public void testGetXMeanCalibratedDistances() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(0.22,volume.getXMean(false), tolerance10);

    }

    @Test
    public void testGetYMeanPixelDistances() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(5.5,volume.getYMean(true), tolerance10);

    }

    @Test
    public void testGetYMeanCalibratedDistances() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(0.11,volume.getYMean(false), tolerance10);

    }

    @Test
    public void testGetZMeanPixelDistancesDoesntMatchXY() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(1.5,volume.getZMean(true,false), tolerance10);

    }

    @Test
    public void testGetZMeanPixelDistancesDoesMatchXY() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(7.5,volume.getZMean(true,true), tolerance10);

    }

    @Test
    public void testGetZMeanCalibratedDistances() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(0.15,volume.getZMean(false,false), tolerance10);
        assertEquals(0.15,volume.getZMean(false,true), tolerance10);

    }


    // ANGLE BETWEEN TWO VOLUMES

    @Test
    public void testCalculateAngle2DTopRight() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(VolumeType.QUADTREE,20,20,1,dppXY,dppZ,units);
        volume1.add(0,0,0);

        Volume volume2 = new Volume(VolumeType.QUADTREE,20,20,1,dppXY,dppZ,units);
        volume2.add(10,10,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(45);

        assertEquals(expectedAngle,actualAngle, tolerance10);

    }

    @Test
    public void testCalculateAngle2DTopLeft() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(VolumeType.QUADTREE,20,11,5,dppXY,dppZ,units);
        volume1.add(5,5,0);

        Volume volume2 = new Volume(VolumeType.QUADTREE,20,11,5,dppXY,dppZ,units);
        volume2.add(0,10,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(135);

        assertEquals(expectedAngle,actualAngle, tolerance10);

    }

    @Test
    public void testCalculateAngle2DBottomLeft() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(VolumeType.QUADTREE,20,11,5,dppXY,dppZ,units);
        volume1.add(5,5,0);

        Volume volume2 = new Volume(VolumeType.QUADTREE,20,11,5,dppXY,dppZ,units);
        volume2.add(0,0,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(-135);

        assertEquals(expectedAngle,actualAngle, tolerance10);

    }

    @Test
    public void testCalculateAngle2DBottomRight() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(VolumeType.QUADTREE,20,11,5,dppXY,dppZ,units);
        volume1.add(5,5,0);

        Volume volume2 = new Volume(VolumeType.QUADTREE,20,11,5,dppXY,dppZ,units);
        volume2.add(10,0,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(-45);

        assertEquals(expectedAngle,actualAngle, tolerance10);

    }

    @Test
    public void testCalculateAngle2DPositiveXAxis() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume1.add(0,0,0);

        Volume volume2 = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume2.add(5,0,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(0);

        assertEquals(expectedAngle,actualAngle, tolerance10);

    }

    @Test
    public void testCalculateAngle2DPositiveYAxis() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume1.add(0,0,0);

        Volume volume2 = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume2.add(0,5,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(90);

        assertEquals(expectedAngle,actualAngle, tolerance10);

    }

    @Test
    public void testCalculateAngle2DNegativeXAxis() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(VolumeType.QUADTREE,20,11,5,dppXY,dppZ,units);
        volume1.add(5,5,0);

        Volume volume2 = new Volume(VolumeType.QUADTREE,20,11,5,dppXY,dppZ,units);
        volume2.add(0,5,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(180);

        assertEquals(expectedAngle,actualAngle, tolerance10);

    }

    @Test
    public void testCalculateAngle2DNegativeYAxis() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(VolumeType.QUADTREE,20,11,5,dppXY,dppZ,units);
        volume1.add(5,5,0);

        Volume volume2 = new Volume(VolumeType.QUADTREE,20,11,5,dppXY,dppZ,units);
        volume2.add(5,0,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(-90);

        assertEquals(expectedAngle,actualAngle, tolerance10);

    }


    // HEIGHT
    @Test
    public void testGetHeightPixelDistancesDoesntMatchXY() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,3);
        volume.add(13,7,1);

        assertEquals(2,volume.getHeight(true,false), tolerance10);

    }

    @Test
    public void testGetHeightPixelDistancesDoesMatchXY() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,3);
        volume.add(13,7,1);

        assertEquals(10,volume.getHeight(true,true), tolerance10);

    }

    @Test
    public void testGetHeightCalibratedDistances() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,3);
        volume.add(13,7,1);

        assertEquals(0.2,volume.getHeight(false,true), tolerance10);
        assertEquals(0.2,volume.getHeight(false,false), tolerance10);

    }


    // EXTENTS
    @Test
    public void testGetExtentsPixelDistancesDoesntMatchXY() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,3);
        volume.add(13,7,1);

        double[][] actualExtents = volume.getExtents(true,false);
        double[][] expectedExtents = new double[][]{{10,13},{5,7},{1,3}};

        assertArrayEquals(expectedExtents[0],actualExtents[0], tolerance10);
        assertArrayEquals(expectedExtents[1],actualExtents[1], tolerance10);
        assertArrayEquals(expectedExtents[2],actualExtents[2], tolerance10);

    }

    @Test
    public void testGetExtentsPixelDistancesDoesMatchXY() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,3);
        volume.add(13,7,1);

        double[][] actualExtents = volume.getExtents(true,true);
        double[][] expectedExtents = new double[][]{{10,13},{5,7},{5,15}};

        assertArrayEquals(expectedExtents[0],actualExtents[0], tolerance10);
        assertArrayEquals(expectedExtents[1],actualExtents[1], tolerance10);
        assertArrayEquals(expectedExtents[2],actualExtents[2], tolerance10);

    }

    @Test
    public void testGetExtentsCalibratedDistances() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,3);
        volume.add(13,7,1);

        double[][] expectedExtents = new double[][]{{0.2,0.26},{0.1,0.14},{0.1,0.3}};
        double[][] actualExtents = volume.getExtents(false,true);
        assertArrayEquals(expectedExtents[0],actualExtents[0], tolerance10);
        assertArrayEquals(expectedExtents[1],actualExtents[1], tolerance10);
        assertArrayEquals(expectedExtents[2],actualExtents[2], tolerance10);

        actualExtents = volume.getExtents(false,false);
        assertArrayEquals(expectedExtents[0],actualExtents[0], tolerance10);
        assertArrayEquals(expectedExtents[1],actualExtents[1], tolerance10);
        assertArrayEquals(expectedExtents[2],actualExtents[2], tolerance10);

    }


    // AREA VOLUME CHECKS

    @Test
    public void testHasVolume2NoVolume2() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);

        assertFalse(volume.hasVolume());

    }

    @Test
    public void testHasVolume22D() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,1);
        volume.add(11,5,1);
        volume.add(13,7,1);

        assertFalse(volume.hasVolume());

    }

    @Test
    public void testHasVolume23D() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertTrue(volume.hasVolume());

    }

    @Test
    public void testHasAreaNoVolume2() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);

        assertFalse(volume.hasArea());

    }

    @Test
    public void testHasArea2D() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,1);
        volume.add(11,5,1);
        volume.add(13,7,1);

        assertTrue(volume.hasArea());

    }

    @Test
    public void testHasArea3D() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertTrue(volume.hasArea());

    }


    // VOLUME

    @Test
    public void testContainsPointDoesContain() throws IntegerOverflowException {
        Volume volume = new Volume(VolumeType.QUADTREE,10,5,15,2.0,1.0,"PX");
        volume.add(1,2,3);
        volume.add(4,3,12);
        volume.add(2,1,2);
        volume.add(1,2,5);

        Point<Integer> point = new Point<>(1,2,3);

        assertTrue(volume.contains(point));

    }

    @Test
    public void testContainsPointDoesntContain() throws IntegerOverflowException {
        Volume volume = new Volume(VolumeType.QUADTREE,10,5,15,2.0,1.0,"PX");
        volume.add(1,2,3);
        volume.add(4,3,12);
        volume.add(2,1,2);
        volume.add(1,2,5);

        Point<Integer> point = new Point<>(2,2,3);

        assertFalse(volume.contains(point));

    }

    @Test
    public void testGetNVoxelsNoVolume2() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);

        assertEquals(0,volume.size());

    }

    @Test
    public void testGetNVoxelsHasVolume2() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(4,volume.size());

    }

    @Test
    public void testGetContainedVolume2PixelDistances() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(20,volume.getContainedVolume(true), tolerance10);

    }

    @Test
    public void testGetContainedVolume2CalibratedDistances() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(0.00016,volume.getContainedVolume(false), tolerance10);

    }

    @Test
    public void testGetContainedVolume2PixelDistancesFlatObject() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,6,1);
        volume.add(11,5,1);
        volume.add(13,7,1);

        assertEquals(20,volume.getContainedVolume(true), tolerance10);

    }

    @Test
    public void testGetContainedVolume2CalibratedDistancesFlatObject() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,6,1);
        volume.add(11,5,1);
        volume.add(13,7,1);

        assertEquals(0.00016,volume.getContainedVolume(false), tolerance10);

    }


    // HASHCODE TESTS

    @Test
    public void testHashCodeDifferentValue() throws IntegerOverflowException {
        Volume volume1 = new Volume(VolumeType.QUADTREE,5,4,13,1.0,1.0,"Test");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(VolumeType.QUADTREE,5,4,13,1.0,1.0,"Test");
        volume2.add(1,2,3);
        volume2.add(0,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        assertNotEquals(volume1.hashCode(),volume2.hashCode());

    }

    @Test
    public void testHashCodeDifferentPointOrder() throws IntegerOverflowException {
        // Verifying that the order of point placement doesn't matter
        Volume volume1 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"Test");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"Test");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        Volume volume3 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"Test");
        volume3.add(2,1,2);
        volume3.add(1,2,3);
        volume3.add(4,3,12);
        volume3.add(1,2,5);

        assertEquals(volume1.hashCode(),volume2.hashCode());
        assertEquals(volume1.hashCode(),volume3.hashCode());
        assertEquals(volume2.hashCode(),volume3.hashCode());

    }

    @Test
    public void testHashCodeMissingPoint() throws IntegerOverflowException {
        // Verifying that all points need to be present for equality
        Volume volume1 = new Volume(VolumeType.QUADTREE,5,4,13,1.0,1.0,"Test");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(VolumeType.QUADTREE,5,4,13,1.0,1.0,"Test");
        volume2.add(2,1,2);
        volume2.add(4,3,12);
        volume2.add(1,2,5);

        assertNotEquals(volume1.hashCode(),volume2.hashCode());

    }

    @Test
    public void testHashCodeDifferentCalibration() throws IntegerOverflowException {
        Volume volume1 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(VolumeType.QUADTREE,10,10,20,2.1,1.0,"PX");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        assertNotEquals(volume1.hashCode(),volume2.hashCode());

    }

    @Test
    public void testHashCodeDifferentUnitsCase() throws IntegerOverflowException {
        Volume volume1 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"px");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        assertEquals(volume1.hashCode(),volume2.hashCode());

    }

    @Test
    public void testHashCodeDifferentUnits() throws IntegerOverflowException {
        Volume volume1 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"um");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        assertNotEquals(volume1.hashCode(),volume2.hashCode());

    }


    // EQUALITY TESTS

    @Test
    public void testEqualsDifferentValue() throws IntegerOverflowException {
        Volume volume1 = new Volume(VolumeType.QUADTREE,5,4,13,1.0,1.0,"Test");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(VolumeType.QUADTREE,5,4,13,1.0,1.0,"Test");
        volume2.add(1,2,3);
        volume2.add(0,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        assertFalse(volume1.equals(volume2));
        assertFalse(volume2.equals(volume1));

    }

    @Test
    public void testEqualsDifferentPointOrder() throws IntegerOverflowException {// Verifying that the order of point placement doesn't matter
        Volume volume1 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        Volume volume3 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume3.add(2,1,2);
        volume3.add(1,2,3);
        volume3.add(4,3,12);
        volume3.add(1,2,5);

        assertTrue(volume1.equals(volume2));
        assertTrue(volume2.equals(volume1));
        assertTrue(volume1.equals(volume3));
        assertTrue(volume3.equals(volume1));
        assertTrue(volume2.equals(volume3));
        assertTrue(volume3.equals(volume2));

    }

    @Test
    public void testEqualsMissingPoint() throws IntegerOverflowException {
        // Verifying that all points need to be present for equality
        Volume volume1 = new Volume(VolumeType.QUADTREE,5,4,13,1.0,1.0,"Test");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(VolumeType.QUADTREE,5,4,13,1.0,1.0,"Test");
        volume2.add(2,1,2);
        volume2.add(4,3,12);
        volume2.add(1,2,5);

        assertFalse(volume1.equals(volume2));
        assertFalse(volume2.equals(volume1));

    }

    @Test
    public void testEqualsDifferentCalibration() throws IntegerOverflowException {
        Volume volume1 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(VolumeType.QUADTREE,10,10,20,2.1,1.0,"PX");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        assertFalse(volume1.equals(volume2));
        assertFalse(volume2.equals(volume1));

    }

    @Test
    public void testEqualsDifferentUnitsCase() throws IntegerOverflowException {
        Volume volume1 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        assertTrue(volume1.equals(volume2));
        assertTrue(volume2.equals(volume1));

    }

    @Test
    public void testEqualsDifferentUnits() throws IntegerOverflowException {
        Volume volume1 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"um");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        assertFalse(volume1.equals(volume2));
        assertFalse(volume2.equals(volume1));

    }


    // OVERLAP TESTS

    @Test
    public void testGetOverlappingPointsWithOverlap() throws IntegerOverflowException {
        Volume volume1 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume2.add(2,2,3);
        volume2.add(4,3,12);
        volume2.add(2,2,2);
        volume2.add(1,2,5);

        Volume actual = volume1.getOverlappingPoints(volume2);

        assertTrue(actual instanceof Volume);

        HashSet<Point<Integer>> expected = new HashSet<>();
        expected.add(new Point<>(4,3,12));
        expected.add(new Point<>(1,2,5));

        assertEquals(2,actual.size());
        for (Point<Integer> point:expected) assertTrue(actual.contains(point));


    }

    @Test
    public void testGetOverlappingPointsWithoutOverlap() throws IntegerOverflowException  {
        Volume volume1 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume2.add(2,2,3);
        volume2.add(5,3,12);
        volume2.add(2,2,2);
        volume2.add(2,2,5);

        Volume actual = volume1.getOverlappingPoints(volume2);

        assertTrue(actual instanceof Volume);

        assertEquals(0,actual.size());

    }

    @Test
    public void testGetOverlappingPointsTotalOverlap() throws IntegerOverflowException {
        Volume volume1 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        Volume actual = volume1.getOverlappingPoints(volume2);

        assertTrue(actual instanceof Volume);

        HashSet<Point<Integer>> expected = new HashSet<>();
        expected.add(new Point<>(1,2,3));
        expected.add(new Point<>(4,3,12));
        expected.add(new Point<>(2,1,2));
        expected.add(new Point<>(1,2,5));

        assertEquals(4,actual.size());
        for (Point<Integer> point:expected) assertTrue(actual.contains(point));

    }

    @Test
    public void testGetOverlappingPointsWithOverlapMoreIn1() throws IntegerOverflowException {
        Volume volume1 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);
        volume1.add(4,5,5);

        Volume volume2 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume2.add(2,2,3);
        volume2.add(4,3,12);
        volume2.add(2,2,2);
        volume2.add(1,2,5);

        Volume actual = volume1.getOverlappingPoints(volume2);

        assertTrue(actual instanceof Volume);

        HashSet<Point<Integer>> expected = new HashSet<>();
        expected.add(new Point<>(4,3,12));
        expected.add(new Point<>(1,2,5));

        assertEquals(2,actual.size());
        for (Point<Integer> point:expected) assertTrue(actual.contains(point));

    }

    @Test
    public void testGetOverlappingPointsWithOverlapMoreIn2() throws IntegerOverflowException {
        Volume volume1 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume2.add(2,2,3);
        volume2.add(4,3,12);
        volume2.add(2,2,2);
        volume2.add(1,2,5);
        volume2.add(4,5,5);

        Volume actual = volume1.getOverlappingPoints(volume2);

        assertTrue(actual instanceof Volume);

        HashSet<Point<Integer>> expected = new HashSet<>();
        expected.add(new Point<>(4,3,12));
        expected.add(new Point<>(1,2,5));

        assertEquals(2,actual.size());
        for (Point<Integer> point:expected) assertTrue(actual.contains(point));

    }

    @Test
    public void testGetOverlapWithOverlap() throws IntegerOverflowException {
        Volume volume1 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume2.add(2,2,3);
        volume2.add(4,3,12);
        volume2.add(2,2,2);
        volume2.add(1,2,5);

        int actual = volume1.getOverlap(volume2);
        int expected = 2;

        assertEquals(expected,actual);

    }

    @Test
    public void testGetOverlapWithoutOverlap() throws IntegerOverflowException {
        Volume volume1 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume2.add(2,2,3);
        volume2.add(5,3,12);
        volume2.add(2,2,2);
        volume2.add(2,2,5);

        int actual = volume1.getOverlap(volume2);
        int expected = 0;

        assertEquals(expected,actual);

    }

    @Test
    public void testGetOverlapTotalOverlap() throws IntegerOverflowException {
        Volume volume1 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        int actual = volume1.getOverlap(volume2);
        int expected = 4;

        assertEquals(expected,actual);

    }

    @Test
    public void testGetOverlapTotalOverlapMoreIn1() throws IntegerOverflowException {
        Volume volume1 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);
        volume1.add(4,5,5);

        Volume volume2 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        int actual = volume1.getOverlap(volume2);
        int expected = 4;

        assertEquals(expected,actual);

    }

    @Test
    public void testGetOverlapTotalOverlapMoreIn2() throws IntegerOverflowException {
        Volume volume1 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(VolumeType.QUADTREE,10,10,20,2.0,1.0,"PX");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);
        volume2.add(4,5,5);

        int actual = volume1.getOverlap(volume2);
        int expected = 4;

        assertEquals(expected,actual);

    }


    // MISCELLANEOUS METHODS

    @Test
    public void testClearSurface() throws IntegerOverflowException {
        Volume volume = new Volume(VolumeType.QUADTREE,10,7,15,2.0,1.0,"PX");
        volume.add(1,2,3);
        volume.add(4,3,12);
        volume.add(2,1,2);
        volume.add(1,2,5);
        volume.add(1,2,8);
        volume.add(1,4,8);
        volume.add(2,4,8);
        volume.add(3,4,8);
        volume.add(2,4,2);
        volume.add(2,6,9);

        assertNotNull(volume.getSurface());

        volume.clearSurface();
        assertNull(volume.surface);

    }

    @Test
    public void testClearPoints() throws IntegerOverflowException {
        Volume volume = new Volume(VolumeType.QUADTREE,10,7,15,2.0,1.0,"PX");
        volume.add(1,2,3);
        volume.add(4,3,12);
        volume.add(2,1,2);
        volume.add(1,2,5);
        volume.add(1,2,8);
        volume.add(1,4,8);
        volume.add(2,4,8);
        volume.add(3,4,8);
        volume.add(2,4,2);
        volume.add(2,6,9);

        volume.clearPoints();
        assertEquals(0,volume.size());
    }


    // MISSING TESTS

    @Test
    public void testGetSurface() throws IntegerOverflowException {
        Volume volume = new Volume(VolumeType.QUADTREE,10,10,8,2.0,1.0,"PX");
        volume.add(5,7,3);
        volume.add(5,8,3);
        volume.add(5,9,3);
        volume.add(6,7,3);
        volume.add(6,8,3);
        volume.add(6,9,3);
        volume.add(7,7,3);
        volume.add(7,8,3);
        volume.add(7,9,3);
        volume.add(5,7,4);
        volume.add(5,8,4);
        volume.add(5,9,4);
        volume.add(6,7,4);
        volume.add(6,8,4);
        volume.add(6,9,4);
        volume.add(7,7,4);
        volume.add(7,8,4);
        volume.add(7,9,4);
        volume.add(5,7,5);
        volume.add(5,8,5);
        volume.add(5,9,5);
        volume.add(6,7,5);
        volume.add(6,8,5);
        volume.add(6,9,5);
        volume.add(7,7,5);
        volume.add(7,8,5);
        volume.add(7,9,5);

        Volume surface = volume.getSurface();

        assertEquals(26,surface.size());
        assertFalse(surface.contains(new Point<>(6,8,4)));
        assertTrue(surface.contains(new Point<>(5,7,3)));
        assertTrue(surface.contains(new Point<>(7,7,4)));
        assertTrue(surface.contains(new Point<>(5,8,5)));
        assertTrue(surface.contains(new Point<>(7,9,5)));

    }

    @Test @Ignore
    public void testGetXSurfaceCoords() {

    }

    @Test @Ignore
    public void testGetYSurfaceCoords() {

    }

    @Test @Ignore
    public void testGetZSufaceCoords() {

    }

    @Test @Ignore
    public void testGetXSurfacePixelDistances() {

    }

    @Test @Ignore
    public void testGetYSurfacePixelDistances() {

    }

    @Test @Ignore
    public void testGetZSurfacePixelDistances() {

    }

    @Test @Ignore
    public void testGetXSurfaceCalibratedDistances() {

    }

    @Test @Ignore
    public void testGetYSurfaceCalibratedDistances() {

    }

    @Test @Ignore
    public void testGetZSurfaceCalibratedDistances() {

    }

    @Test
    public void testGetProjectedAreaPixelDistances() throws IntegerOverflowException {
        Volume volume = new Volume(VolumeType.QUADTREE,10,7,15,2.0,1.0,"PX");
        volume.add(1,2,3);
        volume.add(4,3,12);
        volume.add(2,1,2);
        volume.add(1,2,5);
        volume.add(1,2,8);
        volume.add(1,4,8);
        volume.add(2,4,8);
        volume.add(3,4,8);
        volume.add(2,4,2);
        volume.add(2,6,9);

        double actual = volume.getProjectedArea(true);

        assertEquals(7,actual,tolerance10);

    }

    @Test
    public void testGetProjectedAreaCalibratedDistances() throws IntegerOverflowException {
        Volume volume = new Volume(VolumeType.QUADTREE,10,7,15,2.0,1.0,"PX");
        volume.add(1,2,3);
        volume.add(4,3,12);
        volume.add(2,1,2);
        volume.add(1,2,5);
        volume.add(1,2,8);
        volume.add(1,4,8);
        volume.add(2,4,8);
        volume.add(3,4,8);
        volume.add(2,4,2);
        volume.add(2,6,9);

        double actual = volume.getProjectedArea(false);

        assertEquals(28,actual,tolerance10);

    }

    @Test
    public void testGetCentroidSeparation2DPixelDistances() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(VolumeType.QUADTREE,20,20,1,dppXY,dppZ,units);
        volume1.add(10,5,0);
        volume1.add(11,5,0);
        volume1.add(11,6,0);
        volume1.add(12,6,0);

        Volume volume2 = new Volume(VolumeType.QUADTREE,20,20,1,dppXY,dppZ,units);
        volume2.add(1,2,0);
        volume2.add(1,3,0);
        volume2.add(2,1,0);
        volume2.add(2,2,0);
        volume2.add(3,2,0);

        double expected = 9.8433;
        double actual = volume1.getCentroidSeparation(volume2,true);

        assertEquals(expected,actual, tolerance2);

    }

    @Test
    public void testGetCentroidSeparation2DCalibratedDistances() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(VolumeType.QUADTREE,20,20,1,dppXY,dppZ,units);
        volume1.add(10,5,0);
        volume1.add(11,5,0);
        volume1.add(11,6,0);
        volume1.add(12,6,0);

        Volume volume2 = new Volume(VolumeType.QUADTREE,20,20,1,dppXY,dppZ,units);
        volume2.add(1,2,0);
        volume2.add(1,3,0);
        volume2.add(2,1,0);
        volume2.add(2,2,0);
        volume2.add(3,2,0);

        double expected = 0.1878;
        double actual = volume1.getCentroidSeparation(volume2,false);

        assertEquals(expected,actual, tolerance2);

    }

    @Test
    public void testGetCentroidSeparation3DPixelDistances() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume1.add(10,5,0);
        volume1.add(11,5,0);
        volume1.add(11,6,0);
        volume1.add(12,6,0);
        volume1.add(10,5,1);
        volume1.add(11,5,1);
        volume1.add(12,6,1);

        Volume volume2 = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume2.add(1,2,0);
        volume2.add(1,3,0);
        volume2.add(2,1,0);
        volume2.add(2,2,0);
        volume2.add(3,2,0);
        volume2.add(1,3,1);
        volume2.add(2,1,1);
        volume2.add(2,2,1);
        volume2.add(2,1,2);

        double expected = 9.8986;
        double actual = volume1.getCentroidSeparation(volume2,true);

        assertEquals(expected,actual, tolerance2);

    }

    @Test
    public void testGetCentroidSeparation3DCalibratedDistances() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume1.add(10,5,0);
        volume1.add(11,5,0);
        volume1.add(11,6,0);
        volume1.add(12,6,0);
        volume1.add(10,5,1);
        volume1.add(11,5,1);
        volume1.add(12,6,1);

        Volume volume2 = new Volume(VolumeType.QUADTREE,20,10,5,dppXY,dppZ,units);
        volume2.add(1,2,0);
        volume2.add(1,3,0);
        volume2.add(2,1,0);
        volume2.add(2,2,0);
        volume2.add(3,2,0);
        volume2.add(1,3,1);
        volume2.add(2,1,1);
        volume2.add(2,2,1);
        volume2.add(2,1,2);

        double expected = 0.1980;
        double actual = volume1.getCentroidSeparation(volume2,false);

        assertEquals(expected,actual, tolerance2);

    }
}