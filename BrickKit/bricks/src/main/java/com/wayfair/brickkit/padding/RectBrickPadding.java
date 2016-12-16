package com.wayfair.brickkit.padding;

import android.graphics.Rect;

/**
 * {@link BrickPadding} which uses the {@link Rect} to get padding values.
 */
public abstract class RectBrickPadding extends BrickPadding {

    @Override
    protected int innerLeftPadding() {
        return padding().left;
    }

    @Override
    protected int innerTopPadding() {
        return padding().top;
    }

    @Override
    protected int innerRightPadding() {
        return padding().right;
    }

    @Override
    protected int innerBottomPadding() {
        return padding().bottom;
    }

    @Override
    protected int outerLeftPadding() {
        return padding().left;
    }

    @Override
    protected int outerTopPadding() {
        return padding().top;
    }

    @Override
    protected int outerRightPadding() {
        return padding().right;
    }

    @Override
    protected int outerBottomPadding() {
        return padding().bottom;
    }

    /**
     * Method to return the {@link Rect} to use to get the padding to use for this brick.
     *
     * @return {@link Rect} to use to get the padding to use for this brick.
     */
    protected abstract Rect padding();
}
