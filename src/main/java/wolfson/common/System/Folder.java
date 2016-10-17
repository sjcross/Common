package wolfson.common.System;

import java.io.File;

/**
 * FileCrawler File
 * Created by Stephen on 16/10/2016.
 */
public class Folder {
    private final File folder;
    private final Folder parent;
    private File[] files;
    private File[] folders;
    private int file_count = 0; //Current file count
    private int folder_count = 0; //Current folder count
    private int n_folders = 0; //Number of children folders
    private int n_files = 0; //Number of children files

    public Folder(File folder, Folder parent) {
        this.folder = folder;
        this.parent = parent;

        File[] file_list = folder.listFiles();

        //Counting the number of files and folders
        if (file_list != null) {
            for (int i = 0; i < file_list.length; i++) {
                if (file_list[i].isDirectory()) {
                    n_folders++;
                } else {
                    n_files++;
                }
            }

            System.out.println(folder.getName()+" has "+n_files+" files and "+n_folders+" folders");

            //Creating arrays for files and folders
            files = new File[n_files];
            folders = new File[n_folders];
            for (int i = 0; i < file_list.length; i++) {
                if (file_list[i].isDirectory()) {
                    folders[folder_count] = file_list[i];
                    folder_count++;
                } else {
                    files[file_count] = file_list[i];
                    System.out.println("    Files: "+files[file_count].getName());
                    file_count++;
                }
            }

        } else {
            System.out.println(folder.getName()+" is empty");

        }

        //Resetting the counters
        file_count = 0;
        folder_count = 0;

    }

    public File getFolder() {
        return folder;

    }

    public Folder getParent() {
        return parent;

    }

    public boolean hasMoreFiles() {
        if (file_count < n_files) {
            return true;

        } else {
            return false;

        }
    }

    public File getNextFile() {
        if (hasMoreFiles()) {
            return files[file_count++];

        } else {
            return null;

        }

    }

    public boolean hasMoreFolders() {
        if (folder_count < n_folders) {
            return true;

        } else {
            return false;

        }
    }

    public Folder getNextFolder() {
        if (hasMoreFolders()) {
            return new Folder(folders[folder_count++],this);

        } else {
            return null;

        }
    }

    public void printFiles() {
        for (int i=0;i<files.length;i++) {
            System.out.println("    Files: "+files[i].getName());
        }
    }
}
