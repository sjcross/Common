package wbif.sjx.common.Analysis;

import ij.ImageStack;
import wbif.sjx.common.Object.Point;
import wbif.sjx.common.Object.Volume2.Volume2;

public class ColocalisationCalculator {
    public static double calculatePCC(ImageStack image1, ImageStack image2, Volume2 volume) {
        // Getting the mean intensities for the two channels
        double meanI1 = IntensityCalculator.calculate(image1,volume).getMean();
        double meanI2 = IntensityCalculator.calculate(image2,volume).getMean();

        double numerator = 0;
        double denominator1 = 0;
        double denominator2 = 0;

        // Running through all pixels in the volume, adding them to the CumStat object
        for (Point<Integer> point:volume.getPoints()) {
            int x = point.getX();
            int y = point.getY();
            int z = point.getZ();

            double i1 = image1.getVoxel(x,y,z);
            double i2 = image2.getVoxel(x,y,z);

            numerator = numerator + (i1-meanI1)*(i2-meanI2);
            denominator1 = denominator1 + (i1-meanI1)*(i1-meanI1);
            denominator2 = denominator2 + (i2-meanI2)*(i2-meanI2);

        }

        return numerator/Math.sqrt(denominator1*denominator2);

    }

    /**
     * Measures colocalisation for image1 and image2 for pixels where mask has a value of 0.  This is consistent with
     * the default ImageJ convention of black objects on a white background (however, it is opposite to masking in
     * Coloc2).
     * @param image1
     * @param image2
     * @param mask
     * @return
     */
    public static double calculatePCC(ImageStack image1, ImageStack image2, ImageStack mask) {
        // Getting the mean intensities for the two channels
        double meanI1 = IntensityCalculator.calculate(image1,mask).getMean();
        double meanI2 = IntensityCalculator.calculate(image2,mask).getMean();

        double numerator = 0;
        double denominator1 = 0;
        double denominator2 = 0;

        // Running through all pixels in the volume, adding them to the CumStat object
        for (int z = 0; z < image1.size(); z++) {
            for (int y = 0; y < image1.getHeight(); y++) {
                for (int x = 0; x < image1.getWidth(); x++) {
                    if (mask.getVoxel(x,y,z) == 0) {
                        double i1 = image1.getVoxel(x, y, z);
                        double i2 = image2.getVoxel(x, y, z);

                        numerator = numerator + (i1 - meanI1) * (i2 - meanI2);
                        denominator1 = denominator1 + (i1 - meanI1) * (i1 - meanI1);
                        denominator2 = denominator2 + (i2 - meanI2) * (i2 - meanI2);
                    }
                }
            }
        }

        return numerator/Math.sqrt(denominator1*denominator2);

    }

    public static double calculatePCC(ImageStack image1, ImageStack image2) {
        // Getting the mean intensities for the two channels
        double meanI1 = IntensityCalculator.calculate(image1).getMean();
        double meanI2 = IntensityCalculator.calculate(image2).getMean();

        double numerator = 0;
        double denominator1 = 0;
        double denominator2 = 0;

        // Running through all pixels in the volume, adding them to the CumStat object
        for (int z = 0; z < image1.size(); z++) {
            for (int y = 0; y < image1.getHeight(); y++) {
                for (int x = 0; x < image1.getWidth(); x++) {

                    double i1 = image1.getVoxel(x, y, z);
                    double i2 = image2.getVoxel(x, y, z);

                    numerator = numerator + (i1 - meanI1) * (i2 - meanI2);
                    denominator1 = denominator1 + (i1 - meanI1) * (i1 - meanI1);
                    denominator2 = denominator2 + (i2 - meanI2) * (i2 - meanI2);

                }
            }
        }

        return numerator / Math.sqrt(denominator1 * denominator2);

    }
}
