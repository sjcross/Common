package wbif.sjx.common.Process.HoughTransform.Transforms;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import wbif.sjx.common.Process.HoughTransform.Accumulators.Accumulator;
import wbif.sjx.common.Process.HoughTransform.PixelArrays.BytePixelArray;
import wbif.sjx.common.Process.HoughTransform.PixelArrays.FloatPixelArray;
import wbif.sjx.common.Process.HoughTransform.PixelArrays.PixelArray;
import wbif.sjx.common.Process.HoughTransform.PixelArrays.ShortPixelArray;

import java.util.ArrayList;

/**
 * Created by sc13967 on 12/01/2018.
 */
public abstract class GenericHoughTransform {
    protected PixelArray pixels;
    protected Accumulator accumulator;
    protected boolean randomSampling = false;
    protected double sampleFraction = 0.5;


    public GenericHoughTransform(ImageProcessor ipr) {
        // Getting dimensions of the input image
        int[] dims = new int[]{ipr.getWidth(),ipr.getHeight()};

        // Converting the image to a 1D pixel array
        switch (ipr.getBitDepth()) {
            case 8:
                pixels = new BytePixelArray((byte[]) ipr.getPixels(),dims);
                break;

            case 16:
                pixels = new ShortPixelArray((short[]) ipr.getPixels(),dims);
                break;

            case 32:
                pixels = new FloatPixelArray((float[]) ipr.getPixels(),dims);
                break;
        }
    }

    /**
     * Performs the Hough Transform on the image passed to the constructor.  This should be an edge image.
     */
    public abstract void run();

    public void addDetectedObjectsOverlay(ImagePlus ipl, ArrayList<double[]> objects) {
        accumulator.addDetectedObjectsOverlay(ipl,objects);

    }

    /**
     * Normalises scores to the number of points that contribute to that position.
     */
    public void normaliseScores() {
        accumulator.normaliseScores();

    }

    /**
     * Iterates over all pixels in the Accumulator and stores the pixel (parameter set) with the highest score.
     * All pixels proximal to this (within {@param exclusionR}) have their score set to zero.
     * This prevents identification of multiple objects very close to each other.  Currently this can't return multiple
     * overlapping objects with different radii (i.e. concentric circles).
     * @param minScore
     * @param exclusionR
     * @return
     */
    public ArrayList<double[]> getObjects(double minScore, int exclusionR) {
        return accumulator.getObjects(minScore,exclusionR);

    }

    public ImagePlus getAccumulatorAsImage() {
        return accumulator.getAccumulatorAsImage();

    }

    public boolean isRandomSampling() {
        return randomSampling;
    }

    public void setRandomSampling(boolean randomSampling) {
        this.randomSampling = randomSampling;
    }

    public double getSampleFraction() {
        return sampleFraction;
    }

    public void setSampleFraction(double sampleFraction) {
        this.sampleFraction = sampleFraction;
    }
}
