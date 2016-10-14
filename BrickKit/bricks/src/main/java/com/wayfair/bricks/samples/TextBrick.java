package com.wayfair.bricks.samples;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wayfair.bricks.BaseBrick;
import com.wayfair.bricks.BrickSize;
import com.wayfair.bricks.BrickViewHolder;
import com.wayfair.bricks.R;
import com.wayfair.bricks.ViewHolderRegistry;

public class TextBrick extends BaseBrick {
    private static final String BRICK_TEMPLATE = "cms/bricks/text_brick";

    public CharSequence text;

    static {
        ViewHolderRegistry.register(BRICK_TEMPLATE, new ViewHolderRegistry.GenerateViewHolderInterface() {
            @Override
            public BrickViewHolder generateViewHolder(ViewGroup parent) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_brick, parent, false);

                return new TextViewHolder(itemView);
            }
        });
    }

    public TextBrick(Context context, BrickSize spanSize, CharSequence text) {
        super(context, spanSize);

        this.text = text;
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder) {
        if (holder instanceof TextViewHolder) {
            TextViewHolder editTextViewHolder = (TextViewHolder) holder;
            editTextViewHolder.textView.setText(text);
        }
    }

    @Override
    public void padding(Rect outRect) {
        int defaultPadding = (int) context.getResources().getDimension(R.dimen.default_brick_inset_padding);

        outRect.set(defaultPadding, defaultPadding, defaultPadding, defaultPadding);
    }

    @Override
    public String getTemplate() {
        return BRICK_TEMPLATE;
    }

    private static class TextViewHolder extends BrickViewHolder {
        TextView textView;

        TextViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }
}
