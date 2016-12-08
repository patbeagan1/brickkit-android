package com.wayfair.brickkit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

public abstract class BaseBrick {
    protected final Context context;
    private final BrickPadding padding;
    private final BrickSize spanSize;

    private boolean hidden = false;
    private boolean header = false;
    private boolean footer = false;
    private boolean isInFirstRow;
    private boolean isInLastRow;
    private boolean isOnLeftWall;
    private boolean isOnRightWall;

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
        this.padding = new SimpleBrickPadding() {
            @Override
            protected int padding() {
                return 0;
            }
        };
    }

    public abstract void onBindData(RecyclerView.ViewHolder holder);

    public abstract String getTemplate();

    public BrickSize getSpanSize() {
        return spanSize;
    }

    public BrickPadding getPadding() {
        return padding;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public boolean isFooter() {
        return footer;
    }

    public void setFooter(boolean footer) {
        this.footer = footer;
    }

    public boolean isInFirstRow() {
        return isInFirstRow;
    }

    public void setInFirstRow(boolean inFirstRow) {
        isInFirstRow = inFirstRow;
    }

    public boolean isInLastRow() {
        return isInLastRow;
    }

    public void setInLastRow(boolean inLastRow) {
        isInLastRow = inLastRow;
    }

    public boolean isOnLeftWall() {
        return isOnLeftWall;
    }

    public void setOnLeftWall(boolean onLeftWall) {
        isOnLeftWall = onLeftWall;
    }

    public boolean isOnRightWall() {
        return isOnRightWall;
    }

    public void setOnRightWall(boolean onRightWall) {
        isOnRightWall = onRightWall;
    }
}
