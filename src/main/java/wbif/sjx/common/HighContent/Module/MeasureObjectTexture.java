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
    public static final String MEASUREMENT_RADIUS = "Measurement radius";
    public static final String CALIBRATED_RADIUS = "Calibrated radius";
    public static final String X_OFFSET = "X-offset";
    public static final String Y_OFFSET = "Y-offset";
    public static final String Z_OFFSET = "Z-offset";


    @Override
    public void execute(HCWorkspace workspace, boolean verbose) {
        if (verbose) System.out.println("   Running object texture analysis");

        // Getting parameters
        int xOffs = parameters.getValue(X_OFFSET);
        int yOffs = parameters.getValue(Y_OFFSET);
        int zOffs = parameters.getValue(Z_OFFSET);
        double radius = parameters.getValue(MEASUREMENT_RADIUS);
        boolean calibrated = parameters.getValue(CALIBRATED_RADIUS);

        // Getting input image
        HCImageName inputImageName = parameters.getValue(INPUT_IMAGE);
        HCImage inputImage = workspace.getImages().get(inputImageName);
        ImagePlus inputImagePlus = inputImage.getImagePlus();

        // Getting input objects
        HCObjectName inputObjectsName = parameters.getValue(INPUT_OBJECTS);
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
            HCSingleMeasurement ASMMeasurement = new HCSingleMeasurement(inputImageName.getName()+"_ASM",textureCalculator.getASM());
            ASMMeasurement.setSource(this);
            object.getParent().addSingleMeasurement(ASMMeasurement.getName(),ASMMeasurement);

            HCSingleMeasurement contrastMeasurement = new HCSingleMeasurement(inputImageName.getName()+"_CONTRAST",textureCalculator.getContrast());
            contrastMeasurement.setSource(this);
            object.getParent().addSingleMeasurement(contrastMeasurement.getName(),contrastMeasurement);

            HCSingleMeasurement correlationMeasurement = new HCSingleMeasurement(inputImageName.getName()+"_CORRELATION",textureCalculator.getCorrelation());
            correlationMeasurement.setSource(this);
            object.getParent().addSingleMeasurement(correlationMeasurement.getName(),correlationMeasurement);

            HCSingleMeasurement entropyMeasurement = new HCSingleMeasurement(inputImageName.getName()+"_ENTROPY",textureCalculator.getEntropy());
            entropyMeasurement.setSource(this);
            object.getParent().addSingleMeasurement(entropyMeasurement.getName(),entropyMeasurement);

        }

        if (verbose) System.out.println("\r        Measurements complete");

    }

    @Override
    public HCParameterCollection initialiseParameters() {
        HCParameterCollection parameters = new HCParameterCollection();

        parameters.addParameter(new HCParameter(this,MODULE_TITLE, HCParameter.MODULE_TITLE,"Object texture measurement",true));
        parameters.addParameter(new HCParameter(this,INPUT_IMAGE, HCParameter.INPUT_IMAGE,"Im1",false));
        parameters.addParameter(new HCParameter(this,INPUT_OBJECTS, HCParameter.INPUT_OBJECTS,"Obj1",false));
        parameters.addParameter(new HCParameter(this,CALIBRATED_RADIUS, HCParameter.BOOLEAN,false,true));
        parameters.addParameter(new HCParameter(this,MEASUREMENT_RADIUS, HCParameter.DOUBLE,10.0,true));
        parameters.addParameter(new HCParameter(this,X_OFFSET, HCParameter.INTEGER,1,true));
        parameters.addParameter(new HCParameter(this,Y_OFFSET, HCParameter.INTEGER,0,true));
        parameters.addParameter(new HCParameter(this,Z_OFFSET, HCParameter.INTEGER,0,true));

        return parameters;

    }

    @Override
    public HCParameterCollection getActiveParameters() {
        return parameters;
    }
}
