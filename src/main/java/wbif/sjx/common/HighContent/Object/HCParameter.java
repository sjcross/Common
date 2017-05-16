package wbif.sjx.common.HighContent.Object;

import java.util.HashMap;

/**
 * Created by sc13967 on 03/05/2017.
 */
public class HCParameter {
    /**
     * Class only used for module titles.  These are displayed by ParameterWindow, but don't have anything to set
     */
    public final static int MODULE_TITLE = 0;

    /**
     * Name of Image class objects input to the module.  Used to connect images to be analysed between classes.  Input
     * images have been created by previous modules.
     */
    public final static int INPUT_IMAGE = 1;

    /**
     * Name of Image class objects output by the module.  Used to connect images to be analysed between classes.  Output
     * images are created by this module.
     */
    public final static int OUTPUT_IMAGE = 2;

    /**
     * Name of HCObject class objects input to the module.  Used to connect HCObjects to be analysed between classes.
     * Input HCObjects have been created by previous modules.
     */
    public final static int INPUT_OBJECTS = 3;

    /**
     * Name of HCObject class objects output by the module.  Used to connect HCObjects to be analysed between classes.
     * Output HCObjects are created by this module.
     */
    public final static int OUTPUT_OBJECTS = 4;

    /**
     * Single integer variable.  These can be set in ParameterWindow by numeric fields
     */
    public final static int INTEGER = 5;

    /**
     * Single double variable.  These can be set in ParameterWindow by numeric fields
     */
    public final static int DOUBLE = 6;

    /**
     * Single string variable.  These can be set in ParameterWindow by string fields
     */
    public final static int STRING = 7;

    /**
     * String array containing choices (e.g. names of thresholding methods).  These are displayed as drop-down choice
     * menus in ParameterWindow
     */
    public final static int CHOICE_ARRAY = 8;

    /**
     * HashMap containing numeric values to be set in ParameterWindow.  ParameterWindow iterates through each of these
     * and displays it in its own numeric field
     */
    public final static int CHOICE_MAP = 9;

    /**
     * Boolean class parameter.  These are displayed by ParameterWindow as checkboxes.
     */
    public final static int BOOLEAN = 10;

    /**
     * System file parameter.  These are displayed as buttons for loading file open dialog.  This is stored as an
     * absolute path String.
     */
    public final static int FILE_PATH = 11;

    /**
     * Miscellaneous object class parameter.  These can be anything not fitting the other categories.  These can't be
     * set using ParameterWindow.
     */
    public final static int OBJECT = 12;

    final Object module;
    final String name;
    final int type;
    Object valueRange;
    Object value;
    boolean visible;


    // CONSTRUCTORS

    public HCParameter(Object module, String name, int type, Object value, Object defaultValue, boolean visible) {
        this.module = module;
        this.type = type;
        this.name = name;
        this.value = value;
        this.valueRange = defaultValue;
        this.visible = visible;

    }

    public HCParameter(Object module, String name, int type, Object value, boolean visible) {
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

    public <T> T getValue() {
        return (T) value;

    }

//    public Object getValue() {
//        return value;
//    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        if (type == MODULE_TITLE) {
            return (String) value;

        } else if (type == INPUT_IMAGE | type == OUTPUT_IMAGE) {
            System.out.println("Value: "+value);
            System.out.println("Value.toString(): "+value.toString());
            return value.toString();

        } else if (type == INPUT_OBJECTS | type == OUTPUT_OBJECTS) {
            return value.toString();

        } else if (type == INTEGER) {
            return String.valueOf(value);

        } else if (type == DOUBLE) {
            return String.valueOf(value);

        } else if (type == STRING) {
            return (String) value;

        } else if (type == CHOICE_ARRAY) {
            return (String) value;

        } else if (type == CHOICE_MAP) {
            HashMap<String,Double> vals = (HashMap<String, Double>) value;

            StringBuilder stringBuilder = new StringBuilder();
            for (String key:vals.keySet()) {
                stringBuilder.append("(");
                stringBuilder.append(key);
                stringBuilder.append("/");
                stringBuilder.append(vals.get(key));
                stringBuilder.append(")");

            }

            return stringBuilder.toString();

        } else if (type == BOOLEAN) {
            return String.valueOf(value);

        } else if (type == OBJECT) {
            return value.getClass().getName();

        }

        return "";

    }
}