package wbif.sjx.common.Process;

import net.imagej.ImgPlus;
import net.imagej.axis.Axes;
import net.imagej.axis.DefaultLinearAxis;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

public class ImgPlusTools <T extends RealType<T> & NativeType<T>> {
    public static <T> long[] getDimensionsXYCZT(ImgPlus<T> img) {
        long[] dims1 = new long[5];
        dims1[0] = img.dimension(img.dimensionIndex(Axes.X));
        dims1[1] = img.dimension(img.dimensionIndex(Axes.Y));
        dims1[2] = img.dimension(img.dimensionIndex(Axes.CHANNEL));
        dims1[3] = img.dimension(img.dimensionIndex(Axes.Z));
        dims1[4] = img.dimension(img.dimensionIndex(Axes.TIME));

        return dims1;

    }

    public static <T> void applyCalibrationXYCZT(ImgPlus<T> sourceImg, ImgPlus<T> targetImg) {
        // Setting calibration from first image
        int axisIdx = sourceImg.dimensionIndex(Axes.X);
        if (axisIdx >= 0) {
            targetImg.setAxis(sourceImg.axis(axisIdx).copy(),0);
        } else {
            targetImg.setAxis(new DefaultLinearAxis(Axes.X,1),0);
        }

        axisIdx = sourceImg.dimensionIndex(Axes.Y);
        if (axisIdx >= 0) {
            targetImg.setAxis(sourceImg.axis(axisIdx).copy(),1);
        } else {
            targetImg.setAxis(new DefaultLinearAxis(Axes.Y,1),1);
        }

        axisIdx = sourceImg.dimensionIndex(Axes.CHANNEL);
        if (axisIdx >= 0) {
            targetImg.setAxis(sourceImg.axis(axisIdx).copy(),2);
        } else {
            targetImg.setAxis(new DefaultLinearAxis(Axes.CHANNEL,1),2);
        }

        axisIdx = sourceImg.dimensionIndex(Axes.Z);
        if (axisIdx >= 0) {
            targetImg.setAxis(sourceImg.axis(axisIdx).copy(),3);
        } else {
            targetImg.setAxis(new DefaultLinearAxis(Axes.Z,1),3);
        }

        axisIdx = sourceImg.dimensionIndex(Axes.TIME);
        if (axisIdx >= 0) {
            targetImg.setAxis(sourceImg.axis(axisIdx).copy(),4);
        } else {
            targetImg.setAxis(new DefaultLinearAxis(Axes.TIME,1),4);
        }
    }
}
