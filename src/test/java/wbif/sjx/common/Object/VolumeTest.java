package wbif.sjx.common.Object;

import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class VolumeTest {
    private double tolerance10 = 1E-10;
    private double tolerance2 = 1E-2;


    // ADDING COORDINATES

    @Test
    public void testAddCoordAlreadyExists() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(0,0,0);
        volume.addCoord(0,0,0);
        volume.addCoord(1,0,0);

        assertEquals(2,volume.getPoints().size());

    }


    // COORDINATE TESTS

    @Test
    public void testGetXCoords() {
        Volume volume = new Volume(2.0,1.0,"PX", false);
        volume.addCoord(1,2,3);
        volume.addCoord(4,3,12);
        volume.addCoord(2,1,2);
        volume.addCoord(1,2,5);
        volume.addCoord(1,2,8);
        volume.addCoord(1,4,8);
        volume.addCoord(2,4,8);
        volume.addCoord(3,4,8);
        volume.addCoord(2,4,2);
        volume.addCoord(2,6,9);

        ArrayList<Integer> actual = volume.getXCoords();
        Collections.sort(actual);

        ArrayList<Integer> expected = new ArrayList<Integer>(Arrays.asList(1,4,2,1,1,1,2,3,2,2));
        Collections.sort(expected);

        assertEquals(10,actual.size());
        assertEquals(expected,actual);

    }

    @Test
    public void testGetYCoords() {
        Volume volume = new Volume(2.0,1.0,"PX", false);
        volume.addCoord(1,2,3);
        volume.addCoord(4,3,12);
        volume.addCoord(2,1,2);
        volume.addCoord(1,2,5);
        volume.addCoord(1,2,8);
        volume.addCoord(1,4,8);
        volume.addCoord(2,4,8);
        volume.addCoord(3,4,8);
        volume.addCoord(2,4,2);
        volume.addCoord(2,6,9);

        ArrayList<Integer> actual = volume.getYCoords();
        Collections.sort(actual);

        ArrayList<Integer> expected = new ArrayList<Integer>(Arrays.asList(2,3,1,2,2,4,4,4,4,6));
        Collections.sort(expected);

        assertEquals(10,actual.size());
        assertEquals(expected,actual);

    }

    @Test
    public void testGetZCoords() {
        Volume volume = new Volume(2.0,1.0,"PX", false);
        volume.addCoord(1,2,3);
        volume.addCoord(4,3,12);
        volume.addCoord(2,1,2);
        volume.addCoord(1,2,5);
        volume.addCoord(1,2,8);
        volume.addCoord(1,4,8);
        volume.addCoord(2,4,8);
        volume.addCoord(3,4,8);
        volume.addCoord(2,4,2);
        volume.addCoord(2,6,9);

        ArrayList<Integer> actual = volume.getZCoords();
        Collections.sort(actual);

        ArrayList<Integer> expected = new ArrayList<Integer>(Arrays.asList(3,12,2,5,8,8,8,8,2,9));
        Collections.sort(expected);

        assertEquals(10,actual.size());
        assertEquals(expected,actual);

    }

    @Test
    public void testGetXNoVolume() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);

        double[] actualX = volume.getX(true);
        double[] expectedX = new double[]{};

        assertArrayEquals(expectedX,actualX, tolerance10);
        assertEquals(0,volume.getPoints().size());

    }

    @Test
    public void testGetXPixelDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        double[] actualX = volume.getX(true);
        double[] expectedX = new double[]{10,10,11,13};

        assertArrayEquals(expectedX,actualX, tolerance10);

    }

    @Test
    public void testGetXCalibratedDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        double[] actualX = volume.getX(false);
        double[] expectedX = new double[]{0.2,0.2,0.22,0.26};

        assertArrayEquals(expectedX,actualX, tolerance10);

    }

    @Test
    public void testGetYPixelDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        double[] actualY = volume.getY(true);
        double[] expectedY = new double[]{5,5,5,7};

        assertArrayEquals(expectedY,actualY, tolerance10);

    }

    @Test
    public void testGetYCalibratedDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        double[] actualY = volume.getY(false);
        double[] expectedY = new double[]{0.1,0.1,0.1,0.14};

        assertArrayEquals(expectedY,actualY, tolerance10);

    }

    @Test
    public void testGetZPixelDistancesDoesntMatchXY() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        double[] actualZ = volume.getZ(true,false);
        double[] expectedZ = new double[]{1,2,2,1};

        assertArrayEquals(expectedZ,actualZ, tolerance10);

    }

    @Test
    public void testGetZPixelDistancesDoesMatchXY() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        double[] actualZ = volume.getZ(true,true);
        double[] expectedZ = new double[]{5,10,10,5};

        assertArrayEquals(expectedZ,actualZ, tolerance10);

    }

    @Test
    public void testGetZCalibratedDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

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

        Volume volume = new Volume(dppXY,dppZ,units, false);

        double actual = volume.getXYScaledZ(1);
        double expected = 5d;

        assertEquals(expected,actual, tolerance10);

    }


    // MEAN POSITION

    @Test
    public void testGetXMeanPixelDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertEquals(11,volume.getXMean(true), tolerance10);

    }

    @Test
    public void testGetXMeanCalibratedDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertEquals(0.22,volume.getXMean(false), tolerance10);

    }

    @Test
    public void testGetYMeanPixelDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertEquals(5.5,volume.getYMean(true), tolerance10);

    }

    @Test
    public void testGetYMeanCalibratedDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertEquals(0.11,volume.getYMean(false), tolerance10);

    }

    @Test
    public void testGetZMeanPixelDistancesDoesntMatchXY() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertEquals(1.5,volume.getZMean(true,false), tolerance10);

    }

    @Test
    public void testGetZMeanPixelDistancesDoesMatchXY() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertEquals(7.5,volume.getZMean(true,true), tolerance10);

    }

    @Test
    public void testGetZMeanCalibratedDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertEquals(0.15,volume.getZMean(false,false), tolerance10);
        assertEquals(0.15,volume.getZMean(false,true), tolerance10);

    }


    // MEDIAN POSITION

    @Test
    public void testGetXMedianPixelDistancesEvenN() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertEquals(10.5,volume.getXMedian(true), tolerance10);
    }

    @Test
    public void testGetXMedianCalibratedDistancesEvenN() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertEquals(0.21,volume.getXMedian(false), tolerance10);

    }

    @Test
    public void testGetYMedianPixelDistancesEvenN() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertEquals(5,volume.getYMedian(true), tolerance10);
    }

    @Test
    public void testGetYMedianCalibratedDistancesEvenN() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertEquals(0.1,volume.getYMedian(false), tolerance10);

    }

    @Test
    public void testGetZMedianPixelDistancesDoesntMatchXYEvenN() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertEquals(1.5,volume.getZMedian(true,false), tolerance10);

    }

    @Test
    public void testGetZMedianPixelDistancesDoesMatchXYEvenN() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertEquals(7.5,volume.getZMedian(true,true), tolerance10);

    }

    @Test
    public void testGetZMedianCalibratedDistancesEvenN() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertEquals(0.15,volume.getZMedian(false,false), tolerance10);
        assertEquals(0.15,volume.getZMedian(false,true), tolerance10);

    }

    @Test
    public void testGetXMedianPixelDistancesOddN() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);
        volume.addCoord(13,7,2);

        assertEquals(11,volume.getXMedian(true), tolerance10);
    }

    @Test
    public void testGetXMedianCalibratedDistancesOddN() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);
        volume.addCoord(13,7,2);

        assertEquals(0.22,volume.getXMedian(false), tolerance10);

    }

    @Test
    public void testGetYMedianPixelDistancesOddN() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);
        volume.addCoord(13,7,2);

        assertEquals(5,volume.getYMedian(true), tolerance10);
    }

    @Test
    public void testGetYMedianCalibratedDistancesOddN() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);
        volume.addCoord(13,7,2);

        assertEquals(0.1,volume.getYMedian(false), tolerance10);

    }

    @Test
    public void testGetZMedianPixelDistancesDoesntMatchXYOddN() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);
        volume.addCoord(13,7,2);

        assertEquals(2,volume.getZMedian(true,false), tolerance10);

    }

    @Test
    public void testGetZMedianPixelDistancesDoesMatchXYOddN() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);
        volume.addCoord(13,7,2);

        assertEquals(10,volume.getZMedian(true,true), tolerance10);

    }

    @Test
    public void testGetZMedianCalibratedDistancesOddN() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);
        volume.addCoord(13,7,2);

        assertEquals(0.2,volume.getZMedian(false,false), tolerance10);
        assertEquals(0.2,volume.getZMedian(false,true), tolerance10);

    }


    // ANGLE BETWEEN TWO VOLUMES

    @Test
    public void testCalculateAngle2DTopRight() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(dppXY,dppZ,units, false);
        volume1.addCoord(0,0,0);

        Volume volume2 = new Volume(dppXY,dppZ,units, false);
        volume2.addCoord(10,10,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(45);

        assertEquals(expectedAngle,actualAngle, tolerance10);

    }

    @Test
    public void testCalculateAngle2DTopLeft() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(dppXY,dppZ,units, false);
        volume1.addCoord(0,0,0);

        Volume volume2 = new Volume(dppXY,dppZ,units, false);
        volume2.addCoord(-5,5,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(135);

        assertEquals(expectedAngle,actualAngle, tolerance10);

    }

    @Test
    public void testCalculateAngle2DBottomLeft() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(dppXY,dppZ,units, false);
        volume1.addCoord(0,0,0);

        Volume volume2 = new Volume(dppXY,dppZ,units, false);
        volume2.addCoord(-5,-5,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(-135);

        assertEquals(expectedAngle,actualAngle, tolerance10);

    }

    @Test
    public void testCalculateAngle2DBottomRight() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(dppXY,dppZ,units, false);
        volume1.addCoord(0,0,0);

        Volume volume2 = new Volume(dppXY,dppZ,units, false);
        volume2.addCoord(5,-5,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(-45);

        assertEquals(expectedAngle,actualAngle, tolerance10);

    }

    @Test
    public void testCalculateAngle2DPositiveXAxis() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(dppXY,dppZ,units, false);
        volume1.addCoord(0,0,0);

        Volume volume2 = new Volume(dppXY,dppZ,units, false);
        volume2.addCoord(5,0,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(0);

        assertEquals(expectedAngle,actualAngle, tolerance10);

    }

    @Test
    public void testCalculateAngle2DPositiveYAxis() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(dppXY,dppZ,units, false);
        volume1.addCoord(0,0,0);

        Volume volume2 = new Volume(dppXY,dppZ,units, false);
        volume2.addCoord(0,5,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(90);

        assertEquals(expectedAngle,actualAngle, tolerance10);

    }

    @Test
    public void testCalculateAngle2DNegativeXAxis() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(dppXY,dppZ,units, false);
        volume1.addCoord(0,0,0);

        Volume volume2 = new Volume(dppXY,dppZ,units, false);
        volume2.addCoord(-5,0,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(180);

        assertEquals(expectedAngle,actualAngle, tolerance10);

    }

    @Test
    public void testCalculateAngle2DNegativeYAxis() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(dppXY,dppZ,units, false);
        volume1.addCoord(0,0,0);

        Volume volume2 = new Volume(dppXY,dppZ,units, false);
        volume2.addCoord(0,-5,0);

        double actualAngle = volume1.calculateAngle2D(volume2);
        double expectedAngle = Math.toRadians(-90);

        assertEquals(expectedAngle,actualAngle, tolerance10);

    }


    // HEIGHT
    @Test
    public void testGetHeightPixelDistancesDoesntMatchXY() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,3);
        volume.addCoord(13,7,1);

        assertEquals(2,volume.getHeight(true,false), tolerance10);

    }

    @Test
    public void testGetHeightPixelDistancesDoesMatchXY() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,3);
        volume.addCoord(13,7,1);

        assertEquals(10,volume.getHeight(true,true), tolerance10);

    }

    @Test
    public void testGetHeightCalibratedDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,3);
        volume.addCoord(13,7,1);

        assertEquals(0.2,volume.getHeight(false,true), tolerance10);
        assertEquals(0.2,volume.getHeight(false,false), tolerance10);

    }


    // EXTENTS
    @Test
    public void testGetExtentsPixelDistancesDoesntMatchXY() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,3);
        volume.addCoord(13,7,1);

        double[][] actualExtents = volume.getExtents(true,false);
        double[][] expectedExtents = new double[][]{{10,13},{5,7},{1,3}};

        assertArrayEquals(expectedExtents[0],actualExtents[0], tolerance10);
        assertArrayEquals(expectedExtents[1],actualExtents[1], tolerance10);
        assertArrayEquals(expectedExtents[2],actualExtents[2], tolerance10);

    }

    @Test
    public void testGetExtentsPixelDistancesDoesMatchXY() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,3);
        volume.addCoord(13,7,1);

        double[][] actualExtents = volume.getExtents(true,true);
        double[][] expectedExtents = new double[][]{{10,13},{5,7},{5,15}};

        assertArrayEquals(expectedExtents[0],actualExtents[0], tolerance10);
        assertArrayEquals(expectedExtents[1],actualExtents[1], tolerance10);
        assertArrayEquals(expectedExtents[2],actualExtents[2], tolerance10);

    }

    @Test
    public void testGetExtentsCalibratedDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,3);
        volume.addCoord(13,7,1);

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

    @Test
    public void testGetExtents2DPixelDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,3);
        volume.addCoord(13,7,1);

        double[][] expectedExtents = new double[][]{{10,13},{5,7}};
        double[][] actualExtents = volume.getExtents2D(true);
        assertArrayEquals(expectedExtents[0],actualExtents[0], tolerance10);
        assertArrayEquals(expectedExtents[1],actualExtents[1], tolerance10);

    }

    @Test
    public void testGetExtents2DDistancesCalibratedDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,3);
        volume.addCoord(13,7,1);

        double[][] expectedExtents = new double[][]{{0.2,0.26},{0.1,0.14}};
        double[][] actualExtents = volume.getExtents2D(false);
        assertArrayEquals(expectedExtents[0],actualExtents[0], tolerance10);
        assertArrayEquals(expectedExtents[1],actualExtents[1], tolerance10);

    }


    // AREA VOLUME CHECKS

    @Test
    public void testHasVolumeNoVolume() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);

        assertFalse(volume.hasVolume());

    }

    @Test
    public void testHasVolume2D() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
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

        Volume volume = new Volume(dppXY,dppZ,units, false);
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

        Volume volume = new Volume(dppXY,dppZ,units, false);

        assertFalse(volume.hasArea());

    }

    @Test
    public void testHasArea2D() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
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

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertTrue(volume.hasArea());

    }


    // VOLUME

    @Test
    public void testContainsPointDoesContain() {
        Volume volume = new Volume(2.0,1.0,"PX", false);
        volume.addCoord(1,2,3);
        volume.addCoord(4,3,12);
        volume.addCoord(2,1,2);
        volume.addCoord(1,2,5);

        Point<Integer> point = new Point<>(1,2,3);

        assertTrue(volume.getPoints().contains(point));

    }

    @Test
    public void testContainsPointDoesntContain() {
        Volume volume = new Volume(2.0,1.0,"PX", false);
        volume.addCoord(1,2,3);
        volume.addCoord(4,3,12);
        volume.addCoord(2,1,2);
        volume.addCoord(1,2,5);

        Point<Integer> point = new Point<>(2,2,3);

        assertFalse(volume.getPoints().contains(point));

    }

    @Test
    public void testGetNVoxelsNoVolume() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);

        assertEquals(0,volume.getNVoxels());

    }

    @Test
    public void testGetNVoxelsHasVolume() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
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

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertEquals(20,volume.getContainedVolume(true), tolerance10);

    }

    @Test
    public void testGetContainedVolumeCalibratedDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,5,2);
        volume.addCoord(11,5,2);
        volume.addCoord(13,7,1);

        assertEquals(0.00016,volume.getContainedVolume(false), tolerance10);

    }

    @Test
    public void testGetContainedVolumePixelDistancesFlatObject() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,6,1);
        volume.addCoord(11,5,1);
        volume.addCoord(13,7,1);

        assertEquals(20,volume.getContainedVolume(true), tolerance10);

    }

    @Test
    public void testGetContainedVolumeCalibratedDistancesFlatObject() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume = new Volume(dppXY,dppZ,units, false);
        volume.addCoord(10,5,1);
        volume.addCoord(10,6,1);
        volume.addCoord(11,5,1);
        volume.addCoord(13,7,1);

        assertEquals(0.00016,volume.getContainedVolume(false), tolerance10);

    }


    // HASHCODE TESTS

    @Test
    public void testHashCodeDifferentValue() {
        Volume volume1 = new Volume(1.0,1.0,"Test", false);
        volume1.addCoord(1,2,3);
        volume1.addCoord(4,3,12);
        volume1.addCoord(2,1,2);
        volume1.addCoord(1,2,5);

        Volume volume2 = new Volume(1.0,1.0,"Test", false);
        volume2.addCoord(1,2,3);
        volume2.addCoord(-4,3,12);
        volume2.addCoord(2,1,2);
        volume2.addCoord(1,2,5);

        assertNotEquals(volume1.hashCode(),volume2.hashCode());

    }

    @Test
    public void testHashCodeDifferentPointOrder() {
        // Verifying that the order of point placement doesn't matter
        Volume volume1 = new Volume(2.0,1.0,"Test", false);
        volume1.addCoord(1,2,3);
        volume1.addCoord(4,3,12);
        volume1.addCoord(2,1,2);
        volume1.addCoord(1,2,5);

        Volume volume2 = new Volume(2.0,1.0,"Test", false);
        volume2.addCoord(1,2,3);
        volume2.addCoord(4,3,12);
        volume2.addCoord(2,1,2);
        volume2.addCoord(1,2,5);

        Volume volume3 = new Volume(2.0,1.0,"Test", false);
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
        Volume volume1 = new Volume(1.0,1.0,"Test", false);
        volume1.addCoord(1,2,3);
        volume1.addCoord(4,3,12);
        volume1.addCoord(2,1,2);
        volume1.addCoord(1,2,5);

        Volume volume2 = new Volume(1.0,1.0,"Test", false);
        volume2.addCoord(2,1,2);
        volume2.addCoord(4,3,12);
        volume2.addCoord(1,2,5);

        assertNotEquals(volume1.hashCode(),volume2.hashCode());

    }

    @Test
    public void testHashCodeDifferentCalibration() {
        Volume volume1 = new Volume(2.0,1.0,"PX", false);
        volume1.addCoord(1,2,3);
        volume1.addCoord(4,3,12);
        volume1.addCoord(2,1,2);
        volume1.addCoord(1,2,5);

        Volume volume2 = new Volume(2.1,1.0,"PX", false);
        volume2.addCoord(1,2,3);
        volume2.addCoord(4,3,12);
        volume2.addCoord(2,1,2);
        volume2.addCoord(1,2,5);

        assertNotEquals(volume1.hashCode(),volume2.hashCode());

    }

    @Test
    public void testHashCodeDifferentUnitsCase() {
        Volume volume1 = new Volume(2.0,1.0,"PX", false);
        volume1.addCoord(1,2,3);
        volume1.addCoord(4,3,12);
        volume1.addCoord(2,1,2);
        volume1.addCoord(1,2,5);

        Volume volume2 = new Volume(2.0,1.0,"px", false);
        volume2.addCoord(1,2,3);
        volume2.addCoord(4,3,12);
        volume2.addCoord(2,1,2);
        volume2.addCoord(1,2,5);

        assertEquals(volume1.hashCode(),volume2.hashCode());

    }

    @Test
    public void testHashCodeDifferentUnits() {
        Volume volume1 = new Volume(2.0,1.0,"PX", false);
        volume1.addCoord(1,2,3);
        volume1.addCoord(4,3,12);
        volume1.addCoord(2,1,2);
        volume1.addCoord(1,2,5);

        Volume volume2 = new Volume(2.0,1.0,"um", false);
        volume2.addCoord(1,2,3);
        volume2.addCoord(4,3,12);
        volume2.addCoord(2,1,2);
        volume2.addCoord(1,2,5);

        assertNotEquals(volume1.hashCode(),volume2.hashCode());

    }


    // EQUALITY TESTS

    @Test
    public void testEqualsDifferentValue() {
        Volume volume1 = new Volume(1.0,1.0,"Test", false);
        volume1.addCoord(1,2,3);
        volume1.addCoord(4,3,12);
        volume1.addCoord(2,1,2);
        volume1.addCoord(1,2,5);

        Volume volume2 = new Volume(1.0,1.0,"Test", false);
        volume2.addCoord(1,2,3);
        volume2.addCoord(-4,3,12);
        volume2.addCoord(2,1,2);
        volume2.addCoord(1,2,5);

        assertFalse(volume1.equals(volume2));
        assertFalse(volume2.equals(volume1));

    }

    @Test
    public void testEqualsDifferentPointOrder() {// Verifying that the order of point placement doesn't matter
        Volume volume1 = new Volume(2.0,1.0,"PX", false);
        volume1.addCoord(1,2,3);
        volume1.addCoord(4,3,12);
        volume1.addCoord(2,1,2);
        volume1.addCoord(1,2,5);

        Volume volume2 = new Volume(2.0,1.0,"PX", false);
        volume2.addCoord(1,2,3);
        volume2.addCoord(4,3,12);
        volume2.addCoord(2,1,2);
        volume2.addCoord(1,2,5);

        Volume volume3 = new Volume(2.0,1.0,"PX", false);
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
        Volume volume1 = new Volume(1.0,1.0,"Test", false);
        volume1.addCoord(1,2,3);
        volume1.addCoord(4,3,12);
        volume1.addCoord(2,1,2);
        volume1.addCoord(1,2,5);

        Volume volume2 = new Volume(1.0,1.0,"Test", false);
        volume2.addCoord(2,1,2);
        volume2.addCoord(4,3,12);
        volume2.addCoord(1,2,5);

        assertFalse(volume1.equals(volume2));
        assertFalse(volume2.equals(volume1));

    }

    @Test
    public void testEqualsDifferentCalibration() {
        Volume volume1 = new Volume(2.0,1.0,"PX", false);
        volume1.addCoord(1,2,3);
        volume1.addCoord(4,3,12);
        volume1.addCoord(2,1,2);
        volume1.addCoord(1,2,5);

        Volume volume2 = new Volume(2.1,1.0,"PX", false);
        volume2.addCoord(1,2,3);
        volume2.addCoord(4,3,12);
        volume2.addCoord(2,1,2);
        volume2.addCoord(1,2,5);

        assertFalse(volume1.equals(volume2));
        assertFalse(volume2.equals(volume1));

    }

    @Test
    public void testEqualsDifferentUnitsCase() {
        Volume volume1 = new Volume(2.0,1.0,"PX", false);
        volume1.addCoord(1,2,3);
        volume1.addCoord(4,3,12);
        volume1.addCoord(2,1,2);
        volume1.addCoord(1,2,5);

        Volume volume2 = new Volume(2.0,1.0,"px", false);
        volume2.addCoord(1,2,3);
        volume2.addCoord(4,3,12);
        volume2.addCoord(2,1,2);
        volume2.addCoord(1,2,5);

        assertTrue(volume1.equals(volume2));
        assertTrue(volume2.equals(volume1));

    }

    @Test
    public void testEqualsDifferentUnits() {
        Volume volume1 = new Volume(2.0,1.0,"PX", false);
        volume1.addCoord(1,2,3);
        volume1.addCoord(4,3,12);
        volume1.addCoord(2,1,2);
        volume1.addCoord(1,2,5);

        Volume volume2 = new Volume(2.0,1.0,"um", false);
        volume2.addCoord(1,2,3);
        volume2.addCoord(4,3,12);
        volume2.addCoord(2,1,2);
        volume2.addCoord(1,2,5);

        assertFalse(volume1.equals(volume2));
        assertFalse(volume2.equals(volume1));

    }


    // MISCELLANEOUS METHODS

    @Test
    public void testClearSurface() {
        Volume volume = new Volume(2.0,1.0,"PX", false);
        volume.addCoord(1,2,3);
        volume.addCoord(4,3,12);
        volume.addCoord(2,1,2);
        volume.addCoord(1,2,5);
        volume.addCoord(1,2,8);
        volume.addCoord(1,4,8);
        volume.addCoord(2,4,8);
        volume.addCoord(3,4,8);
        volume.addCoord(2,4,2);
        volume.addCoord(2,6,9);

        volume.calculateSurface();
        assertNotNull(volume.surface);

        volume.clearSurface();
        assertNull(volume.surface);
    }

    @Test
    public void testClearPoints() {
        Volume volume = new Volume(2.0,1.0,"PX", false);
        volume.addCoord(1,2,3);
        volume.addCoord(4,3,12);
        volume.addCoord(2,1,2);
        volume.addCoord(1,2,5);
        volume.addCoord(1,2,8);
        volume.addCoord(1,4,8);
        volume.addCoord(2,4,8);
        volume.addCoord(3,4,8);
        volume.addCoord(2,4,2);
        volume.addCoord(2,6,9);

        volume.clearPoints();
        assertEquals(0,volume.points.size());
    }


    // MISSING TESTS
    @Test @Ignore
    public void testGetOverlappingPoints() {

    }

    @Test @Ignore
    public void testGetOverlap() {

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

    @Test @Ignore
    public void testGetProjectedAreaPixelDistances() {

    }

    @Test @Ignore
    public void testGetProjectedAreaCalibratedDistances() {

    }

    @Test
    public void testGetCentroidSeparation2DPixelDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(dppXY,dppZ,units, true);
        volume1.addCoord(10,5,0);
        volume1.addCoord(11,5,0);
        volume1.addCoord(11,6,0);
        volume1.addCoord(12,6,0);

        Volume volume2 = new Volume(dppXY,dppZ,units, true);
        volume2.addCoord(1,2,0);
        volume2.addCoord(1,3,0);
        volume2.addCoord(2,1,0);
        volume2.addCoord(2,2,0);
        volume2.addCoord(3,2,0);

        double expected = 9.8433;
        double actual = volume1.getCentroidSeparation(volume2,true);

        assertEquals(expected,actual, tolerance2);

    }

    @Test
    public void testGetCentroidSeparation2DCalibratedDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(dppXY,dppZ,units, true);
        volume1.addCoord(10,5,0);
        volume1.addCoord(11,5,0);
        volume1.addCoord(11,6,0);
        volume1.addCoord(12,6,0);

        Volume volume2 = new Volume(dppXY,dppZ,units, true);
        volume2.addCoord(1,2,0);
        volume2.addCoord(1,3,0);
        volume2.addCoord(2,1,0);
        volume2.addCoord(2,2,0);
        volume2.addCoord(3,2,0);

        double expected = 0.1878;
        double actual = volume1.getCentroidSeparation(volume2,false);

        assertEquals(expected,actual, tolerance2);

    }

    @Test
    public void testGetCentroidSeparation3DPixelDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(dppXY,dppZ,units, false);
        volume1.addCoord(10,5,0);
        volume1.addCoord(11,5,0);
        volume1.addCoord(11,6,0);
        volume1.addCoord(12,6,0);
        volume1.addCoord(10,5,1);
        volume1.addCoord(11,5,1);
        volume1.addCoord(12,6,1);

        Volume volume2 = new Volume(dppXY,dppZ,units, false);
        volume2.addCoord(1,2,0);
        volume2.addCoord(1,3,0);
        volume2.addCoord(2,1,0);
        volume2.addCoord(2,2,0);
        volume2.addCoord(3,2,0);
        volume2.addCoord(1,3,1);
        volume2.addCoord(2,1,1);
        volume2.addCoord(2,2,1);
        volume2.addCoord(2,1,2);

        double expected = 9.8986;
        double actual = volume1.getCentroidSeparation(volume2,true);

        assertEquals(expected,actual, tolerance2);

    }

    @Test
    public void testGetCentroidSeparation3DCalibratedDistances() {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        Volume volume1 = new Volume(dppXY,dppZ,units, false);
        volume1.addCoord(10,5,0);
        volume1.addCoord(11,5,0);
        volume1.addCoord(11,6,0);
        volume1.addCoord(12,6,0);
        volume1.addCoord(10,5,1);
        volume1.addCoord(11,5,1);
        volume1.addCoord(12,6,1);

        Volume volume2 = new Volume(dppXY,dppZ,units, false);
        volume2.addCoord(1,2,0);
        volume2.addCoord(1,3,0);
        volume2.addCoord(2,1,0);
        volume2.addCoord(2,2,0);
        volume2.addCoord(3,2,0);
        volume2.addCoord(1,3,1);
        volume2.addCoord(2,1,1);
        volume2.addCoord(2,2,1);
        volume2.addCoord(2,1,2);

        double expected = 0.1980;
        double actual = volume1.getCentroidSeparation(volume2,false);

        assertEquals(expected,actual, tolerance2);

    }
}