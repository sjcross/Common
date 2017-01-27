package wolfson.common.FileConditions;

import org.apache.commons.io.FilenameUtils;

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
            String extension = FilenameUtils.getExtension(file.getName());

            for (int i = 0; i < exts.length; i++) {
                if (extension.matches(exts[i])) cnd = true;

            }
        }

        return cnd;

    }
}