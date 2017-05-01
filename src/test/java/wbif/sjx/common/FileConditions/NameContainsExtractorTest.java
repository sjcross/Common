package wbif.sjx.common.FileConditions;

import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Created by steph on 12/04/2017.
 */
public class NameContainsExtractorTest {
    @Test
    public void testTestSingleIncPartial() {
        // Initialising pattern to match and class to be tested
        Pattern pattern = Pattern.compile("W(\\d+?)F(\\d+?)T(\\d+?)Z(\\d+?)C(\\d+?)");
        NameContainsPattern nameContainsPattern = new NameContainsPattern(pattern,FileCondition.INC_PARTIAL);

        // Initialising temporary files and folders
        TemporaryFolder temporaryFolder = new TemporaryFolder();
        File testFileTrue = null;
        File testFileFalse = null;

        try {
            // Creating the folder and two files: one which should match and one which shouldn't
            temporaryFolder.create();
            testFileTrue = temporaryFolder.newFile("Test_file-W1F001T0001Z01C1.tif");
            testFileFalse = temporaryFolder.newFile("Test_file-W1A002F001T0001Z01C1.tif");

        } catch (IOException e) {
            e.printStackTrace();

        }

        // Obtaining results for the true and false cases
        boolean resultTrue = nameContainsPattern.test(testFileTrue);
        boolean resultFalse = nameContainsPattern.test(testFileFalse);

        // Checking results
        assertEquals("Must find match for pattern \"Test_file-W1F001T0001Z01C1.tif\"",true,resultTrue);
        assertEquals("Must not find match in extension for \"Test_file-W1A002F001T0001Z01C1.tif\"",false,resultFalse);
    }

    @Test
    public void testTestSingleIncComplete() {
        // Initialising pattern to match and class to be tested
        Pattern pattern = Pattern.compile("W(\\d+?)F(\\d+?)T(\\d+?)Z(\\d+?)C(\\d+?)");
        NameContainsPattern nameContainsPattern = new NameContainsPattern(pattern,FileCondition.INC_COMPLETE);

        // Initialising temporary files and folders
        TemporaryFolder temporaryFolder = new TemporaryFolder();
        File testFileTrue = null;
        File testFileFalse = null;

        try {
            // Creating the folder and two files: one which should match and one which shouldn't
            temporaryFolder.create();
            testFileTrue = temporaryFolder.newFile("W1F001T0001Z01C1.tif");
            testFileFalse = temporaryFolder.newFile("Test_file-W1F001T0001Z01C1.tif");

        } catch (IOException e) {
            e.printStackTrace();

        }

        // Obtaining results for the true and false cases
        boolean resultTrue = nameContainsPattern.test(testFileTrue);
        boolean resultFalse = nameContainsPattern.test(testFileFalse);

        // Checking results
        assertEquals("Must find match for pattern \"W1F001T0001Z01C1.tif\"",true,resultTrue);
        assertEquals("Must not find match in extension for \"Test_file-W1A002F001T0001Z01C1.tif\"",false,resultFalse);
    }

    @Test
    public void testTestSingleExcPartial() {
        // Initialising pattern to match and class to be tested
        Pattern pattern = Pattern.compile("W(\\d+?)F(\\d+?)T(\\d+?)Z(\\d+?)C(\\d+?)");
        NameContainsPattern nameContainsPattern = new NameContainsPattern(pattern,FileCondition.EXC_PARTIAL);

        // Initialising temporary files and folders
        TemporaryFolder temporaryFolder = new TemporaryFolder();
        File testFileTrue = null;
        File testFileFalse = null;

        try {
            // Creating the folder and two files: one which should match and one which shouldn't
            temporaryFolder.create();
            testFileTrue = temporaryFolder.newFile("Test_file-W1F001T0001Z01C1.tif");
            testFileFalse = temporaryFolder.newFile("Test_file-W1A002F001T0001Z01C1.tif");

        } catch (IOException e) {
            e.printStackTrace();

        }

        // Obtaining results for the true and false cases
        boolean resultTrue = nameContainsPattern.test(testFileTrue);
        boolean resultFalse = nameContainsPattern.test(testFileFalse);

        // Checking results
        assertEquals("Must find match for pattern \"Test_file-W1F001T0001Z01C1.tif\" and return false",false,resultTrue);
        assertEquals("Must not find match in extension for \"Test_file-W1A002F001T0001Z01C1.tif\" and return true",true,resultFalse);
    }


    @Test
    public void testTestSingleExcComplete() {
        // Initialising pattern to match and class to be tested
        Pattern pattern = Pattern.compile("W(\\d+?)F(\\d+?)T(\\d+?)Z(\\d+?)C(\\d+?)");
        NameContainsPattern nameContainsPattern = new NameContainsPattern(pattern,FileCondition.EXC_COMPLETE);

        // Initialising temporary files and folders
        TemporaryFolder temporaryFolder = new TemporaryFolder();
        File testFileTrue = null;
        File testFileFalse = null;

        try {
            // Creating the folder and two files: one which should match and one which shouldn't
            temporaryFolder.create();
            testFileTrue = temporaryFolder.newFile("W1F001T0001Z01C1.tif");
            testFileFalse = temporaryFolder.newFile("Test_file-W1F001T0001Z01C1.tif");

        } catch (IOException e) {
            e.printStackTrace();

        }

        // Obtaining results for the true and false cases
        boolean resultTrue = nameContainsPattern.test(testFileTrue);
        boolean resultFalse = nameContainsPattern.test(testFileFalse);

        // Checking results
        assertEquals("Must find match for pattern \"W1F001T0001Z01C1.tif\" and return false",false,resultTrue);
        assertEquals("Must not find match in extension for \"Test_file-W1A002F001T0001Z01C1.tif\" and return true",true,resultFalse);
    }

    @Test
    public void testTestMultiIncPartial() {
        // Initialising pattern to match and class to be tested
        Pattern[] patterns = new Pattern[2];
        patterns[0] = Pattern.compile("W(\\d+?)F(\\d+?)T(\\d+?)Z(\\d+?)C(\\d+?)");
        patterns[1] = Pattern.compile("(.+)_([A-Z]\\d+?)_(\\d++)");
        NameContainsPattern nameContainsPattern = new NameContainsPattern(patterns,FileCondition.INC_PARTIAL);

        // Initialising temporary files and folders
        TemporaryFolder temporaryFolder = new TemporaryFolder();
        File testFileTrue1 = null;
        File testFileTrue2 = null;
        File testFileFalse1 = null;
        File testFileFalse2 = null;

        try {
            // Creating the folder and two files: one which should match and one which shouldn't
            temporaryFolder.create();
            testFileTrue1 = temporaryFolder.newFile("Test_file-W1F001T0001Z01C1.tif");
            testFileTrue2 = temporaryFolder.newFile("Test_file_B2_12.tif");
            testFileFalse1= temporaryFolder.newFile("Test_file-W1A002F001T0001Z01C1.tif");
            testFileTrue2 = temporaryFolder.newFile("Test_file_B2_A2_12.tif");

        } catch (IOException e) {
            e.printStackTrace();

        }

        // Obtaining results for the true and false cases
        boolean resultTrue1 = nameContainsPattern.test(testFileTrue1);
        boolean resultTrue2 = nameContainsPattern.test(testFileTrue2);
        boolean resultFalse1 = nameContainsPattern.test(testFileFalse1);
        boolean resultFalse2 = nameContainsPattern.test(testFileFalse2);

        // Checking results
        assertEquals("Must find match for pattern \"Test_file-W1F001T0001Z01C1.tif\"",true,resultTrue1);
        assertEquals("Must find match for pattern \"Test_file_B2_12.tif\"",true,resultTrue2);
        assertEquals("Must not find match in extension for \"Test_file-W1A002F001T0001Z01C1.tif\"",false,resultFalse1);
        assertEquals("Must not find match in extension for \"Test_file_B2_A2_12.tif\"",false,resultFalse2);

    }

}