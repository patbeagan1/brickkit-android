package com.wayfair.brickkit;

import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class BrickFragmentTest {

    private TestBrickFragment testBrickFragment;
    private LayoutInflater inflater;

    @Before
    public void setup() {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        testBrickFragment = new TestBrickFragment();
        inflater = LayoutInflater.from(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void onCreate() {
        testBrickFragment.onCreate(new Bundle());

    }

    @Test
    public void testOnCreateView() {
        testBrickFragment.setOrientation(OrientationHelper.HORIZONTAL);
        View view = testBrickFragment.onCreateView(inflater, null, null);
        assertNotNull(view);
        assertTrue(testBrickFragment.createBricksCalled);

        testBrickFragment.setOrientation(OrientationHelper.VERTICAL);
        view = testBrickFragment.onCreateView(inflater, null, null);
        assertNotNull(view);
        assertTrue(testBrickFragment.createBricksCalled);
    }

    @Test
    public void testOnDestroyView() {
        View view = testBrickFragment.onCreateView(inflater, null, null);
        assertNotNull(view);
        testBrickFragment.onDestroyView();
    }

    @Test
    public void testOrientation() {
        int orientation = (testBrickFragment.getDefaultOrientation());
        assertEquals(orientation, GridLayoutManager.VERTICAL);
    }

    @Test
    public void testReverse() {
        boolean isReverse = testBrickFragment.reverse();
        assertEquals(isReverse, false);
    }

    public static final class TestBrickFragment extends BrickFragment {
        private int orientation;
        private boolean createBricksCalled;

        public void setOrientation(int orientation) {
            this.orientation = orientation;
        }

        @Override
        public int maxSpans() {
            return super.maxSpans();
        }

        @Override
        public int orientation() {
            return orientation;
        }

        @Override
        public boolean reverse() {
            return super.reverse();
        }

        @Override
        public void createBricks() {
            createBricksCalled = true;
        }

        public int getDefaultOrientation(){
            return super.orientation();
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }
    }
}
