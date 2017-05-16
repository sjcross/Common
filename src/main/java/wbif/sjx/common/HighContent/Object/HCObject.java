package wbif.sjx.common.HighContent.Object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 * Created by steph on 30/04/2017.
 */
public class HCObject {
    public static final int X = 0;
    public static final int Y = 1;
    public static final int Z = 2;
    public static final int C = 3;
    public static final int T = 4;

    /**
     * Unique instance ID for this object
     */
    private int ID = 0;

    /**
     * 3D coordinates of this instance of the object.
     */
    private ArrayList<int[]>coordinates = new ArrayList<>();

    /**
     * HashMap containing extra dimensions specifying the location of this instance
     */
    private HashMap<Integer,Integer> extraDimensions = new HashMap<>();
    private HCObject parent = null;
    private ArrayList<HCObject> children = new ArrayList<HCObject>();
    private LinkedHashMap<String,HCMeasurement> measurements = new LinkedHashMap<>();
    private String calibratedUnits = "px";

    /**
     * Calibration for each dimension.  Stored as physical distance per pixel
     */
    private HashMap<Integer,Double> calibration = new HashMap<>();


    // CONSTRUCTORS

    public HCObject(int ID) {
        this.ID = ID;

    }


    // PUBLIC METHODS

    public int[][] getCoordinateRange() {
        int[][] dimSize = new int[5+extraDimensions.size()][2];

        for (int i=0;i<coordinates.size();i++) {
            if (coordinates.get(i)[0] < dimSize[0][0]) {
                dimSize[0][0] = coordinates.get(i)[0];
            } else if (coordinates.get(i)[0] > dimSize[0][1]) {
                dimSize[0][1] = coordinates.get(i)[0];
            } else if (coordinates.get(i)[1] < dimSize[1][0]) {
                dimSize[1][0] = coordinates.get(i)[1];
            } else if (coordinates.get(i)[1] > dimSize[1][1]) {
                dimSize[1][1] = coordinates.get(i)[1];
            } else if (coordinates.get(i)[2] < dimSize[2][0]) {
                dimSize[2][0] = coordinates.get(i)[2];
            } else if (coordinates.get(i)[2] > dimSize[2][1]) {
                dimSize[2][1] = coordinates.get(i)[2];
            }
        }

        // Adding the extra dimensions.  These are single valued, so the min and max is the same thing
        for (int i=0;i<extraDimensions.size();i++) {
            dimSize[3+i][0] = extraDimensions.get(i);
            dimSize[3+i][1] = extraDimensions.get(i);

        }

        return dimSize;

    }

    public void addMeasurement(String name, HCMeasurement measurement) {
        measurements.put(name,measurement);

    }

    public HCMeasurement getMeasurement(String name) {
        return measurements.get(name);

    }

    public void addCalibration(Integer dim, double cal) {
        calibration.put(dim,cal);

    }

    public double getCalibration(Integer dim) {
        // If no calibration has been set, return 1
        if (coordinates.get(dim) == null) return 1;

        return calibration.get(dim);

    }

    @Override
    public String toString() {
        return  "HCObject with "+coordinates.size()+" coordinate points";

    }


    // GETTERS AND SETTERS

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setCoordinates(ArrayList<int[]> coordinates) {
        this.coordinates = coordinates;

    }

    public ArrayList<int[]> getCoordinates() {
        return coordinates;

    }

    public int getExtraDimension(int dim) {
        return extraDimensions.get(dim) == null ? -1 : extraDimensions.get(dim);

    }

    public void setExtraDimensions(int dim, int value) {
        extraDimensions.put(dim,value);

    }

    public HCObject getParent() {
        return parent;
    }

    public void setParent(HCObject parent) {
        this.parent = parent;
    }

    public ArrayList<HCObject> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<HCObject> children) {
        this.children = children;
    }

    public void addChild(HCObject child) {
        children.add(child);
    }

    public void removeChild(HCObject child) {
        children.remove(child);
    }

    public LinkedHashMap<String, HCMeasurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(LinkedHashMap<String, HCMeasurement> measurements) {
        this.measurements = measurements;

    }

    public HashMap<Integer, Double> getCalibration() {
        return calibration;
    }

    public void setCalibration(HashMap<Integer, Double> calibration) {
        this.calibration = calibration;
    }

    public String getCalibratedUnits() {
        return calibratedUnits;
    }

    public void setCalibratedUnits(String calibratedUnits) {
        this.calibratedUnits = calibratedUnits;
    }

}
