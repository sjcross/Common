package wbif.sjx.common.HighContent.Module;

import ij.ImagePlus;
import wbif.sjx.common.Analysis.TextureCalculator;
import wbif.sjx.common.HighContent.Object.*;

/**
 * Created by Stephen on 09/05/2017.
 */
public class MeasureImageTexture implements Module {
    public static final String INPUT_IMAGE = "Input image";
    public static final String MEASUREMENT_RADIUS = "Measurement radius";
    public static final String X_OFFSET = "X-offset";
    public static final String Y_OFFSET = "Y-offset";
    public static final String Z_OFFSET = "Z-offset";


    @Override
    public void execute(Workspace workspace, ParameterCollection parameters, boolean verbose) {
        if (verbose) System.out.println("   Running image texture analysis");

        // Getting parameters
        int xOffs = (int) parameters.getParameter(this,X_OFFSET).getValue();
        int yOffs = (int) parameters.getParameter(this,Y_OFFSET).getValue();
        int zOffs = (int) parameters.getParameter(this,Z_OFFSET).getValue();

        // Getting input image
        ImageName inputImageName = (ImageName) parameters.getParameter(this,INPUT_IMAGE).getValue();
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

        Measurement contrastMeasurement = new Measurement("Contrast",textureCalculator.getContrast());
        contrastMeasurement.setSource(this);
        inputImage.addMeasurement(contrastMeasurement.getName(),contrastMeasurement);
        if (verbose) System.out.println("        Contrast = "+contrastMeasurement.getValue());

        Measurement correlationMeasurement = new Measurement("Correlation",textureCalculator.getCorrelation());
        contrastMeasurement.setSource(this);
        inputImage.addMeasurement(correlationMeasurement.getName(),correlationMeasurement);
        if (verbose) System.out.println("        Correlation = "+correlationMeasurement.getValue());

    }

    @Override
    public void initialiseParameters(ParameterCollection parameters) {
        parameters.addParameter(new Parameter(this,Parameter.MODULE_TITLE,MODULE_TITLE,"Object texture measurement",true));
        parameters.addParameter(new Parameter(this,Parameter.IMAGE_NAME,INPUT_IMAGE,"Im1",false));
        parameters.addParameter(new Parameter(this,Parameter.NUMBER,MEASUREMENT_RADIUS,5d,true));
        parameters.addParameter(new Parameter(this,Parameter.NUMBER,X_OFFSET,1d,true));
        parameters.addParameter(new Parameter(this,Parameter.NUMBER,Y_OFFSET,0d,true));
        parameters.addParameter(new Parameter(this,Parameter.NUMBER,Z_OFFSET,0d,true));

    }
}
