package wbif.sjx.common.Object.Volume;

import ij.ImagePlus;
import ij.measure.Calibration;

public class SpatCal {
    public final double dppXY; //Calibration in xy
    public final double dppZ; //Calibration in z
    public final String units;

    public final int width;
    public final int height;
    public final int nSlices;

    public SpatCal(double dppXY, double dppZ, String units, int width, int height, int nSlices) {
        this.dppXY = dppXY;
        this.dppZ = dppZ;
        this.units = units;
        this.width = width;
        this.height = height;
        this.nSlices = nSlices;
    }

    public SpatCal duplicate() {
        return new SpatCal(dppXY,dppZ, units,width,height,nSlices);
    }

    public static SpatCal getFromImage(ImagePlus ipl) {
        Calibration calibration = ipl.getCalibration();

        int width = ipl.getWidth();
        int height = ipl.getHeight();
        int nSlices = ipl.getNSlices();
        double dppXY = calibration.getX(1);
        double dppZ = calibration.getZ(1);
        String units = calibration.getUnits();

        return new SpatCal(dppXY,dppZ,units,width,height,nSlices);

    }

    public Calibration createImageCalibration() {
        Calibration calibration = new Calibration();

        calibration.pixelWidth = dppXY;
        calibration.pixelHeight= dppXY;
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

    public int getnSlices() {
        return nSlices;
    }

}
