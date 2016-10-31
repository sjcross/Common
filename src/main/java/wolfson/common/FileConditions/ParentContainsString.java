package wolfson.common.FileConditions;

import java.io.File;

/**
 * Checks the input file against a list of acceptable extensions
 * Created by sc13967 on 24/10/2016.
 */
public class ParentContainsString implements FileCondition {
    private String test_str;
    private int mode;

    public ParentContainsString(String test_str) {
        this.test_str = test_str;
        this.mode = FileCondition.INC_PARTIAL;

    }

    public ParentContainsString(String test_str, int mode) {
        this.test_str = test_str;
        this.mode = mode;

    }

    public boolean test(File file) {
        boolean cnd = false;

        if (file != null) {
            String name = file.getParent();

            if (mode == FileCondition.INC_COMPLETE) {
                if (name.equals(test_str)) cnd = true;

            } else if (mode == FileCondition.INC_PARTIAL) {
                if (name.contains(test_str)) cnd = true;

            } else if (mode == FileCondition.EXC_COMPLETE) {
                if (!name.equals(test_str)) cnd = true;

            } else if (mode == FileCondition.EXC_PARTIAL) {
                if (!name.contains(test_str)) cnd = true;

            }

        }

        return cnd;

    }
}