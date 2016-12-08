package com.wayfair.brickkit;

import android.content.Context;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class BrickDataManagerTest {
    private static final int MAX_SPANS = 8;
    private static final int HALF_SPAN = 4;
    private static final int PADDING = 2;

    private Context context;
    private BrickDataManager manager;
    private TestAdapterDataObserver observer;

    @Before
    public void setup() {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        context = InstrumentationRegistry.getTargetContext();

        manager = new BrickDataManager(context, new RecyclerView(context), MAX_SPANS);
        manager.addLast(generateBrick());
        manager.addLast(generateBrick());
        manager.addLast(generateBrick());
        manager.addLast(generateBrick());

        observer = new TestAdapterDataObserver();
        manager.brickRecyclerAdapter.registerAdapterDataObserver(observer);
    }

    @Test
    public void testGetMaxSpanCount() {
        assertEquals(MAX_SPANS, manager.getMaxSpanCount());
    }

    @Test
    public void testSetItems() {
        List<BaseBrick> newItems = new LinkedList<>();
        newItems.add(generateBrick());
        newItems.add(generateHiddenBrick());
        newItems.add(generateBrick());
        newItems.add(generateHiddenBrick());
        newItems.add(generateBrick());

        manager.setItems(newItems);

        assertEquals(3, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(0, observer.itemRangeRemovedPositionStart);
        assertEquals(4, observer.itemRangeRemovedItemCount);

        assertEquals(0, observer.itemRangeInsertedPositionStart);
        assertEquals(3, observer.itemRangeInsertedItemCount);
    }

    @Test
    public void testAddLastVisible() {
        manager.addLast(generateBrick());

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(4, observer.itemRangeInsertedPositionStart);
        assertEquals(1, observer.itemRangeInsertedItemCount);

        assertEquals(2, observer.itemRangeChangedPositionStart);
        assertEquals(2, observer.itemRangeChangedItemCount);
    }

    @Test
    public void testAddLastHidden() {
        manager.addLast(generateHiddenBrick());

        assertEquals(4, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(-1, observer.itemRangeInsertedPositionStart);
        assertEquals(-1, observer.itemRangeInsertedItemCount);

        assertEquals(-1, observer.itemRangeChangedPositionStart);
        assertEquals(-1, observer.itemRangeChangedItemCount);
    }

    @Test
    public void testAddLastCollection() {
        List<BaseBrick> newItems = new LinkedList<>();
        newItems.add(generateBrick());
        newItems.add(generateHiddenBrick());
        newItems.add(generateBrick());
        newItems.add(generateHiddenBrick());
        newItems.add(generateBrick());

        manager.addLast(newItems);

        assertEquals(7, manager.getRecyclerViewItems().size());
        assertEquals(9, manager.getDataManagerItems().size());

        assertEquals(4, observer.itemRangeInsertedPositionStart);
        assertEquals(3, observer.itemRangeInsertedItemCount);

        assertEquals(2, observer.itemRangeChangedPositionStart);
        assertEquals(2, observer.itemRangeChangedItemCount);
    }

    @Test
    public void testAddLastCollectionAllHidden() {
        List<BaseBrick> newItems = new LinkedList<>();
        newItems.add(generateHiddenBrick());
        newItems.add(generateHiddenBrick());
        newItems.add(generateHiddenBrick());
        newItems.add(generateHiddenBrick());
        newItems.add(generateHiddenBrick());

        manager.addLast(newItems);

        assertEquals(4, manager.getRecyclerViewItems().size());
        assertEquals(9, manager.getDataManagerItems().size());

        assertEquals(-1, observer.itemRangeInsertedPositionStart);
        assertEquals(-1, observer.itemRangeInsertedItemCount);

        assertEquals(-1, observer.itemRangeChangedPositionStart);
        assertEquals(-1, observer.itemRangeChangedItemCount);
    }

    @Test
    public void testAddFirstVisible() {
        manager.addFirst(generateBrick());

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(0, observer.itemRangeInsertedPositionStart);
        assertEquals(1, observer.itemRangeInsertedItemCount);

        assertEquals(1, observer.itemRangeChangedPositionStart);
        assertEquals(4, observer.itemRangeChangedItemCount);
    }

    @Test
    public void testAddFirstHidden() {
        manager.addFirst(generateHiddenBrick());

        assertEquals(4, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(-1, observer.itemRangeInsertedPositionStart);
        assertEquals(-1, observer.itemRangeInsertedItemCount);

        assertEquals(-1, observer.itemRangeChangedPositionStart);
        assertEquals(-1, observer.itemRangeChangedItemCount);
    }

    @Test
    public void testAddFirstCollection() {
        List<BaseBrick> newItems = new LinkedList<>();
        newItems.add(generateBrick());
        newItems.add(generateHiddenBrick());
        newItems.add(generateBrick());
        newItems.add(generateHiddenBrick());
        newItems.add(generateBrick());

        manager.addFirst(newItems);

        assertEquals(7, manager.getRecyclerViewItems().size());
        assertEquals(9, manager.getDataManagerItems().size());

        assertEquals(0, observer.itemRangeInsertedPositionStart);
        assertEquals(3, observer.itemRangeInsertedItemCount);

        assertEquals(3, observer.itemRangeChangedPositionStart);
        assertEquals(4, observer.itemRangeChangedItemCount);
    }

    @Test
    public void testAddFirstCollectionAllHidden() {
        List<BaseBrick> newItems = new LinkedList<>();
        newItems.add(generateHiddenBrick());
        newItems.add(generateHiddenBrick());
        newItems.add(generateHiddenBrick());
        newItems.add(generateHiddenBrick());
        newItems.add(generateHiddenBrick());

        manager.addFirst(newItems);

        assertEquals(4, manager.getRecyclerViewItems().size());
        assertEquals(9, manager.getDataManagerItems().size());

        assertEquals(-1, observer.itemRangeInsertedPositionStart);
        assertEquals(-1, observer.itemRangeInsertedItemCount);

        assertEquals(-1, observer.itemRangeChangedPositionStart);
        assertEquals(-1, observer.itemRangeChangedItemCount);
    }

//    @Test
//    public void testAddBeforeFirstItem() {
//        manager.addBeforeItem(manager.getRecyclerViewItems().get(0), generateBrick());
//
//        assertEquals(5, manager.getRecyclerViewItems().size());
//        assertEquals(5, manager.getDataManagerItems().size());
//
//        assertEquals(0, observer.itemRangeInsertedPositionStart);
//        assertEquals(1, observer.itemRangeInsertedItemCount);
//
//        assertEquals(1, observer.itemRangeChangedPositionStart);
//        assertEquals(4, observer.itemRangeChangedItemCount);
//    }

    @Test
    public void testAddBeforeLastItem() {
        manager.addBeforeItem(manager.getRecyclerViewItems().get(3), generateBrick());

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(3, observer.itemRangeInsertedPositionStart);
        assertEquals(1, observer.itemRangeInsertedItemCount);

        assertEquals(2, observer.itemRangeChangedPositionStart);
        assertEquals(3, observer.itemRangeChangedItemCount);
    }

//    @Test
//    public void testAddBeforeInvalidItem() {
//        manager.addBeforeItem(generateBrick(), generateBrick());
//
//        assertEquals(5, manager.getRecyclerViewItems().size());
//        assertEquals(5, manager.getDataManagerItems().size());
//
//        assertEquals(0, observer.itemRangeInsertedPositionStart);
//        assertEquals(1, observer.itemRangeInsertedItemCount);
//
//        assertEquals(1, observer.itemRangeChangedPositionStart);
//        assertEquals(4, observer.itemRangeChangedItemCount);
//    }

    @Test
    public void testAddAfterFirstItem() {
        manager.addAfterItem(manager.getRecyclerViewItems().get(0), generateBrick());

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(1, observer.itemRangeInsertedPositionStart);
        assertEquals(1, observer.itemRangeInsertedItemCount);

        assertEquals(2, observer.itemRangeChangedPositionStart);
        assertEquals(3, observer.itemRangeChangedItemCount);
    }

    @Test
    public void testAddAfterLastItem() {
        manager.addAfterItem(manager.getRecyclerViewItems().get(3), generateBrick());

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(4, observer.itemRangeInsertedPositionStart);
        assertEquals(1, observer.itemRangeInsertedItemCount);

        assertEquals(2, observer.itemRangeChangedPositionStart);
        assertEquals(3, observer.itemRangeChangedItemCount);
    }

    @Test
    public void testAddAfterInvalidItem() {
        manager.addAfterItem(generateBrick(), generateBrick());

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(4, observer.itemRangeInsertedPositionStart);
        assertEquals(1, observer.itemRangeInsertedItemCount);

        assertEquals(2, observer.itemRangeChangedPositionStart);
        assertEquals(3, observer.itemRangeChangedItemCount);
    }

    @Test
    public void testRemoveFirstItem() {
        manager.removeItem(manager.getRecyclerViewItems().get(0));

        assertEquals(3, manager.getRecyclerViewItems().size());
        assertEquals(3, manager.getDataManagerItems().size());

        assertEquals(0, observer.itemRangeRemovedPositionStart);
        assertEquals(1, observer.itemRangeRemovedItemCount);

        assertEquals(0, observer.itemRangeChangedPositionStart);
        assertEquals(3, observer.itemRangeChangedItemCount);
    }

    @Test
    public void testRemoveLastItem() {
        manager.removeItem(manager.getRecyclerViewItems().get(3));

        assertEquals(3, manager.getRecyclerViewItems().size());
        assertEquals(3, manager.getDataManagerItems().size());

        assertEquals(3, observer.itemRangeRemovedPositionStart);
        assertEquals(1, observer.itemRangeRemovedItemCount);

        assertEquals(-1, observer.itemRangeChangedPositionStart);
        assertEquals(-1, observer.itemRangeChangedItemCount);
    }

    @Test
    public void testRemoveMiddleItem() {
        manager.removeItem(manager.getRecyclerViewItems().get(1));

        assertEquals(3, manager.getRecyclerViewItems().size());
        assertEquals(3, manager.getDataManagerItems().size());

        assertEquals(1, observer.itemRangeRemovedPositionStart);
        assertEquals(1, observer.itemRangeRemovedItemCount);

        //assertEquals(1, observer.itemRangeChangedPositionStart);
        //assertEquals(2, observer.itemRangeChangedItemCount);
    }

    @Test
    public void testRemoveHiddenItem() {
        manager.addLast(generateHiddenBrick());
        manager.addLast(generateBrick());

        observer.itemRangeChangedPositionStart = -1;
        observer.itemRangeChangedItemCount = -1;

        manager.removeItem(manager.getDataManagerItems().get(4));

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertEquals(-1, observer.itemRangeRemovedPositionStart);
        assertEquals(-1, observer.itemRangeRemovedItemCount);

        assertEquals(-1, observer.itemRangeChangedPositionStart);
        assertEquals(-1, observer.itemRangeChangedItemCount);
    }

    @Test
    public void testRemoveSomeItems() {
        List<BaseBrick> itemsToRemove = new LinkedList<>();
        itemsToRemove.add(manager.getRecyclerViewItems().get(1));
        itemsToRemove.add(manager.getRecyclerViewItems().get(2));

        manager.removeItems(itemsToRemove);

        assertEquals(2, manager.getRecyclerViewItems().size());
        assertEquals(2, manager.getDataManagerItems().size());

        assertTrue(observer.changed);
    }

    @Test
    public void testRemoveAllItems() {
        List<BaseBrick> itemsToRemove = new LinkedList<>();
        itemsToRemove.add(manager.getRecyclerViewItems().get(0));
        itemsToRemove.add(manager.getRecyclerViewItems().get(1));
        itemsToRemove.add(manager.getRecyclerViewItems().get(2));
        itemsToRemove.add(manager.getRecyclerViewItems().get(3));

        manager.removeItems(itemsToRemove);

        assertEquals(0, manager.getRecyclerViewItems().size());
        assertEquals(0, manager.getDataManagerItems().size());

        assertTrue(observer.changed);
    }

    @Test
    public void testRemoveAllBaseBrick() {
        manager.addLast(generateOtherBrick());

        manager.removeAll(TestBrick.class);

        assertEquals(1, manager.getRecyclerViewItems().size());
        assertEquals(1, manager.getDataManagerItems().size());

        assertTrue(observer.changed);
    }

    @Test
    public void testRemoveHiddenItems() {
        manager.addLast(generateHiddenBrick());
        manager.addLast(generateHiddenBrick());
        manager.addLast(generateBrick());

        List<BaseBrick> itemsToRemove = new LinkedList<>();
        itemsToRemove.add(manager.getDataManagerItems().get(4));
        itemsToRemove.add(manager.getDataManagerItems().get(5));

        manager.removeItems(itemsToRemove);

        assertEquals(5, manager.getRecyclerViewItems().size());
        assertEquals(5, manager.getDataManagerItems().size());

        assertFalse(observer.changed);
    }

    @Test
    public void testClear() {
        manager.clear();

        assertEquals(0, manager.getRecyclerViewItems().size());
        assertEquals(0, manager.getDataManagerItems().size());

        assertEquals(0, observer.itemRangeRemovedPositionStart);
        assertEquals(4, observer.itemRangeRemovedItemCount);
    }

    private static class TestAdapterDataObserver extends RecyclerView.AdapterDataObserver {
        private boolean changed = false;

        private int itemRangeChangedPositionStart = -1;
        private int itemRangeChangedItemCount = -1;

        private int itemRangeInsertedPositionStart = -1;
        private int itemRangeInsertedItemCount = -1;

        private int itemRangeRemovedPositionStart = -1;
        private int itemRangeRemovedItemCount = -1;

        public void onChanged() {
            changed = true;
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
    }

    private static final class TestBrick extends BaseBrick {

        private TestBrick(Context context, BrickSize spanSize, BrickPadding padding) {
            super(context, spanSize, padding);
        }

        @Override
        public void onBindData(RecyclerView.ViewHolder holder) {

        }

        @Override
        public String getTemplate() {
            return null;
        }
    }

    private static final class TestBrick2 extends BaseBrick {

        private TestBrick2(Context context, BrickSize spanSize, BrickPadding padding) {
            super(context, spanSize, padding);
        }

        @Override
        public void onBindData(RecyclerView.ViewHolder holder) {

        }

        @Override
        public String getTemplate() {
            return null;
        }
    }

    private BaseBrick generateHiddenBrick() {
        BaseBrick brick = generateBrick();
        brick.setHidden(true);

        return brick;
    }

    private BaseBrick generateBrick() {
        return new TestBrick(context, new SimpleBrickSize(manager) {
            @Override
            public int getSpans(Context context) {
                return HALF_SPAN;
            }

            @Override
            protected int size() {
                return HALF_SPAN;
            }
        }, new SimpleBrickPadding() {

            @Override
            protected int padding() {
                return PADDING;
            }
        });
    }

    private BaseBrick generateOtherBrick() {
        return new TestBrick2(context, new SimpleBrickSize(manager) {
            @Override
            public int getSpans(Context context) {
                return HALF_SPAN;
            }

            @Override
            protected int size() {
                return HALF_SPAN;
            }
        }, new SimpleBrickPadding() {

            @Override
            protected int padding() {
                return PADDING;
            }
        });
    }
}
