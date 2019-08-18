package wbif.sjx.common.MathFunc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by steph on 29/05/2017.
 */
public class IndexerTest {
    /**
     * Testing the accumulatorIndexer as constructed explicitly for 2D
     * @throws Exception
     */
    @Test
    public void testGetIndexFor2D() throws Exception {
        int width = 7;
        int height = 13;

        Indexer indexer = new Indexer(width,height);

        assertEquals(0,indexer.getIndex(new int[]{0,0}));
        assertEquals(38,indexer.getIndex(new int[]{3,5}));
        assertEquals(90,indexer.getIndex(new int[]{6,12}));
        assertEquals(-1,indexer.getIndex(new int[]{7,0}));
        assertEquals(-1,indexer.getIndex(new int[]{4,-5}));

    }

    /**
     * Testing the accumulatorIndexer as constructed explicitly for 3D
     * @throws Exception
     */
    @Test
    public void testGetIndexFor3D() throws Exception {
        int width = 7;
        int height = 13;
        int depth = 6;

        Indexer indexer = new Indexer(width,height,depth);

        assertEquals(0,indexer.getIndex(new int[]{0,0,0}));
        assertEquals(311,indexer.getIndex(new int[]{3,5,3}));
        assertEquals(545,indexer.getIndex(new int[]{6,12,5}));
        assertEquals(-1,indexer.getIndex(new int[]{7,0,0}));
        assertEquals(-1,indexer.getIndex(new int[]{4,-5,3}));

    }

    /**
     * Testing the accumulatorIndexer as constructed generically for 3D
     * @throws Exception
     */
    @Test
    public void testGetIndexForGeneric() throws Exception {
        int width = 7;
        int height = 13;
        int depth = 6;

        Indexer indexer = new Indexer(new int[]{width,height,depth});

        assertEquals(0,indexer.getIndex(new int[]{0,0,0}));
        assertEquals(311,indexer.getIndex(new int[]{3,5,3}));
        assertEquals(545,indexer.getIndex(new int[]{6,12,5}));
        assertEquals(-1,indexer.getIndex(new int[]{7,0,0}));
        assertEquals(-1,indexer.getIndex(new int[]{4,-5,3}));

    }

    /**
     * Testing the reverse operation (getting coordinates from an index) for accumulatorIndexer explicitly constructed in 2D
     * @throws Exception
     */
    @Test
    public void testGetCoordFor2D() throws Exception {
        int width = 7;
        int height = 13;

        Indexer indexer = new Indexer(width,height);

        assertArrayEquals(new int[]{0,0},indexer.getCoord(0));
        assertArrayEquals(new int[]{3,5},indexer.getCoord(38));
        assertArrayEquals(new int[]{6,12},indexer.getCoord(90));
        assertArrayEquals(null,indexer.getCoord(-1));
        assertArrayEquals(null,indexer.getCoord(1000));

    }

    /**
     * Testing the reverse operation (getting coordinates from an index) for accumulatorIndexer explicitly constructed in 3D
     * @throws Exception
     */
    @Test
    public void testGetCoordFor3D() throws Exception {
        int width = 7;
        int height = 13;
        int depth = 6;

        Indexer indexer = new Indexer(width,height,depth);

        assertArrayEquals(new int[]{0,0,0},indexer.getCoord(0));
        assertArrayEquals(new int[]{3,5,3},indexer.getCoord(311));
        assertArrayEquals(new int[]{6,12,5},indexer.getCoord(545));
        assertArrayEquals(null,indexer.getCoord(-1));
        assertArrayEquals(null,indexer.getCoord(1000));

    }

    /**
     * Testing the reverse operation (getting coordinates from an index) for accumulatorIndexer explicitly constructed in 3D
     * @throws Exception
     */
    @Test
    public void testGetCoordForGeneric() throws Exception {
        int width = 7;
        int height = 13;
        int depth = 6;

        Indexer indexer = new Indexer(new int[]{width,height,depth});

        assertArrayEquals(new int[]{0,0,0},indexer.getCoord(0));
        assertArrayEquals(new int[]{3,5,3},indexer.getCoord(311));
        assertArrayEquals(new int[]{6,12,5},indexer.getCoord(545));
        assertArrayEquals(null,indexer.getCoord(-1));
        assertArrayEquals(null,indexer.getCoord(1000));

    }
}