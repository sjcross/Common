package wbif.sjx.common.FileConditions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Stephen on 12/04/2017.
 */
public class ExtensionMatchesStringTest {
    @Test
    public void testTestIncludesCompleteTrue(@TempDir Path temporaryFolder) {
        // Initialising the class to be tested
        String[] exts = new String[]{"tif","tiff"};
        ExtensionMatchesString extensionMatchesString = new ExtensionMatchesString(exts,FileCondition.Mode.INC_COMPLETE);

        // Initialising temporary files and folders
        File testFileTrue = temporaryFolder.resolve("test.tif").toFile();

        // Obtaining results for the true and false cases
        boolean resultTrue = extensionMatchesString.test(testFileTrue);

        // Checking results
        assertTrue(resultTrue);

    }

    @Test
    public void testTestIncludesCompleteFalse(@TempDir Path temporaryFolder) {
        // Initialising the class to be tested
        String[] exts = new String[]{"tif","tiff"};
        ExtensionMatchesString extensionMatchesString = new ExtensionMatchesString(exts,FileCondition.Mode.INC_COMPLETE);

        // Initialising temporary files and folders
        File testFileFalse = temporaryFolder.resolve("test.tifg").toFile();

        // Obtaining results for the true and false cases
        boolean resultFalse = extensionMatchesString.test(testFileFalse);

        // Checking results
        assertFalse(resultFalse);

    }

    @Test
    public void testTestIncludesPartialTrue(@TempDir Path temporaryFolder) {
        // Initialising the class to be tested
        String[] exts = new String[]{"tif","tiff"};
        ExtensionMatchesString extensionMatchesString = new ExtensionMatchesString(exts, FileCondition.Mode.INC_PARTIAL);

        // Initialising temporary files and folders
        File testFileTrue = temporaryFolder.resolve("test.tifg").toFile();

        // Obtaining results for the true and false cases
        boolean resultTrue = extensionMatchesString.test(testFileTrue);

        // Checking results
        assertTrue(resultTrue);

    }

    @Test
    public void testTestIncludesPartialFalse(@TempDir Path temporaryFolder) {
        // Initialising the class to be tested
        String[] exts = new String[]{"tif","tiff"};
        ExtensionMatchesString extensionMatchesString = new ExtensionMatchesString(exts,FileCondition.Mode.INC_PARTIAL);

        // Initialising temporary files and folders
        File testFileFalse = temporaryFolder.resolve("test.tofg").toFile();

        // Obtaining results for the true and false cases
        boolean resultFalse = extensionMatchesString.test(testFileFalse);

        // Checking results
        assertFalse(resultFalse);

    }
}