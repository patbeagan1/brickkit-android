package com.wayfair.brickkit.size;

import android.support.test.runner.AndroidJUnit4;

import com.wayfair.brickkit.BrickDataManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class DeviceBrickSizeTest {
    private static final int TABLET_SIZE = 7;
    private static final int PHONE_SIZE = 3;

    private TestDeviceBrickSize brickSize;

    @Before
    public void setup() {
        BrickDataManager manager = mock(BrickDataManager.class);
        brickSize = new TestDeviceBrickSize(manager);
    }

    @Test
    public void testLandscapeTablet() {
        assertEquals(TABLET_SIZE, brickSize.landscapeTablet());
    }

    @Test
    public void testLandscapePhone() {
        assertEquals(PHONE_SIZE, brickSize.landscapePhone());
    }

    @Test
    public void testPortraitTablet() {
        assertEquals(TABLET_SIZE, brickSize.portraitTablet());
    }

    @Test
    public void testPortraitPhone() {
        assertEquals(PHONE_SIZE, brickSize.portraitPhone());
    }

    private static final class TestDeviceBrickSize extends DeviceBrickSize {
        private TestDeviceBrickSize(BrickDataManager manager) {
            super(manager);
        }

        @Override
        protected int tablet() {
            return TABLET_SIZE;
        }

        @Override
        protected int phone() {
            return PHONE_SIZE;
        }
    }
}
