package wbif.sjx.common.HighContent.Module;

import fiji.threshold.Auto_Threshold;
import ij.ImagePlus;
import ij.plugin.Filters3D;
import inra.ijpb.binary.conncomp.FloodFillComponentsLabeling3D;
import inra.ijpb.segment.Threshold;
import wbif.sjx.common.HighContent.Object.*;
import wbif.sjx.common.HighContent.Object.ParameterCollection;

import java.util.HashMap;

/**
 * Created by sc13967 on 02/05/2017.
 */
public class IdentifyPrimaryObjects implements Module {
    public static final String MODULE_TITLE = "Module title";
    public static final String INPUT_IMAGE = "Input image";
    public static final String OUTPUT_OBJECT = "Output object";
    public static final String MEDIAN_FILTER_RADIUS = "Median filter radius";
    public static final String THRESHOLD_MULTIPLIER = "Threshold multiplier";

    public void execute(Workspace workspace, ParameterCollection parameters) {
        ImageName targetImageName = (ImageName) parameters.getParameter(this,INPUT_IMAGE).getValue();
        HCObjectName outputObjectName = (HCObjectName) parameters.getParameter(this,OUTPUT_OBJECT).getValue();
        double medFiltR = (double) parameters.getParameter(this,MEDIAN_FILTER_RADIUS).getValue();
        double thrMult = (double) parameters.getParameter(this,THRESHOLD_MULTIPLIER).getValue();

        // Getting image stack
        ImagePlus ipl = workspace.getImages().get(targetImageName).getImagePlus();

        // Applying smoothing filter and threshold
        ipl.setStack(Filters3D.filter(ipl.getImageStack(), Filters3D.MEDIAN, (float) medFiltR, (float) medFiltR, (float) medFiltR));
        Auto_Threshold auto_threshold = new Auto_Threshold();
        Object[] results1 = auto_threshold.exec(ipl,"Otsu",true,false,true,true,false,true);
        ipl = Threshold.threshold(ipl,(Integer) results1[0]*thrMult,Integer.MAX_VALUE);
        FloodFillComponentsLabeling3D ffcl3D = new FloodFillComponentsLabeling3D();
        ipl.setStack(ffcl3D.computeLabels(ipl.getImageStack()));

        // Converting image to objects
        HashMap<Integer,HCObject> objects = new ObjectImageConverter().convertImageToObjects(new Image(ipl));
        workspace.addObjects(outputObjectName,objects);

    }

    @Override
    public void initialiseParameters(ParameterCollection parameters) {
        // Setting the input image stack name
        parameters.addParameter(new Parameter(this,Parameter.MODULE_TITLE,MODULE_TITLE,"Primary object identification",true));
        parameters.addParameter(new Parameter(this,Parameter.IMAGE_NAME,INPUT_IMAGE,null,false));
        parameters.addParameter(new Parameter(this,Parameter.OBJECT_NAME,OUTPUT_OBJECT,null,false));
        parameters.addParameter(new Parameter(this,Parameter.NUMBER,MEDIAN_FILTER_RADIUS,2d,true));
        parameters.addParameter(new Parameter(this,Parameter.NUMBER,THRESHOLD_MULTIPLIER,1d,true));

    }
}
