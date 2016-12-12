package com.wayfair.brickkit;

import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wayfair.brickkit.brick.BaseBrick;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class BrickRecyclerAdapterTest {

    private static final int POSITION = 1;
    private static final int TO_POSITION = 2;
    private static final int COUNT = 3;
    private static final Object PAYLOAD = new Object();
    private static final int BRICK_COUNT = 3;
    private static final String TEMPLATE = "template";
    private static final int INDEX = 5;
    private BrickRecyclerAdapter adapter;
    private TestAdapterDataObserver observer;
    private LinkedList<BaseBrick> bricks;
    private RecyclerView recyclerView;

    @Before
    public void setup() {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        BrickDataManager dataManager = mock(BrickDataManager.class);

        BaseBrick brick = mock(BaseBrick.class);

        bricks = mock(LinkedList.class);
        when(bricks.size()).thenReturn(BRICK_COUNT);
        when(bricks.get(anyInt())).thenReturn(brick);
        when(bricks.indexOf(any(BaseBrick.class))).thenReturn(INDEX);

        when(dataManager.getRecyclerViewItems()).thenReturn(bricks);

        recyclerView = mock(RecyclerView.class);
        when(recyclerView.isComputingLayout()).thenReturn(false);

        observer = new TestAdapterDataObserver();

        adapter = new BrickRecyclerAdapter(dataManager, recyclerView);
        adapter.registerAdapterDataObserver(observer);

        TemplateRegistry.getInstance().reset();
        TemplateRegistry.getInstance().get(TEMPLATE);

        ViewHolderRegistry.register(TEMPLATE, new ViewHolderRegistry.GenerateViewHolderInterface() {

            @Override
            public BrickViewHolder generateViewHolder(ViewGroup parent) {
                return mock(BrickViewHolder.class);
            }
        });
    }

    @Test
    public void testGetRecyclerView() {
        assertEquals(recyclerView, adapter.getRecyclerView());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullRecyclerView() {
        new BrickRecyclerAdapter(mock(BrickDataManager.class), null);
    }

    @Test
    public void testSafeNotifyDataSetChanged() {
        adapter.safeNotifyDataSetChanged();

        assertTrue(observer.changed);
    }

    @Test
    public void testSafeNotifyDataSetChangedComputingLayout() {
        when(recyclerView.isComputingLayout()).thenReturn(true);
        adapter.safeNotifyDataSetChanged();

        assertFalse(observer.changed);
    }

    @Test
    public void testSafeNotifyItemChanged() {
        adapter.safeNotifyItemChanged(POSITION);

        assertEquals(POSITION, observer.itemRangeChangedPositionStart);
        assertEquals(1, observer.itemRangeChangedItemCount);
        assertNull(observer.itemRangeChangedPayload);
    }

    @Test
    public void testSafeNotifyItemChangedComputingLayout() {
        when(recyclerView.isComputingLayout()).thenReturn(true);
        adapter.safeNotifyItemChanged(POSITION);

        assertEquals(-1, observer.itemRangeChangedPositionStart);
        assertEquals(-1, observer.itemRangeChangedItemCount);
        assertNull(observer.itemRangeChangedPayload);
    }

    @Test
    public void testSafeNotifyItemChangedWithPayload() {
        adapter.safeNotifyItemChanged(POSITION, PAYLOAD);

        assertEquals(POSITION, observer.itemRangeChangedPositionStart);
        assertEquals(1, observer.itemRangeChangedItemCount);
        assertNotNull(observer.itemRangeChangedPayload);
    }

    @Test
    public void testSafeNotifyItemChangedWithPayloadComputingLayout() {
        when(recyclerView.isComputingLayout()).thenReturn(true);
        adapter.safeNotifyItemChanged(POSITION, PAYLOAD);

        assertEquals(-1, observer.itemRangeChangedPositionStart);
        assertEquals(-1, observer.itemRangeChangedItemCount);
        assertNull(observer.itemRangeChangedPayload);
    }

    @Test
    public void testSafeNotifyItemInserted() {
        adapter.safeNotifyItemInserted(POSITION);

        assertEquals(POSITION, observer.itemRangeInsertedPositionStart);
        assertEquals(1, observer.itemRangeInsertedItemCount);
    }

    @Test
    public void testSafeNotifyItemInsertedComputingLayout() {
        when(recyclerView.isComputingLayout()).thenReturn(true);
        adapter.safeNotifyItemInserted(POSITION);

        assertEquals(-1, observer.itemRangeInsertedPositionStart);
        assertEquals(-1, observer.itemRangeInsertedItemCount);
    }

    @Test
    public void testSafeNotifyItemRangeInserted() {
        adapter.safeNotifyItemRangeInserted(POSITION, COUNT);

        assertEquals(POSITION, observer.itemRangeInsertedPositionStart);
        assertEquals(COUNT, observer.itemRangeInsertedItemCount);
    }

    @Test
    public void testSafeNotifyItemRangeInsertedComputingLayout() {
        when(recyclerView.isComputingLayout()).thenReturn(true);
        adapter.safeNotifyItemRangeInserted(POSITION, COUNT);

        assertEquals(-1, observer.itemRangeInsertedPositionStart);
        assertEquals(-1, observer.itemRangeInsertedItemCount);
    }

    @Test
    public void testSafeNotifyItemMoved() {
        adapter.safeNotifyItemMoved(POSITION, TO_POSITION);

        assertEquals(POSITION, observer.itemRangeMovedFromPosition);
        assertEquals(TO_POSITION, observer.itemRangeMovedToPosition);
        assertEquals(1, observer.itemRangeMovedItemCount);
    }

    @Test
    public void testSafeNotifyItemMovedComputingLayout() {
        when(recyclerView.isComputingLayout()).thenReturn(true);
        adapter.safeNotifyItemMoved(POSITION, TO_POSITION);

        assertEquals(-1, observer.itemRangeMovedFromPosition);
        assertEquals(-1, observer.itemRangeMovedToPosition);
        assertEquals(-1, observer.itemRangeMovedItemCount);
    }

    @Test
    public void testSafeNotifyItemRangeChangedWithPayload() {
        adapter.safeNotifyItemRangeChanged(POSITION, COUNT, PAYLOAD);

        assertEquals(POSITION, observer.itemRangeChangedPositionStart);
        assertEquals(COUNT, observer.itemRangeChangedItemCount);
        assertEquals(PAYLOAD, observer.itemRangeChangedPayload);
    }

    @Test
    public void testSafeNotifyItemRangeChangedWithPayloadComputingLayout() {
        when(recyclerView.isComputingLayout()).thenReturn(true);
        adapter.safeNotifyItemRangeChanged(POSITION, COUNT, PAYLOAD);

        assertEquals(-1, observer.itemRangeChangedPositionStart);
        assertEquals(-1, observer.itemRangeChangedItemCount);
        assertNull(observer.itemRangeChangedPayload);
    }

    @Test
    public void testSafeNotifyItemRangeChanged() {
        adapter.safeNotifyItemRangeChanged(POSITION, COUNT);

        assertEquals(POSITION, observer.itemRangeChangedPositionStart);
        assertEquals(COUNT, observer.itemRangeChangedItemCount);
        assertNull(observer.itemRangeChangedPayload);
    }

    @Test
    public void testSafeNotifyItemRangeChangedComputingLayout() {
        when(recyclerView.isComputingLayout()).thenReturn(true);
        adapter.safeNotifyItemRangeChanged(POSITION, COUNT);

        assertEquals(-1, observer.itemRangeChangedPositionStart);
        assertEquals(-1, observer.itemRangeChangedItemCount);
        assertNull(observer.itemRangeChangedPayload);
    }

    @Test
    public void testSafeNotifyItemRemoved() {
        adapter.safeNotifyItemRemoved(POSITION);

        assertEquals(POSITION, observer.itemRangeRemovedPositionStart);
        assertEquals(1, observer.itemRangeRemovedItemCount);
    }

    @Test
    public void testSafeNotifyItemRemovedComputingLayout() {
        when(recyclerView.isComputingLayout()).thenReturn(true);
        adapter.safeNotifyItemRemoved(POSITION);

        assertEquals(-1, observer.itemRangeRemovedPositionStart);
        assertEquals(-1, observer.itemRangeRemovedItemCount);
    }

    @Test
    public void testSafeNotifyItemRangeRemoved() {
        adapter.safeNotifyItemRangeRemoved(POSITION, COUNT);

        assertEquals(POSITION, observer.itemRangeRemovedPositionStart);
        assertEquals(COUNT, observer.itemRangeRemovedItemCount);
    }

    @Test
    public void testSafeNotifyItemRangeRemovedComputingLayout() {
        when(recyclerView.isComputingLayout()).thenReturn(true);

        adapter.safeNotifyItemRangeRemoved(POSITION, COUNT);

        assertEquals(-1, observer.itemRangeRemovedPositionStart);
        assertEquals(-1, observer.itemRangeRemovedItemCount);
    }

    @Test
    public void testOnCreateViewHolder() {
        assertNotNull(adapter.onCreateViewHolder(new LinearLayout(InstrumentationRegistry.getTargetContext()), 0));
    }

    @Test
    public void testOnBindViewHolderNullBrick() {
        OnReachedItemAtPosition listener = mock(OnReachedItemAtPosition.class);
        adapter.setOnReachedItemAtPosition(listener);

        when(bricks.get(0)).thenReturn(null);

        BrickViewHolder holder = mock(BrickViewHolder.class);

        adapter.onBindViewHolder(holder, 0);

        verify(listener, never()).bindingItemAtPosition(0);
    }

    @Test
    public void testOnBindViewHolderNullBindListener() {
        BaseBrick brick = mock(BaseBrick.class);

        when(bricks.get(0)).thenReturn(brick);

        BrickViewHolder holder = mock(BrickViewHolder.class);

        adapter.onBindViewHolder(holder, 0);

        verify(brick).onBindData(holder);
    }

    @Test
    public void testOnBindViewHolder() {
        BaseBrick brick = mock(BaseBrick.class);

        OnReachedItemAtPosition listener = mock(OnReachedItemAtPosition.class);
        adapter.setOnReachedItemAtPosition(listener);

        when(bricks.get(0)).thenReturn(brick);

        BrickViewHolder holder = mock(BrickViewHolder.class);

        adapter.onBindViewHolder(holder, 0);

        verify(brick).onBindData(holder);
        verify(listener).bindingItemAtPosition(0);
    }

    @Test
    public void testOnViewDetachedFromWindow() {
        BrickViewHolder brickViewHolder = mock(BrickViewHolder.class);

        adapter.onViewDetachedFromWindow(brickViewHolder);

        verify(brickViewHolder).releaseViewsOnDetach();
    }

    @Test
    public void testGetItemCount() {
        assertEquals(BRICK_COUNT, adapter.getItemCount());
    }

    @Test
    public void testGetItemViewType() {
        BaseBrick brick = mock(BaseBrick.class);
        when(brick.getTemplate()).thenReturn(TEMPLATE);

        when(bricks.get(0)).thenReturn(null);
        when(bricks.get(1)).thenReturn(brick);

        assertEquals(-1, adapter.getItemViewType(0));
        assertEquals(0, adapter.getItemViewType(1));
    }

    @Test
    public void testGet() {
        assertNotNull(adapter.get(0));
    }

    @Test
    public void testIndexOf() {
        assertEquals(INDEX, adapter.indexOf(mock(BaseBrick.class)));
    }

    @Test
    public void testGetSectionHeaderNoHeader() {
        BaseBrick brick = mock(BaseBrick.class);

        when(bricks.get(0)).thenReturn(brick);
        when(bricks.get(1)).thenReturn(null);

        assertNull(adapter.getSectionHeader(1));
    }

    @Test
    public void testGetSectionHeader() {
        BaseBrick headerBrick = mock(BaseBrick.class);
        when(headerBrick.isHeader()).thenReturn(true);

        BaseBrick brick = mock(BaseBrick.class);

        when(bricks.get(0)).thenReturn(headerBrick);
        when(bricks.get(1)).thenReturn(brick);
        when(bricks.get(2)).thenReturn(null);

        assertEquals(headerBrick, adapter.getSectionHeader(2));
    }

    @Test
    public void testGetSectionHeaderBadPosition() {
        assertNull(adapter.getSectionHeader(-1));
    }

    @Test
    public void testGetSectionFooterNoFooter() {
        BaseBrick brick = mock(BaseBrick.class);

        when(bricks.get(0)).thenReturn(null);
        when(bricks.get(1)).thenReturn(brick);

        assertNull(adapter.getSectionFooter(0));
    }

    @Test
    public void testGetSectionFooter() {
        BaseBrick footerBrick = mock(BaseBrick.class);
        when(footerBrick.isFooter()).thenReturn(true);

        BaseBrick brick = mock(BaseBrick.class);

        when(bricks.get(0)).thenReturn(null);
        when(bricks.get(1)).thenReturn(brick);
        when(bricks.get(2)).thenReturn(footerBrick);

        assertEquals(footerBrick, adapter.getSectionFooter(0));
    }

    @Test
    public void testGetSectionFooterBadPosition() {
        assertNull(adapter.getSectionFooter(-1));
    }

    private class TestAdapterDataObserver extends RecyclerView.AdapterDataObserver {

        private boolean changed = false;

        private int itemRangeChangedPositionStart = -1;
        private int itemRangeChangedItemCount = -1;
        private Object itemRangeChangedPayload;

        private int itemRangeInsertedPositionStart = -1;
        private int itemRangeInsertedItemCount = -1;

        private int itemRangeRemovedPositionStart = -1;
        private int itemRangeRemovedItemCount = -1;

        private int itemRangeMovedFromPosition = -1;
        private int itemRangeMovedToPosition = -1;
        private int itemRangeMovedItemCount = -1;


        public void onChanged() {
            changed = true;
        }

        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            itemRangeChangedPositionStart = positionStart;
            itemRangeChangedItemCount = itemCount;
            itemRangeChangedPayload = payload;
        }

        public void onItemRangeChanged(int positionStart, int itemCount) {
            itemRangeChangedPositionStart = positionStart;
            itemRangeChangedItemCount = itemCount;
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
            itemRangeInsertedPositionStart = positionStart;
            itemRangeInsertedItemCount = itemCount;
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
            itemRangeRemovedPositionStart = positionStart;
            itemRangeRemovedItemCount = itemCount;
        }

        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            itemRangeMovedFromPosition = fromPosition;
            itemRangeMovedToPosition = toPosition;
            itemRangeMovedItemCount = itemCount;
        }
    }
}
