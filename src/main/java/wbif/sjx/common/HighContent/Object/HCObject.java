package wbif.sjx.common.HighContent.Object;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

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

    public void setCoordinate(ArrayList<Integer> coordinateList, int dim) {
        coordinates.put(dim, coordinateList);

    }

    public ArrayList<Integer> getCoordinate(int dim) {
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
}
