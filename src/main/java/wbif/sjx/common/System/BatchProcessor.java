package wbif.sjx.common.System;

import wbif.sjx.common.HighContent.Process.Analysis;
import wbif.sjx.common.HighContent.Process.Exporter;
import wbif.sjx.common.HighContent.Object.Result;
import wbif.sjx.common.HighContent.Object.ResultCollection;

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

    private ArrayList<Exporter> Exporters = new ArrayList<Exporter>();
    private Analysis analysis = null;
    private int save_mode = PERSTRUCTURE; // Default save mode is once for the entire structure

    public BatchProcessor(File root_folder) {
        super(root_folder);

    }

    public BatchProcessor() {

    }

    public void setSaveMode(int save_mode) {
        this.save_mode = save_mode;

    }

    public ResultCollection<String,Result> runAnalysisOnStructure() {
        int num_valid_files = getNumberOfValidFilesInStructure();
        resetIterator();

        Folder temp_folder = folder; // Keeping track of where we were

        ResultCollection<String,Result> results = new ResultCollection<String,Result>();

        folder = root_folder;
        File next = getNextValidFileInStructure();
        File prev_folder = folder.getFolderAsFile();

        int iter = 1;

        if (analysis != null) {
            while (next != null) {
                System.out.println("Processing file: "+next.getName()+" (file "+iter+" of "+num_valid_files+")");

                // Checking if the new file is in a different folder
                if (Exporters.size() != 0 & folder.getFolderAsFile() != prev_folder & save_mode == PERFOLDER) {
                    Iterator<Exporter> iterator = Exporters.iterator();

                    // Folder-level export.  Passes the previous folder and it's name
                    while (iterator.hasNext()) {
                        iterator.next().export(results, prev_folder, prev_folder.getName());

                    }

                    results = new ResultCollection<String,Result>(); // Resetting the collection
                }

                // Running the analysis
                ResultCollection<String,Result> curr_results = analysis.execute(next);

                // Saving the current results
                if (Exporters.size() != 0 & save_mode == PERFILE) {
                    Iterator<Exporter> iterator = Exporters.iterator();

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

            if (Exporters.size() != 0 & save_mode == PERFOLDER) {
                Iterator<Exporter> iterator = Exporters.iterator();

                // Folder-level export.  Passes the previous folder and it's name
                while (iterator.hasNext()) {
                    iterator.next().export(results, prev_folder, prev_folder.getName());

                }

            } else if (Exporters.size() != 0 & save_mode == PERSTRUCTURE) {
                Iterator<Exporter> iterator = Exporters.iterator();

                // Structure-level export.  Passes the root folder and it's name
                while (iterator.hasNext()) {
                    iterator.next().export(results, root_folder.getFolderAsFile(), root_folder.getFolderAsFile().getName());

                }
            }

        }

        folder = temp_folder;

        return results;
    }

    public void setAnalysis(Analysis analysis) {
        this.analysis = analysis;

    }

    public void addExporter(Exporter Exporter) {
        Exporters.add(Exporter);

    }
}
