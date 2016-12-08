package com.wayfair.brickkit;

import java.util.HashMap;

public final class TemplateRegistry {
    private static TemplateRegistry instance;
    private HashMap<String, Integer> templateToViewType;
    private HashMap<Integer, String> viewTypeToTemplate;

    private TemplateRegistry() {
        templateToViewType = new HashMap<>();
        viewTypeToTemplate = new HashMap<>();
    }

    public static TemplateRegistry getInstance() {
        if (instance == null) {
            instance = new TemplateRegistry();
        }

        return instance;
    }

    private void put(String template, int viewType) {
        templateToViewType.put(template, viewType);
        viewTypeToTemplate.put(viewType, template);
    }

    public String get(int viewType) {
        return viewTypeToTemplate.get(viewType);
    }

    public int get(String template) {
        Integer viewType = templateToViewType.get(template);

        if (viewType == null) {
            put(template, templateToViewType.size());
        }

        return templateToViewType.get(template);
    }
}
