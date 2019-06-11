package wbif.sjx.common.FileConditions;

import java.io.File;

/**
 * Checks the input file against a list of acceptable extensions
 * Created by sc13967 on 24/10/2016.
 */
public class NameContainsString implements FileCondition {
    private String[] testStr;
    private int mode;

    public NameContainsString(String testStr) {
        this.testStr = new String[]{testStr};
        this.mode = INC_PARTIAL;

    }

    public NameContainsString(String testStr, int mode) {
        this.testStr = new String[]{testStr};
        this.mode = mode;

    }

    public NameContainsString(String[] testStr) {
        this.testStr = testStr;
        this.mode = INC_PARTIAL;

    }

    public NameContainsString(String[] testStr, int mode) {
        this.testStr = testStr;
        this.mode = mode;

    }

    public boolean test(String string) {
        boolean cnd = false;

        for (int i = 0; i < testStr.length; i++) {
            switch (mode) {
                case INC_COMPLETE:
                    if (string.equals(testStr[i])) cnd = true;
                    break;
                case INC_PARTIAL:
                    if (string.contains(testStr[i])) cnd = true;
                    break;
                case EXC_COMPLETE:
                    if (!string.equals(testStr[i])) cnd = true;
                    break;
                case EXC_PARTIAL:
                    if (!string.contains(testStr[i])) cnd = true;
                    break;
            }
        }

        return cnd;

    }

    public boolean test(File file) {
        if (file == null) return false;

        String name = file.getName();
        return test(name);

    }
}