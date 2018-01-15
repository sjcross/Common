package wbif.sjx.common.Process.HoughTransform;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

/**
 * Created by sc13967 on 13/01/2018.
 */
public class CircleAccumulator extends Accumulator {
    /**
     * Constructor for Accumulator object.
     *
     * @param parameterRanges 2D Integer array containing the dimensions over which the accumulator exists.
     */
    public CircleAccumulator(int[][] parameterRanges) {
        super(parameterRanges);
    }

    @Override
    public void addPoints(int[] parameters, double value, int[] x, int[] y) {
        for (int i = 0; i < x.length; i++) {
                int xx = parameters[0]+x[i]-parameterRanges[0][0];
                int yy = parameters[1]+y[i]-parameterRanges[1][0];
                int rr = parameters[2]-parameterRanges[2][0];

                int idx = indexer.getIndex(new int[]{xx,yy,rr});

                if (idx == -1) return;

                accumulator[idx] = value;

        }
    }

    public ImagePlus getAccumulatorAsImage() {
        int[] dim = indexer.getDim();
        ImagePlus ipl = IJ.createImage("Circle_Accumulator",dim[0],dim[1],dim[2],32);

        for (int idx=0;idx<indexer.getLength();idx++) {
            int[] coord = indexer.getCoord(idx);

            double value = accumulator[idx];

            ipl.setSlice(coord[2]);
            ipl.getProcessor().putPixelValue(coord[0],coord[1],value);

        }

        return ipl;

    }
}
