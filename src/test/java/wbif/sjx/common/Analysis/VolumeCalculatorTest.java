//package wbif.sjx.common.Analysis;
//
//import org.junit.Ignore;
//import org.junit.Test;
//import wbif.sjx.common.Object.Volume.Volume;
//import wbif.sjx.common.Object.Volume.Volume;
//
//import static org.junit.Assert.*;
//
///**
// * Created by sc13967 on 18/06/2018.
// */
//public class VolumeCalculatorTest {
//    @Test @Ignore
//    public void canFitHull() throws Exception {
//    }
//
//    @Test @Ignore
//    public void getHullVertices() throws Exception {
//    }
//
//    @Test @Ignore
//    public void getHullFaces() throws Exception {
//    }
//
//    @Test @Ignore
//    public void getHullSurfaceArea() throws Exception {
//    }
//
//    @Test @Ignore
//    public void getHullVolume() throws Exception {
//    }
//
//    @Test @Ignore
//    public void getSphericity() throws Exception {
//    }
//
//    @Test @Ignore
//    public void getSolidity() throws Exception {
//    }
//
//    /**
//     * Basic test to check the method can return the points inside a cube.  It isn't capable of showing points outside
//     * this cube aren't tested.
//     * @throws Exception
//     */
//    @Test @Ignore
//    public void getContainedPointsCube() throws Exception {
//        double dppXY = 0.02;
//        double dppZ = 0.1;
//        String units = "um";
//
//        // Creating a basic cube volume
//        Volume volume = new Volume(Volume.VolumeType.POINTLIST,15,30,8,dppXY,dppZ,units);
//        volume.add(10,20,5);
//        volume.add(12,20,5);
//        volume.add(12,22,5);
//        volume.add(10,22,5);
//        volume.add(10,20,7);
//        volume.add(12,20,7);
//        volume.add(12,22,7);
//        volume.add(10,22,7);
//
//        // Creating the VolumeCalculator
//        VolumeCalculator volumeCalculator = new VolumeCalculator(volume,VolumeCalculator.CENTROID);
//
//        // Getting contained points
//        Volume actual = volumeCalculator.getContainedPoints();
//
//        // Creating expected object
//        Volume expected = new Volume(Volume.VolumeType.POINTLIST,dppXY,dppZ,units,false);
//        expected.add(10,20,5);
//        expected.add(11,20,5);
//        expected.add(12,20,5);
//        expected.add(10,21,5);
//        expected.add(11,21,5);
//        expected.add(12,21,5);
//        expected.add(12,22,5);
//        expected.add(11,22,5);
//        expected.add(10,22,5);
//        expected.add(10,20,6);
//        expected.add(11,20,6);
//        expected.add(12,20,6);
//        expected.add(10,21,6);
//        expected.add(11,21,6);
//        expected.add(12,21,6);
//        expected.add(12,22,6);
//        expected.add(11,22,6);
//        expected.add(10,22,6);
//        expected.add(10,20,7);
//        expected.add(11,20,7);
//        expected.add(12,20,7);
//        expected.add(10,21,7);
//        expected.add(11,21,7);
//        expected.add(12,21,7);
//        expected.add(12,22,7);
//        expected.add(11,22,7);
//        expected.add(10,22,7);
//
//        assertEquals(expected,actual);
//
//    }
//}