package com.wayfair.brickkit.brick;

import android.view.View;

/**
 * Interface defining a brick that can be clicked in two places.
 */
public interface MultiTouchableBrick {
    /**
     * {@link android.view.View.OnClickListener} to be called when primary hit target on brick is clicked.
     *
     * @return {@link android.view.View.OnClickListener}
     */
    View.OnClickListener onTouch();

    /**
     * {@link android.view.View.OnClickListener} to be called when second hit target on brick is clicked.
     *
     * @return {@link android.view.View.OnClickListener}
     */
    View.OnClickListener onSecondaryTouch();

    /**
     * Whether or not the brick is currently handling click events.
     *
     * @return true if the brick is currently handling click events, false otherwise
     */
    boolean isEnabled();
}