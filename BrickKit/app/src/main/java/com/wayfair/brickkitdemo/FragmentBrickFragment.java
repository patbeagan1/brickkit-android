package com.wayfair.brickkitdemo;

import android.support.v7.widget.OrientationHelper;

import com.wayfair.brickkit.BaseBrick;
import com.wayfair.brickkit.BrickFragment;
import com.wayfair.brickkitdemo.bricks.FragmentBrick;
import com.wayfair.brickkit.SimpleBrickSize;

/**
 * Example of fragment containing {@link FragmentBrick}'s containing {@link SimpleBrickFragment}'s.
 */
public class FragmentBrickFragment extends BrickFragment {
    private static final int MAX_SPANS = 240;
    private static final int HALF = 120;

    @Override
    public int maxSpans() {
        return MAX_SPANS;
    }

    @Override
    public void createBricks() {
        for (int i = 0; i < 1; i++) {
            BaseBrick brick = new FragmentBrick(
                    getContext(),
                    new SimpleBrickSize(dataManager) {
                        @Override
                        protected int size() {
                            return MAX_SPANS;
                        }
                    },
                    getChildFragmentManager(),
                    new SimpleBrickFragment(),
                    "simple" + i
            );
            dataManager.addLast(brick);
        }
    }

    @Override
    public void addBehaviours() {

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
