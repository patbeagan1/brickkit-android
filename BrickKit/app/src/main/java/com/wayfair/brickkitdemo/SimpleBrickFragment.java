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

    @Override
    public void createBricks() {
        for (int i = 0; i < 100; i++) {
            TextBrick textBrick = new TextBrick(
                    getContext(),
                    new OrientationBrickSize(dataManager) {
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
