package com.wayfair.brickkit;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class BrickGridLayoutManagerTest {
    private static final int POSITION = 4;
    private static final int SPAN_COUNT = 3;
    private static final int ORIENTATION = 1;
    private static final boolean REVERSE = false;
    private static final int DEFAULT_SPANS = 1;
    private static final int SPANS = 5;
    private BrickGridLayoutManager layoutManager;
    private BrickDataManager manager;
    private LinkedList<BaseBrick> bricks;

    @Before
    public void setup() {
        manager = mock(BrickDataManager.class);

        layoutManager = new BrickGridLayoutManager(InstrumentationRegistry.getTargetContext(), SPAN_COUNT, manager, ORIENTATION, REVERSE);

        bricks = mock(LinkedList.class);

        when(manager.getRecyclerViewItems()).thenReturn(bricks);
    }

    @Test
    public void testDefaultSpanSize() {
        when(bricks.get(POSITION)).thenReturn(null);

        assertEquals(DEFAULT_SPANS, layoutManager.getSpanSizeLookup().getSpanSize(POSITION));
    }

    @Test
    public void testBrickSpanSize() {
        BrickSize brickSize = mock(BrickSize.class);
        when(brickSize.getSpans(any(Context.class))).thenReturn(SPANS);

        BaseBrick brick = mock(BaseBrick.class);
        when(brick.getSpanSize()).thenReturn(brickSize);

        when(bricks.get(POSITION)).thenReturn(brick);

        when(manager.getRecyclerViewItems()).thenReturn(bricks);

        assertEquals(SPANS, layoutManager.getSpanSizeLookup().getSpanSize(POSITION));
    }
}
