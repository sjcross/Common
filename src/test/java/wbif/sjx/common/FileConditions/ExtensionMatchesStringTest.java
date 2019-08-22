package wbif.sjx.common.FileConditions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by steph on 12/04/2017.
 */
public class ExtensionMatchesStringTest {
    @Test
    public void testTest(@TempDir Path temporaryFolder) {
        // Initialising the class to be tested
        String[] exts = new String[]{"tif","tiff"};
        ExtensionMatchesString extensionMatchesString = new ExtensionMatchesString(exts);


        // Initialising temporary files and folders
        File testFileTrue = temporaryFolder.resolve("test.tif").toFile();
        File testFileFalse = temporaryFolder.resolve("test.tof").toFile();

        // Obtaining results for the true and false cases
        boolean resultTrue = extensionMatchesString.test(testFileTrue);
        boolean resultFalse = extensionMatchesString.test(testFileFalse);

        // Checking results
        assertEquals(true,resultTrue);
        assertEquals(false,resultFalse);

    }
}