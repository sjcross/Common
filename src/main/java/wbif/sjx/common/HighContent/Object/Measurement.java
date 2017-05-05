package wbif.sjx.common.HighContent.Object;

/**
 * Created by sc13967 on 05/05/2017.
 */
public class Measurement {
    private String name;
    private double value;
    private Object source;


    // CONSTRUCTOR

    public Measurement(String name, double value) {
        this.name = name;
        this.value = value;

    }


    // GETTERS AND SETTERS

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
}
