package com.wayfair.brickkit;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public class BrickRecyclerAdapter extends RecyclerView.Adapter<BrickViewHolder> {
    private final BrickDataManager dataManager;
    private OnReachedItemAtPosition onReachedItemAtPosition;
    public RecyclerView recyclerView;
    private Handler handler;

    public BrickRecyclerAdapter(BrickDataManager brickDataManager, RecyclerView recyclerView) {
        if (recyclerView == null) {
            throw new IllegalArgumentException("Recycler View cannot be null");
        }
        this.recyclerView = recyclerView;
        this.dataManager = brickDataManager;
        handler = new Handler();
    }

    public void safeNotifyDataSetChanged() {
        if (recyclerView.isComputingLayout()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        } else {
            notifyDataSetChanged();
        }
    }

    public void safeNotifyItemChanged(final int position, final Object payload) {
        if (recyclerView.isComputingLayout()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    notifyItemChanged(position, payload);
                }
            });
        } else {
            notifyItemChanged(position, payload);
        }

    }

    public void safeNotifyItemChanged(final int position) {
        if (recyclerView.isComputingLayout()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    notifyItemChanged(position);
                }
            });
        } else {
            notifyItemChanged(position);
        }
    }

    public void safeNotifyItemInserted(final int position) {
        if (recyclerView.isComputingLayout()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    notifyItemInserted(position);
                }
            });
        } else {
            notifyItemInserted(position);
        }
    }

    public void safeNotifyItemMoved(final int fromPosition, final int toPosition) {
        if (recyclerView.isComputingLayout()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    notifyItemMoved(fromPosition, toPosition);
                }
            });
        } else {
            notifyItemMoved(fromPosition, toPosition);
        }
    }

    public void safeNotifyItemRangeChanged(final int positionStart, final int itemCount, final Object payload) {
        if (recyclerView.isComputingLayout()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    notifyItemRangeChanged(positionStart, itemCount, payload);
                }
            });
        } else {
            notifyItemRangeChanged(positionStart, itemCount, payload);
        }
    }

    public void safeNotifyItemRangeChanged(final int positionStart, final int itemCount) {
        if (recyclerView.isComputingLayout()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    notifyItemRangeChanged(positionStart, itemCount);
                }
            });
        } else {
            notifyItemRangeChanged(positionStart, itemCount);
        }
    }

    public void safeNotifyItemRangeInserted(final int positionStart, final int itemCount) {
        if (recyclerView.isComputingLayout()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    notifyItemRangeInserted(positionStart, itemCount);
                }
            });
        } else {
            notifyItemRangeInserted(positionStart, itemCount);
        }
    }

    public void safeNotifyItemRangeRemoved(final int positionStart, final int itemCount) {
        if (recyclerView.isComputingLayout()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    notifyItemRangeRemoved(positionStart, itemCount);
                }
            });
        } else {
            notifyItemRangeRemoved(positionStart, itemCount);
        }
    }

    public void safeNotifyItemRemoved(final int position) {
        if (recyclerView.isComputingLayout()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    notifyItemRemoved(position);
                }
            });
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
        BaseBrick baseBrick = get(holder.getLayoutPosition());

        if (baseBrick != null) {
            baseBrick.onBindData(holder);
            if (onReachedItemAtPosition != null) {
                onReachedItemAtPosition.bindingItemAtPosition(position);
            }
        }
    }

    @Override
    public void onViewDetachedFromWindow(BrickViewHolder holder) {
        holder.releaseViewsOnDetach();
    }

    @Override
    public int getItemCount() {
        return dataManager.getRecyclerViewItems().size();
    }

    @Override
    public int getItemViewType(int position) {
        if (get(position) == null) {
            return -1;
        }

        return TemplateRegistry.getInstance().get(get(position).getTemplate());
    }

    public BaseBrick get(int position) {
        return dataManager.getRecyclerViewItems().get(position);
    }

    public int indexOf(BaseBrick brick) {
        return dataManager.getRecyclerViewItems().indexOf(brick);
    }

    public BaseBrick getSectionHeader(int position) {
        if (position >= 0) {
            for (int i = position; i >= 0; i--) {
                BaseBrick baseBrick = get(i);
                if (baseBrick != null && baseBrick.isHeader()) {
                    return baseBrick;
                }
            }
        }

        return null;
    }

    public BaseBrick getSectionFooter(int position) {
        if (position >= 0) {
            for (int i = position; i < dataManager.getRecyclerViewItems().size(); i++) {
                BaseBrick baseBrick = get(i);
                if (baseBrick != null && baseBrick.isFooter()) {
                    return baseBrick;
                }
            }
        }

        return null;
    }

    public void setOnReachedItemAtPosition(OnReachedItemAtPosition onReachedItemAtPosition) {
        this.onReachedItemAtPosition = onReachedItemAtPosition;
    }
}
