package wbif.sjx.common.HighContent.Object;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by sc13967 on 27/10/2016.
 */
public class WorkspaceCollection extends ArrayList<Workspace> {


    // PUBLIC METHODS

    /**
     * Creates a new workspace and adds it to the collection
     * @param currentFile
     * @return
     */
    public Workspace getNewWorkspace(File currentFile) {
        Workspace workspace =  new Workspace(currentFile);
        add(workspace);

        return workspace;

    }
}
