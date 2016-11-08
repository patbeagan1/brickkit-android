package com.wayfair.bricks;

import android.content.Context;
import android.graphics.Rect;

import com.wayfair.bricks.BaseBrick;
import com.wayfair.bricks.BrickDataManager;
import com.wayfair.bricks.R;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public abstract class BrickPadding {

    public Rect getPadding() {
        Rect padding;
        padding = innerPadding();

        return padding;
    }

    protected abstract Rect innerPadding();
    protected abstract Rect outerPadding();

}
