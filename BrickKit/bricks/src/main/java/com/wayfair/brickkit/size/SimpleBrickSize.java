package com.wayfair.brickkit.size;

import com.wayfair.brickkit.BrickDataManager;

/**
 * {@link BrickSize} which returns the result of size() for all cases.
 */
public abstract class SimpleBrickSize extends BrickSize {
    /**
     * Constructor.
     *
     * @param brickDataManager {@link BrickDataManager} whose max span count we should use
     */
    protected SimpleBrickSize(BrickDataManager brickDataManager) {
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
