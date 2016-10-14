package com.wayfair.bricks.samples;

import android.view.View;

public interface TouchableBrick {
    View.OnClickListener onTouch();

    boolean isEnabled();
}
