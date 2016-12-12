package com.wayfair.brickkit;

/**
 * {@link BrickSize} which returns the result of size() for all cases.
 */
public abstract class SimpleBrickSize extends BrickSize {
    /**
     * Constructor.
     *
     * @param brickDataManager {@link BrickDataManager} whose max span count we should use
     */
    public SimpleBrickSize(BrickDataManager brickDataManager) {
        super(brickDataManager);
    }

    @Override
    protected int landscapeTablet() {
        return size();
    }

    @Override
    protected int portraitTablet() {
        return size();
    }

    @Override
    protected int landscapePhone() {
        return size();
    }

    @Override
    protected int portraitPhone() {
        return size();
    }

    /**
     * Method to return the size to use for this brick.
     *
     * @return size to use for this brick.
     */
    protected abstract int size();
}
