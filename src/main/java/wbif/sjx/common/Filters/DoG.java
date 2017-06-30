package wbif.sjx.common.Filters;

import ij.ImagePlus;
import ij.plugin.Duplicator;
import ij.plugin.ImageCalculator;
import ij.plugin.filter.GaussianBlur;
import ij.process.ImageConverter;

/**
 * Created by steph on 13/04/2017.
 */
public class DoG {
    public static void run(ImagePlus ipl, double sigma, boolean stack) {
        // Converting input image to 32-bit
        new ImageConverter(ipl).convertToGray32();

        // Duplicating the input ImagePlus
        ImagePlus ipl1 = new Duplicator().run(ipl);
        ImagePlus ipl2 = new Duplicator().run(ipl);

        if (stack) {
            // Performing the next couple of steps on all channels, slices and frames
            for (int channel = 0; channel < ipl.getNChannels(); channel++) {
                for (int slice = 0; slice < ipl.getNSlices(); slice++) {
                    for (int frame = 0; frame < ipl.getNFrames(); frame++) {
                        // Setting the current slice number
                        ipl1.setPosition(channel+1,slice+1,frame+1);
                        ipl2.setPosition(channel+1,slice+1,frame+1);

                        applyDoG(ipl1,ipl2,sigma);

                    }
                }
            }

        } else {
            applyDoG(ipl1,ipl2,sigma);

        }

        ipl1.setPosition(1,1,1);
        ipl2.setPosition(1,1,1);

        // Subtracting one from the other to give the final DoG result
        ipl.setImage(new ImageCalculator().run("Subtract create stack 32-bit",ipl2,ipl1));

    }

    private static void applyDoG(ImagePlus ipl1, ImagePlus ipl2, double sigma) {
        // Multiplying the intensities by 10, to give a smoother final result
        ipl1.getProcessor().multiply(10);
        ipl2.getProcessor().multiply(10);

        // Running Gaussian blur at two length scales.  One 1.6 times larger than the other (based on discussion at
        // http://dsp.stackexchange.com/questions/2529/what-is-the-relationship-between-the-sigma-in-the-laplacian-of-
        // gaussian-and-the (Accessed 14-03-2017)
        new GaussianBlur().blurGaussian(ipl1.getProcessor(), sigma, sigma, 0.01);
        new GaussianBlur().blurGaussian(ipl2.getProcessor(), sigma * 1.6, sigma * 1.6, 0.01);
    }
}
