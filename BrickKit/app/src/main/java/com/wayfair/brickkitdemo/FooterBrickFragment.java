package com.wayfair.brickkitdemo;

import android.support.v7.widget.OrientationHelper;

import com.wayfair.brickkit.BrickFragment;
import com.wayfair.brickkit.InnerOuterBrickPadding;
import com.wayfair.brickkit.SimpleBrickSize;
import com.wayfair.brickkit.StickyFooterHelper;
import com.wayfair.brickkit.bricks.TextBrick;


/**
 * Fragment which shows the brick footer behavior.
 *
 * Every tenth brick is a "footer" which means it will remain on screen until
 * another "footer" brick is scrolled into the footer area.
 */
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
                    new SimpleBrickSize(dataManager) {
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
                unusedBrick2.setFooter(true);
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
