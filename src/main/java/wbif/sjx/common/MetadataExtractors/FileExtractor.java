package wbif.sjx.common.MetadataExtractors;

import wbif.sjx.common.Object.Metadata;

import java.io.File;

/**
 * Created by sc13967 on 20/07/2017.
 */
public interface FileExtractor {
    String getName();
    boolean extract(Metadata result, File file);

}
