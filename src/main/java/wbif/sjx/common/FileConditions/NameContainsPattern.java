package wbif.sjx.common.FileConditions;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sc13967 on 24/10/2016.
 */
public class NameContainsPattern implements FileCondition {
    private Pattern[] pattern;
    private int mode;

    public NameContainsPattern(Pattern pattern) {
        this.pattern = new Pattern[]{pattern};
        this.mode = INC_PARTIAL;

    }

    public NameContainsPattern(Pattern pattern, int mode) {
        this.pattern = new Pattern[]{pattern};
        this.mode = mode;

    }

    public NameContainsPattern(Pattern[] pattern) {
        this.pattern = pattern;
        this.mode = INC_PARTIAL;

    }

    public NameContainsPattern(Pattern[] pattern, int mode) {
        this.pattern = pattern;
        this.mode = mode;

    }

    public boolean test(File file) {
        boolean cnd = false;

        if (file != null) {
            String name = FilenameUtils.removeExtension(file.getName());

            for (int i = 0; i < pattern.length; i++) {
                Matcher matcher = pattern[i].matcher(name);

                switch (mode) {
                    case INC_COMPLETE:
                        if (matcher.matches()) cnd = true;
                        break;
                    case INC_PARTIAL:
                        if (matcher.find()) cnd = true;
                        break;
                    case EXC_COMPLETE:
                        if (!matcher.matches()) cnd = true;
                        break;
                    case EXC_PARTIAL:
                        if (!matcher.find()) cnd = true;
                        break;
                }
            }
        }

        return cnd;

    }
}
