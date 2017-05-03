package wbif.sjx.common.HighContent.Module;

import wbif.sjx.common.HighContent.Object.ParameterCollection;
import wbif.sjx.common.HighContent.Object.Workspace;

import java.util.LinkedHashMap;

/**
 * Created by sc13967 on 02/05/2017.
 */
public interface Module {
    void execute(Workspace workspace);

    /**
     * Get a HashMap of the parameters this class requires
     * @return
     */
    void initialiseParameters(ParameterCollection parameters);

}
