package wbif.sjx.common.HighContent.Object;

import java.io.File;
import java.util.HashMap;

/**
 * Created by sc13967 on 02/05/2017.
 */
public class Workspace {
    private HashMap<HCObjectName, HCObjectSet> objects = new HashMap<>();
    private HashMap<ImageName, Image> images = new HashMap<>();
    private Metadata metadata = new Metadata();
    private int ID;

    // CONSTRUCTOR

    public Workspace(int ID, File currentFile) {
        this.ID = ID;
        metadata.put(Metadata.FILE,currentFile);

    }

    // PUBLIC METHODS

    public void addObjects(HCObjectName name, HCObjectSet object) {
        objects.put(name, object);
    }

    public void removeObject(String name) {
        objects.remove(name);
    }

    public void addImage(ImageName name, Image image) {
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
            for (Image image:images.values()) {
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

    public void setObjects(HashMap<HCObjectName, HCObjectSet> objects) {
        this.objects = objects;
    }

    public HashMap<ImageName, Image> getImages() {
        return images;
    }

    public void setImages(HashMap<ImageName, Image> images) {
        this.images = images;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public int getID() {
        return ID;
    }
}
