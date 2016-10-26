package wolfson.common.FileConditions;

import java.io.File;

/**
 * Created by sc13967 on 24/10/2016.
 */
public interface FileCondition {
    int COMPLETE = 0; //Complete match for test string
    int PARTIAL = 1; //Partial match for test string

    boolean test(File file);

}
