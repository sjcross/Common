package io.github.sjcross.sjcommon.expectedobjects;

import java.util.HashMap;
import java.util.List;

import io.github.sjcross.sjcommon.exceptions.IntegerOverflowException;
import io.github.sjcross.sjcommon.object.volume.Volume;

public class VerticalCylinderR5 extends ExpectedObjects {
    @Override
    public List<Integer[]> getCoordinates5D() {
        return getCoordinates5D("/coordinates/VerticalBinaryCylinder3D_R5_whiteBG_8bit.csv");
    }

    public Volume getObject(double dppXY, double dppZ, String units) throws IntegerOverflowException {
        return super.getObject(21,41,15, dppXY, dppZ, units);
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
