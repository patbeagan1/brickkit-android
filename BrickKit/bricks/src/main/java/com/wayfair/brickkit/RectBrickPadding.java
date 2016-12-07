package com.wayfair.brickkit;


import android.graphics.Rect;

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

    protected abstract Rect padding();
}
