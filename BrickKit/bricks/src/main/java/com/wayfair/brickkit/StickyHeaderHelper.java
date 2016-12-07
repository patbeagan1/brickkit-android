package com.wayfair.brickkit;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

public class StickyHeaderHelper extends BrickBehaviour {
    private BrickRecyclerAdapter adapter;
    private int headerPosition = RecyclerView.NO_POSITION;
    private BrickViewHolder stickyHeaderViewHolder;
    private ViewGroup stickyHolderLayout;

    public StickyHeaderHelper(BrickDataManager brickDataManager) {
        this.adapter = brickDataManager.brickRecyclerAdapter;
        attachToRecyclerView();
    }

    private static void resetHeader(RecyclerView.ViewHolder header) {
        final View view = header.itemView;
        removeViewFromParent(view);
        //Reset transformation on removed header
        view.setTranslationX(0);
        view.setTranslationY(0);
        header.setIsRecyclable(true);
    }

    private static void removeViewFromParent(final View view) {
        final ViewParent parent = view.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(view);
        }
    }

    private static int getOrientation(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).getOrientation();
        }
        return LinearLayoutManager.HORIZONTAL;
    }

    private static ViewGroup.LayoutParams getDefaultLayoutParams() {
        return new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onDataSetChanged() {
        updateOrClearHeader(true);
    }

    @Override
    public void onScroll() {
        //Initialize Holder Layout and show sticky header if exists already
        stickyHolderLayout = getStickySectionHeadersHolder();
        if (stickyHolderLayout != null) {
            if (stickyHolderLayout.getLayoutParams() == null) {
                stickyHolderLayout.setLayoutParams(getDefaultLayoutParams());
                //TODO: Animate Layout change when attach and detach
            }
            updateOrClearHeader(false);
        } else {
            Log.w(this.getClass().getSimpleName(), "WARNING! ViewGroup for Sticky Headers unspecified! You must include @layout/sticky_header_layout"
                    + " or implement FlexibleAdapter.getStickySectionFootersHolder() method");
        }
    }

    @Override
    public void attachToRecyclerView() {
        adapter.recyclerView.addOnScrollListener(this);
        adapter.recyclerView.post(new Runnable() {
            @Override
            public void run() {
                onScroll();
            }
        });
    }

    @Override
    public void detachFromRecyclerView() {
        if (adapter.recyclerView != null) {
            adapter.recyclerView.removeOnScrollListener(this);
            clearHeader();
        }
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        updateOrClearHeader(false);
    }

    private void updateOrClearHeader(boolean updateHeaderContent) {
        if (stickyHolderLayout == null || adapter.recyclerView == null || adapter.recyclerView.getChildCount() == 0) {
            clearHeader();
            return;
        }
        int firstHeaderPosition = getHeaderPosition(RecyclerView.NO_POSITION);
        if (firstHeaderPosition >= 0 && firstHeaderPosition < adapter.getItemCount()) {
            updateHeader(firstHeaderPosition, updateHeaderContent);
        } else {
            clearHeader();
        }
    }

    private int getHeaderPosition(int adapterPosHere) {
        if (adapterPosHere == RecyclerView.NO_POSITION) {
            View firstChild = adapter.recyclerView.getChildAt(0);
            adapterPosHere = adapter.recyclerView.getChildAdapterPosition(firstChild);
        }
        BaseBrick header = adapter.getSectionHeader(adapterPosHere);
        //Header cannot be sticky if it's also an Expandable in collapsed status, RV will raise an exception
        if (header == null) {
            return RecyclerView.NO_POSITION;
        }
        return adapter.indexOf(header);
    }

    private void clearHeader() {
        if (stickyHeaderViewHolder != null) {
            //if (FlexibleAdapter.DEBUG) Log.v(TAG, "clearFooter");
            resetHeader(stickyHeaderViewHolder);
            stickyHeaderViewHolder = null;
            headerPosition = RecyclerView.NO_POSITION;
        }
    }

    private void updateHeader(int headerPosition, boolean updateHeaderContent) {
        // Check if there is a new header should be sticky
        if (this.headerPosition != headerPosition) {
            this.headerPosition = headerPosition;
            BrickViewHolder holder = getHeaderViewHolder(headerPosition);
            if (stickyHeaderViewHolder != holder) {
                //if (FlexibleAdapter.DEBUG) Log.v(TAG, "swapHeader newPosition=" + mHeaderPosition);
                swapHeader(holder);
            }
        } else if (updateHeaderContent && stickyHeaderViewHolder != null) {
            adapter.onBindViewHolder(stickyHeaderViewHolder, this.headerPosition);
            ensureHeaderParent();
        }
        translateHeader();
    }

    private BrickViewHolder getHeaderViewHolder(int position) {
        //Find existing ViewHolder
        BrickViewHolder holder = (BrickViewHolder) adapter.recyclerView.findViewHolderForAdapterPosition(position);
        if (holder == null) {
            //Create and binds a new ViewHolder
            holder = adapter.createViewHolder(adapter.recyclerView, adapter.getItemViewType(position));
            adapter.bindViewHolder(holder, position);

            //Calculate width and height
            int widthSpec;
            int heightSpec;
            if (getOrientation(adapter.recyclerView) == LinearLayoutManager.VERTICAL) {
                widthSpec = View.MeasureSpec.makeMeasureSpec(adapter.recyclerView.getWidth(), View.MeasureSpec.EXACTLY);
                heightSpec = View.MeasureSpec.makeMeasureSpec(adapter.recyclerView.getHeight(), View.MeasureSpec.UNSPECIFIED);
            } else {
                widthSpec = View.MeasureSpec.makeMeasureSpec(adapter.recyclerView.getWidth(), View.MeasureSpec.UNSPECIFIED);
                heightSpec = View.MeasureSpec.makeMeasureSpec(adapter.recyclerView.getHeight(), View.MeasureSpec.EXACTLY);
            }

            //Measure and Layout the itemView
            final View headerView = holder.itemView;
            int childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                    adapter.recyclerView.getPaddingLeft() + adapter.recyclerView.getPaddingRight(),
                    headerView.getLayoutParams().width);
            int childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                    adapter.recyclerView.getPaddingTop() + adapter.recyclerView.getPaddingBottom(),
                    headerView.getLayoutParams().height);

            headerView.measure(childWidth, childHeight);
            headerView.layout(0, 0, headerView.getMeasuredWidth(), headerView.getMeasuredHeight());
        }
        return holder;
    }

    private void swapHeader(BrickViewHolder newHeader) {
        if (stickyHeaderViewHolder != null) {
            resetHeader(stickyHeaderViewHolder);
        }
        stickyHeaderViewHolder = newHeader;
        if (stickyHeaderViewHolder != null) {
            stickyHeaderViewHolder.setIsRecyclable(false);
            ensureHeaderParent();
        }
    }

    private void ensureHeaderParent() {
        final View view = stickyHeaderViewHolder.itemView;
        ViewGroup.LayoutParams params = stickyHolderLayout.getLayoutParams();
        params.width = view.getMeasuredWidth();
        params.height = view.getMeasuredHeight();
        removeViewFromParent(view);
        stickyHolderLayout.addView(view);
    }

    private void translateHeader() {
        if (stickyHeaderViewHolder == null) {
            return;
        }

        int headerOffsetX = 0, headerOffsetY = 0;

        //Search for the position where the next header item is found and take the new offset
        for (int i = 1; i > 0; i--) {
            final View nextChild = adapter.recyclerView.getChildAt(i);
            if (nextChild != null) {
                int adapterPos = adapter.recyclerView.getChildAdapterPosition(nextChild);
                int nextHeaderPosition = getHeaderPosition(adapterPos);
                if (headerPosition != nextHeaderPosition) {
                    if (getOrientation(adapter.recyclerView) == LinearLayoutManager.HORIZONTAL) {
                        if (nextChild.getLeft() > 0) {
                            int headerWidth = stickyHeaderViewHolder.itemView.getMeasuredWidth();
                            headerOffsetX = Math.min(nextChild.getLeft() - headerWidth, 0);
                            if (headerOffsetX < 0) {
                                break;
                            }
                        }
                    } else {
                        if (nextChild.getTop() > 0) {
                            int headerHeight = stickyHeaderViewHolder.itemView.getMeasuredHeight();
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
        stickyHeaderViewHolder.itemView.setTranslationX(headerOffsetX);
        stickyHeaderViewHolder.itemView.setTranslationY(headerOffsetY);
    }

    private ViewGroup getStickySectionHeadersHolder() {
        return adapter.recyclerView != null ? (ViewGroup) ((Activity) adapter.recyclerView.getContext()).findViewById(R.id.sticky_header_container) : null;
    }
}
