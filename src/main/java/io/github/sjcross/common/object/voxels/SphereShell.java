package io.github.sjcross.common.object.voxels;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.ImageStack;

public class SphereShell extends AbstractSphere {
    public enum Connectivity {
        SIX, TWENTY_SIX;
    }

    public static void main(String[] args) {
        SphereShell sphereShell = new SphereShell(46,Connectivity.TWENTY_SIX);
        int[] x = sphereShell.getX();
        int[] y = sphereShell.getY();
        int[] z = sphereShell.getZ();

        new ImageJ();
        ImagePlus ipl = IJ.createImage("", 100, 100, 100, 8);
        ImageStack ist = ipl.getStack();
        for (int i = 0; i < x.length; i++) {
            ist.setVoxel(x[i] + 50, y[i] + 50, z[i] + 50, 255);
        }
        ipl.show();
    }

    public SphereShell(int r, Connectivity connectivity) {
        for (int xx = -r; xx < r+1; xx++) {
            for (int yy = -r; yy < r+1; yy++) {
                for (int zz = -r; zz < r+1; zz++) {
                    // First, check if current point is close to being in shell
                    if (inSphere(xx, yy, zz, r) & !inSphere(xx, yy, zz, r - 1)) {
                        // Now, check if point is actually on the surface
                        switch (connectivity) {
                            case SIX:
                                if (!onShellSixWay(xx, yy, zz, r))
                                    continue;
                                break;
                            case TWENTY_SIX:
                                if (!onShellTwentySixWay(xx, yy, zz, r))
                                    continue;
                                break;
                        }

                        x.add(xx);
                        y.add(yy);
                        z.add(zz);
                    }
                }
            }
        }
    }

    public static boolean onShellSixWay(int xx, int yy, int zz, int r) {
        if (!inSphere(xx - 1, yy, zz, r))
            return true;

        if (!inSphere(xx + 1, yy, zz, r))
            return true;

        if (!inSphere(xx, yy - 1, zz, r))
            return true;

        if (!inSphere(xx, yy + 1, zz, r))
            return true;

        if (!inSphere(xx, yy, zz - 1, r))
            return true;

        if (!inSphere(xx, yy, zz + 1, r))
            return true;

        return false;

    }

    public static boolean onShellTwentySixWay(int xx, int yy, int zz, int r) {
        // Z-1
        if (!inSphere(xx - 1, yy - 1, zz - 1, r))
            return true;

        if (!inSphere(xx, yy - 1, zz - 1, r))
            return true;

        if (!inSphere(xx + 1, yy - 1, zz - 1, r))
            return true;

        if (!inSphere(xx - 1, yy, zz - 1, r))
            return true;

        if (!inSphere(xx, yy, zz - 1, r))
            return true;

        if (!inSphere(xx + 1, yy, zz - 1, r))
            return true;

        if (!inSphere(xx - 1, yy + 1, zz - 1, r))
            return true;

        if (!inSphere(xx, yy + 1, zz - 1, r))
            return true;

        if (!inSphere(xx + 1, yy + 1, zz - 1, r))
            return true;

        // Z
        if (!inSphere(xx - 1, yy - 1, zz, r))
            return true;

        if (!inSphere(xx, yy - 1, zz, r))
            return true;

        if (!inSphere(xx + 1, yy - 1, zz, r))
            return true;

        if (!inSphere(xx - 1, yy, zz, r))
            return true;

        if (!inSphere(xx + 1, yy, zz, r))
            return true;

        if (!inSphere(xx - 1, yy + 1, zz, r))
            return true;

        if (!inSphere(xx, yy + 1, zz, r))
            return true;

        if (!inSphere(xx + 1, yy + 1, zz, r))
            return true;

        // Z+1
        if (!inSphere(xx - 1, yy - 1, zz + 1, r))
            return true;

        if (!inSphere(xx, yy - 1, zz + 1, r))
            return true;

        if (!inSphere(xx + 1, yy - 1, zz + 1, r))
            return true;

        if (!inSphere(xx - 1, yy, zz + 1, r))
            return true;

        if (!inSphere(xx, yy, zz + 1, r))
            return true;

        if (!inSphere(xx + 1, yy, zz + 1, r))
            return true;

        if (!inSphere(xx - 1, yy + 1, zz + 1, r))
            return true;

        if (!inSphere(xx, yy + 1, zz + 1, r))
            return true;

        if (!inSphere(xx + 1, yy + 1, zz + 1, r))
            return true;

        return false;

    }
}
