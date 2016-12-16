package com.wayfair.brickkitdemo;

import android.view.View;

import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.BrickFragment;
import com.wayfair.brickkit.padding.InnerOuterBrickPadding;
import com.wayfair.brickkit.size.SimpleBrickSize;
import com.wayfair.brickkitdemo.bricks.UnusedBrick;
import com.wayfair.brickkitdemo.bricks.UsedBrick;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends BrickFragment {
    private static final int ONE_FIFTH = 48;
    private static final int TWO_FIFTH = 96;

    @Override
    public void createBricks() {
        ArrayList<BaseBrick> usedBricks = new ArrayList<>();

        usedBricks.add(
                new UsedBrick(
                        getContext(),
                        new SimpleBrickSize(maxSpans()) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        },
                        new InnerOuterBrickPadding() {
                            @Override
                            protected int innerPadding() {
                                return 5;
                            }

                            @Override
                            protected int outerPadding() {
                                return 0;
                            }
                        },
                        "Simple Brick View",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.content, new SimpleBrickFragment())
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }
                )
        );
        usedBricks.add(
                new UsedBrick(
                        getContext(),
                        new SimpleBrickSize(maxSpans()) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        },
                        new InnerOuterBrickPadding() {
                            @Override
                            protected int innerPadding() {
                                return 5;
                            }

                            @Override
                            protected int outerPadding() {
                                return 0;
                            }
                        },
                        "Header Brick View",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.content, new HeaderBrickFragment())
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }
                )
        );
        usedBricks.add(
                new UsedBrick(
                        getContext(),
                        new SimpleBrickSize(maxSpans()) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        },
                        new InnerOuterBrickPadding() {
                            @Override
                            protected int innerPadding() {
                                return 5;
                            }

                            @Override
                            protected int outerPadding() {
                                return 0;
                            }
                        },
                        "Footer Brick View",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.content, new FooterBrickFragment())
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }
                )
        );
        usedBricks.add(
                new UsedBrick(
                        getContext(),
                        new SimpleBrickSize(maxSpans()) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        },
                        new InnerOuterBrickPadding() {
                            @Override
                            protected int innerPadding() {
                                return 5;
                            }

                            @Override
                            protected int outerPadding() {
                                return 0;
                            }
                        },
                        "Add/Remove Brick View",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.content, new AddRemoveBrickFragment())
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }
                )
        );
        usedBricks.add(
                new UsedBrick(
                        getContext(),
                        new SimpleBrickSize(maxSpans()) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        },
                        new InnerOuterBrickPadding() {
                            @Override
                            protected int innerPadding() {
                                return 5;
                            }

                            @Override
                            protected int outerPadding() {
                                return 0;
                            }
                        },
                        "Infinite Scroll Brick View",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.content, new InfiniteScrollBrickFragment())
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }
                )
        );
        usedBricks.add(
                new UsedBrick(
                        getContext(),
                        new SimpleBrickSize(maxSpans()) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        },
                        new InnerOuterBrickPadding() {
                            @Override
                            protected int innerPadding() {
                                return 5;
                            }

                            @Override
                            protected int outerPadding() {
                                return 0;
                            }
                        },
                        "Fragment Brick View",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.content, new FragmentBrickFragment())
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }
                )
        );

        for (int i = 0; i < usedBricks.size() + 2; i++) {
            SimpleBrickSize first;
            SimpleBrickSize last;

            if (i % 2 == 0) {
                first =
                        new SimpleBrickSize(maxSpans()) {
                            @Override
                            protected int size() {
                                return ONE_FIFTH;
                            }
                        };
                last =
                        new SimpleBrickSize(maxSpans()) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        };
            } else {
                first =
                        new SimpleBrickSize(maxSpans()) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        };
                last =
                        new SimpleBrickSize(maxSpans()) {
                            @Override
                            protected int size() {
                                return ONE_FIFTH;
                            }
                        };
            }

            UnusedBrick unusedBrick1 = new UnusedBrick(getContext(), first, new InnerOuterBrickPadding() {
                @Override
                protected int innerPadding() {
                    return 5;
                }

                @Override
                protected int outerPadding() {
                    return 0;
                }
            });
            dataManager.addLast(unusedBrick1);

            if (i == 0 || i == usedBricks.size() + 1) {
                UnusedBrick usedBrick = new UnusedBrick(
                        getContext(),
                        new SimpleBrickSize(maxSpans()) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        },
                        new InnerOuterBrickPadding() {
                            @Override
                            protected int innerPadding() {
                                return 5;
                            }

                            @Override
                            protected int outerPadding() {
                                return 0;
                            }
                        }
                );
                dataManager.addLast(usedBrick);
            } else {
                dataManager.addLast(usedBricks.get(i - 1));
            }

            UnusedBrick unusedBrick2 = new UnusedBrick(getContext(), last, new InnerOuterBrickPadding() {
                @Override
                protected int innerPadding() {
                    return 5;
                }

                @Override
                protected int outerPadding() {
                    return 0;
                }
            });
            dataManager.addLast(unusedBrick2);

        }

    }
}
