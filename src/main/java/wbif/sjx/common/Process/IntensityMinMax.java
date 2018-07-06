package wbif.sjx.common.Process;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import ij.process.ImageStatistics;
import wbif.sjx.common.MathFunc.CumStat;

/**
 * Created by steph on 15/04/2017.
 */
public class IntensityMinMax {
    public static void run(ImagePlus ipl, boolean stack) {
        if (stack && ipl.getNDimensions() > 2) processStack(ipl,0);
        else processSingle(ipl,0);

        ipl.updateChannelAndDraw();

    }

    public static void run(ImagePlus ipl, boolean stack, double weight) {
        if (stack) {
            processStack(ipl,weight);
        } else {
            processSingle(ipl,weight);
        }

        ipl.updateChannelAndDraw();

    }

    static void processSingle(ImagePlus ipl, double weight) {
        ImageStatistics stats = ImageStatistics.getStatistics(ipl.getProcessor(), ImageStatistics.MIN_MAX, null);

        // Getting the minimum and maximum values
        double min = Double.NaN; double max = Double.NaN;

        if (weight == 0) {
            min = stats.min;
            max = stats.max;

        } else {
            boolean setMin = false;
            boolean setMax = false;
            int count = 0;
            int sum = ipl.getWidth() * ipl.getHeight() * ipl.getNSlices() * ipl.getNFrames();
            int[] hist = ipl.getBitDepth() == 16 ? stats.histogram16 : stats.histogram;
            for (int i = 0; i < hist.length; i++) {
                count = count + hist[i];
                if (!setMin && count >= sum * weight) {
                    min = i;
                    setMin = true;
                }
                if (!setMax && count >= sum - sum * weight) {
                    max = i;
                    setMax = true;
                }
            }
        }

        ipl.setDisplayRange(min,max);
        ipl.updateChannelAndDraw();

    }

    static void processStack(ImagePlus ipl, double weight) {
        for (int channel = 0; channel < ipl.getNChannels(); channel++) {
            double[] range;
            if (weight == 0) {
                range = getAbsoluteChannelRange(ipl,channel);
            } else {
                range = getWeightedChannelRange(ipl, channel, weight);
            }

            for (int slice = 0; slice < ipl.getNSlices(); slice++) {
                for (int frame = 0; frame < ipl.getNFrames(); frame++) {
                    ipl.setPosition(channel + 1, slice + 1, frame + 1);
                    ipl.setDisplayRange(range[0],range[1]);
                }
            }

            ipl.updateChannelAndDraw();

        }
    }

    static double[] getAbsoluteChannelRange(ImagePlus ipl, int channel) {
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;

        for (int slice = 0; slice < ipl.getNSlices(); slice++) {
            for (int frame = 0; frame < ipl.getNFrames(); frame++) {
                ipl.setPosition(channel + 1, slice + 1, frame + 1);
                ImageStatistics stats = ImageStatistics.getStatistics(ipl.getProcessor(), ImageStatistics.MIN_MAX, null);

                if (stats.min < min) min = stats.min;
                if (stats.max > max) max = stats.max;

            }
        }

        return new double[]{min,max};

    }

    /**
     *
     * @param ipl
     * @param channel Zero-based channel number
     * @param weight
     * @return
     */
    static double[] getWeightedChannelRange(ImagePlus ipl, int channel, double weight) {
        CumStat[] cs = new CumStat[(int) Math.pow(2,16)];
        for (int i=0;i<cs.length;i++) cs[i] = new CumStat();

        for (int slice = 0; slice < ipl.getNSlices(); slice++) {
            for (int frame = 0; frame < ipl.getNFrames(); frame++) {
                ipl.setPosition(channel+1,slice+1,frame+1);

                ImageStatistics stats = ImageStatistics.getStatistics(ipl.getProcessor(), ImageStatistics.MIN_MAX, null);
                int[] hist = ipl.getBitDepth() == 16 ? stats.histogram16 : stats.histogram;
                for (int i=0;i<hist.length;i++) cs[i].addMeasure(hist[i]);

            }
        }

        // Getting the minimum and maximum values
        double min = Double.NaN; double max = Double.NaN;
        boolean setMin = false; boolean setMax = false;
        int count = 0;
        int sum = ipl.getWidth() * ipl.getHeight() * ipl.getNSlices() * ipl.getNFrames();
        for (int i = 0; i < cs.length; i++) {
            count = count + (int) cs[i].getSum();
            if (!setMin && count >= sum * weight) {
                min = i;
                setMin = true;
            }
            if (!setMax && count >= sum - sum * weight) {
                max = i;
                setMax = true;
            }
        }

        return new double[]{min,max};

    }

}
