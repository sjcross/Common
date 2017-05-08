// TODO: Complete adding verbose output to modules
// TODO: Module to show object outlines on an image (returns the image itself)
// TODO: Module to save images and to save objects (could roll this into ShowImage and ShowObjects)
// TODO: Module to plot histograms of measurements (e.g. mean intensity for objects)
// TODO: Module to measure intensity texture (can call other Common library class, which does the calculation)

package wbif.sjx.common.HighContent.Module;

import wbif.sjx.common.HighContent.Object.ParameterCollection;
import wbif.sjx.common.HighContent.Object.Workspace;

/**
 * Created by sc13967 on 02/05/2017.
 */
public interface Module {


    // PUBLIC METHODS

    void execute(Workspace workspace, ParameterCollection parameters, boolean verbose);

    /**
     * Get a HashMap of the parameters this class requires
     * @return
     */
    void initialiseParameters(ParameterCollection parameters);


    // DEFAULT METHODS

    default void execute(Workspace workspace, ParameterCollection parameters) {
        execute(workspace,parameters,false);

    }

}
