package com.wayfair.bricks;


public abstract class InnerOuterBrickPadding extends BrickPadding {

    @Override
    protected int innerLeftPadding() {
        return innerPadding();
    }

    @Override
    protected int innerTopPadding() {
        return innerPadding();
    }

    @Override
    protected int innerRightPadding() {
        return innerPadding();
    }

    @Override
    protected int innerBottomPadding() {
        return innerPadding();
    }

    @Override
    protected int outerLeftPadding() {
        return outerPadding();
    }

    @Override
    protected int outerTopPadding() {
        return outerPadding();
    }

    @Override
    protected int outerRightPadding() {
        return outerPadding();
    }

    @Override
    protected int outerBottomPadding() {
        return outerPadding();
    }

    protected abstract int innerPadding();

    protected abstract int outerPadding();
}
