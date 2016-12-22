package com.wayfair.brickkit.behavior;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wayfair.brickkit.BrickRecyclerAdapter;
import com.wayfair.brickkit.StickyScrollMode;
import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.BrickDataManager;
import com.wayfair.brickkit.R;

/**
 * {@link BrickBehavior} that will provide a sticky header view. Sticky header views will remain
 * on screen in the header position until another sticky header view is scrolled into that area.
 */
public class StickyHeaderBehavior extends StickyViewBehavior {
    /**
     * Constructor.
     *
     * @param brickDataManager {@link BrickDataManager} whose adapter is used for finding bricks
     */
    public StickyHeaderBehavior(BrickDataManager brickDataManager) {
        super(brickDataManager, R.id.sticky_header_container, "@layout/sticky_header_layout");
    }

    /**
     * Constructor for Unit Tests.
     *
     * @param brickDataManager   {@link BrickDataManager} whose adapter is used for finding bricks
     * @param stickyHolderLayout sticky layout needed for the behavior
     */
    public StickyHeaderBehavior(BrickDataManager brickDataManager, ViewGroup stickyHolderLayout) {
        super(brickDataManager, "@layout/sticky_header_layout", stickyHolderLayout);
    }

    @Override
    protected int getStickyViewPosition(int adapterPosHere) {
        BrickRecyclerAdapter adapter = brickDataManager.getBrickRecyclerAdapter();

        if (adapterPosHere == RecyclerView.NO_POSITION) {
            View firstChild = adapter.getRecyclerView().getChildAt(0);
            adapterPosHere = adapter.getRecyclerView().getChildAdapterPosition(firstChild);
        }
        BaseBrick header = adapter.getSectionHeader(adapterPosHere);
        //Header cannot be sticky if it's also an Expandable in collapsed status, RV will raise an exception
        if (header == null) {
            stickyScrollMode = StickyScrollMode.SHOW_ON_SCROLL;
            return RecyclerView.NO_POSITION;
        } else {
            stickyScrollMode = header.getStickyScrollMode();
        }
        return adapter.indexOf(header);
    }

    @Override
    protected void translateStickyView() {
        BrickRecyclerAdapter adapter = brickDataManager.getBrickRecyclerAdapter();

        if (stickyViewHolder == null || adapter == null || adapter.getRecyclerView() == null) {
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

    @Override
    protected void stickyViewFadeTranslate(int dy) {
        if (stickyHolderLayout != null && stickyHolderLayout.getHeight() > 0 && stickyScrollMode == StickyScrollMode.SHOW_ON_SCROLL_UP) {
            float headerY = stickyHolderLayout.getY();
            if (dy > 0) {
                stickyHolderLayout.setTranslationY(Math.min(headerY + dy, 0));
            } else {
                stickyHolderLayout.setTranslationY(Math.max(headerY + dy, -stickyHolderLayout.getHeight()));
            }
        }

        if (stickyHolderLayout != null && stickyHolderLayout.getHeight() > 0 && stickyScrollMode == StickyScrollMode.SHOW_ON_SCROLL_DOWN) {
            float headerY = stickyHolderLayout.getY();
            if (dy > 0) {
                stickyHolderLayout.setTranslationY(Math.max(headerY - dy, -stickyHolderLayout.getHeight()));
            } else {
                stickyHolderLayout.setTranslationY(Math.min(headerY - dy, 0));
            }
        }
    }
}
