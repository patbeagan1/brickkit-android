package com.wayfair.brickkit;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

class BrickGridLayoutManager extends GridLayoutManager {
    BrickGridLayoutManager(Context context, int spanCount, BrickDataManager brickDataManager, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);

        setSpanSizeLookup(brickDataManager, context);
    }

    private void setSpanSizeLookup(final BrickDataManager brickDataManager, final Context context) {
        setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                BaseBrick brick = brickDataManager.getRecyclerViewItems().get(position);

                return brick != null ? brick.getSpanSize().getSpans(context) : 1;
            }
        });
    }
}
