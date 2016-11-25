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

    private ArrayList<HCExporter> HCExporters = new ArrayList<HCExporter>();
    private HCAnalysis analysis = null;
    private int save_mode = PERSTRUCTURE; // Default save mode is once for the entire structure

    public BatchProcessor(File root_folder) {
        super(root_folder);

    }

    public BatchProcessor() {

    }

    public void setSaveMode(int save_mode) {
        this.save_mode = save_mode;

    }

    public HCResultCollection<String,HCResult> runAnalysisOnStructure() {
        int num_valid_files = getNumberOfValidFilesInStructure();
        resetIterator();

        Folder temp_folder = folder; // Keeping track of where we were

        HCResultCollection<String,HCResult> results = new HCResultCollection<String,HCResult>();

        folder = root_folder;
        File next = getNextValidFileInStructure();
        File prev_folder = folder.getFolderAsFile();

        int iter = 1;

        if (analysis != null) {
            while (next != null) {
                System.out.print("\rProcessing file: "+next.getName()+" (file "+iter+" of "+num_valid_files+")");
                // Checking if the new file is in a different folder
                if (HCExporters.size() != 0 & folder.getFolderAsFile() != prev_folder & save_mode == PERFOLDER) {
                    Iterator<HCExporter> iterator = HCExporters.iterator();

                    // Folder-level export.  Passes the previous folder and it's name
                    while (iterator.hasNext()) {
                        iterator.next().export(results, prev_folder, prev_folder.getName());

                    }

                    results = new HCResultCollection<String,HCResult>(); // Resetting the collection
                }

                // Running the analysis
                HCResultCollection<String,HCResult> curr_results = analysis.execute(next);

                // Saving the current results
                if (HCExporters.size() != 0 & save_mode == PERFILE) {
                    Iterator<HCExporter> iterator = HCExporters.iterator();

                    //File-level export.  Passes the current folder and the current file's name
                    while (iterator.hasNext()) {
                        iterator.next().export(curr_results, folder.getFolderAsFile(), next.getName());

                    }
                }

                // Appending new results to main results file
                results.putAll(curr_results);

                prev_folder = folder.getFolderAsFile();
                next = getNextValidFileInStructure();

                iter++;

            }

            if (HCExporters.size() != 0 & save_mode == PERFOLDER) {
                Iterator<HCExporter> iterator = HCExporters.iterator();

                // Folder-level export.  Passes the previous folder and it's name
                while (iterator.hasNext()) {
                    iterator.next().export(results, prev_folder, prev_folder.getName());

                }

            } else if (HCExporters.size() != 0 & save_mode == PERSTRUCTURE) {
                Iterator<HCExporter> iterator = HCExporters.iterator();

                // Structure-level export.  Passes the root folder and it's name
                while (iterator.hasNext()) {
                    System.out.println("");
                    iterator.next().export(results, root_folder.getFolderAsFile(), root_folder.getFolderAsFile().getName());

                }
            }

        }

        System.out.println("");

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
