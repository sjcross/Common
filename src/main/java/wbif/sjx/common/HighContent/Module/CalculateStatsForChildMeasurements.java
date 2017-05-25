package wbif.sjx.common.HighContent.Module;

import wbif.sjx.common.HighContent.Object.*;
import wbif.sjx.common.MathFunc.CumStat;

import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Runs through all the different children HCObjectSets assigned to an object and calculates statistics for all
 * measurements.  Eventually it would be good to offer the option which statistics will be processed.
 */
public class CalculateStatsForChildMeasurements extends HCModule {
    public static final String PARENT_OBJECTS = "Parent objects";
    public static final String CHILD_OBJECTS = "Child objects";


    @Override
    public String getTitle() {
        return "Calculate statistics for child measurements";
    }

    @Override
    public void execute(HCWorkspace workspace, boolean verbose) {
        String moduleName = this.getClass().getSimpleName();
        if (verbose) System.out.println("["+moduleName+"] Initialising");

        // Getting input objects
        HCName parentObjectsName = parameters.getValue(PARENT_OBJECTS);
        HCObjectSet parentObjects = workspace.getObjects().get(parentObjectsName);

        // Getting child objects to calculate statistics for
        HCName childObjectsName = parameters.getValue(CHILD_OBJECTS);

        // Getting a list of the measurement names from the first child object in the set
        Set<String> exampleMeasurements = parentObjects.values().iterator().next().getChildren(childObjectsName)
                .values().iterator().next().getMeasurements().keySet();

        // Running through objects, calculating statistics for selected children
        for (HCObject parentObject:parentObjects.values()) {
            HCObjectSet childObjects = parentObject.getChildren(childObjectsName);
            for (String measurement:exampleMeasurements) {
                // For each measurement type, calculating the mean, standard deviation, etc.
                CumStat cs = new CumStat(1);
                for (HCObject childObject : childObjects.values()) {
                    cs.addMeasure(childObject.getMeasurement(measurement).getValue());

                }

                // Checking at least one measurement was taken
                if (cs.getN()[0] == 0) {
                    // Adding measurements to parent object
                    HCMeasurement summaryMeasurement = new HCMeasurement(measurement+"_MEAN",Double.NaN);
                    summaryMeasurement.setSource(this);
                    parentObject.addMeasurement(summaryMeasurement);

                    summaryMeasurement = new HCMeasurement(measurement+"_STD",Double.NaN);
                    summaryMeasurement.setSource(this);
                    parentObject.addMeasurement(summaryMeasurement);

                    summaryMeasurement = new HCMeasurement(measurement+"_MIN",Double.NaN);
                    summaryMeasurement.setSource(this);
                    parentObject.addMeasurement(summaryMeasurement);

                    summaryMeasurement = new HCMeasurement(measurement+"_MAX",Double.NaN);
                    summaryMeasurement.setSource(this);
                    parentObject.addMeasurement(summaryMeasurement);

                    summaryMeasurement = new HCMeasurement(measurement+"_SUM",Double.NaN);
                    summaryMeasurement.setSource(this);
                    parentObject.addMeasurement(summaryMeasurement);

                } else {
                    // Adding measurements to parent object
                    HCMeasurement summaryMeasurement = new HCMeasurement(measurement + "_MEAN", cs.getMean()[0]);
                    summaryMeasurement.setSource(this);
                    parentObject.addMeasurement(summaryMeasurement);

                    summaryMeasurement = new HCMeasurement(measurement + "_STD", cs.getStd()[0]);
                    summaryMeasurement.setSource(this);
                    parentObject.addMeasurement(summaryMeasurement);

                    summaryMeasurement = new HCMeasurement(measurement + "_MIN", cs.getMin()[0]);
                    summaryMeasurement.setSource(this);
                    parentObject.addMeasurement(summaryMeasurement);

                    summaryMeasurement = new HCMeasurement(measurement + "_MAX", cs.getMax()[0]);
                    summaryMeasurement.setSource(this);
                    parentObject.addMeasurement(summaryMeasurement);

                    summaryMeasurement = new HCMeasurement(measurement + "_SUM", cs.getSum()[0]);
                    summaryMeasurement.setSource(this);
                    parentObject.addMeasurement(summaryMeasurement);

                }
            }
        }
    }

    @Override
    public void initialiseParameters() {
        parameters.addParameter(new HCParameter(this,PARENT_OBJECTS,HCParameter.INPUT_OBJECTS,null));
        parameters.addParameter(new HCParameter(this,CHILD_OBJECTS,HCParameter.CHILD_OBJECTS,null,null));

    }

    @Override
    public HCParameterCollection getActiveParameters() {
        HCParameterCollection returnedParameters = new HCParameterCollection();
        returnedParameters.addParameter(parameters.getParameter(PARENT_OBJECTS));
        returnedParameters.addParameter(parameters.getParameter(CHILD_OBJECTS));

        // Updating measurements with measurement choices from currently-selected object
        HCName objectName = parameters.getValue(PARENT_OBJECTS);
        if (objectName != null) {
            parameters.updateValueRange(CHILD_OBJECTS, objectName);

        } else {
            parameters.updateValueRange(CHILD_OBJECTS, null);

        }

        return returnedParameters;

    }

    @Override
    public void addMeasurements(HCMeasurementCollection measurements) {
        if (parameters.getValue(PARENT_OBJECTS) != null & parameters.getValue(CHILD_OBJECTS) != null) {
            String[] names = measurements.getMeasurementNames(parameters.getValue(CHILD_OBJECTS));

            for (String name:names) {
                measurements.addMeasurement(parameters.getValue(PARENT_OBJECTS),name+"_MEAN");
                measurements.addMeasurement(parameters.getValue(PARENT_OBJECTS),name+"_STD");
                measurements.addMeasurement(parameters.getValue(PARENT_OBJECTS),name+"_MIN");
                measurements.addMeasurement(parameters.getValue(PARENT_OBJECTS),name+"_MAX");
                measurements.addMeasurement(parameters.getValue(PARENT_OBJECTS),name+"_SUM");

            }
        }
    }

    @Override
    public void addRelationships(HCRelationshipCollection relationships) {

    }
}
