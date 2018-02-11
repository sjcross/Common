package wbif.sjx.common.MetadataExtractors;

import wbif.sjx.common.Object.HCMetadata;

import java.util.StringTokenizer;
import java.util.regex.Matcher;

/**
 * Created by sc13967 on 07/02/2018.
 */
public class KeywordExtractor {
    private static final String name = "Keyword";
    private static String[] keywordArray;

    public static void main(String[] args) {
        new KeywordExtractor("MRSF,CORS, FG, ddf_2DD");
    }

    public KeywordExtractor(String keywords) {
        StringTokenizer tokenizer = new StringTokenizer(keywords,",");
        keywordArray = new String[tokenizer.countTokens()];

        int i = 0;
        while (tokenizer.hasMoreTokens()) {
            keywordArray[i++] = tokenizer.nextToken().trim();
        }
    }

    public String getName() {
        return name;
    }

    public boolean extract(HCMetadata result, String str) {
        for (String keyword:keywordArray) {
            if (str.contains(keyword)) {
                result.put(HCMetadata.KEYWORD,keyword);
                return true;
            }
        }

        return false;
    }
}
