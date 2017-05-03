package wbif.sjx.common.HighContent.Object;

import ij.ImageJ;
import wbif.sjx.common.FileConditions.ExtensionMatchesString;
import wbif.sjx.common.FileConditions.FileCondition;
import wbif.sjx.common.FileConditions.NameContainsPattern;
import wbif.sjx.common.FileConditions.NameContainsString;
import wbif.sjx.common.HighContent.Extractor.CellVoyagerFilenameExtractor;
import wbif.sjx.common.HighContent.Process.Analysis;
import wbif.sjx.common.HighContent.Process.BatchProcessor;

import java.io.File;
import java.util.regex.Pattern;

/**
 * Created by steph on 30/04/2017.
 */
public class TestHC {
    public static void main(String[] args) {
        new ImageJ();

        // Creating Workspace to store parameters, images and objects
        Workspace workspace = new Workspace();

        Analysis analysis = new TestAnalysis();
        analysis.initialise(workspace);

        BatchProcessor bp = setupBatchProcessor();
        bp.setAnalysis(analysis);

        bp.runAnalysisOnStructure(workspace);

        System.out.println("Done!");

    }

    static private BatchProcessor setupBatchProcessor() {
        // Getting root folder
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

        return bp;

    }
}
