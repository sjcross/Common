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
    private Mode mode;

    public NameContainsPattern(Pattern pattern) {
        this.pattern = new Pattern[]{pattern};
        this.mode = Mode.INC_PARTIAL;

    }

    public NameContainsPattern(Pattern pattern, Mode mode) {
        this.pattern = new Pattern[]{pattern};
        this.mode = mode;

    }

    public NameContainsPattern(Pattern[] pattern) {
        this.pattern = pattern;
        this.mode = Mode.INC_PARTIAL;

    }

    public NameContainsPattern(Pattern[] pattern, Mode mode) {
        this.pattern = pattern;
        this.mode = mode;

    }

    public boolean test(File file) {
        if (file != null) {
            String name = FilenameUtils.removeExtension(file.getName());

            for (Pattern value : pattern) {
                Matcher matcher = value.matcher(name);

                switch (mode) {
                    case INC_COMPLETE:
                        if (matcher.matches()) return true;
                        break;
                    case INC_PARTIAL:
                        if (matcher.find()) return true;
                        break;
                    case EXC_COMPLETE:
                        if (matcher.matches()) return false;
                        break;
                    case EXC_PARTIAL:
                        if (matcher.find()) return false;
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

    public Pattern[] getPattern() {
        return pattern;
    }

    public Mode getMode() {
        return mode;
    }
}
