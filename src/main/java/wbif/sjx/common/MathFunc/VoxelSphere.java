package wbif.sjx.common.MathFunc;

import java.util.ArrayList;

public class VoxelSphere {
    private ArrayList<Integer> x = new ArrayList<>();
    private ArrayList<Integer> y = new ArrayList<>();
    private ArrayList<Integer> z = new ArrayList<>();

    public VoxelSphere(int r) {
        for (int xx = -r; xx < r; xx++) {
            for (int yy = -r; yy < r; yy++) {
                for (int zz= -r; zz < r; zz++) {    
                    if (Math.sqrt(xx*xx+yy*yy+zz*zz) <= r) {
                        x.add(xx);
                        y.add(yy);
                        z.add(zz);
                    }
                }
            }
        }
    }

    public int[] getXSphereFill() {
        int[] xx = new int[x.size()];
        int i=0;
        for (int xxx:x)
            xx[i++] = xxx;

        return xx;

    }

    public int[] getYSphereFill() {
        int[] yy = new int[y.size()];
        int i=0;
        for (int yyy:y)
            yy[i++] = yyy;

        return yy;

    }

    public int[] getZSphereFill() {
        int[] zz = new int[z.size()];
        int i=0;
        for (int zzz:z)
            zz[i++] = zzz;

        return zz;

    }
}
