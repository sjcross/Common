package wbif.sjx.common.Process;

import java.util.TreeMap;

/**
 * Created by sc13967 on 02/08/2017.
 */
@Deprecated
public class DoubleTreeMapConverter {
    public static TreeMap<Integer,Double> convert(int[] keys, double[] values) {
        TreeMap<Integer,Double> treeMap = new TreeMap<>();

        for (int i=0;i<keys.length;i++) {
            treeMap.put(keys[i],values[i]);

        }

        return treeMap;

    }
}
