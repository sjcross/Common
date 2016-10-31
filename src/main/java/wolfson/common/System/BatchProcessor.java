package wolfson.common.System;

import wolfson.common.HighContent.HCExporter;
import wolfson.common.HighContent.HCResult;
import wolfson.common.HighContent.HCResultCollection;
import wolfson.common.HighContent.HCAnalysis;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by sc13967 on 21/10/2016.
 */
public class BatchProcessor extends FileCrawler {
    public final static int PERFILE = 0; // Save a new results file for each analysed file
    public final static int PERFOLDER = 1; // Save a new results file for each analysed folder
    public final static int PERSTRUCTURE = 2; // Save a new results file for each analysed structure (i.e. just once)

    private ArrayList<HCExporter> HCExporters = null;
    private HCAnalysis analysis = null;
    private int save_mode = PERSTRUCTURE; // Default save mode is once for the entire structure

    public BatchProcessor(File root_folder) {
        super(root_folder);

    }

    public void setSaveMode(int save_mode) {
        this.save_mode = save_mode;

    }

    public HCResultCollection<HCResult> runAnalysisOnStructure() {
        Folder temp_folder = folder; // Keeping track of where we were

        HCResultCollection<HCResult> results = new HCResultCollection<HCResult>();

        folder = root_folder;
        File next = getNextFileInStructure();
        File prev_folder = folder.getFolderAsFile();

        if (analysis != null) {
            while (next != null) {
                // Checking if the new file is in a different folder
                if (HCExporters != null & folder.getFolderAsFile() != prev_folder & save_mode == PERFOLDER) {
                    Iterator<HCExporter> iterator = HCExporters.iterator();
                    while (iterator.hasNext()) {
                        iterator.next().export(results, prev_folder); // Performing the specified export tasks
                    }

                    results = new HCResultCollection<HCResult>(); // Resetting the collection
                }

                // Running the analysis
                HCResultCollection<HCResult> curr_results = analysis.execute(next);

                // Saving the current results
                if (HCExporters != null & save_mode == PERFILE) {
                    Iterator<HCExporter> iterator = HCExporters.iterator();
                    while (iterator.hasNext()) {
                        iterator.next().export(curr_results, folder.getFolderAsFile()); // Performing the specified export tasks
                    }
                }

                // Appending new results to main results file
                results.addAll(curr_results);

                prev_folder = folder.getFolderAsFile();
                next = getNextFileInStructure();

            }

            if (HCExporters != null & save_mode == PERFOLDER) {
                Iterator<HCExporter> iterator = HCExporters.iterator();
                while (iterator.hasNext()) {
                    iterator.next().export(results, prev_folder); // Performing the specified export tasks
                }

            } else if (HCExporters != null & save_mode == PERSTRUCTURE) {
                Iterator<HCExporter> iterator = HCExporters.iterator();
                while (iterator.hasNext()) {
                    iterator.next().export(results, root_folder.getFolderAsFile()); // Performing the specified export tasks
                }
            }
        }

        folder = temp_folder;

        return results;
    }

    public void setAnalysis(HCAnalysis analysis) {
        this.analysis = analysis;

    }

    public void addExporter(HCExporter HCExporter) {
        HCExporters.add(HCExporter);

    }
}
