package io.github.sjcross.sjcommon.object.volume;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.github.sjcross.sjcommon.exceptions.IntegerOverflowException;
import io.github.sjcross.sjcommon.object.Point;
import io.github.sjcross.sjcommon.object.volume.PointOutOfRangeException;
import io.github.sjcross.sjcommon.object.volume.QuadtreeCoordinates;

public class QuadtreeCoordinatesTest {
    @Test
    public void testDuplicate() throws IntegerOverflowException, PointOutOfRangeException {
        QuadtreeCoordinates coords = new QuadtreeCoordinates();
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

        QuadtreeCoordinates duplicateCoords = (QuadtreeCoordinates) coords.duplicate();

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
        QuadtreeCoordinates coords = new QuadtreeCoordinates();
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

        QuadtreeCoordinates duplicateCoords = (QuadtreeCoordinates) coords.duplicate();

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
        QuadtreeCoordinates coords = new QuadtreeCoordinates();
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

        QuadtreeCoordinates duplicateCoords = (QuadtreeCoordinates) coords.duplicate();

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
