package wbif.sjx.common.Object;

import ij.process.LUT;

import java.util.Random;

/**
 * Created by sc13967 on 03/05/2017.
 */
public class RandomLUT {
    private byte[] r;
    private byte[] g;
    private byte[] b;


    // PUBLIC METHODS

    public void generate() {
        Random random = new Random(System.currentTimeMillis());

        r = new byte[256];
        g = new byte[256];
        b = new byte[256];

        random.nextBytes(r);
        random.nextBytes(g);
        random.nextBytes(b);

        r[0] = 2^7-1;
        g[0] = 2^7-1;
        b[0] = 2^7-1;

    }

    public LUT getLUT() {
        if (r==null) generate();

        return new LUT(8,256,r,g,b);

    }


    // GETTERS AND SETTERS

    public byte[] getR() {
        return r;
    }

    public byte[] getG() {
        return g;
    }

    public byte[] getB() {
        return b;
    }
}
