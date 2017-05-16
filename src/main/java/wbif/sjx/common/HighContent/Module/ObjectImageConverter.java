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
public class ObjectImageConverter extends HCModule {
    public static final String TEMPLATE_IMAGE = "Template image";
    public static final String INPUT_OBJECTS = "Input objects";
    public static final String INPUT_IMAGE = "Input image";
    public static final String OUTPUT_OBJECTS = "Output objects";
    public static final String OUTPUT_IMAGE = "Output image";
    public static final String CONVERSION_MODE = "Conversion mode";

    public static final int IMAGE_TO_OBJECTS = 0;
    public static final int OBJECTS_TO_IMAGE = 1;


    public HCImage convertObjectsToImage(HCObjectSet objects, HCImage templateImage) {
        ImagePlus ipl;

        if (templateImage == null) {
            // Getting range of object pixels
            int[][] coordinateRange = new int[5][2];

            for (HCObject object : objects.values()) {
                // Getting range of XYZ
                int[][] currCoordinateRange = object.getCoordinateRange();
                for (int dim = 0; dim < coordinateRange.length; dim++) {
                    if (currCoordinateRange[dim][0] < coordinateRange[dim][0]) {
                        coordinateRange[dim][0] = currCoordinateRange[dim][0];
                    }

                    if (currCoordinateRange[dim][1] > coordinateRange[dim][1]) {
                        coordinateRange[dim][1] = currCoordinateRange[dim][1];
                    }
                }

                // Getting range of additional dimensions
                for (int dim:object.getPositions().keySet()) {
                    int currValue = object.getPosition(dim);

                    if (currValue < coordinateRange[dim][0]) {
                        coordinateRange[dim][0] = currValue;
                    }

                    if (currValue > coordinateRange[dim][1]) {
                        coordinateRange[dim][1] = currValue;
                    }
                }
            }

            // Creating a new image
            ipl = IJ.createHyperStack("Objects", coordinateRange[HCObject.X][1] + 1,
                    coordinateRange[HCObject.Y][1] + 1, coordinateRange[HCObject.C][1] + 1,
                    coordinateRange[HCObject.Z][1] + 1, coordinateRange[HCObject.T][1] + 1, 16);

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
            Integer c = object.getCoordinates(HCObject.C);
            Integer t = object.getCoordinates(HCObject.T);

            for (int i=0;i<x.size();i++) {
                int zPos = z==null ? 0 : z.get(i);
                int cPos = c==null ? -1 : c;
                int tPos = t==null ? -1 : t;

                ipl.setPosition(cPos+1,zPos+1,tPos+1);
                ipl.getProcessor().set(x.get(i),y.get(i),object.getID());
            }
        }

        return new HCImage(ipl);

    }

    public HCObjectSet convertImageToObjects(HCImage image) {
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

    @Override
    public void execute(HCWorkspace workspace, boolean verbose) {
        int conversionMode = parameters.getValue(CONVERSION_MODE);

        if (conversionMode == IMAGE_TO_OBJECTS) {
            HCImageName imageName = parameters.getValue(INPUT_IMAGE);
            HCObjectName objectName = parameters.getValue(OUTPUT_OBJECTS);

            HCImage image = workspace.getImages().get(imageName);

            HCObjectSet objects = convertImageToObjects(image);

            workspace.addObjects(objectName,objects);

        } else if (conversionMode == OBJECTS_TO_IMAGE) {
            HCObjectName objectName = parameters.getValue(INPUT_OBJECTS);
            HCImageName templateImageName = parameters.getValue(TEMPLATE_IMAGE);
            HCImageName outputImageName = parameters.getValue(OUTPUT_IMAGE);

            HCObjectSet objects = workspace.getObjects().get(objectName);
            HCImage templateImage = workspace.getImages().get(templateImageName);

            HCImage image = convertObjectsToImage(objects,templateImage);

            workspace.addImage(outputImageName,image);

        }
    }

    @Override
    public HCParameterCollection initialiseParameters() {
        HCParameterCollection parameters = new HCParameterCollection();

        parameters.addParameter(new HCParameter(this,MODULE_TITLE, HCParameter.MODULE_TITLE,"Object-image converter",true));
        parameters.addParameter(new HCParameter(this,TEMPLATE_IMAGE, HCParameter.INPUT_IMAGE,null,false));
        parameters.addParameter(new HCParameter(this,INPUT_OBJECTS, HCParameter.INPUT_OBJECTS,null,false));
        parameters.addParameter(new HCParameter(this,INPUT_IMAGE, HCParameter.INPUT_IMAGE,null,false));
        parameters.addParameter(new HCParameter(this,OUTPUT_OBJECTS, HCParameter.OUTPUT_OBJECTS,null,false));
        parameters.addParameter(new HCParameter(this,OUTPUT_IMAGE, HCParameter.OUTPUT_IMAGE,null,false));
        parameters.addParameter(new HCParameter(this,CONVERSION_MODE, HCParameter.INTEGER,0,false));

        return parameters;

    }

    @Override
    public HCParameterCollection getActiveParameters() {
        return parameters;
    }

}