package wbif.sjx.common.MathFunc;

/**
 * Multidimensional indexing
 * Created by sc13967 on 15/08/2016.
 */

public class Indexer {
    int[] dim;
    int[] dim_m;

    /**
     * Generic constructor for index of arbitrary dimension
     * @param dim Array containing dimensions (x,y,z,...)
     */
    public Indexer(int[] dim) {
        this.dim = dim;
        dim_m = new int[this.dim.length];

        dim_m[0] = 1;
        for (int i=1;i<this.dim.length;i++) {
            dim_m[i] = dim_m[i-1]*this.dim[i-1];
        }
    }

    /**
     * Constructor for index of 2 dimensions
     */
    public Indexer(int dim_x, int dim_y) {
        dim_m = new int[2];
        dim = new int[2];

        this.dim[0] = dim_x;
        this.dim[1] = dim_y;

        dim_m[0] = 1;
        dim_m[1] = this.dim[0];
    }

    /**
     * Constructor for index of 3 dimensions
     */
    public Indexer(int dim_x, int dim_y, int dim_z) {
        dim_m = new int[3];
        dim = new int[3];
        this.dim[0] = dim_x;
        this.dim[1] = dim_y;
        this.dim[2] = dim_z;

        dim_m[0] = 1;
        dim_m[1] = dim_x;
        dim_m[2] = dim_x*dim_y;
    }

    public int getIndex(int[] coord) {
        int ind = 0;

        //Adding each coordinate (multiplied by the relevant dimension) to ind
        for (int i=0;i<dim_m.length;i++) {
            ind += coord[i]*dim_m[i];
        }

        return ind;
    }

    public int[] getCoord(int ind) {
        int[] coord = new int[dim_m.length];

        coord[0] = ind%dim_m[1];
        for (int i=1;i<dim_m.length;i++) {
            coord[i] = (int) Math.floor(ind/dim_m[i])%dim[i];
        }

        return coord;
    }
}