package wbif.sjx.common.ExpectedObjects;

import java.util.HashMap;
import java.util.List;

public class Blobs2D extends ExpectedObjects {
    @Override
    public List<Integer[]> getCoordinates5D() {
        return getCoordinates5D("/coordinates/Blobs_2D_whiteBG_8bit.csv");
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
