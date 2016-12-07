package com.wayfair.bricks;

public abstract class SimpleBrickPadding extends BrickPadding {

    @Override
    protected int innerLeftPadding() {
        return padding();
    }

    @Override
    protected int innerTopPadding() {
        return padding();
    }

    @Override
    protected int innerRightPadding() {
        return padding();
    }

    @Override
    protected int innerBottomPadding() {
        return padding();
    }

    @Override
    protected int outerLeftPadding() {
        return padding();
    }

    @Override
    protected int outerTopPadding() {
        return padding();
    }

    @Override
    protected int outerRightPadding() {
        return padding();
    }

    @Override
    protected int outerBottomPadding() {
        return padding();
    }

    protected abstract int padding();
}
