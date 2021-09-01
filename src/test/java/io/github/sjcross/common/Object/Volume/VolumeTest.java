package io.github.sjcross.common.Object.Volume;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import io.github.sjcross.common.Exceptions.IntegerOverflowException;
import io.github.sjcross.common.Object.Point;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class VolumeTest {
    private final double tolerance10 = 1E-10;
    private final double tolerance2 = 1E-2;


    // ADDING COORDINATES

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testAddCoordAlreadyExists(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,10,10,1,dppXY,dppZ,units);
        volume.add(0,0,0);
        volume.add(0,0,0);
        volume.add(1,0,0);

        assertEquals(2,volume.size());

    }


    // COORDINATE TESTS

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXCoords(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume = new Volume(volumeType,10,10,13,2.0,1.0,"PX");
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

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetYCoords(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume = new Volume(volumeType,10,10,13,2.0,1.0,"PX");
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

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetZCoords(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume = new Volume(volumeType,10,10,13,2.0,1.0,"PX");
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

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXNoVolume2(VolumeType volumeType) {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,10,10,13,dppXY,dppZ,units);

        double[] actualX = volume.getX(true);
        double[] expectedX = new double[]{};

        assertArrayEquals(expectedX,actualX, tolerance10);
        assertEquals(0,volume.size());

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXPixelDistances(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        double[] actualX = volume.getX(true);
        double[] expectedX = new double[]{10,10,11,13};

        assertArrayEquals(expectedX,actualX, tolerance10);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXCalibratedDistances(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        double[] actualX = volume.getX(false);
        double[] expectedX = new double[]{0.2,0.2,0.22,0.26};

        assertArrayEquals(expectedX,actualX, tolerance10);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetYPixelDistances(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        double[] actualY = volume.getY(true);
        double[] expectedY = new double[]{5,5,5,7};

        assertArrayEquals(expectedY,actualY, tolerance10);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetYCalibratedDistances(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        double[] actualY = volume.getY(false);
        double[] expectedY = new double[]{0.1,0.1,0.1,0.14};

        assertArrayEquals(expectedY,actualY, tolerance10);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetZPixelDistancesDoesntMatchXY(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        double[] actualZ = volume.getZ(true,false);
        double[] expectedZ = new double[]{1,2,2,1};

        assertArrayEquals(expectedZ,actualZ, tolerance10);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetZPixelDistancesDoesMatchXY(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        double[] actualZ = volume.getZ(true,true);
        double[] expectedZ = new double[]{5,10,10,5};

        assertArrayEquals(expectedZ,actualZ, tolerance10);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetZCalibratedDistances(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
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

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXYScaledZ(VolumeType volumeType) {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);

        double actual = volume.getXYScaledZ(1);
        double expected = 5d;

        assertEquals(expected,actual, tolerance10);

    }


    // MEAN POSITION

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXMeanPixelDistances(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(11,volume.getXMean(true), tolerance10);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXMeanCalibratedDistances(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(0.22,volume.getXMean(false), tolerance10);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetYMeanPixelDistances(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(5.5,volume.getYMean(true), tolerance10);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetYMeanCalibratedDistances(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(0.11,volume.getYMean(false), tolerance10);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetZMeanPixelDistancesDoesntMatchXY(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(1.5,volume.getZMean(true,false), tolerance10);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetZMeanPixelDistancesDoesMatchXY(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(7.5,volume.getZMean(true,true), tolerance10);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetZMeanCalibratedDistances(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(0.15,volume.getZMean(false,false), tolerance10);
        assertEquals(0.15,volume.getZMean(false,true), tolerance10);

    }


    // ANGLE BETWEEN TWO VOLUMES

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testCalculateAngle2DTopRight(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(volumeType,20,20,1,dppXY,dppZ,units);
        volume1.add(0,0,0);

        Volume volume2 = new Volume(volumeType,20,20,1,dppXY,dppZ,units);
        volume2.add(10,10,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(45);

        assertEquals(expectedAngle,actualAngle, tolerance10);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testCalculateAngle2DTopLeft(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(volumeType,20,11,5,dppXY,dppZ,units);
        volume1.add(5,5,0);

        Volume volume2 = new Volume(volumeType,20,11,5,dppXY,dppZ,units);
        volume2.add(0,10,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(135);

        assertEquals(expectedAngle,actualAngle, tolerance10);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testCalculateAngle2DBottomLeft(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(volumeType,20,11,5,dppXY,dppZ,units);
        volume1.add(5,5,0);

        Volume volume2 = new Volume(volumeType,20,11,5,dppXY,dppZ,units);
        volume2.add(0,0,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(-135);

        assertEquals(expectedAngle,actualAngle, tolerance10);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testCalculateAngle2DBottomRight(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(volumeType,20,11,5,dppXY,dppZ,units);
        volume1.add(5,5,0);

        Volume volume2 = new Volume(volumeType,20,11,5,dppXY,dppZ,units);
        volume2.add(10,0,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(-45);

        assertEquals(expectedAngle,actualAngle, tolerance10);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testCalculateAngle2DPositiveXAxis(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume1.add(0,0,0);

        Volume volume2 = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume2.add(5,0,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(0);

        assertEquals(expectedAngle,actualAngle, tolerance10);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testCalculateAngle2DPositiveYAxis(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume1.add(0,0,0);

        Volume volume2 = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume2.add(0,5,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(90);

        assertEquals(expectedAngle,actualAngle, tolerance10);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testCalculateAngle2DNegativeXAxis(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(volumeType,20,11,5,dppXY,dppZ,units);
        volume1.add(5,5,0);

        Volume volume2 = new Volume(volumeType,20,11,5,dppXY,dppZ,units);
        volume2.add(0,5,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(180);

        assertEquals(expectedAngle,actualAngle, tolerance10);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testCalculateAngle2DNegativeYAxis(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(volumeType,20,11,5,dppXY,dppZ,units);
        volume1.add(5,5,0);

        Volume volume2 = new Volume(volumeType,20,11,5,dppXY,dppZ,units);
        volume2.add(5,0,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(-90);

        assertEquals(expectedAngle,actualAngle, tolerance10);

    }


    // HEIGHT
    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetHeightPixelDistancesDoesntMatchXY(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,3);
        volume.add(13,7,1);

        assertEquals(2,volume.getHeight(true,false), tolerance10);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetHeightPixelDistancesDoesMatchXY(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,3);
        volume.add(13,7,1);

        assertEquals(10,volume.getHeight(true,true), tolerance10);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetHeightCalibratedDistances(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,3);
        volume.add(13,7,1);

        assertEquals(0.2,volume.getHeight(false,true), tolerance10);
        assertEquals(0.2,volume.getHeight(false,false), tolerance10);

    }


    // EXTENTS
    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetExtentsPixelDistancesDoesntMatchXY(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
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

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetExtentsPixelDistancesDoesMatchXY(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
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

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetExtentsCalibratedDistances(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
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

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testHasVolume2NoVolume2(VolumeType volumeType) {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);

        assertFalse(volume.hasVolume());

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testHasVolume22D(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,1);
        volume.add(11,5,1);
        volume.add(13,7,1);

        assertFalse(volume.hasVolume());

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testHasVolume23D(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertTrue(volume.hasVolume());

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testHasAreaNoVolume2(VolumeType volumeType) {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);

        assertFalse(volume.hasArea());

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testHasArea2D(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,1);
        volume.add(11,5,1);
        volume.add(13,7,1);

        assertTrue(volume.hasArea());

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testHasArea3D(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertTrue(volume.hasArea());

    }


    // VOLUME

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testContainsPointDoesContain(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume = new Volume(volumeType,10,5,15,2.0,1.0,"PX");
        volume.add(1,2,3);
        volume.add(4,3,12);
        volume.add(2,1,2);
        volume.add(1,2,5);

        Point<Integer> point = new Point<>(1,2,3);

        assertTrue(volume.contains(point));

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testContainsPointDoesntContain(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume = new Volume(volumeType,10,5,15,2.0,1.0,"PX");
        volume.add(1,2,3);
        volume.add(4,3,12);
        volume.add(2,1,2);
        volume.add(1,2,5);

        Point<Integer> point = new Point<>(2,2,3);

        assertFalse(volume.contains(point));

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetNVoxelsNoVolume2(VolumeType volumeType) {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);

        assertEquals(0,volume.size());

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetNVoxelsHasVolume2(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(4,volume.size());

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetContainedVolume2PixelDistances(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(20,volume.getContainedVolume(true), tolerance10);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetContainedVolume2CalibratedDistances(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(0.00016,volume.getContainedVolume(false), tolerance10);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetContainedVolume2PixelDistancesFlatObject(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,6,1);
        volume.add(11,5,1);
        volume.add(13,7,1);

        assertEquals(20,volume.getContainedVolume(true), tolerance10);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetContainedVolume2CalibratedDistancesFlatObject(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,6,1);
        volume.add(11,5,1);
        volume.add(13,7,1);

        assertEquals(0.00016,volume.getContainedVolume(false), tolerance10);

    }


    // HASHCODE TESTS

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testHashCodeDifferentValue(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume1 = new Volume(volumeType,5,4,13,1.0,1.0,"Test");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(volumeType,5,4,13,1.0,1.0,"Test");
        volume2.add(1,2,3);
        volume2.add(0,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        assertNotEquals(volume1.hashCode(),volume2.hashCode());

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testHashCodeDifferentPointOrder(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        // Verifying that the order of point placement doesn't matter
        Volume volume1 = new Volume(volumeType,10,10,20,2.0,1.0,"Test");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(volumeType,10,10,20,2.0,1.0,"Test");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        Volume volume3 = new Volume(volumeType,10,10,20,2.0,1.0,"Test");
        volume3.add(2,1,2);
        volume3.add(1,2,3);
        volume3.add(4,3,12);
        volume3.add(1,2,5);

        assertEquals(volume1.hashCode(),volume2.hashCode());
        assertEquals(volume1.hashCode(),volume3.hashCode());
        assertEquals(volume2.hashCode(),volume3.hashCode());

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testHashCodeMissingPoint(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        // Verifying that all points need to be present for equality
        Volume volume1 = new Volume(volumeType,5,4,13,1.0,1.0,"Test");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(volumeType,5,4,13,1.0,1.0,"Test");
        volume2.add(2,1,2);
        volume2.add(4,3,12);
        volume2.add(1,2,5);

        assertNotEquals(volume1.hashCode(),volume2.hashCode());

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testHashCodeDifferentCalibration(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume1 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(volumeType,10,10,20,2.1,1.0,"PX");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        assertNotEquals(volume1.hashCode(),volume2.hashCode());

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testHashCodeDifferentUnitsCase(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume1 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(volumeType,10,10,20,2.0,1.0,"px");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        assertEquals(volume1.hashCode(),volume2.hashCode());

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testHashCodeDifferentUnits(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume1 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(volumeType,10,10,20,2.0,1.0,"um");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        assertNotEquals(volume1.hashCode(),volume2.hashCode());

    }


    // EQUALITY TESTS

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testEqualsDifferentValue(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume1 = new Volume(volumeType,5,4,13,1.0,1.0,"Test");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(volumeType,5,4,13,1.0,1.0,"Test");
        volume2.add(1,2,3);
        volume2.add(0,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        assertFalse(volume1.equals(volume2));
        assertFalse(volume2.equals(volume1));

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testEqualsDifferentPointOrder(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {// Verifying that the order of point placement doesn't matter
        Volume volume1 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        Volume volume3 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
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

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testEqualsMissingPoint(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        // Verifying that all points need to be present for equality
        Volume volume1 = new Volume(volumeType,5,4,13,1.0,1.0,"Test");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(volumeType,5,4,13,1.0,1.0,"Test");
        volume2.add(2,1,2);
        volume2.add(4,3,12);
        volume2.add(1,2,5);

        assertFalse(volume1.equals(volume2));
        assertFalse(volume2.equals(volume1));

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testEqualsDifferentCalibration(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume1 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(volumeType,10,10,20,2.1,1.0,"PX");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        assertFalse(volume1.equals(volume2));
        assertFalse(volume2.equals(volume1));

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testEqualsDifferentUnitsCase(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume1 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(volumeType,10,10,20,2.0,1.0,"px");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        assertTrue(volume1.equals(volume2));
        assertTrue(volume2.equals(volume1));

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testEqualsDifferentUnits(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume1 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(volumeType,10,10,20,2.0,1.0,"um");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        assertFalse(volume1.equals(volume2));
        assertFalse(volume2.equals(volume1));

    }


    // OVERLAP TESTS

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetOverlappingPointsWithOverlap(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume1 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
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

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetOverlappingPointsWithoutOverlap(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException  {
        Volume volume1 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
        volume2.add(2,2,3);
        volume2.add(5,3,12);
        volume2.add(2,2,2);
        volume2.add(2,2,5);

        Volume actual = volume1.getOverlappingPoints(volume2);

        assertTrue(actual instanceof Volume);

        assertEquals(0,actual.size());

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetOverlappingPointsTotalOverlap(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume1 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
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

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetOverlappingPointsWithOverlapMoreIn1(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume1 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);
        volume1.add(4,5,5);

        Volume volume2 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
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

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetOverlappingPointsWithOverlapMoreIn2(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume1 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
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

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetOverlapWithOverlap(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume1 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
        volume2.add(2,2,3);
        volume2.add(4,3,12);
        volume2.add(2,2,2);
        volume2.add(1,2,5);

        int actual = volume1.getOverlap(volume2);
        int expected = 2;

        assertEquals(expected,actual);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetOverlapWithoutOverlap(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume1 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
        volume2.add(2,2,3);
        volume2.add(5,3,12);
        volume2.add(2,2,2);
        volume2.add(2,2,5);

        int actual = volume1.getOverlap(volume2);
        int expected = 0;

        assertEquals(expected,actual);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetOverlapTotalOverlap(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume1 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        int actual = volume1.getOverlap(volume2);
        int expected = 4;

        assertEquals(expected,actual);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetOverlapTotalOverlapMoreIn1(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume1 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);
        volume1.add(4,5,5);

        Volume volume2 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        int actual = volume1.getOverlap(volume2);
        int expected = 4;

        assertEquals(expected,actual);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetOverlapTotalOverlapMoreIn2(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume1 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        Volume volume2 = new Volume(volumeType,10,10,20,2.0,1.0,"PX");
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

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testClearSurface(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume = new Volume(volumeType,10,7,15,2.0,1.0,"PX");
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

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testClearPoints(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume = new Volume(volumeType,10,7,15,2.0,1.0,"PX");
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

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetSurface(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume = new Volume(volumeType,10,10,8,2.0,1.0,"PX");
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

    @Disabled
    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXSurfaceCoords() {

    }

    @Disabled
    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetYSurfaceCoords() {

    }

    @Disabled
    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetZSufaceCoords() {

    }

    @Disabled
    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXSurfacePixelDistances() {

    }

    @Disabled
    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetYSurfacePixelDistances() {

    }

    @Disabled
    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetZSurfacePixelDistances() {

    }

    @Disabled
    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetXSurfaceCalibratedDistances() {

    }

    @Disabled
    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetYSurfaceCalibratedDistances() {

    }

    @Disabled
    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetZSurfaceCalibratedDistances() {

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetProjectedAreaPixelDistances(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume = new Volume(volumeType,10,7,15,2.0,1.0,"PX");
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

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetProjectedAreaCalibratedDistances(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        Volume volume = new Volume(volumeType,10,7,15,2.0,1.0,"PX");
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

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetCentroidSeparation2DPixelDistances(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(volumeType,20,20,1,dppXY,dppZ,units);
        volume1.add(10,5,0);
        volume1.add(11,5,0);
        volume1.add(11,6,0);
        volume1.add(12,6,0);

        Volume volume2 = new Volume(volumeType,20,20,1,dppXY,dppZ,units);
        volume2.add(1,2,0);
        volume2.add(1,3,0);
        volume2.add(2,1,0);
        volume2.add(2,2,0);
        volume2.add(3,2,0);

        double expected = 9.8433;
        double actual = volume1.getCentroidSeparation(volume2,true);

        assertEquals(expected,actual, tolerance2);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetCentroidSeparation2DCalibratedDistances(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(volumeType,20,20,1,dppXY,dppZ,units);
        volume1.add(10,5,0);
        volume1.add(11,5,0);
        volume1.add(11,6,0);
        volume1.add(12,6,0);

        Volume volume2 = new Volume(volumeType,20,20,1,dppXY,dppZ,units);
        volume2.add(1,2,0);
        volume2.add(1,3,0);
        volume2.add(2,1,0);
        volume2.add(2,2,0);
        volume2.add(3,2,0);

        double expected = 0.1878;
        double actual = volume1.getCentroidSeparation(volume2,false);

        assertEquals(expected,actual, tolerance2);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetCentroidSeparation3DPixelDistances(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume1.add(10,5,0);
        volume1.add(11,5,0);
        volume1.add(11,6,0);
        volume1.add(12,6,0);
        volume1.add(10,5,1);
        volume1.add(11,5,1);
        volume1.add(12,6,1);

        Volume volume2 = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
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

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetCentroidSeparation3DCalibratedDistances(VolumeType volumeType) throws IntegerOverflowException, PointOutOfRangeException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
        volume1.add(10,5,0);
        volume1.add(11,5,0);
        volume1.add(11,6,0);
        volume1.add(12,6,0);
        volume1.add(10,5,1);
        volume1.add(11,5,1);
        volume1.add(12,6,1);

        Volume volume2 = new Volume(volumeType,20,10,5,dppXY,dppZ,units);
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