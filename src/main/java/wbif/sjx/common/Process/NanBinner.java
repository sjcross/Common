package wbif.sjx.common.Process;

import wbif.sjx.common.MathFunc.CumStat;
import wbif.sjx.common.MathFunc.Indexer;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.process.ImageProcessor;
import org.apache.commons.math3.analysis.function.Ceil;
import org.apache.commons.math3.analysis.function.Floor;

/**
 * Created by steph on 21/08/2016.
 */
public class NanBinner {
    static final int AVERAGE = 0;
    static final int MAX = 1;
    static final int MIN = 2;
    static final int SUM = 3;

    public static ImagePlus shrink(ImagePlus ipl, int xshrink, int yshrink, int zshrink, int method) {
        //Getting image dimensions
        ImageProcessor ipr = ipl.getProcessor();
        int[] dims_in = new int[]{ipl.getWidth(), ipl.getHeight(), ipl.getNSlices()};
        int new_w = (int)(new Ceil()).value((double)ipl.getWidth() / (double)xshrink);
        int new_h = (int)(new Ceil()).value((double)ipl.getHeight() / (double)yshrink);
        int new_d = (int)(new Ceil()).value((double)ipl.getNSlices() / (double)zshrink);
        int[] dims_bin = new int[]{new_w, new_h, new_d};

        //Creating the binned image
        ImagePlus ipl_bin = NewImage.createFloatImage("BINNED", dims_bin[0], dims_bin[1], dims_bin[2], 1);
        ImageProcessor ipr_bin = ipl_bin.getProcessor();

        //Setting all values to NaN in binned image
        for(int z = 0; z < dims_bin[2]; ++z) {
            ipl_bin.setSlice(z + 1);
            for (int x = 0; x < dims_bin[0]; x++) {
                for (int y = 0; y < dims_bin[1]; y++) {
                    ipr_bin.putPixelValue(x, y, Float.NaN);
                }
            }
        }

        //Calculating average value at each pixel
        Indexer indexer = new Indexer(new int[]{dims_bin[0], dims_bin[1], dims_bin[2]});
        int n = dims_bin[0] * dims_bin[1] * dims_bin[2];
        CumStat cs = new CumStat(n);

        for(int z = 0; z < dims_in[2]; ++z) {
            ipl.setSlice(z + 1);
            for(int x = 0; x < dims_in[0]; ++x) {
                for(int y = 0; y < dims_in[1]; ++y) {
                    int x_put = (int)(new Floor()).value((double)(x / xshrink));
                    int y_put = (int)(new Floor()).value((double)(y / yshrink));
                    int z_put = (int)(new Floor()).value((double)(z / zshrink));
                    int ind = indexer.getIndex(new int[]{x_put, y_put, z_put});

                    double val = ipr.getf(x, y); //Input image is in float format
                    cs.addSingleMeasure(ind, val);
                }
            }
        }

        //Getting requested measure
        double[] val = new double[n];
        if(method == AVERAGE) {
            val = cs.getMean();
        } else if(method == MAX) {
            val = cs.getMax();
        } else if(method == MIN) {
            val = cs.getMin();
        } else if(method == SUM) {
            val = cs.getSum();
        }
        double[] num = cs.getN();

        for (int i=0;i<val.length;i++) {
            int[] coord = indexer.getCoord(i);
            ipl_bin.setSlice(coord[2]+1);
            if (num[i] == 0) {
                ipr_bin.putPixelValue(coord[0], coord[1], Float.NaN);
            } else {
                ipr_bin.putPixelValue(coord[0], coord[1], val[i]);
            }
        }

        return ipl_bin;
    }
}
