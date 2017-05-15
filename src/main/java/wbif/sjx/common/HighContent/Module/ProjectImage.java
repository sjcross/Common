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
        ImageName inputImageName = parameters.getValue(this,INPUT_IMAGE);
        Image inputImage = workspace.getImages().get(inputImageName);

        // Getting output image name
        ImageName outputImageName = parameters.getValue(this,OUTPUT_IMAGE);

        // Create max projection image
        Image outputImage = inputImage.projectImageInZ();

        // Adding projected image to workspace
        workspace.addImage(outputImageName,outputImage);

    }

    @Override
    public ParameterCollection initialiseParameters() {
        ParameterCollection parameters = new ParameterCollection();

        parameters.addParameter(new Parameter(this,MODULE_TITLE,Parameter.MODULE_TITLE,"Image projector",false));
        parameters.addParameter(new Parameter(this,INPUT_IMAGE,Parameter.IMAGE_NAME,null,false));
        parameters.addParameter(new Parameter(this,OUTPUT_IMAGE,Parameter.IMAGE_NAME,null,false));

        return parameters;

    }
}
