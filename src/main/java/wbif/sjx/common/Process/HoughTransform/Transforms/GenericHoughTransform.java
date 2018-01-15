package wbif.sjx.common.Process.HoughTransform.Transforms;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import wbif.sjx.common.MathFunc.Indexer;
import wbif.sjx.common.Process.HoughTransform.Accumulator;
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

    public ImagePlus getAccumulatorAsImage() {
        return accumulator.getAccumulatorAsImage();
    }

    /**
     * Performs the Hough Transform on the image passed to the constructor.  This should be an edge image.
     */
    public abstract void run();

    public abstract void setAccumulator(int[][] parameterRanges);

    public static void main(String[] args) {
        // Loading test image
        new ImageJ();
        ImagePlus ipl = IJ.openImage("C:\\Users\\sc13967\\Local Documents\\HoughTest.tif");

        // Initialising the Hough transform
        CircleHoughTransform circleHoughTransform = new CircleHoughTransform(ipl.getProcessor());

        // Creating the Accumulator
        int[][] parameterRanges =
                new int[][]{{0,ipl.getWidth()-1,1},{0,ipl.getHeight()-1,1},{33,33,1}};
        circleHoughTransform.setAccumulator(parameterRanges);

        // Running the transforms
        circleHoughTransform.run();

        // Getting the accumulator as an image
        ImagePlus iplHough = circleHoughTransform.getAccumulatorAsImage();
        iplHough.show();

    }
}
