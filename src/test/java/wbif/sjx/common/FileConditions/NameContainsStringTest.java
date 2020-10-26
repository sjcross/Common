package wbif.sjx.common.FileConditions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class NameContainsStringTest {
    @Test
    public void testConstructorSingleNoMode() {
        NameContainsString nameContainsString = new NameContainsString("test Ext");

        assertEquals(FileCondition.Mode.INC_PARTIAL,nameContainsString.getMode());
        assertArrayEquals(new String[]{"test Ext"},nameContainsString.getTestStr());

    }

    @Test
    public void testConstructorSingleWithMode() {
        NameContainsString nameContainsString = new NameContainsString("test Ext",FileCondition.Mode.EXC_COMPLETE);

        assertEquals(FileCondition.Mode.EXC_COMPLETE,nameContainsString.getMode());
        assertArrayEquals(new String[]{"test Ext"},nameContainsString.getTestStr());

    }

    @Test
    public void testConstructorMultiNoMode() {
        NameContainsString nameContainsString = new NameContainsString(new String[]{"test Ext","ext 2"});

        assertEquals(FileCondition.Mode.INC_PARTIAL,nameContainsString.getMode());
        assertArrayEquals(new String[]{"test Ext","ext 2"},nameContainsString.getTestStr());

    }

    @Test
    public void testConstructorMultiWithMode() {
        NameContainsString nameContainsString = new NameContainsString(new String[]{"test Ext","ext 2"},FileCondition.Mode.EXC_PARTIAL);

        assertEquals(FileCondition.Mode.EXC_PARTIAL,nameContainsString.getMode());
        assertArrayEquals(new String[]{"test Ext","ext 2"},nameContainsString.getTestStr());

    }

    @Test
    public void testTestIncludesCompleteTrue(@TempDir Path temporaryFolder) {
        // Initialising the class to be tested
        String[] exts = new String[]{"test 34","another Test"};
        NameContainsString nameContainsString = new NameContainsString(exts,FileCondition.Mode.INC_COMPLETE);

        // Initialising temporary files and folders
        File testFile1 = temporaryFolder.resolve("test 34.tif").toFile();
        File testFile2 = temporaryFolder.resolve("another Test.tiff").toFile();

        // Obtaining results for the true and false cases
        boolean result1 = nameContainsString.test(testFile1);
        boolean result2 = nameContainsString.test(testFile2);

        // Checking results
        assertTrue(result1);
        assertTrue(result2);

    }

    @Test
    public void testTestIncludesCompleteFalse1(@TempDir Path temporaryFolder) {
        // Initialising the class to be tested
        String[] exts = new String[]{"test 34.tif","another Test.tiff","tif","tiff"};
        NameContainsString nameContainsString = new NameContainsString(exts,FileCondition.Mode.INC_COMPLETE);

        // Initialising temporary files and folders
        File testFile1 = temporaryFolder.resolve("test 34.tif").toFile();
        File testFile2 = temporaryFolder.resolve("another Test.tiff").toFile();

        // Obtaining results for the true and false cases
        boolean result1 = nameContainsString.test(testFile1);
        boolean result2 = nameContainsString.test(testFile2);

        // Checking results
        assertFalse(result1);
        assertFalse(result2);

    }

    @Test
    public void testTestIncludesCompleteFalse2(@TempDir Path temporaryFolder) {
        // Initialising the class to be tested
        String[] exts = new String[]{"test 34.tif","another Test.tiff"};
        NameContainsString nameContainsString = new NameContainsString(exts,FileCondition.Mode.INC_COMPLETE);

        // Initialising temporary files and folders
        File testFile1 = temporaryFolder.resolve("test 34").toFile();
        File testFile2 = temporaryFolder.resolve("another Test").toFile();
        File testFile3 = temporaryFolder.resolve("tif").toFile();
        File testFile4 = temporaryFolder.resolve("tiff").toFile();

        // Obtaining results for the true and false cases
        boolean result1 = nameContainsString.test(testFile1);
        boolean result2 = nameContainsString.test(testFile2);
        boolean result3 = nameContainsString.test(testFile3);
        boolean result4 = nameContainsString.test(testFile4);

        // Checking results
        assertFalse(result1);
        assertFalse(result2);
        assertFalse(result3);
        assertFalse(result4);

    }

    @Test
    public void testTestIncludesCompleteEmpty(@TempDir Path temporaryFolder) {
        // Initialising the class to be tested
        String[] exts = new String[0];
        NameContainsString nameContainsString = new NameContainsString(exts,FileCondition.Mode.INC_COMPLETE);

        // Initialising temporary files and folders
        File testFile1 = temporaryFolder.resolve("test 34").toFile();
        File testFile2 = temporaryFolder.resolve("another Test").toFile();
        File testFile3 = temporaryFolder.resolve("tif").toFile();
        File testFile4 = temporaryFolder.resolve("tiff").toFile();

        // Obtaining results for the true and false cases
        boolean result1 = nameContainsString.test(testFile1);
        boolean result2 = nameContainsString.test(testFile2);
        boolean result3 = nameContainsString.test(testFile3);
        boolean result4 = nameContainsString.test(testFile4);

        // Checking results
        assertFalse(result1);
        assertFalse(result2);
        assertFalse(result3);
        assertFalse(result4);

    }

    @Test
    public void testTestIncludesPartialTrue(@TempDir Path temporaryFolder) {
        // Initialising the class to be tested
        String[] exts = new String[]{"test 34","another Test","tif","tiff"};
        NameContainsString nameContainsString = new NameContainsString(exts,FileCondition.Mode.INC_PARTIAL);

        // Initialising temporary files and folders
        File testFile1 = temporaryFolder.resolve("test 34.tif").toFile();
        File testFile2 = temporaryFolder.resolve("another Test.tiff").toFile();

        // Obtaining results for the true and false cases
        boolean result1 = nameContainsString.test(testFile1);
        boolean result2 = nameContainsString.test(testFile2);

        // Checking results
        assertTrue(result1);
        assertTrue(result2);

    }

    @Test
    public void testTestIncludesPartialFalse(@TempDir Path temporaryFolder) {
        // Initialising the class to be tested
        String[] exts = new String[]{"test 34.tif","another Test.tiff"};
        NameContainsString nameContainsString = new NameContainsString(exts,FileCondition.Mode.INC_PARTIAL);

        // Initialising temporary files and folders
        File testFile1 = temporaryFolder.resolve("test 34").toFile();
        File testFile2 = temporaryFolder.resolve("another Test").toFile();
        File testFile3 = temporaryFolder.resolve("tif").toFile();
        File testFil4 = temporaryFolder.resolve("tiff").toFile();

        // Obtaining results for the true and false cases
        boolean result1 = nameContainsString.test(testFile1);
        boolean result2 = nameContainsString.test(testFile2);
        boolean result3 = nameContainsString.test(testFile3);
        boolean result4 = nameContainsString.test(testFil4);

        // Checking results
        assertFalse(result1);
        assertFalse(result2);
        assertFalse(result3);
        assertFalse(result4);

    }

    @Test
    public void testTestIncludesPartialEmpty(@TempDir Path temporaryFolder) {
        // Initialising the class to be tested
        String[] exts = new String[0];
        NameContainsString nameContainsString = new NameContainsString(exts,FileCondition.Mode.INC_PARTIAL);

        // Initialising temporary files and folders
        File testFile1 = temporaryFolder.resolve("test 34").toFile();
        File testFile2 = temporaryFolder.resolve("another Test").toFile();
        File testFile3 = temporaryFolder.resolve("tif").toFile();
        File testFil4 = temporaryFolder.resolve("tiff").toFile();

        // Obtaining results for the true and false cases
        boolean result1 = nameContainsString.test(testFile1);
        boolean result2 = nameContainsString.test(testFile2);
        boolean result3 = nameContainsString.test(testFile3);
        boolean result4 = nameContainsString.test(testFil4);

        // Checking results
        assertFalse(result1);
        assertFalse(result2);
        assertFalse(result3);
        assertFalse(result4);

    }

    @Test
    public void testTestExcludesCompleteTrue(@TempDir Path temporaryFolder) {
        // Initialising the class to be tested
        String[] exts = new String[]{"test 34","another Test","tif","tiff"};
        NameContainsString nameContainsString = new NameContainsString(exts,FileCondition.Mode.EXC_COMPLETE);

        // Initialising temporary files and folders
        File testFile1 = temporaryFolder.resolve("test 34.tif").toFile();
        File testFile2 = temporaryFolder.resolve("another Test.tiff").toFile();
        File testFile3 = temporaryFolder.resolve("another Test tif.tiff").toFile();

        // Obtaining results for the true and false cases
        boolean result1 = nameContainsString.test(testFile1);
        boolean result2 = nameContainsString.test(testFile2);
        boolean result3 = nameContainsString.test(testFile3);

        // Checking results
        assertFalse(result1);
        assertFalse(result2);
        assertTrue(result3);

    }

    @Test
    public void testTestExcludesCompleteFalse(@TempDir Path temporaryFolder) {
        // Initialising the class to be tested
        String[] exts = new String[]{"test 34","another Test"};
        NameContainsString nameContainsString = new NameContainsString(exts,FileCondition.Mode.EXC_COMPLETE);

        // Initialising temporary files and folders
        File testFile1 = temporaryFolder.resolve("test 34.tif").toFile();
        File testFile2 = temporaryFolder.resolve("another Test.tiff").toFile();

        // Obtaining results for the true and false cases
        boolean result1 = nameContainsString.test(testFile1);
        boolean result2 = nameContainsString.test(testFile2);

        // Checking results
        assertFalse(result1);
        assertFalse(result2);

    }

    @Test
    public void testTestExcludesCompleteEmpty(@TempDir Path temporaryFolder) {
        // Initialising the class to be tested
        String[] exts = new String[0];
        NameContainsString nameContainsString = new NameContainsString(exts,FileCondition.Mode.EXC_COMPLETE);

        // Initialising temporary files and folders
        File testFile1 = temporaryFolder.resolve("test 34.tif").toFile();
        File testFile2 = temporaryFolder.resolve("another Test.tiff").toFile();

        // Obtaining results for the true and false cases
        boolean result1 = nameContainsString.test(testFile1);
        boolean result2 = nameContainsString.test(testFile2);

        // Checking results
        assertTrue(result1);
        assertTrue(result2);

    }

    @Test
    public void testTestExcludesPartialTrue(@TempDir Path temporaryFolder) {
        // Initialising the class to be tested
        String[] exts = new String[]{"tet 34","aother Test"};
        NameContainsString nameContainsString = new NameContainsString(exts,FileCondition.Mode.EXC_PARTIAL);

        // Initialising temporary files and folders
        File testFile1 = temporaryFolder.resolve("test 34.tif").toFile();
        File testFile2 = temporaryFolder.resolve("another Test.tiff").toFile();

        // Obtaining results for the true and false cases
        boolean result1 = nameContainsString.test(testFile1);
        boolean result2 = nameContainsString.test(testFile2);

        // Checking results
        assertTrue(result1);
        assertTrue(result2);

    }

    @Test
    public void testTestExcludesPartialFalse(@TempDir Path temporaryFolder) {
        // Initialising the class to be tested
        String[] exts = new String[]{"test 34","another Test","tif","tiff"};
        NameContainsString nameContainsString = new NameContainsString(exts,FileCondition.Mode.EXC_PARTIAL);

        // Initialising temporary files and folders
        File testFile1 = temporaryFolder.resolve("test 34.tif").toFile();
        File testFile2 = temporaryFolder.resolve("another Test.tiff").toFile();

        // Obtaining results for the true and false cases
        boolean result1 = nameContainsString.test(testFile1);
        boolean result2 = nameContainsString.test(testFile2);

        // Checking results
        assertFalse(result1);
        assertFalse(result2);

    }

    @Test
    public void testTestExcludesPartialEmpty(@TempDir Path temporaryFolder) {
        // Initialising the class to be tested
        String[] exts = new String[0];
        NameContainsString nameContainsString = new NameContainsString(exts,FileCondition.Mode.EXC_PARTIAL);

        // Initialising temporary files and folders
        File testFile1 = temporaryFolder.resolve("test 34.tif").toFile();
        File testFile2 = temporaryFolder.resolve("another Test.tiff").toFile();

        // Obtaining results for the true and false cases
        boolean result1 = nameContainsString.test(testFile1);
        boolean result2 = nameContainsString.test(testFile2);

        // Checking results
        assertTrue(result1);
        assertTrue(result2);

    }
}
