package io.github.sjcross.common.expectedobjects;

import io.github.sjcross.common.exceptions.IntegerOverflowException;
import io.github.sjcross.common.object.volume.PointOutOfRangeException;
import io.github.sjcross.common.object.volume.Volume;
import io.github.sjcross.common.object.volume.VolumeType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;


/**
 * Created by sc13967 on 21/06/2018.
 */
public abstract class ExpectedObjects {
    public abstract List<Integer[]> getCoordinates5D();
    public abstract boolean is2D();

    public Volume getObject(int width, int height, int nSlices, double dppXY, double dppZ, String units) throws IntegerOverflowException {
        Volume volume = new Volume(VolumeType.POINTLIST,width,height,nSlices,dppXY,dppZ,units);

        // Adding all provided coordinates to each object
        List<Integer[]> coordinates = getCoordinates5D();
        for (Integer[] coordinate:coordinates) {
            int x = coordinate[2];
            int y = coordinate[3];
            int z = coordinate[5];

            try {
                volume.add(x,y,z);
            } catch (PointOutOfRangeException e) {
                e.printStackTrace();
            }

        }

        return volume;

    }

    public HashMap<Integer,Volume> getObjects(int width, int height, int nSlices, double dppXY, double dppZ, String units) throws IntegerOverflowException {
        // Initialising object store
        HashMap<Integer,Volume> testObjects = new HashMap<>();

        // Adding all provided coordinates to each object
        List<Integer[]> coordinates = getCoordinates5D();
        for (Integer[] coordinate:coordinates) {
            int ID = coordinate[0];

            int x = coordinate[2];
            int y = coordinate[3];
            int z = coordinate[5];
            int t = coordinate[6];

            ID = ID+(t*65536);

            testObjects.putIfAbsent(ID,new Volume(VolumeType.POINTLIST,width,height,nSlices,dppXY,dppZ,units));
            Volume testObject = testObjects.get(ID);
            try {
                testObject.add(x,y,z);
            } catch (PointOutOfRangeException e) {
                e.printStackTrace();
            }

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

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    abstract public HashMap<Integer, HashMap<String, Double>> getMeasurements();
}
