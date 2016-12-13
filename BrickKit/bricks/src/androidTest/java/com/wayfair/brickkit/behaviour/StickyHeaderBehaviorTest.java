package com.wayfair.brickkit.behaviour;

import android.content.Context;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.wayfair.brickkit.BrickDataManager;
import com.wayfair.brickkit.behavior.StickyHeaderBehavior;
import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.util.BrickTestHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class StickyHeaderBehaviorTest {

    private static final int MAX_SPANS = 8;

    private BrickDataManager dataManager;
    private BrickTestHelper brickTestHelper;
    private TestStickyHeaderBehavior headerBehavior;

    @Before
    public void setup() {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }

        Context context = InstrumentationRegistry.getTargetContext();
        RecyclerView recyclerView = new RecyclerView(context);

        dataManager = new BrickDataManager(context, recyclerView, MAX_SPANS, OrientationHelper.VERTICAL, false);
        brickTestHelper = new BrickTestHelper(context);
        headerBehavior = new TestStickyHeaderBehavior(dataManager);
    }

    @Test
    public void testGetStickyViewPosition() {
        BaseBrick brick = brickTestHelper.generateBrick();
        brick.setHeader(true);
        dataManager.addLast(brick);
        for (int i = 0; i < 9; i++) {
            dataManager.addLast(brickTestHelper.generateBrick());
        }

        int position = headerBehavior.getStickyViewPosition(RecyclerView.NO_POSITION);
        assertEquals(position, -1);
        position = headerBehavior.getStickyViewPosition(5);
        assertEquals(position, 0);
    }

    @Test
    public void testTranslateStickyView() {
        headerBehavior.translateStickyView();
        for (int i = 0; i < 9; i++) {
            dataManager.addLast(brickTestHelper.generateBrick());
        }
        BaseBrick brick = brickTestHelper.generateBrick();
        brick.setFooter(true);
        dataManager.addLast(brick);
        //headerBehavior.onScroll();      //needs Activity context
        //headerBehavior.onDataSetChanged();
        //headerBehavior.translateStickyView();
    }

    public static final class TestStickyHeaderBehavior extends StickyHeaderBehavior {
        TestStickyHeaderBehavior(BrickDataManager brickDataManager) {
            super(brickDataManager);
        }

        @Override
        protected int getStickyViewPosition(int adapterPosHere) {
            return super.getStickyViewPosition(adapterPosHere);
        }

        @Override
        protected void translateStickyView() {
            super.translateStickyView();
        }

        @Override
        public void onScroll() {
            super.onScroll();
        }
    }

}


