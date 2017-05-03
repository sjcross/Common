package wbif.sjx.common.HighContent.Module;

import fiji.threshold.Auto_Threshold;
import ij.ImagePlus;
import ij.plugin.Filters3D;
import ij.process.ImageProcessor;
import inra.ijpb.binary.conncomp.FloodFillComponentsLabeling3D;
import inra.ijpb.segment.Threshold;
import wbif.sjx.common.HighContent.Object.*;
import wbif.sjx.common.MathFunc.ArrayFunc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by sc13967 on 02/05/2017.
 */
public class IdentifyPrimaryObjects implements Module {
    public static final String INPUT_IMAGE = "Input image";
    public static final String OUTPUT_OBJECT = "Output object";
    public static final String MEDIAN_FILTER_RADIUS = "Median filter radius";
    public static final String THRESHOLD_MULTIPLIER = "Threshold multiplier";

    public void execute(Workspace workspace) {
        // Getting parameters for this particular class instance from workspace
        HashMap<String, Object> parameters = getParameters(workspace);

        ImageName targetImageName = (ImageName) parameters.get(INPUT_IMAGE);
        HCObjectName outputObjectName = (HCObjectName) parameters.get(OUTPUT_OBJECT);
        double medFiltR = (double) parameters.get(MEDIAN_FILTER_RADIUS);
        double thrMult = (double) parameters.get(THRESHOLD_MULTIPLIER);

        // Getting image stack
        ImagePlus ipl = workspace.getImages().get(targetImageName).getImage();

        // Applying smoothing filter and threshold
        ipl.setStack(Filters3D.filter(ipl.getImageStack(), Filters3D.MEDIAN, (float) medFiltR, (float) medFiltR, (float) medFiltR));
        Auto_Threshold auto_threshold = new Auto_Threshold();
        Object[] results1 = auto_threshold.exec(ipl,"Otsu",true,false,true,true,false,true);
        ipl = Threshold.threshold(ipl,(Integer) results1[0]*thrMult,Integer.MAX_VALUE);
        FloodFillComponentsLabeling3D ffcl3D = new FloodFillComponentsLabeling3D();
        ipl.setStack(ffcl3D.computeLabels(ipl.getImageStack()));

        // Need to get coordinates and convert to a HCObject
        ArrayList<Integer> IDs = new ArrayList<>();
        ArrayList<HCObject> objects = new ArrayList<>(); //Local ArrayList of objects
        workspace.addObject(outputObjectName,objects);

        ImageProcessor ipr = ipl.getProcessor();

        int h = ipl.getHeight();
        int w = ipl.getWidth();
        int d = ipl.getNSlices();

        int ind = 0;
        for (int z=0;z<d;z++) {
            ipl.setSlice(z+1);
            for (int x=0;x<w;x++) {
                for (int y=0;y<h;y++) {
                    int ID = ipr.getPixel(x,y); //Pixel value

                    if (ID != 0) { //Corresponds to an object
                        if (ArrayFunc.contains(IDs,ID)) { //Already has an assigned "blob" object
                            int tempInd = IDs.indexOf(ID);
                            objects.get(tempInd).addCoordinate(HCObject.X,x);
                            objects.get(tempInd).addCoordinate(HCObject.Y,y);
                            objects.get(tempInd).addCoordinate(HCObject.Z,z);

                        } else { //First instance of detection
                            IDs.add(ind,ID);
                            objects.add(ind, new HCObject());
                            objects.get(ind).addCoordinate(HCObject.X,x);
                            objects.get(ind).addCoordinate(HCObject.Y,y);
                            objects.get(ind).addCoordinate(HCObject.Z,z);

                            ind++;

                        }
                    }
                }
            }
        }

    }

    public LinkedHashMap<String,Object> initialiseParameters() {
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();

        // Setting the input image stack name
        parameters.put("Primary object identification module",new ModuleTitle("Primary object identification"));
        parameters.put(INPUT_IMAGE,new ImageName(""));
        parameters.put(OUTPUT_OBJECT,new HCObjectName(""));
        parameters.put(MEDIAN_FILTER_RADIUS,2d);
        parameters.put(THRESHOLD_MULTIPLIER,1d);

        return parameters;

    }
}
