package wbif.sjx.common.HighContent.Module;

import wbif.sjx.common.HighContent.Object.*;
import wbif.sjx.common.MathFunc.CumStat;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by sc13967 on 11/05/2017.
 */
public class MeasureObjectCentroid implements Module {
    public static final String INPUT_OBJECTS = "Input objects";
    public static final String CENTROID_METHOD = "Centroid method";

    public static final String MEAN = "Mean";
    public static final String MEDIAN = "Median";
    public static final String ALL = "Both";

    private static String[] methodChoices = new String[]{MEAN,MEDIAN,ALL};


    @Override
    public void execute(Workspace workspace, ParameterCollection parameters, boolean verbose) {
        if (verbose) System.out.println("   Measuring object centroids");

        // Getting current objects
        HCObjectName inputObjectName = parameters.getValue(this,INPUT_OBJECTS);
        HCObjectSet inputObjects = workspace.getObjects().get(inputObjectName);

        // Getting which centroid measures to calculate
        String choice = parameters.getValue(this,CENTROID_METHOD);
        boolean useMean = choice.equals(MEAN) | choice.equals(ALL);
        boolean useMedian = choice.equals(MEDIAN) | choice.equals(ALL);
        if (verbose) System.out.println("       Calculating centroid as "+choice);

        // Getting the centroids of each and saving them to the objects
        for (HCObject object:inputObjects.values()) {
            ArrayList<Integer> x = object.getCoordinates(HCObject.X);
            ArrayList<Integer> y = object.getCoordinates(HCObject.Y);
            ArrayList<Integer> z = object.getCoordinates(HCObject.Z);

            if (useMean) {
                if (x != null) {
                    double xMean = calculateCentroid(x,MEAN);
                    Measurement measurement = new Measurement(MeasurementNames.X_CENTROID_MEAN.name(),xMean);
                    measurement.setSource(this);
                    object.addMeasurement(measurement.getName(),measurement);
                }
                if (y!= null) {
                    double yMean = calculateCentroid(y,MEAN);
                    Measurement measurement = new Measurement(MeasurementNames.Y_CENTROID_MEAN.name(),yMean);
                    measurement.setSource(this);
                    object.addMeasurement(measurement.getName(),measurement);
                }
                if (z!= null) {
                    double zMean = calculateCentroid(z,MEAN);
                    Measurement measurement = new Measurement(MeasurementNames.Z_CENTROID_MEAN.name(),zMean);
                    measurement.setSource(this);
                    object.addMeasurement(measurement.getName(),measurement);
                }
            }

            if (useMedian) {
                if (x != null) {
                    double xMedian = calculateCentroid(x,MEDIAN);
                    Measurement measurement = new Measurement(MeasurementNames.X_CENTROID_MEDIAN.name(),xMedian);
                    measurement.setSource(this);
                    object.addMeasurement(measurement.getName(),measurement);
                }
                if (y!= null) {
                    double yMedian = calculateCentroid(y,MEDIAN);
                    Measurement measurement = new Measurement(MeasurementNames.Y_CENTROID_MEDIAN.name(),yMedian);
                    measurement.setSource(this);
                    object.addMeasurement(measurement.getName(),measurement);
                }
                if (z!= null) {
                    double zMedian = calculateCentroid(z,MEDIAN);
                    Measurement measurement = new Measurement(MeasurementNames.Z_CENTROID_MEDIAN.name(),zMedian);
                    measurement.setSource(this);
                    object.addMeasurement(measurement.getName(),measurement);
                }
            }
        }
    }

    @Override
    public void initialiseParameters(ParameterCollection parameters) {
        parameters.addParameter(new Parameter(this,INPUT_OBJECTS,Parameter.IMAGE_NAME,"Im1",true));
        parameters.addParameter(new Parameter(this,CENTROID_METHOD,Parameter.CHOICE_ARRAY,methodChoices[0],true));

    }

    public static double calculateCentroid(ArrayList<Integer> values, String method) {
        if (method.equals(MEAN)) {
            CumStat cs = new CumStat(1);
            for (int value:values) {
                cs.addMeasure(value);
            }

            return cs.getMean()[0];
        }

        if (method.equals(MEDIAN)) {
            // Sorting values in ascending order
            Collections.sort(values);

            // Taking the central value
            double nValues = values.size();
            return values.get((int) Math.floor(nValues/2));

        }

        return 0;

    }
}
