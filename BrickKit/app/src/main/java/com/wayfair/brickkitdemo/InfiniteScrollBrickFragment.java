package com.wayfair.brickkitdemo;

import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.BrickFragment;
import com.wayfair.brickkit.padding.InnerOuterBrickPadding;
import com.wayfair.brickkit.OnReachedItemAtPosition;
import com.wayfair.brickkit.size.OrientationBrickSize;
import com.wayfair.brickkit.brick.TextBrick;

/**
 * Example fragment which loads more bricks when scrolling to the bottom.
 *
 * This fragment takes advantage of the {@link OnReachedItemAtPosition} which calls back when
 * items are bound in the adapter.
 */
public class InfiniteScrollBrickFragment extends BrickFragment {
    private static final int HALF = 120;

    private int page = 0;

    @Override
    public void createBricks() {
        dataManager.getBrickRecyclerAdapter().setOnReachedItemAtPosition(
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

    /**
     * Method to add 100 new text bricks to the data manager.
     */
    private void addNewBricks() {
        for (int i = 0; i < 100; i++) {
            BaseBrick unusedBrick2 = new TextBrick(
                    getContext(),
                    new OrientationBrickSize(maxSpans()) {
                        @Override
                        protected int portrait() {
                            return dataManager.getMaxSpanCount();
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
