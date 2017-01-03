package com.wayfair.brickkit.behaviour;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wayfair.brickkit.BrickDataManager;
import com.wayfair.brickkit.BrickRecyclerAdapter;
import com.wayfair.brickkit.BrickViewHolder;
import com.wayfair.brickkit.R;
import com.wayfair.brickkit.StickyScrollMode;
import com.wayfair.brickkit.behavior.StickyHeaderBehavior;
import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.padding.BrickPadding;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class StickyHeaderBehaviorTest {

    private BrickDataManager dataManager;
    private TestStickyHeaderBehavior headerBehavior;
    private RecyclerView recyclerView;
    private BrickRecyclerAdapter adapter;
    private ViewGroup stickyHolderLayout;
    private Context context;
    private View view;
    private BrickViewHolder stickyViewHolder;
    private View itemView;
    private static int MOCK_VIEW_SIZE = 10;
    private static int ADAPTER_COUNT = 10;
    private static int HEADER_INDEX = 1;
    private static int SCROLL_DISTANCE = 10;
    private static int BOUNDARY_AXIS = 1;
    private static String TEST = "TEXT";

    @Before
    public void setup() {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }

        context = InstrumentationRegistry.getTargetContext();
        recyclerView = mock(RecyclerView.class);
        adapter = mock(BrickRecyclerAdapter.class);
        view = mock(View.class);
        dataManager = mock(BrickDataManager.class);

        when(dataManager.getBrickRecyclerAdapter()).thenReturn(adapter);
        when(adapter.getRecyclerView()).thenReturn(recyclerView);

        itemView = new TextView(context);
        ((TextView) itemView).setText(TEST);
        itemView.measure(MOCK_VIEW_SIZE, MOCK_VIEW_SIZE);

        stickyViewHolder = new BrickViewHolder(itemView);

        headerBehavior = spy(new TestStickyHeaderBehavior(dataManager));
        headerBehavior.swapStickyView(null);
        headerBehavior.translateStickyView();

        stickyHolderLayout = spy((ViewGroup) LayoutInflater.from(context).inflate(R.layout.text_brick, new LinearLayout(context), false));
        stickyHolderLayout.layout(0, 0, MOCK_VIEW_SIZE, MOCK_VIEW_SIZE);
        stickyHolderLayout.setLayoutParams(new ViewGroup.LayoutParams(MOCK_VIEW_SIZE, MOCK_VIEW_SIZE));

        headerBehavior = spy(new TestStickyHeaderBehavior(dataManager, stickyHolderLayout));
        headerBehavior.swapStickyView(stickyViewHolder);
    }

    @Test
    public void testGetStickyViewPosition() {
        when(adapter.getRecyclerView().getChildCount() - 1).thenReturn(ADAPTER_COUNT);
        when(adapter.getRecyclerView().getChildAt(ADAPTER_COUNT)).thenReturn(view);
        when(adapter.getRecyclerView().getChildAdapterPosition(view)).thenReturn(HEADER_INDEX);
        when(adapter.getSectionHeader(HEADER_INDEX)).thenReturn(null);

        int position = headerBehavior.getStickyViewPosition(RecyclerView.NO_POSITION);
        assertEquals(position, RecyclerView.NO_POSITION);

        BaseBrick header = mock(BaseBrick.class);
        when(adapter.getSectionHeader(HEADER_INDEX)).thenReturn(header);
        when(header.getStickyScrollMode()).thenReturn(StickyScrollMode.SHOW_ON_SCROLL_UP);
        when(adapter.indexOf(header)).thenReturn(HEADER_INDEX);

        position = headerBehavior.getStickyViewPosition(HEADER_INDEX);
        assertEquals(position, HEADER_INDEX);
    }

    @Test
    public void testStickyViewFadeTranslate() {
        BaseBrick header = mock(BaseBrick.class);
        when(adapter.getSectionHeader(HEADER_INDEX)).thenReturn(header);
        when(header.getStickyScrollMode()).thenReturn(StickyScrollMode.SHOW_ON_SCROLL_UP);
        when(adapter.indexOf(header)).thenReturn(HEADER_INDEX);
        headerBehavior.getStickyViewPosition(HEADER_INDEX);

        headerBehavior.stickyViewFadeTranslate(SCROLL_DISTANCE);
        assertEquals(stickyHolderLayout.getY(), 0f);

        headerBehavior.stickyViewFadeTranslate(-SCROLL_DISTANCE);
        assertEquals(stickyHolderLayout.getY(), -10f);

        when(header.getStickyScrollMode()).thenReturn(StickyScrollMode.SHOW_ON_SCROLL_DOWN);
        headerBehavior.getStickyViewPosition(HEADER_INDEX);
        headerBehavior.stickyViewFadeTranslate(SCROLL_DISTANCE);
        assertEquals(stickyHolderLayout.getY(), -10f);

        headerBehavior.stickyViewFadeTranslate(-SCROLL_DISTANCE);
        assertEquals(stickyHolderLayout.getY(), 0f);

        stickyHolderLayout.layout(BOUNDARY_AXIS, BOUNDARY_AXIS, BOUNDARY_AXIS, BOUNDARY_AXIS);
        headerBehavior.stickyViewFadeTranslate(SCROLL_DISTANCE);
        assertEquals(stickyHolderLayout.getY(), 1f);

        headerBehavior = spy(new TestStickyHeaderBehavior(dataManager, null));
        headerBehavior.stickyViewFadeTranslate(SCROLL_DISTANCE);
    }

    @Test
    public void testTranslateStickyView() {
        when(adapter.getRecyclerView().getChildAt(HEADER_INDEX)).thenReturn(null);
        headerBehavior.translateStickyView();
        verify(recyclerView).getChildAt(HEADER_INDEX);
        assertNull(recyclerView.getChildAt(HEADER_INDEX));

        View textView = new TextView(context);
        when(adapter.getRecyclerView().getChildAt(HEADER_INDEX)).thenReturn(textView);
        when(adapter.getRecyclerView().getChildAdapterPosition(textView)).thenReturn(-RecyclerView.NO_POSITION);
        headerBehavior.translateStickyView();
        verify(headerBehavior, atLeastOnce()).getStickyViewPosition(-RecyclerView.NO_POSITION);

        when(adapter.getRecyclerView().getChildAdapterPosition(textView)).thenReturn(HEADER_INDEX);
        BaseBrick header = mock(BaseBrick.class);
        when(adapter.getSectionHeader(HEADER_INDEX)).thenReturn(header);
        when(adapter.indexOf(header)).thenReturn(HEADER_INDEX);
        textView.layout(BOUNDARY_AXIS, 0, MOCK_VIEW_SIZE, MOCK_VIEW_SIZE);
        headerBehavior.translateStickyView();
        verify(headerBehavior, atLeastOnce()).getStickyViewPosition(HEADER_INDEX);

        textView.layout(-BOUNDARY_AXIS, BOUNDARY_AXIS, MOCK_VIEW_SIZE, MOCK_VIEW_SIZE);
        headerBehavior.translateStickyView();
        assertEquals(textView.getLeft(), -BOUNDARY_AXIS);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        when(recyclerView.getLayoutManager()).thenReturn(layoutManager);
        headerBehavior.translateStickyView();
        assertEquals(((LinearLayoutManager)recyclerView.getLayoutManager()).getOrientation(), OrientationHelper.VERTICAL);

        textView.layout(-BOUNDARY_AXIS, -BOUNDARY_AXIS, MOCK_VIEW_SIZE, MOCK_VIEW_SIZE);
        headerBehavior.translateStickyView();
        assertEquals(textView.getTop(), -BOUNDARY_AXIS);

        when(adapter.getRecyclerView()). thenReturn(null);
        headerBehavior.translateStickyView();
        assertNull(adapter.getRecyclerView());

        when(dataManager.getBrickRecyclerAdapter()).thenReturn(null);
        headerBehavior.translateStickyView();
        assertNull(dataManager.getBrickRecyclerAdapter());
    }

    @Test
    public void testOnDataSetChanged() {
        headerBehavior.onDataSetChanged();
        verify(headerBehavior).onDataSetChanged();
    }

    @Test
    public void testAttachToRecyclerView() {
        when(adapter.getRecyclerView()).thenReturn(null);
        headerBehavior.attachToRecyclerView();
        verify(headerBehavior).attachToRecyclerView();

        when(dataManager.getBrickRecyclerAdapter()).thenReturn(null);
        headerBehavior.attachToRecyclerView();
        verify(headerBehavior, atLeastOnce()).attachToRecyclerView();
    }

    @Test
    public void testOnScroll() {
        headerBehavior.onScroll();
        verify(headerBehavior).onScroll();

        doReturn(null).when(stickyHolderLayout).getLayoutParams();
        headerBehavior.onScroll();
        verify(headerBehavior, atLeastOnce()).onScroll();

        headerBehavior = spy(new TestStickyHeaderBehavior(dataManager, null));
        Activity context = mock(Activity.class);
        when(dataManager.getContext()).thenReturn(context);
        when((context).findViewById(R.id.sticky_header_container)).thenReturn(stickyHolderLayout);
        ImageView imageView = mock(ImageView.class);
        when((context).findViewById(R.id.bar_shadow)).thenReturn(imageView);
        headerBehavior.onScroll();

        when(dataManager.getContext()).thenReturn(null);
        headerBehavior.onScroll();
        assertNull(dataManager.getContext());

        when(adapter.getRecyclerView()).thenReturn(null);
        headerBehavior.onScroll();
        assertNull(adapter.getRecyclerView());
    }

    @Test
    public void testOnScrolled() {
        headerBehavior = spy(new TestStickyHeaderBehavior(dataManager, null));
        headerBehavior.onScrolled(recyclerView, 0, 0);

        headerBehavior = spy(new TestStickyHeaderBehavior(dataManager));
        Activity context = mock(Activity.class);
        when(dataManager.getContext()).thenReturn(context);
        when((context).findViewById(R.id.sticky_header_container)).thenReturn(stickyHolderLayout);
        ImageView imageView = mock(ImageView.class);
        when((context).findViewById(R.id.bar_shadow)).thenReturn(imageView);
        headerBehavior.onScroll();
        headerBehavior.onScrolled(recyclerView, BOUNDARY_AXIS, BOUNDARY_AXIS);
        assertEquals(stickyHolderLayout.getTop(), (int)stickyHolderLayout.getY());

        stickyHolderLayout.setTranslationY(2f);
        headerBehavior.onScroll();
        headerBehavior.onScrolled(recyclerView, BOUNDARY_AXIS, BOUNDARY_AXIS);
        assertEquals(stickyHolderLayout.getTop() + 2f, stickyHolderLayout.getY());

    }

    @Test
    public void testSwapStickyView() {
        headerBehavior.swapStickyView(null);
        verify(headerBehavior).swapStickyView(null);
    }

    @Test
    public void testDetachFromRecyclerView() {
        headerBehavior.detachFromRecyclerView();

        when(adapter.getRecyclerView()).thenReturn(null);
        headerBehavior.detachFromRecyclerView();
        verify(headerBehavior, atLeastOnce()).detachFromRecyclerView();

        when(dataManager.getBrickRecyclerAdapter()).thenReturn(null);
        headerBehavior.detachFromRecyclerView();
        verify(headerBehavior, atLeastOnce()).detachFromRecyclerView();
    }

    @Test
    public void testUpdateOrClearStickyView() {
        //have to set up stickyLayoutBottomLine
        headerBehavior = spy(new TestStickyHeaderBehavior(dataManager));
        Activity activityContext = mock(Activity.class);
        when(dataManager.getContext()).thenReturn(activityContext);
        when((activityContext).findViewById(R.id.sticky_header_container)).thenReturn(stickyHolderLayout);
        ImageView imageView = mock(ImageView.class);
        when((activityContext).findViewById(R.id.bar_shadow)).thenReturn(imageView);
        headerBehavior.onScroll();
        assertEquals(headerBehavior.getStickyHolderLayout(), stickyHolderLayout);

        when(recyclerView.getChildCount()).thenReturn(ADAPTER_COUNT);
        doReturn(RecyclerView.NO_POSITION).when(headerBehavior).getStickyViewPosition(RecyclerView.NO_POSITION);
        headerBehavior.onScrolled(recyclerView, SCROLL_DISTANCE ,SCROLL_DISTANCE);
        assertEquals(headerBehavior.getStickyViewPosition(RecyclerView.NO_POSITION), RecyclerView.NO_POSITION);

        doReturn(HEADER_INDEX).when(headerBehavior).getStickyViewPosition(RecyclerView.NO_POSITION);
        when(adapter.getItemCount()).thenReturn(0);
        headerBehavior.onScrolled(recyclerView, SCROLL_DISTANCE ,SCROLL_DISTANCE);

        doReturn(HEADER_INDEX).when(headerBehavior).getStickyViewPosition(RecyclerView.NO_POSITION);
        when(adapter.getItemCount()).thenReturn(ADAPTER_COUNT);
        when(adapter.getItemViewType(HEADER_INDEX)).thenReturn(HEADER_INDEX);
        when(adapter.onCreateViewHolder(recyclerView, HEADER_INDEX)).thenReturn(stickyViewHolder);
        BrickPadding padding = new BrickPadding(new Rect(1, 1, 1, 1), new Rect(1, 1, 1, 1));
        BaseBrick brick = mock(BaseBrick.class);
        when(dataManager.brickAtPosition(HEADER_INDEX)).thenReturn(brick);
        when(brick.getPadding()).thenReturn(padding);
        headerBehavior.onScrolled(recyclerView, SCROLL_DISTANCE ,SCROLL_DISTANCE);
        verify(dataManager.brickAtPosition(HEADER_INDEX), atLeastOnce()).getPadding();

        headerBehavior.onDataSetChanged();
        headerBehavior.onScrolled(recyclerView, SCROLL_DISTANCE ,SCROLL_DISTANCE);

        doReturn(2).when(headerBehavior).getStickyViewPosition(RecyclerView.NO_POSITION);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        when(recyclerView.getLayoutManager()).thenReturn(layoutManager);
        when(adapter.getItemViewType(2)).thenReturn(2);
        when(adapter.onCreateViewHolder(recyclerView, 2)).thenReturn(stickyViewHolder);
        when(dataManager.brickAtPosition(2)).thenReturn(brick);
        when(brick.getPadding()).thenReturn(padding);
        headerBehavior.onScrolled(recyclerView, SCROLL_DISTANCE ,SCROLL_DISTANCE);
        verify(dataManager.brickAtPosition(2), atLeastOnce()).getPadding();
        assertEquals((((LinearLayoutManager)recyclerView.getLayoutManager()).getOrientation()), OrientationHelper.VERTICAL);

        when(adapter.getRecyclerView().getChildCount()).thenReturn(0);
        headerBehavior.onScrolled(recyclerView, SCROLL_DISTANCE, SCROLL_DISTANCE);
        assertEquals(adapter.getRecyclerView().getChildCount(), 0);

        when(adapter.getRecyclerView()).thenReturn(null);
        headerBehavior.onScrolled(recyclerView, SCROLL_DISTANCE, SCROLL_DISTANCE);
        assertNull(adapter.getRecyclerView());

        when(dataManager.getBrickRecyclerAdapter()).thenReturn(null);
        headerBehavior.onScrolled(recyclerView, SCROLL_DISTANCE, SCROLL_DISTANCE);
        assertNull(dataManager.getBrickRecyclerAdapter());

        headerBehavior = spy(new TestStickyHeaderBehavior(dataManager, null));
        headerBehavior.onScrolled(recyclerView, SCROLL_DISTANCE, SCROLL_DISTANCE);
        assertNull(headerBehavior.getStickyHolderLayout());
    }

    @Test
    public void testGetStickyViewHolder() {
        headerBehavior.getStickyViewHolder();
        verify(headerBehavior).getStickyViewHolder();
    }

    @Test
    public void getStickPosition() {
        headerBehavior.getStickyPosition();
        verify(headerBehavior).getStickyPosition();
    }

    public class TestStickyHeaderBehavior extends StickyHeaderBehavior {
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

        @Override
        protected void swapStickyView(BrickViewHolder newStickyView) {
            super.swapStickyView(newStickyView);
        }
    }

}


