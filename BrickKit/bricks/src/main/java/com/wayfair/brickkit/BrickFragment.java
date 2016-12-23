package com.wayfair.brickkit;

import android.os.Bundle;
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
    public BrickDataManager dataManager = new BrickDataManager(maxSpans());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataManager.setDragAndDrop(true);
        dataManager.setSwipeToDismiss(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        if (orientation() == OrientationHelper.VERTICAL) {
            view = inflater.inflate(R.layout.vertical_fragment_brick, container, false);
        } else {
            view = inflater.inflate(R.layout.horizontal_fragment_brick, container, false);
        }

        dataManager.setRecyclerView(getContext(), (RecyclerView) view.findViewById(R.id.recycler_view), orientation(), reverse());
        addBehaviors();
        createBricks();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dataManager.onDestroyView();
    }

    /**
     * Get the max spans for this fragment.
     *
     * @return the max spans for this fragment.
     */
    public int maxSpans() {
        return 240;
    }

    /**
     * Method called to create bricks in this fragment.
     */
    public abstract void createBricks();

    /**
     * Method called to add behaviors to this fragment.
     */
    public void addBehaviors() { }

    /**
     * Get the orientation to lay out this fragment.
     *
     * @return the orientation to lay out this fragment.
     */
    public int orientation() {
        return GridLayoutManager.VERTICAL;
    }

    /**
     * Whether or not to reverse the layout of this fragment.
     *
     * @return true if this fragment should be laid out in reverse
     */
    public boolean reverse() {
        return false;
    }
}
