package com.wayfair.brickkit;

/**
 * Simple {@link BrickSize} which returns size based off of device type (phone vs tablet.
 */
public abstract class DeviceBrickSize extends BrickSize {
    /**
     * Constructor.
     *
     * @param brickDataManager {@link BrickDataManager} whose max span count we should use
     */
    public DeviceBrickSize(BrickDataManager brickDataManager) {
        super(brickDataManager);
    }

    @Override
    protected int landscapeTablet() {
        return tablet();
    }

    @Override
    protected int portraitTablet() {
        return tablet();
    }

    @Override
    protected int landscapePhone() {
        return phone();
    }

    @Override
    protected int portraitPhone() {
        return phone();
    }

    /**
     * Method to return the size to use for this brick on phones.
     *
     * @return size to use for this brick on phones.
     */
    protected abstract int phone();

    /**
     * Method to return the size to use for this brick on tablets.
     *
     * @return size to use for this brick on tablets.
     */
    protected abstract int tablet();
}
