package wbif.sjx.common.Object.Volume;

import ij.ImagePlus;
import ij.measure.Calibration;

public class VolumeCalibration {
    final double dppXY; //Calibration in xy
    final double dppZ; //Calibration in z
    final String units;

    final int width;
    final int height;
    final int nSlices;

    public VolumeCalibration(double dppXY, double dppZ, String units, int width, int height, int nSlices) {
        this.dppXY = dppXY;
        this.dppZ = dppZ;
        this.units = units;
        this.width = width;
        this.height = height;
        this.nSlices = nSlices;
    }

    public VolumeCalibration duplicate() {
        return new VolumeCalibration(dppXY,dppZ, units,width,height,nSlices);
    }

    public static VolumeCalibration getFromImage(ImagePlus ipl) {
        Calibration calibration = ipl.getCalibration();

        int width = ipl.getWidth();
        int height = ipl.getHeight();
        int nSlices = ipl.getNSlices();
        double dppXY = calibration.getX(1);
        double dppZ = calibration.getZ(1);
        String units = calibration.getUnits();

        return new VolumeCalibration(dppXY,dppZ,units,width,height,nSlices);

    }

    public double getDppXY() {
        return dppXY;
    }

    public double getDppZ() {
        return dppZ;
    }

    public String getUnits() {
        return units;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getnSlices() {
        return nSlices;
    }
}
