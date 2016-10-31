package wolfson.common.HighContent;

import java.io.File;

/**
 * Created by sc13967 on 21/10/2016.
 *
 * Interface Analysis-type class, which will be extended by particular analyses
 *
 */
public interface HCAnalysis {
    HCResultCollection execute(File file);
    HCExporter getExporter();

}
