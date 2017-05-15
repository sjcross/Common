package wbif.sjx.common.HighContent.Module;

import wbif.sjx.common.HighContent.Object.*;
import wbif.sjx.common.HighContent.Object.ParameterCollection;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Projects xy coordinates into a single plane.  Duplicates of xy coordinates at different heights are removed.
 */
public class ProjectObjects implements Module {
    public static final String INPUT_OBJECTS = "Input objects";
    public static final String OUTPUT_OBJECTS = "Output objects";


    @Override
    public void execute(Workspace workspace,ParameterCollection parameters, boolean verbose) {
        HCObjectName inputObjectsName = parameters.getValue(this,INPUT_OBJECTS);
        HCObjectName outputObjectsName = parameters.getValue(this,OUTPUT_OBJECTS);

        HCObjectSet inputObjects = workspace.getObjects().get(inputObjectsName);
        HCObjectSet outputObjects = new HCObjectSet();

        for (HCObject inputObject:inputObjects.values()) {
            ArrayList<Integer> x = inputObject.getCoordinates().get(HCObject.X);
            ArrayList<Integer> y = inputObject.getCoordinates().get(HCObject.Y);

            // All coordinate pairs will be stored in a HashMap, which will prevent coordinate duplication.  The keys will
            // correspond to the 2D index, for which we need to know the maximum x coordinate
            double maxX = Double.MIN_VALUE;
            for (double currX : x) {
                if (currX > maxX) {
                    maxX = currX;
                }
            }

            // Running through all coordinates, adding them to the HashMap
            HashMap<Double, Integer> projCoords = new HashMap<>();
            for (int i = 0; i < x.size(); i++) {
                Double key = y.get(i) * maxX + x.get(i);
                projCoords.put(key, i);
            }

            // Creating the new HCObject and assigning the parent-child relationship
            HCObject outputObject = new HCObject(inputObject.getID());
            outputObject.setParent(inputObject);
            inputObject.addChild(outputObject);

            // Adding coordinates to the projected object
            for (Double key : projCoords.keySet()) {
                int i = projCoords.get(key);
                outputObject.addCoordinate(HCObject.X,x.get(i));
                outputObject.addCoordinate(HCObject.Y,y.get(i));
            }

            // Inheriting calibration from parent
            outputObject.addCalibration(HCObject.X,outputObject.getParent().getCalibration(HCObject.X));
            outputObject.addCalibration(HCObject.Y,outputObject.getParent().getCalibration(HCObject.Y));
            outputObject.setCalibratedUnits(outputObject.getParent().getCalibratedUnits());

            // Adding current object to object set
            outputObjects.put(outputObject.getID(),outputObject);

        }

        workspace.addObjects(outputObjectsName,outputObjects);

    }

    @Override
    public ParameterCollection initialiseParameters() {
        ParameterCollection parameters = new ParameterCollection();

        parameters.addParameter(new Parameter(this,MODULE_TITLE,Parameter.MODULE_TITLE,"Object projector",false));
        parameters.addParameter(new Parameter(this,INPUT_OBJECTS,Parameter.OBJECT_NAME,null,false));
        parameters.addParameter(new Parameter(this,OUTPUT_OBJECTS,Parameter.OBJECT_NAME,null,false));

        return parameters;

    }
}
