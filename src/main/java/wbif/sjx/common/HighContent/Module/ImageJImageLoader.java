package wbif.sjx.common.HighContent.Module;

import ij.IJ;
import ij.ImagePlus;
import wbif.sjx.common.HighContent.Object.*;

/**
 * Created by Stephen on 10/05/2017.
 */
public class ImageJImageLoader extends HCModule {
    public static final String OUTPUT_IMAGE = "Output image";

    @Override
    public void execute(HCWorkspace workspace, boolean verbose) {
        if (verbose) System.out.println("   Loading image from ImageJ");

        // Getting image
        HCImageName outputImageName = parameters.getValue(OUTPUT_IMAGE);
        ImagePlus imagePlus = IJ.getImage();

        // Adding image to workspace
        if (verbose) System.out.println("       Adding image ("+outputImageName+") to workspace");
        workspace.addImage(outputImageName,new HCImage(imagePlus));

    }

    @Override
    public HCParameterCollection initialiseParameters() {
        HCParameterCollection parameters = new HCParameterCollection();

        parameters.addParameter(new HCParameter(this,OUTPUT_IMAGE, HCParameter.OUTPUT_IMAGE,"Im1",false));

        return parameters;

    }

    @Override
    public HCParameterCollection getActiveParameters() {
        return parameters;
    }
}
