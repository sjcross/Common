// TODO: Enable ability to extract multiple images from .lif files

package wbif.sjx.common.HighContent.Module;

import ij.ImagePlus;
import loci.formats.FormatException;
import loci.plugins.in.ImagePlusReader;
import loci.plugins.in.ImportProcess;
import loci.plugins.in.ImporterOptions;
import wbif.sjx.common.HighContent.Object.*;

import java.io.IOException;

/**
 * Created by sc13967 on 08/05/2017.
 */
public class BioformatsImageLoader implements Module {
    public static final String OUTPUT_IMAGE = "Output image";
    public static final String SHOW_IMAGE = "Show loaded image";

    @Override
    public void execute(Workspace workspace, ParameterCollection parameters, boolean verbose) {
        if (verbose) System.out.println("    Running Bioformats image loader");

        // Getting image name
        ImageName outputImageName = (ImageName) parameters.getParameter(this,OUTPUT_IMAGE).getValue();

        // Running Bioformats importer
        if (verbose) System.out.println("       Loading image");
        ImagePlus[] ipls = null;
        try {
            ImporterOptions opts = new ImporterOptions();

            opts.setLocation(ImporterOptions.LOCATION_LOCAL);
            opts.setId(workspace.getCurrentFile().getAbsolutePath());

            ImportProcess process = new ImportProcess(opts);
            process.execute();
            ImagePlusReader reader = new ImagePlusReader(process);
            ipls = reader.openImagePlus();

        } catch (IOException | FormatException e) {
            e.printStackTrace();
        }

        if (ipls != null) {
            // Adding image to workspace
            if (verbose) System.out.println("       Adding image ("+outputImageName.getName()+") to workspace");
            workspace.addImage(outputImageName, new Image(ipls[0]));

            // (If selected) displaying the loaded image
            boolean showImage = (boolean) parameters.getParameter(this,SHOW_IMAGE).getValue();
            if (showImage) {
                if (verbose) System.out.println("       Displaying loaded image");
                ipls[0].show();
            }

        } else {
            // Warning that no image loaded
            if (verbose) System.out.println("       Image ("+outputImageName.getName()+") failed to load");

        }

    }

    @Override
    public void initialiseParameters(ParameterCollection parameters) {
        parameters.addParameter(new Parameter(this,Parameter.MODULE_TITLE,MODULE_TITLE,"Image stack loader",false));
        parameters.addParameter(new Parameter(this,Parameter.IMAGE_NAME,OUTPUT_IMAGE,"Im1",false));
        parameters.addParameter(new Parameter(this,Parameter.BOOLEAN,SHOW_IMAGE,false,false));

    }
}
