package io.github.sjcross.sjcommon.expectedobjects;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import io.github.sjcross.sjcommon.object.tracks.Track;

public class Tracks2D {
    public static LinkedHashMap<Integer,Track> getTracks() {
        LinkedHashMap<Integer,Track> tracks = new LinkedHashMap<>();

        // Adding all provided coordinates to each object
        List<Integer[]> coordinates = ExpectedObjects.getCoordinates5D("/coordinates/Tracks2D.csv");
        for (Integer[] coordinate:coordinates) {
            int ID = coordinate[0];

            int x = coordinate[2];
            int y = coordinate[3];
            int z = coordinate[5];
            int t = coordinate[6];

            tracks.putIfAbsent(ID,new Track("px"));
            Track track = tracks.get(ID);
            track.addTimepoint(x,y,z,t);

        }

        return tracks;

    }

    public static TreeMap<Integer,Double> getMeanMSD() {
        TreeMap<Integer,Double> msd = new TreeMap<>();

        try {
            String pathToResults = URLDecoder.decode(Tracks2D.class.getResource("/matlab/Tracks2D_MSD.csv").getPath(),"UTF-8");

            BufferedReader reader = new BufferedReader(new FileReader(pathToResults));
            CSVReader csvReader = new CSVReader(reader);

            String[] result = csvReader.readNext();
            while(result != null) {
                msd.put(Integer.parseInt(result[0]), Double.parseDouble(result[1]));
                result = csvReader.readNext();
            }

            return msd;

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }
}
