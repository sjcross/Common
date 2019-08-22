package wbif.sjx.common.ExpectedObjects;

import util.opencsv.CSVReader;
import wbif.sjx.common.Object.Track;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.TreeMap;

public class SingleTrack2D {
    public static Track getTrack() {
        Track track = new Track("px");

        // Adding all provided coordinates to each object
        List<Integer[]> coordinates = ExpectedObjects.getCoordinates5D("/coordinates/SingleTrack2D.csv");
        for (Integer[] coordinate:coordinates) {
            int ID = coordinate[0];

            int x = coordinate[2];
            int y = coordinate[3];
            int z = coordinate[5];
            int t = coordinate[6];

            track.addTimepoint(x,y,z,t);

        }

        return track;

    }

    public static TreeMap<Integer,Double> getMSD() {
        TreeMap<Integer,Double> msd = new TreeMap<>();

        try {
            String pathToResults = URLDecoder.decode(SingleTrack2D.class.getResource("/MATLAB/SingleTrack2D_MSD.csv").getPath(),"UTF-8");

            BufferedReader reader = new BufferedReader(new FileReader(pathToResults));
            CSVReader csvReader = new CSVReader(reader);

            String[] result = csvReader.readNext();
            while(result != null) {
                msd.put(Integer.parseInt(result[0]), Double.parseDouble(result[1]));
                result = csvReader.readNext();
            }

            return msd;

        } catch (IOException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }
}
