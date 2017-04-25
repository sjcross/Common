package wbif.sjx.common.FileConditions;

import java.io.File;

/**
 * Created by sc13967 on 24/10/2016.
 */
public interface FileCondition {
    int INC_COMPLETE = 0; //Including complete match for test string
    int INC_PARTIAL = 1; //Including partial match for test string
    int EXC_COMPLETE = 2; //Excluding complete match for test string
    int EXC_PARTIAL = 3; //Excluding partial match for test string

    boolean test(File file);

}
