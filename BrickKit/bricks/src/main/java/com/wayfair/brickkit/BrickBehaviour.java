package com.wayfair.brickkit;

import android.support.v7.widget.RecyclerView;

/**
 * Abstract class to extend if you want to implement behaviors which work on
 * bricks, like {@link StickyHeaderHelper} and {@link StickyFooterHelper}.
 */
public abstract class BrickBehaviour extends RecyclerView.OnScrollListener {
    /**
     * Method that is called when the dataset for {@link BrickRecyclerAdapter} has changed.
     */
    public abstract void onDataSetChanged();

    /**
     * Method that is called when the {@link android.support.v7.widget.RecyclerView} has scrolled.
     */
    public abstract void onScroll();

    /**
     * Method that is called when this behavior is attached to the {@link android.support.v7.widget.RecyclerView}.
     */
    public abstract void attachToRecyclerView();

    /**
     * Method that is called when this behavior is detached to the {@link android.support.v7.widget.RecyclerView}.
     */
    public abstract void detachFromRecyclerView();
}
