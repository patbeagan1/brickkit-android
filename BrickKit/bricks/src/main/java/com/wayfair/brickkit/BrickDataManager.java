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
            int refreshStartIndex = computePaddingPosition(item);
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
            int refreshStartIndex = computePaddingPosition(getRecyclerViewItems().get(index));
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
            int refreshStartIndex = computePaddingPosition(item);
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
            int refreshStartIndex = computePaddingPosition(item);
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
                int refreshStartIndex = computePaddingPosition(getRecyclerViewItems().get(index));
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
        int refreshStartIndex = computePaddingPosition(replacement);
        brickRecyclerAdapter.safeNotifyItemChanged(index);
        brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartIndex, getRecyclerViewItems().size() - refreshStartIndex);
    }

    public void refreshItem(BaseBrick item) {
        dataHasChanged();
        int refreshStartIndex = computePaddingPosition(item);
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
     * @param currentBrick BaseBrick item that was changed / added / removed
     */
    private int computePaddingPosition(BaseBrick currentBrick) {
        int currentRow = 0;
        int startingBrickIndex = getRecyclerViewItems().indexOf(currentBrick);

        if (startingBrickIndex >= 0) {
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
            if (!currentBrick.isInLastRow()) {
                currentBrick.setInLastRow(true);
            }

            currentBrick = iterator.previous();
            addBottomToRowEndingWithItem(iterator, currentBrick);
        }

        return startingBrickIndex;
    }

    private void addBottomToRowEndingWithItem(ListIterator<BaseBrick> iterator, BaseBrick currentBrick) {
        while (iterator.hasPrevious()) {
            currentBrick = iterator.previous();

            currentBrick.setInLastRow(true);

            if (currentBrick.isOnLeftWall()) {
                removeBottomFromRowBeforeItem(iterator, currentBrick);
                break;
            }
        }
    }

    private void removeBottomFromRowBeforeItem(ListIterator<BaseBrick> iterator, BaseBrick currentBrick) {
        while (iterator.hasPrevious()) {
            currentBrick = iterator.previous();

            currentBrick.setInLastRow(false);

            if (currentBrick.isOnLeftWall()) {
                break;
            }
        }
    }

    public int getMaxSpanCount() {
        return maxSpanCount;
    }
}
