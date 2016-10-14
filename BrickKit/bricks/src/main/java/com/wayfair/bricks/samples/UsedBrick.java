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

public class UsedBrick extends BaseBrick implements TouchableBrick {
    private static final String BRICK_TEMPLATE = "cms/bricks/used_brick";

    public CharSequence text;
    private View.OnClickListener onTouch;

    static {
        ViewHolderRegistry.register(BRICK_TEMPLATE, new ViewHolderRegistry.GenerateViewHolderInterface() {
            @Override
            public BrickViewHolder generateViewHolder(ViewGroup parent) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.used_brick, parent, false);

                return new UsedBrickHolder(itemView);
            }
        });
    }

    public UsedBrick(Context context, BrickSize spanSize, CharSequence text, View.OnClickListener onTouch) {
        super(context, spanSize);

        this.text = text;
        this.onTouch = onTouch;
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder) {
        if (holder instanceof UsedBrickHolder) {
            UsedBrickHolder editTextViewHolder = (UsedBrickHolder) holder;
            editTextViewHolder.textView.setText(text);
            if (isEnabled()) {
                holder.itemView.setOnClickListener(onTouch());
            }
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

    @Override
    public View.OnClickListener onTouch() {
        return onTouch;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private static class UsedBrickHolder extends BrickViewHolder {
        TextView textView;

        UsedBrickHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.label);
        }
    }
}
