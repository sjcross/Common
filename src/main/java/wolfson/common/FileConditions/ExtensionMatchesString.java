package wolfson.common.FileConditions;

import java.io.File;

/**
 * Checks the input file against a list of acceptable extensions
 * Created by sc13967 on 24/10/2016.
 */
public class ExtensionMatchesString implements FileCondition {
    private String[] exts;

    public ExtensionMatchesString(String[] exts) {
        this.exts = exts;

    }

    public boolean test(File file) {
        boolean cnd = false;

        if (file != null) {
            String name = file.getName();
            int loc = name.lastIndexOf(".");

            for (int i = 0; i < exts.length; i++) {
                if (loc >= 0 & name.substring(loc + 1).contains(exts[i])) cnd = true;

            }
        }

        return cnd;

    }
}