package com.wayfair.bricks;

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
    int maxSpanCount = 1;
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

            for (int i = 0; i < items.size(); i++) {
                if (items.get(i) != null && !items.get(i).hidden) {
                    currentlyVisibleItems.add(items.get(i));
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
        computePaddingPosition(0);
        brickRecyclerAdapter.safeNotifyItemRangeInserted(0, getRecyclerViewItems().size());
    }

    public void addLast(BaseBrick item) {
        this.items.addLast(item);
        dataHasChanged();
        int refreshStartBrick = computePaddingPosition(getRecyclerViewItems().size() - 1);
        brickRecyclerAdapter.safeNotifyItemInserted(getRecyclerViewItems().size());
        brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartBrick, getRecyclerViewItems().size());
    }

    public void addFirst(BaseBrick item) {
        this.items.addFirst(item);
        dataHasChanged();
        computePaddingPosition(0);
        brickRecyclerAdapter.safeNotifyItemInserted(0);
        brickRecyclerAdapter.safeNotifyItemRangeChanged(0, getRecyclerViewItems().size());
    }

    public void addLast(Collection<BaseBrick> items) {
        int index = getRecyclerViewItems().size();
        this.items.addAll(items);
        dataHasChanged();
        int refreshStartBrick = computePaddingPosition(index);
        brickRecyclerAdapter.safeNotifyItemRangeInserted(index, items.size());
        brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartBrick, getRecyclerViewItems().size());
    }

    public void addFirst(Collection<BaseBrick> items) {
        this.items.addAll(0, items);
        dataHasChanged();
        computePaddingPosition(0);
        brickRecyclerAdapter.safeNotifyItemRangeInserted(0, items.size());
        brickRecyclerAdapter.safeNotifyItemRangeChanged(0, getRecyclerViewItems().size());
    }

    public void addBeforeItem(BaseBrick anchor, BaseBrick item) {
        int anchorDataManagerIndex = this.items.indexOf(anchor);

        if (anchorDataManagerIndex == -1) {
            this.items.addFirst(item);
        } else {
            this.items.add(anchorDataManagerIndex, item);
        }

        if (!item.hidden) {
            dataHasChanged();
            if (anchorDataManagerIndex == -1) {
                computePaddingPosition(0);
                brickRecyclerAdapter.safeNotifyItemInserted(0);
                brickRecyclerAdapter.safeNotifyItemRangeChanged(0, getRecyclerViewItems().size());
            } else {
                int anchorRecyclerViewIndex = getPreviousVisibleItem(anchor);

                if (anchorRecyclerViewIndex == -1) {
                    computePaddingPosition(0);
                    brickRecyclerAdapter.safeNotifyItemInserted(0);
                    brickRecyclerAdapter.safeNotifyItemRangeChanged(0, getRecyclerViewItems().size());
                } else {
                    int refreshStartBrick = computePaddingPosition(anchorRecyclerViewIndex - 1);
                    brickRecyclerAdapter.safeNotifyItemInserted(anchorRecyclerViewIndex - 1);
                    brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartBrick, getRecyclerViewItems().size());
                }
            }
        }
    }

    public void addAfterItem(BaseBrick anchor, BaseBrick item) {
        int anchorDataManagerIndex = this.items.indexOf(anchor);

        if (anchorDataManagerIndex == -1) {
            this.items.addLast(item);
        } else {
            this.items.add(anchorDataManagerIndex + 1, item);
        }

        if (!item.hidden) {
            dataHasChanged();

            if (anchorDataManagerIndex == -1) {
                int refreshStartBrick = computePaddingPosition(getRecyclerViewItems().size() - 1);
                brickRecyclerAdapter.safeNotifyItemInserted(getRecyclerViewItems().size());
                brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartBrick, getRecyclerViewItems().size());
            } else {
                int anchorRecyclerViewIndex = getNextVisibleItem(anchor);

                if (anchorRecyclerViewIndex == -1) {
                    int refreshStartBrick = computePaddingPosition(getRecyclerViewItems().size() - 1);
                    brickRecyclerAdapter.safeNotifyItemInserted(getRecyclerViewItems().size());
                    brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartBrick, getRecyclerViewItems().size());
                } else {
                    int refreshStartBrick = computePaddingPosition(anchorRecyclerViewIndex + 1);
                    brickRecyclerAdapter.safeNotifyItemInserted(anchorRecyclerViewIndex + 1);
                    brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartBrick, getRecyclerViewItems().size());
                }
            }
        }
    }

    public void removeItem(BaseBrick item) {
        this.items.remove(item);

        if (!item.hidden) {
            int index = getRecyclerViewItems().indexOf(item);
            dataHasChanged();
            int refreshStartBrick = computePaddingPosition(index - 1);
            brickRecyclerAdapter.safeNotifyItemRemoved(index);
            brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartBrick, getRecyclerViewItems().size());
        }
    }

    public void removeItems(Collection<BaseBrick> items) {
        this.items.removeAll(items);
        dataHasChanged();
        computePaddingPosition(0);
        brickRecyclerAdapter.safeNotifyDataSetChanged();
        brickRecyclerAdapter.safeNotifyItemRangeChanged(0, getRecyclerViewItems().size());
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
        int refreshStartBrick = computePaddingPosition(index);
        brickRecyclerAdapter.safeNotifyItemChanged(index);
        brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartBrick, getRecyclerViewItems().size());
    }

    public void refreshItem(BaseBrick item) {
        dataHasChanged();
        int refreshStartBrick = computePaddingPosition(getRecyclerViewItems().indexOf(item));
        brickRecyclerAdapter.safeNotifyItemChanged(adapterIndex(item));
        brickRecyclerAdapter.safeNotifyItemRangeChanged(refreshStartBrick, getRecyclerViewItems().size());
    }

    public void dataHasChanged() {
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

    public int getPreviousVisibleItem(BaseBrick item) {
        if (!item.hidden) {
            return getRecyclerViewItems().indexOf(item);
        } else {
            BaseBrick baseBrick;
            ListIterator<BaseBrick> listIterator = this.items.listIterator(this.items.indexOf(item));
            while (listIterator.hasPrevious()) {
                baseBrick = listIterator.previous();
                if (!baseBrick.hidden) {
                    return getRecyclerViewItems().indexOf(baseBrick);
                }
            }
        }

        return -1;
    }

    public int getNextVisibleItem(BaseBrick item) {
        if (!item.hidden) {
            return getRecyclerViewItems().indexOf(item);
        } else {
            BaseBrick baseBrick;
            ListIterator<BaseBrick> listIterator = this.items.listIterator(this.items.indexOf(item));
            while (listIterator.hasNext()) {
                baseBrick = listIterator.next();
                if (!baseBrick.hidden) {
                    return getRecyclerViewItems().indexOf(baseBrick);
                }
            }
        }

        return -1;
    }

    /**
     * Checks / Determines if the brick is on the left wall, first row, right wall, last row
     *
     * @param position Position in the adapter of the brick
     */
    private int computePaddingPosition(int position) {
        if (position == -1) {
            position = 0;
        }
        int changedBrickStart = 0;
        ListIterator<BaseBrick> it = items.listIterator(position);
        while (it.hasNext()) {
            BaseBrick item = it.next();

            item.isOnLeftWall = false;
            item.isOnRightWall = false;

            computeRowSpanCount(item, position);

            checkLeftWall(item);
            checkRightWall(item);

            checkFirstRow(item, it);

            changedBrickStart = checkLastRow(item, it);

            position++;
            it = items.listIterator(position);
        }
        return changedBrickStart;
    }

    private void computeRowSpanCount(BaseBrick item, int position) {
        ListIterator<BaseBrick> it = items.listIterator(position);
        spanCount = item.spanSize.getSpans(context);
        while (it.hasPrevious()) {
            BaseBrick prevBrick = it.previous();
            spanCount = spanCount + prevBrick.spanSize.getSpans(context);
            if (prevBrick.isOnLeftWall) {
                break;
            }
        }
    }

    private void checkLeftWall(BaseBrick item) {
        if (spanCount == item.spanSize.getSpans(context) || spanCount > maxSpanCount) {
            item.isOnLeftWall = true;
        }
    }

    private void checkRightWall(BaseBrick item) {
        if (spanCount == maxSpanCount) {
            item.isOnRightWall = true;
        }
    }

    private void checkFirstRow(BaseBrick item, ListIterator<BaseBrick> it) {
        it.previous();
        if (!it.hasPrevious()) {
            item.isInFirstRow = true;
        } else {
            BaseBrick prev = it.previous();
            if (!prev.isInFirstRow || spanCount > maxSpanCount ) {
                item.isInFirstRow = false;
            } else {
                item.isInFirstRow = true;
            }
            it.next();
        }
    }

    private int checkLastRow(BaseBrick item, ListIterator<BaseBrick> it) {
        int refreshIndex = 0;
        refreshIndex = items.indexOf(item);

        it.next();
        if (!it.hasNext()) {
            item.isInLastRow = true;
            it.previous();
            // Inform every brick in last row
            while (!item.isOnLeftWall && it.hasPrevious()) {
                BaseBrick prevBrick = it.previous();
                prevBrick.isInLastRow = true;
                if (prevBrick.isOnLeftWall) {
                    refreshIndex = items.indexOf(prevBrick);
                    break;
                }
            }

            // Inform every brick in second last row that they are no longer last row
            while (it.hasPrevious()) {
                BaseBrick prevBrick = it.previous();
                prevBrick.isInLastRow = false;
                if (prevBrick.isOnLeftWall) {
                    refreshIndex = items.indexOf(prevBrick);
                    break;
                }
            }
        }
        return refreshIndex;
    }
}
