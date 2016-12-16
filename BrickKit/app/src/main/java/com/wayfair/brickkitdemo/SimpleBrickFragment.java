package com.wayfair.brickkitdemo;

import com.wayfair.brickkit.BrickFragment;
import com.wayfair.brickkit.padding.InnerOuterBrickPadding;
import com.wayfair.brickkit.size.OrientationBrickSize;
import com.wayfair.brickkit.brick.TextBrick;

/**
 * Example fragment which shows text bricks.
 *
 * In portrait, the bricks are full width.
 * In landscape the bricks are half width.
 */
public class SimpleBrickFragment extends BrickFragment {
    private static final int HALF = 120;

    private int numberOfBricks = 0;

    /**
     * Create a new instance of a SimpleBrickFragment.
     *
     * @param numberOfBricks The number of bricks you want in the sub-{@link com.wayfair.brickkit.BrickDataManager}
     * @return The SimpleBrickFragment your created
     */
    public static SimpleBrickFragment newInstance(int numberOfBricks) {
        SimpleBrickFragment fragment = new SimpleBrickFragment();
        fragment.numberOfBricks = numberOfBricks;
        return fragment;
    }

    @Override
    public void createBricks() {
        for (int i = 0; i < numberOfBricks; i++) {
            TextBrick textBrick = new TextBrick(
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
                    "Brick: " + i
            );
            dataManager.addLast(textBrick);
        }
    }
}
