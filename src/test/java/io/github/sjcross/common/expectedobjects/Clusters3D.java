package io.github.sjcross.common.expectedobjects;

import io.github.sjcross.common.object.Point;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.TreeMap;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class Clusters3D {
    public static ArrayList<Point<Double>> getCentroids() throws FileNotFoundException, CsvValidationException {
        try {
            String path = "/matlab/kfunction/Clusters_3D_centroids.csv";
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

    public static TreeMap<Double,Double> getKFunctionWithoutCorrection() throws CsvValidationException {
        try {
            String path = "/matlab/kfunction/Clusters_3D_Kfn_WithoutCorrection.csv";
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

    public static TreeMap<Double,Double> getKFunctionWithCorrection() throws CsvValidationException {
        try {
            String path = "/matlab/kfunction/Clusters_3D_Kfn_WithCorrection.csv";
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
