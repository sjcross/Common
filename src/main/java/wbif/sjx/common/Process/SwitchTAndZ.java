package wbif.sjx.common.Process;

import ij.ImagePlus;

/**
 * Created by steph on 15/04/2017.
 */
public class SwitchTAndZ {
    public static void run(ImagePlus ipl) {
        int[] dimensions = ipl.getDimensions();
        ipl.setDimensions(dimensions[2],dimensions[4],dimensions[3]);

    }
}
