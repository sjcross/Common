package io.github.sjcross.common.FileConditions;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Created by Stephen on 12/04/2017.
 */
public class ExtensionMatchesStringTest {
    @Test
    public void testConstructorSingleNoMode() {
        ExtensionMatchesString extensionMatchesString = new ExtensionMatchesString("test Ext");

        assertEquals(FileCondition.Mode.INC_PARTIAL,extensionMatchesString.getMode());
        assertArrayEquals(new String[]{"test Ext"},extensionMatchesString.getExts());

    }

    @Test
    public void testConstructorSingleWithMode() {
        ExtensionMatchesString extensionMatchesString = new ExtensionMatchesString("test Ext",FileCondition.Mode.EXC_COMPLETE);

        assertEquals(FileCondition.Mode.EXC_COMPLETE,extensionMatchesString.getMode());
        assertArrayEquals(new String[]{"test Ext"},extensionMatchesString.getExts());

    }

    @Test
    public void testConstructorMultiNoMode() {
        ExtensionMatchesString extensionMatchesString = new ExtensionMatchesString(new String[]{"test Ext","ext 2"});

        assertEquals(FileCondition.Mode.INC_PARTIAL,extensionMatchesString.getMode());
        assertArrayEquals(new String[]{"test Ext","ext 2"},extensionMatchesString.getExts());

    }

    @Test
    public void testConstructorMultiWithMode() {
        ExtensionMatchesString extensionMatchesString = new ExtensionMatchesString(new String[]{"test Ext","ext 2"},FileCondition.Mode.EXC_PARTIAL);

        assertEquals(FileCondition.Mode.EXC_PARTIAL,extensionMatchesString.getMode());
        assertArrayEquals(new String[]{"test Ext","ext 2"},extensionMatchesString.getExts());

    }

    @Test
    public void testTestIncludesCompleteTrue(@TempDir Path temporaryFolder) {
        // Initialising the class to be tested
        String[] exts = new String[]{"tif","tiff"};
        ExtensionMatchesString extensionMatchesString = new ExtensionMatchesString(exts,FileCondition.Mode.INC_COMPLETE);

        // Initialising temporary files and folders
        File testFileTrue1 = temporaryFolder.resolve("test.tif").toFile();
        File testFileTrue2 = temporaryFolder.resolve("test.tiff").toFile();

        // Obtaining results for the true and false cases
        boolean resultTrue1 = extensionMatchesString.test(testFileTrue1);
        boolean resultTrue2 = extensionMatchesString.test(testFileTrue2);

        // Checking results
        assertTrue(resultTrue1);
        assertTrue(resultTrue2);

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
        File testFileTrue1 = temporaryFolder.resolve("test.tifg").toFile();
        File testFileTrue2 = temporaryFolder.resolve("test.atiffg").toFile();

        // Obtaining results for the true and false cases
        boolean resultTrue1 = extensionMatchesString.test(testFileTrue1);
        boolean resultTrue2 = extensionMatchesString.test(testFileTrue2);

        // Checking results
        assertTrue(resultTrue1);
        assertTrue(resultTrue2);

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

    @Test
    public void testTestExcludesCompleteTrue(@TempDir Path temporaryFolder) {
        // Initialising the class to be tested
        String[] exts = new String[]{"tif","tiff"};
        ExtensionMatchesString extensionMatchesString = new ExtensionMatchesString(exts,FileCondition.Mode.EXC_COMPLETE);

        // Initialising temporary files and folders
        File testFileTrue1 = temporaryFolder.resolve("test.taf").toFile();
        File testFileTrue2 = temporaryFolder.resolve("test.tifD").toFile();

        // Obtaining results for the true and false cases
        boolean resultTrue1 = extensionMatchesString.test(testFileTrue1);
        boolean resultTrue2 = extensionMatchesString.test(testFileTrue2);

        // Checking results
        assertTrue(resultTrue1);
        assertTrue(resultTrue2);

    }

    @Test
    public void testTestExcludesCompleteFalse(@TempDir Path temporaryFolder) {
        // Initialising the class to be tested
        String[] exts = new String[]{"tif","tiff"};
        ExtensionMatchesString extensionMatchesString = new ExtensionMatchesString(exts,FileCondition.Mode.EXC_COMPLETE);

        // Initialising temporary files and folders
        File testFileFalse1 = temporaryFolder.resolve("test.tif").toFile();
        File testFileFalse2 = temporaryFolder.resolve("test.tiff").toFile();

        // Obtaining results for the true and false cases
        boolean resultFalse1 = extensionMatchesString.test(testFileFalse1);
        boolean resultFalse2 = extensionMatchesString.test(testFileFalse2);

        // Checking results
        assertFalse(resultFalse1);
        assertFalse(resultFalse2);

    }

    @Test
    public void testTestExcludesPartialTrue(@TempDir Path temporaryFolder) {
        // Initialising the class to be tested
        String[] exts = new String[]{"tif","tiff"};
        ExtensionMatchesString extensionMatchesString = new ExtensionMatchesString(exts, FileCondition.Mode.EXC_PARTIAL);

        // Initialising temporary files and folders
        File testFileTrue = temporaryFolder.resolve("test.taf").toFile();

        // Obtaining results for the true and false cases
        boolean resultTrue = extensionMatchesString.test(testFileTrue);

        // Checking results
        assertTrue(resultTrue);

    }

    @Test
    public void testTestExcludesPartialFalse(@TempDir Path temporaryFolder) {
        // Initialising the class to be tested
        String[] exts = new String[]{"tif","tiff"};
        ExtensionMatchesString extensionMatchesString = new ExtensionMatchesString(exts,FileCondition.Mode.EXC_PARTIAL);

        // Initialising temporary files and folders
        File testFileFalse1 = temporaryFolder.resolve("test.tif").toFile();
        File testFileFalse2 = temporaryFolder.resolve("test.tifg").toFile();
        File testFileFalse3 = temporaryFolder.resolve("test.atift").toFile();

        // Obtaining results for the true and false cases
        boolean resultFalse1 = extensionMatchesString.test(testFileFalse1);
        boolean resultFalse2 = extensionMatchesString.test(testFileFalse2);
        boolean resultFalse3 = extensionMatchesString.test(testFileFalse3);

        // Checking results
        assertFalse(resultFalse1);
        assertFalse(resultFalse2);
        assertFalse(resultFalse3);

    }
}