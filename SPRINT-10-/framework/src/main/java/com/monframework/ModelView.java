package com.monframework;

import java.util.HashMap;
import java.util.Map;


public class ModelView {
    private String view;
    private Map<String, Object> data;

    public ModelView() {
        this.data = new HashMap<>();
    }

    public ModelView(String view) {
        this();
        this.view = view;
    }

    public void addObject(String key, Object value) {
        this.data.put(key, value);
    }

    public Map<String, Object> getData() {
        return data;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public Object getData(String key) {
        return data.get(key);
    }
}