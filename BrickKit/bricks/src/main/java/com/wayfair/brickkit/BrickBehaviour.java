package com.wayfair.brickkit;

import android.support.v7.widget.RecyclerView;

public abstract class BrickBehaviour extends RecyclerView.OnScrollListener {
    public abstract void onDataSetChanged();
    public abstract void onScroll();
    public abstract void attachToRecyclerView();
    public abstract void detachFromRecyclerView();
}
