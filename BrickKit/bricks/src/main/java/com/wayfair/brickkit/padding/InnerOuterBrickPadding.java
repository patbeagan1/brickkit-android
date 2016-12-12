package com.wayfair.brickkit.padding;

/**
 * {@link BrickPadding} which uses the values from innerPadding() and outerPadding().
 */
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

    /**
     * Method to the padding to use for inner padding on this brick.
     *
     * @return padding to use for inner padding on this brick.
     */
    protected abstract int innerPadding();

    /**
     * Method to the padding to use for outer padding on this brick.
     *
     * @return padding to use for outer padding on this brick.
     */
    protected abstract int outerPadding();
}
