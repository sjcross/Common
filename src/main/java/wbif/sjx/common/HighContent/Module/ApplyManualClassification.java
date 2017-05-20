package wbif.sjx.common.HighContent.Module;

import wbif.sjx.common.HighContent.Object.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
        HCName inputObjectsName = parameters.getValue(INPUT_OBJECTS);
        HCObjectSet inputObjects = workspace.getObjects().get(inputObjectsName);

        // Getting classification file and storing classifications as HashMap that can be easily read later on
        String classificationFilePath = parameters.getValue(CLASSIFICATION_FILE);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(classificationFilePath));
            String line;
            while((line=bufferedReader.readLine())!=null){
                // Getting current object value
                String vals[] = line.split(",");
                int x = Integer.valueOf(vals[0]);
                int y = Integer.valueOf(vals[1]);
                int f = Integer.valueOf(vals[2]);
                int currClass = Integer.valueOf(vals[3]);

                for (HCObject object:inputObjects.values()) {
                    double xCent = MeasureObjectCentroid.calculateCentroid(object.getCoordinates(HCObject.X));
                    double yCent = MeasureObjectCentroid.calculateCentroid(object.getCoordinates(HCObject.Y));
                    if (xCent==x & yCent==y & object.getCoordinates(HCObject.T).equals(f)) {
                        HCMeasurement objClass = new HCMeasurement(HCMeasurement.CLASS,currClass);
                        objClass.setSource(this);
                        object.addMeasurement(objClass);

                        break;
                    }
                }
            }

            // Removing objects that don't have an assigned class
            inputObjects.entrySet().removeIf(entry -> entry.getValue().getMeasurement("CLASS") == null);

        } catch (IOException e) {
            e.printStackTrace();
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

    /**
     * Adds measurements from the current module to the measurement collection
     */
//    @Override
    public HCMeasurementCollection addActiveMeasurements() {
        HCMeasurementCollection measurements = new HCMeasurementCollection();

        measurements.addMeasurement(parameters.getValue(INPUT_OBJECTS),HCMeasurement.CLASS);

        return measurements;

    }
}
