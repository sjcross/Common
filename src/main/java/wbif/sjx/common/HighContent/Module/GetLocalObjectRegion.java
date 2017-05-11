package wbif.sjx.common.HighContent.Module;

import ij.ImagePlus;
import ij.measure.Calibration;
import wbif.sjx.common.HighContent.Object.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Returns a spherical object around a point object.  This is useful for calculating local object features.
 */
public class GetLocalObjectRegion implements Module {
    public static final String INPUT_OBJECTS = "Input objects";
    public static final String OUTPUT_OBJECTS = "Output objects";
    public static final String LOCAL_RADIUS = "Local radius";

    @Override
    public void execute(Workspace workspace, ParameterCollection parameters, boolean verbose) {
        if (verbose) System.out.println("   Calculating local volume around object centroids");

        // Getting input objects
        HCObjectName inputObjectsName = (HCObjectName) parameters.getParameter(this,INPUT_OBJECTS).getValue();
        HashMap<Integer,HCObject> inputObjects = workspace.getObjects().get(inputObjectsName);

        // Getting output objects name
        HCObjectName outputObjectsName = (HCObjectName) parameters.getParameter(this,OUTPUT_OBJECTS).getValue();

        // Getting parameters
        double radius = (double) parameters.getParameter(this,LOCAL_RADIUS).getValue();
        if (verbose) System.out.println("       Using local radius of "+radius+" px");

        // Creating store for output objects
        HashMap<Integer,HCObject> outputObjects = new HashMap<>();

        // Running through each object, calculating the local texture
        for (HCObject inputObject:inputObjects.values()) {
            // Creating new object and assigning relationship to input objects
            HCObject outputObject = new HCObject(inputObject.getID());
            outputObject.setParent(inputObject);
            inputObject.addChild(outputObject);

            // Getting image calibration (to deal with different xy-z dimensions)
            double xy_z_ratio = inputObject.getCalibration(HCObject.X)/inputObject.getCalibration(HCObject.Z);

            // Getting centroid coordinates
            double xCent = MeasureObjectCentroid.calculateCentroid(inputObject.getCoordinates(HCObject.X),MeasureObjectCentroid.MEAN);
            double yCent = MeasureObjectCentroid.calculateCentroid(inputObject.getCoordinates(HCObject.Y),MeasureObjectCentroid.MEAN);
            double zCent = MeasureObjectCentroid.calculateCentroid(inputObject.getCoordinates(HCObject.Z),MeasureObjectCentroid.MEAN);

            for (int x = (int) Math.floor(xCent-radius); x <= (int) Math.ceil(xCent+radius) ; x++) {
                for (int y = (int) Math.floor(yCent-radius); y <= (int) Math.ceil(yCent+radius) ; y++) {
                    for (int z = (int) Math.floor(zCent-radius*xy_z_ratio); z <= (int) Math.ceil(zCent+radius*xy_z_ratio) ; z++) {
                        outputObject.addCoordinate(HCObject.X,x);
                        outputObject.addCoordinate(HCObject.Y,y);
                        outputObject.addCoordinate(HCObject.Z,z);

                    }
                }
            }

            // Adding object to HashMap
            outputObjects.put(outputObject.getID(),outputObject);

        }

        // Adding output objects to workspace
        workspace.addObjects(outputObjectsName,outputObjects);
        if (verbose) System.out.println("       Adding objects ("+outputObjectsName+") to workspace");

    }

    @Override
    public void initialiseParameters(ParameterCollection parameters) {
        parameters.addParameter(new Parameter(this,INPUT_OBJECTS,Parameter.OBJECT_NAME,"Obj1",false));
        parameters.addParameter(new Parameter(this,OUTPUT_OBJECTS,Parameter.OBJECT_NAME,"Obj2",false));
        parameters.addParameter(new Parameter(this,LOCAL_RADIUS,Parameter.NUMBER,10,true));

    }
}
