package io.github.sjcross.common.fileconditions;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Created by Stephen on 12/04/2017.
 */
public class NameContainsPatternTest {
    @Test
    public void testConstructorSingleNoMode() {
        Pattern pattern = Pattern.compile("[A-Z]+");
        NameContainsPattern nameContainsPattern = new NameContainsPattern(pattern);

        assertEquals(FileCondition.Mode.INC_PARTIAL,nameContainsPattern.getMode());
        assertArrayEquals(new Pattern[]{pattern},nameContainsPattern.getPattern());

    }

    @Test
    public void testConstructorSingleWithMode() {
        Pattern pattern = Pattern.compile("[A-Z]+");
        NameContainsPattern nameContainsPattern = new NameContainsPattern(pattern,FileCondition.Mode.EXC_COMPLETE);

        assertEquals(FileCondition.Mode.EXC_COMPLETE,nameContainsPattern.getMode());
        assertArrayEquals(new Pattern[]{pattern},nameContainsPattern.getPattern());

    }

    @Test
    public void testConstructorMultiNoMode() {
        Pattern pattern1 = Pattern.compile("[A-Z]+");
        Pattern pattern2 = Pattern.compile("([A-Z]+)_([0-1]?)");
        NameContainsPattern nameContainsPattern = new NameContainsPattern(new Pattern[]{pattern1,pattern2});

        assertEquals(FileCondition.Mode.INC_PARTIAL,nameContainsPattern.getMode());
        assertArrayEquals(new Pattern[]{pattern1,pattern2},nameContainsPattern.getPattern());

    }

    @Test
    public void testConstructorMultiWithMode() {
        Pattern pattern1 = Pattern.compile("[A-Z]+");
        Pattern pattern2 = Pattern.compile("([A-Z]+)_([0-1]?)");
        NameContainsPattern nameContainsPattern = new NameContainsPattern(new Pattern[]{pattern1,pattern2},FileCondition.Mode.EXC_PARTIAL);

        assertEquals(FileCondition.Mode.EXC_PARTIAL,nameContainsPattern.getMode());
        assertArrayEquals(new Pattern[]{pattern1,pattern2},nameContainsPattern.getPattern());

    }

    @Test
    public void testTestSingleIncPartialTrueIgnoreCase(@TempDir Path temporaryFolder) {
        // Initialising pattern to match and class to be tested
        Pattern pattern = Pattern.compile("W(\\d+?)F(\\d+?)T(\\d+?)Z(\\d+?)C(\\d+?)");
        NameContainsPattern nameContainsPattern = new NameContainsPattern(pattern,FileCondition.Mode.INC_PARTIAL);

        File testFile = temporaryFolder.resolve("Test_file-W1F001T0001Z01C1.tif").toFile();

        // Obtaining results for the true and false cases
        boolean result = nameContainsPattern.test(testFile,false);

        // Checking results
        assertTrue(result);

    }

    @Test
    public void testTestSingleIncPartialFalseIgnoreCase(@TempDir Path temporaryFolder) {
        // Initialising pattern to match and class to be tested
        Pattern pattern = Pattern.compile("W(\\d+?)F(\\d+?)T(\\d+?)Z(\\d+?)C(\\d+?)");
        NameContainsPattern nameContainsPattern = new NameContainsPattern(pattern,FileCondition.Mode.INC_PARTIAL);

        File testFile = temporaryFolder.resolve("Test_file-W1A002F001T0001Z01C1.tif").toFile();

        // Obtaining results for the true and false cases
        boolean result = nameContainsPattern.test(testFile,false);

        // Checking results
        assertFalse(result);

    }

    @Test
    public void testTestSingleIncPartialEmptyIgnoreCase(@TempDir Path temporaryFolder) {
        // Initialising pattern to match and class to be tested
        NameContainsPattern nameContainsPattern = new NameContainsPattern(new Pattern[0],FileCondition.Mode.INC_PARTIAL);

        File testFile = temporaryFolder.resolve("Test_file-W1F001T0001Z01C1.tif").toFile();

        // Obtaining results for the true and false cases
        boolean result = nameContainsPattern.test(testFile,false);

        // Checking results
        assertFalse(result);

    }

    @Test
    public void testTestSingleIncCompleteTrueIgnoreCase(@TempDir Path temporaryFolder) {
        // Initialising pattern to match and class to be tested
        Pattern pattern = Pattern.compile("W(\\d+?)F(\\d+?)T(\\d+?)Z(\\d+?)C(\\d+?)");
        NameContainsPattern nameContainsPattern = new NameContainsPattern(pattern,FileCondition.Mode.INC_COMPLETE);

        File testFile = temporaryFolder.resolve("W1F001T0001Z01C1.tif").toFile();

        // Obtaining results for the true and false cases
        boolean result = nameContainsPattern.test(testFile,false);

        // Checking results
        assertTrue(result);

    }

    @Test
    public void testTestSingleIncCompleteFalseIgnoreCase(@TempDir Path temporaryFolder) {
        // Initialising pattern to match and class to be tested
        Pattern pattern = Pattern.compile("W(\\d+?)F(\\d+?)T(\\d+?)Z(\\d+?)C(\\d+?)");
        NameContainsPattern nameContainsPattern = new NameContainsPattern(pattern,FileCondition.Mode.INC_COMPLETE);

        File testFile = temporaryFolder.resolve("Test_file-W1F001T0001Z01C1.tif").toFile();

        // Obtaining results for the true and false cases
        boolean result = nameContainsPattern.test(testFile,false);

        // Checking results
        assertFalse(result);

    }

    @Test
    public void testTestSingleIncCompleteEmptyIgnoreCase(@TempDir Path temporaryFolder) {
        // Initialising pattern to match and class to be tested
        NameContainsPattern nameContainsPattern = new NameContainsPattern(new Pattern[0],FileCondition.Mode.INC_COMPLETE);

        File testFile = temporaryFolder.resolve("Test_file-W1F001T0001Z01C1.tif").toFile();

        // Obtaining results for the true and false cases
        boolean result = nameContainsPattern.test(testFile,false);

        // Checking results
        assertFalse(result);

    }

    @Test
    public void testTestSingleExcCompleteTrueIgnoreCase(@TempDir Path temporaryFolder) {
        // Initialising pattern to match and class to be tested
        Pattern pattern = Pattern.compile("W(\\d+?)F(\\d+?)T(\\d+?)Z(\\d+?)C(\\d+?)");
        NameContainsPattern nameContainsPattern = new NameContainsPattern(pattern,FileCondition.Mode.EXC_COMPLETE);

        File testFile = temporaryFolder.resolve("Test_file-W1F001T0001Z01C1.tif").toFile();

        // Obtaining results for the true and false cases
        boolean result = nameContainsPattern.test(testFile,false);

        // Checking results
        assertTrue(result);

    }

    @Test
    public void testTestSingleExcCompleteFalseIgnoreCase(@TempDir Path temporaryFolder) {
        // Initialising pattern to match and class to be tested
        Pattern pattern = Pattern.compile("W(\\d+?)F(\\d+?)T(\\d+?)Z(\\d+?)C(\\d+?)");
        NameContainsPattern nameContainsPattern = new NameContainsPattern(pattern,FileCondition.Mode.EXC_COMPLETE);

        File testFile = temporaryFolder.resolve("W1F001T0001Z01C1.tif").toFile();

        // Obtaining results for the true and false cases
        boolean result = nameContainsPattern.test(testFile,false);

        // Checking results
        assertFalse(result);

    }

    @Test
    public void testTestSingleExcCompleteEmptyIgnoreCase(@TempDir Path temporaryFolder) {
        // Initialising pattern to match and class to be tested
        NameContainsPattern nameContainsPattern = new NameContainsPattern(new Pattern[0],FileCondition.Mode.EXC_COMPLETE);

        File testFile = temporaryFolder.resolve("Test_file-W1F001T0001Z01C1.tif").toFile();

        // Obtaining results for the true and false cases
        boolean result = nameContainsPattern.test(testFile,false);

        // Checking results
        assertTrue(result);

    }

    @Test
    public void testTestSingleExcPartialTrueIgnoreCase(@TempDir Path temporaryFolder) {
        // Initialising pattern to match and class to be tested
        Pattern pattern = Pattern.compile("W(\\d+?)F(\\d+?)T(\\d+?)Z(\\d+?)C(\\d+?)");
        NameContainsPattern nameContainsPattern = new NameContainsPattern(pattern,FileCondition.Mode.EXC_PARTIAL);

        File testFile = temporaryFolder.resolve("Test_file-W1A002F001T0001Z01C1.tif").toFile();

        // Obtaining results for the true and false cases
        boolean result = nameContainsPattern.test(testFile,false);

        // Checking results
        assertTrue(result);

    }

    @Test
    public void testTestSingleExcPartialFalseIgnoreCase(@TempDir Path temporaryFolder) {
        // Initialising pattern to match and class to be tested
        Pattern pattern = Pattern.compile("W(\\d+?)F(\\d+?)T(\\d+?)Z(\\d+?)C(\\d+?)");
        NameContainsPattern nameContainsPattern = new NameContainsPattern(pattern,FileCondition.Mode.EXC_PARTIAL);

        File testFile = temporaryFolder.resolve("Test_file-W1F001T0001Z01C1.tif").toFile();

        // Obtaining results for the true and false cases
        boolean result = nameContainsPattern.test(testFile,false);

        // Checking results
        assertFalse(result);

    }

    @Test
    public void testTestSingleExcPartialEmptyIgnoreCase(@TempDir Path temporaryFolder) {
        // Initialising pattern to match and class to be tested
        NameContainsPattern nameContainsPattern = new NameContainsPattern(new Pattern[0],FileCondition.Mode.EXC_PARTIAL);

        File testFile = temporaryFolder.resolve("Test_file-W1A002F001T0001Z01C1.tif").toFile();

        // Obtaining results for the true and false cases
        boolean result = nameContainsPattern.test(testFile,false);

        // Checking results
        assertTrue(result);

    }

    @Test
    public void testTestMultiIncPartialIgnoreCase(@TempDir Path temporaryFolder) {
        // Initialising pattern to match and class to be tested
        Pattern[] patterns = new Pattern[2];
        patterns[0] = Pattern.compile("W(\\d+?)F(\\d+?)T(\\d+?)Z(\\d+?)C(\\d+?)");
        patterns[1] = Pattern.compile("(.+)_([A-Z]\\d+?)_(\\d++)");
        NameContainsPattern nameContainsPattern = new NameContainsPattern(patterns,FileCondition.Mode.INC_PARTIAL);

        File testFileTrue1 = temporaryFolder.resolve("Test_file-W1F001T0001Z01C1.tif").toFile();
        File testFileTrue2 = temporaryFolder.resolve("Test_file_B2_12.tif").toFile();
        File testFileFalse1 = temporaryFolder.resolve("Test_file-W1A002F001T0001Z01C1.tif").toFile();
        File testFileFalse2 = temporaryFolder.resolve("Test_file_B2_A2.tif").toFile();

        // Obtaining results for the true and false cases
        boolean resultTrue1 = nameContainsPattern.test(testFileTrue1,false);
        boolean resultTrue2 = nameContainsPattern.test(testFileTrue2,false);
        boolean resultFalse1 = nameContainsPattern.test(testFileFalse1,false);
        boolean resultFalse2 = nameContainsPattern.test(testFileFalse2,false);

        // Checking results
        assertTrue(resultTrue1);
        assertTrue(resultTrue2);
        assertFalse(resultFalse1);
        assertFalse(resultFalse2);

    }
}