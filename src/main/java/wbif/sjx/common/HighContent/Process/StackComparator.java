package wbif.sjx.common.HighContent.Process;

import wbif.sjx.common.HighContent.Object.Result;

import java.util.Comparator;

/**
 * Created by steph on 01/05/2017.
 */
public class StackComparator implements Comparator<Result> {
    private String field = Result.ZPOSITION;


    // PUBLIC METHODS

    @Override
    public int compare(Result result1, Result result2) {
        double z1 = Double.parseDouble(result1.getAsString(field));
        double z2 = Double.parseDouble(result2.getAsString(field));

        return z1 < z2 ? -1 : z1 == z2 ? 0 : 1;

    }


    // GETTERS AND SETTERS

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
