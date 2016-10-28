package wolfson.common.System;

import wolfson.common.Object.Analysis;
import wolfson.common.Object.HCResult;
import wolfson.common.Object.HCResultCollection;

import java.io.File;

/**
 * Created by sc13967 on 21/10/2016.
 */
public class BatchProcessor extends FileCrawler {
    public final static int PERFILE = 0; // Save a new results file for each analysed file
    public final static int PERFOLDER = 1; // Save a new results file for each analysed folder
    public final static int PERSTRUCTURE = 2; // Save a new results file for each analysed structure (i.e. just once)

    private Exporter exporter;
    private int save_mode = PERSTRUCTURE; // Default save mode is once for the entire structure

    public BatchProcessor(File root_folder) {
        super(root_folder);

    }

    public void setSaveMode(int save_mode) {
        this.save_mode = save_mode;

    }

    public void runAnalysisOnStructure(Analysis analysis) {
        Folder temp_folder = folder; // Keeping track of where we were

        HCResultCollection results = new HCResultCollection();

        folder = root_folder;
        File next = getNextFileInStructure();
        File prev_folder = folder.getFolderAsFile();

        while (next != null) {
            // Checking if the new file is in a different folder
            if (folder.getFolderAsFile() != prev_folder & save_mode == PERFOLDER) {
                exporter.export(results, prev_folder); // Performing the specified export tasks
                results = new HCResultCollection(); // Resetting the collection
            }

            // Running the analysis
            HCResultCollection curr_results = analysis.execute(next);

            // Saving the current results
            if (save_mode == PERFILE) {
                exporter.export(curr_results, folder.getFolderAsFile()); // Performing the specified export tasks

            } else {
                results.addAll(curr_results);

            }

            prev_folder = folder.getFolderAsFile();
            next = getNextFileInStructure();

        }

        if (save_mode == PERFOLDER) {
            exporter.export(results, prev_folder); // Performing the specified export tasks

        } else if (save_mode == PERSTRUCTURE) {
            exporter.export(results, root_folder.getFolderAsFile()); // Performing the specified export tasks

        }

        folder = temp_folder;
    }

    public void setExporter(Exporter exporter) {
        this.exporter = exporter;

    }
}
