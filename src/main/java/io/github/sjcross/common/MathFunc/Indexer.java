package io.github.sjcross.common.MathFunc;

/**
 * Multidimensional indexing
 * Created by sc13967 on 15/08/2016.
 */

public class Indexer {
    private int[] dim;
    private int[] dim_m;

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

        dim[0] = dim_x;
        dim[1] = dim_y;

        dim_m[0] = 1;
        dim_m[1] = dim[0];
    }

    /**
     * Constructor for index of 3 dimensions
     */
    public Indexer(int dim_x, int dim_y, int dim_z) {
        dim_m = new int[3];
        dim = new int[3];
        dim[0] = dim_x;
        dim[1] = dim_y;
        dim[2] = dim_z;

        dim_m[0] = 1;
        dim_m[1] = dim_x;
        dim_m[2] = dim_x*dim_y;
    }

    /**
     * Calculates a single-valued index for the provided coordinates.  If the coordinates are outside the initialised
     * size a value of -1 is returned.
     * @param coord
     * @return
     */
    public int getIndex(int[] coord) {
        // Verifying the provided coordinates aren't outside the specified dimensions
        for (int i=0;i<coord.length;i++) {
            if (coord[i] < 0 | coord[i] >= dim[i]) {
                return -1;

            }
        }

        //Adding each coordinate (multiplied by the relevant dimension) to ind
        int ind = 0;
        for (int i=0;i<dim_m.length;i++) {
            ind += coord[i]*dim_m[i];
        }

        return ind;
    }

    public int[] getCoord(int ind) {
        // Verifying the index is within the accumulatorIndexer size
        int indexerSize = 1;
        for (int i=0;i<dim.length;i++) {
            indexerSize *= dim[i];
        }

        if (ind < 0 | ind > indexerSize) return null;

        int[] coord = new int[dim_m.length];
        coord[0] = ind%dim_m[1];
        for (int i=1;i<dim_m.length;i++) {
            coord[i] = (int) Math.floor(ind/dim_m[i])%dim[i];
        }

        return coord;
    }

    public int getLength() {
        int size = 1;
        for (int dimension:dim) size *= dimension;
        return size;

    }

    public int[] getDim() {
        return dim;
    }
}