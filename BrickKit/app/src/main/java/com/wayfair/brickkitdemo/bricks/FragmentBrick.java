package com.wayfair.brickkitdemo.bricks;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.wayfair.brickkit.BaseBrick;
import com.wayfair.brickkit.BrickSize;
import com.wayfair.brickkit.BrickViewHolder;
import com.wayfair.brickkit.R;
import com.wayfair.brickkit.ViewHolderRegistry;

/**
 * Brick whose content is a fragment.
 */
public class FragmentBrick extends BaseBrick {
    private static final String BRICK_TEMPLATE = "cms/bricks/fragment_brick";

    static {
        ViewHolderRegistry.register(BRICK_TEMPLATE, new ViewHolderRegistry.GenerateViewHolderInterface() {
            @Override
            public BrickViewHolder generateViewHolder(ViewGroup parent) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_brick, parent, false);

                return new FragmentBrickViewHolder(itemView);
            }
        });
    }

    private Fragment fragment;

    private FragmentManager fragmentManager;
    private String tag;

    /**
     * Constructor.
     *
     * @param context context this brick exists in
     * @param spanSize size information for this brick
     * @param fragmentManager fragmentManager to add the fragment to
     * @param fragment fragment to display in this brick
     * @param tag tag to use in {@link android.support.v4.app.FragmentTransaction}'s on the fragment
     */
    public FragmentBrick(Context context, BrickSize spanSize, FragmentManager fragmentManager, Fragment fragment, String tag) {
        super(context, spanSize);

        this.fragmentManager = fragmentManager;
        this.fragment = fragment;
        this.tag = tag;
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder) {
        if (holder instanceof FragmentBrickViewHolder) {
            View view;

            if (!fragment.isAdded()) {
                fragmentManager.beginTransaction().add(fragment, tag).commit();
                fragmentManager.executePendingTransactions();
                fragment.onAttach(context);
                fragment.onCreate(null);
                view = fragment.onCreateView(
                        LayoutInflater.from(holder.itemView.getContext()),
                        ((FragmentBrickViewHolder) holder).frameLayout,
                        null
                );
            } else {
                view = fragment.getView();
            }

            if (view != null && view.getParent() != null && view.getParent() instanceof ViewGroup) {
                ((ViewGroup) view.getParent()).removeView(view);
            }

            ((FragmentBrickViewHolder) holder).frameLayout.addView(view);
        }
    }

    @Override
    public String getTemplate() {
        return BRICK_TEMPLATE;
    }

    /**
     * {@link BrickViewHolder} for FragmentBrick.
     */
    private static final class FragmentBrickViewHolder extends BrickViewHolder {
        private final FrameLayout frameLayout;

        /**
         * Constructor for FragmentBrickViewHolder.
         *
         * @param itemView view for this brick
         */
        private FragmentBrickViewHolder(View itemView) {
            super(itemView);

            frameLayout = (FrameLayout) itemView.findViewById(R.id.fragment_container);
        }
    }
}