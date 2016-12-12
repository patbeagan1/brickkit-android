package com.wayfair.brickkit.padding;

/**
 * {@link BrickPadding} which returns the result of padding() for all cases.
 */
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

    /**
     * Method to return the padding to use for this brick.
     *
     * @return padding to use for this brick.
     */
    protected abstract int padding();
}
