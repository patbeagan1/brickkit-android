package com.wayfair.bricks;


import android.graphics.Rect;

public abstract class SimpleBrickPadding4 extends BrickPadding {

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

    protected abstract Rect innerPadding();

    protected abstract Rect outerPadding();
}
