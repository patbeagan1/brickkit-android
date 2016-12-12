package com.wayfair.brickkit.padding;


import android.graphics.Rect;

/**
 * {@link BrickPadding} which uses the inner and outer {@link Rect}'s to get padding values.
 */
public abstract class InnerOuterRectBrickPadding extends BrickPadding {

    @Override
    protected int innerLeftPadding() {
        return innerPadding().left;
    }

    @Override
    protected int innerTopPadding() {
        return innerPadding().top;
    }

    @Override
    protected int innerRightPadding() {
        return innerPadding().right;
    }

    @Override
    protected int innerBottomPadding() {
        return innerPadding().bottom;
    }

    @Override
    protected int outerLeftPadding() {
        return outerPadding().left;
    }

    @Override
    protected int outerTopPadding() {
        return outerPadding().top;
    }

    @Override
    protected int outerRightPadding() {
        return outerPadding().right;
    }

    @Override
    protected int outerBottomPadding() {
        return outerPadding().bottom;
    }

    /**
     * Method to return the {@link Rect} to use to get the padding to use for inner padding on this brick.
     *
     * @return {@link Rect} to use to get the padding to use for inner padding on this brick.
     */
    protected abstract Rect innerPadding();

    /**
     * Method to return the {@link Rect} to use to get the padding to use for outer padding on this brick.
     *
     * @return {@link Rect} to use to get the padding to use for outer padding on this brick.
     */
    protected abstract Rect outerPadding();
}
