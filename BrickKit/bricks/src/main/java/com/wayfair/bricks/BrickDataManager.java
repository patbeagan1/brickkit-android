package com.wayfair.bricks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class BrickDataManager implements Serializable {
    int maxSpanCount = 1;
    private LinkedList<BaseBrick> items;
    private LinkedList<BaseBrick> currentlyVisibleItems;
    private boolean dataHasChanged;
    public ArrayList<BrickBehaviour> behaviours;
    public BrickRecyclerAdapter brickRecyclerAdapter;
    private Context context;
    private  int spanCount;
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
        checkPadding(item, items.size()-1);
        brickRecyclerAdapter.safeNotifyItemInserted(getRecyclerViewItems().size());

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
            checkPadding(item, 0);
        } else {
            this.items.add(anchorDataManagerIndex, item);
            checkPadding(item, anchorDataManagerIndex);
        }

        if (!item.hidden) {
            dataHasChanged();

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
            dataHasChanged();
            brickRecyclerAdapter.safeNotifyItemRemoved(index);
        }
    }

    public void removeItems(Collection<BaseBrick> items) {
        this.items.removeAll(items);
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

    private void checkPadding(BaseBrick item, int position ) {

        prevSpanCount = 0;
        spanCount = 0;

            ListIterator<BaseBrick> it = items.listIterator(position);
            while (it.hasNext()) {
                item = it.next();


//            item = items.get(position);
            item.isOnLeftWall = false;
            item.isOnRightWallWithExtraSpace=false;
            item.isOnRightWallWithoutExtraSpace=false;
            it = items.listIterator(position);

            spanCount = item.spanSize.getSpans(context);
            while (it.hasPrevious()){
                BaseBrick prevBrick = it.previous();
                spanCount = spanCount + prevBrick.spanSize.getSpans(context);
                if(prevBrick.isOnLeftWall){
                    break;
                }

            }


            if (spanCount == item.spanSize.getSpans(context)) {
                item.isOnLeftWall = true;
            }
            Log.wtf("Kunal SC",position +" "+items.size() +" "+ spanCount +item.spanSize.getSpans(context));
            it = items.listIterator(position);
            if (spanCount > maxSpanCount) {
                if(spanCount - item.spanSize.getSpans(context) < maxSpanCount){
                    it.previous().isOnRightWallWithExtraSpace = true;
                } else {
                    it.previous().isOnRightWallWithoutExtraSpace = true;
                }
//                it.previous().isOnRightWall = true;
                item.isOnLeftWall = true;
                prevSpanCount = spanCount;
                spanCount = item.spanSize.getSpans(context);
                //it.previous().isOnRightWall= false;
                if (it.hasPrevious()){
                    BaseBrick prevBrick = it.previous();
                    prevBrick.isOnRightWallWithExtraSpace = false;
                    prevBrick.isOnRightWallWithoutExtraSpace = false;
                }


            }

            Log.wtf("Kunal SC",position +" "+items.size() +" "+ spanCount);
            it = items.listIterator(position);

            if (!it.hasPrevious()) {
                item.isInFirstRow = true;
            } else {
                BaseBrick prev = it.previous();
                if (!prev.isInFirstRow || spanCount > maxSpanCount || spanCount + prevSpanCount > maxSpanCount){
                    item.isInFirstRow = false;
                } else {
                    item.isInFirstRow = true;
                }
            }
            it = items.listIterator(position);
            it.next();
            if (!it.hasNext()) {
                if(spanCount - item.spanSize.getSpans(context) < maxSpanCount){
                    item.isOnRightWallWithExtraSpace = true;
                } else {
                    item.isOnRightWallWithoutExtraSpace = true;
                }
                item.isInLastRow = true;
                it.previous();
                while (!item.isOnLeftWall && it.hasPrevious()){
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
