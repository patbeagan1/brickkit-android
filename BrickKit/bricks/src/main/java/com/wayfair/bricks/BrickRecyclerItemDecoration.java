package com.wayfair.bricks;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

        int adapterPosition = parent.getChildAdapterPosition(view);
        BaseBrick brick = brickDataManager.getRecyclerViewItems().get(adapterPosition);
        applyDynamicPadding(view.getContext(), outRect, brick, adapterPosition);
    }

    /**
     * Applies dynamic padding to a brick
     *
     * Dynamic padding takes into consideration the number of bricks in a group
     * and the span size to appropriately set the offsets when the section has
     * more than one brick. Using the traditional padding mechanism
     * duplicates the padding between bricks where only half is desired.
     *
     * @param context           A context
     * @param outRect           The Rect provided by {@link #getItemOffsets(Rect, View, RecyclerView, RecyclerView.State)}
     * @param brick             The brick
     */
    private void applyDynamicPadding(Context context, Rect outRect, BaseBrick brick, int adapterPosition) {
        int innerPaddingLeft = brick.padding.getInnerLeftPadding();
        int innerPaddingTop = brick.padding.getInnerTopPadding();
        int innerPaddingRight = brick.padding.getInnerRightPadding();
        int innerPaddingBottom = brick.padding.getInnerBottomPadding();

        int outerPaddingLeft = brick.padding.getOuterLeftPadding();
        int outerPaddingTop = brick.padding.getOuterTopPadding();
        int outerPaddingRight = brick.padding.getOuterRightPadding();
        int outerPaddingBottom = brick.padding.getOuterBottomPadding();
        Log.wtf("Kunal", brick.isOnLeftWall+" " + brick.isInFirstRow +" "+ brick.isOnRightWall+" "+brick.isInLastRow +" "+adapterPosition);

        // Apply padding
        if (brick.spanSize.getSpans(context) == brickDataManager.maxSpanCount) {
            // Single column
            if (brick.isInFirstRow) {
                if (brick.isInLastRow) {
                    outRect.set(outerPaddingLeft, outerPaddingTop, outerPaddingRight, outerPaddingBottom);
                } else {
                    outRect.set(outerPaddingLeft, outerPaddingTop, outerPaddingRight, innerPaddingBottom);
                }
            } else if (brick.isInLastRow) {
                outRect.set(outerPaddingLeft, innerPaddingTop, outerPaddingRight, outerPaddingBottom);
            } else {
                outRect.set(outerPaddingLeft, innerPaddingTop, outerPaddingRight, innerPaddingBottom);
            }

        } else {
            // Multi-column
            if (brick.isOnLeftWall) {
                if (brick.isInFirstRow) {
                    if (brick.isInLastRow) {
                        outRect.set(outerPaddingLeft, outerPaddingTop, innerPaddingRight, outerPaddingBottom);
                    } else {
                        outRect.set(outerPaddingLeft, outerPaddingTop, innerPaddingRight, innerPaddingBottom);
                    }
                } else if (brick.isInLastRow) {
                    outRect.set(outerPaddingLeft, innerPaddingTop, innerPaddingRight, outerPaddingBottom);
                } else {
                    outRect.set(outerPaddingLeft, innerPaddingTop, innerPaddingRight, innerPaddingBottom);
                }
            } else if (brick.isOnRightWall) {
                if (brick.isInFirstRow) {
                    if (brick.isInLastRow) {
                        outRect.set(innerPaddingLeft, outerPaddingTop, outerPaddingRight, outerPaddingBottom);
                    } else {
                        outRect.set(innerPaddingLeft, outerPaddingTop, outerPaddingRight, innerPaddingBottom);
                    }
                } else if (brick.isInLastRow) {
                    outRect.set(innerPaddingLeft, innerPaddingTop, outerPaddingRight, outerPaddingBottom);
                } else {
                    outRect.set(innerPaddingLeft, innerPaddingTop, outerPaddingRight, innerPaddingBottom);
                }
            } else {
                if (brick.isInFirstRow) {
                    if (brick.isInLastRow) {
                        outRect.set(innerPaddingLeft, outerPaddingTop, innerPaddingRight, outerPaddingBottom);
                    } else {
                        outRect.set(innerPaddingLeft, outerPaddingTop, innerPaddingRight, innerPaddingBottom);
                    }
                } else if (brick.isInLastRow) {
                    outRect.set(innerPaddingLeft, innerPaddingTop, innerPaddingRight, outerPaddingBottom);
                } else {
                    outRect.set(innerPaddingLeft, innerPaddingTop, innerPaddingRight, innerPaddingBottom);
                }
            }
        }
    }
}
