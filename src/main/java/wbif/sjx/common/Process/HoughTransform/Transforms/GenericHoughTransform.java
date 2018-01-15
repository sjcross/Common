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

    public static void main(String[] args) {
        // Loading test image
        new ImageJ();
        ImagePlus ipl = IJ.openImage("C:\\Users\\sc13967\\Desktop\\1000px-Data_Setup-Gradient.tif");

        // Initialising the Hough transform
        int[][] parameterRanges =
                new int[][]{{0,ipl.getWidth()-1},{0,ipl.getHeight()-1},{30,70}};
        CircleHoughTransform circleHoughTransform = new CircleHoughTransform(ipl.getProcessor(),parameterRanges);

        // Running the transforms
        circleHoughTransform.run();

        // Normalising scores based on the number of points in that circle
        circleHoughTransform.normaliseScores();

        // Getting the accumulator as an image
        ImagePlus iplHough = circleHoughTransform.getAccumulatorAsImage();
        iplHough.show();

        // Getting brightest points.
        ArrayList<double[]> circles = circleHoughTransform.getObjects(35,50);

        // Adding an overlay to the input image showing the detected circles
        circleHoughTransform.addDetectedObjectsOverlay(ipl,circles);
        ipl.show();

    }
}
