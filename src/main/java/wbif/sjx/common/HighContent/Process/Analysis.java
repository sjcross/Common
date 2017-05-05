package wbif.sjx.common.HighContent.Process;

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

    /**
     * Initialisation method is where workspace is populated with modules and module-specific parameters.
     */
    void initialise();

    /**
     * The method that gets called by the BatchProcessor.  This shouldn't have any user interaction elements
     * @param workspace
     * @return
     */
    void execute(Workspace workspace);


    // GETTERS

    default ParameterCollection getParameters() {
        return parameters;

    }

    default ModuleCollection getModules() {
        return modules;

    }
}
