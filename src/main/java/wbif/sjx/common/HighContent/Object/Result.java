package wbif.sjx.common.HighContent.Object;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * High-content result structure as an abstract class, which is extended on an experiment-by experiment basis
 * Created by sc13967 on 25/10/2016.
 */
public class Result extends HashMap<String,Object> {
    public static final String WELL = "Well";
    public static final String FIELD = "Field";
    public static final String TIMEPOINT = "Timepoint";
    public static final String ZPOSITION = "Z-position";
    public static final String CHANNEL = "Channel";
    public static final String YEAR = "Year";
    public static final String MONTH = "Month";
    public static final String DAY = "Day";
    public static final String HOUR = "Hour";
    public static final String MINUTE = "Minute";
    public static final String SECOND = "Second";
    public static final String CELLTYPE = "Cell type";
    public static final String MAGNIFICATION = "Magnification";
    public static final String COMMENT = "Comment";
    public static final String FILE = "File";
    public static final String EXTENSION = "Extension";


    public Result() {

    }

    public String getExt() {
        return (String) get(EXTENSION);
    }

    public void setExt(String ext) {
        put(EXTENSION,ext);
    }

    public File getFile() {
        return (File) get(FILE);
    }

    public void setFile(File file) {
        put(FILE,file);
    }

    public int getHour() {
        return (Integer) get(HOUR);
    }

    public void setHour(int hour) {
        put(HOUR,hour);
    }

    public int getMin() {
        return (Integer) get(MINUTE);
    }

    public void setMin(int min) {
        put(MINUTE,min);
    }

    public int getSec() {
        return (Integer) get(SECOND);
    }

    public void setSec(int sec) {
        put(SECOND,sec);
    }

    public String getWell() {
        return (String) get(WELL);
    }

    public void setWell(String well) {
        put(WELL,well);
    }

    public int getField() {
        return (Integer) get(FIELD);
    }

    public void setField(int field) {
        put(FIELD,field);
    }

    public int getTimepoint() {
        return (Integer) get(TIMEPOINT);
    }

    public void setTimepoint(int timepoint) {
        put(TIMEPOINT,timepoint);
    }

    public int getZ() {
        return (Integer) get(ZPOSITION);
    }

    public void setZ(int z) {
        put(ZPOSITION,z);
    }

    public int getChannel() {
        return (Integer) get(CHANNEL);
    }

    public void setChannel(int channel) {
        put(CHANNEL,channel);
    }

    public String getCelltype() {
        return (String) get(CELLTYPE);
    }

    public void setCelltype(String celltype) {
        put(CELLTYPE,celltype);
    }

    public String getMag() {
        return (String) get(MAGNIFICATION);
    }

    public void setMag(String mag) {
        put(MAGNIFICATION,mag);
    }

    public int getYear() {
        return (Integer) get(YEAR);
    }

    public void setYear(int year) {
        put(YEAR,year);
    }

    public int getMonth() {
        return (Integer) get(MONTH);
    }

    public void setMonth(int month) {
        put(MONTH,month);
    }

    public int getDay() {
        return (Integer) get(DAY);
    }

    public void setDay(int day) {
        put(DAY,day);
    }

    public String getComment() {
        return (String) get(COMMENT);
    }

    public void setComment(String comment) {
        put(COMMENT,comment);
    }

    public String getAsString(String property) {
        return String.valueOf(get(property));
    }

    public void printParameters() {
        DecimalFormat time_df = new DecimalFormat("00");

        System.out.println("    Primary channel: "+ getFile().getName());

        System.out.println("        Date: "+getDay()+"/"+getMonth()+"/"+getYear());
        System.out.println("        Time: "+time_df.format(getHour())+":"+time_df.format(getMin())+":"+time_df.format(getSec()));
        System.out.println("        Well: "+getWell());
        System.out.println("        Field: "+getField());
        System.out.println("        Timepoint: "+getTimepoint());
        System.out.println("        Z-position: "+getZ());
        System.out.println("        Channel: "+getChannel());
        System.out.println("        Magnification: "+getMag());
        System.out.println("        Cell type: "+getCelltype());
        System.out.println("        Comment: "+getComment());
    }

}
