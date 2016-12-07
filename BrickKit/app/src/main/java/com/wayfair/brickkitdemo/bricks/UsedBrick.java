package com.wayfair.brickkitdemo.bricks;

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
import com.wayfair.brickkit.ViewHolderRegistry;
import com.wayfair.brickkitdemo.R;

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

    public UsedBrick(Context context, BrickSize spanSize, BrickPadding brickPadding, CharSequence text, View.OnClickListener onTouch) {
        super(context, spanSize, brickPadding);
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
