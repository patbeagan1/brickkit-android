package com.wayfair.brickkit;

import android.support.annotation.IntDef;

/**
 * /**
 * The set of flags that might be passed to
 * {@link com.wayfair.brickkit.brick.BaseBrick#setStickyScrollMode(int)}.
 */
@IntDef({StickyScrollMode.SHOW_ON_SCROLL_DOWN, StickyScrollMode.SHOW_ON_SCROLL, StickyScrollMode.SHOW_ON_SCROLL_UP})
public @interface StickyScrollMode {
    int SHOW_ON_SCROLL_UP = 1;
    int SHOW_ON_SCROLL_DOWN = -1;
    int SHOW_ON_SCROLL = 0;
}