package com.wayfair.bricks;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

public class BrickRecyclerAdapter extends RecyclerView.Adapter<BrickViewHolder> {
    public final BrickDataManager dataManager;
    private OnReachedItemAtPosition onReachedItemAtPosition;
    public RecyclerView recyclerView;
    private Handler handler = new Handler();


    public BrickRecyclerAdapter(BrickDataManager brickDataManager, RecyclerView recyclerView) {
        assert recyclerView != null;
        this.recyclerView = recyclerView;
        this.dataManager = brickDataManager;
    }

    public void safeNotifyDataSetChanged() {
        if (recyclerView != null && recyclerView.isComputingLayout()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            };
            handler.post(runnable);
        } else {
            notifyDataSetChanged();
        }
    }

    public void safeNotifyItemChanged(final int position, final Object payload) {
        if (recyclerView != null && recyclerView.isComputingLayout()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    notifyItemChanged(position, payload);
                }
            };
            handler.post(runnable);
        } else {
            notifyItemChanged(position, payload);
        }

    }

    public void safeNotifyItemChanged(final int position) {
        if (recyclerView != null && recyclerView.isComputingLayout()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    notifyItemChanged(position);
                }
            };
            handler.post(runnable);
        } else {
            notifyItemChanged(position);
        }
    }

    public void safeNotifyItemInserted(final int position) {
        if (recyclerView != null && recyclerView.isComputingLayout()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    notifyItemInserted(position);
                }
            };
            handler.post(runnable);
        } else {
            notifyItemInserted(position);
        }
    }

    public void safeNotifyItemMoved(final int fromPosition, final int toPosition) {
        if (recyclerView != null && recyclerView.isComputingLayout()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    notifyItemMoved(fromPosition, toPosition);
                }
            };
            handler.post(runnable);
        } else {
            notifyItemMoved(fromPosition, toPosition);
        }
    }
    public void safeNotifyItemRangeChanged(int positionStart, int itemCount, Object payload) {
        if (recyclerView != null && recyclerView.isComputingLayout()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            };
            handler.post(runnable);
        } else {
            notifyDataSetChanged();
        }
        notifyItemRangeChanged(positionStart, itemCount, payload);
    }

    public void safeNotifyItemRangeChanged(final int positionStart, final int itemCount) {
        if (recyclerView != null && recyclerView.isComputingLayout()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    notifyItemRangeChanged(positionStart, itemCount);
                }
            };
            handler.post(runnable);
        } else {
            notifyItemRangeChanged(positionStart, itemCount);
        }
    }

    public void safeNotifyItemRangeInserted(final int positionStart, final int itemCount) {
        if (recyclerView != null && recyclerView.isComputingLayout()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    notifyItemRangeInserted(positionStart, itemCount);
                }
            };
            handler.post(runnable);
        } else {
            notifyItemRangeInserted(positionStart, itemCount);
        }
    }

    public void safeNotifyItemRangeRemoved(final int positionStart, final int itemCount) {
        if (recyclerView != null && recyclerView.isComputingLayout()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    notifyItemRangeRemoved(positionStart, itemCount);
                }
            };
            handler.post(runnable);
        } else {
            notifyItemRangeRemoved(positionStart, itemCount);
        }
    }

    public void safeNotifyItemRemoved(final int position) {
        if (recyclerView != null && recyclerView.isComputingLayout()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    notifyItemRemoved(position);
                }
            };
            handler.post(runnable);
        } else {
            notifyItemRemoved(position);
        }
    }

    @Override
    public BrickViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolderRegistry.mapToRecyclerView(TemplateRegistry.getInstance().get(viewType), parent);
    }

    @Override
    public void onBindViewHolder(BrickViewHolder holder, int position) {
        BaseBrick brickController = get(holder.getLayoutPosition());

        if (brickController != null) {
            brickController.onBindData(holder);
            if (onReachedItemAtPosition != null) {
                onReachedItemAtPosition.bindingItemAtPosition(position);
            }
        }
    }

    @Override
    public void onViewDetachedFromWindow(BrickViewHolder holder) {
        holder.onViewDetachedFromWindow();
    }

    @Override
    public void onViewRecycled(BrickViewHolder holder) {
        holder.onViewRecycled();
    }

    @Override
    public int getItemCount() {
        return dataManager.getItems().size();
    }

    @Override
    public int getItemViewType(int position) {
        if (get(position) == null) {
            return 0;
        }

        return TemplateRegistry.getInstance().get(get(position).getTemplate());
    }

    public BaseBrick get(int position) {
        return dataManager.getItems().get(position);
    }

    public int indexOf(BaseBrick brick) {
        return dataManager.getItems().indexOf(brick);
    }

    public BaseBrick getSectionHeader(int position) {
        if (position != -1) {
            for (int i = position; i >= 0; i--) {
                BaseBrick baseBrick = get(i);
                if (baseBrick != null && baseBrick.header) {
                    return baseBrick;
                }
            }
        }

        return null;
    }

    public BaseBrick getSectionFooter(int position) {
        if (position != -1) {
            for (int i = position; i < dataManager.adapterSize(); i++) {
                BaseBrick baseBrick = get(i);
                if (baseBrick != null && baseBrick.footer) {
                    return baseBrick;
                }
            }
        }

        return null;
    }

    public void setItems(ArrayList<BaseBrick> items) {
        dataManager.setItems(items);
    }

    public void addItem(BaseBrick item) {
        dataManager.addItem(item);
        safeNotifyItemInserted(dataManager.adapterSize());
    }

    public void addItem(int index, BaseBrick item) {
        dataManager.addItem(index, item);
        safeNotifyItemInserted(index);
    }

    public void removeItem(BaseBrick item) {
        dataManager.removeItem(item);
        safeNotifyItemRemoved(dataManager.adapterIndex(item));
    }

    public void removeItem(int index) {
        dataManager.removeItem(index);
        safeNotifyItemRemoved(index);
    }

    public void addItems(ArrayList<BaseBrick> items) {
        if (items != null && items.size() > 0) {
            dataManager.addItems(items);
            safeNotifyItemRangeInserted(items.indexOf(items.get(0)), items.size());
        }
    }

    public void addItems(int index, ArrayList<BaseBrick> items) {
        if (items != null && items.size() > 0) {
            dataManager.addItems(items);
            safeNotifyItemRangeInserted(index, items.size());
        }
    }

    public void removeItems(int start, int count) {
        dataManager.removeItems(start, count);
        safeNotifyItemRangeRemoved(start, count);
    }

    public void removeItems(ArrayList<BaseBrick> items) {
        if (items != null && items.size() > 0) {
            dataManager.removeItems(items);
            safeNotifyItemRangeRemoved(items.indexOf(items.get(0)), items.size());
        }
    }

    public void clear() {
        dataManager.clear();
        safeNotifyDataSetChanged();
    }

    public void replaceItem(int index, BaseBrick replacement) {
        dataManager.replaceItem(index, replacement);
        safeNotifyItemChanged(index);
    }

    public void replaceItem(BaseBrick target, BaseBrick replacement) {
        dataManager.replaceItem(target, replacement);
        safeNotifyItemChanged(dataManager.adapterIndex(replacement));
    }

    public void refreshItem(int index) {
        dataManager.dataHasChanged();
        safeNotifyItemChanged(index);
    }

    public void refreshItem(BaseBrick item) {
        dataManager.dataHasChanged();
        safeNotifyItemChanged(dataManager.adapterIndex(item));
    }

    public int dataSourceIndex(BaseBrick item) {
        return dataManager.dataSourceIndex(item);
    }

    public int adapterIndex(BaseBrick item) {
        return dataManager.adapterIndex(item);
    }

    public void removeAll(Class clazz) {
        dataManager.removeAll(clazz);
    }

    public int size() {
        return dataManager.adapterSize();
    }

    public void setOnReachedItemAtPosition(OnReachedItemAtPosition onReachedItemAtPosition) {
        this.onReachedItemAtPosition = onReachedItemAtPosition;
    }
}
