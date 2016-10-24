package com.wayfair.bricks;

import android.support.v7.widget.RecyclerView;

public abstract class BrickBehaviour extends RecyclerView.OnScrollListener {
    public abstract void onScroll();
    public abstract void onDataSetChanged();

    public void attachToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(this);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                onScroll();
            }
        });
    }
}
