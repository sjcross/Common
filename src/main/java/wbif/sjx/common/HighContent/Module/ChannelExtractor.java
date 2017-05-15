package wbif.sjx.common.HighContent.Module;

import ij.ImagePlus;
import ij.plugin.SubHyperstackMaker;
import wbif.sjx.common.HighContent.Object.*;

/**
 * Created by sc13967 on 08/05/2017.
 */
public class ChannelExtractor implements Module {
    public static final String INPUT_IMAGE = "Input image";
    public static final String OUTPUT_IMAGE = "Output image1";
    public static final String CHANNEL_TO_EXTRACT = "Channel to extract";
    public static final String SHOW_IMAGE = "Show output image";

    @Override
    public void execute(Workspace workspace, ParameterCollection parameters, boolean verbose) {
        if (verbose) System.out.println("   Running channel extractor");

        // Loading input image
        ImageName inputImageName = parameters.getValue(this,INPUT_IMAGE);
        if (verbose) System.out.println("       Loading image ("+inputImageName.getName()+") into workspace");
        ImagePlus ipl = workspace.getImages().get(inputImageName).getImagePlus();

        // Getting parameters
        ImageName outputImageName = parameters.getValue(this,OUTPUT_IMAGE);
        int channel = parameters.getValue(this,CHANNEL_TO_EXTRACT);

        // Getting selected channel
        if (verbose) System.out.println("       Extracting channel "+channel);
        ImagePlus outputChannelImagePlus = SubHyperstackMaker.makeSubhyperstack(ipl,String.valueOf(channel),"1-"+ipl.getNSlices(),"1-"+ipl.getNFrames());

        // Adding image to workspace
        if (verbose) System.out.println("       Adding image ("+outputImageName.getName()+") to workspace");
        workspace.addImage(outputImageName,new Image(outputChannelImagePlus));

        // (If selected) displaying the loaded image
        boolean showImage = parameters.getValue(this,SHOW_IMAGE);
        if (showImage) {
            if (verbose) System.out.println("       Displaying extracted image");
            outputChannelImagePlus.show();
        }
    }

    @Override
    public ParameterCollection initialiseParameters() {
        ParameterCollection parameters = new ParameterCollection();

        parameters.addParameter(new Parameter(this,INPUT_IMAGE,Parameter.IMAGE_NAME,"Im1",false));
        parameters.addParameter(new Parameter(this,OUTPUT_IMAGE,Parameter.IMAGE_NAME,"Im2",false));
        parameters.addParameter(new Parameter(this,CHANNEL_TO_EXTRACT,Parameter.INTEGER,1,false));
        parameters.addParameter(new Parameter(this,SHOW_IMAGE,Parameter.BOOLEAN,false,false));

        return parameters;

    }
}
