package com.wayfair.brickkitdemo;

import android.annotation.SuppressLint;
import android.support.v7.widget.OrientationHelper;
import android.view.View;

import com.wayfair.brickkit.BaseBrick;
import com.wayfair.brickkit.BrickFragment;
import com.wayfair.brickkit.InnerOuterBrickPadding;
import com.wayfair.brickkit.SimpleBrickSize;
import com.wayfair.brickkit.StickyFooterHelper;
import com.wayfair.brickkit.StickyHeaderHelper;
import com.wayfair.brickkitdemo.bricks.ControllerBrick;
import com.wayfair.brickkit.bricks.TextBrick;

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
                        String.valueOf(NUMBER_OF_BRICKS - 1),
                        "Index",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int index = Integer.parseInt(controllerBrick.value);

                                if (index < 0 || index >= dataManager.getRecyclerViewItems().size()) {
                                    return;
                                }

                                dataManager.removeItem(dataManager.getRecyclerViewItems().get(index));
                            }
                        },
                        new View.OnClickListener() {
                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onClick(View v) {
                                int index = Integer.parseInt(controllerBrick.value);

                                if (index < 0 || index > dataManager.getRecyclerViewItems().size()) {
                                    return;
                                }

                                if (index == dataManager.getRecyclerViewItems().size()) {
                                    dataManager.addLast(
                                            new TextBrick(
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
                                                    String.format(FORMAT, index)
                                            )
                                    );
                                } else {
                                    dataManager.addBeforeItem(
                                            dataManager.getRecyclerViewItems().get(index),
                                            new TextBrick(
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
                                                    String.format(FORMAT, index)
                                            )
                                    );
                                }

                                controllerBrick.value = String.valueOf(index + 1);
                                dataManager.refreshItem(controllerBrick);
                            }
                        }
                );

                controllerBrick.setHeader(true);
                controllerBrick.setFooter(true);

                dataManager.addLast(controllerBrick);
            } else {
                @SuppressLint("DefaultLocale") BaseBrick brick = new TextBrick(
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
                        String.format(FORMAT, i)
                );

                dataManager.addLast(brick);
            }
        }
    }

    @Override
    public void addBehaviours() {
        dataManager.behaviours.add(new StickyHeaderHelper(dataManager));
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
