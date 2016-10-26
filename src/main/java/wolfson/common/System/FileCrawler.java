//TODO: Verbose option on System.out.println

package wolfson.common.System;

import wolfson.common.FileConditions.FileCondition;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Stephen on 16/10/2016.
 */
public class FileCrawler {
    Folder root_folder = null; //Root folder
    Folder folder = null; //Current folder
    private HashSet<FileCondition> file_conditions = new HashSet<FileCondition>(); //List of file conditions
    private HashSet<FileCondition> folder_conditions = new HashSet<FileCondition>(); //List of folder conditions

    public FileCrawler(File root) {
        folder = new Folder(root,null);
        root_folder = folder;

    }

    public Folder getCurrentFolderAsFolder() {
        return folder;

    }

    public File getCurrentFolderAsFile() {
        return folder.getFolderAsFile();

    }

    /**
     * Runs through all folders, listing files and folder
     */
    public void scanFolders() {
        while(folder.getParent() != null | folder.hasMoreFolders()) {
            Folder next_folder = folder.getNextFolder();

            if (next_folder != null) {
                next_folder.printFiles();
                folder = next_folder;

            } else { //Reached deepest point, so go to current folder's parent
                folder = folder.getParent();

            }
        }

        System.out.println("End of file structure");
        System.out.println(" ");

    }

    public boolean hasMoreFilesInFolder() {
        return folder.hasMoreFiles();

    }

    public boolean hasMoreFoldersInFolder() {
        return folder.hasMoreFolders();

    }

    public File getNextValidFileInFolder() {
        File file = folder.getNextFile();

        // While the current file doesn't match the conditions, but isn't null go to the next file
        while ((!testFileConditions(file)) & (file != null)) {
            file = folder.getNextFile();

        }

        return file;
    }

    public File getNextFileInFolder() {
        return folder.getNextFile();

    }

    public File getNextFileInStructure() {
        // First, attempt to return the next file in the current folder
        File next_file = getNextValidFileInFolder();
        if (next_file != null) {
            return next_file;

        }

        // Now, test the files in the remaining folder
        while(goToNextValidFolder()) {
            next_file = getNextValidFileInFolder();
            if (next_file != null) {
                return next_file;

            }
        }

        // Failing this, there are no files left, so return null
        return null;

    }

    /**
     * Depth of the folder relative to the root folder.  The root folder has a depth of 0
     * @return depth of the current folder
     */
    public int getCurrentDepth() {
        int depth = 0;

        Folder parent = folder.getParent();
        while (parent != null) {
            depth++;
            parent = parent.getParent();
        }

        return depth;

    }

    public boolean goToNextValidFolder() {
        boolean hasmore = goToNextFolder();

        while (hasmore) {
            if (testFolderConditions(folder.getFolderAsFile())) {
                return hasmore;
            }

            hasmore = goToNextFolder();

        }

        return false;

    }

    /**
     * Sets the current folder as the next folder in the structure.
     * @return true if there was a next folder and false if the end of the structure has been reached
     */
    public boolean goToNextFolder() {
        Folder next_folder = folder.getNextFolder();

        if (next_folder != null) {
            folder = next_folder;
            System.out.println(">>> "+folder.getFolderAsFile().getAbsolutePath());

        } else { //Reached deepest point, so go to current folder's parent
            folder = folder.getParent();
            if (folder != null) System.out.println("<<< "+folder.getFolderAsFile().getAbsolutePath());
        }

        // Returns a condition stating if there are more folders to go
        boolean hasmore = true;
        if (folder == null) {
            hasmore = false;

        }

        return hasmore;

    }

    public void addFileCondition(FileCondition file_condition) {
        file_conditions.add(file_condition);

    }

    public void addFolderCondition(FileCondition folder_condition) {
        folder_conditions.add(folder_condition);

    }

    /**
     * Runs through any specified file conditions and returns true if all are met
     * @return
     */
    public boolean testFileConditions(File test_file) {
        boolean cnd = true;

        if (file_conditions != null) {
            Iterator<FileCondition> iterator = file_conditions.iterator();
            while(iterator.hasNext()) {
                //If any condition fails, the output is false
                if (!iterator.next().test(test_file)) cnd = false;

            }
        }

        return cnd;

    }

    /**
     * Runs through any specified folder conditions and returns true if all are met
     * @return
     */
    public boolean testFolderConditions(File test_folder) {
        boolean cnd = true;

        if (folder_conditions != null) {
            Iterator<FileCondition> iterator = folder_conditions.iterator();
            while (iterator.hasNext()) {
                //If any condition fails, the output is false
                if (!iterator.next().test(test_folder)) cnd = false;

            }
        }

        return cnd;

    }
}