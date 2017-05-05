package wbif.sjx.common.HighContent.Object;

import ij.ImagePlus;
import ij.plugin.ZProjector;

/**
 * Created by steph on 30/04/2017.
 */
public class Image {
    private ImagePlus imagePlus;


    // CONSTRUCTORS

    public Image (ImagePlus imagePlus) {
        this.imagePlus = imagePlus;
    }


    // PUBLIC METHODS

    public Image projectImageInZ() {
        ZProjector z_projector = new ZProjector(imagePlus);
        z_projector.setMethod(ZProjector.MAX_METHOD);
        z_projector.doProjection();
        ImagePlus iplOut = z_projector.getProjection();

        return new Image(iplOut);

    }


    // GETTERS AND SETTERS

    public ImagePlus getImagePlus() {
        return imagePlus;
    }

    public void setImagePlus(ImagePlus imagePlus) {
        this.imagePlus = imagePlus;
    }
}