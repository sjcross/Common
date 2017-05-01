package wbif.sjx.common.HighContent.Process;

import wbif.sjx.common.HighContent.Object.ResultCollection;

import java.io.File;

/**
 * Created by sc13967 on 21/10/2016.
 *
 * Interface Analysis-type class, which will be extended by particular analyses
 *
 */
public interface Analysis {
    ResultCollection execute(File file);
    Exporter getExporter();

}
