package wbif.sjx.common.HighContent.Process;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.io.Opener;
import wbif.sjx.common.HighContent.Extractor.Extractor;
import wbif.sjx.common.HighContent.Object.Result;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by steph on 30/04/2017.
 */
public class ImageStackLoader {
    private Extractor extractor = null;
    private String orderField = null;


    // CONSTRUCTORS

    public ImageStackLoader(Extractor extractor) {
        this.extractor = extractor;

    }


    // PUBLIC METHODS

    public ImagePlus extract(File referenceFile, ArrayList<File> files, ArrayList<String> staticFields, HashMap<String,String> setFields) {
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
            extractor.extract(result,file.getName());

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

        // Before importing images they need to be ordered based on the orderField parameter
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

        return ipl;

    }


    // GETTERS AND SETTERS

    public Extractor getExtractor() {
        return extractor;
    }

    public void setExtractor(Extractor extractor) {
        this.extractor = extractor;
    }

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }
}
