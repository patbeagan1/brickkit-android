package com.wayfair.brickkit;

import android.support.v7.widget.OrientationHelper;

import com.wayfair.bricks.BrickFragment;
import com.wayfair.bricks.SimpleBrickSize;
import com.wayfair.bricks.samples.TextBrick;

public class FooterBrickFragment extends BrickFragment {
    private static final int MAX_SPANS = 240;

    @Override
    public int maxSpans() {
        return MAX_SPANS;
    }

    @Override
    public void createBricks() {
        for (int i = 0; i < 100; i++) {
            TextBrick unusedBrick2 = new TextBrick(
                    getContext(),
                    new SimpleBrickSize(brickRecyclerAdapter){
                        @Override
                        protected int size() {
                            return MAX_SPANS;
                        }
                    },
                    "Brick: " + i
            );

            if (i % 10 == 0) {
                unusedBrick2.footer = true;
            }

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
