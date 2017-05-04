package wbif.sjx.common.HighContent.Module;

import wbif.sjx.common.HighContent.Object.HCObject;
import wbif.sjx.common.HighContent.Object.ParameterCollection;
import wbif.sjx.common.HighContent.Object.Workspace;

import java.util.HashMap;

/**
 * Created by sc13967 on 04/05/2017.
 */
public class ObjectLinker implements Module {

    public void linkMatchingIDs(HashMap<Integer,HCObject> objects1, HashMap<Integer,HCObject> objects2) {
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
    public void execute(Workspace workspace) {

    }

    @Override
    public void initialiseParameters(ParameterCollection parameters) {

    }
}
