package com.wolfson.common.Object;

import org.w3c.dom.Element;

public class Spot {
    public static final String MAXI = "Maximum intensity";
    public static final String MINI = "Minimum intensity";
    public static final String MEANI = "Mean intensity";
    public static final String MEDI = "Median intensity";
    public static final String TOTALI = "Total intensity";
    public static final String STDI = "Intensity std. dev.";
    public static final String ESTDIA = "Estimated diameter";

    public String name;
    public double ID;
    public double x;
    public double y;
    public double z;
    public double t;
    public double frame;
    public double[] min_I;
    public double[] max_I;
    public double[] mean_I;
    public double[] med_I;
    public double[] total_I;
    public double[] std_I;
    public double radius;
    public double est_dia;
    public double vis;
    public double quality;
    public double manual_colour;
    public double contrast;
    public double snr;
    public int n_ch = 1;

    boolean hasint = false; //True when intensity has been measured

    public Spot() {
        min_I = new double[n_ch];
        max_I = new double[n_ch];
        mean_I = new double[n_ch];
        med_I = new double[n_ch];
        total_I = new double[n_ch];
        std_I = new double[n_ch];
    }

    public void addTrackMateXML(Element e) {
        name = e.getAttribute("name");
        ID = Double.valueOf(e.getAttribute("ID"));
        x = Double.valueOf(e.getAttribute("POSITION_X"));
        y = Double.valueOf(e.getAttribute("POSITION_Y"));
        z = Double.valueOf(e.getAttribute("POSITION_Z"));
        t = Double.valueOf(e.getAttribute("POSITION_T"));
        frame = Double.valueOf(e.getAttribute("FRAME"));
        min_I[0] = Double.valueOf(e.getAttribute("MIN_INTENSITY"));
        max_I[0] = Double.valueOf(e.getAttribute("MAX_INTENSITY"));
        mean_I[0] = Double.valueOf(e.getAttribute("MEAN_INTENSITY"));
        med_I[0] = Double.valueOf(e.getAttribute("MEDIAN_INTENSITY"));
        total_I[0] = Double.valueOf(e.getAttribute("TOTAL_INTENSITY"));
        std_I[0] = Double.valueOf(e.getAttribute("STANDARD_DEVIATION"));
        radius = Double.valueOf(e.getAttribute("RADIUS"));
        est_dia = Double.valueOf(e.getAttribute("ESTIMATED_DIAMETER"));
        vis = Double.valueOf(e.getAttribute("VISIBILITY"));
        quality = Double.valueOf(e.getAttribute("QUALITY"));
        manual_colour = Double.valueOf(e.getAttribute("MANUAL_COLOR"));
        contrast = Double.valueOf(e.getAttribute("CONTRAST"));
        snr = Double.valueOf(e.getAttribute("SNR"));

        hasint = true;
    }

    public void setNChannels(int n_ch_in) {
        n_ch = n_ch_in;

        hasint = false;

        min_I = new double[n_ch];
        max_I = new double[n_ch];
        mean_I = new double[n_ch];
        med_I = new double[n_ch];
        total_I = new double[n_ch];
        std_I = new double[n_ch];

    }

}