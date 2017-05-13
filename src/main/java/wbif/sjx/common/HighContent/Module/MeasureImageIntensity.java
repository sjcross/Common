package wbif.sjx.common.HighContent.Module;

import ij.ImagePlus;
import wbif.sjx.common.Analysis.IntensityCalculator;
import wbif.sjx.common.HighContent.Object.*;
import wbif.sjx.common.MathFunc.CumStat;

/**
 * Created by sc13967 on 12/05/2017.
 */
public class MeasureImageIntensity implements Module {
    public static final String INPUT_IMAGE = "Input image";

    @Override
    public void execute(Workspace workspace, ParameterCollection parameters, boolean verbose) {
        if (verbose) System.out.println("   Measuring image intensity");

        // Getting input image
        ImageName inputImageName = parameters.getValue(this,INPUT_IMAGE);
        if (verbose) System.out.println("       Loading image ("+inputImageName+")");
        Image inputImage = workspace.getImages().get(inputImageName);
        ImagePlus inputImagePlus = inputImage.getImagePlus();

        // Running measurement
        CumStat cs = IntensityCalculator.calculate(inputImagePlus);

        // Adding measurements to image
        Measurement meanIntensity = new Measurement(Measurement.MEAN_INTENSITY,cs.getMean()[0]);
        meanIntensity.setSource(this);
        inputImage.addMeasurement(meanIntensity.getName(),meanIntensity);
        if (verbose) System.out.println("       Mean intensity = "+meanIntensity.getValue());

        Measurement stdIntensity = new Measurement(Measurement.STD_INTENSITY,cs.getStd(CumStat.SAMPLE)[0]);
        stdIntensity.setSource(this);
        inputImage.addMeasurement(stdIntensity.getName(),stdIntensity);
        if (verbose) System.out.println("       Std intensity (sample) = "+stdIntensity.getValue());

        Measurement minIntensity = new Measurement(Measurement.MIN_INTENSITY,cs.getMin()[0]);
        minIntensity.setSource(this);
        inputImage.addMeasurement(minIntensity.getName(),minIntensity);
        if (verbose) System.out.println("       Min intensity = "+minIntensity.getValue());

        Measurement maxIntensity = new Measurement(Measurement.MAX_INTENSITY,cs.getMax()[0]);
        maxIntensity.setSource(this);
        inputImage.addMeasurement(maxIntensity.getName(),maxIntensity);
        if (verbose) System.out.println("       Max intensity = "+maxIntensity.getValue());

    }

    @Override
    public ParameterCollection initialiseParameters() {
        ParameterCollection parameters = new ParameterCollection();

        parameters.addParameter(new Parameter(this,MODULE_TITLE,Parameter.MODULE_TITLE,"Measure image intensity",false));
        parameters.addParameter(new Parameter(this,INPUT_IMAGE,Parameter.IMAGE_NAME,"Im1",false));

        return parameters;

    }

}
