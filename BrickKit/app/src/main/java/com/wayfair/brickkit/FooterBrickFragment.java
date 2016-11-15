package com.wayfair.brickkit;

import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.wayfair.bricks.BrickBehaviour;
import com.wayfair.bricks.BrickFragment;
import com.wayfair.bricks.BrickRecyclerAdapter;
import com.wayfair.bricks.InnerOuterBrickPadding;
import com.wayfair.bricks.SimpleBrickSize;
import com.wayfair.bricks.StickyFooterHelper;
import com.wayfair.bricks.StickyHeaderHelper;
import com.wayfair.bricks.samples.TextBrick;

import java.util.ArrayList;

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
                    new SimpleBrickSize(dataManager){
                        @Override
                        protected int size() {
                            return MAX_SPANS;
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
                unusedBrick2.footer = true;
            }

            dataManager.addLast(unusedBrick2);
        }
    }

    @Override
    public void addBehaviours() {
        dataManager.behaviours.add(new StickyFooterHelper(dataManager));
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
