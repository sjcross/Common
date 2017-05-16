package wbif.sjx.common.HighContent.Object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by steph on 30/04/2017.
 */
public class HCObject {
    // Coordinate dimensions 0-4 are reserved for X,Y,C,Z,T
    public static final int X = 0;
    public static final int Y = 1;
    public static final int C = 2;
    public static final int Z = 3;
    public static final int T = 4;

    private int ID = 0;
    private HashMap<Integer, ArrayList<Integer>> coordinates = new HashMap<>();
    private HCObject parent = null;
    private ArrayList<HCObject> children = new ArrayList<HCObject>();
    private LinkedHashMap<String,HCSingleMeasurement> singleMeasurements = new LinkedHashMap<>();
    private LinkedHashMap<String,HCMultiMeasurement> multiMeasurements = new LinkedHashMap<>();
    private String calibratedUnits = "px";

    /**
     * Calibration for each dimension.  Stored as physical distance per pixel
     */
    private HashMap<Integer, Double> calibration = new HashMap<>();


    // CONSTRUCTORS

    public HCObject(int ID) {
        this.ID = ID;

    }


    // PUBLIC METHODS

    public void addCoordinate(int dim, int coordinate) {
        coordinates.computeIfAbsent(dim, k -> new ArrayList<>());
        coordinates.get(dim).add(coordinate);

    }

    public void removeCoordinate(int dim, double coordinate) {
        coordinates.get(dim).remove(coordinate);

    }

    public int[][] getCoordinateRange() {
        int nDims = Collections.max(coordinates.keySet())+1;
        nDims = nDims <= 5 ? 5 : nDims;

        int[][] dimSize = new int[nDims][2];

        for (int dim:coordinates.keySet()) {
            if (coordinates.get(dim) != null) {
                ArrayList<Integer> vals = coordinates.get(dim);
                dimSize[dim][0] = Collections.min(vals);
                dimSize[dim][1] = Collections.max(vals);
            }
        }

        return dimSize;
    }

    public void addSingleMeasurement(String name, HCSingleMeasurement singleMeasurement) {
        singleMeasurements.put(name,singleMeasurement);

    }

    public HCSingleMeasurement getSingleMeasurement(String name) {
        return singleMeasurements.get(name);

    }

    public void addMultiMeasurement(String name, HCMultiMeasurement multiMeasurement) {
        multiMeasurements.put(name,multiMeasurement);

    }

    public HCMultiMeasurement getMultiMeasurement(String name) {
        return multiMeasurements.get(name);

    }

    public void setMultiMeasurementPoint(String name, double coordinate, double value) {
        multiMeasurements.computeIfAbsent(name,k -> new HCMultiMeasurement(name));
        multiMeasurements.get(name).addValue(coordinate,value);

    }

    public double getMultiMeasurementPoint(String name, double coordinate) {
        return multiMeasurements.get(name).getValue(coordinate);

    }

    public void addCalibration(Integer dim, double cal) {
        calibration.put(dim,cal);

    }

    public double getCalibration(Integer dim) {
        // If no calibration has been set, return 1
        if (coordinates.get(dim) == null) return 1;

        return calibration.get(dim);

    }

    public int[] getCoordinatesAsArray(Integer dim) {
        int[] coords = new int[coordinates.get(dim).size()];

        int iter = 0;
        for (int coord:coordinates.get(dim)) {
            coords[iter++] = coord;

        }

        return coords;

    }

    @Override
    public String toString() {
        return  "HCObject with "+coordinates.size()+" dimensions and "+coordinates.values().iterator().next().size()+
                " coordinate points";

    }


    // GETTERS AND SETTERS

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setCoordinates(int dim, ArrayList<Integer> coordinateList) {
        coordinates.put(dim, coordinateList);

    }

    public ArrayList<Integer> getCoordinates(int dim) {
        return coordinates.get(dim);

    }

    public HashMap<Integer, ArrayList<Integer>> getCoordinates() {
        return coordinates;

    }

    public void setCoordinates(HashMap<Integer, ArrayList<Integer>> coordinates) {
        this.coordinates = coordinates;

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

    public LinkedHashMap<String, HCSingleMeasurement> getSingleMeasurements() {
        return singleMeasurements;
    }

    public void setSingleMeasurements(LinkedHashMap<String, HCSingleMeasurement> singleMeasurements) {
        this.singleMeasurements = singleMeasurements;

    }

    public LinkedHashMap<String, HCMultiMeasurement> getMultiMeasurements() {
        return multiMeasurements;
    }

    public void setMultiMeasurements(LinkedHashMap<String, HCMultiMeasurement> multiMeasurements) {
        this.multiMeasurements = multiMeasurements;
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
