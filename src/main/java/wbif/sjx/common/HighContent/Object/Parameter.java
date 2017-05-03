package wbif.sjx.common.HighContent.Object;

/**
 * Created by sc13967 on 03/05/2017.
 */
public class Parameter {
    String name;
    Object module;
    Object value;
    boolean visible;

    Parameter(Object module, String name, Object value, boolean visible) {
        this.module = module;
        this.value = value;
        this.visible = visible;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getModule() {
        return module;
    }

    public void setModule(Object module) {
        this.module = module;
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
