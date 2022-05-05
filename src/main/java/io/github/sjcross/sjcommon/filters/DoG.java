package io.github.sjcross.sjcommon.filters;

import ij.ImagePlus;
import ij.plugin.filter.GaussianBlur;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

/**
 * Created by Stephen on 13/04/2017.
 */
public class DoG {
    public static void run(ImagePlus ipl, double sigma, boolean stack) {
        // Converting input image to 32-bit
        new ImageConverter(ipl).convertToGray32();

        ImageProcessor ipr1 = null;
        ImageProcessor ipr2 = null;

        if (stack) {
            // Performing the next couple of steps on all channels, slices and frames
            for (int channel = 0; channel < ipl.getNChannels(); channel++) {
                for (int slice = 0; slice < ipl.getNSlices(); slice++) {
                    for (int frame = 0; frame < ipl.getNFrames(); frame++) {
                        // Setting the current slice number
                        ipl.setPosition(channel+1,slice+1,frame+1);

                        ipr1 = ipl.getProcessor();
                        ipr2 = ipr1.duplicate();

                        ipl.setProcessor(applyDoG(ipr1,ipr2,sigma));

                    }
                }
            }

        } else {
            ipr1 = ipl.getProcessor();
            ipr2 = ipr1.duplicate();

            ipl.setProcessor(applyDoG(ipr1,ipr2,sigma));

        }
    }

    private static ImageProcessor applyDoG(ImageProcessor ipr1, ImageProcessor ipr2, double sigma) {
        // Multiplying the intensities by 10, to give a smoother final result
        ipr1.multiply(10);
        ipr2.multiply(10);

        // Running Gaussian blur at two length scales.  One 1.6 times larger than the other (based on discussion at
        // http://dsp.stackexchange.com/questions/2529/what-is-the-relationship-between-the-sigma-in-the-laplacian-of-
        // gaussian-and-the (Accessed 14-03-2017)
        new GaussianBlur().blurGaussian(ipr1, sigma, sigma, 0.01);
        new GaussianBlur().blurGaussian(ipr2, sigma * 1.6, sigma * 1.6, 0.01);

        ImageProcessor iprOut = ipr1.duplicate();
        for (int x=0;x<ipr1.getWidth();x++) {
            for (int y=0;y<ipr1.getHeight();y++) {
                float val = ipr2.getf(x,y)-ipr1.getf(x,y);
                iprOut.setf(x,y,val);
            }
        }

        return iprOut;

    }
}
