package io.github.sjcross.common.fileconditions;

import java.io.File;

/**
 * Checks the input file against a list of acceptable extensions
 * Created by sc13967 on 24/10/2016.
 */
public class ParentContainsString implements FileCondition {
    private String[] testStr;
    private Mode mode;

    public ParentContainsString(String testStr) {
        this.testStr = new String[]{testStr};
        this.mode = Mode.INC_PARTIAL;

    }

    public ParentContainsString(String testStr, Mode mode) {
        this.testStr = new String[]{testStr};
        this.mode = mode;

    }

    public ParentContainsString(String[] testStr) {
        this.testStr = testStr;
        this.mode = Mode.INC_PARTIAL;

    }

    public ParentContainsString(String[] testStr, Mode mode) {
        this.testStr = testStr;
        this.mode = mode;

    }

    public boolean test(File file) {
        if (file != null) {
            String name = file.getParent();

            for (int i = 0; i < testStr.length; i++) {
                switch (mode) {
                    case INC_COMPLETE:
                        if (name.equals(testStr[i])) return true;
                        break;
                    case INC_PARTIAL:
                        if (name.contains(testStr[i])) return true;
                        break;
                    case EXC_COMPLETE:
                        if (name.equals(testStr[i])) return false;
                        break;
                    case EXC_PARTIAL:
                        if (name.contains(testStr[i])) return false;
                        break;
                }
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
}