package wbif.sjx.common.HighContent.Process;

import ij.ImagePlus;
import wbif.sjx.common.HighContent.Extractor.Extractor;
import wbif.sjx.common.HighContent.Object.Result;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by steph on 30/04/2017.
 */
public class ImageStackLoader {
    Extractor extractor = null;

    public ImageStackLoader(Extractor extractor) {
        this.extractor = extractor;

    }

    public ImagePlus extract(File referenceFile, ArrayList<File> files, ArrayList<String> staticFields, String orderField) {
        // Creating a Result object holding parameters about the reference file
        Result referenceResult = new Result();
        referenceResult.setFile(referenceFile);
        extractor.extract(referenceResult,referenceFile.getName());

        // Creating a structure to store only Result objects containing file parameters matching those specified
        ArrayList<Result> results = new ArrayList<Result>();

        // Running through all provided files, extracting parameters
        for (File file:files) {
            Result result = new Result();
            result.setFile(file);
            extractor.extract(result,file.getName());

            // Comparing all results to template
            boolean addResult = true;
            for (String field:staticFields) {
                if (!result.getAsString(field).equals(referenceResult.getAsString(field))) {
                    addResult = false;
                }
            }

            if (addResult) {
                results.add(result);
            }
        }

        for (Result res:results) {
            System.out.println(res.getFile().getName());
        }

        // Before importing images they need to be ordered based on the orderField parameter

        return null;

    }
}
