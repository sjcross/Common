package wbif.sjx.common.HighContent.Process;

import wbif.sjx.common.HighContent.Object.Patterns;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sc13967 on 13/12/2016.
 */
@Deprecated
public class FilenameGenerator {
    public static final int CELLVOYAGER = 0;

    private int mode = CELLVOYAGER;
    private DecimalFormat[] df = new DecimalFormat[]{
            new DecimalFormat("0"),
            new DecimalFormat("00"),
            new DecimalFormat("000"),
            new DecimalFormat("0000"),
            new DecimalFormat("00000")};
    private int Wn = 1;
    private int Fn = 3;
    private int Tn = 4;
    private int Zn = 2;
    private int Cn = 1;

    public int getWn() {
        return Wn;
    }

    public void setWn(int wn) {
        Wn = wn;
    }

    public int getFn() {
        return Fn;
    }

    public void setFn(int fn) {
        Fn = fn;
    }

    public int getTn() {
        return Tn;
    }

    public void setTn(int tn) {
        Tn = tn;
    }

    public int getZn() {
        return Zn;
    }

    public void setZn(int zn) {
        Zn = zn;
    }

    public int getCn() {
        return Cn;
    }

    public void setCn(int cn) {
        Cn = cn;
    }

    public FilenameGenerator() {

    }

    public FilenameGenerator(String template, int mode) {
        Matcher matcher = null;

        if (mode == CELLVOYAGER) {
            Pattern pattern = Pattern.compile(Patterns.getCellVoyagerFilenamePattern());
            matcher = pattern.matcher(template);
            matcher.find();
        }

        Wn = matcher.end(1)-matcher.start(1);
        Fn = matcher.end(2)-matcher.start(2);
        Tn = matcher.end(3)-matcher.start(3);
        Zn = matcher.end(4)-matcher.start(4);
        Cn = matcher.end(5)-matcher.start(5);

    }

    public String generateCVFile(int W, int F, int T, int Z, int C, String ext) {
        String output = "W"+df[Wn-1].format(W)+
                "F"+df[Fn-1].format(F)+
                "T"+df[Tn-1].format(T)+
                "Z"+df[Zn-1].format(Z)+
                "C"+df[Cn-1].format(C)+
                "."+ext;

        return output;
    }

    static public String generateIncuCyteShortFile(String comment, String W, int F, String ext) {
        return comment+"_"+W+"_"+F+"."+ext;

    }
}
