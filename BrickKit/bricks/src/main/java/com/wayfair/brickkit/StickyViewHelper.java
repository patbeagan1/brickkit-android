package com.wayfair.brickkit;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public abstract class StickyViewHelper extends BrickBehaviour {

    protected static void resetHeader(RecyclerView.ViewHolder header) {
        final View view = header.itemView;
        removeViewFromParent(view);
        //Reset transformation on removed header
        view.setTranslationX(0);
        view.setTranslationY(0);
        header.setIsRecyclable(true);
    }

    protected static void removeViewFromParent(final View view) {
        final ViewParent parent = view.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(view);
        }
    }
}
