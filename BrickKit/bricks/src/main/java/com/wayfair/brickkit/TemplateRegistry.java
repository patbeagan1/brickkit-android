package com.wayfair.brickkit;

import java.util.HashMap;

/**
 * Registry that keeps track of relationship between template name and viewType index.
 */
public final class TemplateRegistry {
    private static TemplateRegistry instance;
    private HashMap<String, Integer> templateToViewType;
    private HashMap<Integer, String> viewTypeToTemplate;

    /**
     * Constructor.
     */
    private TemplateRegistry() {
        templateToViewType = new HashMap<>();
        viewTypeToTemplate = new HashMap<>();
    }

    /**
     * Testing method to clear the TemplateRegistry.
     */
    void reset() {
        templateToViewType.clear();
        viewTypeToTemplate.clear();
    }

    /**
     * Method to get the singleton instance of the TemplateRegistry.
     * @return singleton instance of the TemplateRegistry
     */
    public static TemplateRegistry getInstance() {
        if (instance == null) {
            instance = new TemplateRegistry();
        }

        return instance;
    }

    /**
     * Helper method to insert new template / viewType index pairs.
     *
     * @param template template name to insert
     * @param viewType viewType index to insert.
     */
    private void put(String template, int viewType) {
        templateToViewType.put(template, viewType);
        viewTypeToTemplate.put(viewType, template);
    }

    /**
     * Get template name based off of the viewType index.
     *
     * @param viewType viewType index to look up
     * @return Template name
     */
    public String get(int viewType) {
        return viewTypeToTemplate.get(viewType);
    }

    /**
     * Get viewType index based off of the template name. A new index is generated if
     * this is a new template name.
     *
     * @param template template name to look up
     * @return viewType index for this template name
     */
    public int get(String template) {
        Integer viewType = templateToViewType.get(template);

        if (viewType == null) {
            put(template, templateToViewType.size());
        }

        return templateToViewType.get(template);
    }
}
