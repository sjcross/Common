package wbif.sjx.common.HighContent.Object;

import ij.ImageJ;
import ij.ImagePlus;
import wbif.sjx.common.FileConditions.ExtensionMatchesString;
import wbif.sjx.common.FileConditions.FileCondition;
import wbif.sjx.common.FileConditions.NameContainsPattern;
import wbif.sjx.common.FileConditions.NameContainsString;
import wbif.sjx.common.HighContent.Extractor.CellVoyagerFilenameExtractor;
import wbif.sjx.common.HighContent.Extractor.Extractor;
import wbif.sjx.common.HighContent.Process.ImageStackLoader;
import wbif.sjx.common.HighContent.Process.BatchProcessor;

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
        new ImageJ();
        String root = "E:\\Stephen\\Google Drive\\People\\P\\Melanie Panagi\\2016-10-05 Cell Voyager\\20160908T142009_10x_KO1_sparse\\";

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
            bp.addImage("C1",ipl1);
            bp.addImage("C2",ipl2);

            // Running primary object identification



        }

        System.out.println("Done!");

    }
}
