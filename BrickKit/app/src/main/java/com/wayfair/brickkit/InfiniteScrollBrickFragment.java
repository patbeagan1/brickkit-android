package com.wayfair.brickkit;

import android.support.v7.widget.OrientationHelper;

import com.wayfair.bricks.BaseBrick;
import com.wayfair.bricks.BrickFragment;
import com.wayfair.bricks.OnReachedItemAtPosition;
import com.wayfair.bricks.OrientationBrickSize;
import com.wayfair.bricks.samples.TextBrick;
import com.wayfair.bricks.samples.VerticalTextBrick;

public class InfiniteScrollBrickFragment extends BrickFragment {
    private static final int MAX_SPANS = 240;
    private static final int HALF = 120;

    private int page = 0;

    @Override
    public int maxSpans() {
        return MAX_SPANS;
    }

    @Override
    public void createBricks() {
        brickRecyclerAdapter.setOnReachedItemAtPosition(
                new OnReachedItemAtPosition() {
                    @Override
                    public void bindingItemAtPosition(int position) {
                        if (position == brickRecyclerAdapter.getItemCount() - 1) {
                            page++;
                            addNewBricks();
                        }
                    }
                }
        );

        addNewBricks();
    }

    @Override
    public int orientation() {
        return OrientationHelper.HORIZONTAL;
    }

    @Override
    public boolean reverse() {
        return false;
    }

    public void addNewBricks() {
        for (int i = 0; i < 100; i++) {
            BaseBrick unusedBrick2 = new VerticalTextBrick(
                    getContext(),
                    new OrientationBrickSize(brickRecyclerAdapter) {
                        @Override
                        protected int portrait() {
                            return MAX_SPANS;
                        }

                        @Override
                        protected int landscape() {
                            return HALF;
                        }
                    },
                    "Brick: " + page + " " + i
            );
            brickRecyclerAdapter.addItem(unusedBrick2);
        }
    }
}
