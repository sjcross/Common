package wbif.sjx.common.HighContent.Module;

import ij.ImagePlus;
import wbif.sjx.common.HighContent.Object.*;
import wbif.sjx.common.MathFunc.CumStat;

import java.util.ArrayList;

/**
 * Created by sc13967 on 05/05/2017.
 */
public class MeasureObjectIntensity implements Module {
    public static final String INPUT_OBJECTS = "Input objects";
    public static final String INPUT_IMAGE = "Input image";


    @Override
    public void execute(Workspace workspace, ParameterCollection parameters, boolean verbose) {
        // Getting input objects
        HCObjectName objectName = (HCObjectName) parameters.getParameter(this,INPUT_OBJECTS).getValue();
        HCObjectSet objects = workspace.getObjects().get(objectName);

        // Getting input image
        ImageName imageName = (ImageName) parameters.getParameter(this,INPUT_IMAGE).getValue();
        Image image = workspace.getImages().get(imageName);
        ImagePlus ipl = image.getImagePlus();

        // Measuring intensity for each object and adding the measurement to that object
        for (HCObject object:objects.values()) {
            // Initialising the cumulative statistics object to store pixel intensities
            CumStat cs = new CumStat(1);

            // Getting pixel coordinates
            ArrayList<Integer> x = object.getCoordinates(HCObject.X);
            ArrayList<Integer> y = object.getCoordinates(HCObject.Y);
            ArrayList<Integer> c = object.getCoordinates(HCObject.C);
            ArrayList<Integer> z = object.getCoordinates(HCObject.Z);
            ArrayList<Integer> t = object.getCoordinates(HCObject.T);

            // Running through all pixels in this object and adding the intensity to the CumStat object
            for (int i=0;i<x.size();i++) {
                int cPos = c==null ? 0 : c.get(i);
                int zPos = z==null ? 0 : z.get(i);
                int tPos = t==null ? 0 : t.get(i);

                ipl.setPosition(cPos+1,zPos+1,tPos+1);
                cs.addMeasure(ipl.getProcessor().getPixelValue(x.get(i),y.get(i)));

            }

            // Calculating mean, std, min and max intensity
            Measurement meanIntensity = new Measurement(imageName.getName()+"_MEAN", cs.getMean()[0]);
            meanIntensity.setSource(this);
            object.addMeasurement(meanIntensity.getName(),meanIntensity);

            Measurement stdIntensity = new Measurement(imageName.getName()+"_STD", cs.getStd(CumStat.SAMPLE)[0]);
            stdIntensity.setSource(this);
            object.addMeasurement(stdIntensity.getName(),stdIntensity);

            Measurement minIntensity = new Measurement(imageName.getName()+"_MIN", cs.getMin()[0]);
            minIntensity.setSource(this);
            object.addMeasurement(minIntensity.getName(),minIntensity);

            Measurement maxIntensity = new Measurement(imageName.getName()+"_MAX", cs.getMax()[0]);
            maxIntensity.setSource(this);
            object.addMeasurement(maxIntensity.getName(),maxIntensity);

        }

    }

    @Override
    public void initialiseParameters(ParameterCollection parameters) {
        parameters.addParameter(new Parameter(this,MODULE_TITLE,Parameter.MODULE_TITLE,"Measure object intensity",true));
        parameters.addParameter(new Parameter(this,INPUT_OBJECTS,Parameter.OBJECT_NAME,null,false));
        parameters.addParameter(new Parameter(this,INPUT_IMAGE,Parameter.IMAGE_NAME,null,false));

    }
}
