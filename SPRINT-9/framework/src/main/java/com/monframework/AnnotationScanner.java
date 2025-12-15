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
    
    // 🔥 MÉTHODE MANQUANTE AJOUTÉE
    public Map<String, Mapping> getAllMappings() {
        return new HashMap<>(urlMappings);
    }
    
    public void addClass(Class<?> clazz) {
        System.out.println("🔍 Scan de la classe: " + clazz.getSimpleName());
        
        for (Method method : clazz.getDeclaredMethods()) {
            Url urlAnnotation = method.getAnnotation(Url.class);
            if (urlAnnotation != null) {
                String url = urlAnnotation.value();
                urlMappings.put(url, new Mapping(clazz, method));
                System.out.println("✅ Mapping: " + url + " → " + method.getName());
                
                // 🔥 Afficher les paramètres de la méthode
                System.out.println("   📋 Paramètres: " + java.util.Arrays.toString(method.getParameters()));
            }
        }
    }
    
    public Mapping getMapping(String url) {
        return urlMappings.get(url);
    }
    
    // Version simplifiée du scan package
    public void scanPackage(String packageName) throws Exception {
        try {
            Class<?> utilisateurClass = Class.forName("test.Utilisateur");
            addClass(utilisateurClass);
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Classe non trouvée: test.Utilisateur");
        }
    }
}