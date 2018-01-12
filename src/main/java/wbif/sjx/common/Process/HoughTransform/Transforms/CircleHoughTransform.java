package wbif.sjx.common.Process.HoughTransform.Transforms;

import ij.process.ImageProcessor;

/**
 * Created by sc13967 on 12/01/2018.
 */
public class CircleHoughTransform extends GenericHoughTransform {
    public CircleHoughTransform(ImageProcessor ipr) {
        super(ipr);

    }

    @Override
    public void run(int[][] parameterRanges) {
        // Parameters are x,y and R
    }
}
