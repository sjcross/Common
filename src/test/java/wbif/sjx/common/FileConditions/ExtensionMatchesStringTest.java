package wbif.sjx.common.FileConditions;

import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by steph on 12/04/2017.
 */
public class ExtensionMatchesStringTest {
    @Test
    public void testTest() {
        // Initialising the class to be tested
        String[] exts = new String[]{"tif","tiff"};
        ExtensionMatchesString extensionMatchesString = new ExtensionMatchesString(exts);

        // Initialising temporary files and folders
        TemporaryFolder temporaryFolder = new TemporaryFolder();
        File testFileTrue = null;
        File testFileFalse = null;

        try {
            // Creating the folder and two files: one which should match and one which shouldn't
            temporaryFolder.create();
            testFileTrue = temporaryFolder.newFile("test.tif");
            testFileFalse = temporaryFolder.newFile("test.tof");

        } catch (IOException e) {
            e.printStackTrace();

        }

        // Obtaining results for the true and false cases
        boolean resultTrue = extensionMatchesString.test(testFileTrue);
        boolean resultFalse = extensionMatchesString.test(testFileFalse);

        // Checking results
        assertEquals("Must find match in extension for \"test.tif\"",true,resultTrue);
        assertEquals("Must not find match in extension for \"test.tof\"",false,resultFalse);

    }

}