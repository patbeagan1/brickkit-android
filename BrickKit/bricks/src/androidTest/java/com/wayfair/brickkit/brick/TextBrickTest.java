package com.wayfair.brickkit.brick;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.LinearLayout;

import com.wayfair.brickkit.padding.BrickPadding;
import com.wayfair.brickkit.size.BrickSize;
import com.wayfair.brickkit.ViewHolderRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class TextBrickTest {
    private static final String TEMPLATE = "cms/bricks/text_brick";
    private static final String TEXT = "text";
    private Context context;
    private BrickSize brickSize;
    private BrickPadding brickPadding;

    @Before
    public void setup() {
        context = InstrumentationRegistry.getTargetContext();
        brickSize = mock(BrickSize.class);
        brickPadding = mock(BrickPadding.class);
    }

    @Test
    public void testThreeArgumentConstructor() {
        assertNotNull(new TextBrick(context, brickSize, TEXT));
    }

    @Test
    public void testFourArgumentConstructor() {
        assertNotNull(new TextBrick(context, brickSize, brickPadding, TEXT));
    }

    @Test
    public void testGetTemplate() {
        TextBrick textBrick = new TextBrick(context, brickSize, TEXT);

        assertEquals(TEMPLATE, textBrick.getTemplate());
    }

    @Test
    public void testOnBindData() {
        TextBrick brick = new TextBrick(context, brickSize, TEXT);

        LinearLayout linearLayout = new LinearLayout(context);

        TextBrick.TextViewHolder holder = (TextBrick.TextViewHolder) ViewHolderRegistry.mapToRecyclerView(TEMPLATE, linearLayout);

        brick.onBindData(holder);

        assertEquals(TEXT, holder.textView.getText().toString());
    }
}
