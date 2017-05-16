package wbif.sjx.common.HighContent.Object;

import java.util.HashMap;

/**
 * Multidimensional measurement.  Each measurement value is stored along with its coordinate
 */
public class HCMultiMeasurement {
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
    private HashMap<int[],Double> values;
    private Object source;


    // CONSTRUCTOR

    public HCMultiMeasurement(String name) {
        this.name = name;

    }


    // PUBLIC METHODS

    public void addValue(int[] coordinate, double value) {
        values.put(coordinate,value);

    }

    public double getValue(int[] coordinate) {
        return values.get(coordinate);

    }


    // GETTERS AND SETTERS

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<int[],Double> getValues() {
        return values;
    }

    public void setValues(HashMap<int[],Double> value) {
        this.values = value;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
}
