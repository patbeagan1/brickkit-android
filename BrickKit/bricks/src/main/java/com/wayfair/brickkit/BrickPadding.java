package com.wayfair.brickkit;

public abstract class BrickPadding {
    public int getInnerLeftPadding() {
        return innerLeftPadding();
    }

    public int getInnerTopPadding() {
        return innerTopPadding();
    }

    public int getInnerRightPadding() {
        return innerRightPadding();
    }

    public int getInnerBottomPadding() {
        return innerBottomPadding();
    }

    public int getOuterLeftPadding() {
        return outerLeftPadding();
    }

    public int getOuterTopPadding() {
        return outerTopPadding();
    }

    public int getOuterRightPadding() {
        return outerRightPadding();
    }

    public int getOuterBottomPadding() {
        return outerBottomPadding();
    }

    protected abstract int innerLeftPadding();

    protected abstract int innerTopPadding();

    protected abstract int innerRightPadding();

    protected abstract int innerBottomPadding();

    protected abstract int outerLeftPadding();

    protected abstract int outerTopPadding();

    protected abstract int outerRightPadding();

    protected abstract int outerBottomPadding();

}
