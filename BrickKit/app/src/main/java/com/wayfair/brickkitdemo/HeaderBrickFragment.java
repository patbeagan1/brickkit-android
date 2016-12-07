package com.wayfair.brickkitdemo;

import android.support.v7.widget.OrientationHelper;

import com.wayfair.brickkit.BrickFragment;
import com.wayfair.brickkit.InnerOuterBrickPadding;
import com.wayfair.brickkit.OrientationBrickSize;
import com.wayfair.brickkit.StickyHeaderHelper;
import com.wayfair.brickkit.bricks.TextBrick;

public class HeaderBrickFragment extends BrickFragment {
    private static final int MAX_SPANS = 240;
    private static final int HALF = 120;

    @Override
    public int maxSpans() {
        return MAX_SPANS;
    }

    @Override
    public void createBricks() {
        for (int i = 0; i < 100; i++) {
            TextBrick unusedBrick2 = new TextBrick(
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
                    "Brick: " + i
            );

            if (i % 10 == 0) {
                unusedBrick2.header = true;
            }

            dataManager.addLast(unusedBrick2);
        }
    }

    @Override
    public void addBehaviours() {
        dataManager.behaviours.add(new StickyHeaderHelper(dataManager));
    }

    @Override
    public int orientation() {
        return OrientationHelper.VERTICAL;
    }

    @Override
    public boolean reverse() {
        return false;
    }
}
