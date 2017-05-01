package wbif.sjx.common.HighContent.Object;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by steph on 30/04/2017.
 */
public class Workspace {
    private HashMap<String, ArrayList<HCObject>> objects = new HashMap<>();
    private HashMap<String, Image> images = new HashMap<>();


    // PUBLIC METHODS

    public void addObject(String name, ArrayList<HCObject> object) {
        objects.put(name, object);
    }

    public void removeObject(String name) {
        objects.remove(name);
    }

    public void addImage(String name, Image image) {
        images.put(name, image);
    }

    public void removeImage(String name) {
        images.remove(name);
    }


    // GETTERS AND SETTERS

    public HashMap<String, ArrayList<HCObject>> getObjects() {
        return objects;
    }

    public void setObjects(HashMap<String, ArrayList<HCObject>> objects) {
        this.objects = objects;
    }

    public HashMap<String, Image> getImages() {
        return images;
    }

    public void setImages(HashMap<String, Image> images) {
        this.images = images;
    }

}
