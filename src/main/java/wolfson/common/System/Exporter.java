package wolfson.common.System;

import wolfson.common.Object.HCResultCollection;

import java.io.File;

/**
 * Created by sc13967 on 27/10/2016.
 */
public interface Exporter<HCResult> {
    void export(HCResultCollection HC_result_collection);
    void export(HCResultCollection HC_result_collection, File file);

}