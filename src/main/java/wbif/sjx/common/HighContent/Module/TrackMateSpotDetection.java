package wbif.sjx.common.HighContent.Module;

import fiji.plugin.trackmate.*;
import fiji.plugin.trackmate.detection.DetectorKeys;
import fiji.plugin.trackmate.detection.LogDetectorFactory;
import ij.IJ;
import ij.ImagePlus;
import wbif.sjx.common.HighContent.Object.*;

import java.util.HashMap;

/**
 * Created by sc13967 on 08/05/2017.
 */
public class TrackMateSpotDetection implements Module {
    public static final String INPUT_IMAGE = "Input image";
    public static final String OUTPUT_OBJECTS = "Output objects";
    public static final String BLOB_RADIUS = "Blob radius";
    public static final String THRESHOLD = "Threshold";

    @Override
    public void execute(Workspace workspace, ParameterCollection parameters, boolean verbose) {
        if (verbose) System.out.println("   Running TrackMate spot detection");

        // Loading input image
        ImageName targetImageName = (ImageName) parameters.getParameter(this,INPUT_IMAGE).getValue();
        if (verbose) System.out.println("       Loading image ("+targetImageName.getName()+") into workspace");
        ImagePlus ipl = workspace.getImages().get(targetImageName).getImagePlus();

        ipl.setCalibration(null);

        // Getting parameters
        double blobRadius = (double) parameters.getParameter(this,BLOB_RADIUS).getValue();
        double threshold = (double) parameters.getParameter(this,THRESHOLD).getValue();
        HCObjectName outputObjectsName = (HCObjectName) parameters.getParameter(this,OUTPUT_OBJECTS).getValue();

        // Initialising TrackMate spot detection
        if (verbose) System.out.println("       Initialising TrackMate");

        Model model = new Model();
        model.setLogger(Logger.VOID_LOGGER);

        Settings settings = new Settings();
        settings.setFrom(ipl);
        settings.detectorFactory = new LogDetectorFactory();
        settings.detectorSettings.put(DetectorKeys.KEY_DO_SUBPIXEL_LOCALIZATION,true);
        settings.detectorSettings.put(DetectorKeys.KEY_RADIUS,blobRadius);
        settings.detectorSettings.put(DetectorKeys.KEY_TARGET_CHANNEL,1);
        settings.detectorSettings.put(DetectorKeys.KEY_THRESHOLD,threshold);
        settings.detectorSettings.put(DetectorKeys.KEY_DO_MEDIAN_FILTERING,false);

        TrackMate trackmate = new TrackMate(model,settings);

        if (!trackmate.checkInput()) {
            IJ.log(trackmate.getErrorMessage());
        }

        // Running TrackMate spot detection
        if (verbose) System.out.println("       Executing TrackMate spot detection");
        trackmate.execDetection();

        // Getting objects and adding them to the output objects
        if (verbose) System.out.println("       Processing detected objects");
        HashMap<Integer,HCObject> objects = new HashMap<>();

        SpotCollection spots = model.getSpots();
        for (Spot spot:spots.iterable(false)) {
            HCObject object = new HCObject(spot.ID());

            object.addCoordinate(HCObject.X,(int) spot.getDoublePosition(0));
            object.addCoordinate(HCObject.Y,(int) spot.getDoublePosition(1));
            object.addCoordinate(HCObject.Z,(int) spot.getDoublePosition(2));

            Measurement radiusMeasurement = new Measurement("RADIUS", spot.getFeature(Spot.RADIUS));
            radiusMeasurement.setSource(this);
            object.addMeasurement(radiusMeasurement.getName(),radiusMeasurement);

            objects.put(object.getID(),object);

        }

        if (verbose) System.out.println("           "+spots.getNSpots(false)+" objects detected");

        // Adding objects to the workspace
        if (verbose) System.out.println("       Adding objects ("+outputObjectsName.getName()+") to workspace");
        workspace.addObjects(outputObjectsName,objects);

    }

    @Override
    public void initialiseParameters(ParameterCollection parameters) {
        parameters.addParameter(new Parameter(this,Parameter.IMAGE_NAME,INPUT_IMAGE,"Im1",false));
        parameters.addParameter(new Parameter(this,Parameter.OBJECT_NAME,OUTPUT_OBJECTS,"Obj1",false));
        parameters.addParameter(new Parameter(this,Parameter.NUMBER,BLOB_RADIUS,1d,true));
        parameters.addParameter(new Parameter(this,Parameter.NUMBER,THRESHOLD,20000d,true));

    }
}
