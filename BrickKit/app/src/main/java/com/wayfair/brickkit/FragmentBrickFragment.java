package com.wayfair.brickkit;

import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.wayfair.bricks.BaseBrick;
import com.wayfair.bricks.BrickBehaviour;
import com.wayfair.bricks.BrickFragment;
import com.wayfair.bricks.BrickRecyclerAdapter;
import com.wayfair.bricks.FragmentBrick;
import com.wayfair.bricks.OrientationBrickSize;
import com.wayfair.bricks.SimpleBrickSize;
import com.wayfair.bricks.samples.TextBrick;

import java.util.ArrayList;

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
