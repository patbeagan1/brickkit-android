package com.wayfair.bricks;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

class BrickGridLayoutManager extends GridLayoutManager {
    private Context context;

    public BrickGridLayoutManager(Context context, int spanCount, BrickDataManager brickDataManager,
                                  int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);

        this.context = context;

        setSpanSizeLookup(brickDataManager);
    }

    private void setSpanSizeLookup(final BrickDataManager brickDataManager) {
        setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (brickDataManager.getItems().get(position) == null) {
                    return 1;
                }

                return brickDataManager.getItems().get(position).spanSize.getSpans(context);
            }
        });
    }
}
