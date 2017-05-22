package wbif.sjx.common.HighContent.Module;

import ij.gui.Plot;
import wbif.sjx.common.HighContent.Object.*;
import wbif.sjx.common.MathFunc.CumStat;

import java.awt.*;

/**
 * Created by sc13967 on 19/05/2017.
 */
public class PlotMeasurementsScatter extends HCModule {
    public static final String INPUT_OBJECTS = "Input objects";
    public static final String MEASUREMENT1 = "First measurement (X)";
    public static final String MEASUREMENT2 = "Second measurement (Y)";
    public static final String INCLUDE_COLOUR = "Add third measurement as colour";
    public static final String MEASUREMENT3 = "Third measurement (Colour)";
    public static final String COLOURMAP = "Colourmap";

    private static final String[] COLOURMAPS = new String[]{"Red to blue","Red to green"};

    private Color[] createColourGradient(double startH, double endH, double[] values) {
        // Getting colour range
        CumStat cs = new CumStat(1);
        for (double value:values) cs.addMeasure(value);
        double min = cs.getMin()[0];
        double max = cs.getMax()[0];

        Color[] colours = new Color[values.length];
        for (int i=0;i<colours.length;i++) {
            double H = values[i]*(endH-startH)/(max-min) + startH;
            colours[i] = Color.getHSBColor((float) H,1,1);

        }

        return colours;

    }

    @Override
    public String getTitle() {
        return "Plot measurements as scatter";
    }

    @Override
    public void execute(HCWorkspace workspace, boolean verbose) {
        if (verbose) System.out.println("   Plotting measurements as scatter");

        // Getting input objects
        HCName inputObjectsName = parameters.getValue(INPUT_OBJECTS);
        HCObjectSet inputObjects = workspace.getObjects().get(inputObjectsName);

        // Getting parameters
        boolean useColour = parameters.getValue(INCLUDE_COLOUR);
        String colourmap = null;
        if (useColour) colourmap = parameters.getValue(COLOURMAP);

        // Getting measurement names
        String measurement1 = parameters.getValue(MEASUREMENT1);
        String measurement2 = parameters.getValue(MEASUREMENT2);
        String measurement3 = null;
        if (useColour) measurement3 = parameters.getValue(MEASUREMENT3);

        // Getting measurement values
        double[] measurementValues1 = new double[inputObjects.size()];
        double[] measurementValues2 = new double[inputObjects.size()];
        double[] measurementValues3 = null;
        if (useColour) measurementValues3 = new double[inputObjects.size()];

        int iter = 0;
        for (HCObject inputObject:inputObjects.values()) {
            measurementValues1[iter] = inputObject.getMeasurement(measurement1).getValue();
            measurementValues2[iter] = inputObject.getMeasurement(measurement2).getValue();
            if (useColour) measurementValues3[iter] = inputObject.getMeasurement(measurement3).getValue();

        }

        // Creating the scatter plot
        if (useColour) {
            String title = "Scatter plot of " + measurement1 + ", " + measurement2+" and "+measurement3;
            Plot plot = new Plot(title, measurement1, measurement2);

            Color[] colors = null;
            if (colourmap.equals(COLOURMAPS[0])) { // Red to blue
                colors = createColourGradient(0,240/255,measurementValues3);

            } else if (colourmap.equals(COLOURMAPS[1])) { // Red to green
                colors = createColourGradient(0,120/255,measurementValues3);

            }

            for (int i=0;i<measurementValues1.length;i++) {
                plot.setColor(colors[i]);
                plot.addPoints(new double[]{measurementValues1[i]},new double[]{measurementValues2[i]},Plot.DOT);

            }

            plot.show();

        } else {
            String title = "Scatter plot of " + measurement1 + " and " + measurement2;
            Plot plot = new Plot(title, measurement1, measurement2);
            plot.addPoints(measurementValues1, measurementValues2, Plot.DOT);
            plot.show();

        }

    }

    @Override
    public HCParameterCollection initialiseParameters() {
        HCParameterCollection parameters = new HCParameterCollection();

        parameters.addParameter(new HCParameter(this,INPUT_OBJECTS,HCParameter.INPUT_OBJECTS,null));
        parameters.addParameter(new HCParameter(this,MEASUREMENT1,HCParameter.MEASUREMENT,null,null));
        parameters.addParameter(new HCParameter(this,MEASUREMENT2,HCParameter.MEASUREMENT,null,null));
        parameters.addParameter(new HCParameter(this,INCLUDE_COLOUR,HCParameter.BOOLEAN,false,null));
        parameters.addParameter(new HCParameter(this,MEASUREMENT3,HCParameter.MEASUREMENT,null,null));
        parameters.addParameter(new HCParameter(this,COLOURMAP,HCParameter.CHOICE_ARRAY,COLOURMAPS[0],COLOURMAPS));

        return parameters;

    }

    @Override
    public HCParameterCollection getActiveParameters() {
        HCParameterCollection returnedParameters = new HCParameterCollection();
        returnedParameters.addParameter(parameters.getParameter(INPUT_OBJECTS));
        returnedParameters.addParameter(parameters.getParameter(MEASUREMENT1));
        returnedParameters.addParameter(parameters.getParameter(MEASUREMENT2));

        // Updating measurements with measurement choices from currently-selected object
        HCName objectName = parameters.getValue(INPUT_OBJECTS);
        if (objectName != null) {
            parameters.updateValueRange(MEASUREMENT1, objectName);
            parameters.updateValueRange(MEASUREMENT2, objectName);

        } else {
            parameters.updateValueRange(MEASUREMENT1, null);
            parameters.updateValueRange(MEASUREMENT2, null);

        }

        returnedParameters.addParameter(parameters.getParameter(INCLUDE_COLOUR));
        if (parameters.getValue(INCLUDE_COLOUR)) {
            returnedParameters.addParameter(parameters.getParameter(MEASUREMENT3));
            returnedParameters.addParameter(parameters.getParameter(COLOURMAP));
//
//            if (objectName != null) {
//                parameters.updateValueRange(MEASUREMENT3, objectName);
//
//            } else {
//                parameters.updateValueRange(MEASUREMENT3, null);
//
//            }
//
        }

        return returnedParameters;

    }

    @Override
    public HCMeasurementCollection addActiveMeasurements() {
        return null;

    }
}
