package wbif.sjx.common.Analysis;

import ij.IJ;
import ij.ImagePlus;
import org.junit.jupiter.api.Test;
import wbif.sjx.common.ExpectedObjects.Objects2D;
import wbif.sjx.common.Object.Volume.Volume;

import java.net.URLDecoder;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ColocalisationCalculatorTest {
    private double tolerance = 1E-2;

    @Test
    public void calculatePCCWithVolume2D() throws Exception {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "um";

        // Loading images
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/ColocalisationChannel1_2D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/ColocalisationChannel2_2D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl2 = IJ.openImage(pathToImage);

        // Loading objects
        HashMap<Integer,Volume> testObjects = new Objects2D().getObjects(dppXY,dppZ,calibratedUnits);

        // Getting expected results
        HashMap<Integer, HashMap<String, Double>> expectedMeasurements = new Objects2D().getMeasurements();

        // Iterating over each Volume, testing the PCC value
        for (int ID:testObjects.keySet()) {
            Volume testObject = testObjects.get(ID);

            double expected = expectedMeasurements.get(ID).get(Objects2D.Measures.PCC.name());
            double actual = ColocalisationCalculator.calculatePCC(ipl1.getImageStack(),ipl2.getImageStack(),testObject);

            assertEquals(expected,actual,tolerance);
        }
    }

    @Test
    public void calculatePCCWholeImage2D8bit() throws Exception {
        // Loading images
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/ColocalisationChannel1_2D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/ColocalisationChannel2_2D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl2 = IJ.openImage(pathToImage);

        // Calculating PCC (expected value from Coloc2)
        double expected = 0.44;
        double actual = ColocalisationCalculator.calculatePCC(ipl1.getImageStack(),ipl2.getImageStack());

        // Testing returned value
        assertEquals(expected,actual,tolerance);

    }

    @Test
    public void calculatePCCWholeImage2D16bit() throws Exception {
        // Loading images
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/ColocalisationChannel1_2D_16bit.tif").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/ColocalisationChannel2_2D_16bit.tif").getPath(),"UTF-8");
        ImagePlus ipl2 = IJ.openImage(pathToImage);

        // Calculating PCC (expected value from Coloc2)
        double expected = 0.44;
        double actual = ColocalisationCalculator.calculatePCC(ipl1.getImageStack(),ipl2.getImageStack());

        // Testing returned value
        assertEquals(expected,actual,tolerance);

    }

    @Test
    public void calculatePCCWholeImage2D32bit() throws Exception {
        // Loading images
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/ColocalisationChannel1_2D_32bit.tif").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/ColocalisationChannel2_2D_32bit.tif").getPath(),"UTF-8");
        ImagePlus ipl2 = IJ.openImage(pathToImage);

        // Calculating PCC (expected value from Coloc2)
        double expected = 0.44;
        double actual = ColocalisationCalculator.calculatePCC(ipl1.getImageStack(),ipl2.getImageStack());

        // Testing returned value
        assertEquals(expected,actual,tolerance);

    }

    @Test
    public void calculatePCCWholeImage3D8bit() throws Exception {
        // Loading images
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/ColocalisationChannel1_3D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/ColocalisationChannel2_3D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl2 = IJ.openImage(pathToImage);

        // Calculating PCC (expected value from Coloc2)
        double expected = 0.08;
        double actual = ColocalisationCalculator.calculatePCC(ipl1.getImageStack(),ipl2.getImageStack());

        // Testing returned value
        assertEquals(expected,actual,tolerance);

    }

}