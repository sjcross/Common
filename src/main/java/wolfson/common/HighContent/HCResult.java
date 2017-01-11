package wolfson.common.HighContent;

import java.io.File;
import java.text.DecimalFormat;

/**
 * High-content result structure as an abstract class, which is extended on an experiment-by experiment basis
 * Created by sc13967 on 25/10/2016.
 */
public abstract class HCResult {
    int well;
    int field;
    int timepoint;
    int z;
    int channel;
    int year;
    int month;
    int day;
    int hour;
    int min;
    int sec;
    String celltype;
    String mag;
    String comment;
    File file1;
    String ext;

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public File getFile1() {
        return file1;
    }

    public void setFile1(File file1) {
        this.file1 = file1;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getSec() {
        return sec;
    }

    public void setSec(int sec) {
        this.sec = sec;
    }

    public int getWell() {
        return well;
    }

    public void setWell(int well) {
        this.well = well;
    }

    public int getField() {
        return field;
    }

    public void setField(int field) {
        this.field = field;
    }

    public int getTimepoint() {
        return timepoint;
    }

    public void setTimepoint(int timepoint) {
        this.timepoint = timepoint;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getCelltype() {
        return celltype;
    }

    public void setCelltype(String celltype) {
        this.celltype = celltype;
    }

    public String getMag() {
        return mag;
    }

    public void setMag(String mag) {
        this.mag = mag;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void printParameters() {
        DecimalFormat time_df = new DecimalFormat("00");

        System.out.println("    Primary channel: "+file1.getName());

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
