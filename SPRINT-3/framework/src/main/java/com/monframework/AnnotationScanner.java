package com.monframework;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationScanner {
    private Map<String, Mapping> urlMappings = new HashMap<>();
    
    public static class Mapping {
        public Class<?> className;
        public Method method;
        
        public Mapping(Class<?> className, Method method) {
            this.className = className;
            this.method = method;
        }
    }
    
    public void addClass(Class<?> clazz) throws Exception {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Url.class)) {
                Url urlAnnotation = method.getAnnotation(Url.class);
                String url = urlAnnotation.value();
                urlMappings.put(url, new Mapping(clazz, method));
                System.out.println("🌐 URL mappée: " + url + " → " + clazz.getSimpleName() + "." + method.getName());
            }
        }
    }
    
    public Mapping getMapping(String url) {
        return urlMappings.get(url);
    }
}