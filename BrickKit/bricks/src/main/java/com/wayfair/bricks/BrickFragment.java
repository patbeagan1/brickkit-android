package com.wayfair.bricks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public abstract class BrickFragment extends Fragment {
    public BrickRecyclerAdapter brickRecyclerAdapter;


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
            BrickDataManager dataManager = new BrickDataManager(maxSpans());

            int defaultPadding = (int) getContext().getResources().getDimension(R.dimen.default_brick_inset_padding);
            recyclerView.setPadding(defaultPadding, defaultPadding, defaultPadding, defaultPadding);

            recyclerView.setLayoutManager(
                    new BrickGridLayoutManager(
                            getContext(),
                            maxSpans(),
                            dataManager,
                            orientation(),
                            reverse()
                    )
            );
            brickRecyclerAdapter = new BrickRecyclerAdapter(dataManager, recyclerView);
            recyclerView.setAdapter(brickRecyclerAdapter);
            recyclerView.addItemDecoration(new BrickRecyclerItemDecoration(dataManager));

            dataManager.behaviours.addAll(addBehaviours(brickRecyclerAdapter, recyclerView));

            createBricks();
        }
    }

    public abstract int maxSpans();
    public abstract void createBricks();
    public abstract ArrayList<BrickBehaviour> addBehaviours(BrickRecyclerAdapter brickRecyclerAdapter, RecyclerView recyclerView);
    public abstract int orientation();
    public abstract boolean reverse();
}
