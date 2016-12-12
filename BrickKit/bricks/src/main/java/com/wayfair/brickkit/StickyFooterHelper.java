package com.wayfair.brickkit;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * {@link BrickBehaviour} that will provide a sticky footer view. Sticky footer views will remain
 * on screen in the footer position until another sticky footer view is scrolled into that area.
 */
public class StickyFooterHelper extends StickyViewHelper {
    /**
     * Constructor.
     *
     * @param brickDataManager {@link BrickDataManager} whose adapter is used for finding bricks
     */
    public StickyFooterHelper(BrickDataManager brickDataManager) {
        super(brickDataManager, R.id.sticky_footer_container, "@layout/sticky_footer_layout");
    }

    @Override
    protected int getStickyViewPosition(int adapterPosHere) {
        if (adapterPosHere == RecyclerView.NO_POSITION) {
            View lastChild = adapter.recyclerView.getChildAt(adapter.recyclerView.getChildCount() - 1);
            adapterPosHere = adapter.recyclerView.getChildAdapterPosition(lastChild);
        }
        BaseBrick footer = adapter.getSectionFooter(adapterPosHere);
        //Footer cannot be sticky if it's also an Expandable in collapsed status, RV will raise an exception
        if (footer == null) {
            return RecyclerView.NO_POSITION;
        }
        return adapter.indexOf(footer);
    }

    @Override
    protected void translateStickyView() {
        if (stickyViewHolder == null) {
            return;
        }

        int footerOffsetX = 0, footerOffsetY = 0;

        //Search for the position where the next footer item is found and take the new offset
        for (int i = adapter.recyclerView.getChildCount() - 2; i < adapter.getItemCount(); i++) {
            final View nextChild = adapter.recyclerView.getChildAt(i);
            if (nextChild != null) {
                int adapterPos = adapter.recyclerView.getChildAdapterPosition(nextChild);
                int nextFooterPosition = getStickyViewPosition(adapterPos);
                if (stickyPosition != nextFooterPosition) {
                    if (getOrientation(adapter.recyclerView) == LinearLayoutManager.HORIZONTAL) {
                        if (nextChild.getRight() < adapter.recyclerView.getRight()) {
                            int footerWidth = stickyViewHolder.itemView.getMeasuredWidth();
                            footerOffsetX = Math.max(footerWidth - (adapter.recyclerView.getRight() - nextChild.getRight()), 0);
                            if (footerOffsetX > 0) {
                                break;
                            }
                        }
                    } else {
                        if (nextChild.getBottom() < adapter.recyclerView.getBottom()) {
                            int footerHeight = stickyViewHolder.itemView.getMeasuredHeight();
                            footerOffsetY = Math.max(footerHeight - (adapter.recyclerView.getBottom() - nextChild.getBottom()), 0);
                            if (footerOffsetY > 0) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        //Apply translation
        stickyViewHolder.itemView.setTranslationX(footerOffsetX);
        stickyViewHolder.itemView.setTranslationY(footerOffsetY);
    }
}
