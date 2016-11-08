package com.wayfair.bricks;

import android.graphics.Rect;

public abstract class SimpleBrickPadding extends BrickPadding {

    @Override
    protected Rect innerPadding() {
        return new Rect (padding(), padding(), padding(), padding());
    }

    @Override
    protected Rect outerPadding(){
        return new Rect (padding(), padding(), padding(), padding());
    }

    protected abstract int padding();
}
