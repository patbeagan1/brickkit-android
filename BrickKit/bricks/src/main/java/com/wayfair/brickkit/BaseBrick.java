package com.wayfair.brickkit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

/**
 * Abstract class which defines Bricks.
 */
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

    /**
     * Constructor.
     *
     * @param context context this brick exists in
     * @param spanSize size information for this brick
     * @param padding padding for this brick
     */
    public BaseBrick(Context context, BrickSize spanSize, BrickPadding padding) {
        this.context = context;
        this.spanSize = spanSize;
        this.spanSize.setBaseBrick(this);
        this.padding = padding;
    }

    /**
     * Constructor which uses the default padding.
     *
     * @param context context this brick exists in
     * @param spanSize size information for this brick
     */
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

    /**
     * Called by the BrickRecyclerAdapter to display the information in this brick to the specified ViewHolder.
     *
     * @param holder ViewHolder which should be updated.
     */
    public abstract void onBindData(RecyclerView.ViewHolder holder);

    /**
     * Gets the template string for this brick type.
     *
     * @return the template string for this brick type
     */
    public abstract String getTemplate();

    /**
     * Gets the {@link BrickSize} for this brick.
     *
     * @return the {@link BrickSize} for this brick
     */
    public BrickSize getSpanSize() {
        return spanSize;
    }

    /**
     * Gets the {@link BrickPadding} for this brick.
     *
     * @return the {@link BrickPadding} for this brick
     */
    public BrickPadding getPadding() {
        return padding;
    }

    /**
     * Whether or not this brick should be hidden.
     *
     * @return true if brick should be hidden, false otherwise
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Set whether the brick should be hidden.
     *
     * @param hidden whether the brick should be hidden
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * Whether or not this brick should act as a header.
     *
     * @return true if brick should act as a header, false otherwise
     */
    public boolean isHeader() {
        return header;
    }

    /**
     * Set whether the brick should act as a header.
     *
     * @param header whether the brick should act as a header
     */
    public void setHeader(boolean header) {
        this.header = header;
    }

    /**
     * Whether or not this brick should act as a footer.
     *
     * @return true if brick should act as a footer, false otherwise
     */
    public boolean isFooter() {
        return footer;
    }

    /**
     * Set whether the brick should act as a footer.
     *
     * @param footer whether the brick should act as a footer
     */
    public void setFooter(boolean footer) {
        this.footer = footer;
    }

    /**
     * Set whether the brick should act as it is in the first row.
     *
     * @return true if brick is in the first row, false otherwise
     */
    public boolean isInFirstRow() {
        return isInFirstRow;
    }

    /**
     * Set whether the brick should act as it is in the first row.
     *
     * @param inFirstRow whether the brick should act as it is in the first row
     */
    public void setInFirstRow(boolean inFirstRow) {
        isInFirstRow = inFirstRow;
    }

    /**
     * Set whether the brick should act as it is in the last row.
     *
     * @return true if brick is in the last row, false otherwise
     */
    public boolean isInLastRow() {
        return isInLastRow;
    }

    /**
     * Set whether the brick should act as it is in the last row.
     *
     * @param inLastRow whether the brick should act as it is in the last row
     */
    public void setInLastRow(boolean inLastRow) {
        isInLastRow = inLastRow;
    }

    /**
     * Set whether the brick should act as it is on the left wall.
     *
     * @return true if brick is is on the left wall, false otherwise
     */
    public boolean isOnLeftWall() {
        return isOnLeftWall;
    }

    /**
     * Set whether the brick should act as it is on the left wall.
     *
     * @param onLeftWall whether the brick should act as it is on the left wall
     */
    public void setOnLeftWall(boolean onLeftWall) {
        isOnLeftWall = onLeftWall;
    }

    /**
     * Set whether the brick should act as it is on the right wall.
     *
     * @return true if brick is is on the right wall, false otherwise
     */
    public boolean isOnRightWall() {
        return isOnRightWall;
    }

    /**
     * Set whether the brick should act as it is on the right wall.
     *
     * @param onRightWall whether the brick should act as it is on the right wall
     */
    public void setOnRightWall(boolean onRightWall) {
        isOnRightWall = onRightWall;
    }
}
