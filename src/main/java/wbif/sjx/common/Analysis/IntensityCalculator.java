package wbif.sjx.common.Analysis;

import ij.ImagePlus;
import ij.ImageStack;
import wbif.sjx.common.MathFunc.CumStat;
import wbif.sjx.common.Object.Point;
import wbif.sjx.common.Object.Volume;

/**
 * Created by sc13967 on 12/05/2017.
 */
public class IntensityCalculator {

    // ANALYSIS OVER A SPECIFIED VOLUME

    public static CumStat calculate(ImageStack image, Volume volume) {
        // Initialising the pixel value store
        CumStat cs = new CumStat();

        calculate(image,cs,volume);

        return cs;

    }

    public static void calculate(ImageStack image, CumStat cs, Volume volume) {
        // Running through all pixels in the volume, adding them to the CumStat object
        for (Point<Integer> point:volume.getPoints()) {
            cs.addMeasure(image.getProcessor(point.getZ()+1).getPixelValue(point.getX(), point.getY()));
        }
    }


    // ANALYSIS OVER THE ENTIRE IMAGE

    public static CumStat calculate(ImageStack image) {
        // Initialising the pixel value store
        CumStat cs = new CumStat();

        calculate(image,cs);

        return cs;

    }

    public static void calculate(ImageStack image, CumStat cs) {
        // Running through all pixels in the image, adding them to the CumStat object
        for (int z = 0; z < image.size(); z++) {
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    cs.addMeasure(image.getVoxel(x,y,z));
                }
            }
        }
    }

    @Deprecated
    public static CumStat calculate(ImagePlus image) {
        // Initialising the pixel value store
        CumStat cs = new CumStat();

        calculate(image,cs);

        return cs;

    }

    @Deprecated
    public static void calculate(ImagePlus image, CumStat cs) {
        // Running through all pixels in the image, adding them to the CumStat object
        for (int t = 0; t < image.getNFrames(); t++) {
            for (int z = 0; z < image.getNSlices(); z++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    for (int x = 0; x < image.getWidth(); x++) {
                        image.setPosition(1, z + 1, t + 1);
                        cs.addMeasure(image.getProcessor().getPixelValue(x, y));
                    }
                }
            }
        }
    }
}
