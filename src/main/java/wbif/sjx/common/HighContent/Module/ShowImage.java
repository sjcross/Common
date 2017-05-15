package wbif.sjx.common.HighContent.Module;

import wbif.sjx.common.HighContent.Object.ImageName;
import wbif.sjx.common.HighContent.Object.Parameter;
import wbif.sjx.common.HighContent.Object.ParameterCollection;
import wbif.sjx.common.HighContent.Object.Workspace;

/**
 * Created by sc13967 on 03/05/2017.
 */
public class ShowImage implements Module {
    public final static String DISPLAY_IMAGE = "Display image";

    @Override
    public void execute(Workspace workspace,ParameterCollection parameters, boolean verbose) {
        ImageName imageName = parameters.getValue(this,DISPLAY_IMAGE);

        workspace.getImages().get(imageName).getImagePlus().show();

    }

    @Override
    public ParameterCollection initialiseParameters() {
        ParameterCollection parameters = new ParameterCollection();

        parameters.addParameter(new Parameter(this,MODULE_TITLE,Parameter.MODULE_TITLE,"Show images",false));
        parameters.addParameter(new Parameter(this,DISPLAY_IMAGE,Parameter.IMAGE_NAME,null,false));

        return parameters;

    }
}
