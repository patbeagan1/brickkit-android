package com.wayfair.bricks;

public abstract class OrientationBrickSize extends BrickSize {
    public OrientationBrickSize(BrickRecyclerAdapter brickRecyclerAdapter) {
        super(brickRecyclerAdapter);
    }

    @Override
    protected int landscapeTablet() {
        return landscape();
    }

    @Override
    protected int portraitTablet() {
        return portrait();
    }

    @Override
    protected int landscapePhone() {
        return landscape();
    }

    @Override
    protected int portraitPhone() {
        return portrait();
    }

    protected abstract int portrait();
    protected abstract int landscape();
}
