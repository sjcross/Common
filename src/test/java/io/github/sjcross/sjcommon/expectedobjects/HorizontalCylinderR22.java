package io.github.sjcross.sjcommon.expectedobjects;

import java.util.HashMap;
import java.util.List;

import io.github.sjcross.sjcommon.exceptions.IntegerOverflowException;
import io.github.sjcross.sjcommon.object.volume.Volume;

public class HorizontalCylinderR22 extends ExpectedObjects {
    @Override
    public List<Integer[]> getCoordinates5D() {
        return getCoordinates5D("/coordinates/HorizontalBinaryCylinder3D_R22_whiteBG_8bit.csv");
    }

    public Volume getObject(double dppXY, double dppZ, String units) throws IntegerOverflowException {
        return super.getObject(51,72,10, dppXY, dppZ, units);
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
