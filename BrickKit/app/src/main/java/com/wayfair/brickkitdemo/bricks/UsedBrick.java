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

/**
 * {@link TouchableBrick} used in {@link com.wayfair.brickkitdemo.MainActivityFragment} to link to other fragments.
 */
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

    /**
     * Constructor for UsedBrick.
     *
     * @param context context this brick exists in
     * @param spanSize size information for this brick
     * @param padding padding for this brick
     * @param text text to display on this brick
     * @param onTouch listener for click events on this brick
     */
    public UsedBrick(Context context, BrickSize spanSize, BrickPadding padding, CharSequence text, View.OnClickListener onTouch) {
        super(context, spanSize, padding);
        this.text = text;
        this.onTouch = onTouch;
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder viewHolder) {
        UsedBrickHolder holder = (UsedBrickHolder) viewHolder;
        holder.textView.setText(text);
        if (isEnabled()) {
            holder.itemView.setOnClickListener(onTouch());
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

    /**
     * {@link BrickViewHolder} for UsedBrick.
     */
    private static class UsedBrickHolder extends BrickViewHolder {
        private final TextView textView;

        /**
         * Constructor for UsedBrickHolder.
         *
         * @param itemView view for this brick
         */
        UsedBrickHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.label);
        }
    }
}
