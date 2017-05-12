package wbif.sjx.common.HighContent.Object;

/**
 * Names for common measurements.  This isn't an exhaustive list.  Modules can create their own measurements.
 */
public enum MeasurementNames {
    // Spatial measures
    RADIUS("Radius"),
    X_CENTROID_MEAN("X-Centroid Mean"),
    Y_CENTROID_MEAN("Y-Centroid Mean"),
    Z_CENTROID_MEAN("Z-Centroid Mean"),
    X_CENTROID_MEDIAN("X-Centroid Median"),
    Y_CENTROID_MEDIAN("Y-Centroid Median"),
    Z_CENTROID_MEDIAN("Z-Centroid Median"),

    // Intensity measures
    MEAN_INTENSITY("Mean intensity"),
    MIN_INTENSITY("Minimum intensity"),
    MAX_INTENSITY("Maximum intensity"),
    STD_INTENSITY("Stdev intensity");

    private final String text;

    MeasurementNames(final String text) {
        this.text = text;

    }
}
