package wbif.sjx.common.HighContent.Module;

import ij.ImagePlus;
import wbif.sjx.common.HighContent.Object.*;
import wbif.sjx.common.MathFunc.CumStat;

import java.util.ArrayList;

/**
 * Similar to MeasureObjectIntensity, but performed on circular (or spherical) regions of interest around each point in
 * 3D.  Allows the user to specify the region around each point to be measured.  Intensity traces are stored as
 * HCMultiMeasurements
 */
public class MeasureTrackIntensity extends HCModule {
    public static final String INPUT_IMAGE = "Input image";
    public static final String INPUT_OBJECTS = "Input objects";
    public static final String MEASUREMENT_RADIUS = "Measurement radius";
    public static final String CALIBRATED_RADIUS = "Calibrated radius";

    
    @Override
    public void execute(HCWorkspace workspace, boolean verbose) {
        if (verbose) System.out.println("   Measuring track intensity");

        // Getting image to measure track intensity for
        HCImageName inputImageName = parameters.getValue(INPUT_IMAGE);
        HCImage inputImage = workspace.getImages().get(inputImageName);
        ImagePlus ipl = inputImage.getImagePlus();

        // Getting objects to measure
        HCObjectName inputObjectsName = parameters.getValue(INPUT_OBJECTS);
        HCObjectSet inputObjects = workspace.getObjects().get(inputObjectsName);

        // Getting parameters
        double radius = parameters.getValue(MEASUREMENT_RADIUS);
        boolean calibrated = parameters.getValue(CALIBRATED_RADIUS);

        // Getting local object region
        inputObjects = GetLocalObjectRegion.getLocalRegions(inputObjects, radius, calibrated);

        // Running through each object's timepoints, getting intensity measurements
        for (HCObject object:inputObjects.values()) {
            // Getting pixel coordinates
            ArrayList<Integer> x = object.getCoordinates(HCObject.X);
            ArrayList<Integer> y = object.getCoordinates(HCObject.Y);
            ArrayList<Integer> c = object.getCoordinates(HCObject.C);
            ArrayList<Integer> z = object.getCoordinates(HCObject.Z);
            ArrayList<Integer> t = object.getCoordinates(HCObject.T);

            System.out.println(x+"_"+y+"_"+z+"_"+c+"_"+t);

            // Initialising the cumulative statistics object to store pixel intensities.  Unlike MeasureObjectIntensity,
            // this uses a multi-element CumStat where each element corresponds to a different frame
            CumStat cs = new CumStat(t.size());

            // Running through all pixels in this object and adding the intensity to the CumStat object
            for (int i=0;i<x.size();i++) {
                int cPos = c==null ? 0 : c.get(i);
                int zPos = z==null ? 0 : z.get(i);
                int tPos = t.get(i);

                ipl.setPosition(cPos+1,zPos+1,tPos+1);
                cs.addSingleMeasure(tPos,ipl.getProcessor().getPixelValue(x.get(i),y.get(i)));

            }

            // Calculating mean, std, min and max intensity and adding to the relevant object parent (we don't want to
            // add the measurements to the expanded objects)
            HCMultiMeasurement meanIntensity = new HCMultiMeasurement(inputImageName.getName()+"_MEAN");
            meanIntensity.setSource(this);
            object.getParent().addMultiMeasurement(meanIntensity.getName(),meanIntensity);

            HCMultiMeasurement stdIntensity = new HCMultiMeasurement(inputImageName.getName()+"_STD");
            stdIntensity.setSource(this);
            object.getParent().addMultiMeasurement(stdIntensity.getName(),stdIntensity);

            HCMultiMeasurement minIntensity = new HCMultiMeasurement(inputImageName.getName()+"_MIN");
            minIntensity.setSource(this);
            object.getParent().addMultiMeasurement(minIntensity.getName(),minIntensity);

            HCMultiMeasurement maxIntensity = new HCMultiMeasurement(inputImageName.getName()+"_MAX");
            maxIntensity.setSource(this);
            object.getParent().addMultiMeasurement(maxIntensity.getName(),maxIntensity);

            double[] mean = cs.getMean();
            double[] std = cs.getStd(CumStat.SAMPLE);
            double[] min = cs.getMin();
            double[] max = cs.getMax();

            for (int i=0;i<mean.length;i++) {
                meanIntensity.addValue(t.get(i),mean[i]);
                stdIntensity.addValue(t.get(i),std[i]);
                minIntensity.addValue(t.get(i),min[i]);
                maxIntensity.addValue(t.get(i),max[i]);

            }
        }
    }

    @Override
    public HCParameterCollection initialiseParameters() {
        HCParameterCollection parameters = new HCParameterCollection();

        parameters.addParameter(new HCParameter(this,MODULE_TITLE,HCParameter.MODULE_TITLE,"Measure track intensity",true));
        parameters.addParameter(new HCParameter(this,INPUT_IMAGE,HCParameter.INPUT_IMAGE,null,false));
        parameters.addParameter(new HCParameter(this,INPUT_OBJECTS,HCParameter.INPUT_OBJECTS,null,false));
        parameters.addParameter(new HCParameter(this,CALIBRATED_RADIUS, HCParameter.BOOLEAN,false,true));
        parameters.addParameter(new HCParameter(this,MEASUREMENT_RADIUS, HCParameter.DOUBLE,10.0,true));
        
        return parameters;
        
    }

    @Override
    public HCParameterCollection getActiveParameters() {
        return parameters;
        
    }
}
