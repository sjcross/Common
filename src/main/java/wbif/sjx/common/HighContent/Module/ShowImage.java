package wbif.sjx.common.HighContent.Module;

import wbif.sjx.common.HighContent.Object.*;

/**
 * Created by sc13967 on 03/05/2017.
 */
public class ShowImage extends HCModule {
    public final static String DISPLAY_IMAGE = "Display image";

    @Override
    public void execute(HCWorkspace workspace, boolean verbose) {
        HCName imageName = parameters.getValue(DISPLAY_IMAGE);

        workspace.getImages().get(imageName).getImagePlus().show();

    }

    @Override
    public HCParameterCollection initialiseParameters() {
        HCParameterCollection parameters = new HCParameterCollection();

        parameters.addParameter(new HCParameter(this,MODULE_TITLE, HCParameter.MODULE_TITLE,"Show images",false));
        parameters.addParameter(new HCParameter(this,DISPLAY_IMAGE, HCParameter.INPUT_IMAGE,null,false));

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
