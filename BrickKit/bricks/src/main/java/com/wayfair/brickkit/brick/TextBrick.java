package com.wayfair.brickkit.brick;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wayfair.brickkit.padding.BrickPadding;
import com.wayfair.brickkit.size.BrickSize;
import com.wayfair.brickkit.BrickViewHolder;
import com.wayfair.brickkit.R;

/**
 * Simple Brick with a single text view.
 */
public class TextBrick extends BaseBrick {
    private static final String BRICK_TEMPLATE = "cms/bricks/text_brick";

    private final CharSequence text;

    /**
     * Constructor which uses the default padding.
     *
     * @param context context this brick exists in
     * @param spanSize size information for this brick
     * @param text text to display on this brick
     */
    public TextBrick(Context context, BrickSize spanSize, CharSequence text) {
        super(context, spanSize);
        this.text = text;
    }

    /**
     * Constructor.
     *
     * @param context context this brick exists in
     * @param spanSize size information for this brick
     * @param padding padding for this brick
     * @param text text to display on this brick
     */
    public TextBrick(Context context, BrickSize spanSize, BrickPadding padding, CharSequence text) {
        super(context, spanSize, padding);
        this.text = text;
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder viewHolder) {
        TextViewHolder editTextViewHolder = (TextViewHolder) viewHolder;
        editTextViewHolder.textView.setText(text);
    }

    @Override
    public String getTemplate() {
        return BRICK_TEMPLATE;
    }

    @Override
    public int getLayout() {
        return R.layout.text_brick;
    }

    @Override
    public BrickViewHolder createViewHolder(View itemView) {
        return new TextViewHolder(itemView);
    }

    /**
     * {@link BrickViewHolder} for TextBrick.
     */
    static class TextViewHolder extends BrickViewHolder {
        TextView textView;

        /**
         * Constructor for TextViewHolder.
         *
         * @param itemView view for this brick
         */
        TextViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }
}
