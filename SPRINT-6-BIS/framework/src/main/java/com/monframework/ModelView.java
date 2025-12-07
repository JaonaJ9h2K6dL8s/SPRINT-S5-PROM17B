package com.monframework;

import java.util.HashMap;
import java.util.Map;

public class ModelView {
    private String view;
    private Map<String, Object> data;
    
    public ModelView(String view) {
        this.view = view;
        this.data = new HashMap<>();
    }
    
    // GETTERS
    public String getView() {
        return view;
    }
    
    public Map<String, Object> getData() {
        return data;
    }
    
    // 🔥 AJOUTE CETTE MÉTHODE MANQUANTE
    public void addObject(String key, Object value) {
        this.data.put(key, value);
    }
    
    // 🔥 METHODE ALTERNATIVE POUR LES CHAÎNES
    public void addObject(String key, String value) {
        this.data.put(key, value);
    }
    
    // Autres méthodes utiles
    public Object getObject(String key) {
        return this.data.get(key);
    }
    
    public void removeObject(String key) {
        this.data.remove(key);
    }
    
    public boolean containsKey(String key) {
        return this.data.containsKey(key);
    }
}