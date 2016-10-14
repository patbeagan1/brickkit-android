package com.wayfair.bricks;

import java.io.Serializable;
import java.util.ArrayList;

public class BrickDataManager implements Serializable {
    int maxSpanCount = 1;
    private ArrayList<BaseBrick> items;
    private ArrayList<BaseBrick> currentlyVisibleItems;
    private boolean dataHasChanged;
    StickyHelperCallback stickyHeaderCallback;
    StickyHelperCallback stickyFooterCallback;

    public BrickDataManager(int maxSpanCount) {
        this.maxSpanCount = maxSpanCount;
        this.items = new ArrayList<>();
        this.currentlyVisibleItems = new ArrayList<>();
    }

    public ArrayList<BaseBrick> getItems() {
        if (dataHasChanged) {
            currentlyVisibleItems = new ArrayList<>();

            for (int i = 0; i < items.size(); i++) {
                if (items.get(i) != null && !items.get(i).hidden) {
                    currentlyVisibleItems.add(items.get(i));
                }
            }

            dataHasChanged = false;
        }

        return currentlyVisibleItems;
    }

    public void setItems(ArrayList<BaseBrick> items) {
        this.items = items;
        dataHasChanged();
    }

    public void addItem(BaseBrick item) {
        items.add(item);
        dataHasChanged();
    }

    public void addItem(int index, BaseBrick item) {
        items.add(index, item);
        dataHasChanged();
    }

    public void removeItem(BaseBrick item) {
        items.remove(item);
        dataHasChanged();
    }

    public void removeItem(int index) {
        items.remove(index);
        dataHasChanged();
    }

    public void addItems(ArrayList<BaseBrick> items) {
        this.items.addAll(items);
        dataHasChanged();
    }

    public void addItems(int index, ArrayList<BaseBrick> items) {
        this.items.addAll(index, items);
        dataHasChanged();
    }

    public void removeItems(int start, int count) {
        for (int i = start + count - 1; i >= start; i--) {
            items.remove(i);
        }
        dataHasChanged();
    }

    public void removeItems(ArrayList<BaseBrick> items) {
        this.items.removeAll(items);
        dataHasChanged();
    }

    public void clear() {
        this.items.clear();
        dataHasChanged();
    }

    public void replaceItem(int index, BaseBrick replacement) {
        this.items.remove(index);
        this.items.add(index, replacement);
        dataHasChanged();
    }

    public void replaceItem(BaseBrick target, BaseBrick replacement) {
        int position = this.items.indexOf(target);
        this.items.remove(target);
        this.items.add(position, replacement);
        dataHasChanged();
    }

    public void dataHasChanged() {
        dataHasChanged = true;
        stickyHeaderCallback.updateStickyItem();
        stickyFooterCallback.updateStickyItem();
    }

    public int dataSourceIndex(BaseBrick item) {
        return items.indexOf(item);
    }

    public int adapterIndex(BaseBrick item) {
        return getItems().indexOf(item);
    }

    public void removeAll(Class clazz) {
        for (int i = items.size() - 1; i > 0; i--) {
            if (clazz.isInstance(items.get(i))) {
                removeItem(i);
            }
        }
    }

    public int adapterSize() {
        return currentlyVisibleItems.size();
    }

    public int dataSourceSize() {
        return items.size();
    }
}
