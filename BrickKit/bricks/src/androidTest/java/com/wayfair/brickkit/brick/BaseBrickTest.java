package com.wayfair.brickkit.brick;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.wayfair.brickkit.BrickViewHolder;
import com.wayfair.brickkit.padding.BrickPadding;
import com.wayfair.brickkit.size.BrickSize;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class BaseBrickTest {
    private static final int PADDING = 3;
    private Context context;
    private BrickSize brickSize;

    @Before
    public void setup() {
        context = InstrumentationRegistry.getTargetContext();
        brickSize = mock(BrickSize.class);
    }

    @Test
    public void testTwoArgumentConstructor() {
        TestBaseBrick brick = new TestBaseBrick(context, brickSize);

        assertEquals(brickSize, brick.getSpanSize());
        assertEquals(0, brick.getPadding().getInnerBottomPadding());
    }

    @Test
    public void testThreeArgumentConstructor() {
        BrickPadding padding = mock(BrickPadding.class);
        when(padding.getInnerBottomPadding()).thenReturn(PADDING);

        TestBaseBrick brick = new TestBaseBrick(context, brickSize, padding);

        assertEquals(brickSize, brick.getSpanSize());
        assertEquals(PADDING, brick.getPadding().getInnerBottomPadding());
    }

    @Test
    public void testHidden() {
        TestBaseBrick brick = new TestBaseBrick(context, brickSize);

        assertFalse(brick.isHidden());

        brick.setHidden(true);
        assertTrue(brick.isHidden());
    }

    @Test
    public void testHeader() {
        TestBaseBrick brick = new TestBaseBrick(context, brickSize);

        assertFalse(brick.isHeader());

        brick.setHeader(true);
        assertTrue(brick.isHeader());
    }

    @Test
    public void testFooter() {
        TestBaseBrick brick = new TestBaseBrick(context, brickSize);

        assertFalse(brick.isFooter());

        brick.setFooter(true);
        assertTrue(brick.isFooter());
    }

    @Test
    public void testInFirstRow() {
        TestBaseBrick brick = new TestBaseBrick(context, brickSize);

        assertFalse(brick.isInFirstRow());

        brick.setInFirstRow(true);
        assertTrue(brick.isInFirstRow());
    }

    @Test
    public void testInLastRow() {
        TestBaseBrick brick = new TestBaseBrick(context, brickSize);

        assertFalse(brick.isInLastRow());

        brick.setInLastRow(true);
        assertTrue(brick.isInLastRow());
    }

    @Test
    public void testOnLeftWall() {
        TestBaseBrick brick = new TestBaseBrick(context, brickSize);

        assertFalse(brick.isOnLeftWall());

        brick.setOnLeftWall(true);
        assertTrue(brick.isOnLeftWall());
    }

    @Test
    public void testOnRightWall() {
        TestBaseBrick brick = new TestBaseBrick(context, brickSize);

        assertFalse(brick.isOnRightWall());

        brick.setOnRightWall(true);
        assertTrue(brick.isOnRightWall());
    }

    private static final class TestBaseBrick extends BaseBrick {

        private TestBaseBrick(Context context, BrickSize spanSize, BrickPadding padding) {
            super(context, spanSize, padding);
        }

        private TestBaseBrick(Context context, BrickSize spanSize) {
            super(context, spanSize);
        }

        @Override
        public void onBindData(BrickViewHolder holder) {

        }

        @Override
        public int getLayout() {
            return 0;
        }

        @Override
        public BrickViewHolder createViewHolder(View itemView) {
            return null;
        }
    }
}
