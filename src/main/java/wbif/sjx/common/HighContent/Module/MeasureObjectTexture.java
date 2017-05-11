package wbif.sjx.common.HighContent.Module;

import ij.ImagePlus;
import ij.measure.Calibration;
import wbif.sjx.common.Analysis.TextureCalculator;
import wbif.sjx.common.HighContent.Object.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Takes a set of objects and measures intensity texture values on a provided image.  Measurements are stored with the
 * objects.
 */
public class MeasureObjectTexture implements Module {
    public static final String INPUT_IMAGE = "Input image";
    public static final String INPUT_OBJECTS = "Input objects";
    public static final String MEASUREMENT_RADIUS = "Measurement radius";

    public static final String X_OFFSET = "X-offset";
    public static final String Y_OFFSET = "Y-offset";
    public static final String Z_OFFSET = "Z-offset";


    @Override
    public void execute(Workspace workspace, ParameterCollection parameters, boolean verbose) {
        if (verbose) System.out.println("   Running object texture analysis");

        // Getting parameters
        int xOffs = (int) parameters.getParameter(this,X_OFFSET).getValue();
        int yOffs = (int) parameters.getParameter(this,Y_OFFSET).getValue();
        int zOffs = (int) parameters.getParameter(this,Z_OFFSET).getValue();

        // Getting input image
        ImageName inputImageName = (ImageName) parameters.getParameter(this,INPUT_IMAGE).getValue();
        Image inputImage = workspace.getImages().get(inputImageName);
        ImagePlus inputImagePlus = inputImage.getImagePlus();

        // Getting input objects
        HCObjectName inputObjectsName = (HCObjectName) parameters.getParameter(this,INPUT_OBJECTS).getValue();
        HashMap<Integer,HCObject> inputObjects = workspace.getObjects().get(inputObjectsName);

        // Running texture measurement
        if (verbose) System.out.println("       Calculating co-occurance matrix");
        if (verbose) System.out.println("           X-offset: "+xOffs);
        if (verbose) System.out.println("           Y-offset: "+yOffs);
        if (verbose) System.out.println("           Z-offset: "+zOffs);

        TextureCalculator textureCalculator = new TextureCalculator();

        for (HCObject object:inputObjects.values()) {
            ArrayList<int[]> coords = new ArrayList<>();

            ArrayList<Integer> x = object.getCoordinates(HCObject.X);
            ArrayList<Integer> y = object.getCoordinates(HCObject.Y);
            ArrayList<Integer> z = object.getCoordinates(HCObject.Z);

            for (int i=0;i<x.size();i++) {
                coords.add(new int[]{x.get(i),y.get(i),z.get(i)});

            }

            textureCalculator.calculate(inputImagePlus,xOffs,yOffs,zOffs,coords);

            // Acquiring measurements
            Measurement ASMMeasurement = new Measurement(inputImageName.getName()+"_ASM",textureCalculator.getASM());
            ASMMeasurement.setSource(this);
            object.addMeasurement(ASMMeasurement.getName(),ASMMeasurement);
            if (verbose) System.out.println("        ASM = "+ASMMeasurement.getValue());

            Measurement contrastMeasurement = new Measurement(inputImageName.getName()+"CONTRAST",textureCalculator.getContrast());
            contrastMeasurement.setSource(this);
            object.addMeasurement(contrastMeasurement.getName(),contrastMeasurement);
            if (verbose) System.out.println("        Contrast = "+contrastMeasurement.getValue());

            Measurement correlationMeasurement = new Measurement(inputImageName.getName()+"CORRELATION",textureCalculator.getCorrelation());
            correlationMeasurement.setSource(this);
            object.addMeasurement(correlationMeasurement.getName(),correlationMeasurement);
            if (verbose) System.out.println("        Correlation = "+correlationMeasurement.getValue());

            Measurement entropyMeasurement = new Measurement(inputImageName.getName()+"ENTROPY",textureCalculator.getEntropy());
            entropyMeasurement.setSource(this);
            object.addMeasurement(entropyMeasurement.getName(),entropyMeasurement);
            if (verbose) System.out.println("        Entropy = "+entropyMeasurement.getValue());

        }
    }

    @Override
    public void initialiseParameters(ParameterCollection parameters) {
        parameters.addParameter(new Parameter(this,MODULE_TITLE,Parameter.MODULE_TITLE,"Object texture measurement",true));
        parameters.addParameter(new Parameter(this,INPUT_IMAGE,Parameter.IMAGE_NAME,"Im1",false));
        parameters.addParameter(new Parameter(this,INPUT_OBJECTS,Parameter.OBJECT_NAME,"Obj1",false));
        parameters.addParameter(new Parameter(this,MEASUREMENT_RADIUS,Parameter.NUMBER,5d,true));

        parameters.addParameter(new Parameter(this,X_OFFSET,Parameter.NUMBER,1d,true));
        parameters.addParameter(new Parameter(this,Y_OFFSET,Parameter.NUMBER,0d,true));
        parameters.addParameter(new Parameter(this,Z_OFFSET,Parameter.NUMBER,0d,true));

    }
}
