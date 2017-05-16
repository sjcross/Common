package wbif.sjx.common.HighContent.Module;

import fiji.plugin.trackmate.*;
import fiji.plugin.trackmate.detection.LogDetectorFactory;
import fiji.plugin.trackmate.tracking.LAPUtils;
import fiji.plugin.trackmate.tracking.sparselap.SparseLAPTrackerFactory;
import ij.IJ;
import ij.ImagePlus;
import ij.measure.Calibration;
import wbif.sjx.common.HighContent.Object.*;

import java.util.Set;

/**
 * Created by sc13967 on 15/05/2017.
 */
public class RunTrackMate extends HCModule {
    public static final String INPUT_IMAGE = "Input image";
    public static final String OUTPUT_OBJECTS = "Output objects";
    public static final String DO_SUBPIXEL_LOCALIZATION = "Do sub-pixel localisation";
    public static final String RADIUS = "Radius";
    public static final String THRESHOLD = "Threshold";
    public static final String DO_MEDIAN_FILTERING = "Median filtering";
    public static final String LINKING_MAX_DISTANCE = "Max linking distance";
    public static final String GAP_CLOSING_MAX_DISTANCE = "Gap closing max distance";
    public static final String MAX_FRAME_GAP = "Max frame gap";
    public static final String DO_TRACKING = "Run tracking";

    @Override
    public void execute(HCWorkspace workspace, boolean verbose) {
        if (verbose) System.out.println("   Running TrackMate detection");

        // Loading input image
        HCImageName targetImageName = parameters.getValue(INPUT_IMAGE);
        if (verbose) System.out.println("       Loading image ("+targetImageName.getName()+") into workspace");
        ImagePlus ipl = workspace.getImages().get(targetImageName).getImagePlus();

        // Getting image calibration
        Calibration calibration = ipl.getCalibration();

        // Getting parameters
        boolean subpixelLocalisation = parameters.getValue(DO_SUBPIXEL_LOCALIZATION);
        double radius = parameters.getValue(RADIUS);
        double threshold = parameters.getValue(THRESHOLD);
        boolean medianFiltering = parameters.getValue(DO_MEDIAN_FILTERING);
        double maxLinkDist = parameters.getValue(LINKING_MAX_DISTANCE);
        double maxGapDist = parameters.getValue(GAP_CLOSING_MAX_DISTANCE);
        int maxFrameGap = parameters.getValue(MAX_FRAME_GAP);

        // Getting name of output objects
        HCObjectName outputObjectsName = parameters.getValue(OUTPUT_OBJECTS);
        HCObjectSet objects = new HCObjectSet();

        // Initialising settings for TrackMate
        Settings settings = new Settings();

        settings.setFrom(ipl);
        settings.detectorFactory = new LogDetectorFactory();
        settings.detectorSettings.put("DO_SUBPIXEL_LOCALIZATION", subpixelLocalisation);
        settings.detectorSettings.put("RADIUS", radius);
        settings.detectorSettings.put("THRESHOLD", threshold);
        settings.detectorSettings.put("TARGET_CHANNEL", 1);
        settings.detectorSettings.put("DO_MEDIAN_FILTERING", medianFiltering);

        settings.trackerFactory  = new SparseLAPTrackerFactory();
        settings.trackerSettings = LAPUtils.getDefaultLAPSettingsMap();
        settings.trackerSettings.put("ALLOW_TRACK_SPLITTING", false);
        settings.trackerSettings.put("ALLOW_TRACK_MERGING", false);
        settings.trackerSettings.put("LINKING_MAX_DISTANCE", maxLinkDist);
        settings.trackerSettings.put("GAP_CLOSING_MAX_DISTANCE", maxGapDist);
        settings.trackerSettings.put("MAX_FRAME_GAP",maxFrameGap);

        // Initialising TrackMate
        Model model = new Model();
        model.setLogger(Logger.VOID_LOGGER);
        TrackMate trackmate = new TrackMate(model, settings);

        // Running TrackMate
        if (!trackmate.checkInput()) {
            IJ.log(trackmate.getErrorMessage());
        }

        if (!trackmate.process()) {
            IJ.log(trackmate.getErrorMessage());
        }

        // Converting tracks to local track model
        int ID = 1;

        // Initialising measurement store for radius
        HCMultiMeasurement radiusMeasure = new HCMultiMeasurement(HCMultiMeasurement.RADIUS);
        radiusMeasure.setSource(this);

        TrackModel trackModel = model.getTrackModel();
        Set<Integer> trackIDs = trackModel.trackIDs(false);
        for (Integer trackID:trackIDs) {
            Set<Spot> spots = trackModel.trackSpots(trackID);

            // Initialising a new HCObject to store this track
            HCObject object = new HCObject(ID++);

            // Getting x,y,f and 2-channel spot intensities from TrackMate results
            for (Spot spot:spots) {
                System.out.println(trackID+"_"+spot.ID());
                int x = (int) calibration.getRawX(spot.getDoublePosition(0));
                int y = (int) calibration.getRawY(spot.getDoublePosition(1));
                int z = (int) (spot.getDoublePosition(2)/calibration.getZ(1));
                int t = (int) Math.round(spot.getFeature(Spot.FRAME));
                object.addCoordinate(HCObject.X,x);
                object.addCoordinate(HCObject.Y,y);
                object.addCoordinate(HCObject.Z,z);
                object.addCoordinate(HCObject.T,t);

                // Adding radius measurement
                radiusMeasure.addValue(new int[]{x,y,z,t},calibration.getRawX(spot.getFeature(Spot.RADIUS)));

                // Adding calibration values to the HCObject (physical distance per pixel)
                object.addCalibration(HCObject.X,calibration.getX(1));
                object.addCalibration(HCObject.Y,calibration.getY(1));
                object.addCalibration(HCObject.Z,calibration.getZ(1));
                object.setCalibratedUnits(calibration.getUnits());

            }

            object.addMultiMeasurement(radiusMeasure.getName(),radiusMeasure);

            objects.put(object.getID(),object);

        }

        // Adding objects to the workspace
        if (verbose) System.out.println("       Adding objects ("+outputObjectsName.getName()+") to workspace");
        workspace.addObjects(outputObjectsName,objects);

    }

    @Override
    public HCParameterCollection initialiseParameters() {
        HCParameterCollection parameters = new HCParameterCollection();

        parameters.addParameter(new HCParameter(this,MODULE_TITLE,HCParameter.MODULE_TITLE,"TrackMate tracking",true));

        parameters.addParameter(new HCParameter(this,INPUT_IMAGE,HCParameter.INPUT_IMAGE,null,false));
        parameters.addParameter(new HCParameter(this,OUTPUT_OBJECTS,HCParameter.OUTPUT_OBJECTS,null,false));

        parameters.addParameter(new HCParameter(this,DO_SUBPIXEL_LOCALIZATION,HCParameter.BOOLEAN,true,true));
        parameters.addParameter(new HCParameter(this,RADIUS,HCParameter.DOUBLE,2.0,true));
        parameters.addParameter(new HCParameter(this,THRESHOLD,HCParameter.DOUBLE,5000.0,true));
        parameters.addParameter(new HCParameter(this,DO_MEDIAN_FILTERING,HCParameter.BOOLEAN,false,true));

        parameters.addParameter(new HCParameter(this,DO_TRACKING,HCParameter.BOOLEAN,true,true));
        parameters.addParameter(new HCParameter(this,LINKING_MAX_DISTANCE,HCParameter.DOUBLE,2.0,true));
        parameters.addParameter(new HCParameter(this,GAP_CLOSING_MAX_DISTANCE,HCParameter.DOUBLE,2.0,true));
        parameters.addParameter(new HCParameter(this,MAX_FRAME_GAP,HCParameter.INTEGER,3,true));

        return parameters;
    }

    @Override
    public HCParameterCollection getActiveParameters() {
        return parameters;

    }

}