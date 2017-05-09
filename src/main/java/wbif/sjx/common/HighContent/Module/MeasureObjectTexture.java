package wbif.sjx.common.HighContent.Module;

import wbif.sjx.common.HighContent.Object.*;

import java.util.HashMap;

/**
 * Takes a set of objects and measures intensity texture values on a provided image.  Measurements are stored with the
 * objects.
 */
public class MeasureObjectTexture implements Module {
    public static final String INPUT_IMAGE = "Input image";
    public static final String INPUT_OBJECTS = "Input objects";
    public static final String MEASUREMENT_RADIUS = "Measurement radius";


    @Override
    public void execute(Workspace workspace, ParameterCollection parameters, boolean verbose) {
        if (verbose) System.out.println("   Running texture analysis");

        // Getting input image
        ImageName inputImageName = (ImageName) parameters.getParameter(this,INPUT_IMAGE).getValue();
        Image inputImage = workspace.getImages().get(inputImageName);

        // Getting input objects
        HCObjectName inputObjectsName = (HCObjectName) parameters.getParameter(this,INPUT_OBJECTS).getValue();
        HashMap<Integer,HCObject> inputObjects = workspace.getObjects().get(inputObjectsName);
        
        // Could use JFeatureLib (https://github.com/locked-fg/JFeatureLib/blob/master/src/main/java/de/lmu/ifi/dbs/
        // jfeaturelib/features/Haralick.java); however, this appears to be calculated across the entire image (same for
        // other libraries I've seen).  Apparently implementing Haralick features isn't too hard


    }

    @Override
    public void initialiseParameters(ParameterCollection parameters) {
        parameters.addParameter(new Parameter(this,Parameter.MODULE_TITLE,MODULE_TITLE,"Object texture measurement",true));
        parameters.addParameter(new Parameter(this,Parameter.IMAGE_NAME,INPUT_IMAGE,"Im1",false));
        parameters.addParameter(new Parameter(this,Parameter.OBJECT_NAME,INPUT_OBJECTS,"Obj1",false));
        parameters.addParameter(new Parameter(this,Parameter.NUMBER,MEASUREMENT_RADIUS,5d,true));

    }
}
