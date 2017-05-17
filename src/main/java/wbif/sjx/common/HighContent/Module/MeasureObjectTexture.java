package wbif.sjx.common.HighContent.Module;

import ij.ImagePlus;
import wbif.sjx.common.Analysis.TextureCalculator;
import wbif.sjx.common.HighContent.Object.*;

import java.util.ArrayList;

/**
 * Takes a set of objects and measures intensity texture values on a provided image.  Measurements are stored with the
 * objects.
 */
public class MeasureObjectTexture extends HCModule {
    public static final String INPUT_IMAGE = "Input image";
    public static final String INPUT_OBJECTS = "Input objects";
    public static final String POINT_MEASUREMENT = "Measurements based on centroid point";
    public static final String MEASUREMENT_RADIUS = "Measurement radius";
    public static final String CALIBRATED_RADIUS = "Calibrated radius";
    public static final String X_OFFSET = "X-offset";
    public static final String Y_OFFSET = "Y-offset";
    public static final String Z_OFFSET = "Z-offset";


    @Override
    public void execute(HCWorkspace workspace, boolean verbose) {
        if (verbose) System.out.println("   Running object texture analysis");

        // Getting input image
        HCImageName inputImageName = parameters.getValue(INPUT_IMAGE);
        HCImage inputImage = workspace.getImages().get(inputImageName);
        ImagePlus inputImagePlus = inputImage.getImagePlus();

        // Getting input objects
        HCObjectName inputObjectsName = parameters.getValue(INPUT_OBJECTS);
        HCObjectSet inputObjects = workspace.getObjects().get(inputObjectsName);

        // Getting parameters
        int xOffs = parameters.getValue(X_OFFSET);
        int yOffs = parameters.getValue(Y_OFFSET);
        int zOffs = parameters.getValue(Z_OFFSET);
        boolean centroidMeasurement = parameters.getValue(POINT_MEASUREMENT);

        // If a centroid region is being used calculate the local region and reassign that to inputObjects reference
        if (centroidMeasurement) {
            double radius = parameters.getValue(MEASUREMENT_RADIUS);
            boolean calibrated = parameters.getValue(CALIBRATED_RADIUS);

            // Getting local object region
            inputObjects = GetLocalObjectRegion.getLocalRegions(inputObjects, radius, calibrated);

        }

        // Running texture measurement
        if (verbose) System.out.println("       Calculating co-occurance matrix");
        if (verbose) System.out.println("           X-offset: "+xOffs);
        if (verbose) System.out.println("           Y-offset: "+yOffs);
        if (verbose) System.out.println("           Z-offset: "+zOffs);

        TextureCalculator textureCalculator = new TextureCalculator();

        int nObjects = inputObjects.size();
        int iter = 1;
        if (verbose) System.out.println("        Initialising measurements");
        for (HCObject object:inputObjects.values()) {
            if (verbose) System.out.print("\r            Processing object "+(iter++)+" of "+nObjects);
            ArrayList<int[]> coords = new ArrayList<>();

            ArrayList<Integer> x = object.getCoordinates(HCObject.X);
            ArrayList<Integer> y = object.getCoordinates(HCObject.Y);
            ArrayList<Integer> z = object.getCoordinates(HCObject.Z);

            for (int i=0;i<x.size();i++) {
                coords.add(new int[]{x.get(i),y.get(i),z.get(i)});

            }

            textureCalculator.calculate(inputImagePlus,xOffs,yOffs,zOffs,coords);

            // Acquiring measurements
            HCMeasurement ASMMeasurement = new HCMeasurement(inputImageName.getName()+"_ASM",textureCalculator.getASM());
            ASMMeasurement.setSource(this);
            if (centroidMeasurement) {
                object.getParent().addMeasurement(ASMMeasurement);
            } else {
                object.addMeasurement(ASMMeasurement);
            }

            HCMeasurement contrastMeasurement = new HCMeasurement(inputImageName.getName()+"_CONTRAST",textureCalculator.getContrast());
            contrastMeasurement.setSource(this);
            if (centroidMeasurement) {
                object.getParent().addMeasurement(contrastMeasurement);
            } else {
                object.addMeasurement(contrastMeasurement);
            }

            HCMeasurement correlationMeasurement = new HCMeasurement(inputImageName.getName()+"_CORRELATION",textureCalculator.getCorrelation());
            correlationMeasurement.setSource(this);
            if (centroidMeasurement) {
                object.getParent().addMeasurement(correlationMeasurement);
            } else {
                object.addMeasurement(correlationMeasurement);
            }

            HCMeasurement entropyMeasurement = new HCMeasurement(inputImageName.getName()+"_ENTROPY",textureCalculator.getEntropy());
            entropyMeasurement.setSource(this);
            if (centroidMeasurement) {
                object.getParent().addMeasurement(entropyMeasurement);
            } else {
                object.addMeasurement(entropyMeasurement);
            }

        }

        if (verbose) System.out.println("\r        Measurements complete");

    }

    @Override
    public HCParameterCollection initialiseParameters() {
        HCParameterCollection parameters = new HCParameterCollection();

        parameters.addParameter(new HCParameter(this,MODULE_TITLE, HCParameter.MODULE_TITLE,"Object texture measurement",true));
        parameters.addParameter(new HCParameter(this,INPUT_IMAGE, HCParameter.INPUT_IMAGE,"Im1",false));
        parameters.addParameter(new HCParameter(this,INPUT_OBJECTS, HCParameter.INPUT_OBJECTS,"Obj1",false));
        parameters.addParameter(new HCParameter(this,POINT_MEASUREMENT,HCParameter.BOOLEAN,false,true));
        parameters.addParameter(new HCParameter(this,CALIBRATED_RADIUS, HCParameter.BOOLEAN,false,true));
        parameters.addParameter(new HCParameter(this,MEASUREMENT_RADIUS, HCParameter.DOUBLE,10.0,true));
        parameters.addParameter(new HCParameter(this,X_OFFSET, HCParameter.INTEGER,1,true));
        parameters.addParameter(new HCParameter(this,Y_OFFSET, HCParameter.INTEGER,0,true));
        parameters.addParameter(new HCParameter(this,Z_OFFSET, HCParameter.INTEGER,0,true));

        return parameters;

    }

    @Override
    public HCParameterCollection getActiveParameters() {
        HCParameterCollection returnedParameters = new HCParameterCollection();
        returnedParameters.addParameter(parameters.getParameter(MODULE_TITLE));
        returnedParameters.addParameter(parameters.getParameter(INPUT_IMAGE));
        returnedParameters.addParameter(parameters.getParameter(INPUT_OBJECTS));
        returnedParameters.addParameter(parameters.getParameter(POINT_MEASUREMENT));

        if (parameters.getValue(POINT_MEASUREMENT)) {
            returnedParameters.addParameter(parameters.getParameter(CALIBRATED_RADIUS));
            returnedParameters.addParameter(parameters.getParameter(MEASUREMENT_RADIUS));
        }

        returnedParameters.addParameter(parameters.getParameter(X_OFFSET));
        returnedParameters.addParameter(parameters.getParameter(Y_OFFSET));
        returnedParameters.addParameter(parameters.getParameter(Z_OFFSET));

        return returnedParameters;
    }
}
