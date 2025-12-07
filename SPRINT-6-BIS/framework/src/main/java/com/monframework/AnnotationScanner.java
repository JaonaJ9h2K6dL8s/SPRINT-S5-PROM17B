package com.monframework;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
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
        
        @Override
        public String toString() {
            return className.getSimpleName() + "." + method.getName();
        }
    }
    
    // SCAN AUTOMATIQUE D'UN PACKAGE
    public void scanPackage(String packageName) throws Exception {
        System.out.println("📦 Début du scan du package: " + packageName);
        
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        URL resource = classLoader.getResource(path);
        
        if (resource == null) {
            throw new RuntimeException("❌ Package non trouvé: " + packageName);
        }
        
        File directory = new File(resource.getFile());
        if (!directory.exists() || !directory.isDirectory()) {
            throw new RuntimeException("❌ Dossier non trouvé: " + directory.getAbsolutePath());
        }
        
        System.out.println("📁 Scan du dossier: " + directory.getAbsolutePath());
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".class"));
        
        if (files == null || files.length == 0) {
            System.out.println("⚠️ Aucun fichier .class trouvé dans " + directory.getAbsolutePath());
            return;
        }
        
        for (File file : files) {
            String className = packageName + '.' + file.getName().replace(".class", "");
            System.out.println("🔍 Tentative de chargement: " + className);
            
            try {
                Class<?> clazz = Class.forName(className);
                addClass(clazz);
            } catch (ClassNotFoundException e) {
                System.out.println("❌ Classe non trouvée: " + className);
            } catch (Exception e) {
                System.out.println("❌ Erreur avec " + className + ": " + e.getMessage());
            }
        }
        
        System.out.println("✅ Scan du package " + packageName + " terminé");
    }
    
    // AJOUT D'UNE CLASSE AU SCAN
    public void addClass(Class<?> clazz) {
        System.out.println("🔍 Analyse des méthodes de: " + clazz.getSimpleName());
        
        int mappingCount = 0;
        for (Method method : clazz.getDeclaredMethods()) {
            Url urlAnnotation = method.getAnnotation(Url.class);
            if (urlAnnotation != null) {
                String url = urlAnnotation.value();
                urlMappings.put(url, new Mapping(clazz, method));
                mappingCount++;
                System.out.println("   ✅ " + url + " → " + method.getName());
            }
        }
        
        if (mappingCount == 0) {
            System.out.println("   ⚠️ Aucune annotation @Url trouvée dans " + clazz.getSimpleName());
        } else {
            System.out.println("   📍 " + mappingCount + " mapping(s) trouvé(s)");
        }
    }
    
    // RÉCUPÉRATION D'UN MAPPING
    public Mapping getMapping(String url) {
        return urlMappings.get(url);
    }
    
    // RÉCUPÉRATION DE TOUS LES MAPPINGS
    public Map<String, Mapping> getAllMappings() {
        return new HashMap<>(urlMappings);
    }
}