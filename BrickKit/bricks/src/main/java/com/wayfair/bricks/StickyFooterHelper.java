package com.wayfair.bricks;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

public class StickyFooterHelper extends BrickBehaviour {
    private BrickRecyclerAdapter adapter;
    private int footerPosition = RecyclerView.NO_POSITION;
    private BrickViewHolder stickyFooterViewHolder;
    private ViewGroup stickyHolderLayout;

    public StickyFooterHelper(BrickDataManager brickDataManager) {
        this.adapter = brickDataManager.brickRecyclerAdapter;
        attachToRecyclerView();
    }

    private static void resetFooter(RecyclerView.ViewHolder footer) {
        final View view = footer.itemView;
        removeViewFromParent(view);
        //Reset transformation on removed footer
        view.setTranslationX(0);
        view.setTranslationY(0);
        footer.setIsRecyclable(true);
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
    public void onScroll() {
        //Initialize Holder Layout and show sticky footer if exists already
        stickyHolderLayout = getStickySectionFootersHolder();
        if (stickyHolderLayout != null) {
            if (stickyHolderLayout.getLayoutParams() == null) {
                stickyHolderLayout.setLayoutParams(getDefaultLayoutParams());
                //TODO: Animate Layout change when attach and detach
            }
            updateOrClearFooter(false);
        } else {
            Log.w(this.getClass().getSimpleName(), "WARNING! ViewGroup for Sticky Footers unspecified! You must include @layout/sticky_footer_layout"
                    + " or implement FlexibleAdapter.getStickySectionFootersHolder() method");
        }
    }

    @Override
    public void onDataSetChanged() {
        updateOrClearFooter(true);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        updateOrClearFooter(false);
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
            clearFooter();
        }
    }

    private void updateOrClearFooter(boolean updateFooterContent) {
        if (stickyHolderLayout == null || adapter.recyclerView == null || adapter.recyclerView.getChildCount() == 0) {
            clearFooter();
            return;
        }
        int firstFooterPosition = getFooterPosition(RecyclerView.NO_POSITION);
        if (firstFooterPosition >= 0 && firstFooterPosition < adapter.getItemCount()) {
            updateFooter(firstFooterPosition, updateFooterContent);
        } else {
            clearFooter();
        }
    }

    private int getFooterPosition(int adapterPosHere) {
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

    private void clearFooter() {
        if (stickyFooterViewHolder != null) {
            //if (FlexibleAdapter.DEBUG) Log.v(TAG, "clearFooter");
            resetFooter(stickyFooterViewHolder);
            stickyFooterViewHolder = null;
            footerPosition = RecyclerView.NO_POSITION;
        }
    }

    private void updateFooter(int footerPosition, boolean updateFooterContent) {
        // Check if there is a new footer should be sticky
        if (this.footerPosition != footerPosition) {
            this.footerPosition = footerPosition;
            BrickViewHolder holder = getFooterViewHolder(footerPosition);
            if (stickyFooterViewHolder != holder) {
                //if (FlexibleAdapter.DEBUG) Log.v(TAG, "swapFooter newPosition=" + mFooterPosition);
                swapFooter(holder);
            }
        } else if (updateFooterContent && stickyFooterViewHolder != null) {
            adapter.onBindViewHolder(stickyFooterViewHolder, this.footerPosition);
            ensureFooterParent();
        }
        translateFooter();
    }

    private BrickViewHolder getFooterViewHolder(int position) {
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
            final View footerView = holder.itemView;
            int childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                    adapter.recyclerView.getPaddingLeft() + adapter.recyclerView.getPaddingRight(),
                    footerView.getLayoutParams().width);
            int childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                    adapter.recyclerView.getPaddingTop() + adapter.recyclerView.getPaddingBottom(),
                    footerView.getLayoutParams().height);

            footerView.measure(childWidth, childHeight);
            footerView.layout(0, 0, footerView.getMeasuredWidth(), footerView.getMeasuredHeight());
        }
        return holder;
    }

    private void swapFooter(BrickViewHolder newFooter) {
        if (stickyFooterViewHolder != null) {
            resetFooter(stickyFooterViewHolder);
        }
        stickyFooterViewHolder = newFooter;
        if (stickyFooterViewHolder != null) {
            stickyFooterViewHolder.setIsRecyclable(false);
            ensureFooterParent();
        }
    }

    private void ensureFooterParent() {
        final View view = stickyFooterViewHolder.itemView;
        ViewGroup.LayoutParams params = stickyHolderLayout.getLayoutParams();
        params.width = view.getMeasuredWidth();
        params.height = view.getMeasuredHeight();
        removeViewFromParent(view);
        stickyHolderLayout.addView(view);
    }

    private void translateFooter() {
        if (stickyFooterViewHolder == null) {
            return;
        }

        int footerOffsetX = 0, footerOffsetY = 0;

        //Search for the position where the next footer item is found and take the new offset
        for (int i = adapter.recyclerView.getChildCount() - 2; i < adapter.getItemCount(); i++) {
            final View nextChild = adapter.recyclerView.getChildAt(i);
            if (nextChild != null) {
                int adapterPos = adapter.recyclerView.getChildAdapterPosition(nextChild);
                int nextFooterPosition = getFooterPosition(adapterPos);
                if (footerPosition != nextFooterPosition) {
                    if (getOrientation(adapter.recyclerView) == LinearLayoutManager.HORIZONTAL) {
                        if (nextChild.getRight() < adapter.recyclerView.getRight()) {
                            int footerWidth = stickyFooterViewHolder.itemView.getMeasuredWidth();
                            footerOffsetX = Math.max(footerWidth - (adapter.recyclerView.getRight() - nextChild.getRight()), 0);
                            if (footerOffsetX > 0) {
                                break;
                            }
                        }
                    } else {
                        if (nextChild.getBottom() < adapter.recyclerView.getBottom()) {
                            int footerHeight = stickyFooterViewHolder.itemView.getMeasuredHeight();
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
        stickyFooterViewHolder.itemView.setTranslationX(footerOffsetX);
        stickyFooterViewHolder.itemView.setTranslationY(footerOffsetY);
    }

    private ViewGroup getStickySectionFootersHolder() {
        return adapter.recyclerView != null ? (ViewGroup) ((Activity) adapter.recyclerView.getContext()).findViewById(R.id.sticky_footer_container) : null;
    }
}
