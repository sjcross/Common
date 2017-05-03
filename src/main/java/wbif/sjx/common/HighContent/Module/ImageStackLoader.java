package wbif.sjx.common.HighContent.Module;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.OvalRoi;
import ij.io.Opener;
import wbif.sjx.common.HighContent.Extractor.CellVoyagerFilenameExtractor;
import wbif.sjx.common.HighContent.Extractor.Extractor;
import wbif.sjx.common.HighContent.Object.*;
import wbif.sjx.common.HighContent.Process.StackComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

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
    public void execute(Workspace workspace) {
        // Getting parameters
        ParameterCollection parameters = workspace.getParameters();
        Extractor extractor = (Extractor) parameters.getParameter(this,EXTRACTOR);
        String orderField = (String) parameters.getParameter(this,ORDER_FIELD);
        ArrayList<String> staticFields = (ArrayList<String>) parameters.getParameter(this,STATIC_FIELDS);
        HashMap<String,String> setFields = (HashMap<String, String>) parameters.getParameter(this,SET_FIELDS);
        ImageName outputImage = (ImageName) parameters.getParameter(this,OUTPUT_IMAGE);

        // Getting files
        File referenceFile = workspace.getActiveFile();
        File[] files = referenceFile.getParentFile().listFiles();

        // Creating a Result object holding parameters about the reference file
        Result referenceResult = new Result();
        referenceResult.setFile(referenceFile);
        extractor.extract(referenceResult,referenceFile.getName());

        // Creating a structure to store only Result objects containing file parameters matching those specified
        ArrayList<Result> results = new ArrayList<>();

        // Running through all provided files, extracting parameters
        for (File file:files) {
            Result result = new Result();
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
        for (Result res:results) {
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
        parameters.addParameter(this,"Image stack loading module",new ModuleTitle("Image stack loader"),false);
        parameters.addParameter(this,EXTRACTOR,null,false);
        parameters.addParameter(this,ORDER_FIELD,"",false);
        parameters.addParameter(this,STATIC_FIELDS,new ArrayList<String>(),false);
        parameters.addParameter(this,OUTPUT_IMAGE,new ImageName("StackImage"),false);
        parameters.addParameter(this,SET_FIELDS,new HashMap<String,String>(),false);

    }
}
