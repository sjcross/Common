package wbif.sjx.common.HighContent.Object;

import ij.ImagePlus;
import ij.plugin.ZProjector;

/**
 * Created by steph on 30/04/2017.
 */
public class Image {
    private ImagePlus image;


    // CONSTRUCTORS

    public Image (ImagePlus image) {
        this.image = image;
    }


    // PUBLIC METHODS

    public Image projectImageInZ() {
        ZProjector z_projector = new ZProjector(image);
        z_projector.setMethod(ZProjector.MAX_METHOD);
        z_projector.doProjection();
        ImagePlus iplOut = z_projector.getProjection();

        return new Image(iplOut);

    }


    // GETTERS AND SETTERS

    public ImagePlus getImage() {
        return image;
    }

    public void setImage(ImagePlus image) {
        this.image = image;
    }
}