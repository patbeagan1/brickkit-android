
<div align="center">
<span><img src="Docs/SampleImage/BrickKit.PNG" alt="BrickKit" title="BrickKit" ></span>
<span style="font-size:60px; writing-mode: tb-rl;">BrickKit</span>
</div>


## What is the BrickKit

BrickKit is a tool developed with the Android RecyclerView and GridLayout. With BrickKit, you could manager complex and different layouts (bricks) on the same page by one RecyclerView and DataManger. It's easy to reuse and extend bricks which highly reduces the code redundancy and difficulty of UI testing.


## How to import BrickKit as a library
```
 compile 'XXXXXXX' on your project build.gralde
```

## How to run BrickKit demo project

```
1. git clone 'XXXXXXX'
2. Open Android Studio -> New -> Import Project
3. Run BrickKit
```

## Features of BrickKit

### BasicFragment

![SimpleFragment](Docs/SampleImage/SimpleFragment.PNG)
```java
public class SimpleBrickFragment extends BrickFragment {
    private static final int MAX_SPANS = 240;
    private static final int HALF = 120;

    @Override
    public int maxSpans() {
        return MAX_SPANS;
    }

    @Override
    public void createBricks() {
        for (int i = 0; i < 100; i++) {
            TextBrick textBrick = new TextBrick(
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
            dataManager.addLast(textBrick);
        }
    }

    @Override
    public void addBehaviors() { }

    @Override
    public int orientation() {
        return OrientationHelper.VERTICAL;
    }

    @Override
    public boolean reverse() {
        return false;
    }
}

```

| Method   |      Description  |
|----------|:-------------:|
| maxSpans |  Allows to specify the maxSpans size of screen.
| createBricks |  Allows to constitute the different types of 'bricks' to the View.
| addBehaviors |  Adds complex layout interactions to View.  
| orientation |   Allows the 'bricks' displayed at two orientations (OrientationHelper.VERTICAL/OrientationHelper.HORIZONTAL)
| reverse |  While reverse is false, the bricks will be added from the top of screen, otherwise it will be added from the bottom.


### Bricks with different spans

![Span Example](Docs/SampleImage/SpanExample.PNG)

```java
@Override
public void createBricks() {
  for (int i = 0; i < MAX_SPANS / HALF; i++) {
      TextBrick textBrick = new TextBrick(
              getContext(),
              new OrientationBrickSize(dataManager) {
                  @Override
                  protected int portrait() {
                      return HALF;
                  }

                  @Override
                  protected int landscape() {
                      return HALF;
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
              "Brick: " + 0
      );
      dataManager.addLast(textBrick);
  }

  for (int i = 0; i < MAX_SPANS / ONE_THIRD; i++) {
      TextBrick textBrick = new TextBrick(
              getContext(),
              new OrientationBrickSize(dataManager) {
                  @Override
                  protected int portrait() {
                      return ONE_THIRD;
                  }

                  @Override
                  protected int landscape() {
                      return ONE_THIRD;
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
              "Brick: " + 0
      );
      dataManager.addLast(textBrick);
  }

  for (int i = 0; i < MAX_SPANS / QUARTER; i++) {
      TextBrick textBrick = new TextBrick(
              getContext(),
              new OrientationBrickSize(dataManager) {
                  @Override
                  protected int portrait() {
                      return QUARTER;
                  }

                  @Override
                  protected int landscape() {
                      return QUARTER;
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
              "Brick: " + 0
      );
      dataManager.addLast(textBrick);
    }
}
```

### Bricks with different behaviors

#### StickyHeader layout
![StickyHeader Example](Docs/SampleImage/StickyHeader.PNG)

```java
@Override
public void addBehaviors() {
     dataManager.addBehavior(new StickyHeaderBehavior(dataManager));
}

@Override
public void createBricks() {
        BaseBrick brick = new TextBrick(
                getContext(),
                new SimpleBrickSize(dataManager) {
                    @Override
                    protected int size() {
                        return MAX_SPANS;
                    }
                },
                "simple" + i
        );
        brick.setFooter(true);
        dataManager.addLast(brick);
    }
}Â
```

#### StickyFooter layout
![StickyFooter Example](Docs/SampleImage/StickyFooter.PNG)

```java
@Override
public void addBehaviors() {
     dataManager.addBehavior(new StickyFooterBehavior(dataManager));
}

@Override
public void createBricks() {
        BaseBrick brick = new TextBrick(
                getContext(),
                new SimpleBrickSize(dataManager) {
                    @Override
                    protected int size() {
                        return MAX_SPANS;
                    }
                },
                "simple" + i
        );
        brick.setFooter(true);
        dataManager.addLast(brick);
    }
}
```

#### Reverse layout
![Reverse Example](Docs/SampleImage/Reverse.PNG)
```java
@Override
public boolean reverse() {
    return true;
}
```

#### InfiniteScroll layout
```java
@Override
public void createBricks() {
    dataManager.getBrickRecyclerAdapter().setOnReachedItemAtPosition(
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
```

## Customize your own brick
```java
public class CustomizedBrick extends BaseBrick {
  /**
   * Constructor.
   *
   * @param context context this brick exists in
   * @param spanSize size information for this brick
   * @param padding padding for this brick
   */
    public CustomizedBrick(Context context, BrickSize spanSize, BrickPadding padding) {
        super(context, spanSize, padding);
    }

    /**
     * Called by the BrickRecyclerAdapter to display the information in this brick to the specified ViewHolder.
     *
     * @param holder ViewHolder which should be updated.
     */    
    @Override
    public void onBindData(RecyclerView.ViewHolder holder) {

    }

    /**
     * Gets the template string for this brick type.
     *
     * @return the template string for this brick type
     */
    @Override
    public String getTemplate() {
        return null;
    }

    /**
     * Get layout resource id for this brick.
     *
     * @return the layout resource id for this brick
     */
    @Override
    public int getLayout() {
        return 0;
    }

    /**
     * Creates an instance of the {@link BrickViewHolder} for this class.
     *
     * @param itemView view to pass into the {@link BrickViewHolder}
     * @return the {@link BrickViewHolder}
     */    @Override
    public BrickViewHolder createViewHolder(View itemView) {
        return null;
    }
}
```


## Manager your bricks with BaseDataManager

The 'BaseDataManager' manages the RecyclerView's adapter and manipulate the each bricks. You could add/remove 'bricks' at different positions, get/replace certain 'bricks' at the certain position.

| Methods Used Frequently   |      Description  |
|----------|:-------------:|
| getRecyclerViewItems |  Gets all 'visible brick' items in the 'BaseDataManager'.
| getDataManagerItems |  Gets all 'brick' items in the 'BaseDataManager'.
| addLast |  Inserts brick/Collection of bricks after all other bricks.
| addFirst |   Inserts brick/Collection of bricks before all other bricks.
| removeItem |  Remove a brick from the 'BaseDataManager'.
| brickWithLayout |  Retrieves a brick who's associated layout resource ID matches.
| brickAtPosition |  Retrieves a brick at a specific position.

## Credits

BrickKit is owned and maintained by [Wayfair](https://www.wayfair.com).

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).


## License

BrickKit is released under the Apache license. See LICENSE for details.
