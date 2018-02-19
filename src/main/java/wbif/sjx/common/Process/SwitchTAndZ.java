package wbif.sjx.common.Process;

import fiji.stacks.Hyperstack_rearranger;
import ij.ImagePlus;

/**
 * Created by steph on 15/04/2017.
 */
@Deprecated
public class SwitchTAndZ {
    public static void run(ImagePlus ipl) {
        ipl.setStack(Hyperstack_rearranger.reorderHyperstack(ipl,"CTZ",true,false).getStack());

    }
}
