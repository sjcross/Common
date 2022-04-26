package io.github.sjcross.common.analysis;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import io.github.sjcross.common.mathfunc.CumStat;
import io.github.sjcross.common.mathfunc.GeneralOps;
import io.github.sjcross.common.object.Point;
import io.github.sjcross.common.object.volume.Volume;

/**
* Created by sc13967 on 20/06/2018.
*/
public class LongestChordCalculator {
    private double tolerance = 1E-10;
    private final Volume volume;
    
    private final double[][] LC; //Longest chord
    
    public LongestChordCalculator(Volume volume) {
        this.volume = volume;
        
        LC = calculateLC();
        
    }
    
    double[][] calculateLC() {
        //Reference for use as orientation descriptor: "Computer-Assisted Microscopy: The Measurement and Analysis of
        //Images", John C. Russ, Springer, 6 Dec 2012
        
        // Iterating over all point combinations on the Volume surface       
        double[][] lc = new double[2][3];
        
        double len = 0;
        for (Point<Integer> point1:volume.getSurface().getCoordinateSet()) {
            for (Point<Integer> point2:volume.getSurface().getCoordinateSet()) {
                if (point1 == point2) continue;
                
                double[] a = {point1.x, point1.y, volume.getXYScaledZ(point1.z)};
                double[] b = {point2.x, point2.y, volume.getXYScaledZ(point2.z)};
                
                double pp = GeneralOps.ppdist(a, b);
                
                if (pp > len) {
                    len = pp;
                    lc[0][0] = point1.x;
                    lc[0][1] = point1.y;
                    lc[0][2] = point1.z;
                    lc[1][0] = point2.x;
                    lc[1][1] = point2.y;
                    lc[1][2] = point2.z;
                }
            }
        }        
        
        return lc;
        
    }
    
    public CumStat calculateAverageDistanceFromLC() {
        CumStat cumStat = new CumStat();
        
        // Creating a vector between the two end points of the longest chord
        Vector3D v1 = new Vector3D(LC[0][0],LC[0][1],volume.getXYScaledZ(LC[0][2]));
        Vector3D v2 = new Vector3D(LC[1][0],LC[1][1],volume.getXYScaledZ(LC[1][2]));
        Line line;
        try {
            line = new Line(v1, v2, tolerance);
        } catch (MathIllegalArgumentException e) {
            return null;
        }
        
        for (Point<Integer> point:volume.getSurface().getCoordinateSet()) {
            Vector3D p1 = new Vector3D(point.x,point.y,volume.getXYScaledZ(point.z));
            cumStat.addMeasure(line.distance(p1));
        }
        
        return cumStat;
        
    }
    
    public double[][] getLC() {
        return LC;
    }
    
    public double getLCLength() {
        double[] a = {LC[0][0], LC[0][1], volume.getXYScaledZ(LC[0][2])};
        double[] b = {LC[1][0], LC[1][1], volume.getXYScaledZ(LC[1][2])};
        
        return GeneralOps.ppdist(a, b);
        
    }
    
    public double getXYOrientationRads() {
        return Math.atan2(LC[0][1]-LC[1][1],LC[0][0]-LC[1][0]);
    }
}
