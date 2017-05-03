package wbif.sjx.common.HighContent.Process;

import wbif.sjx.common.HighContent.Object.ResultCollection;
import wbif.sjx.common.HighContent.Object.Workspace;

import java.io.File;

/**
 * Created by sc13967 on 21/10/2016.
 *
 * Interface Analysis-type class, which will be extended by particular analyses
 *
 */
public interface Analysis {
    /**
     * Initialisation method is where workspace is populated with modules and module-specific parameters.
     * @param workspace
     */
    void initialise(Workspace workspace);

    /**
     * The method that gets called by the BatchProcessor.  This shouldn't have any user interaction elements
     * @param workspace
     * @return
     */
    ResultCollection execute(Workspace workspace);

    /**
     * Provides the relevant exporter function.
     * (This may get deprecated in the future if exporting is standardised)
     * @return
     */
    Exporter getExporter();

}
