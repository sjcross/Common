package wbif.sjx.common.Process;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageStatistics;

/**
 * Created by steph on 15/04/2017.
 */
public class IntensityMinMax {
    public static void run(ImagePlus ipl, boolean stack) {
        // Getting min and max values from stack
        double min = Double.MAX_VALUE;
        double max = 0;

        if (stack) {
            for (int channel = 0; channel < ipl.getNChannels(); channel++) {
                for (int slice = 0; slice < ipl.getNSlices(); slice++) {
                    for (int frame = 0; frame < ipl.getNFrames(); frame++) {
                        ipl.setPosition(channel+1,slice+1,frame+1);

                        ImageStatistics stats = ImageStatistics.getStatistics(ipl.getProcessor(), ImageStatistics.MIN_MAX, null);

                        if (stats.min < min) {
                            min = stats.min;
                        }

                        if (stats.max > max) {
                            max = stats.max;
                        }
                    }
                }
            }

        } else {
            ImageStatistics stats = ImageStatistics.getStatistics(ipl.getProcessor(), ImageStatistics.MIN_MAX, null);

            min = stats.min;
            max = stats.max;

        }

        IJ.setMinAndMax(ipl,min,max);

    }
}
