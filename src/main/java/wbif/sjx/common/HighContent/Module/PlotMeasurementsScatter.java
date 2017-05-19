package wbif.sjx.common.HighContent.Module;

import wbif.sjx.common.HighContent.Object.*;

/**
 * Created by sc13967 on 19/05/2017.
 */
public class PlotMeasurementsScatter extends HCModule {
    public static final String INPUT_OBJECTS = "Input objects";
    public static final String MEASUREMENT1 = "First measurement";
    public static final String MEASUREMENT2 = "Second measurement";

    @Override
    public void execute(HCWorkspace workspace, boolean verbose) {

    }

    @Override
    public HCParameterCollection initialiseParameters() {
        HCParameterCollection parameters = new HCParameterCollection();

        parameters.addParameter(new HCParameter(this,MODULE_TITLE,HCParameter.MODULE_TITLE,"Scatterplot measurements",true));
        parameters.addParameter(new HCParameter(this,INPUT_OBJECTS,HCParameter.INPUT_OBJECTS,null,false));
        parameters.addParameter(new HCParameter(this,MEASUREMENT1,HCParameter.MEASUREMENT,"",new String[]{"",""},true));
        parameters.addParameter(new HCParameter(this,MEASUREMENT2,HCParameter.MEASUREMENT,"",new String[]{"",""},true));

        return parameters;

    }

    @Override
    public HCParameterCollection getActiveParameters() {
        HCParameterCollection returnedParameters = new HCParameterCollection();
        returnedParameters.addParameter(parameters.getParameter(MODULE_TITLE));
        returnedParameters.addParameter(parameters.getParameter(INPUT_OBJECTS));
        returnedParameters.addParameter(parameters.getParameter(MEASUREMENT1));
        returnedParameters.addParameter(parameters.getParameter(MEASUREMENT2));

        // Updating measurements with measurement choices from currently-selected object
        HCName objectName = parameters.getValue(INPUT_OBJECTS);
        if (objectName != null) {
            parameters.updateValueRange(MEASUREMENT1, objectName);
            parameters.updateValueRange(MEASUREMENT2, objectName);

        }

        return returnedParameters;

    }

    @Override
    public HCMeasurementCollection addActiveMeasurements() {
        return null;

    }
}
