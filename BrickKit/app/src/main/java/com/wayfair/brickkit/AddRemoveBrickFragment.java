package com.wayfair.brickkit;

import android.annotation.SuppressLint;
import android.support.v7.widget.OrientationHelper;
import android.view.View;

import com.wayfair.brickkit.bricks.ControllerBrick;
import com.wayfair.bricks.BaseBrick;
import com.wayfair.bricks.BrickFragment;
import com.wayfair.bricks.SimpleBrickSize;
import com.wayfair.bricks.samples.TextBrick;

public class AddRemoveBrickFragment extends BrickFragment {
    private static final int MAX_SPANS = 240;
    public static final int NUMBER_OF_BRICKS = 20;
    public static final String FORMAT = "Brick: %d";
    ControllerBrick controllerBrick;

    @Override
    public int maxSpans() {
        return MAX_SPANS;
    }

    @Override
    public void createBricks() {
        for (int i = 0; i < NUMBER_OF_BRICKS; i++) {

            if (i == 0) {
                controllerBrick = new ControllerBrick(
                        getContext(),
                        new SimpleBrickSize(brickRecyclerAdapter){
                            @Override
                            protected int size() {
                                return MAX_SPANS;
                            }
                        },
                        String.valueOf(NUMBER_OF_BRICKS),
                        "Index",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int index = Integer.parseInt(controllerBrick.value);

                                if (index < 0 || index >= brickRecyclerAdapter.size()) {
                                    return;
                                }

                                brickRecyclerAdapter.removeItem(index);

                                controllerBrick.value = String.valueOf(index - 1);
                                brickRecyclerAdapter.refreshItem(brickRecyclerAdapter.adapterIndex(controllerBrick));
                            }
                        },
                        new View.OnClickListener() {
                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onClick(View v) {
                                int index = Integer.parseInt(controllerBrick.value);

                                if (index < 0 || index > brickRecyclerAdapter.size()) {
                                    return;
                                }

                                brickRecyclerAdapter.addItem(
                                        index,
                                        new TextBrick(
                                                getContext(),
                                                new SimpleBrickSize(brickRecyclerAdapter) {
                                                    @Override
                                                    protected int size() {
                                                        return MAX_SPANS;
                                                    }
                                                },
                                                String.format(FORMAT, index)
                                        )
                                );

                                controllerBrick.value = String.valueOf(index + 1);
                                brickRecyclerAdapter.refreshItem(brickRecyclerAdapter.adapterIndex(controllerBrick));
                            }
                        }
                );

                controllerBrick.header = true;

                brickRecyclerAdapter.addItem(controllerBrick);
            } else {
                @SuppressLint("DefaultLocale") BaseBrick brick = new TextBrick(
                        getContext(),
                        new SimpleBrickSize(brickRecyclerAdapter) {
                            @Override
                            protected int size() {
                                return MAX_SPANS;
                            }
                        },
                        String.format(FORMAT, i)
                );

                brickRecyclerAdapter.addItem(brick);
            }
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
