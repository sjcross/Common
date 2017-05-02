package wbif.sjx.common.HighContent.Object;

import ij.IJ;
import ij.ImageJ;
import wbif.sjx.common.FileConditions.ExtensionMatchesString;
import wbif.sjx.common.FileConditions.FileCondition;
import wbif.sjx.common.FileConditions.NameContainsPattern;
import wbif.sjx.common.FileConditions.NameContainsString;
import wbif.sjx.common.HighContent.Extractor.CellVoyagerFilenameExtractor;
import wbif.sjx.common.HighContent.Extractor.Extractor;
import wbif.sjx.common.HighContent.GUI.ParameterWindow;
import wbif.sjx.common.HighContent.Module.PrimaryObjectIdentification;
import wbif.sjx.common.HighContent.Process.BatchProcessor;
import wbif.sjx.common.HighContent.Process.ImageStackLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by steph on 30/04/2017.
 */
public class TestHC {
    public static void main(String[] args) {
        // Initialising variable names for images and objects
        ImageName Stack1 = new ImageName("C1");
        ImageName Stack2 = new ImageName("C2");
        String NucleiObjs = "Nuclei";

        // Creating parameter store
        ParameterCollection parameters = new ParameterCollection();

        // Creating Workspace to store parameters, images and objects
        Workspace workspace = new Workspace();
        workspace.setParameters(parameters);

        // Initialising primary object identification
        PrimaryObjectIdentification objIdent1 = new PrimaryObjectIdentification();
        parameters.put(objIdent1.hashCode(),objIdent1.initialiseParameters());
        parameters.get(objIdent1.hashCode()).put(PrimaryObjectIdentification.INPUT_IMAGE,Stack1);
        parameters.get(objIdent1.hashCode()).put(PrimaryObjectIdentification.OUTPUT_OBJECT,NucleiObjs);

        // Displaying settings for parameters
        new ParameterWindow().updateParameters(parameters);

        new ImageJ();
        //String root = "E:\\Stephen\\Google Drive\\People\\P\\Melanie Panagi\\2016-10-05 Cell Voyager\\20160908T142009_10x_KO1_sparse\\";
        String root = "C:\\Users\\sc13967\\Google Drive\\People\\P\\Melanie Panagi\\2016-10-05 Cell Voyager\\20160908T142009_10x_KO1_sparse\\";

        // Creating BatchProcessor, which will run through the file structure and pull out files to analyse
        BatchProcessor bp = new BatchProcessor();
        bp.setRootFolder(new File(root));

        // Compulsory items
        bp.addFileCondition(new ExtensionMatchesString(new String[]{"tif", "tiff"})); //It's a .tif image
        Pattern pattern = Pattern.compile(new CellVoyagerFilenameExtractor().getPattern());
        bp.addFileCondition(new NameContainsPattern(pattern, FileCondition.INC_PARTIAL)); //The name matches the form of the specified regular expression
        bp.addFileCondition(new NameContainsString("Z01", FileCondition.INC_PARTIAL)); //It's the z-slice (other matching files will be loaded in the main execution)
        bp.addFileCondition(new NameContainsString("C1", FileCondition.INC_PARTIAL)); //It's the nuclei channel (other matching files will be loaded in the main execution)
        bp.addFolderCondition(new NameContainsString("Image", FileCondition.INC_COMPLETE)); //It's in the Image folder

        bp.goToNextValidFolder();
        ArrayList<File> allFiles = new ArrayList<>(Arrays.asList(bp.getCurrentFolderAsFolder().getFiles()));
        ArrayList<File> validFiles = bp.getAllValidFilesInFolder();
        Extractor extractor = new CellVoyagerFilenameExtractor();

        ArrayList<String> staticFields = new ArrayList<>();
        staticFields.add(Result.WELL);
        staticFields.add(Result.FIELD);

        ImageStackLoader imageStackLoader = new ImageStackLoader(extractor);
        imageStackLoader.setOrderField(Result.ZPOSITION);

        for (File file:validFiles) {
            // Loading channel 1 images
            HashMap<String,String> setFields1 = new HashMap<>();
            setFields1.put(Result.CHANNEL,"1");
            Image ipl1 = new Image(imageStackLoader.extract(file, allFiles, staticFields, setFields1));

            // Loading corresponding channel 2 images
            HashMap<String,String> setFields2 = new HashMap<>();
            setFields2.put(Result.CHANNEL,"2");
            Image ipl2 = new Image(imageStackLoader.extract(file, allFiles, staticFields, setFields2));

            // Storing as HCImage class objects
            workspace.addImage(Stack1,ipl1);
            workspace.addImage(Stack2,ipl2);

            // Running primary object identification
            objIdent1.execute(workspace);

            IJ.runMacro("waitForUser");

        }

        System.out.println("Done!");

    }
}
