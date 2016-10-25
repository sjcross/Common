package wolfson.common.FileConditions;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sc13967 on 24/10/2016.
 */
public class NameContainsPattern implements FileCondition {
    private Pattern pattern;

    public NameContainsPattern(Pattern pattern) {
        this.pattern = pattern;

    }

    public boolean test(File file) {
        boolean cnd = false;

        if (file != null) {
            String name = file.getName();

            Matcher matcher = pattern.matcher(name);
            if (matcher.find()) cnd = true;

        }

        return cnd;

    }
}
