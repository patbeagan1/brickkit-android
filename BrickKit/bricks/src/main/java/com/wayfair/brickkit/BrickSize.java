package com.wayfair.brickkit;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

public abstract class BrickSize {
    private int maxSpan;
    private BaseBrick baseBrick;

    public BrickSize(BrickDataManager dataManager) {
        this.maxSpan = dataManager.getMaxSpanCount();
    }

    public void setBaseBrick(BaseBrick baseBrick) {
        this.baseBrick = baseBrick;
    }

    public int getSpans(Context context) {
        int spans;

        if (baseBrick.isHeader() || baseBrick.isFooter()) {
            spans = maxSpan;
        } else {
            if (context.getResources().getBoolean(R.bool.tablet)) {
                if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    spans = landscapeTablet();
                } else {
                    spans = portraitTablet();
                }
            } else {
                if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    spans = landscapePhone();
                } else {
                    spans = portraitPhone();
                }
            }

            if (spans > maxSpan) {
                Log.i(getClass().getSimpleName(), "Span needs to be less than or equal to: " + maxSpan);
                spans = maxSpan;
            }
        }

        return spans;
    }

    protected abstract int landscapeTablet();
    protected abstract int portraitTablet();
    protected abstract int landscapePhone();
    protected abstract int portraitPhone();
}
