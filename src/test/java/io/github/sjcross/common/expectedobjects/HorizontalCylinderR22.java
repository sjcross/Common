package io.github.sjcross.common.expectedobjects;

import io.github.sjcross.common.exceptions.IntegerOverflowException;
import io.github.sjcross.common.object.volume.Volume;

import java.util.HashMap;
import java.util.List;

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
