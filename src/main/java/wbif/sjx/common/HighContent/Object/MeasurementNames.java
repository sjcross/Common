package wbif.sjx.common.HighContent.Object;

/**
 * Names for common measurements.  This isn't an exhaustive list.  Modules can create their own measurements.
 */
public enum MeasurementNames {
    RADIUS("Radius"),

    X_CENTROID_MEAN("X-Centroid Mean"),
    Y_CENTROID_MEAN("Y-Centroid Mean"),
    Z_CENTROID_MEAN("Z-Centroid Mean"),

    X_CENTROID_MEDIAN("X-Centroid Median"),
    Y_CENTROID_MEDIAN("Y-Centroid Median"),
    Z_CENTROID_MEDIAN("Z-Centroid Median");

    private final String text;

    MeasurementNames(final String text) {
        this.text = text;

    }
}
