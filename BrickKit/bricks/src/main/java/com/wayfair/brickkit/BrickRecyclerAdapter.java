package com.wayfair.brickkit;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wayfair.brickkit.brick.BaseBrick;

import java.util.ListIterator;

/**
 * Extension of {@link android.support.v7.widget.RecyclerView.Adapter} which combines a given
 * {@link BrickDataManager} to a given {@link RecyclerView}.
 */
public class BrickRecyclerAdapter extends RecyclerView.Adapter<BrickViewHolder> {
    private final BrickDataManager dataManager;
    private OnReachedItemAtPosition onReachedItemAtPosition;
    private RecyclerView recyclerView;
    private Handler handler;

    /**
     * Constructor.
     *
     * @param brickDataManager {@link BrickDataManager} for this adapter
     * @param recyclerView {@link RecyclerView} for this adapter
     */
    public BrickRecyclerAdapter(BrickDataManager brickDataManager, RecyclerView recyclerView) {
        if (recyclerView == null) {
            throw new IllegalArgumentException("Recycler View cannot be null");
        }
        this.recyclerView = recyclerView;
        this.dataManager = brickDataManager;
        handler = new Handler();
    }

    /**
     * Safe version of {@link RecyclerView.Adapter#notifyDataSetChanged()}.
     */
    public void safeNotifyDataSetChanged() {
        if (recyclerView.isComputingLayout()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    BrickRecyclerAdapter.super.notifyDataSetChanged();
                }
            });
        } else {
            super.notifyDataSetChanged();
        }
    }

    /**
     * Safe version of {@link RecyclerView.Adapter#notifyItemChanged(int, Object)}.
     *
     * @param position Position of the item that has changed
     * @param payload Optional parameter, use null to identify a "full" update
     */
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

    /**
     *  Safe version of {@link RecyclerView.Adapter#notifyItemChanged(int)}.
     *
     * @param position Position of the item that has changed
     */
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

    /**
     * Safe version of {@link RecyclerView.Adapter#notifyItemInserted(int)}.
     *
     * @param position Position of the newly inserted item in the data set

     */
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

    /**
     * Safe version of {@link RecyclerView.Adapter#notifyItemMoved(int, int)}.
     *
     * @param fromPosition Previous position of the item.
     * @param toPosition New position of the item.
     */
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

    /**
     * Safe version of {@link RecyclerView.Adapter#notifyItemRangeChanged(int, int, Object)}.
     *
     * @param positionStart Position of the first item that has changed
     * @param itemCount Number of items that have changed
     * @param payload  Optional parameter, use null to identify a "full" update
     */
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

    /**
     * Safe version of {@link RecyclerView.Adapter#notifyItemRangeChanged(int, int)}.
     *
     * @param positionStart Position of the first item that has changed
     * @param itemCount Number of items that have changed
     */
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

    /**
     * Safe version of {@link RecyclerView.Adapter#notifyItemRangeInserted(int, int)}.
     *
     * @param positionStart Position of the first item that was inserted
     * @param itemCount Number of items inserted
     */
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

    /**
     * Safe version of {@link RecyclerView.Adapter#notifyItemRangeRemoved(int, int)}.
     *
     * @param positionStart Previous position of the first item that was removed
     * @param itemCount Number of items removed from the data set
     */
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

    /**
     * Safe version of {@link RecyclerView.Adapter#notifyItemRemoved(int)}.
     *
     * @param position Position of the item that has now been removed
     */
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return dataManager.brickWithLayout(viewType).createViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BrickViewHolder holder, int position) {
        BaseBrick baseBrick = dataManager.brickAtPosition(position);

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
        BaseBrick brick = dataManager.brickAtPosition(position);

        if (brick == null) {
            return 0;
        }

        return brick.getLayout();
    }

    /**
     * Get brick at the given position.
     *
     * @param position position of the brick to get
     * @return the brick at the given position
     */
    public BaseBrick get(int position) {
        return dataManager.getRecyclerViewItems().get(position);
    }

    /**
     * Get the index of the given brick.
     * @param brick brick to get the index of
     * @return index of the given brick
     */
    public int indexOf(BaseBrick brick) {
        return dataManager.getRecyclerViewItems().indexOf(brick);
    }

    /**
     * Get the first header before the given position.
     * @param position position before which to find the next header
     * @return the first header before the given position.
     */
    public BaseBrick getSectionHeader(int position) {
        if (position >= 0) {
            BaseBrick brick = dataManager.getRecyclerViewItems().get(position);
            if (brick != null && brick.isHeader()) {
                return brick;
            }

            ListIterator<BaseBrick> iterator = dataManager.getRecyclerViewItems().listIterator(position);

            while (iterator.hasPrevious()) {
                brick = iterator.previous();
                if (brick != null && brick.isHeader()) {
                    return brick;
                }
            }
        }

        return null;
    }

    /**
     * Get the first footer after the given position.
     * @param position position after which to find the next footer
     * @return the first footer after the given position.
     */
    public BaseBrick getSectionFooter(int position) {
        if (position >= 0) {
            BaseBrick brick = dataManager.getRecyclerViewItems().get(position);
            if (brick != null && brick.isFooter()) {
                return brick;
            }

            ListIterator<BaseBrick> iterator = dataManager.getRecyclerViewItems().listIterator(position);

            while (iterator.hasNext()) {
                brick = iterator.next();
                if (brick != null && brick.isFooter()) {
                    return brick;
                }
            }
        }

        return null;
    }

    /**
     * Set an {@link OnReachedItemAtPosition}.
     *
     * @param onReachedItemAtPosition {@link OnReachedItemAtPosition} to set
     */
    public void setOnReachedItemAtPosition(OnReachedItemAtPosition onReachedItemAtPosition) {
        this.onReachedItemAtPosition = onReachedItemAtPosition;
    }

    /**
     * Get the {@link RecyclerView} for this adapter.
     * @return the {@link RecyclerView} for this adapter
     */
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}
