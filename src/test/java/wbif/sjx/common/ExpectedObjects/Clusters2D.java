package wbif.sjx.common.ExpectedObjects;

import util.opencsv.CSVReader;
import wbif.sjx.common.Object.Point;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class Clusters2D {
    public static ArrayList<Point<Double>> getCentroids() throws FileNotFoundException {
        try {
            String path = "/MATLAB/K-function/Clusters_2D_centroids.csv";
            String pathToCoordinates = URLDecoder.decode(ExpectedObjects.class.getResource(path).getPath(),"UTF-8");

            BufferedReader reader = new BufferedReader(new FileReader(pathToCoordinates));
            CSVReader csvReader = new CSVReader(reader);

            ArrayList<Point<Double>> centroids = new ArrayList<>();
            String[] row = csvReader.readNext();
            while (row != null) {
                double x = Double.parseDouble(row[0]);
                double y = Double.parseDouble(row[1]);

                centroids.add(new Point<>(x,y,0d));
                row = csvReader.readNext();
            }

            return centroids;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static HashMap<Point<Double>,Double> getExpectedCorrections() throws FileNotFoundException {
        try {
            String path = "/MATLAB/K-function/Clusters_2D_corrections.csv";
            String pathToCoordinates = URLDecoder.decode(ExpectedObjects.class.getResource(path).getPath(),"UTF-8");

            BufferedReader reader = new BufferedReader(new FileReader(pathToCoordinates));
            CSVReader csvReader = new CSVReader(reader);

            HashMap<Point<Double>,Double> corrections = new HashMap<>();
            String[] row = csvReader.readNext();
            while (row != null) {
                double x = Double.parseDouble(row[0]);
                double y = Double.parseDouble(row[1]);
                double corr = Double.parseDouble(row[2]);

                corrections.put(new Point<>(x,y,0d),corr);
                row = csvReader.readNext();
            }

            return corrections;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static TreeMap<Double,Double> getKFunctionWithoutCorrection() {
        try {
            String path = "/MATLAB/K-function/Clusters_2D_Kfn_WithoutCorrection.csv";
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
            String path = "/MATLAB/K-function/Clusters_2D_Kfn_WithCorrection.csv";
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

    public static TreeMap<Double,Double> getLFunctionWithoutCorrection() {
        try {
            String path = "/MATLAB/K-function/Clusters_2D_Lfn_WithoutCorrection.csv";
            String pathToCoordinates = URLDecoder.decode(ExpectedObjects.class.getResource(path).getPath(),"UTF-8");

            BufferedReader reader = new BufferedReader(new FileReader(pathToCoordinates));
            CSVReader csvReader = new CSVReader(reader);

            TreeMap<Double,Double> results = new TreeMap<>();
            String[] row = csvReader.readNext();
            while (row != null) {
                double ts = Double.parseDouble(row[1]);
                double lVal = Double.parseDouble(row[2]);

                results.put(ts,lVal);
                row = csvReader.readNext();
            }

            return results;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static TreeMap<Double,Double> getLFunctionWithCorrection() {
        try {
            String path = "/MATLAB/K-function/Clusters_2D_Lfn_WithCorrection.csv";
            String pathToCoordinates = URLDecoder.decode(ExpectedObjects.class.getResource(path).getPath(),"UTF-8");

            BufferedReader reader = new BufferedReader(new FileReader(pathToCoordinates));
            CSVReader csvReader = new CSVReader(reader);

            TreeMap<Double,Double> results = new TreeMap<>();
            String[] row = csvReader.readNext();
            while (row != null) {
                double ts = Double.parseDouble(row[1]);
                double lVal = Double.parseDouble(row[2]);

                results.put(ts,lVal);
                row = csvReader.readNext();
            }

            return results;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
