package wbif.sjx.common.HighContent.Object;

import ij.ImagePlus;
import ij.plugin.ZProjector;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by steph on 30/04/2017.
 */
public class HCImage {
    private ImagePlus imagePlus;
    private LinkedHashMap<String,HCMeasurement> singleMeasurements = new LinkedHashMap<>();
    private LinkedHashMap<String,HCMultiMeasurement> multiMeasurements = new LinkedHashMap<>();


    // CONSTRUCTORS

    public HCImage(ImagePlus imagePlus) {
        this.imagePlus = imagePlus;
    }


    // PUBLIC METHODS

    public HCImage projectImageInZ() {
        ZProjector z_projector = new ZProjector(imagePlus);
        z_projector.setMethod(ZProjector.MAX_METHOD);
        z_projector.doProjection();
        ImagePlus iplOut = z_projector.getProjection();

        return new HCImage(iplOut);

    }

    public void addMeasurement(String name, HCMeasurement measurement) {
        singleMeasurements.put(name,measurement);

    }

    public HCMeasurement getMeasurement(String name) {
        return singleMeasurements.get(name);

    }


    // GETTERS AND SETTERS

    public ImagePlus getImagePlus() {
        return imagePlus;
    }

    public void setImagePlus(ImagePlus imagePlus) {
        this.imagePlus = imagePlus;
    }

    public HashMap<String, HCMeasurement> getSingleMeasurements() {
        return singleMeasurements;
    }

    public void setSingleMeasurements(LinkedHashMap<String, HCMeasurement> singleMeasurements) {
        this.singleMeasurements = singleMeasurements;
    }

}