package wbif.sjx.common.Object;

/**
 * Created by sc13967 on 22/09/2017.
 */
public class Timepoint<T extends Number> extends Point<T> {
    protected int f;

    public Timepoint(T x, T y, T z, int f) {
        super(x, y, z);
        this.f = f;

    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

}
