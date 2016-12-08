package com.wayfair.brickkit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.ListIterator;

public class BrickDataManager implements Serializable {
    public ArrayList<BrickBehaviour> behaviours;
    public BrickRecyclerAdapter brickRecyclerAdapter;
    private final int maxSpanCount;
    private LinkedList<BaseBrick> items;
    private LinkedList<BaseBrick> currentlyVisibleItems;
    private boolean dataHasChanged;
    private Context context;
    private int spanCount;

    public BrickDataManager(Context context, RecyclerView recyclerView, int maxSpanCount) {
        this.context = context;
        this.maxSpanCount = maxSpanCount;
        this.items = new LinkedList<>();
        this.behaviours = new ArrayList<>();
        this.currentlyVisibleItems = new LinkedList<>();
        this.brickRecyclerAdapter = new BrickRecyclerAdapter(this, recyclerView);

        recyclerView.setAdapter(brickRecyclerAdapter);
        recyclerView.addItemDecoration(new BrickRecyclerItemDecoration(this));
    }

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

    public LinkedList<BaseBrick> getDataManagerItems() {
        return items;
    }

    public void setItems(Collection<BaseBrick> items) {
        clear();

        this.items = new LinkedList<>(items);
        dataHasChanged();
        computePaddingPosition(this.items.getFirst());
        brickRecyclerAdapter.safeNotifyItemRangeInserted(0, getRecyclerViewItems().size());
    }

    public void addLast(BaseBrick item) {
        this.items.addLast(item);
        if (!item.isHidden()) {
            dataHasChanged();
            BaseBrick refreshStartBrick = computePaddingPosition(item);
            int refreshStartIndex = getRecyclerViewItems().indexOf(refreshStartBrick);
            brickRecyclerAdapter.safeNotifyItemInserted(getRecyclerViewItems().size() - 1);
            brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, getRecyclerViewItems().size() - 1 - refreshStartIndex);
        }
    }

    public void addFirst(BaseBrick item) {
        this.items.addFirst(item);
        if (!item.isHidden()) {
            dataHasChanged();
            computePaddingPosition(item);
            brickRecyclerAdapter.safeNotifyItemInserted(0);
            brickRecyclerAdapter.safeNotifyItemRangeChanged(1, getRecyclerViewItems().size() - 1);
        }
    }

    public void addLast(Collection<BaseBrick> items) {
        int index = getRecyclerViewItems().size();
        this.items.addAll(items);
        int visibleCount = getVisibleCount(items);
        if (visibleCount > 0) {
            dataHasChanged();
            BaseBrick refreshStartBrick = computePaddingPosition(getRecyclerViewItems().get(index));
            int refreshStartIndex = getRecyclerViewItems().indexOf(refreshStartBrick);
            brickRecyclerAdapter.safeNotifyItemRangeInserted(index, visibleCount);
            brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, getRecyclerViewItems().size() - visibleCount - refreshStartIndex);
        }
    }

    private int getVisibleCount(Collection<BaseBrick> items) {
        int visibleCount = 0;
        for (BaseBrick brick : items) {
            if (!brick.isHidden()) {
                visibleCount++;
            }
        }

        return visibleCount;
    }

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

    public void addBeforeItem(BaseBrick anchor, BaseBrick item) {
        int anchorDataManagerIndex = items.indexOf(anchor);

        if (anchorDataManagerIndex == -1) {
            items.addFirst(item);
        } else {
            items.add(anchorDataManagerIndex, item);
        }

        if (!item.isHidden()) {
            dataHasChanged();
            BaseBrick refreshStartBrick = computePaddingPosition(item);
            int refreshStartIndex = getRecyclerViewItems().indexOf(refreshStartBrick);
            if (anchorDataManagerIndex == -1) {
                brickRecyclerAdapter.safeNotifyItemInserted(0);
            } else {
                int anchorRecyclerViewIndex = getPreviousVisibleItem(anchor);
                if (anchorRecyclerViewIndex == -1) {
                    brickRecyclerAdapter.safeNotifyItemInserted(0);
                } else {
                    brickRecyclerAdapter.safeNotifyItemInserted(anchorRecyclerViewIndex - 1);
                }
            }
            brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, getRecyclerViewItems().size() - refreshStartIndex);
        }
    }

    public void addAfterItem(BaseBrick anchor, BaseBrick item) {
        int anchorDataManagerIndex = this.items.indexOf(anchor);

        if (anchorDataManagerIndex == -1) {
            this.items.addLast(item);
        } else {
            this.items.add(anchorDataManagerIndex + 1, item);
        }

        if (!item.isHidden()) {
            dataHasChanged();
            BaseBrick refreshStartBrick = computePaddingPosition(item);
            int refreshStartIndex = getRecyclerViewItems().indexOf(refreshStartBrick);
            if (anchorDataManagerIndex == -1) {
                brickRecyclerAdapter.safeNotifyItemInserted(getRecyclerViewItems().size() - 1);
            } else {
                int anchorRecyclerViewIndex = getNextVisibleItem(anchor);
                if (anchorRecyclerViewIndex == -1) {
                    brickRecyclerAdapter.safeNotifyItemInserted(getRecyclerViewItems().size());
                } else {
                    brickRecyclerAdapter.safeNotifyItemInserted(anchorRecyclerViewIndex + 1);
                }
            }
            brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, getRecyclerViewItems().size() - refreshStartIndex);
        }
    }

    public void removeItem(BaseBrick item) {
        this.items.remove(item);

        if (!item.isHidden()) {
            int index = getRecyclerViewItems().indexOf(item);
            dataHasChanged();
            brickRecyclerAdapter.safeNotifyItemRemoved(index);
            if (index < getRecyclerViewItems().size()) {
                BaseBrick refreshStartBrick = computePaddingPosition(getRecyclerViewItems().get(index));
                int refreshStartIndex = getRecyclerViewItems().indexOf(refreshStartBrick);
                brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, getRecyclerViewItems().size() - refreshStartIndex);
            }
        }
    }

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

    public void clear() {
        int startCount = getRecyclerViewItems().size();
        this.items = new LinkedList<>();
        dataHasChanged();
        brickRecyclerAdapter.safeNotifyItemRangeRemoved(0, startCount);
    }

    public void replaceItem(BaseBrick target, BaseBrick replacement) {
        int index = this.items.indexOf(target);
        this.items.remove(index);
        this.items.add(index, replacement);
        dataHasChanged();
        BaseBrick refreshStartBrick = computePaddingPosition(replacement);
        int refreshStartIndex = getRecyclerViewItems().indexOf(refreshStartBrick);
        brickRecyclerAdapter.safeNotifyItemChanged(index);
        brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, getRecyclerViewItems().size() - refreshStartIndex);
    }

    public void refreshItem(BaseBrick item) {
        dataHasChanged();
        BaseBrick refreshStartBrick = computePaddingPosition(item);
        int refreshStartIndex = getRecyclerViewItems().indexOf(refreshStartBrick);
        brickRecyclerAdapter.safeNotifyItemChanged(adapterIndex(item));
        brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, getRecyclerViewItems().size() - refreshStartIndex);
    }

    private void dataHasChanged() {
        dataHasChanged = true;
        for (BrickBehaviour behaviour : behaviours) {
            behaviour.onDataSetChanged();
        }
    }

    public int dataSourceIndex(BaseBrick item) {
        return items.indexOf(item);
    }

    public int adapterIndex(BaseBrick item) {
        return getRecyclerViewItems().indexOf(item);
    }

    public void removeAll(Class clazz) {
        ArrayList<BaseBrick> itemToRemove = new ArrayList<>();

        for (BaseBrick item : this.items) {
            if (clazz.isInstance(item)) {
                itemToRemove.add(item);
            }
        }

        removeItems(itemToRemove);
    }

    private int getPreviousVisibleItem(BaseBrick item) {
        if (!item.isHidden()) {
            return getRecyclerViewItems().indexOf(item);
        } else {
            BaseBrick baseBrick;
            ListIterator<BaseBrick> listIterator = this.items.listIterator(this.items.indexOf(item));
            while (listIterator.hasPrevious()) {
                baseBrick = listIterator.previous();
                if (!baseBrick.isHidden()) {
                    return getRecyclerViewItems().indexOf(baseBrick);
                }
            }
        }

        return -1;
    }

    private int getNextVisibleItem(BaseBrick item) {
        if (!item.isHidden()) {
            return getRecyclerViewItems().indexOf(item);
        } else {
            BaseBrick baseBrick;
            ListIterator<BaseBrick> listIterator = this.items.listIterator(this.items.indexOf(item));
            while (listIterator.hasNext()) {
                baseBrick = listIterator.next();
                if (!baseBrick.isHidden()) {
                    return getRecyclerViewItems().indexOf(baseBrick);
                }
            }
        }

        return -1;
    }

    /**
     * Checks / Determines if the brick is on the left wall, first row, right wall, last row
     *
     * @param item BaseBrick item that was changed / added / removed
     */
    private BaseBrick computePaddingPosition(final BaseBrick item) {

        int position = getRecyclerViewItems().indexOf(item);
        BaseBrick leftMostItemVisited = item;
        ListIterator<BaseBrick> it = getRecyclerViewItems().listIterator(position);
        spanCount = 0;
        while (it.hasPrevious()) {
            BaseBrick prev = it.previous();
            position--;
            if (prev.isOnLeftWall()) {
                break;
            }
        }

        it = getRecyclerViewItems().listIterator(position);
        while (it.hasNext()) {
            BaseBrick iteratorItem = it.next();

            iteratorItem.setOnLeftWall(false);
            iteratorItem.setOnRightWall(false);

            spanCount += iteratorItem.getSpanSize().getSpans(context);

            checkLeftWall(iteratorItem);
            checkRightWall(iteratorItem);

            checkFirstRow(iteratorItem, it);

            leftMostItemVisited = checkLastRow(iteratorItem, it);

            if (spanCount > maxSpanCount) {
                spanCount = iteratorItem.getSpanSize().getSpans(context);
            }

            position++;
            it = getRecyclerViewItems().listIterator(position);
        }
        return leftMostItemVisited;
    }

    private void checkLeftWall(BaseBrick item) {
        if (spanCount == item.getSpanSize().getSpans(context) || spanCount > maxSpanCount) {
            item.setOnLeftWall(true);
        }
    }

    private void checkRightWall(BaseBrick item) {
        if (spanCount == maxSpanCount) {
            item.setOnRightWall(true);
        }
    }

    private void checkFirstRow(BaseBrick item, ListIterator<BaseBrick> it) {
        it.previous();
        if (!it.hasPrevious()) {
            item.setInFirstRow(true);
        } else {
            BaseBrick prev = it.previous();
            if (!prev.isInFirstRow() || spanCount > maxSpanCount || spanCount == item.getSpanSize().getSpans(context)) {
                item.setInFirstRow(false);
            } else {
                item.setInFirstRow(true);
            }
            it.next();
        }
    }

    private BaseBrick checkLastRow(BaseBrick item, ListIterator<BaseBrick> it) {
        BaseBrick leftMostItemVisited = item;

        it.next();
        if (!it.hasNext()) {
            item.setInLastRow(true);
            it.previous();
            // Inform every brick in last row
            while (!item.isOnLeftWall() && it.hasPrevious()) {
                BaseBrick prevBrick = it.previous();
                prevBrick.setInLastRow(true);
                if (prevBrick.isOnLeftWall()) {
                    leftMostItemVisited = prevBrick;
                    break;
                }
            }

            // Inform every brick in second last row that they are no longer last row
            while (it.hasPrevious()) {
                BaseBrick prevBrick = it.previous();
                prevBrick.setInLastRow(false);
                if (prevBrick.isOnLeftWall()) {
                    leftMostItemVisited = prevBrick;
                    break;
                }
            }
        }
        return leftMostItemVisited;
    }

    public int getMaxSpanCount() {
        return maxSpanCount;
    }
}
