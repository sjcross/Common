// TODO: Enable ability to extract multiple images from .lif files

package wbif.sjx.common.HighContent.Module;

import ij.ImagePlus;
import ij.io.Opener;
import wbif.sjx.common.HighContent.Object.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by sc13967 on 08/05/2017.
 */
public class BioformatsImageLoader extends HCModule {
    public static final String OUTPUT_IMAGE = "Output image";
    public static final String SHOW_IMAGE = "Show loaded image";

    @Override
    public String getTitle() {
        return "Bio-formats image loader";

    }

    @Override
    public void execute(HCWorkspace workspace, boolean verbose) {
        if (verbose) System.out.println("    Running Bioformats image loader");

        // Getting image name
        HCName outputImageName = parameters.getValue(OUTPUT_IMAGE);

        // Running Bio-formats importer
        if (verbose) System.out.println("       Loading image");

        // Bio-formats writes lots of unwanted information to System.out.  This diverts it to a fake PrintStream
        PrintStream realStream = System.out;
        PrintStream fakeStream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {}
        });

        System.setOut(fakeStream);
        ImagePlus ipl = Opener.openUsingBioFormats(workspace.getMetadata().getFile().getAbsolutePath());
        System.setOut(realStream);

        if (ipl != null) {
            // Adding image to workspace
            if (verbose) System.out.println("       Adding image ("+outputImageName.getName()+") to workspace");
            workspace.addImage(outputImageName, new HCImage(ipl));

            // (If selected) displaying the loaded image
            boolean showImage = parameters.getValue(SHOW_IMAGE);
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
    public HCParameterCollection initialiseParameters() {
        HCParameterCollection parameters = new HCParameterCollection();

        parameters.addParameter(new HCParameter(this,OUTPUT_IMAGE, HCParameter.OUTPUT_IMAGE,null));
        parameters.addParameter(new HCParameter(this,SHOW_IMAGE, HCParameter.BOOLEAN,false));

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
