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
    public void execute(Workspace workspace) {
        ImageName imageName = (ImageName) workspace.getParameters().getParameter(this,DISPLAY_IMAGE).getValue();

        workspace.getImages().get(imageName).getImage().show();

    }

    @Override
    public void initialiseParameters(ParameterCollection parameters) {
        parameters.addParameter(new Parameter(this,Parameter.IMAGE_NAME,DISPLAY_IMAGE,null,false));

    }
}
