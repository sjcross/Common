package wbif.sjx.common.ExpectedObjects;

import util.opencsv.CSVReader;
import wbif.sjx.common.Object.Volume;

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
    private boolean is2D;
    public abstract List<Integer[]> getCoordinates5D();
    public abstract boolean is2D();

    public ExpectedObjects() {
        this.is2D = is2D();
    }

    public Volume getObject(double dppXY, double dppZ, String calibratedUnits) {
        Volume volume = new Volume(dppXY,dppZ,calibratedUnits,is2D);

        // Adding all provided coordinates to each object
        List<Integer[]> coordinates = getCoordinates5D();
        for (Integer[] coordinate:coordinates) {
            int x = coordinate[2];
            int y = coordinate[3];
            int z = coordinate[5];

            volume.addCoord(x,y,z);

        }

        return volume;

    }

    protected List<Integer[]> getCoordinates5D(String path) {
        try {
            String pathToCoordinates = URLDecoder.decode(this.getClass().getResource(path).getPath(),"UTF-8");

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

}
