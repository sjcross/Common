package wbif.sjx.common.HighContent.Module;

import fiji.plugin.trackmate.*;
import fiji.plugin.trackmate.detection.DetectorKeys;
import fiji.plugin.trackmate.detection.LogDetectorFactory;
import ij.ImagePlus;
import ij.measure.Calibration;
import wbif.sjx.common.HighContent.Object.*;


/**
 * Created by sc13967 on 08/05/2017.
 */
@Deprecated
public class TrackMateSpotDetection extends HCModule {
    public static final String INPUT_IMAGE = "Input image";
    public static final String OUTPUT_OBJECTS = "Output objects";
    public static final String BLOB_RADIUS = "Blob radius";
    public static final String THRESHOLD = "Threshold";

    @Override
    public void execute(HCWorkspace workspace, boolean verbose) {
        if (verbose) System.out.println("   Running TrackMate spot detection");

        // Loading input image
        HCImageName targetImageName = parameters.getValue(INPUT_IMAGE);
        if (verbose) System.out.println("       Loading image ("+targetImageName.getName()+") into workspace");
        ImagePlus ipl = workspace.getImages().get(targetImageName).getImagePlus();

        // Getting image calibration
        Calibration calibration = ipl.getCalibration();

        // Getting parameters
        double blobRadius = parameters.getValue(BLOB_RADIUS);
        double threshold = parameters.getValue(THRESHOLD);
        HCObjectName outputObjectsName = parameters.getValue(OUTPUT_OBJECTS);

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

        // Running TrackMate spot detection
        if (verbose) System.out.println("       Executing TrackMate spot detection");
        trackmate.execDetection();

        // Getting objects and adding them to the output objects
        if (verbose) System.out.println("       Processing detected objects");
        HCObjectSet objects = new HCObjectSet();

        SpotCollection spots = model.getSpots();
        for (Spot spot:spots.iterable(false)) {
            HCObject object = new HCObject(spot.ID());

            object.addCoordinate(HCObject.X,(int) calibration.getRawX(spot.getDoublePosition(0)));
            object.addCoordinate(HCObject.Y,(int) calibration.getRawY(spot.getDoublePosition(1)));
            object.addCoordinate(HCObject.Z,(int) (spot.getDoublePosition(2)/calibration.getZ(1)));

            HCSingleMeasurement radiusMeasurement = new HCSingleMeasurement(HCSingleMeasurement.RADIUS,calibration.getRawX(spot.getFeature(Spot.RADIUS)));
            radiusMeasurement.setSource(this);
            object.addSingleMeasurement(radiusMeasurement.getName(),radiusMeasurement);

            // Adding calibration values to the HCObject (physical distance per pixel)
            object.addCalibration(HCObject.X,calibration.getX(1));
            object.addCalibration(HCObject.Y,calibration.getY(1));
            object.addCalibration(HCObject.Z,calibration.getZ(1));
            object.setCalibratedUnits(calibration.getUnits());

            objects.put(object.getID(),object);

        }

        if (verbose) System.out.println("           "+spots.getNSpots(false)+" objects detected");

        // Adding objects to the workspace
        if (verbose) System.out.println("       Adding objects ("+outputObjectsName.getName()+") to workspace");
        workspace.addObjects(outputObjectsName,objects);

    }

    @Override
    public HCParameterCollection initialiseParameters() {
        HCParameterCollection parameters = new HCParameterCollection();

        parameters.addParameter(new HCParameter(this,MODULE_TITLE, HCParameter.MODULE_TITLE,"TrackMate spot detection",true));
        parameters.addParameter(new HCParameter(this,INPUT_IMAGE, HCParameter.INPUT_IMAGE,"Im1",false));
        parameters.addParameter(new HCParameter(this,OUTPUT_OBJECTS, HCParameter.OUTPUT_OBJECTS,"Obj1",false));
        parameters.addParameter(new HCParameter(this,BLOB_RADIUS, HCParameter.DOUBLE,0.1,true));
        parameters.addParameter(new HCParameter(this,THRESHOLD, HCParameter.DOUBLE,10000.0,true));

        return parameters;

    }

    @Override
    public HCParameterCollection getActiveParameters() {
        return parameters;

    }
}
