package com.wayfair.brickkit;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wayfair.brickkit.behavior.BrickBehavior;
import com.wayfair.brickkit.brick.BaseBrick;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Class which maintains a collection of bricks and manages how they are laid out in an provided RecyclerView.
 *
 * This class maintains the bricks and handles notifying the underlying adapter when items are updated.
 */
public class BrickDataManager implements Serializable {
    private ArrayList<BrickBehavior> behaviors;
    private BrickRecyclerAdapter brickRecyclerAdapter;
    private final int maxSpanCount;
    private LinkedList<BaseBrick> items;
    private LinkedList<BaseBrick> currentlyVisibleItems;
    private boolean dataHasChanged;
    private Context context;

    /**
     * Constructor.
     *  @param context {@link Context} to use
     * @param recyclerView {@link RecyclerView} to put views in
     * @param maxSpanCount max spans used when laying out bricks
     * @param orientation Layout orientation. Should be {@link GridLayoutManager#HORIZONTAL} or {@link GridLayoutManager#VERTICAL}.
     * @param reverse When set to true, layouts from end to start.
     */
    public BrickDataManager(Context context, RecyclerView recyclerView, int maxSpanCount, int orientation, boolean reverse) {
        this.context = context;
        this.maxSpanCount = maxSpanCount;
        this.items = new LinkedList<>();
        this.behaviors = new ArrayList<>();
        this.currentlyVisibleItems = new LinkedList<>();
        this.brickRecyclerAdapter = new BrickRecyclerAdapter(this, recyclerView);

        recyclerView.setAdapter(brickRecyclerAdapter);
        recyclerView.addItemDecoration(new BrickRecyclerItemDecoration(this));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, maxSpanCount, orientation, reverse);
        gridLayoutManager.setSpanSizeLookup(new BrickSpanSizeLookup(context, this));
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    /**
     * Get the items visible in the {@link RecyclerView}.
     *
     * @return LinkedList of visible bricks.
     */
    public LinkedList<BaseBrick> getRecyclerViewItems() {
        if (dataHasChanged) {
            currentlyVisibleItems = new LinkedList<>();

            for (BaseBrick item : items) {
                if (!item.isHidden()) {
                    currentlyVisibleItems.add(item);
                }
            }

            dataHasChanged = false;
        }

        return currentlyVisibleItems;
    }

    /**
     * Get all bricks.
     *
     * @return LinkedList of all bricks.
     */
    public LinkedList<BaseBrick> getDataManagerItems() {
        return items;
    }

    /**
     * Replace current items with new {@link Collection} of bricks.
     *
     * @param items new bricks to be added.
     */
    public void setItems(Collection<BaseBrick> items) {
        clear();

        this.items = new LinkedList<>(items);
        dataHasChanged();
        computePaddingPosition(this.items.getFirst());
        brickRecyclerAdapter.safeNotifyItemRangeInserted(0, getRecyclerViewItems().size());
    }

    /**
     * Inserts brick after all other bricks.
     *
     * @param item the brick to add
     */
    public void addLast(BaseBrick item) {
        this.items.addLast(item);
        if (!item.isHidden()) {
            dataHasChanged();
            int refreshStartIndex = computePaddingPosition(item);
            brickRecyclerAdapter.safeNotifyItemInserted(getRecyclerViewItems().size() - 1);
            brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, getRecyclerViewItems().size() - 1 - refreshStartIndex);
        }
    }

    /**
     * Inserts brick before all other bricks.
     *
     * @param item the brick to add
     */
    public void addFirst(BaseBrick item) {
        this.items.addFirst(item);
        if (!item.isHidden()) {
            dataHasChanged();
            computePaddingPosition(item);
            brickRecyclerAdapter.safeNotifyItemInserted(0);
            brickRecyclerAdapter.safeNotifyItemRangeChanged(1, getRecyclerViewItems().size() - 1);
        }
    }

    /**
     * Inserts collection of bricks after all other bricks.
     *
     * @param items the bricks to add
     */
    public void addLast(Collection<BaseBrick> items) {
        int index = getRecyclerViewItems().size();
        this.items.addAll(items);
        int visibleCount = getVisibleCount(items);
        if (visibleCount > 0) {
            dataHasChanged();
            int refreshStartIndex = computePaddingPosition(getRecyclerViewItems().get(index));
            brickRecyclerAdapter.safeNotifyItemRangeInserted(index, visibleCount);
            brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, getRecyclerViewItems().size() - visibleCount - refreshStartIndex);
        }
    }

    /**
     * Inserts collection of bricks before all other bricks.
     *
     * @param items the bricks to add
     */
    public void addFirst(Collection<BaseBrick> items) {
        this.items.addAll(0, items);
        int visibleCount = getVisibleCount(items);
        if (visibleCount > 0) {
            dataHasChanged();
            computePaddingPosition(this.items.getFirst());
            brickRecyclerAdapter.safeNotifyItemRangeInserted(0, visibleCount);
            brickRecyclerAdapter.safeNotifyItemRangeChanged(visibleCount, getRecyclerViewItems().size() - visibleCount);
        }
    }

    /**
     * Gets count of visible bricks in a collecton of bricks.
     *
     * @param items collection of bricks to get visible count from
     * @return number of visible bricks in the collection
     */
    private int getVisibleCount(Collection<BaseBrick> items) {
        int visibleCount = 0;
        for (BaseBrick brick : items) {
            if (!brick.isHidden()) {
                visibleCount++;
            }
        }

        return visibleCount;
    }

    /**
     * Inserts brick before the anchor brick.
     *
     * @param anchor brick to insert before
     * @param item the brick to add
     */
    public void addBeforeItem(BaseBrick anchor, BaseBrick item) {
        int anchorDataManagerIndex = items.indexOf(anchor);

        if (anchorDataManagerIndex == -1) {
            items.addFirst(item);
        } else {
            items.add(anchorDataManagerIndex, item);
        }

        if (!item.isHidden()) {
            dataHasChanged();
            int refreshStartIndex = computePaddingPosition(item);
            brickRecyclerAdapter.safeNotifyItemInserted(adapterIndex(item));
            brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, getRecyclerViewItems().size() - refreshStartIndex);
        }
    }

    /**
     * Inserts brick after the anchor brick.
     *
     * @param anchor brick to insert after
     * @param item the brick to add
     */
    public void addAfterItem(BaseBrick anchor, BaseBrick item) {
        int anchorDataManagerIndex = this.items.indexOf(anchor);

        if (anchorDataManagerIndex == -1) {
            this.items.addLast(item);
        } else {
            this.items.add(anchorDataManagerIndex + 1, item);
        }

        if (!item.isHidden()) {
            dataHasChanged();
            int refreshStartIndex = computePaddingPosition(item);
            brickRecyclerAdapter.safeNotifyItemInserted(adapterIndex(item));
            brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, getRecyclerViewItems().size() - refreshStartIndex);
        }
    }

    /**
     * Remove a brick.
     *
     * @param item the brick to remove
     */
    public void removeItem(BaseBrick item) {
        this.items.remove(item);

        if (!item.isHidden()) {
            int index = getRecyclerViewItems().indexOf(item);
            dataHasChanged();
            brickRecyclerAdapter.safeNotifyItemRemoved(index);
            if (index < getRecyclerViewItems().size()) {
                int refreshStartIndex = computePaddingPosition(getRecyclerViewItems().get(index));
                brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, getRecyclerViewItems().size() - refreshStartIndex);
            }
        }
    }

    /**
     * Remove a collection of bricks.
     *
     * @param items the bricks to remove
     */
    public void removeItems(Collection<BaseBrick> items) {
        this.items.removeAll(items);
        int visibleCount = getVisibleCount(items);
        if (visibleCount > 0) {
            dataHasChanged();
            if (getRecyclerViewItems().size() > 0) {
                computePaddingPosition(getRecyclerViewItems().getFirst());
            }

            brickRecyclerAdapter.safeNotifyDataSetChanged();
        }
    }

    /**
     * Remove all bricks.
     */
    public void clear() {
        int startCount = getRecyclerViewItems().size();
        this.items = new LinkedList<>();
        dataHasChanged();
        brickRecyclerAdapter.safeNotifyItemRangeRemoved(0, startCount);
    }

    /**
     * Replace a target brick with a replacement.
     *
     * @param target brick to replace
     * @param replacement the brick being added
     */
    public void replaceItem(BaseBrick target, BaseBrick replacement) {
        boolean targetIsHidden = adapterIndex(target) == -1;
        if (targetIsHidden == replacement.isHidden()) {
            if (!target.isHidden()) {
                int index = dataSourceIndex(target);
                items.remove(index);
                items.add(index, replacement);
                dataHasChanged();
                int refreshStartIndex = computePaddingPosition(replacement);
                brickRecyclerAdapter.safeNotifyItemChanged(index);
                brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, getRecyclerViewItems().size() - refreshStartIndex);
            }
        } else if (!targetIsHidden) {
            int refreshStartIndex = computePaddingPosition(target);
            dataHasChanged();
            int index = dataSourceIndex(target);
            items.remove(index);
            items.add(index, replacement);
            brickRecyclerAdapter.safeNotifyItemRemoved(index);
            brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, getRecyclerViewItems().size() - refreshStartIndex);
        } else {
            int index = dataSourceIndex(target);
            items.remove(index);
            items.add(index, replacement);
            dataHasChanged();
            int refreshStartIndex = computePaddingPosition(replacement);
            brickRecyclerAdapter.safeNotifyItemInserted(index);
            brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, getRecyclerViewItems().size() - refreshStartIndex);
        }
    }

    /**
     * Refresh a brick.
     *
     * @param item the brick to refresh
     */
    public void refreshItem(BaseBrick item) {
        boolean wasHidden = currentlyVisibleItems.indexOf(item) == -1;
        if (wasHidden == item.isHidden()) {
            if (!wasHidden) {
                int index = dataSourceIndex(item);
                int refreshStartIndex = computePaddingPosition(item);
                brickRecyclerAdapter.safeNotifyItemChanged(index);
                brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, getRecyclerViewItems().size() - refreshStartIndex);
            }
        } else if (!wasHidden) {
            int refreshStartIndex = computePaddingPosition(item);
            dataHasChanged();
            int index = dataSourceIndex(item);
            brickRecyclerAdapter.safeNotifyItemRemoved(index);
            brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, getRecyclerViewItems().size() - refreshStartIndex);
        } else {
            int index = dataSourceIndex(item);
            dataHasChanged();
            int refreshStartIndex = computePaddingPosition(item);
            brickRecyclerAdapter.safeNotifyItemInserted(index);
            brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, getRecyclerViewItems().size() - refreshStartIndex);
        }
    }

    /**
     * Helper method to tell manager to update the items returned from getRecyclerViewItems().
     */
    private void dataHasChanged() {
        dataHasChanged = true;
        for (BrickBehavior behavior : behaviors) {
            behavior.onDataSetChanged();
        }
    }

    /**
     * Method to get the index of the item in all items.
     *
     * @param item item to get the index of
     * @return index of the item in all items.
     */
    private int dataSourceIndex(BaseBrick item) {
        return items.indexOf(item);
    }

    /**
     * Method to get the index of the item in the visible items.
     *
     * @param item item to get the index of
     * @return index of the item in the visible items.
     */
    private int adapterIndex(BaseBrick item) {
        return getRecyclerViewItems().indexOf(item);
    }

    /**
     * Removes all instances of a given class.
     *
     * @param clazz class to remove all instances of
     */
    public void removeAll(Class clazz) {
        ArrayList<BaseBrick> itemToRemove = new ArrayList<>();

        for (BaseBrick item : this.items) {
            if (clazz.isInstance(item)) {
                itemToRemove.add(item);
            }
        }

        removeItems(itemToRemove);
    }

    /**
     * Checks / Determines if the brick is on the left wall, first row, right wall, last row.
     *
     * @param currentBrick BaseBrick item that was changed / added / removed
     * @return index of first modified item
     */
    private int computePaddingPosition(BaseBrick currentBrick) {
        int currentRow = 0;
        int startingBrickIndex = getRecyclerViewItems().indexOf(currentBrick);

        ListIterator<BaseBrick> iterator = getRecyclerViewItems().listIterator(startingBrickIndex);

        if (!currentBrick.isOnLeftWall()) {
            while (iterator.hasPrevious()) {
                currentBrick = iterator.previous();
                startingBrickIndex--;
                if (currentBrick.isOnLeftWall()) {
                    break;
                }
            }
        }

        boolean topRow = false;
        currentBrick.setOnLeftWall(true);
        currentBrick.setInFirstRow(false);
        currentBrick.setOnRightWall(false);
        currentBrick.setInLastRow(false);

        if (!iterator.hasPrevious()) {
            currentBrick.setInFirstRow(true);
            topRow = true;
        }

        currentRow += currentBrick.getSpanSize().getSpans(context);

        if (currentRow == maxSpanCount) {
            currentBrick.setOnRightWall(true);
        }

        if (currentRow >= maxSpanCount) {
            currentRow = 0;
        }

        currentBrick = iterator.next();
        while (iterator.hasNext()) {
            currentBrick = iterator.next();

            currentBrick.setOnLeftWall(false);
            currentBrick.setInFirstRow(false);
            currentBrick.setOnRightWall(false);
            currentBrick.setInLastRow(false);

            if (currentRow == 0) {
                currentBrick.setOnLeftWall(true);
            }

            if (topRow) {
                currentBrick.setInFirstRow(true);
            }

            currentRow += currentBrick.getSpanSize().getSpans(context);

            if (currentRow == maxSpanCount) {
                currentBrick.setOnRightWall(true);
            }

            if (currentRow >= maxSpanCount) {
                currentRow = 0;
                topRow = false;
            }
        }

        addBottomToRowEndingWithItem(iterator);

        return startingBrickIndex;
    }

    /**
     * Sets all items to being in the last row from the current brick to the left most brick in that row.
     *
     * @param iterator iterator to use to find items in the row
     */
    private void addBottomToRowEndingWithItem(ListIterator<BaseBrick> iterator) {
        while (iterator.hasPrevious()) {
            BaseBrick currentBrick = iterator.previous();

            currentBrick.setInLastRow(true);

            if (currentBrick.isOnLeftWall()) {
                break;
            }
        }
    }

    /**
     * Get the max span count.
     *
     * @return the max span count
     */
    public int getMaxSpanCount() {
        return maxSpanCount;
    }

    /**
     * Add a {@link BrickBehavior}.
     *
     * @param behavior {@link BrickBehavior} to add
     */
    public void addBehavior(BrickBehavior behavior) {
        behaviors.add(behavior);
    }

    /**
     * Get the {@link BrickRecyclerAdapter} for this instance.
     *
     * @return the {@link BrickRecyclerAdapter}.
     */
    public BrickRecyclerAdapter getBrickRecyclerAdapter() {
        return brickRecyclerAdapter;
    }

    /**
     * Method called to release any related resources.
     */
    public void onDestroyView() {
        for (BrickBehavior behavior : behaviors) {
            behavior.detachFromRecyclerView();
        }
    }

    /**
     * Retrieves a brick who's associated layout resource ID matches that of the parameter.
     *
     * @param layoutResId   Layout resource ID
     * @return              An instance of BaseBrick or null
     */
    public BaseBrick brickWithLayout(@LayoutRes int layoutResId) {
        List<BaseBrick> visibleItems = getRecyclerViewItems();
        for (int i = 0; i < visibleItems.size(); i++) {
            if (visibleItems.get(i).getLayout() == layoutResId) {
                return visibleItems.get(i);
            }
        }

        return null;
    }

    /**
     * Retrieves a brick at a specific position.
     *
     * @param position  Position of the brick within the data set
     * @return          An instance of BaseBrick or null
     */
    public BaseBrick brickAtPosition(int position) {
        if (position >= 0 && position < getDataManagerItems().size()) {
            return getDataManagerItems().get(position);
        }
        return null;
    }
}
