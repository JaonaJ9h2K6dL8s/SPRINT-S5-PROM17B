package com.monframework;

import java.util.HashMap;
import java.util.Map;

public class ModelView {
    private String view;
    private Map<String, Object> data = new HashMap<>();
    private Map<String, Object> session = new HashMap<>();
    
    public ModelView(String view) {
        this.view = view;
    }
    
    public String getView() {
        return view;
    }
    
    public void addItem(String key, Object value) {
        data.put(key, value);
    }
    
    public Map<String, Object> getData() {
        return data;
    }
    
    public void addSession(String key, Object value) {
        session.put(key, value);
    }
    
    public Map<String, Object> getSession() {
        return session;
    }
}