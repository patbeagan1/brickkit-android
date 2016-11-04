package com.wayfair.bricks;

public abstract class SimpleBrickSize extends BrickSize {
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

    protected abstract int size();
}
