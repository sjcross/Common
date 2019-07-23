package wbif.sjx.common.Object.Volume2;

import org.junit.Ignore;
import org.junit.Test;
import wbif.sjx.common.Exceptions.IntegerOverflowException;
import wbif.sjx.common.Object.Point;

import java.util.*;

import static org.junit.Assert.*;

public class PointVolumeTest {
    private double tolerance10 = 1E-10;
    private double tolerance2 = 1E-2;


    // ADDING COORDINATES

    @Test
    public void testAddCoordAlreadyExists() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(10,10,1,dppXY,dppZ,units);
        volume.add(0,0,0);
        volume.add(0,0,0);
        volume.add(1,0,0);

        assertEquals(2,volume.getPoints().size());

    }


    // COORDINATE TESTS

    @Test
    public void testGetXCoords() throws IntegerOverflowException {
        PointVolume volume = new PointVolume(10,10,13,2.0,1.0,"PX");
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
        PointVolume volume = new PointVolume(10,10,13,2.0,1.0,"PX");
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
        PointVolume volume = new PointVolume(10,10,13,2.0,1.0,"PX");
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
    public void testGetXNoPointVolume() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(10,10,13,dppXY,dppZ,units);

        double[] actualX = volume.getX(true);
        double[] expectedX = new double[]{};

        assertArrayEquals(expectedX,actualX, tolerance10);
        assertEquals(0,volume.getPoints().size());

    }

    @Test
    public void testGetXPixelDistances() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
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

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
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

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
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

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
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

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
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

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
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

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
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

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);

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

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
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

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
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

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
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

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
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

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
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

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
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

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(0.15,volume.getZMean(false,false), tolerance10);
        assertEquals(0.15,volume.getZMean(false,true), tolerance10);

    }


    // MEDIAN POSITION

    @Test
    public void testGetXMedianPixelDistancesEvenN() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(10.5,volume.getXMedian(true), tolerance10);
    }

    @Test
    public void testGetXMedianCalibratedDistancesEvenN() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(0.21,volume.getXMedian(false), tolerance10);

    }

    @Test
    public void testGetYMedianPixelDistancesEvenN() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(5,volume.getYMedian(true), tolerance10);
    }

    @Test
    public void testGetYMedianCalibratedDistancesEvenN() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(0.1,volume.getYMedian(false), tolerance10);

    }

    @Test
    public void testGetZMedianPixelDistancesDoesntMatchXYEvenN() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(1.5,volume.getZMedian(true,false), tolerance10);

    }

    @Test
    public void testGetZMedianPixelDistancesDoesMatchXYEvenN() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(7.5,volume.getZMedian(true,true), tolerance10);

    }

    @Test
    public void testGetZMedianCalibratedDistancesEvenN() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(0.15,volume.getZMedian(false,false), tolerance10);
        assertEquals(0.15,volume.getZMedian(false,true), tolerance10);

    }

    @Test
    public void testGetXMedianPixelDistancesOddN() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);
        volume.add(13,7,2);

        assertEquals(11,volume.getXMedian(true), tolerance10);
    }

    @Test
    public void testGetXMedianCalibratedDistancesOddN() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);
        volume.add(13,7,2);

        assertEquals(0.22,volume.getXMedian(false), tolerance10);

    }

    @Test
    public void testGetYMedianPixelDistancesOddN() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);
        volume.add(13,7,2);

        assertEquals(5,volume.getYMedian(true), tolerance10);
    }

    @Test
    public void testGetYMedianCalibratedDistancesOddN() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);
        volume.add(13,7,2);

        assertEquals(0.1,volume.getYMedian(false), tolerance10);

    }

    @Test
    public void testGetZMedianPixelDistancesDoesntMatchXYOddN() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);
        volume.add(13,7,2);

        assertEquals(2,volume.getZMedian(true,false), tolerance10);

    }

    @Test
    public void testGetZMedianPixelDistancesDoesMatchXYOddN() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);
        volume.add(13,7,2);

        assertEquals(10,volume.getZMedian(true,true), tolerance10);

    }

    @Test
    public void testGetZMedianCalibratedDistancesOddN() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);
        volume.add(13,7,2);

        assertEquals(0.2,volume.getZMedian(false,false), tolerance10);
        assertEquals(0.2,volume.getZMedian(false,true), tolerance10);

    }


    // ANGLE BETWEEN TWO VOLUMES

    @Test
    public void testCalculateAngle2DTopRight() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume1 = new PointVolume(20,20,1,dppXY,dppZ,units);
        volume1.add(0,0,0);

        PointVolume volume2 = new PointVolume(20,20,1,dppXY,dppZ,units);
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

        PointVolume volume1 = new PointVolume(20,11,5,dppXY,dppZ,units);
        volume1.add(5,5,0);

        PointVolume volume2 = new PointVolume(20,11,5,dppXY,dppZ,units);
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

        PointVolume volume1 = new PointVolume(20,11,5,dppXY,dppZ,units);
        volume1.add(5,5,0);

        PointVolume volume2 = new PointVolume(20,11,5,dppXY,dppZ,units);
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

        PointVolume volume1 = new PointVolume(20,11,5,dppXY,dppZ,units);
        volume1.add(5,5,0);

        PointVolume volume2 = new PointVolume(20,11,5,dppXY,dppZ,units);
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

        PointVolume volume1 = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume1.add(0,0,0);

        PointVolume volume2 = new PointVolume(20,10,5,dppXY,dppZ,units);
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

        PointVolume volume1 = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume1.add(0,0,0);

        PointVolume volume2 = new PointVolume(20,10,5,dppXY,dppZ,units);
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

        PointVolume volume1 = new PointVolume(20,11,5,dppXY,dppZ,units);
        volume1.add(5,5,0);

        PointVolume volume2 = new PointVolume(20,11,5,dppXY,dppZ,units);
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

        PointVolume volume1 = new PointVolume(20,11,5,dppXY,dppZ,units);
        volume1.add(5,5,0);

        PointVolume volume2 = new PointVolume(20,11,5,dppXY,dppZ,units);
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

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
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

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
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

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
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

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
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

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
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

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
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
    public void testHasPointVolumeNoPointVolume() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);

        assertFalse(volume.hasVolume());

    }

    @Test
    public void testHasPointVolume2D() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,1);
        volume.add(11,5,1);
        volume.add(13,7,1);

        assertFalse(volume.hasVolume());

    }

    @Test
    public void testHasPointVolume3D() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertTrue(volume.hasVolume());

    }

    @Test
    public void testHasAreaNoPointVolume() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);

        assertFalse(volume.hasArea());

    }

    @Test
    public void testHasArea2D() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
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

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertTrue(volume.hasArea());

    }


    // VOLUME

    @Test
    public void testContainsPointDoesContain() throws IntegerOverflowException {
        PointVolume volume = new PointVolume(10,5,15,2.0,1.0,"PX");
        volume.add(1,2,3);
        volume.add(4,3,12);
        volume.add(2,1,2);
        volume.add(1,2,5);

        Point<Integer> point = new Point<>(1,2,3);

        assertTrue(volume.getPoints().contains(point));

    }

    @Test
    public void testContainsPointDoesntContain() throws IntegerOverflowException {
        PointVolume volume = new PointVolume(10,5,15,2.0,1.0,"PX");
        volume.add(1,2,3);
        volume.add(4,3,12);
        volume.add(2,1,2);
        volume.add(1,2,5);

        Point<Integer> point = new Point<>(2,2,3);

        assertFalse(volume.getPoints().contains(point));

    }

    @Test
    public void testGetNVoxelsNoPointVolume() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);

        assertEquals(0,volume.getNVoxels());

    }

    @Test
    public void testGetNVoxelsHasPointVolume() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(4,volume.getNVoxels());

    }

    @Test
    public void testGetContainedPointVolumePixelDistances() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(20,volume.getContainedVolume(true), tolerance10);

    }

    @Test
    public void testGetContainedPointVolumeCalibratedDistances() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,5,2);
        volume.add(11,5,2);
        volume.add(13,7,1);

        assertEquals(0.00016,volume.getContainedVolume(false), tolerance10);

    }

    @Test
    public void testGetContainedPointVolumePixelDistancesFlatObject() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,6,1);
        volume.add(11,5,1);
        volume.add(13,7,1);

        assertEquals(20,volume.getContainedVolume(true), tolerance10);

    }

    @Test
    public void testGetContainedPointVolumeCalibratedDistancesFlatObject() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume.add(10,5,1);
        volume.add(10,6,1);
        volume.add(11,5,1);
        volume.add(13,7,1);

        assertEquals(0.00016,volume.getContainedVolume(false), tolerance10);

    }


    // HASHCODE TESTS

    @Test
    public void testHashCodeDifferentValue() throws IntegerOverflowException {
        PointVolume volume1 = new PointVolume(5,4,13,1.0,1.0,"Test");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        PointVolume volume2 = new PointVolume(5,4,13,1.0,1.0,"Test");
        volume2.add(1,2,3);
        volume2.add(0,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        assertNotEquals(volume1.hashCode(),volume2.hashCode());

    }

    @Test
    public void testHashCodeDifferentPointOrder() throws IntegerOverflowException {
        // Verifying that the order of point placement doesn't matter
        PointVolume volume1 = new PointVolume(10,10,20,2.0,1.0,"Test");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        PointVolume volume2 = new PointVolume(10,10,20,2.0,1.0,"Test");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        PointVolume volume3 = new PointVolume(10,10,20,2.0,1.0,"Test");
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
        PointVolume volume1 = new PointVolume(5,4,13,1.0,1.0,"Test");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        PointVolume volume2 = new PointVolume(5,4,13,1.0,1.0,"Test");
        volume2.add(2,1,2);
        volume2.add(4,3,12);
        volume2.add(1,2,5);

        assertNotEquals(volume1.hashCode(),volume2.hashCode());

    }

    @Test
    public void testHashCodeDifferentCalibration() throws IntegerOverflowException {
        PointVolume volume1 = new PointVolume(10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        PointVolume volume2 = new PointVolume(10,10,20,2.1,1.0,"PX");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        assertNotEquals(volume1.hashCode(),volume2.hashCode());

    }

    @Test
    public void testHashCodeDifferentUnitsCase() throws IntegerOverflowException {
        PointVolume volume1 = new PointVolume(10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        PointVolume volume2 = new PointVolume(10,10,20,2.0,1.0,"px");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        assertEquals(volume1.hashCode(),volume2.hashCode());

    }

    @Test
    public void testHashCodeDifferentUnits() throws IntegerOverflowException {
        PointVolume volume1 = new PointVolume(10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        PointVolume volume2 = new PointVolume(10,10,20,2.0,1.0,"um");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        assertNotEquals(volume1.hashCode(),volume2.hashCode());

    }


    // EQUALITY TESTS

    @Test
    public void testEqualsDifferentValue() throws IntegerOverflowException {
        PointVolume volume1 = new PointVolume(5,4,13,1.0,1.0,"Test");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        PointVolume volume2 = new PointVolume(5,4,13,1.0,1.0,"Test");
        volume2.add(1,2,3);
        volume2.add(0,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        assertFalse(volume1.equals(volume2));
        assertFalse(volume2.equals(volume1));

    }

    @Test
    public void testEqualsDifferentPointOrder() throws IntegerOverflowException {// Verifying that the order of point placement doesn't matter
        PointVolume volume1 = new PointVolume(10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        PointVolume volume2 = new PointVolume(10,10,20,2.0,1.0,"PX");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        PointVolume volume3 = new PointVolume(10,10,20,2.0,1.0,"PX");
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
        PointVolume volume1 = new PointVolume(5,4,13,1.0,1.0,"Test");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        PointVolume volume2 = new PointVolume(5,4,13,1.0,1.0,"Test");
        volume2.add(2,1,2);
        volume2.add(4,3,12);
        volume2.add(1,2,5);

        assertFalse(volume1.equals(volume2));
        assertFalse(volume2.equals(volume1));

    }

    @Test
    public void testEqualsDifferentCalibration() throws IntegerOverflowException {
        PointVolume volume1 = new PointVolume(10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        PointVolume volume2 = new PointVolume(10,10,20,2.1,1.0,"PX");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        assertFalse(volume1.equals(volume2));
        assertFalse(volume2.equals(volume1));

    }

    @Test
    public void testEqualsDifferentUnitsCase() throws IntegerOverflowException {
        PointVolume volume1 = new PointVolume(10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        PointVolume volume2 = new PointVolume(10,10,20,2.0,1.0,"px");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        assertTrue(volume1.equals(volume2));
        assertTrue(volume2.equals(volume1));

    }

    @Test
    public void testEqualsDifferentUnits() throws IntegerOverflowException {
        PointVolume volume1 = new PointVolume(10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        PointVolume volume2 = new PointVolume(10,10,20,2.0,1.0,"um");
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
        PointVolume volume1 = new PointVolume(10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        PointVolume volume2 = new PointVolume(10,10,20,2.0,1.0,"PX");
        volume2.add(2,2,3);
        volume2.add(4,3,12);
        volume2.add(2,2,2);
        volume2.add(1,2,5);

        Volume2 actual = volume1.getOverlappingPoints(volume2);

        HashSet<Point<Integer>> expected = new HashSet<>();
        expected.add(new Point<>(4,3,12));
        expected.add(new Point<>(1,2,5));

        assertEquals(2,actual.getNVoxels());
        for (Point<Integer> point:expected) assertTrue(actual.containsPoint(point));


    }

    @Test
    public void testGetOverlappingPointsWithoutOverlap() throws IntegerOverflowException  {
        PointVolume volume1 = new PointVolume(10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        PointVolume volume2 = new PointVolume(10,10,20,2.0,1.0,"PX");
        volume2.add(2,2,3);
        volume2.add(5,3,12);
        volume2.add(2,2,2);
        volume2.add(2,2,5);

        Volume2 actual = volume1.getOverlappingPoints(volume2);

        assertEquals(0,actual.getNVoxels());

    }

    @Test
    public void testGetOverlappingPointsTotalOverlap() throws IntegerOverflowException {
        PointVolume volume1 = new PointVolume(10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        PointVolume volume2 = new PointVolume(10,10,20,2.0,1.0,"PX");
        volume2.add(1,2,3);
        volume2.add(4,3,12);
        volume2.add(2,1,2);
        volume2.add(1,2,5);

        Volume2 actual = volume1.getOverlappingPoints(volume2);

        HashSet<Point<Integer>> expected = new HashSet<>();
        expected.add(new Point<>(1,2,3));
        expected.add(new Point<>(4,3,12));
        expected.add(new Point<>(2,1,2));
        expected.add(new Point<>(1,2,5));

        assertEquals(4,actual.getNVoxels());
        for (Point<Integer> point:expected) assertTrue(actual.containsPoint(point));

    }

    @Test
    public void testGetOverlappingPointsWithOverlapMoreIn1() throws IntegerOverflowException {
        PointVolume volume1 = new PointVolume(10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);
        volume1.add(4,5,5);

        PointVolume volume2 = new PointVolume(10,10,20,2.0,1.0,"PX");
        volume2.add(2,2,3);
        volume2.add(4,3,12);
        volume2.add(2,2,2);
        volume2.add(1,2,5);

        Volume2 actual = volume1.getOverlappingPoints(volume2);

        HashSet<Point<Integer>> expected = new HashSet<>();
        expected.add(new Point<>(4,3,12));
        expected.add(new Point<>(1,2,5));

        assertEquals(2,actual.getNVoxels());
        for (Point<Integer> point:expected) assertTrue(actual.containsPoint(point));

    }

    @Test
    public void testGetOverlappingPointsWithOverlapMoreIn2() throws IntegerOverflowException {
        PointVolume volume1 = new PointVolume(10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        PointVolume volume2 = new PointVolume(10,10,20,2.0,1.0,"PX");
        volume2.add(2,2,3);
        volume2.add(4,3,12);
        volume2.add(2,2,2);
        volume2.add(1,2,5);
        volume2.add(4,5,5);

        Volume2 actual = volume1.getOverlappingPoints(volume2);

        HashSet<Point<Integer>> expected = new HashSet<>();
        expected.add(new Point<>(4,3,12));
        expected.add(new Point<>(1,2,5));

        assertEquals(2,actual.getNVoxels());
        for (Point<Integer> point:expected) assertTrue(actual.containsPoint(point));

    }

    @Test
    public void testGetOverlapWithOverlap() throws IntegerOverflowException {
        PointVolume volume1 = new PointVolume(10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        PointVolume volume2 = new PointVolume(10,10,20,2.0,1.0,"PX");
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
        PointVolume volume1 = new PointVolume(10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        PointVolume volume2 = new PointVolume(10,10,20,2.0,1.0,"PX");
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
        PointVolume volume1 = new PointVolume(10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        PointVolume volume2 = new PointVolume(10,10,20,2.0,1.0,"PX");
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
        PointVolume volume1 = new PointVolume(10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);
        volume1.add(4,5,5);

        PointVolume volume2 = new PointVolume(10,10,20,2.0,1.0,"PX");
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
        PointVolume volume1 = new PointVolume(10,10,20,2.0,1.0,"PX");
        volume1.add(1,2,3);
        volume1.add(4,3,12);
        volume1.add(2,1,2);
        volume1.add(1,2,5);

        PointVolume volume2 = new PointVolume(10,10,20,2.0,1.0,"PX");
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
        PointVolume volume = new PointVolume(10,7,15,2.0,1.0,"PX");
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

        volume.calculateSurface();
        assertNotNull(volume.surface);

        volume.clearSurface();
        assertNull(volume.surface);
    }

    @Test
    public void testClearPoints() throws IntegerOverflowException {
        PointVolume volume = new PointVolume(10,7,15,2.0,1.0,"PX");
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
        assertEquals(0,volume.getNVoxels());
    }


    // MISSING TESTS

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

    @Test @Ignore
    public void testGetProjectedAreaPixelDistances() {

    }

    @Test @Ignore
    public void testGetProjectedAreaCalibratedDistances() {

    }

    @Test
    public void testGetCentroidSeparation2DPixelDistances() throws IntegerOverflowException {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        PointVolume volume1 = new PointVolume(20,20,1,dppXY,dppZ,units);
        volume1.add(10,5,0);
        volume1.add(11,5,0);
        volume1.add(11,6,0);
        volume1.add(12,6,0);

        PointVolume volume2 = new PointVolume(20,20,1,dppXY,dppZ,units);
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

        PointVolume volume1 = new PointVolume(20,20,1,dppXY,dppZ,units);
        volume1.add(10,5,0);
        volume1.add(11,5,0);
        volume1.add(11,6,0);
        volume1.add(12,6,0);

        PointVolume volume2 = new PointVolume(20,20,1,dppXY,dppZ,units);
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

        PointVolume volume1 = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume1.add(10,5,0);
        volume1.add(11,5,0);
        volume1.add(11,6,0);
        volume1.add(12,6,0);
        volume1.add(10,5,1);
        volume1.add(11,5,1);
        volume1.add(12,6,1);

        PointVolume volume2 = new PointVolume(20,10,5,dppXY,dppZ,units);
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

        PointVolume volume1 = new PointVolume(20,10,5,dppXY,dppZ,units);
        volume1.add(10,5,0);
        volume1.add(11,5,0);
        volume1.add(11,6,0);
        volume1.add(12,6,0);
        volume1.add(10,5,1);
        volume1.add(11,5,1);
        volume1.add(12,6,1);

        PointVolume volume2 = new PointVolume(20,10,5,dppXY,dppZ,units);
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