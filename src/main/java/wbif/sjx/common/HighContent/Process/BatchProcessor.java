// TODO: Add methods for XLSX and JSON data export

package wbif.sjx.common.HighContent.Process;

import ij.IJ;
import org.apache.commons.lang.WordUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import wbif.sjx.common.HighContent.Object.*;
import wbif.sjx.common.System.FileCrawler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by sc13967 on 21/10/2016.
 */
public class BatchProcessor extends FileCrawler {
    private boolean verbose = false;


    // CONSTRUCTORS

    public BatchProcessor(File root_folder) {
        super(root_folder);

    }

    public BatchProcessor() {

    }


    // PUBLIC METHODS

    public WorkspaceCollection runAnalysisOnStructure(Analysis analysis, Exporter exporter) {
        int num_valid_files = getNumberOfValidFilesInStructure();
        resetIterator();

        WorkspaceCollection workspaces = new WorkspaceCollection();

        folder = rootFolder;
        File next = getNextValidFileInStructure();

        int iter = 1;

        if (analysis != null) {
            while (next != null) {
                System.out.println("Processing file: " + next.getName() + " (file " + iter++ + " of " + num_valid_files + ")");

                // Running the analysis
                Workspace workspace = workspaces.getNewWorkspace(next);
                analysis.execute(workspace,verbose);

                // Clearing images from the workspace to prevent memory leak
                workspace.clearAllImages();

                next = getNextValidFileInStructure();

                // Adding a blank line to the output
                if (verbose) System.out.println(" ");

            }
        }

        // Saving the results
        exporter.exportResults(workspaces);

        return workspaces;

    }


    // GETTERS AND SETTERS

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
}
