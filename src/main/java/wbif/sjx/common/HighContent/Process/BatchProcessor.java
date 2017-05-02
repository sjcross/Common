// TODO: Could have a HashMap for parameters
// TODO: PERFILE saving can probably be removed.  Talk to users if this is used.

package wbif.sjx.common.HighContent.Process;

import wbif.sjx.common.HighContent.Object.HCObject;
import wbif.sjx.common.HighContent.Object.Image;
import wbif.sjx.common.HighContent.Object.Result;
import wbif.sjx.common.HighContent.Object.ResultCollection;
import wbif.sjx.common.System.FileCrawler;
import wbif.sjx.common.System.Folder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sc13967 on 21/10/2016.
 */
public class BatchProcessor extends FileCrawler {
    public final static int PERFILE = 0; // Save a new results file for each analysed file
    public final static int PERFOLDER = 1; // Save a new results file for each analysed folder
    public final static int PERSTRUCTURE = 2; // Save a new results file for each analysed structure (i.e. just once)

    private ArrayList<Exporter> exporters = new ArrayList<>();
    private Analysis analysis = null;
    private int save_mode = PERSTRUCTURE; // Default save mode is once for the entire structure


    // CONSTRUCTORS

    public BatchProcessor(File root_folder) {
        super(root_folder);

    }

    public BatchProcessor() {

    }


    // PUBLIC METHODS

    public ResultCollection<String,Result> runAnalysisOnStructure() {
        int num_valid_files = getNumberOfValidFilesInStructure();
        resetIterator();

        Folder temp_folder = getCurrentFolderAsFolder(); // Keeping track of where we were

        ResultCollection<String,Result> results = new ResultCollection<String,Result>();

        folder = rootFolder;
        File next = getNextValidFileInStructure();
        File prev_folder = folder.getFolderAsFile();

        int iter = 1;

        if (analysis != null) {
            while (next != null) {
                System.out.println("Processing file: "+next.getName()+" (file "+iter+" of "+num_valid_files+")");

                // Checking if the new file is in a different folder
                if (exporters.size() != 0 & folder.getFolderAsFile() != prev_folder & save_mode == PERFOLDER) {
                    // Folder-level export.  Passes the previous folder and it's name
                    for (Exporter exporter: exporters) {
                        exporter.export(results, prev_folder, prev_folder.getName());
                    }

                    results = new ResultCollection<>(); // Resetting the collection
                }

                // Running the analysis
                ResultCollection<String,Result> curr_results = analysis.execute(next);

                // Saving the current results
                if (exporters.size() != 0 & save_mode == PERFILE) {
                    //File-level export.  Passes the current folder and the current file's name
                    for (Exporter exporter: exporters) {
                     exporter.export(curr_results, folder.getFolderAsFile(), next.getName());
                    }
                }

                // Appending new results to main results file
                results.putAll(curr_results);

                prev_folder = folder.getFolderAsFile();
                next = getNextValidFileInStructure();

                iter++;

            }

            if (exporters.size() != 0 & save_mode == PERFOLDER) {
                // Folder-level export.  Passes the previous folder and it's name
                for (Exporter exporter: exporters) {
                    exporter.export(results, prev_folder, prev_folder.getName());
                }

            } else if (exporters.size() != 0 & save_mode == PERSTRUCTURE) {
                // Structure-level export.  Passes the root folder and it's name
                for (Exporter exporter: exporters) {
                    exporter.export(results, rootFolder.getFolderAsFile(), rootFolder.getFolderAsFile().getName());
                }
            }

        }

        folder = temp_folder;

        return results;
    }

    public void addExporter(Exporter Exporter) {
        exporters.add(Exporter);

    }


    // GETTERS AND SETTERS

    public void setSaveMode(int save_mode) {
        this.save_mode = save_mode;

    }

    public void setAnalysis(Analysis analysis) {
        this.analysis = analysis;

    }

}
