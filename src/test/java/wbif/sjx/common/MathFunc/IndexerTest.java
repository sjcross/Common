package wbif.sjx.common.MathFunc;

import org.junit.Test;

import static org.junit.Assert.*;

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

        assertEquals("First element",0,indexer.getIndex(new int[]{0,0}));
        assertEquals("Mid-array element",38,indexer.getIndex(new int[]{3,5}));
        assertEquals("Final element",90,indexer.getIndex(new int[]{6,12}));
        assertEquals("X-value larger than accumulatorIndexer size",-1,indexer.getIndex(new int[]{7,0}));
        assertEquals("Y-value less than zero",-1,indexer.getIndex(new int[]{4,-5}));

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

        assertEquals("First element",0,indexer.getIndex(new int[]{0,0,0}));
        assertEquals("Mid-array element",311,indexer.getIndex(new int[]{3,5,3}));
        assertEquals("Final element",545,indexer.getIndex(new int[]{6,12,5}));
        assertEquals("X-value larger than accumulatorIndexer size",-1,indexer.getIndex(new int[]{7,0,0}));
        assertEquals("Y-value less than zero",-1,indexer.getIndex(new int[]{4,-5,3}));

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

        assertEquals("First element",0,indexer.getIndex(new int[]{0,0,0}));
        assertEquals("Mid-array element",311,indexer.getIndex(new int[]{3,5,3}));
        assertEquals("Final element",545,indexer.getIndex(new int[]{6,12,5}));
        assertEquals("X-value larger than accumulatorIndexer size",-1,indexer.getIndex(new int[]{7,0,0}));
        assertEquals("Y-value less than zero",-1,indexer.getIndex(new int[]{4,-5,3}));

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

        assertArrayEquals("First element",new int[]{0,0},indexer.getCoord(0));
        assertArrayEquals("Mid-array element",new int[]{3,5},indexer.getCoord(38));
        assertArrayEquals("Final element",new int[]{6,12},indexer.getCoord(90));
        assertArrayEquals("Index smaller than zero",null,indexer.getCoord(-1));
        assertArrayEquals("Index larger than accumulatorIndexer size",null,indexer.getCoord(1000));

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

        assertArrayEquals("First element",new int[]{0,0,0},indexer.getCoord(0));
        assertArrayEquals("Mid-array element",new int[]{3,5,3},indexer.getCoord(311));
        assertArrayEquals("Final element",new int[]{6,12,5},indexer.getCoord(545));
        assertArrayEquals("Index smaller than zero",null,indexer.getCoord(-1));
        assertArrayEquals("Index larger than accumulatorIndexer size",null,indexer.getCoord(1000));

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

        assertArrayEquals("First element",new int[]{0,0,0},indexer.getCoord(0));
        assertArrayEquals("Mid-array element",new int[]{3,5,3},indexer.getCoord(311));
        assertArrayEquals("Final element",new int[]{6,12,5},indexer.getCoord(545));
        assertArrayEquals("Index smaller than zero",null,indexer.getCoord(-1));
        assertArrayEquals("Index larger than accumulatorIndexer size",null,indexer.getCoord(1000));

    }
}