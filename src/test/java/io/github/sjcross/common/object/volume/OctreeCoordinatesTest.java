package io.github.sjcross.common.object.volume;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.github.sjcross.common.exceptions.IntegerOverflowException;
import io.github.sjcross.common.object.Point;

public class OctreeCoordinatesTest {
    @Test
    public void testDuplicate() throws IntegerOverflowException, PointOutOfRangeException {
        OctreeCoordinates coords = new OctreeCoordinates();
        coords.add(1,2,3);
        coords.add(4,3,12);
        coords.add(2,1,2);
        coords.add(1,2,5);
        coords.add(1,2,8);
        coords.add(1,4,8);
        coords.add(2,4,8);
        coords.add(3,4,8);
        coords.add(2,4,2);
        coords.add(2,6,9);

        OctreeCoordinates duplicateCoords = (OctreeCoordinates) coords.duplicate();

        assertTrue(coords.contains(new Point<Integer> (1,2,3)));
        assertTrue(coords.contains(new Point<Integer> (4,3,12)));
        assertTrue(coords.contains(new Point<Integer> (2,1,2)));
        assertTrue(coords.contains(new Point<Integer> (1,2,5)));
        assertTrue(coords.contains(new Point<Integer> (1,2,8)));
        assertTrue(coords.contains(new Point<Integer> (1,4,8)));
        assertTrue(coords.contains(new Point<Integer> (2,4,8)));
        assertTrue(coords.contains(new Point<Integer> (3,4,8)));
        assertTrue(coords.contains(new Point<Integer> (2,4,2)));
        assertTrue(coords.contains(new Point<Integer> (2,6,9)));

        assertTrue(duplicateCoords.contains(new Point<Integer> (1,2,3)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (4,3,12)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (2,1,2)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (1,2,5)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (1,2,8)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (1,4,8)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (2,4,8)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (3,4,8)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (2,4,2)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (2,6,9)));

        assertEquals(10,coords.size());
        assertEquals(10,duplicateCoords.size());

    }

    @Test
    public void testDuplicateEditOriginal() throws IntegerOverflowException, PointOutOfRangeException {
        OctreeCoordinates coords = new OctreeCoordinates();
        coords.add(1,2,3);
        coords.add(4,3,12);
        coords.add(2,1,2);
        coords.add(1,2,5);
        coords.add(1,2,8);
        coords.add(1,4,8);
        coords.add(2,4,8);
        coords.add(3,4,8);
        coords.add(2,4,2);
        coords.add(2,6,9);

        OctreeCoordinates duplicateCoords = (OctreeCoordinates) coords.duplicate();

        // Editing original coordinates, then checking this change has only affected the original set
        coords.remove(new Point<Integer> (1,4,8));
        coords.add(6,2,1);
        coords.add(4,4,2);

        assertTrue(coords.contains(new Point<Integer> (1,2,3)));
        assertTrue(coords.contains(new Point<Integer> (4,3,12)));
        assertTrue(coords.contains(new Point<Integer> (2,1,2)));
        assertTrue(coords.contains(new Point<Integer> (1,2,5)));
        assertTrue(coords.contains(new Point<Integer> (1,2,8)));
        assertTrue(coords.contains(new Point<Integer> (2,4,8)));
        assertTrue(coords.contains(new Point<Integer> (3,4,8)));
        assertTrue(coords.contains(new Point<Integer> (2,4,2)));
        assertTrue(coords.contains(new Point<Integer> (2,6,9)));
        assertTrue(coords.contains(new Point<Integer> (6,2,1)));
        assertTrue(coords.contains(new Point<Integer> (4,4,2)));

        assertTrue(duplicateCoords.contains(new Point<Integer> (1,2,3)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (4,3,12)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (2,1,2)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (1,2,5)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (1,2,8)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (1,4,8)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (2,4,8)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (3,4,8)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (2,4,2)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (2,6,9)));

        assertEquals(11,coords.size());
        assertEquals(10,duplicateCoords.size());

    }

    @Test
    public void testDuplicateEditDuplicate() throws IntegerOverflowException, PointOutOfRangeException {
        OctreeCoordinates coords = new OctreeCoordinates();
        coords.add(1,2,3);
        coords.add(4,3,12);
        coords.add(2,1,2);
        coords.add(1,2,5);
        coords.add(1,2,8);
        coords.add(1,4,8);
        coords.add(2,4,8);
        coords.add(3,4,8);
        coords.add(2,4,2);
        coords.add(2,6,9);

        OctreeCoordinates duplicateCoords = (OctreeCoordinates) coords.duplicate();

        // Editing duplicate coordinates, then checking this change has only affected the duplicate set
        duplicateCoords.remove(new Point<Integer> (1,4,8));
        duplicateCoords.add(6,2,1);
        duplicateCoords.add(4,4,2);

        assertTrue(coords.contains(new Point<Integer> (1,2,3)));
        assertTrue(coords.contains(new Point<Integer> (4,3,12)));
        assertTrue(coords.contains(new Point<Integer> (2,1,2)));
        assertTrue(coords.contains(new Point<Integer> (1,2,5)));
        assertTrue(coords.contains(new Point<Integer> (1,2,8)));
        assertTrue(coords.contains(new Point<Integer> (1,4,8)));
        assertTrue(coords.contains(new Point<Integer> (2,4,8)));
        assertTrue(coords.contains(new Point<Integer> (3,4,8)));
        assertTrue(coords.contains(new Point<Integer> (2,4,2)));
        assertTrue(coords.contains(new Point<Integer> (2,6,9)));

        assertTrue(duplicateCoords.contains(new Point<Integer> (1,2,3)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (4,3,12)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (2,1,2)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (1,2,5)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (1,2,8)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (2,4,8)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (3,4,8)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (2,4,2)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (2,6,9)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (6,2,1)));
        assertTrue(duplicateCoords.contains(new Point<Integer> (4,4,2)));

        assertEquals(10,coords.size());
        assertEquals(11,duplicateCoords.size());

    }
}
