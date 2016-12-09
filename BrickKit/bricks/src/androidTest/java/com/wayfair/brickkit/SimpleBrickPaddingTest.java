package com.wayfair.brickkit;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class SimpleBrickPaddingTest {
    private static final int PADDING = 2;

    private SimpleBrickPaddingTest.TestSimpleBrickPadding brickPadding;

    @Before
    public void setup() {
        brickPadding = new SimpleBrickPaddingTest.TestSimpleBrickPadding();
    }

    @Test
    public void testGetInnerLeftPadding() {
        assertEquals(PADDING, brickPadding.getInnerLeftPadding());
    }

    @Test
    public void testGetInnerTopPadding() {
        assertEquals(PADDING, brickPadding.getInnerTopPadding());
    }

    @Test
    public void testGetInnerRightPadding() {
        assertEquals(PADDING, brickPadding.getInnerRightPadding());
    }

    @Test
    public void testGetInnerBottomPadding() {
        assertEquals(PADDING, brickPadding.getInnerBottomPadding());
    }

    @Test
    public void testGetOuterLeftPadding() {
        assertEquals(PADDING, brickPadding.getOuterLeftPadding());
    }

    @Test
    public void testGetOuterTopPadding() {
        assertEquals(PADDING, brickPadding.getOuterTopPadding());
    }

    @Test
    public void testGetOuterRightPadding() {
        assertEquals(PADDING, brickPadding.getOuterRightPadding());
    }

    @Test
    public void testGetOuterBottomPadding() {
        assertEquals(PADDING, brickPadding.getOuterBottomPadding());
    }

    private static final class TestSimpleBrickPadding extends SimpleBrickPadding {
        @Override
        protected int padding() {
            return PADDING;
        }
    }
}
