package wbif.sjx.common.HighContent.Module;

import wbif.sjx.common.HighContent.Object.*;

/**
 * Created by sc13967 on 25/05/2017.
 */
public class TrainObjectClassifier extends HCModule {
    public static final String INPUT_OBJECTS = "Input objects";

    @Override
    public String getTitle() {
        return "Train object classifier";
    }

    @Override
    public void execute(HCWorkspace workspace, boolean verbose) {
        String moduleName = this.getClass().getSimpleName();
        if (verbose) System.out.println("["+moduleName+"] Initialising");

        // Getting input objects
        HCName inputObjectsName = parameters.getValue(INPUT_OBJECTS);
        HCObjectSet inputObjects = workspace.getObjects().get(inputObjectsName);



    }

    @Override
    public void initialiseParameters() {
        parameters.addParameter(new HCParameter(this,INPUT_OBJECTS,HCParameter.INPUT_OBJECTS,null));

    }

    @Override
    public HCParameterCollection getActiveParameters() {
        return parameters;
    }

    @Override
    public void addMeasurements(HCMeasurementCollection measurements) {

    }

    @Override
    public void addRelationships(HCRelationshipCollection relationships) {

    }
}
