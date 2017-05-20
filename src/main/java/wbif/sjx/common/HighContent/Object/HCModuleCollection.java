package wbif.sjx.common.HighContent.Object;

import wbif.sjx.common.HighContent.Module.HCModule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by sc13967 on 03/05/2017.
 */
public class HCModuleCollection extends ArrayList<HCModule> implements Serializable {
    public HCMeasurementCollection getMeasurements(HCModule cutoffModule) {
        HCMeasurementCollection measurements = new HCMeasurementCollection();

        for (HCModule module:this) {
            if (module == cutoffModule) {
                break;
            }

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

    public HCMeasurementCollection getMeasurements() {
        return getMeasurements(null);

    }

    /**
     * Returns an ArrayList of all parameters of a specific type
     * @param type
     * @param cutoffModule
     * @return
     */
    public ArrayList<HCParameter> getParametersMatchingType(int type, HCModule cutoffModule) {
        ArrayList<HCParameter> parameters = new ArrayList<>();

        for (HCModule module:this) {
            // If the current module is the cutoff the loop terminates.  This prevents the system offering measurements
            // that are created after this module
            if (module == cutoffModule) {
                break;
            }

            // Running through all parameters, adding all images to the list
            HCParameterCollection currParameters = module.getActiveParameters();
            for (HCParameter currParameter:currParameters.getParameters().values()) {
                if (currParameter.getType() == type) {
                    parameters.add(currParameter);
                }
            }
        }

        return parameters;

    }

    public ArrayList<HCParameter> getParametersMatchingType(int type) {
        return getParametersMatchingType(type,null);

    }
}
