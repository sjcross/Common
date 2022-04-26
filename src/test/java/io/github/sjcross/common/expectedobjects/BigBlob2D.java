package io.github.sjcross.common.expectedobjects;

import java.util.HashMap;
import java.util.List;

public class BigBlob2D extends ExpectedObjects {
    @Override
    public List<Integer[]> getCoordinates5D() {
        return getCoordinates5D("/coordinates/BigBinaryBlob_2D_whiteBG.csv");
    }

    @Override
    public boolean is2D() {
        return true;
    }

    @Override
    public HashMap<Integer, HashMap<String, Double>> getMeasurements() {
        return null;
    }
}
