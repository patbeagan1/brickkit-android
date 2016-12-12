package com.wayfair.brickkit;

import android.view.ViewGroup;

import java.util.HashMap;

/**
 * Registry which handles mapping templates to {@link GenerateViewHolderInterface} which are used to generate view holders.
 */
public final class ViewHolderRegistry {
    /**
     * Private constructor.
     */
    private ViewHolderRegistry() { }

    private static HashMap<String, GenerateViewHolderInterface> registry = new HashMap<>();

    /**
     * Register a given {@link GenerateViewHolderInterface} to the given template string.
     *
     * @param template template string to register the {@link GenerateViewHolderInterface} at
     * @param generateViewHolderInterface {@link GenerateViewHolderInterface} to register
     */
    public static void register(String template, GenerateViewHolderInterface generateViewHolderInterface) {
        registry.put(template, generateViewHolderInterface);
    }

    /**
     * Get a {@link BrickViewHolder} for the given template string and {@link ViewGroup}.
     *
     * @param template template string to use to lookip the {@link GenerateViewHolderInterface}
     * @param parent {@link ViewGroup} to build the {@link BrickViewHolder} from
     * @return the {@link BrickViewHolder} for the given template string and {@link ViewGroup}
     */
    public static BrickViewHolder mapToRecyclerView(String template, ViewGroup parent) {
        return registry.get(template).generateViewHolder(parent);
    }

    /**
     * Interface registered in this registry.
     */
    public interface GenerateViewHolderInterface {
        /**
         * Method to generate a {@link BrickViewHolder} from the given {@link ViewGroup}.
         *
         * @param parent {@link ViewGroup} to build the {@link BrickViewHolder} from
         * @return the resulting {@link BrickViewHolder}
         */
        BrickViewHolder generateViewHolder(ViewGroup parent);
    }
}
