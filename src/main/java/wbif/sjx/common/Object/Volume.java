package wbif.sjx.common.Object;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.stat.descriptive.rank.Min;
import wbif.sjx.common.MathFunc.ArrayFunc;

import java.util.ArrayList;

/**
 * Created by sc13967 on 28/07/2017.
 */
public class Volume {
    protected final double cal_xy; //Calibration in xy (fixed once declared in constructor)
    protected final double cal_z; //Calibration in z (fixed once declared in constructor)

    protected ArrayList<Double> x = new ArrayList<Double>();
    protected ArrayList<Double> y = new ArrayList<Double>();
    protected ArrayList<Double> z = new ArrayList<Double>();

    public Volume(double cal_xy, double cal_z) {
        this.cal_xy = cal_xy;
        this.cal_z = cal_z;

    }

    public void addCoord(double x_in, double y_in, double z_in) {
        x.add(x_in*cal_xy);
        y.add(y_in*cal_xy);
        z.add(z_in*cal_z);

    }

    public double[] getX() {
        double[] x_coords = new double[x.size()];

        for (int i=0;i<x.size();i++) {
            x_coords[i] = x.get(i);
        }

        return x_coords;
    }

    public double[] getY() {
        double[] y_coords = new double[y.size()];

        for (int i=0;i<y.size();i++) {
            y_coords[i] = y.get(i);
        }

        return y_coords;
    }

    public double[] getZ() {
        double[] z_coords = new double[z.size()];

        for (int i=0;i<z.size();i++) {
            z_coords[i] = z.get(i);
        }

        return z_coords;
    }

    public double getXMean() {
        double x_mean = new Mean().evaluate(getX());

        return x_mean;

    }

    public double getYMean() {
        double y_mean = new Mean().evaluate(getY());

        return y_mean;

    }

    public double getZMean() {
        double z_mean = new Mean().evaluate(getZ());

        return z_mean;

    }

    public double getCalXY() {
        return cal_xy;

    }

    public double getCalZ() {
        return cal_z;

    }

    public double getHeight() {
        double[] z = getZ();

        double min_z = new Min().evaluate(z);
        double max_z = new Max().evaluate(z);

        double height = max_z - min_z;

        return height;

    }

    public double[] getExtents() {
        //Minimum and maximum values for all dimensions [x_min, y_min, z_min; x_max, y_max, z_max]
        double[] extents = new double[6];

        double[] x = getX();
        double[] y = getY();
        double[] z = getZ();

        extents[0] = new Min().evaluate(x);
        extents[1] = new Max().evaluate(x);
        extents[2] = new Min().evaluate(y);
        extents[3] = new Max().evaluate(y);
        extents[4] = new Min().evaluate(z);
        extents[5] = new Max().evaluate(z);

        return extents;

    }

    public boolean hasVolume() {
        //True if all dimension (x,y,z) are > 0

        double[] extents = getExtents();

        boolean hasvol = false;

        if (extents[1]-extents[0] > 0 & extents[3]-extents[2] > 0 & extents[5]-extents[4] > 0) {
            hasvol = true;
        }

        return hasvol;
    }

    public boolean hasArea() {
        //True if all dimensions (x,y) are > 0

        double[] extents = getExtents();

        boolean hasarea = false;

        if (extents[1]-extents[0] > 0 & extents[3]-extents[2] > 0) {
            hasarea = true;
        }

        return hasarea;

    }

    public double getVoxelVolume() {
        return x.size()*cal_xy*cal_xy*cal_z;

    }

    public int getNVoxels() {
        return x.size();

    }

    public double getProjectedArea() {
        double[] x = getX();
        double[] y = getY();
        double[][] coords = new double[x.length][2];

        for (int i=0;i<x.length;i++) {
            coords[i][0] = x[i];
            coords[i][1] = y[i];
        }

        coords = ArrayFunc.uniqueRows(coords);

        return coords.length*cal_xy*cal_xy;

    }

    public int getOverlap(Volume volume2) {
        double[] x2 = volume2.getX();
        double[] y2 = volume2.getY();
        double[] z2 = volume2.getZ();
        int ovl = 0;

        for(int i = 0; i < this.x.size(); ++i) {
            for(int j = 0; j < x2.length; ++j) {
                if(x.get(i) == x2[j] & y.get(i) == y2[j] & z.get(i) == z2[j]) {
                    ovl++;
                }
            }
        }

        return ovl;
    }

    public double getCentroidDistanceToPoint(Spot point) {
        double x_cent = getXMean();
        double y_cent = getYMean();
        double z_cent = getZMean();

        double dist = Math.sqrt(Math.pow(x_cent-point.getX(),2) + Math.pow(y_cent-point.getY(),2) + Math.pow(z_cent-point.getZ(),2))-point.getRadius();

        return dist;

    }

    public double getNearestDistanceToPoint(Spot point) {
        double dist = Double.POSITIVE_INFINITY;
        for (int i=0;i<x.size();i++) {
            double temp_dist = Math.sqrt(Math.pow(x.get(i) - point.getX(), 2) + Math.pow(y.get(i) - point.getY(), 2) + Math.pow(z.get(i) - point.getZ(), 2))-point.getRadius();
            if (temp_dist < dist) {
                dist = temp_dist;
            }
        }

        return dist;

    }

}
