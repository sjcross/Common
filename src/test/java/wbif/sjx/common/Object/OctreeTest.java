package wbif.sjx.common.Object;

import org.junit.Test;
import wbif.sjx.common.ExpectedObjects.BigBlob2D;

import java.util.List;

import static org.junit.Assert.*;

public class OctreeTest {
    @Test
    public void exampleTest() {
        BigBlob2D bigBlob2D = new BigBlob2D();
        List<Integer[]> coords = bigBlob2D.getCoordinates5D();

        coords.forEach(v -> System.out.println("x = "+v[2]+", y = "+v[3]));

        assertNotNull(coords);

    }
}
