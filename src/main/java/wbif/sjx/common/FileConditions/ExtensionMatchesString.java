package wbif.sjx.common.FileConditions;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.regex.Matcher;

/**
 * Checks the input file against a list of acceptable extensions
 * Created by sc13967 on 24/10/2016.
 */
public class ExtensionMatchesString implements FileCondition {
    private String[] exts;
    private int mode;

    public ExtensionMatchesString(String ext) {
        this.exts = new String[]{ext};
    }

    public ExtensionMatchesString(String ext, int mode) {
        this.exts = new String[]{ext};
        this.mode = mode;
    }

    public ExtensionMatchesString(String[] exts) {
        this.exts = exts;
    }

    public ExtensionMatchesString(String[] exts, int mode) {
        this.exts = exts;
        this.mode = mode;
    }

    public boolean test(File file) {
        boolean cnd = false;

        if (file != null) {
            String extension = FilenameUtils.getExtension(file.getName());

            for (int i = 0; i < exts.length; i++) {
                if (extension.matches(exts[i])) cnd = true;

                switch (mode) {
                    case INC_COMPLETE:
                        if (extension.matches(exts[i])) cnd = true;
                        break;
                    case INC_PARTIAL:
                        if (extension.contains(exts[i])) cnd = true;
                        break;
                    case EXC_COMPLETE:
                        if (!extension.matches(exts[i])) cnd = true;
                        break;
                    case EXC_PARTIAL:
                        if (!extension.contains(exts[i])) cnd = true;
                        break;
                }

            }
        }

        return cnd;

    }
}