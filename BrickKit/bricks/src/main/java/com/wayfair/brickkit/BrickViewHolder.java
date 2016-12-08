package com.wayfair.brickkit;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BrickViewHolder extends RecyclerView.ViewHolder {
    public BrickViewHolder(View itemView) {
        super(itemView);
    }

    protected void releaseViewsOnDetach() {
    }
}
