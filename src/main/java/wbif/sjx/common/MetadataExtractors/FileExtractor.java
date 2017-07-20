package wbif.sjx.common.MetadataExtractors;

import wbif.sjx.common.Object.HCMetadata;

import java.io.File;

/**
 * Created by sc13967 on 20/07/2017.
 */
public interface FileExtractor {
    String getName();
    boolean extract(HCMetadata result, File file);

}
