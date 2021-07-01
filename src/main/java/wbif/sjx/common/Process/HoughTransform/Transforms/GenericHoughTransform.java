package wbif.sjx.common.Process.HoughTransform.Transforms;

import java.util.ArrayList;

import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ImageProcessor;
import wbif.sjx.common.Process.HoughTransform.Accumulators.Accumulator;
import wbif.sjx.common.Process.HoughTransform.PixelArrays.BytePixelArray;
import wbif.sjx.common.Process.HoughTransform.PixelArrays.FloatPixelArray;
import wbif.sjx.common.Process.HoughTransform.PixelArrays.PixelArray;
import wbif.sjx.common.Process.HoughTransform.PixelArrays.ShortPixelArray;

/**
 * Created by sc13967 on 12/01/2018.
 */
public abstract class GenericHoughTransform {
    protected PixelArray pixels;
    protected Accumulator accumulator;
    int nThreads = 1;

    public GenericHoughTransform(ImageProcessor ipr) {
        // Getting dimensions of the input image
        int[] dims = new int[] { ipr.getWidth(), ipr.getHeight() };

        // Converting the image to a 1D pixel array
        switch (ipr.getBitDepth()) {
            case 8:
                pixels = new BytePixelArray((byte[]) ipr.getPixels(), dims);
                break;

            case 16:
                pixels = new ShortPixelArray((short[]) ipr.getPixels(), dims);
                break;

            case 32:
                pixels = new FloatPixelArray((float[]) ipr.getPixels(), dims);
                break;
        }
    }

    public GenericHoughTransform(ImageStack ist) {
        // Getting dimensions of the input image
        int[] dims = new int[] { ist.getWidth(), ist.getHeight(), ist.size() };

        // Converting the image to a 1D pixel array
        int i = 0;
        switch (ist.getBitDepth()) {
            case 8:
                byte[] bytePixels = new byte[ist.getWidth() * ist.getHeight() * ist.size()];
                for (int z = 1; z <= ist.size(); z++)
                    for (byte pixel : (byte[]) ist.getPixels(z))
                        bytePixels[i++] = pixel;
                pixels = new BytePixelArray(bytePixels, dims);
                break;

            case 16:
                short[] shortPixels = new short[ist.getWidth() * ist.getHeight() * ist.size()];
                for (int z = 1; z <= ist.size(); z++)
                    for (short pixel : (short[]) ist.getPixels(z))
                        shortPixels[i++] = pixel;
                pixels = new ShortPixelArray(shortPixels, dims);
                break;

            case 32:
                float[] floatPixels = new float[ist.getWidth() * ist.getHeight() * ist.size()];
                for (int z = 1; z <= ist.size(); z++)
                    for (float pixel : (byte[]) ist.getPixels(z))
                        floatPixels[i++] = pixel;
                pixels = new FloatPixelArray(floatPixels, dims);
                break;
        }
    }

    /**
     * Performs the Hough Transform on the image passed to the constructor. This
     * should be an edge image.
     */
    public abstract void run();

    public void addDetectedObjectsOverlay(ImagePlus ipl, ArrayList<double[]> objects) {
        accumulator.addDetectedObjectsOverlay(ipl, objects);

    }

    /**
     * Normalises scores to the number of points that contribute to that position.
     */
    public void normaliseScores() {
        accumulator.normaliseScores();
    }

    /**
     * Iterates over all pixels in the Accumulator and stores the pixel (parameter
     * set) with the highest score. All pixels proximal to this (within
     * {@param exclusionR}) have their score set to zero. This prevents
     * identification of multiple objects very close to each other. Currently this
     * can't return multiple overlapping objects with different radii (i.e.
     * concentric circles).
     * 
     * @param minScore
     * @param exclusionR
     * @return
     */
    public ArrayList<double[]> getObjects(double minScore, int exclusionR) {
        return accumulator.getObjects(minScore, exclusionR);
    }

    public ImagePlus getAccumulatorAsImage() {
        return accumulator.getAccumulatorAsImage();

    }

    public int getnThreads() {
        return nThreads;
    }

    public void setnThreads(int nThreads) {
        this.nThreads = nThreads;
    }
}
