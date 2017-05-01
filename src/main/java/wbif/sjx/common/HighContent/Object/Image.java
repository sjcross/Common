package wbif.sjx.common.HighContent.Object;

import ij.ImagePlus;

/**
 * Created by steph on 30/04/2017.
 */
public class Image {
    private ImagePlus image;


    // CONSTRUCTORS

    public Image (ImagePlus image) {
        this.image = image;
    }


    // GETTERS AND SETTERS

    public ImagePlus getImage() {
        return image;
    }

    public void setImage(ImagePlus image) {
        this.image = image;
    }
}