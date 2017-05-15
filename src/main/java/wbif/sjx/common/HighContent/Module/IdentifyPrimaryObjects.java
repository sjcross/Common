package wbif.sjx.common.HighContent.Module;

import fiji.threshold.Auto_Threshold;
import ij.ImagePlus;
import ij.plugin.Filters3D;
import inra.ijpb.binary.conncomp.FloodFillComponentsLabeling3D;
import inra.ijpb.segment.Threshold;
import wbif.sjx.common.HighContent.Object.*;
import wbif.sjx.common.HighContent.Object.ParameterCollection;


/**
 * Created by sc13967 on 02/05/2017.
 */
public class IdentifyPrimaryObjects implements Module {
    public static final String INPUT_IMAGE = "Input image";
    public static final String OUTPUT_OBJECT = "Output object";
    public static final String MEDIAN_FILTER_RADIUS = "Median filter radius";
    public static final String THRESHOLD_MULTIPLIER = "Threshold multiplier";

    public void execute(Workspace workspace, ParameterCollection parameters, boolean verbose) {
        if (verbose) System.out.println("    Running primary object identification");

        // Getting parameters
        double medFiltR = parameters.getValue(this,MEDIAN_FILTER_RADIUS);
        double thrMult = parameters.getValue(this,THRESHOLD_MULTIPLIER);
        HCObjectName outputObjectName = parameters.getValue(this,OUTPUT_OBJECT);

        // Getting image stack
        ImageName targetImageName = parameters.getValue(this,INPUT_IMAGE);
        ImagePlus ipl = workspace.getImages().get(targetImageName).getImagePlus();

        // Applying smoothing filter
        if (verbose) System.out.println("       Applying filter (radius = "+medFiltR+" px)");
        ipl.setStack(Filters3D.filter(ipl.getImageStack(), Filters3D.MEDIAN, (float) medFiltR, (float) medFiltR, (float) medFiltR));

        // Applying threshold
        if (verbose) System.out.println("       Applying thresholding (multplier = "+thrMult+" x)");
        Auto_Threshold auto_threshold = new Auto_Threshold();
        Object[] results1 = auto_threshold.exec(ipl,"Otsu",true,false,true,true,false,true);
        ipl = Threshold.threshold(ipl,(Integer) results1[0]*thrMult,Integer.MAX_VALUE);

        // Applying connected components labelling
        if (verbose) System.out.println("       Applying connected components labelling");
        FloodFillComponentsLabeling3D ffcl3D = new FloodFillComponentsLabeling3D();
        ipl.setStack(ffcl3D.computeLabels(ipl.getImageStack()));

        // Converting image to objects
        if (verbose) System.out.println("       Converting image to objects");
        HCObjectSet objects = new ObjectImageConverter().convertImageToObjects(new Image(ipl));

        // Adding objects to workspace
        if (verbose) System.out.println("       Adding objects ("+outputObjectName.getName()+") to workspace");
        workspace.addObjects(outputObjectName,objects);

    }

    @Override
    public ParameterCollection initialiseParameters() {
        ParameterCollection parameters = new ParameterCollection();

        // Setting the input image stack name
        parameters.addParameter(new Parameter(this,MODULE_TITLE,Parameter.MODULE_TITLE,"Primary object identification",true));
        parameters.addParameter(new Parameter(this,INPUT_IMAGE,Parameter.IMAGE_NAME,null,false));
        parameters.addParameter(new Parameter(this,OUTPUT_OBJECT,Parameter.OBJECT_NAME,null,false));
        parameters.addParameter(new Parameter(this,MEDIAN_FILTER_RADIUS,Parameter.DOUBLE,2.0,true));
        parameters.addParameter(new Parameter(this,THRESHOLD_MULTIPLIER,Parameter.DOUBLE,1.0,true));

        return parameters;

    }
}
