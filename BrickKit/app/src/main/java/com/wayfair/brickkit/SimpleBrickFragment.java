package com.wayfair.brickkit;

import android.support.v7.widget.OrientationHelper;

import com.wayfair.bricks.BrickFragment;
import com.wayfair.bricks.OrientationBrickSize;
import com.wayfair.bricks.samples.TextBrick;

public class SimpleBrickFragment extends BrickFragment {
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
                    "Brick: " + i
            );
            brickRecyclerAdapter.addItem(unusedBrick2);
        }
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
