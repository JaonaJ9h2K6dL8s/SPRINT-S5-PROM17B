package com.monframework;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Set;
import java.util.Enumeration;
import java.util.Iterator;

public class SessionMap extends HashMap<String, Object> {
    private HttpSession httpSession;
    
    public SessionMap(HttpSession session) {
        this.httpSession = session;
        // Charger toutes les variables de session existantes
        loadSessionAttributes();
    }
    
    private void loadSessionAttributes() {
        if (httpSession != null) {
            Enumeration<String> attributeNames = httpSession.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String key = attributeNames.nextElement();
                Object value = httpSession.getAttribute(key);
                super.put(key, value);
            }
        }
    }
    
    @Override
    public Object put(String key, Object value) {
        if (httpSession != null) {
            httpSession.setAttribute(key, value);
        }
        return super.put(key, value);
    }
    
    @Override
    public Object get(Object key) {
        if (httpSession != null) {
            return httpSession.getAttribute((String) key);
        }
        return super.get(key);
    }
    
    @Override
    public Object remove(Object key) {
        if (httpSession != null) {
            httpSession.removeAttribute((String) key);
        }
        return super.remove(key);
    }
    
    @Override
    public void clear() {
        if (httpSession != null) {
            // Invalider la session
            httpSession.invalidate();
        }
        super.clear();
    }
    
    public void invalidate() {
        if (httpSession != null) {
            httpSession.invalidate();
        }
        super.clear();
    }
    
    @Override
    public Set<String> keySet() {
        // Recharger pour avoir les données à jour
        super.clear();
        loadSessionAttributes();
        return super.keySet();
    }
    
    @Override
    public boolean containsKey(Object key) {
        if (httpSession != null) {
            return httpSession.getAttribute((String) key) != null;
        }
        return super.containsKey(key);
    }
    
    @Override
    public int size() {
        return keySet().size();
    }
    
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }
}