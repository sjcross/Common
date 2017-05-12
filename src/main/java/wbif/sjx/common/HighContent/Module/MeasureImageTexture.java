package wbif.sjx.common.HighContent.Module;

import ij.ImagePlus;
import wbif.sjx.common.Analysis.TextureCalculator;
import wbif.sjx.common.HighContent.Object.*;

/**
 * Created by Stephen on 09/05/2017.
 */
public class MeasureImageTexture implements Module {
    public static final String INPUT_IMAGE = "Input image";
    public static final String X_OFFSET = "X-offset";
    public static final String Y_OFFSET = "Y-offset";
    public static final String Z_OFFSET = "Z-offset";

    @Override
    public void execute(Workspace workspace, ParameterCollection parameters, boolean verbose) {
        if (verbose) System.out.println("   Running image texture analysis");

        // Getting parameters
        int xOffs = parameters.getValue(this,X_OFFSET);
        int yOffs = parameters.getValue(this,Y_OFFSET);
        int zOffs = parameters.getValue(this,Z_OFFSET);

        // Getting input image
        ImageName inputImageName = parameters.getValue(this,INPUT_IMAGE);
        Image inputImage = workspace.getImages().get(inputImageName);
        ImagePlus inputImagePlus = inputImage.getImagePlus();

        // Running texture measurement
        if (verbose) System.out.println("       Calculating co-occurance matrix");
        if (verbose) System.out.println("           X-offset: "+xOffs);
        if (verbose) System.out.println("           Y-offset: "+yOffs);
        if (verbose) System.out.println("           Z-offset: "+zOffs);

        TextureCalculator textureCalculator = new TextureCalculator();
        textureCalculator.calculate(inputImagePlus,xOffs,yOffs,zOffs);

        // Acquiring measurements
        Measurement ASMMeasurement = new Measurement("ASM",textureCalculator.getASM());
        ASMMeasurement.setSource(this);
        inputImage.addMeasurement(ASMMeasurement.getName(),ASMMeasurement);
        if (verbose) System.out.println("        ASM = "+ASMMeasurement.getValue());

        Measurement contrastMeasurement = new Measurement("CONTRAST",textureCalculator.getContrast());
        contrastMeasurement.setSource(this);
        inputImage.addMeasurement(contrastMeasurement.getName(),contrastMeasurement);
        if (verbose) System.out.println("        Contrast = "+contrastMeasurement.getValue());

        Measurement correlationMeasurement = new Measurement("CORRELATION",textureCalculator.getCorrelation());
        correlationMeasurement.setSource(this);
        inputImage.addMeasurement(correlationMeasurement.getName(),correlationMeasurement);
        if (verbose) System.out.println("        Correlation = "+correlationMeasurement.getValue());

        Measurement entropyMeasurement = new Measurement("ENTROPY",textureCalculator.getEntropy());
        entropyMeasurement.setSource(this);
        inputImage.addMeasurement(entropyMeasurement.getName(),entropyMeasurement);
        if (verbose) System.out.println("        Entropy = "+entropyMeasurement.getValue());

    }

    @Override
    public void initialiseParameters(ParameterCollection parameters) {
        parameters.addParameter(new Parameter(this,MODULE_TITLE,Parameter.MODULE_TITLE,"Image texture measurement",true));
        parameters.addParameter(new Parameter(this,INPUT_IMAGE,Parameter.IMAGE_NAME,"Im1",false));
        parameters.addParameter(new Parameter(this,X_OFFSET,Parameter.INTEGER,1,true));
        parameters.addParameter(new Parameter(this,Y_OFFSET,Parameter.INTEGER,0,true));
        parameters.addParameter(new Parameter(this,Z_OFFSET,Parameter.INTEGER,0,true));

    }
}
