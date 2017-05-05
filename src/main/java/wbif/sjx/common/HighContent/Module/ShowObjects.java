package wbif.sjx.common.HighContent.Module;

import ij.process.LUT;
import wbif.sjx.common.HighContent.Object.*;
import wbif.sjx.common.HighContent.Object.ParameterCollection;
import wbif.sjx.common.Object.RandomLUT;

import java.util.HashMap;

/**
 * Created by sc13967 on 03/05/2017.
 */
public class ShowObjects implements Module {
    public static final String MODULE_TITLE = "Module title";
    public final static String INPUT_OBJECTS = "Input objects";
    public final static String TEMPLATE_IMAGE = "Template image";

    @Override
    public void execute(Workspace workspace,ParameterCollection parameters) {
        // Loading objects
        HCObjectName inputObjectName = (HCObjectName) parameters.getParameter(this,INPUT_OBJECTS).getValue();
        HashMap<Integer,HCObject> inputObjects = workspace.getObjects().get(inputObjectName);

        Image templateImage;
        if (parameters.getParameter(this,TEMPLATE_IMAGE) == null) {
            templateImage = null;

        } else {
            ImageName templateImageName = (ImageName) parameters.getParameter(this,TEMPLATE_IMAGE).getValue();
            templateImage = workspace.getImages().get(templateImageName);

        }

        // Converting objects to an image
        Image image = new ObjectImageConverter().convertObjectsToImage(inputObjects,templateImage);
        image.getImagePlus().setTitle(inputObjectName.getName());

        // Creating a random colour LUT and assigning it to the image (maximising intensity range to 0-255)
        LUT randomLUT = new RandomLUT().getLUT();
        image.getImagePlus().setLut(randomLUT);
        image.getImagePlus().getProcessor().setMinAndMax(0,255);

        // Showing the image
        image.getImagePlus().show();

    }

    @Override
    public void initialiseParameters(ParameterCollection parameters) {
        parameters.addParameter(new Parameter(this,Parameter.MODULE_TITLE,MODULE_TITLE,"Show objects",true));
        parameters.addParameter(new Parameter(this,Parameter.OBJECT_NAME,INPUT_OBJECTS,null,false));
        parameters.addParameter(new Parameter(this,Parameter.IMAGE_NAME,TEMPLATE_IMAGE,null,false));

    }
}