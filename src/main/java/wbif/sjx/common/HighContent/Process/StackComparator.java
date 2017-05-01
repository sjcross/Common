package wbif.sjx.common.HighContent.Process;

import wbif.sjx.common.HighContent.Object.Result;

import java.util.Comparator;

/**
 * Created by steph on 01/05/2017.
 */
public class StackComparator implements Comparator<Result> {
    @Override
    public int compare(Result result1, Result result2) {
        int z1 = result1.getZ();
        int z2 = result2.getZ();

        return z1 < z2 ? -1 : z1 == z2 ? 0 : 1;

    }
}
