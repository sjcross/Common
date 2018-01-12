package wbif.sjx.common.Process.HoughTransform.PixelArrays;

import wbif.sjx.common.MathFunc.Indexer;

/**
 * Created by sc13967 on 12/01/2018.
 */
public abstract class PixelArray extends Indexer {
    private Object pixels;

    public PixelArray(int[] dim) {
        super(dim);
        this.pixels = pixels;

    }

    public abstract int length();
    public abstract double get(int i);

}
