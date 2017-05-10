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

/**
 * Created by sc13967 on 21/10/2016.
 */
public class BatchProcessor extends FileCrawler {
    public static final int XML_EXPORT = 0;
    public static final int XLSX_EXPORT = 1;
    public static final int JSON_EXPORT = 2;

    private int exportMode = XML_EXPORT;
    private boolean verbose = false;


    // CONSTRUCTORS

    public BatchProcessor(File root_folder) {
        super(root_folder);

    }

    public BatchProcessor() {

    }


    // PUBLIC METHODS

    public void runAnalysisOnStructure(Analysis analysis) {
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
                Workspace workspace = new Workspace(next);
                analysis.execute(workspace,verbose);

                // Clearing images from the workspace to prevent memory leak
                workspace.clearAllImages();

                // Adding the workspace to the collection
                workspaces.add(workspace);

                next = getNextValidFileInStructure();

                // Adding a blank line to the output
                if (verbose) System.out.println(" ");

            }
        }

        // Saving the results
        exportResults(workspaces);

    }


    // PRIVATE METHODS

    private void exportResults(WorkspaceCollection workspaces) {
        if (exportMode == XML_EXPORT) {
            exportXML(workspaces);

        } else if (exportMode == XLSX_EXPORT) {
            exportXLSX(workspaces);

        } else if (exportMode == JSON_EXPORT) {
            exportJSON(workspaces);

        }
    }

    private void exportXML(WorkspaceCollection workspaces) {
        // Initialising DecimalFormat
        DecimalFormat df = new DecimalFormat("0.000E0");

        try {
            // Initialising the document
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = doc.createElement("ROOT");
            doc.appendChild(root);

            // Running through each workspace (each corresponds to a file) adding file information
            for (Workspace workspace:workspaces) {
                Element setElement =  doc.createElement("SET");

                // Adding metadata from the workspace
                Metadata metadata = workspace.getMetadata();
                for (String key:metadata.keySet()) {
                    String attrName = key.toUpperCase();
                    Attr attr = doc.createAttribute(attrName);
                    attr.appendChild(doc.createTextNode(metadata.getAsString(key)));
                    setElement.setAttributeNode(attr);

                }

                // Creating new elements for each image in the current workspace with at least one measurement
                for (ImageName imageName:workspace.getImages().keySet()) {
                    Image image = workspace.getImages().get(imageName);

                    if (image.getMeasurements() != null) {
                        Element imageElement = doc.createElement("IMAGE");

                        Attr nameAttr = doc.createAttribute("NAME");
                        nameAttr.appendChild(doc.createTextNode(String.valueOf(imageName.getName())));
                        imageElement.setAttributeNode(nameAttr);

                        for (Measurement measurement : image.getMeasurements().values()) {
                            String attrName = measurement.getName().toUpperCase().replaceAll(" ", "_");
                            Attr measAttr = doc.createAttribute(attrName);
                            String attrValue = df.format(measurement.getValue());
                            measAttr.appendChild(doc.createTextNode(attrValue));
                            imageElement.setAttributeNode(measAttr);
                        }

                        setElement.appendChild(imageElement);

                    }
                }

                // Creating new elements for each object in the current workspace
                for (HCObjectName objectNames:workspace.getObjects().keySet()) {
                    for (HCObject object:workspace.getObjects().get(objectNames).values()) {
                        Element objectElement =  doc.createElement("OBJECT");

                        // Setting the ID number
                        Attr idAttr = doc.createAttribute("ID");
                        idAttr.appendChild(doc.createTextNode(String.valueOf(object.getID())));
                        objectElement.setAttributeNode(idAttr);

                        Attr nameAttr = doc.createAttribute("NAME");
                        nameAttr.appendChild(doc.createTextNode(String.valueOf(objectNames.getName())));
                        objectElement.setAttributeNode(nameAttr);

                        for (Measurement measurement:object.getMeasurements().values()) {
                            String attrName = measurement.getName().toUpperCase().replaceAll(" ", "_");
                            Attr measAttr = doc.createAttribute(attrName);
                            String attrValue = df.format(measurement.getValue());
                            measAttr.appendChild(doc.createTextNode(attrValue));
                            objectElement.setAttributeNode(measAttr);
                        }

                        setElement.appendChild(objectElement);

                    }
                }

                root.appendChild(setElement);

            }

            // Preparing the filepath and filename
            String outPath = rootFolder.getFolderAsFile()+"\\"+"output.xml";

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(outPath);

            transformer.transform(source, result);

            System.out.println("Saved "+ outPath);


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }


    }

    private void exportXLSX(WorkspaceCollection workspaces) {
        System.out.println("[WARN] No XLSX export currently implemented.  File not saved.");

    }

    private void exportJSON(WorkspaceCollection workspaces) {
        System.out.println("[WARN] No JSON export currently implemented.  File not saved.");

    }


    // GETTERS AND SETTERS

    public int getExportMode() {
        return exportMode;
    }

    public void setExportMode(int exportMode) {
        this.exportMode = exportMode;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
}
