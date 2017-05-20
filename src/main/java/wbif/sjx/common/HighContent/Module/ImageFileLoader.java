package wbif.sjx.common.HighContent.Module;

import ij.ImagePlus;
import ij.io.Opener;
import wbif.sjx.common.HighContent.Object.*;

/**
 * Created by sc13967 on 15/05/2017.
 */
public class ImageFileLoader extends HCModule {
    public static final String FILE_PATH = "File path";
    public static final String OUTPUT_IMAGE = "Output image";
    public static final String USE_BIOFORMATS = "Use Bio-formats importer";


    @Override
    public String getTitle() {
        return "Load image from file";

    }

    @Override
    public void execute(HCWorkspace workspace, boolean verbose) {
        if (verbose) System.out.println("   Loading image from file");

        // Getting input file
        String filePath = parameters.getValue(FILE_PATH);

        // Getting name to save
        HCName outputImageName = parameters.getValue(OUTPUT_IMAGE);

        // Importing the file
        ImagePlus ipl = Opener.openUsingBioFormats(filePath);

        // Adding image to workspace
        if (verbose) System.out.println("       Adding image ("+outputImageName+") to workspace");
        workspace.addImage(outputImageName,new HCImage(ipl));

    }

    @Override
    public HCParameterCollection initialiseParameters() {
        HCParameterCollection parameters = new HCParameterCollection();

        parameters.addParameter(new HCParameter(this,FILE_PATH,HCParameter.FILE_PATH,null));
        parameters.addParameter(new HCParameter(this,OUTPUT_IMAGE,HCParameter.OUTPUT_IMAGE,null));
        parameters.addParameter(new HCParameter(this,USE_BIOFORMATS,HCParameter.BOOLEAN,true));

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
