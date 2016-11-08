package com.wayfair.bricks;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;

public abstract class BaseBrick {
    public Context context;
    public boolean hidden = false;
    public boolean header = false;
    public boolean footer = false;
    public  boolean isInFirstRow;
    public  boolean isInLastRow;
    public  boolean isOnLeftWall;
    public  boolean isOnRightWallWithExtraSpace;
    public  boolean isOnRightWallWithoutExtraSpace;
    public BrickPadding padding;
    public boolean excludeFromDynamicPadding;

    BrickSize spanSize;


    public BaseBrick(Context context, BrickSize spanSize, BrickPadding padding) {
        this.context = context;
        this.spanSize = spanSize;
        this.spanSize.setBaseBrick(this);
        this.padding = padding;
    }

    public BaseBrick(Context context, BrickSize spanSize) {
        this.context = context;
        this.spanSize = spanSize;
        this.spanSize.setBaseBrick(this);
        this.padding = new BrickPadding() {
            @Override
            protected Rect innerPadding() {
                return new Rect(0, 0, 0, 0);
            }

            @Override
            protected Rect outerPadding() {
                return new Rect(0, 0, 0, 0);
            }
        };
    }

    public abstract void onBindData(RecyclerView.ViewHolder holder);

//    public void padding(Rect outRect);

    public abstract String getTemplate();

}
