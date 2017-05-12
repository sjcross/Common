package wbif.sjx.common.HighContent.Module;

import wbif.sjx.common.HighContent.Object.*;


/**
 * Created by sc13967 on 04/05/2017.
 */
public class ObjectLinker implements Module {
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
    public void execute(Workspace workspace, ParameterCollection parameters, boolean verbose) {
        HCObjectName objectName1 = parameters.getValue(this,INPUT_OBJECTS1);
        HCObjectName objectName2 = parameters.getValue(this,INPUT_OBJECTS2);

        HCObjectSet objects1 = workspace.getObjects().get(objectName1);
        HCObjectSet objects2 = workspace.getObjects().get(objectName2);

        linkMatchingIDs(objects1,objects2);

    }

    @Override
    public void initialiseParameters(ParameterCollection parameters) {
        parameters.addParameter(new Parameter(this,MODULE_TITLE,Parameter.MODULE_TITLE,"Object linker",true));
        parameters.addParameter(new Parameter(this,INPUT_OBJECTS1,Parameter.OBJECT_NAME,null,false));
        parameters.addParameter(new Parameter(this,INPUT_OBJECTS2,Parameter.OBJECT_NAME,null,false));

    }
}

