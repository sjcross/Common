package wbif.sjx.common.HighContent.Process;

import wbif.sjx.common.HighContent.Module.Module;
import wbif.sjx.common.HighContent.Object.ModuleCollection;
import wbif.sjx.common.HighContent.Object.ParameterCollection;
import wbif.sjx.common.HighContent.Object.Workspace;

/**
 * Created by sc13967 on 21/10/2016.
 *
 * Interface Analysis-type class, which will be extended by particular analyses
 *
 */
public interface Analysis {
    ParameterCollection parameters = new ParameterCollection();
    ModuleCollection modules = new ModuleCollection();


    // PUBLIC METHODS

    /**
     * Initialisation method is where workspace is populated with modules and module-specific parameters.
     */
    void initialise();


    // DEFAULT METHODS

    /**
     * The method that gets called by the BatchProcessor.  This shouldn't have any user interaction elements
     * @param workspace Workspace containing stores for images and objects
     * @return
     */
    default void execute(Workspace workspace) {
        execute(workspace,false);

    }

    /**
     * The method that gets called by the BatchProcessor.  This shouldn't have any user interaction elements
     * @param workspace Workspace containing stores for images and objects
     * @param verbose Switch determining if modules should report progress to System.out
     * @return
     */
    default void execute(Workspace workspace, boolean verbose) {
        // Running through modules
        for (Module module:modules) {
            module.execute(workspace,parameters,verbose);
        }
    }

    default ParameterCollection getParameters() {
        return parameters;

    }

    default ModuleCollection getModules() {
        return modules;

    }

}
