package wbif.sjx.common.HighContent.Module;

import wbif.sjx.common.HighContent.Module.HCModule;
import wbif.sjx.common.HighContent.Object.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Applies manual object classifications from a .csv file at the specified location.  Each row of the file must
 * correspond to a different object and have the format [ID],[Classification]
 */
public class ApplyManualClassification extends HCModule {
    public static final String INPUT_OBJECTS = "Input objects";
    public static final String CLASSIFICATION_FILE = "Classification file";

    @Override
    public void execute(HCWorkspace workspace, boolean verbose) {
        if (verbose) System.out.println("   Applying manual classifications");

        // Getting input objects
        HCObjectName inputObjectsName = parameters.getValue(INPUT_OBJECTS);
        HCObjectSet inputObjects = workspace.getObjects().get(inputObjectsName);

        // Getting classification file and storing classifications as HashMap that can be easily read later on
        HashMap<Integer,Integer> classification = new HashMap<>();
        String classificationFilePath = parameters.getValue(CLASSIFICATION_FILE);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(classificationFilePath));
            String line;
            while((line=bufferedReader.readLine())!=null){
                String vals[] = line.split(",");
                classification.put(Integer.valueOf(vals[0]),Integer.valueOf(vals[1]));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Running through each object, applying the relevant classification based on its groupID
        for (HCObject object:inputObjects.values()) {
            HCMeasurement objClass = new HCMeasurement("CLASS",classification.get(object.getGroupID()));
            objClass.setSource(this);
            object.addMeasurement(objClass);

        }
    }

    @Override
    public HCParameterCollection initialiseParameters() {
        HCParameterCollection parameters = new HCParameterCollection();

        parameters.addParameter(new HCParameter(this,MODULE_TITLE,HCParameter.MODULE_TITLE,"Apply manual classification",false));
        parameters.addParameter(new HCParameter(this,INPUT_OBJECTS,HCParameter.INPUT_OBJECTS,null,false));
        parameters.addParameter(new HCParameter(this,CLASSIFICATION_FILE,HCParameter.FILE_PATH,null,false));

        return parameters;

    }

    @Override
    public HCParameterCollection getActiveParameters() {
        return parameters;

    }
}
