package wbif.sjx.common.HighContent.Module;

import wbif.sjx.common.HighContent.Object.ImageName;
import wbif.sjx.common.HighContent.Object.Parameter;
import wbif.sjx.common.HighContent.Object.ParameterCollection;
import wbif.sjx.common.HighContent.Object.Workspace;

/**
 * Created by sc13967 on 03/05/2017.
 */
public class ShowImage implements Module {
    public static final String MODULE_TITLE = "Module title";
    public final static String DISPLAY_IMAGE = "Display image";

    @Override
    public void execute(Workspace workspace,ParameterCollection parameters, boolean verbose) {
        ImageName imageName = (ImageName) parameters.getParameter(this,DISPLAY_IMAGE).getValue();

        workspace.getImages().get(imageName).getImagePlus().show();

    }

    @Override
    public void initialiseParameters(ParameterCollection parameters) {
        parameters.addParameter(new Parameter(this,Parameter.MODULE_TITLE,MODULE_TITLE,"Show images",true));
        parameters.addParameter(new Parameter(this,Parameter.IMAGE_NAME,DISPLAY_IMAGE,null,false));

    }
}
