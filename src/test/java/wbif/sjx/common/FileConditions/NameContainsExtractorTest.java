package wbif.sjx.common.FileConditions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by steph on 12/04/2017.
 */
public class NameContainsExtractorTest {
    @Test
    public void testTestSingleIncPartial(@TempDir Path temporaryFolder) {
        // Initialising pattern to match and class to be tested
        Pattern pattern = Pattern.compile("W(\\d+?)F(\\d+?)T(\\d+?)Z(\\d+?)C(\\d+?)");
        NameContainsPattern nameContainsPattern = new NameContainsPattern(pattern,FileCondition.INC_PARTIAL);

        File testFileTrue = temporaryFolder.resolve("Test_file-W1F001T0001Z01C1.tif").toFile();
        File testFileFalse = temporaryFolder.resolve("Test_file-W1A002F001T0001Z01C1.tif").toFile();

        // Obtaining results for the true and false cases
        boolean resultTrue = nameContainsPattern.test(testFileTrue);
        boolean resultFalse = nameContainsPattern.test(testFileFalse);

        // Checking results
        assertEquals(true,resultTrue);
        assertEquals(false,resultFalse);
    }

    @Test
    public void testTestSingleIncComplete(@TempDir Path temporaryFolder) {
        // Initialising pattern to match and class to be tested
        Pattern pattern = Pattern.compile("W(\\d+?)F(\\d+?)T(\\d+?)Z(\\d+?)C(\\d+?)");
        NameContainsPattern nameContainsPattern = new NameContainsPattern(pattern,FileCondition.INC_COMPLETE);

        File testFileTrue = temporaryFolder.resolve("W1F001T0001Z01C1.tif").toFile();
        File testFileFalse = temporaryFolder.resolve("Test_file-W1F001T0001Z01C1.tif").toFile();

        // Obtaining results for the true and false cases
        boolean resultTrue = nameContainsPattern.test(testFileTrue);
        boolean resultFalse = nameContainsPattern.test(testFileFalse);

        // Checking results
        assertEquals(true,resultTrue);
        assertEquals(false,resultFalse);
    }

    @Test
    public void testTestSingleExcPartial(@TempDir Path temporaryFolder) {
        // Initialising pattern to match and class to be tested
        Pattern pattern = Pattern.compile("W(\\d+?)F(\\d+?)T(\\d+?)Z(\\d+?)C(\\d+?)");
        NameContainsPattern nameContainsPattern = new NameContainsPattern(pattern,FileCondition.EXC_PARTIAL);

        File testFileTrue = temporaryFolder.resolve("Test_file-W1F001T0001Z01C1.tif").toFile();
        File testFileFalse = temporaryFolder.resolve("Test_file-W1A002F001T0001Z01C1.tif").toFile();

        // Obtaining results for the true and false cases
        boolean resultTrue = nameContainsPattern.test(testFileTrue);
        boolean resultFalse = nameContainsPattern.test(testFileFalse);

        // Checking results
        assertEquals(false,resultTrue);
        assertEquals(true,resultFalse);

    }

    @Test
    public void testTestSingleExcComplete(@TempDir Path temporaryFolder) {
        // Initialising pattern to match and class to be tested
        Pattern pattern = Pattern.compile("W(\\d+?)F(\\d+?)T(\\d+?)Z(\\d+?)C(\\d+?)");
        NameContainsPattern nameContainsPattern = new NameContainsPattern(pattern,FileCondition.EXC_COMPLETE);

        File testFileTrue = temporaryFolder.resolve("W1F001T0001Z01C1.tif").toFile();
        File testFileFalse = temporaryFolder.resolve("Test_file-W1F001T0001Z01C1.tif").toFile();

        // Obtaining results for the true and false cases
        boolean resultTrue = nameContainsPattern.test(testFileTrue);
        boolean resultFalse = nameContainsPattern.test(testFileFalse);

        // Checking results
        assertEquals(false,resultTrue);
        assertEquals(true,resultFalse);

    }

    @Test
    public void testTestMultiIncPartial(@TempDir Path temporaryFolder) {
        // Initialising pattern to match and class to be tested
        Pattern[] patterns = new Pattern[2];
        patterns[0] = Pattern.compile("W(\\d+?)F(\\d+?)T(\\d+?)Z(\\d+?)C(\\d+?)");
        patterns[1] = Pattern.compile("(.+)_([A-Z]\\d+?)_(\\d++)");
        NameContainsPattern nameContainsPattern = new NameContainsPattern(patterns,FileCondition.INC_PARTIAL);

        File testFileTrue1 = temporaryFolder.resolve("Test_file-W1F001T0001Z01C1.tif").toFile();
        File testFileTrue2 = temporaryFolder.resolve("Test_file_B2_12.tif").toFile();
        File testFileFalse1 = temporaryFolder.resolve("Test_file-W1A002F001T0001Z01C1.tif").toFile();
        File testFileFalse2 = temporaryFolder.resolve("Test_file_B2_A2.tif").toFile();

        // Obtaining results for the true and false cases
        boolean resultTrue1 = nameContainsPattern.test(testFileTrue1);
        boolean resultTrue2 = nameContainsPattern.test(testFileTrue2);
        boolean resultFalse1 = nameContainsPattern.test(testFileFalse1);
        boolean resultFalse2 = nameContainsPattern.test(testFileFalse2);

        // Checking results
        assertEquals(true,resultTrue1);
        assertEquals(true,resultTrue2);
        assertEquals(false,resultFalse1);
        assertEquals(false,resultFalse2);

    }
}