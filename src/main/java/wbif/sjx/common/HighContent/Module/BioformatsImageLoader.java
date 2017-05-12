// TODO: Enable ability to extract multiple images from .lif files

package wbif.sjx.common.HighContent.Module;

import ij.ImagePlus;
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
        ImagePlus[] ipls = null;
        try {
            // Bio-ormats writes lots of unwanted information to System.out.  This diverts it to a fake PrintStream
            PrintStream realStream = System.out;
            PrintStream fakeStream = new PrintStream(new OutputStream() {
                @Override
                public void write(int b) throws IOException {

                }
            });
            System.setOut(fakeStream);

            ImporterOptions opts = new ImporterOptions();

            opts.setLocation(ImporterOptions.LOCATION_LOCAL);
            opts.setId(workspace.getCurrentFile().getAbsolutePath());

            // Running Bio-formats
            ImportProcess process = new ImportProcess(opts);
            process.execute();
            ImagePlusReader reader = new ImagePlusReader(process);
            ipls = reader.openImagePlus();

            // Restoring the real PrintStream
            System.setOut(realStream);

        } catch (IOException | FormatException e) {
            e.printStackTrace();
        }

        if (ipls != null) {
            // Adding image to workspace
            if (verbose) System.out.println("       Adding image ("+outputImageName.getName()+") to workspace");
            workspace.addImage(outputImageName, new Image(ipls[0]));

            // (If selected) displaying the loaded image
            boolean showImage = parameters.getValue(this,SHOW_IMAGE);
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
        parameters.addParameter(new Parameter(this,MODULE_TITLE,Parameter.MODULE_TITLE,"Image stack loader",false));
        parameters.addParameter(new Parameter(this,OUTPUT_IMAGE,Parameter.IMAGE_NAME,"Im1",false));
        parameters.addParameter(new Parameter(this,SHOW_IMAGE,Parameter.BOOLEAN,false,false));

    }
}
