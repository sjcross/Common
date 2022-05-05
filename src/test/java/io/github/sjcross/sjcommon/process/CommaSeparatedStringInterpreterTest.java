package io.github.sjcross.sjcommon.process;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

import io.github.sjcross.sjcommon.process.CommaSeparatedStringInterpreter;

public class CommaSeparatedStringInterpreterTest {
    @Test
    public void testInterpretIntegersAscendingPositiveComplete() {
        String string = "1-3";

        int[] actual = CommaSeparatedStringInterpreter.interpretIntegers(string,true,0);
        int[] expected = new int[]{1,2,3};

        assertArrayEquals(expected,actual);

    }

    @Test
    public void testInterpretIntegersAscendingNegativeStart() {
        String string = "-2-8";

        int[] actual = CommaSeparatedStringInterpreter.interpretIntegers(string,true,0);
        int[] expected = new int[]{-2,-1,0,1,2,3,4,5,6,7,8};

        assertArrayEquals(expected,actual);

    }

    @Test
    public void testInterpretIntegersAscendingNegative() {
        String string = "-5--1";

        int[] actual = CommaSeparatedStringInterpreter.interpretIntegers(string,true,0);
        int[] expected = new int[]{-5,-4,-3,-2,-1};

        assertArrayEquals(expected,actual);

    }

    @Test
    public void testInterpretIntegersAscendingPositiveStartNegativeEnd() {
        String string = "5--2";

        int[] actual = CommaSeparatedStringInterpreter.interpretIntegers(string,true,0);
        int[] expected = new int[]{-2,-1,0,1,2,3,4,5};

        assertArrayEquals(expected,actual);

    }

    @Test
    public void testInterpretIntegersUnorderedPositiveStartNegativeEnd() {
        String string = "5--2";

        int[] actual = CommaSeparatedStringInterpreter.interpretIntegers(string,false,0);
        int[] expected = new int[]{5,4,3,2,1,0,-1,-2};

        assertArrayEquals(expected,actual);

    }

    @Test
    public void testInterpretIntegersAscendingPositiveStartOpenEnd() {
        String string = "5-end";

        int[] actual = CommaSeparatedStringInterpreter.interpretIntegers(string,true,12);
        int[] expected = new int[]{5,6,7,8,9,10,11,12};

        assertArrayEquals(expected,actual);

    }

    @Test
    public void testInterpretIntegersAscendingPositiveGappy() {
        String string = "1-8-3";

        int[] actual = CommaSeparatedStringInterpreter.interpretIntegers(string,true,0);
        int[] expected = new int[]{1,4,7};

        assertArrayEquals(expected,actual);

    }

    @Test
    public void testInterpretIntegersAscendingPositiveGappyNegative() {
        String string = "8-1--3";

        int[] actual = CommaSeparatedStringInterpreter.interpretIntegers(string,true,0);
        int[] expected = new int[]{2,5,8};

        assertArrayEquals(expected,actual);

    }

    @Test
    public void testInterpretIntegersUnorderedPositiveGappyNegative() {
        String string = "8-1--3";

        int[] actual = CommaSeparatedStringInterpreter.interpretIntegers(string,false,0);
        int[] expected = new int[]{8,5,2};

        assertArrayEquals(expected,actual);

    }

    @Test
    public void testInterpretIntegersAscendingPositiveStartOpenEndGappy() {
        String string = "5-end-2";

        int[] actual = CommaSeparatedStringInterpreter.interpretIntegers(string,true,9);
        int[] expected = new int[]{5,7,9};

        assertArrayEquals(expected,actual);

    }

    @Test
    public void testInterpretIntegersAscendingPositiveStartOpenPreEndGappy() {
        String string = "5-(end-3)-2";

        int[] actual = CommaSeparatedStringInterpreter.interpretIntegers(string,true,15);
        int[] expected = new int[]{5,7,9,11};

        assertArrayEquals(expected,actual);

    }
}