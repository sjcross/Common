package wbif.sjx.common.Object;

import org.w3c.dom.Element;

public class Spot extends Timepoint<Double> {
    public static final String MAXI = "Maximum intensity";
    public static final String MINI = "Minimum intensity";
    public static final String MEANI = "Mean intensity";
    public static final String MEDI = "Median intensity";
    public static final String TOTALI = "Total intensity";
    public static final String STDI = "Intensity std. dev.";
    public static final String ESTDIA = "Estimated diameter";
    public static final int X = 0;
    public static final int Y = 1;
    public static final int Z = 2;
    public static final int ID_NUM = 3;
    public static final int T = 4;
    public static final int FRAME = 5;
    public static final int SLICE = 6;

    public String name;
    int ID;
    double t;
    int slice;
    public double[] min_I;
    public double[] max_I;
    public double[] mean_I;
    public double[] med_I;
    public double[] total_I;
    public double[] std_I;
    double radius;
    public double est_dia;
    public double vis;
    public double quality;
    public double manual_colour;
    public double contrast;
    public double snr;
    public int n_ch = 1;

    boolean hasint = false; //True when intensity has been measured

    public Spot(double x, double y, double z, int f, double radius) {
        super(x,y,z,f);
        this.radius = radius;

    }

    public int getSlice() {
        return slice;

    }

    public void setSlice(int slice) {
        this.slice = slice;

    }

    public int getID() {
        return ID;

    }

    public double getT() {
        return t;

    }

    public double getRadius() {
        return radius;

    }

    public void setID(int ID) {
        this.ID = ID;

    }

    public void setT(double t) {
        this.t = t;

    }

    public void setF(int f) {
        this.f = f;

    }

    public void setRadius(double radius) {
        this.radius = radius;

    }

    public void addTrackMateXML(Element e) {
        name = e.getAttribute("name");
        ID = Integer.valueOf(e.getAttribute("ID"));
        x = Double.valueOf(e.getAttribute("POSITION_X"));
        y = Double.valueOf(e.getAttribute("POSITION_Y"));
        z = Double.valueOf(e.getAttribute("POSITION_Z"));
        t = Double.valueOf(e.getAttribute("POSITION_T"));
        f = Integer.valueOf(e.getAttribute("FRAME"));
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

    public Object getVal(int type) {
        if (type == X)              return x;
        else if (type == Y)         return y;
        else if (type == Z)         return z;
        else if (type == ID_NUM)    return ID;
        else if (type == SLICE)     return slice;
        else if (type == FRAME)     return f;
        else if (type == T)         return t;

        return null;

    }
}