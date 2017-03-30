package wolfson.common.HighContent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sc13967 on 12/12/2016.
 */
public class HCParameterExtractor {
    private HCResult result;


    public HCParameterExtractor(HCResult result) {
        this.result = result;

    }

    public void extractCVFile(String str) {
        Pattern fi_pattern = Pattern.compile(HCPatterns.getCellVoyagerFilenamePattern());
        Matcher fi_matcher = fi_pattern.matcher(str);

        int loc = str.lastIndexOf(".");
        if (loc >= 0) {
            result.setExt(str.substring(loc + 1));
        }

        if (fi_matcher.find()) {
            result.setWell(fi_matcher.group(1));
            result.setField(Integer.parseInt(fi_matcher.group(2)));
            result.setTimepoint(Integer.parseInt(fi_matcher.group(3)));
            result.setZ(Integer.parseInt(fi_matcher.group(4)));
            result.setChannel(Integer.parseInt(fi_matcher.group(5)));

        }
    }

    public void extractCVFolder(String str) {
        Pattern fo_pattern = Pattern.compile(HCPatterns.getCellVoyagerFolderPattern());
        Matcher fo_matcher = fo_pattern.matcher(str);

        if (fo_matcher.find()) {
            result.setYear(Integer.parseInt(fo_matcher.group(1)));
            result.setMonth(Integer.parseInt(fo_matcher.group(2)));
            result.setDay(Integer.parseInt(fo_matcher.group(3)));
            result.setHour(Integer.parseInt(fo_matcher.group(4)));
            result.setMin(Integer.parseInt(fo_matcher.group(5)));
            result.setSec(Integer.parseInt(fo_matcher.group(6)));
            result.setMag(fo_matcher.group(7));
            result.setCelltype(fo_matcher.group(8));
            result.setComment(fo_matcher.group(9));

        }
    }

    public void extractIncuCyteShortFile(String str) {
        Pattern fi_pattern = Pattern.compile(HCPatterns.getIncuCyteShortFilenamePattern());
        Matcher fi_matcher = fi_pattern.matcher(str);

        int loc = str.lastIndexOf(".");
        if (loc >= 0) {
            result.setExt(str.substring(loc + 1));
        }

        if (fi_matcher.find()) {
            result.setComment(fi_matcher.group(1));
            result.setWell(fi_matcher.group(2));
            result.setField(Integer.parseInt(fi_matcher.group(3)));

        }
    }

    public void extractIncuCyteLongFile(String str) {
        Pattern fi_pattern = Pattern.compile(HCPatterns.getIncuCyteLongFilenamePattern());
        Matcher fi_matcher = fi_pattern.matcher(str);

        int loc = str.lastIndexOf(".");
        if (loc >= 0) {
            result.setExt(str.substring(loc + 1));
        }

        if (fi_matcher.find()) {
            result.setComment(fi_matcher.group(1));
            result.setWell(fi_matcher.group(2));
            result.setField(Integer.parseInt(fi_matcher.group(3)));
            result.setYear(Integer.parseInt(fi_matcher.group(4)));
            result.setMonth(Integer.parseInt(fi_matcher.group(5)));
            result.setDay(Integer.parseInt(fi_matcher.group(6)));
            result.setHour(Integer.parseInt(fi_matcher.group(7)));
            result.setMin(Integer.parseInt(fi_matcher.group(8)));

        }
    }

    /**
     * Compatibility method for old version of Common
     * @param str
     */
    public void extractIncuCyteFile(String str) {
        extractIncuCyteLongFile(str);

    }
}
