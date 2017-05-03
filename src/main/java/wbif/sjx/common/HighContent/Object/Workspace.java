package wbif.sjx.common.HighContent.Object;

import wbif.sjx.common.HighContent.Module.ModuleCollection;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sc13967 on 02/05/2017.
 */
public class Workspace {
    private HashMap<HCObjectName, ArrayList<HCObject>> objects = new HashMap<>();
    private HashMap<ImageName, Image> images = new HashMap<>();
    private ParameterCollection parameters = new ParameterCollection();
    private ModuleCollection modules = new ModuleCollection();
    private File activeFile = null;


    // PUBLIC METHODS

    public void addObject(HCObjectName name, ArrayList<HCObject> object) {
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


    // GETTERS AND SETTERS

    public HashMap<HCObjectName, ArrayList<HCObject>> getObjects() {
        return objects;
    }

    public void setObjects(HashMap<HCObjectName, ArrayList<HCObject>> objects) {
        this.objects = objects;
    }

    public HashMap<ImageName, Image> getImages() {
        return images;
    }

    public void setImages(HashMap<ImageName, Image> images) {
        this.images = images;
    }

    public ParameterCollection getParameters() {
        return parameters;
    }

    public void setParameters(ParameterCollection parameters) {
        this.parameters = parameters;
    }

    public ModuleCollection getModules() {
        return modules;
    }

    public void setModules(ModuleCollection modules) {
        this.modules = modules;
    }

    public File getActiveFile() {
        return activeFile;
    }

    public void setActiveFile(File activeFile) {
        this.activeFile = activeFile;
    }
}
