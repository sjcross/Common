package wolfson.common.System;

import wolfson.common.Object.Analysis;

import java.io.File;

/**
 * Created by sc13967 on 21/10/2016.
 */
public class BatchProcessor extends FileCrawler {

    public BatchProcessor(File root_folder) {
        super(root_folder);

    }

    public void runAnalysisOnStructure(Analysis analysis) {
        Folder temp_folder = folder; //Keeping track of where we were

        folder = root_folder;
        File next = getNextFileInStructure();
        while (next != null) {
            analysis.execute(next); //Running the analysis

            next = getNextFileInStructure();
        }

        folder = temp_folder;
    }
}
