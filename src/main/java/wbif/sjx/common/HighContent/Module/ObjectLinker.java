package wbif.sjx.common.HighContent.Module;

import wbif.sjx.common.HighContent.Object.*;


/**
 * Created by sc13967 on 04/05/2017.
 */
public class ObjectLinker extends HCModule {
    public final static String INPUT_OBJECTS1 = "Input objects 1";
    public final static String INPUT_OBJECTS2 = "Input objects 2";

    public void linkMatchingIDs(HCObjectSet objects1, HCObjectSet objects2) {
        for (HCObject object1:objects1.values()) {
            int ID = object1.getID();

            HCObject object2 = objects2.get(ID);

            if (object2 != null) {
                object1.addChild(object2);
                object2.setParent(object1);
            }

        }
    }

    @Override
    public String getTitle() {
        return "Link objects";

    }

    @Override
    public void execute(HCWorkspace workspace, boolean verbose) {
        HCName objectName1 = parameters.getValue(INPUT_OBJECTS1);
        HCName objectName2 = parameters.getValue(INPUT_OBJECTS2);

        HCObjectSet objects1 = workspace.getObjects().get(objectName1);
        HCObjectSet objects2 = workspace.getObjects().get(objectName2);

        linkMatchingIDs(objects1,objects2);

    }

    @Override
    public HCParameterCollection initialiseParameters() {
        HCParameterCollection parameters = new HCParameterCollection();

        parameters.addParameter(new HCParameter(this,INPUT_OBJECTS1, HCParameter.INPUT_OBJECTS,null));
        parameters.addParameter(new HCParameter(this,INPUT_OBJECTS2, HCParameter.INPUT_OBJECTS,null));

        return parameters;

    }

    @Override
    public HCParameterCollection getActiveParameters() {
        return parameters;
    }

    @Override
    public HCMeasurementCollection addActiveMeasurements() {
        return null;
    }
}

