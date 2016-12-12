package com.wayfair.brickkit.size;

import android.support.test.runner.AndroidJUnit4;

import com.wayfair.brickkit.BrickDataManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class SimpleBrickSizeTest {
    private static final int SIZE = 7;
    private TestSimpleBrickSize brickSize;

    @Before
    public void setup() {
        BrickDataManager manager = mock(BrickDataManager.class);
        brickSize = new TestSimpleBrickSize(manager);
    }

    @Test
    public void testLandscapeTablet() {
        assertEquals(SIZE, brickSize.landscapeTablet());
    }

    @Test
    public void testLandscapePhone() {
        assertEquals(SIZE, brickSize.landscapePhone());
    }

    @Test
    public void testPortraitTablet() {
        assertEquals(SIZE, brickSize.portraitTablet());
    }

    @Test
    public void testPortraitPhone() {
        assertEquals(SIZE, brickSize.portraitPhone());
    }

    private static final class TestSimpleBrickSize extends SimpleBrickSize {
        private TestSimpleBrickSize(BrickDataManager manager) {
            super(manager);
        }

        @Override
        protected int size() {
            return SIZE;
        }
    }
}
