package wbif.sjx.common.ExpectedObjects;

import util.opencsv.CSVReader;
import wbif.sjx.common.Exceptions.IntegerOverflowException;
import wbif.sjx.common.Object.Volume2.PointVolume;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by sc13967 on 21/06/2018.
 */
public abstract class ExpectedObjects {
    public abstract List<Integer[]> getCoordinates5D();
    public abstract boolean is2D();

    public PointVolume getObject(int width, int height, int nSlices, double dppXY, double dppZ, String calibratedUnits) throws IntegerOverflowException {
        PointVolume volume = new PointVolume(width,height,nSlices,dppXY,dppZ,calibratedUnits);

        // Adding all provided coordinates to each object
        List<Integer[]> coordinates = getCoordinates5D();
        for (Integer[] coordinate:coordinates) {
            int x = coordinate[2];
            int y = coordinate[3];
            int z = coordinate[5];

            volume.add(x,y,z);

        }

        return volume;

    }

    public HashMap<Integer,PointVolume> getObjects(int width, int height, int nSlices, double dppXY, double dppZ, String calibratedUnits) throws IntegerOverflowException {
        // Initialising object store
        HashMap<Integer,PointVolume> testObjects = new HashMap<>();

        // Adding all provided coordinates to each object
        List<Integer[]> coordinates = getCoordinates5D();
        for (Integer[] coordinate:coordinates) {
            int ID = coordinate[0];

            int x = coordinate[2];
            int y = coordinate[3];
            int z = coordinate[5];
            int t = coordinate[6];

            ID = ID+(t*65536);

            testObjects.putIfAbsent(ID,new PointVolume(width,height,nSlices,dppXY,dppZ,calibratedUnits));
            PointVolume testObject = testObjects.get(ID);
            testObject.add(x,y,z);

        }

        return testObjects;

    }

    public static List<Integer[]> getCoordinates5D(String path) {
        try {
            String pathToCoordinates = URLDecoder.decode(ExpectedObjects.class.getResource(path).getPath(),"UTF-8");

            BufferedReader reader = new BufferedReader(new FileReader(pathToCoordinates));
            CSVReader csvReader = new CSVReader(reader);

            List<Integer[]> coords = new ArrayList<>();

            String[] coord = csvReader.readNext();
            while (coord != null) {
                Integer[] thisCoord = new Integer[coord.length];

                for (int j=0;j<coord.length;j++) {
                    thisCoord[j] = Integer.parseInt(coord[j]);
                }

                coords.add(thisCoord);
                coord = csvReader.readNext();
            }

            return coords;

        } catch (IOException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    abstract public HashMap<Integer, HashMap<String, Double>> getMeasurements();
}
