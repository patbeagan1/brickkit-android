package com.wayfair.bricks;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;

public abstract class BaseBrick {
    public Context context;
    public boolean hidden = false;
    public boolean header = false;
    public boolean footer = false;
    public Rect padding;
    public boolean excludeFromDynamicPadding;

    BrickSize spanSize;


    public BaseBrick(Context context, BrickSize spanSize) {
        this.context = context;
        this.spanSize = spanSize;
        this.spanSize.setBaseBrick(this);
    }

    public abstract void onBindData(RecyclerView.ViewHolder holder);

    public abstract void padding(Rect outRect);

    public abstract String getTemplate();
}
