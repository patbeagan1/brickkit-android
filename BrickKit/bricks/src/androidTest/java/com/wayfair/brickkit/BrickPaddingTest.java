package com.wayfair.brickkit;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class BrickPaddingTest {
    private static final int INNER_LEFT = 1;
    private static final int INNER_TOP = 2;
    private static final int INNER_RIGHT = 3;
    private static final int INNER_BOTTOM = 4;
    private static final int OUTER_LEFT = 5;
    private static final int OUTER_TOP = 6;
    private static final int OUTER_RIGHT = 7;
    private static final int OUTER_BOTTOM = 8;

    private TestBrickPadding brickPadding;

    @Before
    public void setup() {
        brickPadding = new TestBrickPadding();
    }

    @Test
    public void testGetInnerLeftPadding() {
        assertEquals(INNER_LEFT, brickPadding.getInnerLeftPadding());
    }

    @Test
    public void testGetInnerTopPadding() {
        assertEquals(INNER_TOP, brickPadding.getInnerTopPadding());
    }

    @Test
    public void testGetInnerRightPadding() {
        assertEquals(INNER_RIGHT, brickPadding.getInnerRightPadding());
    }

    @Test
    public void testGetInnerBottomPadding() {
        assertEquals(INNER_BOTTOM, brickPadding.getInnerBottomPadding());
    }

    @Test
    public void testGetOuterLeftPadding() {
        assertEquals(OUTER_LEFT, brickPadding.getOuterLeftPadding());
    }

    @Test
    public void testGetOuterTopPadding() {
        assertEquals(OUTER_TOP, brickPadding.getOuterTopPadding());
    }

    @Test
    public void testGetOuterRightPadding() {
        assertEquals(OUTER_RIGHT, brickPadding.getOuterRightPadding());
    }

    @Test
    public void testGetOuterBottomPadding() {
        assertEquals(OUTER_BOTTOM, brickPadding.getOuterBottomPadding());
    }

    private static final class TestBrickPadding extends BrickPadding {
        @Override
        protected int innerLeftPadding() {
            return INNER_LEFT;
        }

        @Override
        protected int innerTopPadding() {
            return INNER_TOP;
        }

        @Override
        protected int innerRightPadding() {
            return INNER_RIGHT;
        }

        @Override
        protected int innerBottomPadding() {
            return INNER_BOTTOM;
        }

        @Override
        protected int outerLeftPadding() {
            return OUTER_LEFT;
        }

        @Override
        protected int outerTopPadding() {
            return OUTER_TOP;
        }

        @Override
        protected int outerRightPadding() {
            return OUTER_RIGHT;
        }

        @Override
        protected int outerBottomPadding() {
            return OUTER_BOTTOM;
        }
    }
}
