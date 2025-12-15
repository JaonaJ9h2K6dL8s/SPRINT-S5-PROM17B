package com.monframework;

import java.util.HashMap;
import java.util.Map;

public class ModelView {
    private String view;
    private Map<String, Object> data = new HashMap<>();

    public ModelView() {}

    public void setView(String view) {
        this.view = view;
    }

    public String getView() {
        return view;
    }

    public void setData(String key, Object value) {
        this.data.put(key, value);
    }

    public Map<String, Object> getData() {
        return data;
    }
}