// TODO: Module to show object outlines on an image (returns the image itself)

package wbif.sjx.common.HighContent.Module;

import wbif.sjx.common.HighContent.Object.ParameterCollection;
import wbif.sjx.common.HighContent.Object.Workspace;

/**
 * Created by sc13967 on 02/05/2017.
 */
public interface Module {
    void execute(Workspace workspace, ParameterCollection parameters);

    /**
     * Get a HashMap of the parameters this class requires
     * @return
     */
    void initialiseParameters(ParameterCollection parameters);

}
