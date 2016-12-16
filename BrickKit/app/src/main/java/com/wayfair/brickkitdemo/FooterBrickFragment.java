package com.wayfair.brickkitdemo;

import com.wayfair.brickkit.BrickFragment;
import com.wayfair.brickkit.padding.InnerOuterBrickPadding;
import com.wayfair.brickkit.size.SimpleBrickSize;
import com.wayfair.brickkit.behavior.StickyFooterBehavior;
import com.wayfair.brickkit.brick.TextBrick;


/**
 * Fragment which shows the brick footer behavior.
 *
 * Every tenth brick is a "footer" which means it will remain on screen until
 * another "footer" brick is scrolled into the footer area.
 */
public class FooterBrickFragment extends BrickFragment {
    @Override
    public void createBricks() {
        for (int i = 0; i < 100; i++) {
            TextBrick unusedBrick2 = new TextBrick(
                    getContext(),
                    new SimpleBrickSize(maxSpans()) {
                        @Override
                        protected int size() {
                            return dataManager.getMaxSpanCount();
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
    public void addBehaviors() {
        dataManager.addBehavior(new StickyFooterBehavior(dataManager));
    }
}
