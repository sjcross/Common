/*
  Comparing results to CellProfiler's MeasureTexture results.
 */

package io.github.sjcross.sjcommon.analysis;

import ij.IJ;
import ij.ImagePlus;
import org.junit.jupiter.api.Test;

import io.github.sjcross.sjcommon.analysis.TextureCalculator;
import io.github.sjcross.sjcommon.expectedobjects.Objects2D;
import io.github.sjcross.sjcommon.object.volume.Volume;

import java.net.URLDecoder;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TextureCalculatorTest {
    private double tolerance = 1E-4;

    @Test
    public void testGetASMWholeImage1px() throws Exception {
        // Loading test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage);

        // Initialising texture calculator
        TextureCalculator calculator = new TextureCalculator();
        calculator.calculate(ipl1.getImageStack(),1,0,0);

        // Comparing results
        double expected = 0.000195420324691;
        double actual = calculator.getASM();
        assertEquals(expected,actual,1E-8);

    }

    @Test
    public void testGetContrastWholeImage1px() throws Exception {
        // Loading test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage);

        // Initialising texture calculator
        TextureCalculator calculator = new TextureCalculator();
        calculator.calculate(ipl1.getImageStack(),1,0,0);

        // Comparing results
        double expected = 200.149749373;
        double actual = calculator.getContrast();
        assertEquals(expected,actual,tolerance);

    }

    @Test
    public void testGetCorrelationWholeImage1px() throws Exception {
        // Loading test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage);

        // Initialising texture calculator
        TextureCalculator calculator = new TextureCalculator();
        calculator.calculate(ipl1.getImageStack(),1,0,0);

        // Comparing results
        double expected = 0.98111352883;
        double actual = calculator.getCorrelation();

        assertEquals(expected,actual,tolerance);

    }

    @Test
    public void testGetEntropyWholeImage1px() throws Exception {
        // Loading test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage);

        // Initialising texture calculator
        TextureCalculator calculator = new TextureCalculator();
        calculator.calculate(ipl1.getImageStack(),1,0,0);

        // Comparing results
        double expected = 12.5183432768;
        double actual = calculator.getEntropy();
        assertEquals(expected,actual,tolerance);

    }

    @Test
    public void testGetASMWholeImage3px() throws Exception {
        // Loading test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage);

        // Initialising texture calculator
        TextureCalculator calculator = new TextureCalculator();
        calculator.calculate(ipl1.getImageStack(),3,0,0);

        // Comparing results
        double expected = 0.000175200683997;
        double actual = calculator.getASM();
        assertEquals(expected,actual,1E-8);

    }

    @Test
    public void testGetContrastWholeImage3px() throws Exception {
        // Loading test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage);

        // Initialising texture calculator
        TextureCalculator calculator = new TextureCalculator();
        calculator.calculate(ipl1.getImageStack(),3,0,0);

        // Comparing results
        double expected = 322.0187662;
        double actual = calculator.getContrast();
        assertEquals(expected,actual,tolerance);

    }

    @Test
    public void testGetCorrelationWholeImage3px() throws Exception {
        // Loading test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage);

        // Initialising texture calculator
        TextureCalculator calculator = new TextureCalculator();
        calculator.calculate(ipl1.getImageStack(),3,0,0);

        // Comparing results
        double expected = 0.967977895;
        double actual = calculator.getCorrelation();

        assertEquals(expected,actual,tolerance);

    }

    @Test
    public void testGetEntropyWholeImage3px() throws Exception {
        // Loading test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage);

        // Initialising texture calculator
        TextureCalculator calculator = new TextureCalculator();
        calculator.calculate(ipl1.getImageStack(),3,0,0);

        // Comparing results
        double expected = 12.63484218;
        double actual = calculator.getEntropy();
        assertEquals(expected,actual,tolerance);

    }

    @Test
    public void testGetASMWithVolume1px() throws Exception {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        // Loading test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage);

        // Loading objects
        HashMap<Integer,Volume> testObjects = new Objects2D().getObjects(dppXY,dppZ,units);

        // Getting expected results
        HashMap<Integer, HashMap<String, Double>> expectedMeasurements = new Objects2D().getMeasurements();

        // Initialising texture calculator
        TextureCalculator calculator = new TextureCalculator();

        // Iterating over each Volume, testing the PCC value
        for (int ID:testObjects.keySet()) {
            Volume testObject = testObjects.get(ID);

            // Running the texture measurement
            calculator.calculate(ipl1.getImageStack(),testObject,1,0,0);

            double expected = expectedMeasurements.get(ID).get(Objects2D.Measures.ASM_1PX.name());
            double actual = calculator.getASM();

            assertEquals(expected,actual,tolerance);

        }
    }

    @Test
    public void testGetContrastWithVolume1px() throws Exception {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        // Loading test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage);

        // Loading objects
        HashMap<Integer,Volume> testObjects = new Objects2D().getObjects(dppXY,dppZ,units);

        // Getting expected results
        HashMap<Integer, HashMap<String, Double>> expectedMeasurements = new Objects2D().getMeasurements();

        // Initialising texture calculator
        TextureCalculator calculator = new TextureCalculator();

        // Iterating over each Volume, testing the PCC value
        for (int ID:testObjects.keySet()) {
            Volume testObject = testObjects.get(ID);

            // Running the texture measurement
            calculator.calculate(ipl1.getImageStack(),testObject,1,0,0);

            double expected = expectedMeasurements.get(ID).get(Objects2D.Measures.CONTRAST_1PX.name());
            double actual = calculator.getContrast();

            assertEquals(expected,actual,tolerance);

        }
    }

    @Test
    public void testGetCorrelationWithVolume1px() throws Exception {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        // Loading test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage);

        // Loading objects
        HashMap<Integer,Volume> testObjects = new Objects2D().getObjects(dppXY,dppZ,units);

        // Getting expected results
        HashMap<Integer, HashMap<String, Double>> expectedMeasurements = new Objects2D().getMeasurements();

        // Initialising texture calculator
        TextureCalculator calculator = new TextureCalculator();

        // Iterating over each Volume, testing the PCC value
        for (int ID:testObjects.keySet()) {
            Volume testObject = testObjects.get(ID);

            // Running the texture measurement
            calculator.calculate(ipl1.getImageStack(),testObject,1,0,0);

            double expected = expectedMeasurements.get(ID).get(Objects2D.Measures.CORRELATION_1PX.name());
            double actual = calculator.getCorrelation();

            assertEquals(expected,actual,tolerance);

        }
    }

    @Test
    public void testGetEntropyWithVolume1px() throws Exception {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        // Loading test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage);

        // Loading objects
        HashMap<Integer,Volume> testObjects = new Objects2D().getObjects(dppXY,dppZ,units);

        // Getting expected results
        HashMap<Integer, HashMap<String, Double>> expectedMeasurements = new Objects2D().getMeasurements();

        // Initialising texture calculator
        TextureCalculator calculator = new TextureCalculator();

        // Iterating over each Volume, testing the PCC value
        for (int ID:testObjects.keySet()) {
            Volume testObject = testObjects.get(ID);

            // Running the texture measurement
            calculator.calculate(ipl1.getImageStack(),testObject,1,0,0);

            double expected = expectedMeasurements.get(ID).get(Objects2D.Measures.ENTROPY_1PX.name());
            double actual = calculator.getEntropy();

            assertEquals(expected,actual,tolerance);

        }
    }

    @Test
    public void testGetASMWithVolume3px() throws Exception {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        // Loading test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage);

        // Loading objects
        HashMap<Integer,Volume> testObjects = new Objects2D().getObjects(dppXY,dppZ,units);

        // Getting expected results
        HashMap<Integer, HashMap<String, Double>> expectedMeasurements = new Objects2D().getMeasurements();

        // Initialising texture calculator
        TextureCalculator calculator = new TextureCalculator();

        // Iterating over each Volume, testing the PCC value
        for (int ID:testObjects.keySet()) {
            Volume testObject = testObjects.get(ID);

            // Running the texture measurement
            calculator.calculate(ipl1.getImageStack(),testObject,3,0,0);

            double expected = expectedMeasurements.get(ID).get(Objects2D.Measures.ASM_3PX.name());
            double actual = calculator.getASM();

            assertEquals(expected,actual,tolerance);

        }
    }

    @Test
    public void testGetContrastWithVolume3px() throws Exception {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        // Loading test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage);

        // Loading objects
        HashMap<Integer,Volume> testObjects = new Objects2D().getObjects(dppXY,dppZ,units);

        // Getting expected results
        HashMap<Integer, HashMap<String, Double>> expectedMeasurements = new Objects2D().getMeasurements();

        // Initialising texture calculator
        TextureCalculator calculator = new TextureCalculator();

        // Iterating over each Volume, testing the PCC value
        for (int ID:testObjects.keySet()) {
            Volume testObject = testObjects.get(ID);

            // Running the texture measurement
            calculator.calculate(ipl1.getImageStack(),testObject,3,0,0);

            double expected = expectedMeasurements.get(ID).get(Objects2D.Measures.CONTRAST_3PX.name());
            double actual = calculator.getContrast();

            assertEquals(expected,actual,tolerance);

        }
    }

    @Test
    public void testGetCorrelationWithVolume3px() throws Exception {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        // Loading test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage);

        // Loading objects
        HashMap<Integer,Volume> testObjects = new Objects2D().getObjects(dppXY,dppZ,units);

        // Getting expected results
        HashMap<Integer, HashMap<String, Double>> expectedMeasurements = new Objects2D().getMeasurements();

        // Initialising texture calculator
        TextureCalculator calculator = new TextureCalculator();

        // Iterating over each Volume, testing the PCC value
        for (int ID:testObjects.keySet()) {
            Volume testObject = testObjects.get(ID);

            // Running the texture measurement
            calculator.calculate(ipl1.getImageStack(),testObject,3,0,0);

            double expected = expectedMeasurements.get(ID).get(Objects2D.Measures.CORRELATION_3PX.name());
            double actual = calculator.getCorrelation();

            assertEquals(expected,actual,tolerance);

        }
    }

    @Test
    public void testGetEntropyWithVolume3px() throws Exception {
        double dppXY = 0.02;
        double dppZ = 0.1;
        String units = "um";

        // Loading test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage);

        // Loading objects
        HashMap<Integer,Volume> testObjects = new Objects2D().getObjects(dppXY,dppZ,units);

        // Getting expected results
        HashMap<Integer, HashMap<String, Double>> expectedMeasurements = new Objects2D().getMeasurements();

        // Initialising texture calculator
        TextureCalculator calculator = new TextureCalculator();

        // Iterating over each Volume, testing the PCC value
        for (int ID:testObjects.keySet()) {
            Volume testObject = testObjects.get(ID);

            // Running the texture measurement
            calculator.calculate(ipl1.getImageStack(),testObject,3,0,0);

            double expected = expectedMeasurements.get(ID).get(Objects2D.Measures.ENTROPY_3PX.name());
            double actual = calculator.getEntropy();

            assertEquals(expected,actual,tolerance);

        }
    }
}