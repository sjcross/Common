package wbif.sjx.common.HighContent.Object;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by sc13967 on 02/05/2017.
 */
public class HCWorkspace {
    private LinkedHashMap<HCObjectName, HCObjectSet> objects = new LinkedHashMap<>();
    private LinkedHashMap<HCImageName, HCImage> images = new LinkedHashMap<>();
    private HCMetadata metadata = new HCMetadata();
    private int ID;

    // CONSTRUCTOR

    public HCWorkspace(int ID, File currentFile) {
        this.ID = ID;
        metadata.put(HCMetadata.FILE,currentFile);

    }

    // PUBLIC METHODS

    public void addObjects(HCObjectName name, HCObjectSet object) {
        objects.put(name, object);
    }

    public void removeObject(String name) {
        objects.remove(name);
    }

    public void addImage(HCImageName name, HCImage image) {
        images.put(name, image);
    }

    public void removeImage(String name) {
        images.remove(name);
    }

    /**
     * Used to reduce memory of the workspace (particularly for batch processing).
     * @param retainMeasurements Delete image data, but leave measurements
     */
    public void clearAllImages(boolean retainMeasurements) {
        if (retainMeasurements) {
            // Sets the ImagePlus to null, but leaves measurements
            for (HCImage image:images.values()) {
                image.setImagePlus(null);
            }

        } else {
            // Removes all the data
            images = null;
        }
    }


    // GETTERS AND SETTERS

    public HashMap<HCObjectName, HCObjectSet> getObjects() {
        return objects;
    }

    public void setObjects(LinkedHashMap<HCObjectName, HCObjectSet> objects) {
        this.objects = objects;
    }

    public HashMap<HCImageName, HCImage> getImages() {
        return images;
    }

    public void setImages(LinkedHashMap<HCImageName, HCImage> images) {
        this.images = images;
    }

    public HCMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(HCMetadata metadata) {
        this.metadata = metadata;
    }

    public int getID() {
        return ID;
    }
}
