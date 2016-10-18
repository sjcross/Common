package wolfson.common.System;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Stephen on 16/10/2016.
 */
public class FileCrawler {
    private Folder folder; //Current folder
    private ArrayList<Folder> folders = new ArrayList<Folder>(); //List of current locations within folders

    public static void main(String[] args) {
        JFileChooser j = new JFileChooser();
        j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        j.showOpenDialog(null);
        FileCrawler fc = new FileCrawler(j.getSelectedFile());
    }

    public FileCrawler(File root_folder) {
        folder = new Folder(root_folder,null);

        folders.add(folder);

        scanFolders();

    }

    /**
     * Moves to the start of the deepest unexplored folder
     */
    public void scanFolders() {
        for (int i=0;i<100;i++) {
            Folder next_folder = folder.getNextFolder();

            if (next_folder != null) {
                folders.add(next_folder);
                folder = next_folder;

            } else { //Reached deepest point, so go to current folder's parent
                folder = folder.getParent();

            }
        }
    }
}