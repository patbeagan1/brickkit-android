package com.wayfair.brickkitdemo;

import android.support.v7.widget.OrientationHelper;

import com.wayfair.brickkit.BaseBrick;
import com.wayfair.brickkit.BrickFragment;
import com.wayfair.brickkit.InnerOuterBrickPadding;
import com.wayfair.brickkit.OnReachedItemAtPosition;
import com.wayfair.brickkit.OrientationBrickSize;
import com.wayfair.brickkit.bricks.TextBrick;

/**
 * Example fragment which loads more bricks when scrolling to the bottom.
 *
 * This fragment takes advantage of the {@link OnReachedItemAtPosition} which calls back when
 * items are bound in the adapter.
 */
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
        dataManager.brickRecyclerAdapter.setOnReachedItemAtPosition(
                new OnReachedItemAtPosition() {
                    @Override
                    public void bindingItemAtPosition(int position) {
                        if (position == dataManager.getRecyclerViewItems().size() - 1) {
                            page++;
                            addNewBricks();
                        }
                    }
                }
        );

        addNewBricks();
    }

    @Override
    public void addBehaviours() { }

    @Override
    public int orientation() {
        return OrientationHelper.VERTICAL;
    }

    @Override
    public boolean reverse() {
        return false;
    }

    /**
     * Method to add 100 new text bricks to the data manager.
     */
    private void addNewBricks() {
        for (int i = 0; i < 100; i++) {
            BaseBrick unusedBrick2 = new TextBrick(
                    getContext(),
                    new OrientationBrickSize(dataManager) {
                        @Override
                        protected int portrait() {
                            return MAX_SPANS;
                        }

                        @Override
                        protected int landscape() {
                            return HALF;
                        }
                    },
                    new InnerOuterBrickPadding() {
                        @Override
                        protected int innerPadding() {
                            return 5;
                        }

                        @Override
                        protected int outerPadding() {
                            return 10;
                        }
                    },
                    "Brick: " + page + " " + i
            );
            dataManager.addLast(unusedBrick2);
        }
    }
}
