//TODO: Could generalise this to regular expressions, so the form of the expression is specified and this has to match a relevant part of it

package wolfson.common.FileConditions;

import java.io.File;

/**
 * Checks the input file against a list of acceptable extensions
 * Created by sc13967 on 24/10/2016.
 */
public class NameContains implements FileCondition {
    private String test_str;

    public NameContains(String test_str) {
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