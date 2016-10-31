package wolfson.common.FileConditions;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sc13967 on 24/10/2016.
 */
public class NameContainsPattern implements FileCondition {
    private Pattern pattern;
    private int mode;

    public NameContainsPattern(Pattern pattern) {
        this.pattern = pattern;
        this.mode = FileCondition.INC_PARTIAL;

    }

    public NameContainsPattern(Pattern pattern, int mode) {
        this.pattern = pattern;
        this.mode = mode;

    }

    public boolean test(File file) {
        boolean cnd = false;

        if (file != null) {
            String name = file.getName();

            Matcher matcher = pattern.matcher(name);
            if (mode == FileCondition.INC_COMPLETE) {
                if (matcher.matches()) cnd = true;

            } else if (mode == FileCondition.INC_PARTIAL) {
                if (matcher.find()) cnd = true;

            } else if (mode == FileCondition.EXC_COMPLETE) {
                if (!matcher.matches()) cnd = true;

            } else if (mode == FileCondition.EXC_PARTIAL) {
                if (!matcher.find()) cnd = true;

            }
        }

        return cnd;

    }
}
