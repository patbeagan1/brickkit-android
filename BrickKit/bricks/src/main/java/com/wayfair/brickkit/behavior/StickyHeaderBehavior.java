package com.wayfair.brickkit.behavior;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wayfair.brickkit.BrickDataManager;
import com.wayfair.brickkit.brick.BaseBrick;

/**
 * {@link BrickBehavior} that will provide a sticky header view. Sticky header views will remain
 * on screen in the header position until another sticky header view is scrolled into that area.
 */
public class StickyHeaderBehavior extends StickyViewBehavior {
    /**
     * Constructor.
     *
     * @param brickDataManager   {@link BrickDataManager} whose adapter is used for finding bricks
     * @param stickyHolderLayout sticky layout needed for the behavior
     */
    public StickyHeaderBehavior(BrickDataManager brickDataManager, ViewGroup stickyHolderLayout) {
        super(brickDataManager, "@layout/sticky_header_layout", stickyHolderLayout);
    }

    @Override
    protected int getStickyViewPosition(int adapterPosHere) {
        if (adapterPosHere == RecyclerView.NO_POSITION) {
            View firstChild = adapter.getRecyclerView().getChildAt(0);
            adapterPosHere = adapter.getRecyclerView().getChildAdapterPosition(firstChild);
        }
        BaseBrick header = adapter.getSectionHeader(adapterPosHere);
        //Header cannot be sticky if it's also an Expandable in collapsed status, RV will raise an exception
        if (header == null) {
            return RecyclerView.NO_POSITION;
        }
        return adapter.indexOf(header);
    }

    @Override
    protected void translateStickyView() {
        if (stickyViewHolder == null) {
            return;
        }

        int headerOffsetX = 0, headerOffsetY = 0;

        //Search for the position where the next header item is found and take the new offset
        for (int i = 1; i > 0; i--) {
            final View nextChild = adapter.getRecyclerView().getChildAt(i);
            if (nextChild != null) {
                int adapterPos = adapter.getRecyclerView().getChildAdapterPosition(nextChild);
                int nextHeaderPosition = getStickyViewPosition(adapterPos);
                if (stickyPosition != nextHeaderPosition) {
                    if (getOrientation(adapter.getRecyclerView()) == LinearLayoutManager.HORIZONTAL) {
                        if (nextChild.getLeft() > 0) {
                            int headerWidth = stickyViewHolder.itemView.getMeasuredWidth();
                            headerOffsetX = Math.min(nextChild.getLeft() - headerWidth, 0);
                            if (headerOffsetX < 0) {
                                break;
                            }
                        }
                    } else {
                        if (nextChild.getTop() > 0) {
                            int headerHeight = stickyViewHolder.itemView.getMeasuredHeight();
                            headerOffsetY = Math.min(nextChild.getTop() - headerHeight, 0);
                            if (headerOffsetY < 0) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        //Apply translation
        stickyViewHolder.itemView.setTranslationX(headerOffsetX);
        stickyViewHolder.itemView.setTranslationY(headerOffsetY);
    }
}
