package com.wayfair.brickkit;

import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.wayfair.bricks.BaseBrick;
import com.wayfair.bricks.BrickBehaviour;
import com.wayfair.bricks.BrickFragment;
import com.wayfair.bricks.BrickRecyclerAdapter;
import com.wayfair.bricks.OnReachedItemAtPosition;
import com.wayfair.bricks.OrientationBrickSize;
import com.wayfair.bricks.samples.TextBrick;
import com.wayfair.bricks.samples.VerticalTextBrick;

import java.util.ArrayList;

public class InfiniteScrollBrickFragment extends BrickFragment {
    private static final int MAX_SPANS = 240;
    private static final int HALF = 120;

    private int page = 0;

    @Override
    public int maxSpans() {
        return MAX_SPANS;
    }

    @Override
    public void createBricks() {
        dataManager.brickRecyclerAdapter.setOnReachedItemAtPosition(
                new OnReachedItemAtPosition() {
                    @Override
                    public void bindingItemAtPosition(int position) {
                        if (position == dataManager.getRecyclerViewItems().size() - 1) {
                            page++;
                            addNewBricks();
                        }
                    }
                }
        );

        addNewBricks();
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

    public void addNewBricks() {
        for (int i = 0; i < 100; i++) {
            BaseBrick unusedBrick2 = new TextBrick(
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
                    "Brick: " + page + " " + i
            );
            dataManager.addLast(unusedBrick2);
        }
    }
}
