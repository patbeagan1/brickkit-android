package com.wayfair.brickkit;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * Abstract parent for {@link StickyHeaderHelper} and {@link StickyFooterHelper}. This class contains
 * the logic for managing the sticky view and updating it when appropriate.
 */
abstract class StickyViewHelper extends BrickBehaviour {
     BrickRecyclerAdapter adapter;
    private ViewGroup stickyHolderLayout;
    int stickyPosition = RecyclerView.NO_POSITION;
    BrickViewHolder stickyViewHolder;
    private final int stickyViewContainerId;
    private final String stickyLayoutName;

    /**
     * Constructor.
     *
     * @param brickDataManager {@link BrickDataManager} whose adapter is used for finding bricks
     * @param stickyViewContainerId id of the container id which will be container for the sticky view
     * @param stickyLayoutName layout name for the sticky layout needed for the behavior
     */
    StickyViewHelper(BrickDataManager brickDataManager, int stickyViewContainerId, String stickyLayoutName) {
        this.adapter = brickDataManager.brickRecyclerAdapter;
        this.stickyViewContainerId = stickyViewContainerId;
        this.stickyLayoutName = stickyLayoutName;
        attachToRecyclerView();
    }

    /**
     * Get the sticky view based off of this position.
     *
     * @param adapterPosHere Adapter position to start search from for sticky view
     * @return sticky view for this position if one exists, null otherwise
     */
    protected abstract int getStickyViewPosition(int adapterPosHere);

    /**
     * Translate the stickyView based off of the translation of the RecyclerView.
     */
    protected abstract void translateStickyView();

    @Override
    public void onDataSetChanged() {
        updateOrClearStickyView(true);
    }

    @Override
    public void onScroll() {
        //Initialize Holder Layout and show sticky view if exists already
        stickyHolderLayout = getStickySectionHolder();
        if (stickyHolderLayout != null) {
            if (stickyHolderLayout.getLayoutParams() == null) {
                stickyHolderLayout.setLayoutParams(
                        new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                //TODO: Animate Layout change when attach and detach
            }
            updateOrClearStickyView(false);
        } else {
            Log.w(this.getClass().getSimpleName(), "WARNING! ViewGroup for Sticky View unspecified! You must include " + stickyLayoutName
                    + " or implement FlexibleAdapter.getStickySectionHolder() method");
        }
    }

    @Override
    public void attachToRecyclerView() {
        adapter.recyclerView.addOnScrollListener(this);
        adapter.recyclerView.post(new Runnable() {
            @Override
            public void run() {
                onScroll();
            }
        });
    }

    @Override
    public void detachFromRecyclerView() {
        if (adapter.recyclerView != null) {
            adapter.recyclerView.removeOnScrollListener(this);
            clearStickyView();
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        updateOrClearStickyView(false);
    }

    /**
     * Static helper method to get the orientation of a recycler view.
     * @param recyclerView {@link RecyclerView} whose orientation we are getting
     * @return the orientation of the given RecyclerView if it can be found, LinearLayoutManager.HORIZONTAL otherwise
     */
    static int getOrientation(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).getOrientation();
        }
        return LinearLayoutManager.HORIZONTAL;
    }

    /**
     * Removes the sticky view from the container and reset the positioning of the container.
     *
     * @param stickyView stickyView to reset
     */
    private static void resetStickyView(RecyclerView.ViewHolder stickyView) {
        final View view = stickyView.itemView;
        removeViewFromParent(view);
        //Reset transformation on removed stickyView
        view.setTranslationX(0);
        view.setTranslationY(0);
        stickyView.setIsRecyclable(true);
    }

    /**
     * Remove the given view from its parent.
     *
     * @param view view to remove from its parent
     */
    private static void removeViewFromParent(final View view) {
        final ViewParent parent = view.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(view);
        }
    }

    /**
     * Replace the current stickyView with the new sticky View.
     *
     * @param newStickyView the new sticky view to use
     */
    private void swapStickyView(BrickViewHolder newStickyView) {
        if (stickyViewHolder != null) {
            resetStickyView(stickyViewHolder);
        }
        stickyViewHolder = newStickyView;
        if (stickyViewHolder != null) {
            stickyViewHolder.setIsRecyclable(false);
            ensureStickyViewParent();
        }
    }

    /**
     * Makes sure that the current stickyViewHolder is the view in the stickyHolderLayout.
     */
    private void ensureStickyViewParent() {
        final View view = stickyViewHolder.itemView;
        ViewGroup.LayoutParams params = stickyHolderLayout.getLayoutParams();
        params.width = view.getMeasuredWidth();
        params.height = view.getMeasuredHeight();
        removeViewFromParent(view);
        stickyHolderLayout.addView(view);
    }

    /**
     * Remove the current sticky view and reset the stickyViewHolder and stickyPosition.
     */
    private void clearStickyView() {
        if (stickyViewHolder != null) {
            //if (FlexibleAdapter.DEBUG) Log.v(TAG, "clearStickyView");
            resetStickyView(stickyViewHolder);
            stickyViewHolder = null;
            stickyPosition = RecyclerView.NO_POSITION;
        }
    }

    /**
     * Method to make sure the stickyview is up to date or clears it if appropriate.
     *
     * @param updateStickyContent whether or not to force an update to the sticky container content
     */
    private void updateOrClearStickyView(boolean updateStickyContent) {
        if (stickyHolderLayout == null || adapter.recyclerView == null || adapter.recyclerView.getChildCount() == 0) {
            clearStickyView();
            return;
        }
        int stickyPosition = getStickyViewPosition(RecyclerView.NO_POSITION);
        if (stickyPosition >= 0 && stickyPosition < adapter.getItemCount()) {
            updateStickyView(stickyPosition, updateStickyContent);
        } else {
            clearStickyView();
        }
    }

    /**
     * Updates the stickyView. This will replace the sticky view if the position has changed or update the content
     * if the position is teh same but you have flagged to force the update.
     *
     * @param stickyPosition new sticky position to use for the stickyView
     * @param updateStickyContent whether or not to force a re-bind a of the sticky view holder
     */
    private void updateStickyView(int stickyPosition, boolean updateStickyContent) {
        // Check if there is a new sticky view should be sticky
        if (this.stickyPosition != stickyPosition) {
            this.stickyPosition = stickyPosition;
            BrickViewHolder holder = getStickyViewHolder(stickyPosition);
            if (stickyViewHolder != holder) {
                //if (FlexibleAdapter.DEBUG) Log.v(TAG, "updateStickyView newPosition = " + stickyPosition);
                swapStickyView(holder);
            }
        } else if (updateStickyContent && stickyViewHolder != null) {
            adapter.onBindViewHolder(stickyViewHolder, this.stickyPosition);
            ensureStickyViewParent();
        }
        translateStickyView();
    }

    /**
     * Get {@link BrickViewHolder} for a given position in the adapter's RecyclerView. The view will be bound, measured and laid out.
     *
     * @param position position to get a {@link BrickViewHolder} for
     * @return a {@link BrickViewHolder}
     */
    private BrickViewHolder getStickyViewHolder(int position) {
        //Find existing ViewHolder
        BrickViewHolder holder = (BrickViewHolder) adapter.recyclerView.findViewHolderForAdapterPosition(position);
        if (holder == null) {
            //Create and binds a new ViewHolder
            holder = adapter.createViewHolder(adapter.recyclerView, adapter.getItemViewType(position));
            adapter.bindViewHolder(holder, position);

            //Calculate width and height
            int widthSpec;
            int heightSpec;
            if (getOrientation(adapter.recyclerView) == LinearLayoutManager.VERTICAL) {
                widthSpec = View.MeasureSpec.makeMeasureSpec(adapter.recyclerView.getWidth(), View.MeasureSpec.EXACTLY);
                heightSpec = View.MeasureSpec.makeMeasureSpec(adapter.recyclerView.getHeight(), View.MeasureSpec.UNSPECIFIED);
            } else {
                widthSpec = View.MeasureSpec.makeMeasureSpec(adapter.recyclerView.getWidth(), View.MeasureSpec.UNSPECIFIED);
                heightSpec = View.MeasureSpec.makeMeasureSpec(adapter.recyclerView.getHeight(), View.MeasureSpec.EXACTLY);
            }

            //Measure and Layout the itemView
            final View stickyView = holder.itemView;
            int childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                    adapter.recyclerView.getPaddingLeft() + adapter.recyclerView.getPaddingRight(),
                    stickyView.getLayoutParams().width);
            int childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                    adapter.recyclerView.getPaddingTop() + adapter.recyclerView.getPaddingBottom(),
                    stickyView.getLayoutParams().height);

            stickyView.measure(childWidth, childHeight);
            stickyView.layout(0, 0, stickyView.getMeasuredWidth(), stickyView.getMeasuredHeight());
        }
        return holder;
    }

    /**
     * Get the {@link ViewGroup} to be used for the sticky container.
     *
     * @return the sticky container if the recyclerview for the adapter isn't null and a view with an id that matches stickyViewContainerId exists
     */
    private ViewGroup getStickySectionHolder() {
        return adapter.recyclerView != null ? (ViewGroup) ((Activity) adapter.recyclerView.getContext()).findViewById(stickyViewContainerId) : null;
    }
}
