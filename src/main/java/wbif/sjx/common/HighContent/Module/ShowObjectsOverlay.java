package wbif.sjx.common.HighContent.Module;

import ij.ImagePlus;
import ij.gui.OvalRoi;
import ij.gui.Overlay;
import ij.gui.TextRoi;
import ij.plugin.Duplicator;
import wbif.sjx.common.HighContent.Object.*;

/**
 * Created by sc13967 on 17/05/2017.
 */
public class ShowObjectsOverlay extends HCModule {
    public static final String INPUT_IMAGE = "Input image";
    public static final String INPUT_OBJECTS = "Input objects";
    public static final String USE_GROUP_ID = "Use group ID";

    @Override
    public void execute(HCWorkspace workspace, boolean verbose) {
        if (verbose) System.out.println("   Overlaying objects on image");

        // Getting parameters
        boolean useGroupID = parameters.getValue(USE_GROUP_ID);

        // Getting input objects
        HCObjectName inputObjectsName = parameters.getValue(INPUT_OBJECTS);
        HCObjectSet inputObjects = workspace.getObjects().get(inputObjectsName);

        // Getting input image
        HCImageName inputImageName = parameters.getValue(INPUT_IMAGE);
        HCImage inputImage = workspace.getImages().get(inputImageName);
        ImagePlus ipl = inputImage.getImagePlus();

        // Duplicating the image
        ImagePlus ipl2 = new Duplicator().run(ipl);

        Overlay overlay = new Overlay();

        // Running through each object, adding it to the overlay along with an ID label
        for (HCObject object:inputObjects.values()) {
            double xMean = MeasureObjectCentroid.calculateCentroid(object.getCoordinates(HCObject.X),MeasureObjectCentroid.MEAN);
            double yMean = MeasureObjectCentroid.calculateCentroid(object.getCoordinates(HCObject.Y),MeasureObjectCentroid.MEAN);
            double zMean = MeasureObjectCentroid.calculateCentroid(object.getCoordinates(HCObject.Z),MeasureObjectCentroid.MEAN);

            OvalRoi roi = new OvalRoi(xMean-1,xMean-1,2,2);
            roi.setPosition(1,(int) Math.round(zMean+1),(int) object.getCoordinates(HCObject.T)+1);
            overlay.add(roi);

            TextRoi text;
            if (useGroupID) {
                text = new TextRoi(xMean, yMean, String.valueOf(object.getGroupID()));
            } else {
                text = new TextRoi(xMean, yMean, String.valueOf(object.getID()));
            }
            text.setPosition(1,(int) Math.round(zMean+1),(int) object.getCoordinates(HCObject.T)+1);
            overlay.add(text);

        }

        ipl2.setOverlay(overlay);
        ipl2.show();

    }

    @Override
    public HCParameterCollection initialiseParameters() {
        HCParameterCollection parameters = new HCParameterCollection();

        parameters.addParameter(new HCParameter(this,MODULE_TITLE,HCParameter.MODULE_TITLE,"Show objects overlay",null,false));
        parameters.addParameter(new HCParameter(this,INPUT_IMAGE,HCParameter.INPUT_IMAGE,null,false));
        parameters.addParameter(new HCParameter(this,INPUT_OBJECTS,HCParameter.INPUT_OBJECTS,null,false));
        parameters.addParameter(new HCParameter(this,USE_GROUP_ID,HCParameter.BOOLEAN,null,false));

        return parameters;

    }

    @Override
    public HCParameterCollection getActiveParameters() {
        return parameters;
    }
}
