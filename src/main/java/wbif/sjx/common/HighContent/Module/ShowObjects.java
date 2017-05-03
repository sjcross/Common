package wbif.sjx.common.HighContent.Module;

import ij.IJ;
import ij.ImagePlus;
import ij.LookUpTable;
import ij.process.LUT;
import wbif.sjx.common.HighContent.Object.*;
import wbif.sjx.common.Object.RandomLUT;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by sc13967 on 03/05/2017.
 */
public class ShowObjects implements Module {
    public final static String INPUT_OBJECTS = "Input objects";

    @Override
    public void execute(Workspace workspace) {
        // Loading objects
        ParameterCollection parameterCollection = workspace.getParameters();
        HCObjectName inputObjectName = (HCObjectName) parameterCollection.getParameter(this,INPUT_OBJECTS);
        ArrayList<HCObject> inputObjects = workspace.getObjects().get(inputObjectName);

        // Getting range of object pixels
        int nDims = Collections.max(inputObjects.get(0).getCoordinates().keySet())+1;
        nDims = nDims <= 5 ? 5 : nDims;

        int[][] dimSize = new int[nDims][2];

        for (HCObject object:inputObjects) {
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
        ImagePlus ipl = IJ.createHyperStack(inputObjectName.toString(),dimSize[HCObject.X][1]+1,
                dimSize[HCObject.Y][1]+1,dimSize[HCObject.C][1]+1,dimSize[HCObject.Z][1]+1,dimSize[HCObject.T][1]+1,16);

        // Labelling pixels in image
        for (HCObject object:inputObjects) {
            ArrayList<Integer> x = object.getCoordinate(HCObject.X);
            ArrayList<Integer> y = object.getCoordinate(HCObject.Y);
            ArrayList<Integer> z = object.getCoordinate(HCObject.Z);
            ArrayList<Integer> c = object.getCoordinate(HCObject.C);
            ArrayList<Integer> t = object.getCoordinate(HCObject.T);

            for (int i=0;i<x.size();i++) {
                int cPos = c==null ? 0 : c.get(i);
                int zPos = z==null ? 0 : z.get(i);
                int tPos = t==null ? 0 : t.get(i);

                ipl.setPosition(cPos+1,zPos+1,tPos+1);
                ipl.getProcessor().set(x.get(i),y.get(i),object.getID());
            }
        }

        // Creating a random colour LUT and assigning it to the image (maximising intensity range to 0-255)
        LUT randomLUT = new RandomLUT().getLUT();
        ipl.setLut(randomLUT);
        ipl.getProcessor().setMinAndMax(0,255);

        // Showing the image
        ipl.show();

    }

    @Override
    public void initialiseParameters(ParameterCollection parameters) {
        parameters.addParameter(this,INPUT_OBJECTS,"Obj1",false);

    }
}