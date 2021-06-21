
package wbif.sjx.common.Object.Voxels;

// Implementation of the midpoint circle algorithm
// Described at https://en.wikipedia.org/wiki/Midpoint_circle_algorithm
// (accessed 12-07-2016)

import java.util.ArrayList;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.ImageStack;

public class MidpointSphere {
    int[] x_oct;
    int[] y_oct;
    int[] z_oct;

    public static void main(String[] args) {
        MidpointSphere midpointSphere = new MidpointSphere(45);
        int[] x = midpointSphere.getXSphere();
        int[] y = midpointSphere.getYSphere();
        int[] z = midpointSphere.getZSphere();

        new ImageJ();
        ImagePlus ipl = IJ.createImage("", 100, 100, 100, 8);
        ImageStack ist = ipl.getStack();
        for (int i = 0; i < x.length; i++) {
            ist.setVoxel(x[i] + 50, y[i] + 50, z[i] + 50, 255);
        }
        ipl.show();
    }

    public MidpointSphere(int r) {
        ArrayList<Integer> x_temp = new ArrayList<Integer>();
        ArrayList<Integer> y_temp = new ArrayList<Integer>();
        ArrayList<Integer> z_temp = new ArrayList<Integer>();

        int prev_r = r;
        for (int z = 0; z <= r; z++) {
            int start_r = (int) Math.round(Math.sqrt(r * r - z * z));
            int curr_r = start_r;
            do {
                int x = curr_r;
                int y = 0;
                int dec = 1 - x;

                while (x >= y) {
                    x_temp.add(x);
                    y_temp.add(y);
                    z_temp.add(z);
                    x_temp.add(x);
                    y_temp.add(y);
                    z_temp.add(-z);

                    y++;

                    if (dec <= 0) {
                        dec += 2 * y + 1;
                    } else {
                        x--;
                        dec += 2 * (y - x) + 1;
                    }
                }

                curr_r++;

            } while (curr_r < prev_r);
            prev_r = start_r;
        }

        x_oct = new int[x_temp.size()];
        y_oct = new int[y_temp.size()];
        z_oct = new int[y_temp.size()];

        for (int i = 0; i < x_temp.size(); i++) {
            x_oct[i] = x_temp.get(i);
            y_oct[i] = y_temp.get(i);
            z_oct[i] = z_temp.get(i);
        }
    }

    public int[] getXOct() {
        return x_oct;
    }

    public int[] getYOct() {
        return y_oct;
    }

    public int[] getZOct() {
        return z_oct;
    }

    public int[] getXQuad() {
        int st = 0;
        int l = x_oct.length * 2;
        if (x_oct[x_oct.length - 1] == y_oct[y_oct.length - 1]) {
            st = 1;
            l = x_oct.length * 2 - 1;
        }
        int[] x_quad = new int[l];

        for (int i = 0; i < x_oct.length; i++)
            x_quad[i] = x_oct[i];

        for (int i = st; i < x_oct.length; i++)
            x_quad[i - st + x_oct.length] = y_oct[y_oct.length - 1 - i];

        return x_quad;
    }

    public int[] getYQuad() {
        int st = 0;
        int l = y_oct.length * 2;
        if (x_oct[x_oct.length - 1] == y_oct[y_oct.length - 1]) {
            st = 1;
            l = y_oct.length * 2 - 1;
        }
        int[] y_quad = new int[l];

        for (int i = 0; i < y_oct.length; i++)
            y_quad[i] = y_oct[i];

        for (int i = st; i < y_oct.length; i++)
            y_quad[i - st + y_oct.length] = x_oct[x_oct.length - 1 - i];

        return y_quad;
    }

    public int[] getZQuad() {
        int st = 0;
        int l = y_oct.length * 2;
        if (x_oct[x_oct.length - 1] == y_oct[y_oct.length - 1]) {
            st = 1;
            l = y_oct.length * 2 - 1;
        }
        int[] z_quad = new int[l];

        for (int i = 0; i < z_oct.length; i++)
            z_quad[i] = z_oct[i];

        for (int i = st; i < z_oct.length; i++)
            z_quad[i - st + z_oct.length] = z_oct[z_oct.length - 1 - i];

        return z_quad;

    }

    public int[] getXHalf() {
        int[] x_quad = getXQuad();
        int[] x_half = new int[x_quad.length * 2 - 1];

        for (int i = 0; i < x_quad.length; i++)
            x_half[i] = x_quad[x_quad.length - 1 - i];

        for (int i = 1; i < x_quad.length; i++)
            x_half[i - 1 + x_quad.length] = x_quad[i];

        return x_half;

    }

    public int[] getYHalf() {
        int[] y_quad = getYQuad();
        int[] y_half = new int[y_quad.length * 2 - 1];

        for (int i = 0; i < y_quad.length; i++)
            y_half[i] = y_quad[y_quad.length - 1 - i];

        for (int i = 1; i < y_quad.length; i++)
            y_half[i - 1 + y_quad.length] = -y_quad[i];

        return y_half;

    }

    public int[] getZHalf() {
        int[] z_quad = getZQuad();
        int[] z_half = new int[z_quad.length * 2 - 1];

        for (int i = 0; i < z_quad.length; i++)
            z_half[i] = z_quad[z_quad.length - 1 - i];

        for (int i = 1; i < z_quad.length; i++)
            z_half[i - 1 + z_quad.length] = z_quad[i];

        return z_half;

    }

    public int[] getXSphere() {
        int[] x_half = getXHalf();
        int[] x_circ = new int[x_half.length * 2 - 2];

        for (int i = 0; i < x_half.length; i++)
            x_circ[i] = x_half[x_half.length - 1 - i];
        
        for (int i = 1; i < x_half.length - 1; i++)
            x_circ[i - 1 + x_half.length] = -x_half[i];
        
        return x_circ;

    }

    public int[] getYSphere() {
        int[] y_half = getYHalf();
        int[] y_circ = new int[y_half.length * 2 - 2];

        for (int i = 0; i < y_half.length; i++)
            y_circ[i] = y_half[y_half.length - 1 - i];
        
        for (int i = 1; i < y_half.length - 1; i++)
            y_circ[i - 1 + y_half.length] = y_half[i];
        
        return y_circ;
    }

    public int[] getZSphere() {
        int[] z_half = getZHalf();
        int[] z_circ = new int[z_half.length * 2 - 2];

        for (int i = 0; i < z_half.length; i++)
            z_circ[i] = z_half[z_half.length - 1 - i];
        
        for (int i = 1; i < z_half.length - 1; i++)
            z_circ[i - 1 + z_half.length] = z_half[i];
        
        return z_circ;

    }

    public int[][] getSphere() {
		int[] x_sph = getXSphere();
		int[] y_sph = getYSphere();
        int[] z_sph = getZSphere();

		int[][] sph = new int[3][x_sph.length];
		for (int i=0;i<x_sph.length;i++) {
			sph[0][i] = x_sph[i];
			sph[1][i] = y_sph[i];
            sph[2][i] = z_sph[i];
		}

		return sph;

	}
}