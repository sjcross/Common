package wbif.sjx.common.HighContent.Object;

/**
 * Measurement that holds a single value for an object
 */
public class HCSingleMeasurement {
    // Spatial measures
    public static final String RADIUS = "Radius";
    public static final String X_CENTROID_MEAN = "X-Centroid Mean";
    public static final String Y_CENTROID_MEAN = "Y-Centroid Mean";
    public static final String Z_CENTROID_MEAN = "Z-Centroid Mean";
    public static final String X_CENTROID_MEDIAN = "X-Centroid Median";
    public static final String Y_CENTROID_MEDIAN = "Y-Centroid Median";
    public static final String Z_CENTROID_MEDIAN = "Z-Centroid Median";

    // Intensity measures
    public static final String MEAN_INTENSITY = "Mean intensity";
    public static final String MIN_INTENSITY = "Minimum intensity";
    public static final String MAX_INTENSITY = "Maximum intensity";
    public static final String STD_INTENSITY = "Stdev intensity";

    private String name;
    private double value;
    private Object source;


    // CONSTRUCTOR

    public HCSingleMeasurement(String name, double value) {
        this.name = name;
        this.value = value;

    }


    // GETTERS AND SETTERS

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
}
