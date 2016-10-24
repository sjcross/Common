package wolfson.common.FileConditions;

import java.io.File;

/**
 * Created by sc13967 on 24/10/2016.
 */
public interface FileCondition {
    public boolean test(File file);

}
