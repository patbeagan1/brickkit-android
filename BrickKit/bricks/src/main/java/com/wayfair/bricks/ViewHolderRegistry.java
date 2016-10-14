package com.wayfair.bricks;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.HashMap;

public class ViewHolderRegistry {
    private static HashMap<String, GenerateViewHolderInterface> registry = new HashMap<>();

    public static void register(String template, GenerateViewHolderInterface generateViewHolderInterface) {
        registry.put(template, generateViewHolderInterface);
    }

    public static BrickViewHolder mapToRecyclerView(String template, ViewGroup parent) {
        return registry.get(template).generateViewHolder(parent);
    }

    public interface GenerateViewHolderInterface {
        BrickViewHolder generateViewHolder(ViewGroup parent);
    }
}
