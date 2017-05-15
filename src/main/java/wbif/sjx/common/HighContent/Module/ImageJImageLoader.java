package wbif.sjx.common.HighContent.Module;

import ij.IJ;
import ij.ImagePlus;
import wbif.sjx.common.HighContent.Object.*;

/**
 * Created by Stephen on 10/05/2017.
 */
public class ImageJImageLoader implements Module {
    public static final String OUTPUT_IMAGE = "Output image";

    @Override
    public void execute(Workspace workspace, ParameterCollection parameters, boolean verbose) {
        if (verbose) System.out.println("   Loading image from ImageJ");

        // Getting image
        ImageName outputImageName = parameters.getValue(this,OUTPUT_IMAGE);
        ImagePlus imagePlus = IJ.getImage();

        // Adding image to workspace
        if (verbose) System.out.println("       Adding image ("+outputImageName+") to workspace");
        workspace.addImage(outputImageName,new Image(imagePlus));

    }

    @Override
    public ParameterCollection initialiseParameters() {
        ParameterCollection parameters = new ParameterCollection();

        parameters.addParameter(new Parameter(this,OUTPUT_IMAGE,Parameter.IMAGE_NAME,"Im1",false));

        return parameters;

    }
}
