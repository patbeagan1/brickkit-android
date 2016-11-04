package com.wayfair.bricks;

public abstract class DeviceBrickSize extends BrickSize {
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

    protected abstract int phone();
    protected abstract int tablet();
}
