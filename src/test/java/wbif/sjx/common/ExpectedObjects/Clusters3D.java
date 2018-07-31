package wbif.sjx.common.ExpectedObjects;

import util.opencsv.CSVReader;
import wbif.sjx.common.Object.Point;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.TreeMap;

public class Clusters3D {
    public static ArrayList<Point<Double>> getCentroids() throws FileNotFoundException {
        try {
            String path = "/MATLAB/K-function/Clusters_3D_centroids.csv";
            String pathToCoordinates = URLDecoder.decode(ExpectedObjects.class.getResource(path).getPath(),"UTF-8");

            BufferedReader reader = new BufferedReader(new FileReader(pathToCoordinates));
            CSVReader csvReader = new CSVReader(reader);

            ArrayList<Point<Double>> centroids = new ArrayList<>();
            String[] row = csvReader.readNext();
            while (row != null) {
                double x = Double.parseDouble(row[0]);
                double y = Double.parseDouble(row[1]);
                double z = Double.parseDouble(row[2]);

                centroids.add(new Point<>(x,y,z));
                row = csvReader.readNext();

            }

            return centroids;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static TreeMap<Double,Double> getKFunctionWithoutCorrection() {
        try {
            String path = "/MATLAB/K-function/Clusters_3D_Kfn_WithoutCorrection.csv";
            String pathToCoordinates = URLDecoder.decode(ExpectedObjects.class.getResource(path).getPath(),"UTF-8");

            BufferedReader reader = new BufferedReader(new FileReader(pathToCoordinates));
            CSVReader csvReader = new CSVReader(reader);

            TreeMap<Double,Double> results = new TreeMap<>();
            String[] row = csvReader.readNext();
            while (row != null) {
                double ts = Double.parseDouble(row[1]);
                double kVal = Double.parseDouble(row[2]);

                results.put(ts,kVal);
                row = csvReader.readNext();
            }

            return results;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static TreeMap<Double,Double> getKFunctionWithCorrection() {
        try {
            String path = "/MATLAB/K-function/Clusters_3D_Kfn_WithCorrection.csv";
            String pathToCoordinates = URLDecoder.decode(ExpectedObjects.class.getResource(path).getPath(),"UTF-8");

            BufferedReader reader = new BufferedReader(new FileReader(pathToCoordinates));
            CSVReader csvReader = new CSVReader(reader);

            TreeMap<Double,Double> results = new TreeMap<>();
            String[] row = csvReader.readNext();
            while (row != null) {
                double ts = Double.parseDouble(row[1]);
                double kVal = Double.parseDouble(row[2]);

                results.put(ts,kVal);
                row = csvReader.readNext();
            }

            return results;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
