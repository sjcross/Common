package wbif.sjx.common.Process;

import ij.ImagePlus;
import ij.gui.NewImage;
import ij.process.ImageProcessor;
import java.util.HashMap;
import wbif.sjx.common.MathFunc.CumStat;
import wbif.sjx.common.MathFunc.Indexer;

public class NanBinner {
    public static final int AVERAGE = 0;
    public static final int MAX = 1;
    public static final int MIN = 2;
    public static final int SUM = 3;

    public static ImagePlus shrink(ImagePlus ipl, int xshrink, int yshrink, int zshrink, int method) {
        ImageProcessor ipr = ipl.getProcessor();
        int widthIn = ipl.getWidth();
        int heightIn = ipl.getHeight();
        int nSlicesIn = ipl.getNSlices();
        int widthOut = (int)Math.ceil((double)widthIn / (double)xshrink);
        int heightOut = (int)Math.ceil((double)heightIn / (double)yshrink);
        int nSlicesOut = (int)Math.ceil((double)nSlicesIn / (double)zshrink);
        ImagePlus ipl_bin = NewImage.createFloatImage("BINNED", widthOut, heightOut, nSlicesOut, 1);
        ImageProcessor ipr_bin = ipl_bin.getProcessor();

        for(int z = 0; z < nSlicesOut; ++z) {
            ipl_bin.setPosition(z + 1);

            for(int x = 0; x < widthOut; ++x) {
                for(z = 0; z < heightOut; ++z) {
                    ipr_bin.putPixelValue(x, z, 0.0D / 0.0);
                }
            }
        }

        Indexer indexer = new Indexer(new int[]{widthOut, heightOut, nSlicesOut});
        HashMap<Integer, CumStat> measurements = new HashMap<>();

        for(int z = 0; z < nSlicesIn; ++z) {
            ipl.setPosition(z + 1);

            for(int x = 0; x < widthIn; ++x) {
                for(int y = 0; y < heightIn; ++y) {
                    int x_put = (int)Math.floor((double)(x / xshrink));
                    int y_put = (int)Math.floor((double)(y / yshrink));
                    int z_put = (int)Math.floor((double)(z / zshrink));

                    double val = (double)ipr.getf(x, y);

                    int idx = indexer.getIndex(new int[]{x_put, y_put, z_put});
                    measurements.putIfAbsent(idx, new CumStat());
                    (measurements.get(idx)).addMeasure(val);
                }
            }
        }

        for(int z = 0; z < nSlicesOut; ++z) {
            ipl.setPosition(z + 1);

            for(int x = 0; x < widthOut; ++x) {
                for(int y = 0; y < heightOut; ++y) {
                    int idx = indexer.getIndex(new int[]{x, y, z});
                    if(measurements.containsKey(idx)) {
                        switch(method) {
                            case 0:
                                ipr_bin.putPixelValue(x, y, (measurements.get(idx)).getMean());
                                break;
                            case 1:
                                ipr_bin.putPixelValue(x, y, (measurements.get(idx)).getMax());
                                break;
                            case 2:
                                ipr_bin.putPixelValue(x, y, (measurements.get(idx)).getMin());
                                break;
                            case 3:
                                ipr_bin.putPixelValue(x, y, (measurements.get(idx)).getSum());
                        }
                    } else {
                        ipr_bin.putPixelValue(x, y, 0.0D / 0.0);
                    }
                }
            }
        }

        return ipl_bin;
    }
}
