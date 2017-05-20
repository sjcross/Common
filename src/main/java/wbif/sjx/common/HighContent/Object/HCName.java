package wbif.sjx.common.HighContent.Object;

/**
 * Created by sc13967 on 19/05/2017.
 */
public class HCName {
    private String name = "";

    // CONSTRUCTOR

    public HCName(String name) {
        this.name = name;

    }


    // GETTERS AND SETTERS

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return name;

    }
}
