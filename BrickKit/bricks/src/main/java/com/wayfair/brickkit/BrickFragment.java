package com.wayfair.brickkit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment which provides a simple interface for adding bricks / behaviors.
 */
public abstract class BrickFragment extends Fragment {
    public BrickDataManager dataManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (orientation() == OrientationHelper.VERTICAL) {
            return inflater.inflate(R.layout.vertical_fragment_brick, container, false);
        } else {
            return inflater.inflate(R.layout.horizontal_fragment_brick, container, false);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getView() != null) {
            RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
            dataManager = new BrickDataManager(getContext(), recyclerView, maxSpans());

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), maxSpans(), orientation(), reverse());
            gridLayoutManager.setSpanSizeLookup(new BrickSpanSizeLookup(getContext(), dataManager));
            recyclerView.setLayoutManager(gridLayoutManager);

            recyclerView.addItemDecoration(new BrickRecyclerItemDecoration(dataManager));

            addBehaviors();
            createBricks();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dataManager.onDestroy();
    }

    /**
     * Get the max spans for this fragment.
     *
     * @return the max spans for this fragment.
     */
    public abstract int maxSpans();

    /**
     * Method called to create bricks in this fragment.
     */
    public abstract void createBricks();

    /**
     * Method called to add behaviors to this fragment.
     */
    public abstract void addBehaviors();

    /**
     * Get the orientation to lay out this fragment.
     *
     * @return the orientation to lay out this fragment.
     */
    public abstract int orientation();

    /**
     * Whether or not to reverse the layout of this fragment.
     *
     * @return true if this fragment should be laid out in reverse
     */
    public abstract boolean reverse();
}
