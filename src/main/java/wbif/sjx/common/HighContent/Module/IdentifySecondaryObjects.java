package wbif.sjx.common.HighContent.Module;

import fiji.threshold.Auto_Threshold;
import ij.ImagePlus;
import ij.plugin.Filters3D;
import inra.ijpb.morphology.Morphology;
import inra.ijpb.morphology.Strel3D;
import inra.ijpb.plugins.MorphologicalFilter3DPlugin;
import inra.ijpb.segment.Threshold;
import inra.ijpb.watershed.Watershed;
import wbif.sjx.common.HighContent.Object.*;


/**
 * Created by sc13967 on 03/05/2017.
 */
public class IdentifySecondaryObjects extends HCModule {
    public static final String INPUT_IMAGE = "Input image";
    public static final String INPUT_OBJECTS = "Input objects";
    public static final String OUTPUT_OBJECTS = "Output objects";
    public static final String MEDIAN_FILTER_RADIUS = "Median filter radius";
    public static final String THRESHOLD_METHOD = "Threshold method";

    private static String[] thresholdMethods = new String[]{"Otsu","Huang","Li"};

    @Override
    public void execute(HCWorkspace workspace, boolean verbose) {
        if (verbose) System.out.println("    Running secondary object identification");

        // Getting relevant parameters
        double medFiltR = parameters.getValue(MEDIAN_FILTER_RADIUS);
        String thrMeth = parameters.getValue(THRESHOLD_METHOD);

        // Loading images and objects into workspace
        HCName inputImageName = parameters.getValue(INPUT_IMAGE);
        HCImage inputImage2 = workspace.getImages().get(inputImageName);
        ImagePlus image2 = inputImage2.getImagePlus();

        HCName inputObjectsName = parameters.getValue(INPUT_OBJECTS);
        HCObjectSet objects1 = workspace.getObjects().get(inputObjectsName);

        // Initialising the output objects ArrayList
        HCName outputObjectsName = parameters.getValue(OUTPUT_OBJECTS);

        // Getting nuclei objects as image
        if (verbose) System.out.println("       Converting objects to image");
        ImagePlus image1 = new ObjectImageConverter().convertObjectsToImage(objects1,inputImage2,true).getImagePlus();

        // Segmenting cell image
        // Filtering cell image
        if (verbose) System.out.println("       Applying filter (radius = "+medFiltR+" px)");
        image2.setStack(Filters3D.filter(image2.getImageStack(), Filters3D.MEDIAN, (float) medFiltR, (float) medFiltR,1));

        // Thresholded cell image
        if (verbose) System.out.println("       Applying threshold (method = "+thrMeth+")");
        Auto_Threshold auto_threshold = new Auto_Threshold();
        Object[] results2 = auto_threshold.exec(image2,thrMeth,true,false,true,true,false,true);
        ImagePlus imMask = Threshold.threshold(image2,(Integer) results2[0],Integer.MAX_VALUE);

        // Gradient of cell image
        if (verbose) System.out.println("       Calculating gradient image");
        Strel3D strel3D = Strel3D.Shape.BALL.fromRadius(1);
        image2 = new MorphologicalFilter3DPlugin().process(image2, Morphology.Operation.INTERNAL_GRADIENT, strel3D);

        // Getting the labelled cells
        if (verbose) System.out.println("       Applying watershed segmentation");
        ImagePlus im2 = Watershed.computeWatershed(image2,image1,imMask,8,false,false);

        // Converting the labelled cell image to objects
        if (verbose) System.out.println("       Converting image to objects");
        HCObjectSet objects2 = new ObjectImageConverter().convertImageToObjects(new HCImage(im2));

        // Watershed will give one cell per nucleus and these should already have the same labelling number.
        if (verbose) System.out.println("       Linking primary and secondary objects by ID number");
        new ObjectLinker().linkMatchingIDs(objects1,objects2);

        // Adding objects to workspace
        if (verbose) System.out.println("       Adding objects ("+outputObjectsName.getName()+") to workspace");
        workspace.addObjects(outputObjectsName,objects2);

    }

    @Override
    public HCParameterCollection initialiseParameters() {
        HCParameterCollection parameters = new HCParameterCollection();

        parameters.addParameter(new HCParameter(this,MODULE_TITLE, HCParameter.MODULE_TITLE,"Secondary object identification",true));
        parameters.addParameter(new HCParameter(this,INPUT_IMAGE, HCParameter.INPUT_IMAGE,null,false));
        parameters.addParameter(new HCParameter(this,INPUT_OBJECTS, HCParameter.INPUT_OBJECTS,null,false));
        parameters.addParameter(new HCParameter(this,OUTPUT_OBJECTS, HCParameter.OUTPUT_OBJECTS,null,false));
        parameters.addParameter(new HCParameter(this,MEDIAN_FILTER_RADIUS, HCParameter.DOUBLE,2.0,true));
        parameters.addParameter(new HCParameter(this,THRESHOLD_METHOD, HCParameter.CHOICE_ARRAY,thresholdMethods[0],thresholdMethods,true));

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
