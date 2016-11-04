package com.wayfair.bricks;

import android.content.Context;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public abstract class BrickSize {
    private int maxSpan;
    private BaseBrick baseBrick;

    public BrickSize(BrickDataManager dataManager) {
        this.maxSpan = dataManager.maxSpanCount;
    }

    public void setBaseBrick(BaseBrick baseBrick) {
        this.baseBrick = baseBrick;
    }

    public int getSpans(Context context) {
        int spans;

        if (baseBrick.header || baseBrick.footer) {
            spans = maxSpan;
        } else {
            if (context.getResources().getBoolean(R.bool.tablet)) {
                if (context.getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
                    spans = landscapeTablet();
                } else {
                    spans = portraitTablet();
                }
            } else {
                if (context.getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
                    spans = landscapePhone();
                } else {
                    spans = portraitPhone();
                }
            }
        }

        if (spans > maxSpan) {
            android.util.Log.i(
                    getClass().getSimpleName(),
                    "Span needs to be less than or equal to: " + maxSpan
            );
            spans = maxSpan;
        }

        return spans;
    }

    protected abstract int landscapeTablet();
    protected abstract int portraitTablet();
    protected abstract int landscapePhone();
    protected abstract int portraitPhone();
}
