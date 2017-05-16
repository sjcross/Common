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
    public void execute(HCWorkspace workspace, boolean verbose) {
        HCObjectName objectName1 = parameters.getValue(INPUT_OBJECTS1);
        HCObjectName objectName2 = parameters.getValue(INPUT_OBJECTS2);

        HCObjectSet objects1 = workspace.getObjects().get(objectName1);
        HCObjectSet objects2 = workspace.getObjects().get(objectName2);

        linkMatchingIDs(objects1,objects2);

    }

    @Override
    public HCParameterCollection initialiseParameters() {
        HCParameterCollection parameters = new HCParameterCollection();

        parameters.addParameter(new HCParameter(this,MODULE_TITLE, HCParameter.MODULE_TITLE,"Object linker",true));
        parameters.addParameter(new HCParameter(this,INPUT_OBJECTS1, HCParameter.INPUT_OBJECTS,null,false));
        parameters.addParameter(new HCParameter(this,INPUT_OBJECTS2, HCParameter.INPUT_OBJECTS,null,false));

        return parameters;

    }

    @Override
    public HCParameterCollection getActiveParameters() {
        return parameters;
    }
}

