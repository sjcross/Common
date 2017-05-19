package wbif.sjx.common.HighContent.Module;

import ij.ImagePlus;
import ij.gui.OvalRoi;
import ij.gui.Overlay;
import ij.gui.TextRoi;
import ij.plugin.HyperStackConverter;
import ij.process.StackConverter;
import wbif.sjx.common.HighContent.Object.*;

import java.awt.*;
import java.util.Random;

/**
 * Created by sc13967 on 17/05/2017.
 */
public class ShowObjectsOverlay extends HCModule {
    public static final String INPUT_IMAGE = "Input image";
    public static final String INPUT_OBJECTS = "Input objects";
    public static final String USE_GROUP_ID = "Use group ID";
    public static final String RANDOM_COLOURS = "Random colours";

    @Override
    public void execute(HCWorkspace workspace, boolean verbose) {
        if (verbose) System.out.println("   Overlaying objects on image");

        // Getting parameters
        boolean useGroupID = parameters.getValue(USE_GROUP_ID);
        boolean randomColours = parameters.getValue(RANDOM_COLOURS);

        // Getting input objects
        HCName inputObjectsName = parameters.getValue(INPUT_OBJECTS);
        HCObjectSet inputObjects = workspace.getObjects().get(inputObjectsName);

        // Getting input image
        HCName inputImageName = parameters.getValue(INPUT_IMAGE);
        HCImage inputImage = workspace.getImages().get(inputImageName);
        ImagePlus ipl = inputImage.getImagePlus();

        // If necessary, turning the image into a HyperStack (if 2 dimensions=1 it will be a standard ImagePlus)
        ImagePlus hyperstack = HyperStackConverter.toHyperStack(ipl,ipl.getNChannels(),ipl.getNSlices(),ipl.getNFrames());
        if (!hyperstack.isHyperStack()) new StackConverter(hyperstack).convertToRGB();

        hyperstack.setOverlay(new Overlay());

        // Running through each object, adding it to the overlay along with an ID label
        for (HCObject object:inputObjects.values()) {
            float randomID = new Random(object.getGroupID()*object.getGroupID()*object.getGroupID()).nextFloat();
            Color colour = randomColours ? Color.getHSBColor(randomID,1,1) : Color.ORANGE;

            double xMean = MeasureObjectCentroid.calculateCentroid(object.getCoordinates(HCObject.X),MeasureObjectCentroid.MEAN);
            double yMean = MeasureObjectCentroid.calculateCentroid(object.getCoordinates(HCObject.Y),MeasureObjectCentroid.MEAN);
            double zMean = MeasureObjectCentroid.calculateCentroid(object.getCoordinates(HCObject.Z),MeasureObjectCentroid.MEAN);

            // Getting coordinates to plot
            int c = ((int) object.getCoordinates(HCObject.C)) + 1;
            int z = (int) Math.round(zMean+1);
            int t = ((int) object.getCoordinates(HCObject.T)) + 1;

            // Adding circles where the object centroids are
            OvalRoi roi = new OvalRoi(xMean-2,yMean-2,4,4);
            if (hyperstack.isHyperStack()) {
                roi.setPosition(c, z, t);
            } else {
                roi.setPosition(Math.max(Math.max(c,z),t));
            }
            roi.setStrokeColor(colour);
            hyperstack.getOverlay().add(roi);

            // Adding text label
            TextRoi text;
            if (useGroupID) {
                text = new TextRoi(xMean, yMean, String.valueOf(object.getGroupID()));
            } else {
                text = new TextRoi(xMean, yMean, String.valueOf(object.getID()));
            }
            if (hyperstack.isHyperStack()) {
                text.setPosition(c, z, t);
            } else {
                text.setPosition(Math.max(Math.max(c,z),t));
            }
            text.setStrokeColor(colour);
            hyperstack.getOverlay().add(text);

        }

        hyperstack.show();

    }

    @Override
    public HCParameterCollection initialiseParameters() {
        HCParameterCollection parameters = new HCParameterCollection();

        parameters.addParameter(new HCParameter(this,MODULE_TITLE,HCParameter.MODULE_TITLE,"Show objects overlay",null,false));
        parameters.addParameter(new HCParameter(this,INPUT_IMAGE,HCParameter.INPUT_IMAGE,null,false));
        parameters.addParameter(new HCParameter(this,INPUT_OBJECTS,HCParameter.INPUT_OBJECTS,null,false));
        parameters.addParameter(new HCParameter(this,USE_GROUP_ID,HCParameter.BOOLEAN,null,false));
        parameters.addParameter(new HCParameter(this,RANDOM_COLOURS,HCParameter.BOOLEAN,true,false));

        return parameters;

    }

    @Override
    public HCParameterCollection getActiveParameters() {
        return parameters;
    }

    @Override
    public HCMeasurementCollection addActiveMeasurements() {
        return null;
    }
}
