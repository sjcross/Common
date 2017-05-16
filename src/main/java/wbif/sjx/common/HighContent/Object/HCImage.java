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
    private LinkedHashMap<String,HCSingleMeasurement> measurements = new LinkedHashMap<>();

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

    public void addMeasurement(String name, HCSingleMeasurement measurement) {
        measurements.put(name,measurement);

    }

    public HCSingleMeasurement getMeasurement(String name) {
        return measurements.get(name);

    }


    // GETTERS AND SETTERS

    public ImagePlus getImagePlus() {
        return imagePlus;
    }

    public void setImagePlus(ImagePlus imagePlus) {
        this.imagePlus = imagePlus;
    }

    public HashMap<String, HCSingleMeasurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(LinkedHashMap<String, HCSingleMeasurement> measurements) {
        this.measurements = measurements;
    }

}