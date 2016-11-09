package com.wayfair.bricks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

public abstract class BaseBrick {
    public Context context;
    public boolean hidden = false;
    public boolean header = false;
    public boolean footer = false;
    public boolean isInFirstRow;
    public boolean isInLastRow;
    public boolean isOnLeftWall;
    public boolean isOnRightWall;
    public BrickPadding padding;
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
            protected int innerLeftPadding() {
                return getInnerBottomPadding();
            }

            @Override
            protected int innerTopPadding() {
                return 0;
            }

            @Override
            protected int innerRightPadding() {
                return 0;
            }

            @Override
            protected int innerBottomPadding() {
                return 0;
            }

            @Override
            protected int outerLeftPadding() {
                return 0;
            }

            @Override
            protected int outerTopPadding() {
                return 0;
            }

            @Override
            protected int outerRightPadding() {
                return 0;
            }

            @Override
            protected int outerBottomPadding() {
                return 0;
            }
        };
    }

    public abstract void onBindData(RecyclerView.ViewHolder holder);

    public abstract String getTemplate();

}
