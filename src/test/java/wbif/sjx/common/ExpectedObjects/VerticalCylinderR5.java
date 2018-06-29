package wbif.sjx.common.ExpectedObjects;

import java.util.HashMap;
import java.util.List;

public class VerticalCylinderR5 extends ExpectedObjects {
    @Override
    public List<Integer[]> getCoordinates5D() {
        return getCoordinates5D("/coordinates/VerticalBinaryCylinder3D_R5_whiteBG_8bit.csv");
    }

    @Override
    public boolean is2D() {
        return false;
    }

    @Override
    public HashMap<Integer, HashMap<String, Double>> getMeasurements() {
        return null;
    }
}
