package wbif.sjx.common.HighContent.Object;

import wbif.sjx.common.HighContent.Module.HCModule;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by sc13967 on 03/05/2017.
 */
public class HCModuleCollection extends ArrayList<HCModule> {
    public HCMeasurementCollection getMeasurements() {
        HCMeasurementCollection measurements = new HCMeasurementCollection();

        for (HCModule module:this) {
            HCMeasurementCollection currentMeasurements = module.addActiveMeasurements();

            if (currentMeasurements != null) {
                // Adding all the current values to the HCMeasurementCollection
                for (HCName objectName : currentMeasurements.keySet()) {
                    measurements.computeIfAbsent(objectName, k -> new HashSet<>());
                    measurements.get(objectName).addAll(currentMeasurements.get(objectName));

                }
            }
        }

        return measurements;

    }
}
