package com.wayfair.brickkit.brick;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.wayfair.brickkit.BrickDataManager;
import com.wayfair.brickkit.BrickViewHolder;
import com.wayfair.brickkit.StickyScrollMode;
import com.wayfair.brickkit.padding.BrickPadding;
import com.wayfair.brickkit.padding.SimpleBrickPadding;
import com.wayfair.brickkit.size.BrickSize;

import java.util.Locale;

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
    @StickyScrollMode
    private int stickyScrollMode = StickyScrollMode.SHOW_ON_SCROLL;

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
     * @param holder BrickViewHolder which should be updated.
     */
    public abstract void onBindData(BrickViewHolder holder);

    /**
     * Get layout resource id for this brick.
     *
     * @return the layout resource id for this brick
     */
    @LayoutRes
    public abstract int getLayout();

    /**
     * Creates an instance of the {@link BrickViewHolder} for this class.
     *
     * @param itemView view to pass into the {@link BrickViewHolder}
     * @return the {@link BrickViewHolder}
     */
    public abstract BrickViewHolder createViewHolder(View itemView);

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
     * Set stickyScrollMode {@link com.wayfair.brickkit.StickyScrollMode}.
     *
     * @param stickyScrollMode whether the brick show header/footer on scrolling up/down
     */
    public void setStickyScrollMode(@StickyScrollMode int stickyScrollMode) {
        this.stickyScrollMode = stickyScrollMode;
    }

    /**
     * Get stickyScrollMode {@link com.wayfair.brickkit.StickyScrollMode}.
     *
     * @return stickyScrollMode whether the brick show header/footer on scrolling up/down
     */
    @StickyScrollMode
    public int getStickyScrollMode() {
        return stickyScrollMode;
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

    /**
     * Called when the brick was moved via drag'n'drop.
     *
     * @param position The position the brick was moved to.
     */
    public void movedTo(final int position) {
    }

    /**
     * Called when an item is swiped-to-dismiss.
     */
    public void dismissed() {
    }

    /**
     * Print out the brick's adapter position, width (in spans), and padding.
     *
     * @param dataManager The data manager the brick is in.
     * @return A string representation of the brick.
     */
    public String toString(BrickDataManager dataManager) {
        int position = dataManager.getRecyclerViewItems().indexOf(this);
        StringBuilder sb = new StringBuilder();

        sb.append("--");
        if (isInFirstRow) {
            sb.append(padding.getOuterTopPadding());
        } else {
            sb.append(padding.getInnerTopPadding());
        }
        sb.append("--\n");


        String positionFormat = "|%d|\n";
        if (position < 100) {
            positionFormat = "| %d|\n";
            if (position < 10) {
                positionFormat = "| %d |\n";
            }
        }
        sb.append(String.format(Locale.getDefault(), positionFormat, position));

        if (isOnLeftWall) {
            sb.append(padding.getOuterLeftPadding());
        } else {
            sb.append(padding.getInnerLeftPadding());
        }

        String widthFormat = "%d";
        if (spanSize.getSpans(context) < 100) {
            widthFormat = " %d";
            if (spanSize.getSpans(context) < 10) {
                widthFormat = " %d ";
            }
        }
        sb.append(String.format(Locale.getDefault(), widthFormat, spanSize.getSpans(context)));

        if (isOnRightWall) {
            sb.append(padding.getOuterRightPadding());
            sb.append("\n");
        } else {
            sb.append(padding.getInnerRightPadding());
            sb.append("\n");
        }

        sb.append("|   |\n");

        sb.append("--");
        if (isInLastRow) {
            sb.append(padding.getOuterBottomPadding());
        } else {
            sb.append(padding.getInnerBottomPadding());
        }
        sb.append("--");

        return sb.toString();
    }
}
