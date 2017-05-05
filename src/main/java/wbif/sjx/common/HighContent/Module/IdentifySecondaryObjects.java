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

import java.util.HashMap;

/**
 * Created by sc13967 on 03/05/2017.
 */
public class IdentifySecondaryObjects implements Module {
    public static final String INPUT_IMAGE = "Input image";
    public static final String INPUT_OBJECTS = "Input objects";
    public static final String OUTPUT_OBJECTS = "Output objects";
    public static final String MEDIAN_FILTER_RADIUS = "Median filter radius";
    public static final String THRESHOLD_METHOD = "Threshold method";


    @Override
    public void execute(Workspace workspace) {
        // Getting relevant parameters
        ParameterCollection parameters = workspace.getParameters();
        double medFiltR = (double) parameters.getParameter(this,MEDIAN_FILTER_RADIUS).getValue();
        String thrMeth = (String) parameters.getParameter(this,THRESHOLD_METHOD).getValue();

        // Loading images and objects into workspace
        ImageName inputImageName = (ImageName) parameters.getParameter(this,INPUT_IMAGE).getValue();
        Image inputImage2 = workspace.getImages().get(inputImageName);
        ImagePlus image2 = inputImage2.getImagePlus();

        HCObjectName inputObjectsName = (HCObjectName) parameters.getParameter(this,INPUT_OBJECTS).getValue();
        HashMap<Integer,HCObject> objects1 = workspace.getObjects().get(inputObjectsName);

        // Initialising the output objects ArrayList and adding to the workspace
        HCObjectName outputObjectsName = (HCObjectName) parameters.getParameter(this,OUTPUT_OBJECTS).getValue();

        // Getting nuclei objects as image
        ImagePlus image1 = new ObjectImageConverter().convertObjectsToImage(objects1,inputImage2).getImagePlus();

        // Segmenting cell image
        // Filtering cell image
        image2.setStack(Filters3D.filter(image2.getImageStack(), Filters3D.MEDIAN, (float) medFiltR, (float) medFiltR,1));

        // Thresholded cell image
        Auto_Threshold auto_threshold = new Auto_Threshold();
        Object[] results2 = auto_threshold.exec(image2,thrMeth,true,false,true,true,false,true);
        ImagePlus imMask = Threshold.threshold(image2,(Integer) results2[0],Integer.MAX_VALUE);

        // Gradient of cell image
        Strel3D strel3D = Strel3D.Shape.BALL.fromRadius(1);
        image2 = new MorphologicalFilter3DPlugin().process(image2, Morphology.Operation.INTERNAL_GRADIENT, strel3D);

        // Getting the labelled cells
        ImagePlus im2 = Watershed.computeWatershed(image2,image1,imMask,8,false,false);

        // Converting the labelled cell image to objects
        HashMap<Integer,HCObject> objects2 = new ObjectImageConverter().convertImageToObjects(new Image(im2));

        // Watershed will give one cell per nucleus and these should already have the same labelling number.
        new ObjectLinker().linkMatchingIDs(objects1,objects2);

        // Adding objects to workspace
        workspace.addObjects(outputObjectsName,objects2);

    }

    @Override
    public void initialiseParameters(ParameterCollection parameters) {
        parameters.addParameter(new Parameter(this,Parameter.MODULE_TITLE,"Secondary object identification module","Secondary object identification",true));
        parameters.addParameter(new Parameter(this,Parameter.IMAGE_NAME,INPUT_IMAGE,null,false));
        parameters.addParameter(new Parameter(this,Parameter.OBJECT_NAME,INPUT_OBJECTS,null,false));
        parameters.addParameter(new Parameter(this,Parameter.OBJECT_NAME,OUTPUT_OBJECTS,null,false));
        parameters.addParameter(new Parameter(this,Parameter.NUMBER,MEDIAN_FILTER_RADIUS,2d,true));
        String[] thresholdMethods = new String[]{"Otsu","Huang","Li"};
        parameters.addParameter(new Parameter(this,Parameter.CHOICE,THRESHOLD_METHOD,thresholdMethods[0],thresholdMethods,true));

    }
}
