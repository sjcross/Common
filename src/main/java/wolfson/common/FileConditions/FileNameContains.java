//TODO: Add "AND", "OR" or "NOR" condition to constructor

package wolfson.common.FileConditions;

import java.io.File;

/**
 * Checks the input file against a list of acceptable extensions
 * Created by sc13967 on 24/10/2016.
 */
public class FileNameContains implements FileCondition {
    private String test_str;

    public FileNameContains(String test_str) {
        this.test_str = test_str;

    }

    public boolean test(File file) {
        boolean cnd = false;

        if (file != null) {
            String name = file.getName();

            if (name.contains(test_str)) cnd = true;

        }

        return cnd;

    }
}