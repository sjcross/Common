package wbif.sjx.common.HighContent.Module;

import ij.ImagePlus;
import wbif.sjx.common.Analysis.TextureCalculator;
import wbif.sjx.common.HighContent.Object.*;

import java.util.ArrayList;

/**
 * Takes a set of objects and measures intensity texture values on a provided image.  Measurements are stored with the
 * objects.
 */
public class MeasureObjectTexture implements Module {
    public static final String INPUT_IMAGE = "Input image";
    public static final String INPUT_OBJECTS = "Input objects";
    public static final String MEASUREMENT_RADIUS = "Measurement radius";
    public static final String CALIBRATED_RADIUS = "Calibrated radius";
    public static final String X_OFFSET = "X-offset";
    public static final String Y_OFFSET = "Y-offset";
    public static final String Z_OFFSET = "Z-offset";


    @Override
    public void execute(Workspace workspace, ParameterCollection parameters, boolean verbose) {
        if (verbose) System.out.println("   Running object texture analysis");

        // Getting parameters
        int xOffs = parameters.getValue(this,X_OFFSET);
        int yOffs = parameters.getValue(this,Y_OFFSET);
        int zOffs = parameters.getValue(this,Z_OFFSET);
        double radius = parameters.getValue(this,MEASUREMENT_RADIUS);
        boolean calibrated = parameters.getValue(this,CALIBRATED_RADIUS);

        // Getting input image
        ImageName inputImageName = parameters.getValue(this,INPUT_IMAGE);
        Image inputImage = workspace.getImages().get(inputImageName);
        ImagePlus inputImagePlus = inputImage.getImagePlus();

        // Getting input objects
        HCObjectName inputObjectsName = parameters.getValue(this,INPUT_OBJECTS);
        HCObjectSet inputObjects = workspace.getObjects().get(inputObjectsName);

        // Getting local object region
        HCObjectSet inputObjectsRegion = GetLocalObjectRegion.getLocalRegions(inputObjects,radius,calibrated);

        // Running texture measurement
        if (verbose) System.out.println("       Calculating co-occurance matrix");
        if (verbose) System.out.println("           X-offset: "+xOffs);
        if (verbose) System.out.println("           Y-offset: "+yOffs);
        if (verbose) System.out.println("           Z-offset: "+zOffs);

        TextureCalculator textureCalculator = new TextureCalculator();

        int nObjects = inputObjectsRegion.size();
        int iter = 1;
        if (verbose) System.out.println("        Initialising measurements");
        for (HCObject object:inputObjectsRegion.values()) {
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
            Measurement ASMMeasurement = new Measurement(inputImageName.getName()+"_ASM",textureCalculator.getASM());
            ASMMeasurement.setSource(this);
            object.getParent().addMeasurement(ASMMeasurement.getName(),ASMMeasurement);

            Measurement contrastMeasurement = new Measurement(inputImageName.getName()+"_CONTRAST",textureCalculator.getContrast());
            contrastMeasurement.setSource(this);
            object.getParent().addMeasurement(contrastMeasurement.getName(),contrastMeasurement);

            Measurement correlationMeasurement = new Measurement(inputImageName.getName()+"_CORRELATION",textureCalculator.getCorrelation());
            correlationMeasurement.setSource(this);
            object.getParent().addMeasurement(correlationMeasurement.getName(),correlationMeasurement);

            Measurement entropyMeasurement = new Measurement(inputImageName.getName()+"_ENTROPY",textureCalculator.getEntropy());
            entropyMeasurement.setSource(this);
            object.getParent().addMeasurement(entropyMeasurement.getName(),entropyMeasurement);

        }

        if (verbose) System.out.println("\r        Measurements complete");

    }

    @Override
    public void initialiseParameters(ParameterCollection parameters) {
        parameters.addParameter(new Parameter(this,MODULE_TITLE,Parameter.MODULE_TITLE,"Object texture measurement",true));
        parameters.addParameter(new Parameter(this,INPUT_IMAGE,Parameter.IMAGE_NAME,"Im1",false));
        parameters.addParameter(new Parameter(this,INPUT_OBJECTS,Parameter.OBJECT_NAME,"Obj1",false));
        parameters.addParameter(new Parameter(this,CALIBRATED_RADIUS,Parameter.BOOLEAN,false,true));
        parameters.addParameter(new Parameter(this,MEASUREMENT_RADIUS,Parameter.DOUBLE,10.0,true));
        parameters.addParameter(new Parameter(this,X_OFFSET,Parameter.INTEGER,1,true));
        parameters.addParameter(new Parameter(this,Y_OFFSET,Parameter.INTEGER,0,true));
        parameters.addParameter(new Parameter(this,Z_OFFSET,Parameter.INTEGER,0,true));

    }
}
