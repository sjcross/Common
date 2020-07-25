package wbif.sjx.common.Object.Volume;

import ij.ImagePlus;
import ij.measure.Calibration;

public class SpatCal {
    final public double dppXY; // Calibration in xy
    final public double dppZ; // Calibration in z
    final public String units;

    final public int width;
    final public int height;
    final public int nSlices;

    public SpatCal(double dppXY, double dppZ, String units, int width, int height, int nSlices) {
        this.dppXY = dppXY;
        this.dppZ = dppZ;
        this.units = units;
        this.width = width;
        this.height = height;
        this.nSlices = nSlices;
    }

    public SpatCal duplicate() {
        return new SpatCal(dppXY, dppZ, units, width, height, nSlices);
    }

    public static SpatCal getFromImage(ImagePlus ipl) {
        Calibration calibration = ipl.getCalibration();

        int width = ipl.getWidth();
        int height = ipl.getHeight();
        int nSlices = ipl.getNSlices();
        double dppXY = calibration.getX(1);
        double dppZ = calibration.getZ(1);
        String units = calibration.getUnits();

        return new SpatCal(dppXY, dppZ, units, width, height, nSlices);

    }

    public void setImageCalibration(ImagePlus ipl) {
        ipl.getCalibration().pixelWidth = dppXY;
        ipl.getCalibration().pixelHeight = dppXY;
        ipl.getCalibration().pixelDepth = nSlices == 1 ? 1 : dppZ;
        ipl.getCalibration().setUnit(units);

    }

    public Calibration createImageCalibration() {
        Calibration calibration = new Calibration();

        calibration.pixelWidth = dppXY;
        calibration.pixelHeight = dppXY;
        calibration.pixelDepth = dppZ;
        calibration.setUnit(units);

        return calibration;

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

    public int getNSlices() {
        return nSlices;
    }
}
