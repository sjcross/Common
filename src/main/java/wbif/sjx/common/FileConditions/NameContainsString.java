package wbif.sjx.common.FileConditions;

import java.io.File;

/**
 * Checks the input file against a list of acceptable extensions
 * Created by sc13967 on 24/10/2016.
 */
public class NameContainsString implements FileCondition {
    private String[] testStr;
    private Mode mode;

    public NameContainsString(String testStr) {
        this.testStr = new String[]{testStr};
        this.mode = Mode.INC_PARTIAL;

    }

    public NameContainsString(String testStr, Mode mode) {
        this.testStr = new String[]{testStr};
        this.mode = mode;

    }

    public NameContainsString(String[] testStr) {
        this.testStr = testStr;
        this.mode = Mode.INC_PARTIAL;

    }

    public NameContainsString(String[] testStr, Mode mode) {
        this.testStr = testStr;
        this.mode = mode;

    }

    public boolean test(String string) {
        for (String s : testStr) {
            switch (mode) {
                case INC_COMPLETE:
                    if (string.equals(s)) return true;
                    break;
                case INC_PARTIAL:
                    if (string.contains(s)) return true;
                    break;
                case EXC_COMPLETE:
                    if (string.equals(s)) return false;
                    break;
                case EXC_PARTIAL:
                    if (string.contains(s)) return false;
                    break;
            }
        }

        switch (mode) {
            case INC_COMPLETE:
            case INC_PARTIAL:
            default:
                return false;
            case EXC_COMPLETE:
            case EXC_PARTIAL:
                return true;
        }

    }

    public boolean test(File file) {
        if (file == null) return false;

        String name = file.getName();
        return test(name);

    }
}