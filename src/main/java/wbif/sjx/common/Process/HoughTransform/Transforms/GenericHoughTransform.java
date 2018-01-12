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
    private PixelArray pixels;
    private Accumulator accumulator;


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

    public abstract void run(int[][] parameterRanges);

    public static void main(String[] args) {
        new ImageJ();
        IJ.runMacro("waitForUser");

        ImagePlus ipl = IJ.getImage();
        CircleHoughTransform circleHoughTransform = new CircleHoughTransform(ipl.getProcessor());

        // Creating parameters
        int[][] parameters = new int[][]{{},{},{}};
        circleHoughTransform.run(parameters);


    }
}
