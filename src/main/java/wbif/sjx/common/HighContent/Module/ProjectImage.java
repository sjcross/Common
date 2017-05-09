package wbif.sjx.common.HighContent.Module;

import wbif.sjx.common.HighContent.Object.*;
import wbif.sjx.common.HighContent.Object.ParameterCollection;

/**
 * Created by sc13967 on 04/05/2017.
 */
public class ProjectImage implements Module {
    public static final String INPUT_IMAGE = "Input image";
    public static final String OUTPUT_IMAGE = "Output image";

    @Override
    public void execute(Workspace workspace,ParameterCollection parameters, boolean verbose) {
        // Loading image into workspace
        ImageName inputImageName = (ImageName) parameters.getParameter(this,INPUT_IMAGE).getValue();
        Image inputImage = workspace.getImages().get(inputImageName);

        // Getting output image name
        ImageName outputImageName = (ImageName) parameters.getParameter(this,OUTPUT_IMAGE).getValue();

        // Create max projection image
        Image outputImage = inputImage.projectImageInZ();

        // Adding projected image to workspace
        workspace.addImage(outputImageName,outputImage);

    }

    @Override
    public void initialiseParameters(ParameterCollection parameters) {
        parameters.addParameter(new Parameter(this,Parameter.MODULE_TITLE,MODULE_TITLE,"Image projector",true));
        parameters.addParameter(new Parameter(this,Parameter.IMAGE_NAME,INPUT_IMAGE,null,false));
        parameters.addParameter(new Parameter(this,Parameter.IMAGE_NAME,OUTPUT_IMAGE,null,false));

    }
}
