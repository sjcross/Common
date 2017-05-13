package wbif.sjx.common.HighContent.Module;

import ij.IJ;
import ij.ImagePlus;
import ij.measure.Calibration;
import ij.process.ImageProcessor;
import wbif.sjx.common.HighContent.Object.*;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by sc13967 on 04/05/2017.
 */
public class ObjectImageConverter implements Module {
    public static final String TEMPLATE_IMAGE = "Template image";
    public static final String INPUT_OBJECTS = "Input objects";
    public static final String INPUT_IMAGE = "Input image";
    public static final String OUTPUT_OBJECTS = "Output objects";
    public static final String OUTPUT_IMAGE = "Output image";
    public static final String CONVERSION_MODE = "Conversion mode";

    public static final int IMAGE_TO_OBJECTS = 0;
    public static final int OBJECTS_TO_IMAGE = 1;

    @Override
    public void execute(Workspace workspace, ParameterCollection parameters, boolean verbose) {
        int conversionMode = parameters.getValue(this,CONVERSION_MODE);

        if (conversionMode == IMAGE_TO_OBJECTS) {
            ImageName imageName = parameters.getValue(this,INPUT_IMAGE);
            HCObjectName objectName = parameters.getValue(this,OUTPUT_OBJECTS);

            Image image = workspace.getImages().get(imageName);

            HCObjectSet objects = convertImageToObjects(image);

            workspace.addObjects(objectName,objects);

        } else if (conversionMode == OBJECTS_TO_IMAGE) {
            HCObjectName objectName = parameters.getValue(this,INPUT_OBJECTS);
            ImageName templateImageName = parameters.getValue(this,TEMPLATE_IMAGE);
            ImageName outputImageName = parameters.getValue(this,OUTPUT_IMAGE);

            HCObjectSet objects = workspace.getObjects().get(objectName);
            Image templateImage = workspace.getImages().get(templateImageName);

            Image image = convertObjectsToImage(objects,templateImage);

            workspace.addImage(outputImageName,image);

        }
    }

    @Override
    public ParameterCollection initialiseParameters() {
        ParameterCollection parameters = new ParameterCollection();

        parameters.addParameter(new Parameter(this,MODULE_TITLE,Parameter.MODULE_TITLE,"Object-image converter",true));
        parameters.addParameter(new Parameter(this,TEMPLATE_IMAGE,Parameter.IMAGE_NAME,null,false));
        parameters.addParameter(new Parameter(this,INPUT_OBJECTS,Parameter.OBJECT_NAME,null,false));
        parameters.addParameter(new Parameter(this,INPUT_IMAGE,Parameter.IMAGE_NAME,null,false));
        parameters.addParameter(new Parameter(this,OUTPUT_OBJECTS,Parameter.OBJECT_NAME,null,false));
        parameters.addParameter(new Parameter(this,OUTPUT_IMAGE,Parameter.IMAGE_NAME,null,false));
        parameters.addParameter(new Parameter(this,CONVERSION_MODE,Parameter.INTEGER,0,false));

        return parameters;

    }

    public Image convertObjectsToImage(HCObjectSet objects, Image templateImage) {
        ImagePlus ipl;

        if (templateImage == null) {
            // Getting range of object pixels
            int nDims = Collections.max(objects.values().iterator().next().getCoordinates().keySet()) + 1;
            nDims = nDims <= 5 ? 5 : nDims;

            int[][] dimSize = new int[nDims][2];

            for (HCObject object : objects.values()) {
                int[][] currDimSize = object.getCoordinateRange();
                for (int dim = 0; dim < dimSize.length; dim++) {
                    if (currDimSize[dim][0] < dimSize[dim][0]) {
                        dimSize[dim][0] = currDimSize[dim][0];
                    }

                    if (currDimSize[dim][1] > dimSize[dim][1]) {
                        dimSize[dim][1] = currDimSize[dim][1];
                    }
                }
            }

            // Creating a new image
            ipl = IJ.createHyperStack("Objects", dimSize[HCObject.X][1] + 1,
                    dimSize[HCObject.Y][1] + 1, dimSize[HCObject.C][1] + 1, dimSize[HCObject.Z][1] + 1, dimSize[HCObject.T][1] + 1, 16);

        } else {
            ImagePlus templateIpl = templateImage.getImagePlus();
            ipl = IJ.createHyperStack("Objects",templateIpl.getWidth(),templateIpl.getHeight(),
                    templateIpl.getNChannels(),templateIpl.getNSlices(),templateIpl.getNFrames(),16);
        }

        // Labelling pixels in image
        for (HCObject object:objects.values()) {
            ArrayList<Integer> x = object.getCoordinates(HCObject.X);
            ArrayList<Integer> y = object.getCoordinates(HCObject.Y);
            ArrayList<Integer> z = object.getCoordinates(HCObject.Z);
            ArrayList<Integer> c = object.getCoordinates(HCObject.C);
            ArrayList<Integer> t = object.getCoordinates(HCObject.T);

            for (int i=0;i<x.size();i++) {
                int cPos = c==null ? 0 : c.get(i);
                int zPos = z==null ? 0 : z.get(i);
                int tPos = t==null ? 0 : t.get(i);

                ipl.setPosition(cPos+1,zPos+1,tPos+1);
                ipl.getProcessor().set(x.get(i),y.get(i),object.getID());
            }
        }

        return new Image(ipl);

    }

    public HCObjectSet convertImageToObjects(Image image) {
        // Converting to ImagePlus for this operation
        ImagePlus ipl = image.getImagePlus();

        // Need to get coordinates and convert to a HCObject
        HCObjectSet objects = new HCObjectSet(); //Local ArrayList of objects

        ImageProcessor ipr = ipl.getProcessor();

        int h = ipl.getHeight();
        int w = ipl.getWidth();
        int d = ipl.getNSlices();

        for (int z=0;z<d;z++) {
            ipl.setSlice(z+1);
            for (int x=0;x<w;x++) {
                for (int y=0;y<h;y++) {
                    int ID = (int) ipr.getPixelValue(x,y); //Pixel value

                    if (ID != 0) {
                        objects.computeIfAbsent(ID, k -> new HCObject(ID));

                        objects.get(ID).addCoordinate(HCObject.X, x);
                        objects.get(ID).addCoordinate(HCObject.Y, y);
                        objects.get(ID).addCoordinate(HCObject.Z, z);

                    }
                }
            }
        }

        // Adding distance calibration to each object
        Calibration calibration = ipl.getCalibration();
        for (HCObject object:objects.values()) {
            object.addCalibration(HCObject.X,calibration.getX(1));
            object.addCalibration(HCObject.Y,calibration.getY(1));
            object.addCalibration(HCObject.Z,calibration.getZ(1));
            object.setCalibratedUnits(calibration.getUnits());

        }

        return objects;

    }

}