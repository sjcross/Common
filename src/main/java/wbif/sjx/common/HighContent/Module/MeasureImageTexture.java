package wbif.sjx.common.HighContent.Module;

import ij.ImagePlus;
import wbif.sjx.common.Analysis.TextureCalculator;
import wbif.sjx.common.HighContent.Object.*;

/**
 * Created by Stephen on 09/05/2017.
 */
public class MeasureImageTexture extends HCModule {
    public static final String INPUT_IMAGE = "Input image";
    public static final String X_OFFSET = "X-offset";
    public static final String Y_OFFSET = "Y-offset";
    public static final String Z_OFFSET = "Z-offset";

    @Override
    public void execute(HCWorkspace workspace, boolean verbose) {
        if (verbose) System.out.println("   Running image texture analysis");

        // Getting parameters
        int xOffs = parameters.getValue(X_OFFSET);
        int yOffs = parameters.getValue(Y_OFFSET);
        int zOffs = parameters.getValue(Z_OFFSET);

        // Getting input image
        HCName inputImageName = parameters.getValue(INPUT_IMAGE);
        HCImage inputImage = workspace.getImages().get(inputImageName);
        ImagePlus inputImagePlus = inputImage.getImagePlus();

        // Running texture measurement
        if (verbose) System.out.println("       Calculating co-occurance matrix");
        if (verbose) System.out.println("           X-offset: "+xOffs);
        if (verbose) System.out.println("           Y-offset: "+yOffs);
        if (verbose) System.out.println("           Z-offset: "+zOffs);

        TextureCalculator textureCalculator = new TextureCalculator();
        textureCalculator.calculate(inputImagePlus,xOffs,yOffs,zOffs);

        // Acquiring measurements
        HCMeasurement ASMMeasurement = new HCMeasurement("ASM",textureCalculator.getASM());
        ASMMeasurement.setSource(this);
        inputImage.addMeasurement(ASMMeasurement.getName(),ASMMeasurement);
        if (verbose) System.out.println("        ASM = "+ASMMeasurement.getValue());

        HCMeasurement contrastMeasurement = new HCMeasurement("CONTRAST",textureCalculator.getContrast());
        contrastMeasurement.setSource(this);
        inputImage.addMeasurement(contrastMeasurement.getName(),contrastMeasurement);
        if (verbose) System.out.println("        Contrast = "+contrastMeasurement.getValue());

        HCMeasurement correlationMeasurement = new HCMeasurement("CORRELATION",textureCalculator.getCorrelation());
        correlationMeasurement.setSource(this);
        inputImage.addMeasurement(correlationMeasurement.getName(),correlationMeasurement);
        if (verbose) System.out.println("        Correlation = "+correlationMeasurement.getValue());

        HCMeasurement entropyMeasurement = new HCMeasurement("ENTROPY",textureCalculator.getEntropy());
        entropyMeasurement.setSource(this);
        inputImage.addMeasurement(entropyMeasurement.getName(),entropyMeasurement);
        if (verbose) System.out.println("        Entropy = "+entropyMeasurement.getValue());

    }

    @Override
    public HCParameterCollection initialiseParameters() {
        HCParameterCollection parameters = new HCParameterCollection();

        parameters.addParameter(new HCParameter(this,MODULE_TITLE, HCParameter.MODULE_TITLE,"Image texture measurement",true));
        parameters.addParameter(new HCParameter(this,INPUT_IMAGE, HCParameter.INPUT_IMAGE,"Im1",false));
        parameters.addParameter(new HCParameter(this,X_OFFSET, HCParameter.INTEGER,1,true));
        parameters.addParameter(new HCParameter(this,Y_OFFSET, HCParameter.INTEGER,0,true));
        parameters.addParameter(new HCParameter(this,Z_OFFSET, HCParameter.INTEGER,0,true));

        return parameters;

    }

    @Override
    public HCParameterCollection getActiveParameters() {
        return parameters;
    }

    @Override
    public HCMeasurementCollection addActiveMeasurements() {
        return null;
    }
}
