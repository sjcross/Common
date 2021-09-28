package io.github.sjcross.common.filters;

import ij.ImagePlus;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import io.github.sjcross.common.mathfunc.Indexer;

/**
 * Created by sc13967 on 17/01/2018.
 */
public class RidgeEnhancement {
    public static void run(ImagePlus ipl, float sigma, boolean stack) {
        if (stack) {
            // Iterating over all images in the Hyperstack
            for (int channel = 0; channel < ipl.getNChannels(); channel++) {
                for (int slice = 0; slice < ipl.getNSlices(); slice++) {
                    for (int frame = 0; frame < ipl.getNFrames(); frame++) {
                        ipl.setPosition(channel+1,slice+1,frame+1);

                        FloatProcessor enhancedProcessor = runOnProcessor(ipl.getProcessor(), sigma);
                        ipl.setProcessor(enhancedProcessor);
                    }
                }
            }

        } else {
            // Running on the current image
            FloatProcessor enhancedProcessor = runOnProcessor(ipl.getProcessor(), sigma);
            ipl.setProcessor(enhancedProcessor);

        }
    }

    public static FloatProcessor runOnProcessor(ImageProcessor ipr, float sigma) {
        // Generating 1D zero-order and second-order Gaussian distributions
        float[] gauss0 = getGaussian(sigma,0);
        float[] gauss2 = getGaussian(sigma,2);

        // Creating the kernels
        Indexer indexer = new Indexer(gauss0.length,gauss0.length);
        float[] xKernel = new float[gauss0.length*gauss0.length];
        float[] yKernel = new float[gauss0.length*gauss0.length];

        for (int i=0;i<gauss0.length;i++) {
            for (int j=0;j<gauss0.length;j++) {
                int idx = indexer.getIndex(new int[]{i,j});
                xKernel[idx] = gauss0[j]*gauss2[i];
                yKernel[idx] = gauss0[i]*gauss2[j];
            }
        }

        // Applying the convolution in x and y
        FloatProcessor xProcessor = (FloatProcessor) ipr.convertToFloat();
        FloatProcessor yProcessor = (FloatProcessor) ipr.convertToFloat();
        xProcessor.convolve(xKernel,gauss0.length,gauss0.length);
        yProcessor.convolve(yKernel,gauss0.length,gauss0.length);

        // Multiplying the values from the convolutions into a single FloatProcessor
        float[] xPixels = (float[]) xProcessor.getPixels();
        float[] yPixels = (float[]) yProcessor.getPixels();
        float[] xyPixels = new float[xPixels.length];

        for (int i=0;i<xyPixels.length;i++) {
            xyPixels[i] = xPixels[i]+yPixels[i];
        }

        // Applying the new pixel values as a FloatProcessor
        return new FloatProcessor(xProcessor.getWidth(),xProcessor.getHeight(),xyPixels);

    }

    public static float[] getGaussian(float sigma, int order) {
        int nElements = (int) Math.round(sigma)*6+1;
        float[] gauss = new float[nElements];

        float a = 1/(sigma* (float) Math.sqrt(2*Math.PI));
        float b = 1/(2*sigma*sigma);

        for (int i=0;i<nElements;i++) {
            int x = i - (int) Math.round(sigma)*3;

            switch (order) {
                case 0:
                    gauss[i] = a*(float) Math.exp(-x*x*b);
                    break;

                case 1:
                    gauss[i] = ((-x*a)/(sigma*sigma))*(float) Math.exp(-x*x*b);
                    break;

                case 2:
                    gauss[i] = ((x*x-sigma*sigma)*a/(sigma*sigma*sigma*sigma))*(float) Math.exp(-x*x*b);
                    break;
            }
        }

        return gauss;

    }
}