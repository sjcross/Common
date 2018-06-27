package wbif.sjx.common.ExpectedObjects;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sc13967 on 21/02/2018.
 */
public class Objects2D extends ExpectedObjects {
    public enum Measures {
        PCC,ASM
    }

    @Override
    public List<Integer[]> getCoordinates5D() {
        return getCoordinates5D("/coordinates/Objects2D.csv");
    }

    @Override
    public boolean is2D() {
        return true;
    }

    @Override
    public HashMap<Integer,HashMap<String,Double>> getMeasurements() {
        HashMap<Integer,HashMap<String,Double>> expectedValues = new HashMap<>();

        HashMap<String,Double> obj = new HashMap<>();
        obj.put(Measures.PCC.name(),0.4d);
        obj.put(Measures.ASM.name(),0.004823023);
        expectedValues.put(3,obj);

        obj = new HashMap<>();
        obj.put(Measures.PCC.name(),Double.NaN); // Can't calculate from a single value object
        obj.put(Measures.ASM.name(),0d);
        expectedValues.put(4,obj);

        obj = new HashMap<>();
        obj.put(Measures.PCC.name(),0.43d);
        expectedValues.put(7,obj);

        obj = new HashMap<>();
        obj.put(Measures.PCC.name(),0.04d);
        expectedValues.put(8,obj);

        obj = new HashMap<>();
        obj.put(Measures.PCC.name(),Double.NaN);
        expectedValues.put(9,obj);

        obj = new HashMap<>();
        obj.put(Measures.PCC.name(),-0.25d);
        expectedValues.put(13,obj);

        obj = new HashMap<>();
        obj.put(Measures.PCC.name(),0.55d);
        expectedValues.put(20,obj);

        obj = new HashMap<>();
        obj.put(Measures.PCC.name(),Double.NaN);
        expectedValues.put(23,obj);

        return expectedValues;

    }
}
