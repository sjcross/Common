//TODO: Conditions for files to meet (e.g. filename contains) - conditions could be a separate class
//TODO: Folder names to skip (don't read files in this folder) - conditions could be a separate class
//TODO: Verbose option on System.out.println

package wolfson.common.System;

import java.io.File;

/**
 * Created by Stephen on 16/10/2016.
 */
public class FileCrawler {
    private Folder root_folder = null; //Root folder
    private Folder folder = null; //Current folder

    public FileCrawler(File root) {
        folder = new Folder(root,null);
        root_folder = folder;

    }

    public Folder getCurrentFolderAsFolder() {
        return folder;

    }

    public File getCurrentFolderAsFile() {
        return folder.getFolder();

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

    public File getNextFileInFolder() {
        return folder.getNextFile();

    }

    public File getNextFileInStructure() {
        if (hasMoreFilesInFolder()) {
            return getNextFileInFolder();

        } else {
            while(goToNextFolder()) {
                if (hasMoreFilesInFolder()) {
                    return getNextFileInFolder();

                }
            }
        }

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

    /**
     * Sets the current folder as the next folder in the structure.
     * @return true if there was a next folder and false if the end of the structure has been reached
     */
    public boolean goToNextFolder() {
        Folder next_folder = folder.getNextFolder();

        if (next_folder != null) {
            folder = next_folder;
            System.out.println(">>> "+folder.getFolder().getAbsolutePath());

        } else { //Reached deepest point, so go to current folder's parent
            folder = folder.getParent();
            if (folder != null) System.out.println("<<< "+folder.getFolder().getAbsolutePath());
        }

        boolean end = true;
        if (folder == null) {
            end = false;

        }

        return end;

    }
}