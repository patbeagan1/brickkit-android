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
import com.wayfair.brickkit.behavior.StickyFooterBehavior;
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
public class StickyFooterBehaviorTest {

    private BrickDataManager dataManager;
    private BrickTestHelper brickTestHelper;
    private TestStickyFooterBehavior footerBehavior;
    private RecyclerView recyclerView;
    private BrickRecyclerAdapter adapter;
    private ViewGroup stickyHolderLayout;
    private Context context;
    private View view;
    private View textBrickView;

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
        BaseBrick footer;
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }


        context = InstrumentationRegistry.getTargetContext();
        recyclerView = mock(RecyclerView.class);
        when(recyclerView.isComputingLayout()).thenReturn(false);
        adapter = mock(BrickRecyclerAdapter.class);
        footer = mock(BaseBrick.class);
        view = mock(View.class);

        when(adapter.getRecyclerView()).thenReturn(recyclerView);
        when(adapter.getItemCount()).thenReturn(30);
        when(adapter.getRecyclerView().getChildCount()).thenReturn(10);
        for (int i = 0; i < 30; i++) {
            when(adapter.getRecyclerView().getChildAt(i)).thenReturn(view);
        }

        when(adapter.getRecyclerView().getChildAdapterPosition(view)).thenReturn(9);
        for (int i = 0; i < 30; i++) {
            when(adapter.getSectionFooter(i)).thenReturn(footer);
        }

        when(adapter.indexOf(footer)).thenReturn(9);
        when(footer.getLayout()).thenReturn(R.layout.text_brick);
        when(adapter.getItemViewType(9)).thenReturn(R.layout.text_brick);
        dataManager = mock(BrickDataManager.class);
        when(dataManager.getBrickRecyclerAdapter()).thenReturn(adapter);
        when(dataManager.brickAtPosition(9)).thenReturn(footer);
        when(dataManager.brickAtPosition(0)).thenReturn(footer);

        LayoutInflater inflater = LayoutInflater.from(context);
        final ViewGroup nullParent = null;
        View fragmentBrick = inflater.inflate(R.layout.vertical_fragment_brick, nullParent);
        textBrickView = inflater.inflate(R.layout.text_brick, nullParent);
        stickyHolderLayout = (ViewGroup) fragmentBrick.findViewById(R.id.sticky_footer_container);

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

        footerBehavior = new TestStickyFooterBehavior(dataManager);
        footerBehavior = new TestStickyFooterBehavior(dataManager, stickyHolderLayout);
    }

    @Test
    public void testGetStickyViewPosition() {
        for (int i = 0; i < 10; i++) {
            dataManager.addLast(brickTestHelper.generateBrick());
        }
        BaseBrick brick = brickTestHelper.generateBrick();
        brick.setFooter(true);
        dataManager.addLast(brick);
        int position = footerBehavior.getStickyViewPosition(RecyclerView.NO_POSITION);

        assertEquals(position, 9);
        position = footerBehavior.getStickyViewPosition(5);
        assertEquals(position, 9);
    }

    @Test
    public void testTranslateStickyView() {
        footerBehavior.translateStickyView();

        for (int i = 0; i < 30; i++) {
            dataManager.addLast(brickTestHelper.generateBrick());
        }
        BaseBrick brick = brickTestHelper.generateBrick();
        brick.setFooter(true);
        dataManager.addLast(brick);
        DummyLayoutManager layoutManager = new DummyLayoutManager(context);
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        when(recyclerView.getLayoutManager()).thenReturn(layoutManager);


        for (int i = 0; i < 30; i++) {
            when(adapter.getRecyclerView().getChildAt(i)).thenReturn(textBrickView);
        }

        footerBehavior.onScroll();
        footerBehavior.onDataSetChanged();

        when(adapter.getRecyclerView().getChildAdapterPosition(view)).thenReturn(0);
        when(adapter.getSectionFooter(0)).thenReturn(null);
        footerBehavior.translateStickyView();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        when(recyclerView.getLayoutManager()).thenReturn(gridLayoutManager);
        footerBehavior.translateStickyView();
    }

    @Test
    public void testOnScrolledForNullHolderLayout() {
        for (int i = 0; i < 10; i++) {
            dataManager.addLast(brickTestHelper.generateBrick());
        }

        footerBehavior = new TestStickyFooterBehavior(dataManager, null);
        footerBehavior.onScrolled(recyclerView, 10, 10);
        footerBehavior.onScroll();
        assertNull(footerBehavior.getStickyViewHolder());
    }

    @Test
    public void testOnDestroy() {
        for (int i = 0; i < 30; i++) {
            dataManager.addLast(brickTestHelper.generateBrick());
        }
        BaseBrick brick = brickTestHelper.generateBrick();
        brick.setFooter(true);
        dataManager.addLast(brick);
        DummyLayoutManager layoutManager = new DummyLayoutManager(context);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        when(recyclerView.getLayoutManager()).thenReturn(layoutManager);
        footerBehavior.onDataSetChanged();

        dataManager.onDestroyView();
        footerBehavior.detachFromRecyclerView();
        assertNull(footerBehavior.getStickyViewHolder());

        when(adapter.getRecyclerView()).thenReturn(null);
        footerBehavior.detachFromRecyclerView();
        assertNull(footerBehavior.getStickyViewHolder());
    }

    @Test
    public void testOnDataSetChanged() {
        when(adapter.getItemCount()).thenReturn(0);
        footerBehavior.onDataSetChanged();

        when(adapter.getSectionFooter(9)).thenReturn(null);
        footerBehavior.onDataSetChanged();

        when(adapter.getRecyclerView().getChildCount()).thenReturn(0);
        footerBehavior.onDataSetChanged();

        stickyHolderLayout = null;
        footerBehavior.onDataSetChanged();

        when(adapter.getRecyclerView()).thenReturn(null);
        footerBehavior.onDataSetChanged();
        assertEquals(footerBehavior.getStickyPosition(), -1);
    }

    public static final class TestStickyFooterBehavior extends StickyFooterBehavior {
        TestStickyFooterBehavior(BrickDataManager brickDataManager) {
            super(brickDataManager);
        }

        TestStickyFooterBehavior(BrickDataManager brickDataManager, ViewGroup stickyHolderLayout) {
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
    }

    private final class DummyLayoutManager extends LinearLayoutManager {
        DummyLayoutManager(Context context) {
            super(context);
        }
    }

}


