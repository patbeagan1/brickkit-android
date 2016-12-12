package com.wayfair.brickkitdemo;

import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.BrickFragment;
import com.wayfair.brickkitdemo.bricks.FragmentBrick;
import com.wayfair.brickkit.size.SimpleBrickSize;

/**
 * Example of fragment containing {@link FragmentBrick}'s containing {@link SimpleBrickFragment}'s.
 */
public class FragmentBrickFragment extends BrickFragment {
    @Override
    public void createBricks() {
        for (int i = 0; i < 1; i++) {
            BaseBrick brick = new FragmentBrick(
                    getContext(),
                    new SimpleBrickSize(dataManager) {
                        @Override
                        protected int size() {
                            return dataManager.getMaxSpanCount();
                        }
                    },
                    getChildFragmentManager(),
                    new SimpleBrickFragment(),
                    "simple" + i
            );
            dataManager.addLast(brick);
        }
    }
}
