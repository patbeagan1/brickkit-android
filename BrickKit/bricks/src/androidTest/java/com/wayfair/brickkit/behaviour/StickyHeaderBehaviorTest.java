package com.wayfair.brickkit.behaviour;

import android.content.Context;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.wayfair.brickkit.BrickDataManager;
import com.wayfair.brickkit.BrickRecyclerAdapter;
import com.wayfair.brickkit.BrickViewHolder;
import com.wayfair.brickkit.R;
import com.wayfair.brickkit.behavior.StickyHeaderBehavior;
import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.padding.BrickPadding;
import com.wayfair.brickkit.util.BrickTestHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class StickyHeaderBehaviorTest {

    private TestStickyHeaderBehavior headerBehavior;
    private BrickDataManager dataManager;
    private BrickTestHelper brickTestHelper;
    private RecyclerView recyclerView;
    private BrickRecyclerAdapter adapter;
    private Context context;
    private View view;
    private View textBrickView;
    private ViewGroup stickyHolderLayout;

    private static final int INNER_LEFT = 1;
    private static final int INNER_TOP = 2;
    private static final int INNER_RIGHT = 3;
    private static final int INNER_BOTTOM = 4;
    private static final int OUTER_LEFT = 5;
    private static final int OUTER_TOP = 6;
    private static final int OUTER_RIGHT = 7;
    private static final int OUTER_BOTTOM = 8;

    @Before
    public void setup() {

        BaseBrick header;
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }

        context = InstrumentationRegistry.getTargetContext();
        recyclerView = mock(RecyclerView.class);
        when(recyclerView.isComputingLayout()).thenReturn(false);
        adapter = mock(BrickRecyclerAdapter.class);
        header = mock(BaseBrick.class);
        view = mock(View.class);

        when(adapter.getRecyclerView()).thenReturn(recyclerView);
        when(adapter.getItemCount()).thenReturn(30);
        when(adapter.getRecyclerView().getChildCount()).thenReturn(10);
        for (int i = 0; i < 30; i++) {
            when(adapter.getRecyclerView().getChildAt(i)).thenReturn(view);
        }

        when(adapter.getRecyclerView().getChildAdapterPosition(view)).thenReturn(0);
        for (int i = 0; i < 30; i++) {
            when(adapter.getSectionHeader(i)).thenReturn(header);
        }

        when(adapter.indexOf(header)).thenReturn(0);
        when(header.getLayout()).thenReturn(R.layout.text_brick);
        when(adapter.getItemViewType(0)).thenReturn(R.layout.text_brick);
        dataManager = mock(BrickDataManager.class);
        when(dataManager.getBrickRecyclerAdapter()).thenReturn(adapter);
        when(dataManager.brickAtPosition(0)).thenReturn(header);
        when(dataManager.brickAtPosition(9)).thenReturn(header);

        LayoutInflater inflater = LayoutInflater.from(context);
        final ViewGroup nullParent = null;
        View fragmentBrick = inflater.inflate(R.layout.vertical_fragment_brick, nullParent);
        textBrickView = inflater.inflate(R.layout.text_brick, nullParent);
        stickyHolderLayout = (ViewGroup) fragmentBrick.findViewById(R.id.sticky_header_container);

        textBrickView.setLayoutParams(
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        BrickViewHolder holder = new BrickViewHolder(textBrickView);
        when(adapter.onCreateViewHolder(null, R.layout.text_brick)).thenReturn(holder);
        when(adapter.createViewHolder(null, R.layout.text_brick)).thenReturn(holder);

        when(adapter.onCreateViewHolder(recyclerView, R.layout.text_brick)).thenReturn(holder);
        when(adapter.createViewHolder(recyclerView, R.layout.text_brick)).thenReturn(holder);

        brickTestHelper = new BrickTestHelper(context);

        BrickPadding brickPadding = mock(BrickPadding.class);
        when(brickPadding.getInnerLeftPadding()).thenReturn(INNER_LEFT);
        when(brickPadding.getInnerTopPadding()).thenReturn(INNER_TOP);
        when(brickPadding.getInnerRightPadding()).thenReturn(INNER_RIGHT);
        when(brickPadding.getInnerBottomPadding()).thenReturn(INNER_BOTTOM);
        when(brickPadding.getOuterLeftPadding()).thenReturn(OUTER_LEFT);
        when(brickPadding.getOuterTopPadding()).thenReturn(OUTER_TOP);
        when(brickPadding.getOuterRightPadding()).thenReturn(OUTER_RIGHT);
        when(brickPadding.getOuterBottomPadding()).thenReturn(OUTER_BOTTOM);
        when(dataManager.brickAtPosition(0).getPadding()).thenReturn(brickPadding);
        when(dataManager.brickAtPosition(9).getPadding()).thenReturn(brickPadding);

        headerBehavior = new TestStickyHeaderBehavior(dataManager);
        headerBehavior = new TestStickyHeaderBehavior(dataManager, stickyHolderLayout);
    }

    @Test
    public void testGetStickyViewPosition() {
        BaseBrick brick = brickTestHelper.generateBrick();
        brick.setHeader(true);
        dataManager.addLast(brick);
        for (int i = 0; i < 10; i++) {
            dataManager.addLast(brickTestHelper.generateBrick());
        }

        int position = headerBehavior.getStickyViewPosition(RecyclerView.NO_POSITION);
        assertEquals(position, 0);
        position = headerBehavior.getStickyViewPosition(5);
        assertEquals(position, 0);
    }

    @Test
    public void testTranslateStickyView() {
        headerBehavior.translateStickyView();

        for (int i = 0; i < 30; i++) {
            dataManager.addLast(brickTestHelper.generateBrick());
        }
        BaseBrick brick = brickTestHelper.generateBrick();
        brick.setHeader(true);
        dataManager.addLast(brick);
        DummyLayoutManager layoutManager = new DummyLayoutManager(context);
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        when(recyclerView.getLayoutManager()).thenReturn(layoutManager);


        for (int i = 0; i < 30; i++) {
            when(adapter.getRecyclerView().getChildAt(i)).thenReturn(textBrickView);
        }
        headerBehavior.onScroll();
        headerBehavior.onDataSetChanged();

        when(adapter.getRecyclerView().getChildAdapterPosition(view)).thenReturn(0);
        when(adapter.getSectionHeader(0)).thenReturn(null);
        headerBehavior.translateStickyView();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        when(recyclerView.getLayoutManager()).thenReturn(gridLayoutManager);
        headerBehavior.translateStickyView();
    }

    @Test
    public void testOnScrolledForNullHolderLayout() {
        for (int i = 0; i < 10; i++) {
            dataManager.addLast(brickTestHelper.generateBrick());
        }

        headerBehavior = new TestStickyHeaderBehavior(dataManager, null);
        headerBehavior.onScrolled(recyclerView, 10, 10);
        headerBehavior.onScroll();
        assertNull(headerBehavior.getStickyViewHolder());
    }

    @Test
    public void testStickyViewFadeTranslate() {
        for (int i = 0; i < 10; i++) {
            dataManager.addLast(brickTestHelper.generateBrick());
        }
        headerBehavior = new TestStickyHeaderBehavior(dataManager, stickyHolderLayout);
        headerBehavior.stickyViewFadeTranslate(1);
    }

    public static final class TestStickyHeaderBehavior extends StickyHeaderBehavior {
        TestStickyHeaderBehavior(BrickDataManager brickDataManager) {
            super(brickDataManager);
        }

        TestStickyHeaderBehavior(BrickDataManager brickDataManager, ViewGroup stickyHolderLayout) {
            super(brickDataManager, stickyHolderLayout);
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
        protected void stickyViewFadeTranslate(int dy) {
            super.stickyViewFadeTranslate(dy);
        }
    }

    private final class DummyLayoutManager extends LinearLayoutManager {
        DummyLayoutManager(Context context) {
            super(context);
        }
    }

}


