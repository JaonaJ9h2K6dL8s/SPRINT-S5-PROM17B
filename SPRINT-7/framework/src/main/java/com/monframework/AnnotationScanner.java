package com.monframework;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AnnotationScanner {
    private List<Class<?>> classes = new ArrayList<>();
    
    public void addClass(Class<?> clazz) {
        this.classes.add(clazz);
    }
    
    // RECHERCHE AVEC MÉTHODE HTTP
    public Mapping getMapping(String url, String httpMethod) {
        for (Class<?> clazz : classes) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Url.class)) {
                    Url urlAnnotation = method.getAnnotation(Url.class);
                    String methodUrl = urlAnnotation.value();
                    
                    if (methodUrl.equals(url)) {
                        String annotationMethod = urlAnnotation.method();
                        if (annotationMethod.equalsIgnoreCase(httpMethod)) {
                            System.out.println("✅ Mapping trouvé: " + url + " -> " + method.getName() + " (" + httpMethod + ")");
                            return new Mapping(clazz, method);
                        }
                    }
                }
            }
        }
        System.out.println("❌ Aucun mapping pour: " + url + " (" + httpMethod + ")");
        return null;
    }
    
    // Ancienne méthode pour compatibilité
    public Mapping getMapping(String url) {
        return getMapping(url, "GET");
    }
    
    public static class Mapping {
        public Class<?> className;
        public Method method;
        
        public Mapping(Class<?> className, Method method) {
            this.className = className;
            this.method = method;
        }
    }
}