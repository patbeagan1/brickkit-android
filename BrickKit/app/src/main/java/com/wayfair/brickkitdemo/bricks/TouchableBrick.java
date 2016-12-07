package com.wayfair.brickkitdemo.bricks;

import android.view.View;

public interface TouchableBrick {
    View.OnClickListener onTouch();

    boolean isEnabled();
}
