package wbif.sjx.common.MetadataExtractors;


import wbif.sjx.common.Object.HCMetadata;

/**
 * Created by steph on 30/04/2017.
 */
public interface Extractor {
    String getName();
    String getPattern();
    boolean extract(HCMetadata result, String str);

}
