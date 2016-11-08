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
    private int prevSpanCount;

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
        brickRecyclerAdapter.safeNotifyItemRangeInserted(0, getRecyclerViewItems().size());
    }

    public void addLast(BaseBrick item) {
        this.items.addLast(item);
        dataHasChanged();
        computePaddingPosition(item);
        brickRecyclerAdapter.safeNotifyItemInserted(getRecyclerViewItems().size());
        brickRecyclerAdapter.safeNotifyItemChanged(getRecyclerViewItems().size()-2);

    }

    public void addFirst(BaseBrick item) {
        this.items.addFirst(item);
        dataHasChanged();
        brickRecyclerAdapter.safeNotifyItemInserted(0);
    }

    public void addLast(Collection<BaseBrick> items) {
        int index = getRecyclerViewItems().size();
        this.items.addAll(items);
        dataHasChanged();
        brickRecyclerAdapter.safeNotifyItemRangeInserted(index, items.size());
    }

    public void addFirst(Collection<BaseBrick> items) {
        this.items.addAll(0, items);
        dataHasChanged();
        brickRecyclerAdapter.safeNotifyItemRangeInserted(0, items.size());
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
            computePaddingPosition(item);
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
            computePaddingPosition(item);
            if (anchorDataManagerIndex == -1) {
                brickRecyclerAdapter.safeNotifyItemInserted(getRecyclerViewItems().size());
            } else {
                int anchorRecyclerViewIndex = getNextVisibleItem(anchor);

                if (anchorRecyclerViewIndex == -1) {
                    brickRecyclerAdapter.safeNotifyItemInserted(getRecyclerViewItems().size());
                } else {
                    brickRecyclerAdapter.safeNotifyItemInserted(anchorRecyclerViewIndex + 1);
                }
            }
        }
    }

    public void removeItem(BaseBrick item) {
        this.items.remove(item);

        if (!item.hidden) {
            int index = getRecyclerViewItems().indexOf(item);
            computePaddingPosition(item);
            dataHasChanged();
            brickRecyclerAdapter.safeNotifyItemRemoved(index);
            brickRecyclerAdapter.safeNotifyItemChanged(index - 1);
        }
    }

    public void removeItems(Collection<BaseBrick> items) {
        this.items.removeAll(items);
        computePaddingPosition(this.items.get(0));
        dataHasChanged();
        brickRecyclerAdapter.safeNotifyDataSetChanged();
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
        computePaddingPosition(replacement);
        dataHasChanged();
        brickRecyclerAdapter.safeNotifyItemChanged(index);
    }

    public void refreshItem(BaseBrick item) {
        dataHasChanged();
        brickRecyclerAdapter.safeNotifyItemChanged(adapterIndex(item));
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
     * @param item   Brick item (from) which we need to compute the position
     */
    private void computePaddingPosition(BaseBrick item) {

        // Get position of item
        int position = items.indexOf(item);

        // If the position is -1, it means it is a remove action
        if (position == -1) {
            position = getRecyclerViewItems().indexOf(item)-1;
            // If the position is still -1 it means first item was removed
            if (position == -1) {
                position = 0;
            }
        }
        prevSpanCount = 0;
        spanCount = 0;
        ListIterator<BaseBrick> it = items.listIterator(position);
        while (it.hasNext()) {
            item = it.next();
            item.isOnLeftWall = false;
            item.isOnRightWallWithExtraSpace = false;
            item.isOnRightWallWithoutExtraSpace = false;
            it = items.listIterator(position);

            spanCount = item.spanSize.getSpans(context);
            while (it.hasPrevious()) {
                BaseBrick prevBrick = it.previous();
                spanCount = spanCount + prevBrick.spanSize.getSpans(context);
                if (prevBrick.isOnLeftWall) {
                    break;
                }

            }


            if (spanCount == item.spanSize.getSpans(context)) {
                item.isOnLeftWall = true;
            }
            it = items.listIterator(position);
            if (spanCount > maxSpanCount) {
                BaseBrick prevBrick = it.previous();
                if (spanCount - item.spanSize.getSpans(context) < maxSpanCount) {
                    prevBrick.isOnRightWallWithExtraSpace = true;
                } else {
                    prevBrick.isOnRightWallWithoutExtraSpace = true;
                }
                item.isOnLeftWall = true;
                prevSpanCount = spanCount;
                spanCount = item.spanSize.getSpans(context);
                if (it.hasPrevious()) {
                    if (prevSpanCount - item.spanSize.getSpans(context) - prevBrick.spanSize.getSpans(context) > 0) {
                        prevBrick = it.previous();
                        prevBrick.isOnRightWallWithExtraSpace = false;
                        prevBrick.isOnRightWallWithoutExtraSpace = false;
                    }
                }
            }

            it = items.listIterator(position);

            if (!it.hasPrevious()) {
                item.isInFirstRow = true;
            } else {
                BaseBrick prev = it.previous();
                if (!prev.isInFirstRow || spanCount > maxSpanCount || spanCount + prevSpanCount > maxSpanCount) {
                    item.isInFirstRow = false;
                } else {
                    item.isInFirstRow = true;
                }
            }
            it = items.listIterator(position);
            it.next();
            if (!it.hasNext()) {
                if (spanCount == maxSpanCount) {
                    item.isOnRightWallWithoutExtraSpace = true;
                } else {
                    item.isOnRightWallWithExtraSpace = true;
                }
                item.isInLastRow = true;
                it.previous();
                while (!item.isOnLeftWall && it.hasPrevious()) {
                    BaseBrick prevBrick = it.previous();
                    prevBrick.isInLastRow = true;
                    prevBrick.isOnRightWallWithExtraSpace = false;
                    prevBrick.isOnRightWallWithoutExtraSpace = false;
                    if (prevBrick.isOnLeftWall) {
                        break;
                    }

                }
                while (it.hasPrevious()) {
                    BaseBrick prevBrick = it.previous();
                    prevBrick.isInLastRow = false;
                    if (prevBrick.isOnLeftWall) {
                        break;
                    }
                }
            }
            position++;
            it = items.listIterator(position);
        }

    }
}
