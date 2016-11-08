package com.wayfair.bricks.samples;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wayfair.bricks.BaseBrick;
import com.wayfair.bricks.BrickPadding;
import com.wayfair.bricks.BrickSize;
import com.wayfair.bricks.BrickViewHolder;
import com.wayfair.bricks.R;
import com.wayfair.bricks.ViewHolderRegistry;

public class VerticalTextBrick extends BaseBrick {
    private static final String BRICK_TEMPLATE = "cms/bricks/vertical_text_brick";

    public CharSequence text;

    static {
        ViewHolderRegistry.register(BRICK_TEMPLATE, new ViewHolderRegistry.GenerateViewHolderInterface() {
            @Override
            public BrickViewHolder generateViewHolder(ViewGroup parent) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_text_brick, parent, false);

                return new VerticalTextViewHolder(itemView);
            }
        });
    }

    public VerticalTextBrick(Context context, BrickSize spanSize, CharSequence text) {
        super(context, spanSize);
        this.text = text;
    }

    public VerticalTextBrick(Context context, BrickSize spanSize, BrickPadding padding, CharSequence text) {
        super(context, spanSize, padding);
        this.text = text;
    }




    @Override
    public void onBindData(RecyclerView.ViewHolder holder) {
        if (holder instanceof VerticalTextViewHolder) {
            VerticalTextViewHolder editTextViewHolder = (VerticalTextViewHolder) holder;
            editTextViewHolder.textView.setText(text);
        }
    }

    @Override
    public String getTemplate() {
        return BRICK_TEMPLATE;
    }

    private static class VerticalTextViewHolder extends BrickViewHolder {
        TextView textView;

        VerticalTextViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }
}
