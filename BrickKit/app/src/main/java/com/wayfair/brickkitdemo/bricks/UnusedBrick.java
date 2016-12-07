package com.wayfair.brickkitdemo.bricks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wayfair.brickkit.BaseBrick;
import com.wayfair.brickkit.BrickPadding;
import com.wayfair.brickkit.BrickSize;
import com.wayfair.brickkit.BrickViewHolder;
import com.wayfair.brickkit.ViewHolderRegistry;
import com.wayfair.brickkitdemo.R;

public class UnusedBrick extends BaseBrick {
    private static final String BRICK_TEMPLATE = "cms/bricks/unused_brick";

    static {
        ViewHolderRegistry.register(BRICK_TEMPLATE, new ViewHolderRegistry.GenerateViewHolderInterface() {
            @Override
            public BrickViewHolder generateViewHolder(ViewGroup parent) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.unused_brick, parent, false);

                return new UnusedBrickHolder(itemView);
            }
        });
    }

    public UnusedBrick(Context context, BrickSize spanSize) {
        super(context, spanSize);
    }

    public UnusedBrick(Context context, BrickSize spanSize, BrickPadding padding) {
        super(context, spanSize, padding);
    }


    @Override
    public void onBindData(RecyclerView.ViewHolder holder) {
    }

    @Override
    public String getTemplate() {
        return BRICK_TEMPLATE;
    }

    private static class UnusedBrickHolder extends BrickViewHolder {
        UnusedBrickHolder(View itemView) {
            super(itemView);
        }
    }
}
