package com.wayfair.brickkit;

import java.util.HashMap;

public class TemplateRegistry {
    private static TemplateRegistry instance;
    private HashMap<String, Integer> templateToViewType;
    private HashMap<Integer, String> viewTypeToTemplate;

    public TemplateRegistry() {
        templateToViewType = new HashMap<>();
        viewTypeToTemplate = new HashMap<>();
    }

    public static TemplateRegistry getInstance() {
        if (instance == null) {
            instance = new TemplateRegistry();
        }

        return instance;
    }

    public void put(String template, Integer viewType) {
        templateToViewType.put(template, viewType);
        viewTypeToTemplate.put(viewType, template);
    }

    public String get(Integer viewType) {
        return viewTypeToTemplate.get(viewType);
    }

    public Integer get(String template) {
        Integer viewType = templateToViewType.get(template);

        if (viewType == null) {
            put(template, templateToViewType.size());
        }

        return templateToViewType.get(template);
    }
}
