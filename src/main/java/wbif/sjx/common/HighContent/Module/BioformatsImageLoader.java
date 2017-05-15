// TODO: Enable ability to extract multiple images from .lif files

package wbif.sjx.common.HighContent.Module;

import ij.ImagePlus;
import ij.io.Opener;
import loci.formats.FormatException;
import loci.plugins.in.ImagePlusReader;
import loci.plugins.in.ImportProcess;
import loci.plugins.in.ImporterOptions;
import wbif.sjx.common.HighContent.Object.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

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
        ImageName outputImageName = parameters.getValue(this,OUTPUT_IMAGE);

        // Running Bio-formats importer
        if (verbose) System.out.println("       Loading image");

        // Bio-formats writes lots of unwanted information to System.out.  This diverts it to a fake PrintStream
        PrintStream realStream = System.out;
        PrintStream fakeStream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {

            }
        });

        System.setOut(fakeStream);
        ImagePlus ipl = Opener.openUsingBioFormats(workspace.getMetadata().getFile().getAbsolutePath());
        System.setOut(realStream);

        if (ipl != null) {
            // Adding image to workspace
            if (verbose) System.out.println("       Adding image ("+outputImageName.getName()+") to workspace");
            workspace.addImage(outputImageName, new Image(ipl));

            // (If selected) displaying the loaded image
            boolean showImage = parameters.getValue(this,SHOW_IMAGE);
            if (showImage) {
                if (verbose) System.out.println("       Displaying loaded image");
                ipl.show();
            }

        } else {
            // Warning that no image loaded
            if (verbose) System.out.println("       Image ("+outputImageName.getName()+") failed to load");

        }
    }

    @Override
    public ParameterCollection initialiseParameters() {
        ParameterCollection parameters = new ParameterCollection();

        parameters.addParameter(new Parameter(this,MODULE_TITLE,Parameter.MODULE_TITLE,"Image stack loader",false));
        parameters.addParameter(new Parameter(this,OUTPUT_IMAGE,Parameter.IMAGE_NAME,"Im1",false));
        parameters.addParameter(new Parameter(this,SHOW_IMAGE,Parameter.BOOLEAN,false,false));

        return parameters;

    }
}
