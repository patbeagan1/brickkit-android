package com.wayfair.brickkit;

import android.graphics.Rect;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wayfair.bricks.BaseBrick;
import com.wayfair.bricks.BrickBehaviour;
import com.wayfair.bricks.BrickFragment;
import com.wayfair.bricks.BrickRecyclerAdapter;
import com.wayfair.bricks.BrickRecyclerItemDecoration;
import com.wayfair.bricks.SimpleBrickPadding;
import com.wayfair.bricks.SimpleBrickSize;
import com.wayfair.bricks.samples.TextBrick;
import com.wayfair.bricks.samples.UnusedBrick;
import com.wayfair.bricks.samples.UsedBrick;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends BrickFragment {
    private static final int MAX_SPANS = 240;
    private static final int ONE_FIFTH = 48;
    private static final int TWO_FIFTH = 96;

    public ArrayList<BaseBrick> usedBricks;

    @Override
    public int maxSpans() {
        return MAX_SPANS;
    }

    public void createBricks() {
        usedBricks = new ArrayList<>();

        usedBricks.add(
                new UsedBrick(
                        getContext(),
                        new SimpleBrickSize(dataManager) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        },
                        new SimpleBrickPadding() {
                            @Override
                            protected int padding() {
                                return 10;
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
                        new SimpleBrickSize(dataManager) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH+ONE_FIFTH-25;
                            }
                        },
                        new SimpleBrickPadding() {
                            @Override
                            protected int padding() {
                                return 10;
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
                        new SimpleBrickSize(dataManager) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        },
                        new SimpleBrickPadding() {
                            @Override
                            protected int padding() {
                                return 10;
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
                        new SimpleBrickSize(dataManager) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        },
                        new SimpleBrickPadding() {
                            @Override
                            protected int padding() {
                                return 10;
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
                        new SimpleBrickSize(dataManager) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        },
                        new SimpleBrickPadding() {
                            @Override
                            protected int padding() {
                                return 10;
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
                        new SimpleBrickSize(dataManager) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        },
                        new SimpleBrickPadding() {
                            @Override
                            protected int padding() {
                                return 10;
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
                        new SimpleBrickSize(dataManager) {
                            @Override
                            protected int size() {
                                return ONE_FIFTH;
                            }
                        };
                last =
                        new SimpleBrickSize(dataManager) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        };
            } else {
                first =
                        new SimpleBrickSize(dataManager) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        };
                last =
                        new SimpleBrickSize(dataManager) {
                            @Override
                            protected int size() {
                                return ONE_FIFTH;
                            }
                        };
            }

            UnusedBrick unusedBrick1 = new UnusedBrick(getContext(), first, new SimpleBrickPadding() {
                @Override
                protected int padding() {
                    return 10;
                }
            });
            dataManager.addLast(unusedBrick1);

            if (i == 0 || i == usedBricks.size() + 1) {
                UnusedBrick usedBrick = new UnusedBrick(
                        getContext(),
                        new SimpleBrickSize(dataManager) {
                            @Override
                            protected int size() {
                                return TWO_FIFTH;
                            }
                        },
                        new SimpleBrickPadding() {
                            @Override
                            protected int padding() {
                                return 10;
                            }
                        }
                );
                dataManager.addLast(usedBrick);
            } else {
                dataManager.addLast(usedBricks.get(i - 1));
            }

            UnusedBrick unusedBrick2 = new UnusedBrick(getContext(), last, new SimpleBrickPadding() {
                @Override
                protected int padding() {
                    return 10;
                }
            });
            dataManager.addLast(unusedBrick2);

            dataManager.addBeforeItem(
                    dataManager.getRecyclerViewItems().get(2),
                    new UsedBrick(
                            getContext(),
                            new SimpleBrickSize(dataManager) {
                                @Override
                                protected int size() {
                                    return 25;
                                }
                            },
                            new SimpleBrickPadding() {
                                @Override
                                protected int padding() {
                                    return 10;
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
//            if (i == 1){
//                break;
//            }

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
