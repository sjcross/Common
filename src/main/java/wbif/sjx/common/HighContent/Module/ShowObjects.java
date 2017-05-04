package wbif.sjx.common.HighContent.Module;

import ij.process.LUT;
import wbif.sjx.common.HighContent.Object.*;
import wbif.sjx.common.HighContent.Object.ParameterCollection;
import wbif.sjx.common.Object.RandomLUT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by sc13967 on 03/05/2017.
 */
public class ShowObjects implements Module {
    public final static String INPUT_OBJECTS = "Input objects";
    public final static String TEMPLATE_IMAGE = "Template image";

    @Override
    public void execute(Workspace workspace) {
        // Loading objects
        ParameterCollection parameterCollection = workspace.getParameters();
        HCObjectName inputObjectName = (HCObjectName) parameterCollection.getParameter(this,INPUT_OBJECTS).getValue();
        HashMap<Integer,HCObject> inputObjects = workspace.getObjects().get(inputObjectName);

        Image templateImage;
        if (parameterCollection.getParameter(this,TEMPLATE_IMAGE) == null) {
            templateImage = null;

        } else {
            ImageName templateImageName = (ImageName) parameterCollection.getParameter(this,TEMPLATE_IMAGE).getValue();
            templateImage = workspace.getImages().get(templateImageName);

        }

        // Converting objects to an image
        Image image = new ObjectImageConverter().convertObjectsToImage(inputObjects,templateImage);
        image.getImage().setTitle(inputObjectName.getName());

        // Creating a random colour LUT and assigning it to the image (maximising intensity range to 0-255)
        LUT randomLUT = new RandomLUT().getLUT();
        image.getImage().setLut(randomLUT);
        image.getImage().getProcessor().setMinAndMax(0,255);

        // Showing the image
        image.getImage().show();

    }

    @Override
    public void initialiseParameters(ParameterCollection parameters) {
        parameters.addParameter(new Parameter(this,Parameter.OBJECT_NAME,INPUT_OBJECTS,null,false));
        parameters.addParameter(new Parameter(this,Parameter.IMAGE_NAME,TEMPLATE_IMAGE,null,false));

    }
}