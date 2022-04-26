package io.github.sjcross.common.metadataextractors;

import java.io.File;

/**
 * Created by sc13967 on 20/07/2017.
 */
public interface FileExtractor {
    String getName();
    boolean extract(Metadata result, File file);

}
