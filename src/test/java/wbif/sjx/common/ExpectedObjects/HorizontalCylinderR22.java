package wbif.sjx.common.ExpectedObjects;

import wbif.sjx.common.Exceptions.IntegerOverflowException;
import wbif.sjx.common.Object.Volume.Volume;

import java.util.HashMap;
import java.util.List;

public class HorizontalCylinderR22 extends ExpectedObjects {
    @Override
    public List<Integer[]> getCoordinates5D() {
        return getCoordinates5D("/coordinates/HorizontalBinaryCylinder3D_R22_whiteBG_8bit.csv");
    }

    public Volume getObject(double dppXY, double dppZ, String calibratedUnits) throws IntegerOverflowException {
        return super.getObject(51,72,10, dppXY, dppZ, calibratedUnits);
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
