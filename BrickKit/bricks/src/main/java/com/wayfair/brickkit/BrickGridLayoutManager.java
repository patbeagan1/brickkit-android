package com.wayfair.brickkit;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

/**
 * {@link GridLayoutManager} used with Bricks. It grabs the span size from the brick at the correct position.
 */
class BrickGridLayoutManager extends GridLayoutManager {

    /**
     * Constructor.
     *
     * @param context the {@link Context} to use
     * @param spanCount the span count for the layout manager
     * @param brickDataManager the {@link BrickDataManager} to get bricks from
     * @param orientation the orientation to use when looking
     * @param reverseLayout whether or not to reverse the layout
     */
    BrickGridLayoutManager(final Context context, int spanCount, final BrickDataManager brickDataManager, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);

        setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                BaseBrick brick = brickDataManager.getRecyclerViewItems().get(position);

                return brick != null ? brick.getSpanSize().getSpans(context) : 1;
            }
        });
    }
}
