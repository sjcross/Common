package wbif.sjx.common.HighContent.Process;

import wbif.sjx.common.HighContent.Extractor.CellVoyagerFilenameExtractor;
import wbif.sjx.common.HighContent.Extractor.CellVoyagerFoldernameExtractor;
import wbif.sjx.common.HighContent.Extractor.IncuCyteLongFilenameExtractor;
import wbif.sjx.common.HighContent.Extractor.IncuCyteShortFilenameExtractor;
import wbif.sjx.common.HighContent.Object.Result;


/**
 * Created by sc13967 on 12/12/2016.
 */
public class ParameterExtractor {
    public static final int CELLVOYAGERFILE = 0;
    public static final int CELLVOYAGERFOLDER = 1;
    public static final int INCUCYTESHORT = 2;
    public static final int INCUCYTELONG = 3;

    private Result result;

    /**
     * Takes a supplied filename and uses regular expression interpretation to extract parameters.  This is basically a
     * shortcut class to all the available Extractors
     *
     * @param result Requires a pre-initialised Result object
     */
    public ParameterExtractor(Result result) {
        this.result = result;

    }

    public void extract(String str, int sourceType) {
        if (sourceType == CELLVOYAGERFILE) {
            new CellVoyagerFilenameExtractor().extract(result,str);

        } else if (sourceType == CELLVOYAGERFOLDER) {
            new CellVoyagerFoldernameExtractor().extract(result,str);

        } else if (sourceType == INCUCYTESHORT) {
            new IncuCyteShortFilenameExtractor().extract(result,str);

        } else if (sourceType == INCUCYTELONG) {
            new IncuCyteLongFilenameExtractor().extract(result,str);

        }
    }

    @Deprecated
    public void extractCVFile(String str) {
        new CellVoyagerFilenameExtractor().extract(result,str);

    }

    @Deprecated
    public void extractCVFolder(String str) {
        new CellVoyagerFoldernameExtractor().extract(result,str);

    }

    @Deprecated
    public void extractIncuCyteShortFile(String str) {
        new IncuCyteShortFilenameExtractor().extract(result,str);
    }

    @Deprecated
    public void extractIncuCyteLongFile(String str) {
        new IncuCyteLongFilenameExtractor().extract(result,str);

    }

    @Deprecated
    public void extractIncuCyteFile(String str) {
        new IncuCyteLongFilenameExtractor().extract(result,str);

    }
}
