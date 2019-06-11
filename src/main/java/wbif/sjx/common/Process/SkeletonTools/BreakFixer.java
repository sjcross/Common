package wbif.sjx.common.Process.SkeletonTools;

import ij.ImagePlus;
import ij.Prefs;
import ij.plugin.ImageCalculator;
import ij.plugin.filter.Convolver;
import ij.process.ImageProcessor;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import wbif.sjx.common.MathFunc.BresenhamLine;

import java.util.ArrayList;

public class BreakFixer {
    static ArrayList<int[]> getEndPoints(ImageProcessor iprConn, int x, int y, int nPx) {
        // Getting the current point and 5 adjacent points.  A line will be fit to these.
        ArrayList<int[]> c = new ArrayList<>();
        int[] lc = new int[]{x,y};
        iprConn.set(lc[0],lc[1],0);
        c.add(lc);

        int nAdded = 1;
        while (nAdded < nPx) {
            nAdded++;

            // Terminate process if a junction is reached
            if (iprConn.get(lc[0]-1,lc[1]+1) > 3 | iprConn.get(lc[0]-1,lc[1]) > 3
                    | iprConn.get(lc[0]-1,lc[1]-1) > 3 | iprConn.get(lc[0],lc[1]+1) > 3
                    | iprConn.get(lc[0],lc[1]-1) > 3 | iprConn.get(lc[0]+1,lc[1]+1) > 3
                    | iprConn.get(lc[0]+1,lc[1]) > 3 | iprConn.get(lc[0]+1,lc[1]-1) > 3) break;

            if (iprConn.get(lc[0]-1,lc[1]+1) == 3) {
                lc = new int[]{lc[0]-1,lc[1]+1};
                c.add(lc);
                iprConn.set(lc[0],lc[1],0);
                continue;
            }

            if (iprConn.get(lc[0]-1,lc[1]) == 3) {
                lc = new int[]{lc[0]-1,lc[1]};
                c.add(lc);
                iprConn.set(lc[0],lc[1],0);
                continue;
            }

            if (iprConn.get(lc[0]-1,lc[1]-1) == 3) {
                lc = new int[]{lc[0]-1,lc[1]-1};
                c.add(lc);
                iprConn.set(lc[0],lc[1],0);
                continue;
            }

            if (iprConn.get(lc[0],lc[1]+1) == 3) {
                lc = new int[]{lc[0],lc[1]+1};
                c.add(lc);
                iprConn.set(lc[0],lc[1],0);
                continue;
            }

            if (iprConn.get(lc[0],lc[1]-1) == 3) {
                lc = new int[]{lc[0],lc[1]-1};
                c.add(lc);
                iprConn.set(lc[0],lc[1],0);
                continue;
            }

            if (iprConn.get(lc[0]+1,lc[1]+1) == 3) {
                lc = new int[]{lc[0]+1,lc[1]+1};
                c.add(lc);
                iprConn.set(lc[0],lc[1],0);
                continue;
            }

            if (iprConn.get(lc[0]+1,lc[1]) == 3) {
                lc = new int[]{lc[0]+1,lc[1]};
                c.add(lc);
                iprConn.set(lc[0],lc[1],0);
                continue;
            }

            if (iprConn.get(lc[0]+1,lc[1]-1) == 3) {
                lc = new int[]{lc[0]+1,lc[1]-1};
                c.add(lc);
                iprConn.set(lc[0],lc[1],0);
            }
        }

        return c;

    }

    static double getEndAngle(ArrayList<int[]> c) {
        double[][] data = new double[c.size()][2];
        int count = 0;
        for (int[] coord:c) {
            data[count][0] = coord[0];
            data[count++][1] = coord[1];
        }

        SimpleRegression sr = new SimpleRegression();
        sr.addData(data);

        return sr.getSlope();

    }

    public static void process(ImageProcessor iprOrig, int nPx, int distLim, int angleLim) {
        // If the image has a maximum intensity of 255, dividing by 255
        if (iprOrig.getStatistics().max == 255) iprOrig.multiply(1d/255d);

        // Duplicating the image
        ImageProcessor iprConn = iprOrig.duplicate();

        // Convolving with connectivity kernel
        Convolver convolver = new Convolver();
        convolver.setNormalize(false);
        convolver.convolve(iprConn,new float[]{1,1,1,1,1,1,1,1,1},3,3);

        // Multiplying the convolved image with the original image
        new ImageCalculator().run("Multiply",new ImagePlus("Conn",iprConn),new ImagePlus("Orig",iprOrig));

        // For each pixel with connectivity = 2, any other connectivity = 2 are searched for in the local vicinity.  The
        // points to be tested are only within an accepted angle from the current end (estimated by the adjacent 5
        // pixels).
        for (int x=0;x<iprConn.getWidth();x++) {
            for (int y = 0; y < iprConn.getHeight(); y++) {
                if (iprConn.get(x,y) != 2) continue;

                // Getting end points for fitting
                ArrayList<int[]> c = getEndPoints(iprConn,x,y,nPx);

                // We need more than 1 pixel to determine the orientation
                if (c.size() > 2) continue;

                // Calculating the angle of the line using linear regression
                double slope = getEndAngle(c);

                // Testing all points within acceptable distance distLim to see if they are also ends
                for (int xx = x- distLim; xx<x+ distLim; xx++) {
                    for (int yy = y- distLim; yy<y+ distLim; yy++) {
                        if (xx < 0 || xx >= iprConn.getWidth() || yy < 0 || yy >= iprConn.getHeight()) continue;
                        if (iprConn.get(xx, yy) != 2) continue;

                        // Checking that the position being tested isn't closer to the next pixel in the tube
                        // (i.e. a connection would be made in the opposite direction to the tube)
                        if (Math.sqrt((xx - x) * (xx - x) + (yy - y) * (yy - y))
                                > Math.sqrt((xx - c.get(1)[0]) * (xx - c.get(1)[0]) + (yy - c.get(1)[1]) * (yy - c.get(1)[1])))
                            continue;

                        double dist = Math.sqrt((xx - x) * (xx - x) + (yy - y) * (yy - y));

                        // Checking the position being tested is within an acceptable distance
                        if (dist > distLim) continue;

                        // Testing the angle to the tube slope
                        double dotP = (xx - x) + (yy - y) * slope;
                        double mag1 = Math.sqrt(1 + slope * slope);
                        double mag2 = Math.sqrt((xx - x) * (xx - x) + (yy - y) * (yy - y));
                        double angle = Math.acos(dotP / (mag1 * mag2));

                        // Checking the position being tested is within an acceptable angle
                        if (Math.abs(angle) > angleLim*Math.PI/180) continue;

                        // Doing the linking
                        int[][] line = BresenhamLine.getLine(x, xx, y, yy);
                        for (int[] px : line) iprOrig.set(px[0], px[1], 1);

                        iprOrig.set(x,y,10);
                        iprOrig.set(xx,yy,10);

                        // Updating connectivity on the joined ends
                        iprConn.set(x,y,3);
                        iprConn.set(xx,yy,3);

                    }
                }
            }
        }

        // Returning to standard levels for ImageJ
        iprOrig.multiply(255);

    }
}
