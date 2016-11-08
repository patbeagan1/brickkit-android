package com.wayfair.bricks;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;

public class BrickRecyclerItemDecoration extends RecyclerView.ItemDecoration {
    private BrickDataManager brickDataManager;
    private boolean useDynamicPadding;


    public BrickRecyclerItemDecoration(BrickDataManager recyclerViewDataManager) {
        this.brickDataManager = recyclerViewDataManager;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == -1 || brickDataManager.getRecyclerViewItems().get(parent.getChildAdapterPosition(view)) == null) {
            return;
        }

        //brickDataManager.getItems().get(parent.getChildAdapterPosition(view)).padding(outRect);
        int adapterPosition = parent.getChildAdapterPosition(view);
        BaseBrick brick = brickDataManager.getRecyclerViewItems().get(adapterPosition);
        applyDynamicPadding(view.getContext(), outRect, adapterPosition, brick);

//        if (useDynamicPadding && !brick.excludeFromDynamicPadding && brick.padding == null) {
//            applyDynamicPadding(view.getContext(), outRect, adapterPosition, brick);
//        } else if (useDynamicPadding && !brick.excludeFromDynamicPadding){
//            outRect.set(brick.padding);
//        } else {
//            brick.padding(outRect);
//        }
//        Log.wtf("Kunal", adapterPosition + " " + outRect.left + " " +outRect.top + " " +outRect.right + " " +outRect.bottom );

        // handle no dynmaic padding
        // brick.padding(outRect);
    }

    /**
     * Enables the use of dynamic padding
     * See {@link #applyDynamicPadding(Context, Rect, int, BaseBrick)} for an explanation on what
     * dynamic padding is
     */
    public void useDynamicPadding() {
        useDynamicPadding = true;
    }


    private void applyDynamicPadding(Context context, Rect outRect, int adapterPosition, BaseBrick brick) {
        // Find range of group
        List<BaseBrick> bricks = brickDataManager.getRecyclerViewItems();
        Log.wtf("Kunal", brick.isOnLeftWall+" " + brick.isInFirstRow +" "+ brick.isOnRightWallWithExtraSpace+" "+" "+brick.isOnRightWallWithoutExtraSpace+brick.isInLastRow +" "+adapterPosition);


        // TODO FIX THIS
        int outsidePadding = brick.padding.getPadding().left;
        // Round up to nearest even number
        int insidePadding = outsidePadding % 2 == 0 ? outsidePadding / 2 : (outsidePadding + 1) / 2;
        // Apply padding
        if (brick.spanSize.getSpans(context) == brickDataManager.maxSpanCount) {
            // Single column
            if (brick.isInFirstRow) {
                if (brick.isInLastRow) {
                    outRect.set(outsidePadding, outsidePadding, outsidePadding, outsidePadding);
                } else {
                    outRect.set(outsidePadding, outsidePadding, outsidePadding, insidePadding);
                }
            } else if (brick.isInLastRow) {
                outRect.set(outsidePadding, insidePadding, outsidePadding, outsidePadding);
            } else {
                outRect.set(outsidePadding, insidePadding, outsidePadding, insidePadding);
            }

        } else {
            // Multi-column
            if (brick.isOnLeftWall) {
                if (brick.isInFirstRow) {
                    if (brick.isInLastRow) {
                        outRect.set(outsidePadding, outsidePadding, insidePadding, outsidePadding);
                    } else {
                        outRect.set(outsidePadding, outsidePadding, insidePadding, insidePadding);
                    }
                } else if (brick.isInLastRow) {
                    outRect.set(outsidePadding, insidePadding, insidePadding, outsidePadding);
                } else {
                    outRect.set(outsidePadding, insidePadding, insidePadding, insidePadding);
                }
            } else if (brick.isOnRightWallWithoutExtraSpace || brick.isOnRightWallWithExtraSpace) {
                if (brick.isInFirstRow) {
                    if (brick.isInLastRow) {
                        outRect.set(insidePadding, outsidePadding, outsidePadding, outsidePadding);
                    } else {
                        outRect.set(insidePadding, outsidePadding, outsidePadding, insidePadding);
                    }
                } else if (brick.isInLastRow) {
                    outRect.set(insidePadding, insidePadding, outsidePadding, outsidePadding);
                } else if (brick.isOnRightWallWithExtraSpace){
                    outRect.set(insidePadding, insidePadding, insidePadding, insidePadding);
                }else {
                    outRect.set(insidePadding, insidePadding, outsidePadding, insidePadding);
                }
            } else {
                if (brick.isInFirstRow) {
                    if (brick.isInLastRow) {
                        outRect.set(insidePadding, outsidePadding, insidePadding, outsidePadding);
                    } else {
                        outRect.set(insidePadding, outsidePadding, insidePadding, insidePadding);
                    }
                } else if (brick.isInLastRow) {
                    outRect.set(insidePadding, insidePadding, insidePadding, outsidePadding);
                } else {
                    outRect.set(insidePadding, insidePadding, insidePadding, insidePadding);
                }
            }
        }
    }
}
