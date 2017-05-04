package wbif.sjx.common.HighContent.Object;

/**
 * Created by sc13967 on 03/05/2017.
 */
public class Parameter {
    public final static int MODULE_TITLE = 0;
    public final static int IMAGE_NAME = 2;
    public final static int OBJECT_NAME = 3;
    public final static int NUMBER = 4;
    public final static int STRING = 5;
    public final static int CHOICE = 6;
    public final static int OBJECT = 7;

    Object module;
    String name;
    int type;
    Object valueRange;
    Object value;
    boolean visible;


    // CONSTRUCTORS

    public Parameter(Object module, int type, String name, Object value, Object defaultValue, boolean visible) {
        this.module = module;
        this.type = type;
        this.name = name;
        this.value = value;
        this.valueRange = defaultValue;
        this.visible = visible;

    }

    public Parameter(Object module, int type, String name, Object value, boolean visible) {
        this.module = module;
        this.type = type;
        this.name = name;
        this.value = value;
        this.valueRange = value;
        this.visible = visible;

    }


    // GETTERS AND SETTERS

    public Object getModule() {
        return module;
    }

    public void setModule(Object module) {
        this.module = module;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getValueRange() {
        return valueRange;
    }

    public void setValueRange(Object valueRange) {
        this.valueRange = valueRange;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}