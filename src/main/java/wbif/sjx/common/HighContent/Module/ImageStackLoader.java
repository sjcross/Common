package wbif.sjx.common.HighContent.Module;

import ij.IJ;
import ij.ImagePlus;
import ij.io.Opener;
import wbif.sjx.common.HighContent.Extractor.Extractor;
import wbif.sjx.common.HighContent.Object.*;
import wbif.sjx.common.HighContent.Object.ParameterCollection;
import wbif.sjx.common.HighContent.Process.StackComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by steph on 30/04/2017.
 */
public class ImageStackLoader implements Module{
    public static final String EXTRACTOR = "Extractor";
    public static final String ORDER_FIELD = "Order field";
    public static final String STATIC_FIELDS = "Static fields";
    public static final String OUTPUT_IMAGE = "Output image";
    public static final String SET_FIELDS = "Set fields";

    @Override
    public void execute(Workspace workspace, ParameterCollection parameters, boolean verbose) {
        // Getting parameters
        Extractor extractor = (Extractor) parameters.getParameter(this,EXTRACTOR).getValue();
        String orderField = (String) parameters.getParameter(this,ORDER_FIELD).getValue();
        ArrayList<String> staticFields = (ArrayList<String>) parameters.getParameter(this,STATIC_FIELDS).getValue();
        HashMap<String,String> setFields = (HashMap<String, String>) parameters.getParameter(this,SET_FIELDS).getValue();
        ImageName outputImage = (ImageName) parameters.getParameter(this,OUTPUT_IMAGE).getValue();

        // Getting files
        File referenceFile = workspace.getCurrentFile();
        File[] files = referenceFile.getParentFile().listFiles();

        // Creating a Result object holding parameters about the reference file
        Metadata referenceResult = new Metadata();
        referenceResult.setFile(referenceFile);
        extractor.extract(referenceResult,referenceFile.getName());

        // Creating a structure to store only Result objects containing file parameters matching those specified
        ArrayList<Metadata> results = new ArrayList<>();

        // Running through all provided files, extracting parameters
        for (File file:files) {
            Metadata result = new Metadata();
            result.setFile(file);
            if (extractor.extract(result,file.getName())) {
                // Checking if fixed fields are the same as for the template
                boolean addResult = true;
                for (String field:staticFields) {
                    if (!result.getAsString(field).equals(referenceResult.getAsString(field))) {
                        addResult = false;
                    }
                }

                // Checking if fields with a specific value (not necessarily same a template) have that value
                for (String key:setFields.keySet()) {
                    if (!result.getAsString(key).equals(setFields.get(key))) {
                        addResult = false;
                    }
                }

                // If the previous conditions were met the result is added to the ArrayList
                if (addResult) {
                    results.add(result);
                }
            }
        }

        // Before importing images they need to be ordered based on the orderField parameter
        StackComparator stackComparator = new StackComparator();
        if (orderField != null) stackComparator.setField(orderField);
        results.sort(new StackComparator());

        // Loading the images and storing them as an ImagePlus
        Opener opener = new Opener();
        ImagePlus refIpl = opener.openImage(referenceFile.getAbsolutePath());
        ImagePlus ipl = IJ.createHyperStack("STACK_"+referenceFile.getName(),refIpl.getWidth(),refIpl.getHeight(),1,results.size(),1,refIpl.getBitDepth());

        int iter = 1;
        for (Metadata res:results) {
            ipl.setPosition(iter);

            ImagePlus singleIpl = opener.openImage(res.getFile().getAbsolutePath());
            ipl.setProcessor(singleIpl.getProcessor());

            iter++;

        }

        ipl.setPosition(1);

        workspace.addImage(outputImage,new Image(ipl));
    }

    @Override
    public void initialiseParameters(ParameterCollection parameters) {
        // Setting the input image stack name
        parameters.addParameter(new Parameter(this,Parameter.MODULE_TITLE,MODULE_TITLE,"Image stack loader",false));
        parameters.addParameter(new Parameter(this,Parameter.IMAGE_NAME,OUTPUT_IMAGE,null,false));
        parameters.addParameter(new Parameter(this,Parameter.OBJECT,EXTRACTOR,null,false));
        parameters.addParameter(new Parameter(this,Parameter.STRING,ORDER_FIELD,"",false));
        parameters.addParameter(new Parameter(this,Parameter.OBJECT,STATIC_FIELDS,new ArrayList<String>(),false));
        parameters.addParameter(new Parameter(this,Parameter.OBJECT,SET_FIELDS,new HashMap<String,String>(),false));

    }
}