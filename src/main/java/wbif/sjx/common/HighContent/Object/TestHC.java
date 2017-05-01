package wbif.sjx.common.HighContent.Object;

import wbif.sjx.common.FileConditions.ExtensionMatchesString;
import wbif.sjx.common.FileConditions.FileCondition;
import wbif.sjx.common.FileConditions.NameContainsPattern;
import wbif.sjx.common.FileConditions.NameContainsString;
import wbif.sjx.common.HighContent.Extractor.CellVoyagerFilenameExtractor;
import wbif.sjx.common.HighContent.Extractor.Extractor;
import wbif.sjx.common.HighContent.Process.ImageStackLoader;
import wbif.sjx.common.System.BatchProcessor;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by steph on 30/04/2017.
 */
public class TestHC {
    public static void main(String[] args) {
        String root = "E:\\Stephen\\Google Drive\\People\\P\\Melanie Panagi\\2016-10-05 Cell Voyager\\20160908T142009_10x_KO1_sparse\\";

        BatchProcessor bp = new BatchProcessor();
        bp.setRootFolder(new File(root));

        // Compulsory items
        bp.addFileCondition(new ExtensionMatchesString(new String[]{"tif", "tiff"})); //It's a .tif image
        Pattern pattern = Pattern.compile(new CellVoyagerFilenameExtractor().getPattern());
        bp.addFileCondition(new NameContainsPattern(pattern, FileCondition.INC_PARTIAL)); //The name matches the form of the specified regular expression
//        bp.addFileCondition(new NameContainsString("Z01", FileCondition.INC_PARTIAL)); //It's the z-slice (other matching files will be loaded in the main execution)
//        bp.addFileCondition(new NameContainsString("C1", FileCondition.INC_PARTIAL)); //It's the nuclei channel (other matching files will be loaded in the main execution)
        bp.addFolderCondition(new NameContainsString("Image", FileCondition.INC_COMPLETE)); //It's in the Image folder

        // Need a decent way to get only 1 channel and z position

        bp.goToNextValidFolder();
        ArrayList<File> files = bp.getAllValidFilesInFolder();
        Extractor extractor = new CellVoyagerFilenameExtractor();
        ArrayList<String> staticFields = new ArrayList<>();
        staticFields.add(Result.CHANNEL);
        staticFields.add(Result.WELL);
        staticFields.add(Result.FIELD);
        new ImageStackLoader(extractor).extract(files.get(0),files,staticFields,"");



    }
}
