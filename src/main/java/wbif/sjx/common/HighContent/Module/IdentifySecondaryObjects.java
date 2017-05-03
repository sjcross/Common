package wbif.sjx.common.HighContent.Module;

import fiji.threshold.Auto_Threshold;
import ij.ImagePlus;
import ij.plugin.Filters3D;
import inra.ijpb.binary.conncomp.FloodFillComponentsLabeling3D;
import inra.ijpb.morphology.Morphology;
import inra.ijpb.morphology.Strel3D;
import inra.ijpb.plugins.MorphologicalFilter3DPlugin;
import inra.ijpb.segment.Threshold;
import inra.ijpb.watershed.Watershed;
import wbif.sjx.common.HighContent.Object.*;

import java.util.ArrayList;

/**
 * Created by sc13967 on 03/05/2017.
 */
public class IdentifySecondaryObjects implements Module {
    public static final String INPUT_IMAGE = "Input image";
    public static final String INPUT_OBJECTS = "Input objects";
    public static final String OUTPUT_OBJECTS = "Output objects";
    public static final String MEDIAN_FILTER_RADIUS = "Median filter radius";


    @Override
    public void execute(Workspace workspace) {
        // Getting relevant parameters
        ParameterCollection parameters = workspace.getParameters();
        double medFiltR = (double) parameters.getParameter(this,MEDIAN_FILTER_RADIUS);

        // Loading images and objects into workspace
        ImageName inputImageName = (ImageName) parameters.getParameter(this,INPUT_IMAGE);
        HCObjectName inputObjectsName = (HCObjectName) parameters.getParameter(this,INPUT_OBJECTS);
        Image inputImage = workspace.getImages().get(inputImageName);
        ArrayList<HCObject> inputObjects = workspace.getObjects().get(inputObjectsName);

        // Initialising the output objects ArrayList and adding to the workspace
        HCObjectName outputObjectsName = (HCObjectName) parameters.getParameter(this,OUTPUT_OBJECTS);
        ArrayList<HCObject> outputObjects = new ArrayList<>();
        workspace.addObjects(outputObjectsName,outputObjects);

        ImagePlus im1 = inputImage.getImage();

        // Segmenting nuclei image
        // Filtering nuclei image
        im1.setStack(Filters3D.filter(im1.getImageStack(), Filters3D.MEDIAN, (float) medFiltR, (float) medFiltR,1));

//        // Thresholding nuclei image
//        Auto_Threshold auto_threshold = new Auto_Threshold();
//        Object[] results1 = auto_threshold.exec(im1,thr_meth,true,false,true,true,false,true);
//        im1 = Threshold.threshold(im1,Math.max(min_thr, (Integer) results1[0]),Integer.MAX_VALUE);
//
//        // Identifying objects
//        im1.setStack(new FloodFillComponentsLabeling3D().computeLabels(im1.getImageStack()));
//
//        // Segmenting cell image
//        // Filtering cell image
//        im2.setStack(Filters3D.filter(im2.getImageStack(), Filters3D.MEDIAN, (float) medFiltR, (float) medFiltR,1));
//
//        // Thresholded cell image
//        Object[] results2 = auto_threshold.exec(im2,thr_meth,true,false,true,true,false,true);
//        ImagePlus im_mask = Threshold.threshold(im2,(Integer) results2[0],Integer.MAX_VALUE);
//
//        // Gradient of cell image
//        Strel3D strel3D = Strel3D.Shape.BALL.fromRadius(1);
//        im2 = new MorphologicalFilter3DPlugin().process(im2, Morphology.Operation.INTERNAL_GRADIENT, strel3D);
//
//        im2 = Watershed.computeWatershed(im2,im1,im_mask,8,false,false);

    }

    @Override
    public void initialiseParameters(ParameterCollection parameters) {
        parameters.addParameter(this,"Secondary object identification module",new ModuleTitle("Secondary object identification"),true);
        parameters.addParameter(this,INPUT_IMAGE,"Im1",false);
        parameters.addParameter(this,INPUT_OBJECTS,"Obj1",false);
        parameters.addParameter(this,OUTPUT_OBJECTS,"Obj2",false);
        parameters.addParameter(this,MEDIAN_FILTER_RADIUS,2d,true);

    }
}
