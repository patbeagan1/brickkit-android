package com.wayfair.bricks;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class BrickRecyclerItemDecoration extends RecyclerView.ItemDecoration {
    private BrickDataManager brickDataManager;

    public BrickRecyclerItemDecoration(BrickDataManager recyclerViewDataManager) {
        this.brickDataManager = recyclerViewDataManager;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == -1 || brickDataManager.getRecyclerViewItems().get(parent.getChildAdapterPosition(view)) == null) {
            return;
        }

        brickDataManager.getRecyclerViewItems().get(parent.getChildAdapterPosition(view)).padding(outRect);
    }
}
