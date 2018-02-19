package wbif.sjx.common.Analysis;

import ij.ImagePlus;
import wbif.sjx.common.MathFunc.CumStat;

/**
 * Created by sc13967 on 12/05/2017.
 */
public class IntensityCalculator {
    public static CumStat calculate(ImagePlus inputImagePlus, CumStat cs) {
        // Running through all pixels in the image, adding them to the CumStat object
        for (int t = 0; t < inputImagePlus.getNFrames(); t++) {
            for (int z = 0; z < inputImagePlus.getNSlices(); z++) {
                for (int y = 0; y < inputImagePlus.getHeight(); y++) {
                    for (int x = 0; x < inputImagePlus.getWidth(); x++) {
                        inputImagePlus.setPosition(1, z + 1, t + 1);
                        cs.addMeasure(inputImagePlus.getProcessor().getPixelValue(x, y));
                    }
                }
            }
        }

        return cs;

    }

    public static CumStat calculate(ImagePlus inputImagePlus) {
        // Initialising the pixel value store
        CumStat cs = new CumStat();

        return calculate(inputImagePlus,cs);

    }
}
