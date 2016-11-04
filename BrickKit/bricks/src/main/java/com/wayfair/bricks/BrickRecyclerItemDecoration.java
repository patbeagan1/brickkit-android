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
    private  int spanCount;
    private  boolean isInFirstRow = true;
    private  boolean isInLastRow = false;
    private  boolean isOnLeftWall = true;
    private  boolean isOnRightWall = false;


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

        if (useDynamicPadding && !brick.excludeFromDynamicPadding && brick.padding == null) {
            applyDynamicPadding(view.getContext(), outRect, adapterPosition, brick);
        } else if (useDynamicPadding && !brick.excludeFromDynamicPadding){
            outRect.set(brick.padding);
        } else {
            brick.padding(outRect);
        }
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

    /**
     * Applies dynamic padding to a brick
     *
     * Dynamic padding takes into consideration the number of bricks in a group (continuous set
     * of a type of brick) and the span size to appropriately set the offsets when the section has
     * more than one brick. Using the traditional padding mechanism {@link BaseBrick#padding(Rect)}
     * duplicates the padding between bricks where only half is desired.
     *
     * @param context           A context
     * @param outRect           The Rect provided by {@link #getItemOffsets(Rect, View, RecyclerView, RecyclerView.State)}
     * @param adapterPosition   Position in the adapter of the brick
     * @param brick             The brick
     */
    private void applyDynamicPadding(Context context, Rect outRect, int adapterPosition, BaseBrick brick) {
        // Find range of group
        List<BaseBrick> bricks = brickDataManager.getRecyclerViewItems();
        int groupStartPosition = -1;
        int groupEndPosition = -1;
        for (int i = 0; i < bricks.size(); i++) {
            if (bricks.get(i).getClass() == brick.getClass()) {
                if (groupStartPosition == -1) {
                    groupStartPosition = i;
                } else {
                    groupEndPosition = i;
                }
            }
        }

        int groupBrickPosition = adapterPosition - groupStartPosition;
        int groupSize = groupEndPosition - groupStartPosition + 1;
        // TODO FIX THIS
        int outsidePadding = 20;
        // Round up to nearest even number
        int insidePadding = outsidePadding % 2 == 0 ? outsidePadding / 2 : (outsidePadding + 1) / 2;
        preCheck(context, bricks, groupBrickPosition, brick.spanSize.getSpans(context));
        // Apply padding
        if (brick.spanSize.getSpans(context) == brickDataManager.maxSpanCount) {
            // Single column
            if (isInFirstRow) {
                if (isInLastRow) {
                    outRect.set(outsidePadding, outsidePadding, outsidePadding, outsidePadding);
                } else {
                    outRect.set(outsidePadding, outsidePadding, outsidePadding, insidePadding);
                }
            } else if (isInLastRow) {
                outRect.set(outsidePadding, insidePadding, outsidePadding, outsidePadding);
            } else {
                outRect.set(outsidePadding, insidePadding, outsidePadding, insidePadding);
            }

        } else {
            // Multi-column
            if (isOnLeftWall) {
                if (isInFirstRow) {
                    if (isInLastRow) {
                        outRect.set(outsidePadding, outsidePadding, insidePadding, outsidePadding);
                    } else {
                        outRect.set(outsidePadding, outsidePadding, insidePadding, insidePadding);
                    }
                } else if (isInLastRow) {
                    outRect.set(outsidePadding, insidePadding, insidePadding, outsidePadding);
                } else {
                    outRect.set(outsidePadding, insidePadding, insidePadding, insidePadding);
                }
            } else if (isOnRightWall) {
                if (isInFirstRow) {
                    if (isInLastRow) {
                        outRect.set(insidePadding, outsidePadding, outsidePadding, outsidePadding);
                    } else {
                        outRect.set(insidePadding, outsidePadding, outsidePadding, insidePadding);
                    }
                } else if (isInLastRow) {
                    outRect.set(insidePadding, insidePadding, outsidePadding, outsidePadding);
                } else {
                    outRect.set(insidePadding, insidePadding, outsidePadding, insidePadding);
                }
            } else {
                if (isInFirstRow) {
                    if (isInLastRow) {
                        outRect.set(insidePadding, outsidePadding, insidePadding, outsidePadding);
                    } else {
                        outRect.set(insidePadding, outsidePadding, insidePadding, insidePadding);
                    }
                } else if (isInLastRow) {
                    outRect.set(insidePadding, insidePadding, insidePadding, outsidePadding);
                } else {
                    outRect.set(insidePadding, insidePadding, insidePadding, insidePadding);
                }
            }


        }
        postCheck(brick.spanSize.getSpans(context));
        brick.padding = new Rect();
        brick.padding.set(outRect);
    }

    private void preCheck(Context context, List<BaseBrick> bricks, int adapterPosition, int spanSize) {
        spanCount = spanCount + spanSize;
        if (spanCount < brickDataManager.maxSpanCount) {
            isOnRightWall = false;
        } else if (spanCount == brickDataManager.maxSpanCount) {
            isOnRightWall = true;
        } else if (spanCount + bricks.get(adapterPosition).spanSize.getSpans(context) > brickDataManager.maxSpanCount){
            isOnRightWall = true;
        }

        int spanCountHenceForth = spanCount;
        for (int i = adapterPosition+1; i < bricks.size(); i++) {
            spanCountHenceForth = spanCountHenceForth + bricks.get(i).spanSize.getSpans(context);
        }
        if(isOnLeftWall && spanCountHenceForth <= brickDataManager.maxSpanCount) {
            isInLastRow = true;
        }
    }

    private void postCheck(int spanSize) {
        if (isOnLeftWall) {
            isOnLeftWall = false;
        }

        if(isInLastRow && isOnRightWall) {
            isInLastRow = false;
        }

        if(isOnRightWall) {
            spanCount = 0;
            isInFirstRow = false;
            isOnLeftWall = true;
        }
    }
}
