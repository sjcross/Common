package io.github.sjcross.common.object.voxels;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

public class BresenhamRectangle {
    public static void main(String[] args) {
        ImagePlus ipl = IJ.createImage("Rect", 200, 200, 360, 8);
        for (int theta = 0; theta < 360; theta++) {
            ipl.setSlice(theta+1);
            ImageProcessor ipr = ipl.getProcessor();

            int[][] rect = BresenhamRectangle.getRectangle(60, 90, theta);
            for (int i = 0; i < rect.length; i++)
                ipr.set(100 + rect[i][0], 100 + rect[i][1], 255);
        }

        new ImageJ();
        ipl.show();
        IJ.runMacro("waitForUser");

    }

    public static int[][] getRectangle(int width, int length, int thetaDegs) {
        double thetaRads = Math.toRadians((double) thetaDegs);
        int x1 = (int) Math.round((-length / 2) * Math.cos(thetaRads) - (width / 2) * Math.sin(thetaRads));
        int y1 = (int) Math.round((width / 2) * Math.cos(thetaRads) - (length / 2) * Math.sin(thetaRads));
        int x2 = (int) Math.round((length / 2) * Math.cos(thetaRads) - (width / 2) * Math.sin(thetaRads));
        int y2 = (int) Math.round((width / 2) * Math.cos(thetaRads) + (length / 2) * Math.sin(thetaRads));
        int x3 = -x1;
        int y3 = -y1;
        int x4 = -x2;
        int y4 = -y2;

        int[][] line1 = BresenhamLine.getLine(x1, x2, y1, y2);
        int[][] line2 = BresenhamLine.getLine(x2, x3, y2, y3);
        int[][] line3 = BresenhamLine.getLine(x3, x4, y3, y4);
        int[][] line4 = BresenhamLine.getLine(x4, x1, y4, y1);

        int nCoords = line1.length + line2.length + line3.length + line4.length;

        int[][] rect = new int[nCoords][2];
        addCoords(rect, line1, 0);
        addCoords(rect, line2, line1.length);
        addCoords(rect, line3, line1.length + line2.length);
        addCoords(rect, line4, line1.length + line2.length + line3.length);

        return rect;

    }

    static void addCoords(int[][] output, int[][] input, int offset) {
        for (int i = 0; i < input.length; i++) {
            output[i + offset][0] = input[i][0];
            output[i + offset][1] = input[i][1];
        }
    }
}
