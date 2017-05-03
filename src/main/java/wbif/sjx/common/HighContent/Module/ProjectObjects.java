package wbif.sjx.common.HighContent.Module;

import wbif.sjx.common.HighContent.Object.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Projects xy coordinates into a single plane.  Duplicates of xy coordinates at different heights are removed.
 */
public class ProjectObjects implements Module {
    public static final String INPUT_OBJECTS = "Input objects";
    public static final String OUTPUT_OBJECTS = "Output objects";


    @Override
    public void execute(Workspace workspace) {
        ParameterCollection parameters = workspace.getParameters();
        HCObjectName inputObjectsName = (HCObjectName) parameters.getParameter(this,INPUT_OBJECTS);
        HCObjectName outputObjectsName = (HCObjectName) parameters.getParameter(this,OUTPUT_OBJECTS);

        ArrayList<HCObject> inputObjects = workspace.getObjects().get(inputObjectsName);
        ArrayList<HCObject> outputObjects = new ArrayList<>();

        int objID = 1;

        for (HCObject inputObject:inputObjects) {
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
            HashMap<Double, Integer> projCoords = new HashMap<Double, Integer>();
            for (int i = 0; i < x.size(); i++) {
                Double key = y.get(i) * maxX + x.get(i);
                projCoords.put(key, i);
            }

            // Creating the new HCObject and assigning the parent-child relationship
            HCObject outputObject = new HCObject(objID++);
            outputObject.setParent(inputObject);
            inputObject.addChild(outputObject);

            // Adding coordinates to the projected object
            for (Double key : projCoords.keySet()) {
                int i = projCoords.get(key);
                outputObject.addCoordinate(HCObject.X,x.get(i));
                outputObject.addCoordinate(HCObject.Y,y.get(i));
            }

            outputObjects.add(outputObject);

        }

        workspace.addObjects(outputObjectsName,outputObjects);

    }

    @Override
    public void initialiseParameters(ParameterCollection parameters) {
        parameters.addParameter(this,INPUT_OBJECTS,"Obj1",false);
        parameters.addParameter(this,OUTPUT_OBJECTS,"Obj2",false);

    }
}
