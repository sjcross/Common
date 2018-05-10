package wbif.sjx.common.MetadataExtractors;

import wbif.sjx.common.Object.HCMetadata;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CV7000FilenameExtractor implements NameExtractor {
    private static final String name = "CV7000 filename";
    private static final String pattern = "(\\w+?)_(\\w+?)_#(\\w+?)_([A-Z a-z]+?\\d+?)_T(\\d+?)F(\\d+?)L(\\d+?)A(\\d+?)Z(\\d+?)C(\\d+)";
//    (.+)_([A-Z]\d+?)_(\d++)
//    AssayPlate_Greiner_#655090_C02_T0001F001L01A01Z01C01

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPattern() {
        return pattern;
    }

    @Override
    public boolean extract(HCMetadata result, String str) {
        Pattern fi_pattern = Pattern.compile(pattern);
        Matcher fi_matcher = fi_pattern.matcher(str);

        int loc = str.lastIndexOf(".");
        if (loc >= 0) {
            result.setExt(str.substring(loc + 1));
        }

        if (fi_matcher.find()) {
            result.setPlateName(fi_matcher.group(1));
            result.setPlateManufacturer(fi_matcher.group(2));
            result.setPlateModel(fi_matcher.group(3));
            result.setWell(fi_matcher.group(4));
            result.setTimepoint(Integer.parseInt(fi_matcher.group(5)));
            result.setField(Integer.parseInt(fi_matcher.group(6)));
            result.setTimelineNumber(Integer.parseInt(fi_matcher.group(7)));
            result.setActionNumber(Integer.parseInt(fi_matcher.group(8)));
            result.setZ(Integer.parseInt(fi_matcher.group(9)));
            result.setChannel(Integer.parseInt(fi_matcher.group(10)));

            return true;

        } else {
            return false;

        }
    }
}
