package wbif.sjx.common.HighContent.Object;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by sc13967 on 19/05/2017.
 */
public class HCMeasurementCollection extends HashMap<HCName,HashSet<String>> {
    // PUBLIC METHODS

    public void addMeasurement(HCName objectName, String measurementName) {
        computeIfAbsent(objectName,k -> new HashSet<>());
        get(objectName).add(measurementName);

    }

    public String[] getMeasurementNames(HCName measurementName) {
        return get(measurementName) == null ? new String[]{""} : get(measurementName).toArray(new String[get(measurementName).size()]);

    }
}
