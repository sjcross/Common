package io.github.sjcross.common.ExpectedObjects;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Stephen Cross on 29/08/2017.
 */
public class Objects3D extends ExpectedObjects {
    public enum Measures {
        PCC
    }

    @Override
    public HashMap<Integer,HashMap<String,Double>> getMeasurements() {
        HashMap<Integer,HashMap<String,Double>> expectedValues = new HashMap<>();

        HashMap<String,Double> obj = new HashMap<>();
        obj.put(Measures.PCC.name(),3d);
        expectedValues.put(3,obj);

        obj = new HashMap<>();
        obj.put(Measures.PCC.name(),3d);
        expectedValues.put(4,obj);

        obj = new HashMap<>();
        obj.put(Measures.PCC.name(),3d);
        expectedValues.put(7,obj);

        obj = new HashMap<>();
        obj.put(Measures.PCC.name(),3d);
        expectedValues.put(8,obj);

        obj = new HashMap<>();
        obj.put(Measures.PCC.name(),3d);
        expectedValues.put(9,obj);

        obj = new HashMap<>();
        obj.put(Measures.PCC.name(),3d);
        expectedValues.put(13,obj);

        obj = new HashMap<>();
        obj.put(Measures.PCC.name(),3d);
        expectedValues.put(20,obj);

        obj = new HashMap<>();
        obj.put(Measures.PCC.name(),3d);
        expectedValues.put(23,obj);

        return expectedValues;

    }

    @Override
    public List<Integer[]> getCoordinates5D() {
        return getCoordinates5D("/coordinates/Objects3D.csv");
    }

    @Override
    public boolean is2D() {
        return false;
    }
}

