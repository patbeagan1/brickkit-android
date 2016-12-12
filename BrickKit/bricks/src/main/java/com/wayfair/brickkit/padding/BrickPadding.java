package com.wayfair.brickkit.padding;

/**
 * This class defines an abstract implementation of brick padding.
 *
 * It is used to determine the amount of the padding to apply to this brick based off of the location in the screen.
 * Different padding is allowed on sides that are on the outside edge of the screen or any inner edges.
 */
public abstract class BrickPadding {
    /**
     * Gets the padding to be used on the left side for inner edges.
     *
     * @return the padding to be used on the left side for inner edges.
     */
    public int getInnerLeftPadding() {
        return innerLeftPadding();
    }

    /**
     * Gets the padding to be used on the top side for inner edges.
     *
     * @return the padding to be used on the top side for inner edges.
     */
    public int getInnerTopPadding() {
        return innerTopPadding();
    }

    /**
     * Gets the padding to be used on the right side for inner edges.
     *
     * @return the padding to be used on the right side for inner edges.
     */
    public int getInnerRightPadding() {
        return innerRightPadding();
    }

    /**
     * Gets the padding to be used on the bottom side for inner edges.
     *
     * @return the padding to be used on the bottom side for inner edges.
     */
    public int getInnerBottomPadding() {
        return innerBottomPadding();
    }

    /**
     * Gets the padding to be used on the left side for outer edges.
     *
     * @return the padding to be used on the left side for outer edges.
     */
    public int getOuterLeftPadding() {
        return outerLeftPadding();
    }

    /**
     * Gets the padding to be used on the top side for outer edges.
     *
     * @return the padding to be used on the top side for outer edges.
     */
    public int getOuterTopPadding() {
        return outerTopPadding();
    }

    /**
     * Gets the padding to be used on the right side for outer edges.
     *
     * @return the padding to be used on the right side for outer edges.
     */
    public int getOuterRightPadding() {
        return outerRightPadding();
    }

    /**
     * Gets the padding to be used on the bottom side for outer edges.
     *
     * @return the padding to be used on the bottom side for outer edges.
     */
    public int getOuterBottomPadding() {
        return outerBottomPadding();
    }

    /**
     * Method called to get the inner left padding.
     *
     * @return the inner left padding
     */
    protected abstract int innerLeftPadding();

    /**
     * Method called to get the inner top padding.
     *
     * @return the inner top padding
     */
    protected abstract int innerTopPadding();

    /**
     * Method called to get the inner right padding.
     *
     * @return the inner right padding
     */
    protected abstract int innerRightPadding();

    /**
     * Method called to get the inner bottom padding.
     *
     * @return the inner bottom padding
     */
    protected abstract int innerBottomPadding();

    /**
     * Method called to get the outer left padding.
     *
     * @return the outer left padding
     */
    protected abstract int outerLeftPadding();

    /**
     * Method called to get the outer top padding.
     *
     * @return the outer top padding
     */
    protected abstract int outerTopPadding();

    /**
     * Method called to get the outer right padding.
     *
     * @return the outer right padding
     */
    protected abstract int outerRightPadding();

    /**
     * Method called to get the outer bottom padding.
     *
     * @return the outer bottom padding
     */
    protected abstract int outerBottomPadding();

}
