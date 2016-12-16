package com.wayfair.brickkitdemo.bricks;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.size.BrickSize;
import com.wayfair.brickkit.BrickViewHolder;
import com.wayfair.brickkitdemo.R;

/**
 * Brick whose content is a fragment.
 */
public class FragmentBrick extends BaseBrick {
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
    public void onBindData(BrickViewHolder holder) {
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
    public int getLayout() {
        return R.layout.fragment_brick;
    }

    @Override
    public BrickViewHolder createViewHolder(View itemView) {
        return new FragmentBrickViewHolder(itemView);
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