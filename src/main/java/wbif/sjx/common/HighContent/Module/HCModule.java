// TODO: Complete adding verbose output to modules
// TODO: Module to show object outlines on an image (returns the image itself)
// TODO: Module to save images and to save objects (could roll this into ShowImage and ShowObjects)
// TODO: Module to plot histograms of measurements (e.g. mean intensity for objects)
// TODO: Module to calculate size metrics of objects (can used Blob class)
// TODO: Module to calulate radial intensity distribution of objects

package wbif.sjx.common.HighContent.Module;

import wbif.sjx.common.HighContent.Object.HCParameterCollection;
import wbif.sjx.common.HighContent.Object.HCWorkspace;

/**
 * Created by sc13967 on 02/05/2017.
 */
public abstract class HCModule {
    public static final String MODULE_TITLE = "Module title";

    HCParameterCollection parameters = null;


    // CONSTRUCTOR

    public HCModule() {
        parameters = initialiseParameters();

    }


    // PUBLIC METHODS

    public abstract void execute(HCWorkspace workspace, boolean verbose);

    /**
     * Get a ParameterCollection of all the possible parameters this class requires (not all may be used).  This returns
     * the ParameterCollection, rather than just setting the local variable directly, which helps ensure the correct
     * operation is included in the method.
     * @return
     */
    public abstract HCParameterCollection initialiseParameters();

    /**
     * Return a ParameterCollection of the currently active parameters.  This is run each time a parameter is changed.
     * For example, if "Export XML" is set to "false" a sub-parameter specifying the measurements to export won't be
     * included in the ParameterCollection.  A separate rendering class will take this ParameterCollection and generate
     * an appropriate GUI panel.
     * @return
     */
    public abstract HCParameterCollection getActiveParameters();

    public void updateParameterValue(String name, Object value) {
        parameters.updateValue(name,value);

    }

    public <T> T getParameterValue(String name) {
        return parameters.getParameter(name).getValue();

    }


    // PRIVATE METHODS

    void execute(HCWorkspace workspace) {
        execute(workspace,false);

    }

}
