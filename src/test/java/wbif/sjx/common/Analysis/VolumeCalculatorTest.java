package wbif.sjx.common.Analysis;

import org.junit.Ignore;
import org.junit.Test;
import wbif.sjx.common.Object.Volume;

import static org.junit.Assert.*;

/**
 * Created by sc13967 on 18/06/2018.
 */
public class VolumeCalculatorTest {
    @Test @Ignore
    public void canFitHull() throws Exception {
    }

    @Test @Ignore
    public void getHullVertices() throws Exception {
    }

    @Test @Ignore
    public void getHullFaces() throws Exception {
    }

    @Test @Ignore
    public void getHullSurfaceArea() throws Exception {
    }

    @Test @Ignore
    public void getHullVolume() throws Exception {
    }

    @Test @Ignore
    public void getSphericity() throws Exception {
    }

    @Test @Ignore
    public void getSolidity() throws Exception {
    }

    @Test
    public void getContainedPointsCube() throws Exception {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        // Creating a basic cube volume
        Volume volume = new Volume(dppXY,dppZ,units,false);
        volume.addCoord(10,20,5);
        volume.addCoord(12,20,5);
        volume.addCoord(12,22,5);
        volume.addCoord(10,22,5);
        volume.addCoord(10,20,6);
        volume.addCoord(12,20,6);
        volume.addCoord(12,22,6);
        volume.addCoord(10,22,6);

        // Creating the VolumeCalculator
        VolumeCalculator volumeCalculator = new VolumeCalculator(volume,VolumeCalculator.CENTROID);

        // Getting contained points
        Volume actual = volumeCalculator.getContainedPoints();

        // Creating expected object
        Volume expected = new Volume(dppXY,dppZ,units,false);
        expected.addCoord(10,20,5);
        expected.addCoord(11,20,5);
        expected.addCoord(12,20,5);
        expected.addCoord(10,21,5);
        expected.addCoord(11,21,5);
        expected.addCoord(12,21,5);
        expected.addCoord(12,22,5);
        expected.addCoord(11,22,5);
        expected.addCoord(10,22,5);
        expected.addCoord(10,20,6);
        expected.addCoord(11,20,6);
        expected.addCoord(12,20,6);
        expected.addCoord(10,21,6);
        expected.addCoord(11,21,6);
        expected.addCoord(12,21,6);
        expected.addCoord(12,22,6);
        expected.addCoord(11,22,6);
        expected.addCoord(10,22,6);
//        expected.addCoord(10,20,7);
//        expected.addCoord(11,20,7);
//        expected.addCoord(12,20,7);
//        expected.addCoord(10,21,7);
//        expected.addCoord(11,21,7);
//        expected.addCoord(12,21,7);
//        expected.addCoord(12,22,7);
//        expected.addCoord(11,22,7);
//        expected.addCoord(10,22,7);

        assertEquals(expected,actual);

    }
}