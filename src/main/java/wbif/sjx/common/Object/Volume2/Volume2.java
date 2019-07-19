package wbif.sjx.common.Object.Volume2;

import wbif.sjx.common.Exceptions.IntegerOverflowException;
import wbif.sjx.common.Object.Point;
import wbif.sjx.common.Object.Volume;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.stream.Collectors;

public abstract class Volume2 {
    protected final double dppXY; //Calibration in xy
    protected final double dppZ; //Calibration in z
    protected final String calibratedUnits;

    protected final int width;
    protected final int height;
    protected final int nSlices;

    public Volume2(double dppXY, double dppZ, String calibratedUnits, int width, int height, int nSlices) {
        this.dppXY = dppXY;
        this.dppZ = dppZ;
        this.calibratedUnits = calibratedUnits;
        this.width = width;
        this.height = height;
        this.nSlices = nSlices;
    }


    // ABSTRACT METHODS

    public abstract Volume2 addCoord(int x, int y, int z) throws IntegerOverflowException ;

    public abstract TreeSet<Point<Integer>> getPoints();

    public abstract Volume2 setPoints(TreeSet<Point<Integer>> points);

    public abstract void clearPoints();

    public abstract TreeSet<Point<Integer>> getSurface();

    public abstract boolean is2D();

    public abstract void calculateSurface();


    // PUBLIC METHODS

    public double getXYScaledZ(double z) {
        return z*dppZ/dppXY;
    }

    public ArrayList<Integer> getXCoords() {
        return getPoints().stream().map(Point::getX).collect(Collectors.toCollection(ArrayList::new));

    }

    public ArrayList<Integer> getYCoords() {
        return getPoints().stream().map(Point::getY).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Integer> getZCoords() {
        return getPoints().stream().map(Point::getZ).collect(Collectors.toCollection(ArrayList::new));

    }

    public ArrayList<Integer> getSurfaceXCoords() {
        return getSurface().stream().map(Point::getX).collect(Collectors.toCollection(ArrayList::new));

    }

    public ArrayList<Integer> getSurfaceYCoords() {
        return getSurface().stream().map(Point::getY).collect(Collectors.toCollection(ArrayList::new));

    }

    public ArrayList<Integer> getSurfaceZCoords() {
        return getSurface().stream().map(Point::getZ).collect(Collectors.toCollection(ArrayList::new));

    }


    // GETTERS AND SETTERS

    public double getDppXY() {
        return dppXY;
    }

    public double getDppZ() {
        return dppZ;
    }

    public String getCalibratedUnits() {
        return calibratedUnits;
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
