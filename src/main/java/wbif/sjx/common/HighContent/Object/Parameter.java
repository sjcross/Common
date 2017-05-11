package wbif.sjx.common.HighContent.Object;

/**
 * Created by sc13967 on 03/05/2017.
 */
public class Parameter {
    /**
     * Class only used for module titles.  These are displayed by ParameterWindow, but don't have anything to set
     */
    public final static int MODULE_TITLE = 0;

    /**
     * Name of Image class objects.  Used to connect images to be analysed between classes
     */
    public final static int IMAGE_NAME = 1;

    /**
     * Name of HCObject class objects.  Used to connect HCObjects to be analysed between classes
     */
    public final static int OBJECT_NAME = 2;

    /**
     * Single number variable.  These can be set in ParameterWindow by numeric fields
     */
    public final static int NUMBER = 3;

    /**
     * Single string variable.  These can be set in ParameterWindow by string fields
     */
    public final static int STRING = 4;

    /**
     * String array containing choices (e.g. names of thresholding methods).  These are displayed as drop-down choice
     * menus in ParameterWindow
     */
    public final static int CHOICE_ARRAY = 5;

    /**
     * HashMap containing numeric values to be set in ParameterWindow.  ParameterWindow iterates through each of these
     * and displays it in its own numeric field
     */
    public final static int CHOICE_MAP = 6;

    /**
     * Boolean class parameter.  These are displayed by ParameterWindow as checkboxes.
     */
    public final static int BOOLEAN = 7;

    /**
     * Miscellaneous object class parameter.  These can be anything not fitting the other categories.  These can't be
     * set using ParameterWindow.
     */
    public final static int OBJECT = 8;

    final Object module;
    final String name;
    final int type;
    Object valueRange;
    Object value;
    boolean visible;


    // CONSTRUCTORS

    public Parameter(Object module, String name, int type, Object value, Object defaultValue, boolean visible) {
        this.module = module;
        this.type = type;
        this.name = name;
        this.value = value;
        this.valueRange = defaultValue;
        this.visible = visible;

    }

    public Parameter(Object module, String name, int type, Object value, boolean visible) {
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

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
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