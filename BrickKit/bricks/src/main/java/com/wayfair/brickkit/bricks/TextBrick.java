package com.wayfair.brickkit.bricks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wayfair.brickkit.BaseBrick;
import com.wayfair.brickkit.BrickPadding;
import com.wayfair.brickkit.BrickSize;
import com.wayfair.brickkit.BrickViewHolder;
import com.wayfair.brickkit.R;
import com.wayfair.brickkit.ViewHolderRegistry;

public class TextBrick extends BaseBrick {
    private static final String BRICK_TEMPLATE = "cms/bricks/text_brick";

    private final CharSequence text;

    static {
        ViewHolderRegistry.register(BRICK_TEMPLATE, new ViewHolderRegistry.GenerateViewHolderInterface() {
            @Override
            public TextViewHolder generateViewHolder(ViewGroup parent) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_brick, parent, false);

                return new TextViewHolder(itemView);
            }
        });
    }

    public TextBrick(Context context, BrickSize spanSize, CharSequence text) {
        super(context, spanSize);
        this.text = text;
    }

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

    static class TextViewHolder extends BrickViewHolder {
        TextView textView;

        TextViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }
}
