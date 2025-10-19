package jframework.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jframework.annotation.Controller;

/**
 * Utilitaire pour scanner et découvrir automatiquement les contrôleurs
 * Basé sur l'annotation @Controller
 */
public class ControllerScanner {
    
    /**
     * Trouve toutes les classes annotées avec @Controller dans un package
     * @param packageName Le nom du package à scanner
     * @return Liste des classes trouvées
     */
    public static List<Class<?>> findControllerClasses(String packageName) {
        try {
            return findClassesWithAnnotation(packageName, Controller.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Trouve toutes les classes annotées avec une annotation spécifique dans un package
     * @param packageName Le nom du package à scanner
     * @param annotationClass La classe d'annotation à rechercher
     * @return Liste des classes trouvées
     */
    public static List<Class<?>> findClassesWithAnnotation(String packageName, Class<Controller> annotationClass) throws Exception {
        List<Class<?>> result = new ArrayList<>();
        String path = packageName.replace('.', '/');
        URL packageURL = Thread.currentThread().getContextClassLoader().getResource(path);
        if (packageURL == null) return result;
        
        File folder = new File(packageURL.toURI());
        File[] files = folder.listFiles();
        if (files == null) return result;

        for (File file : files) {
            if (file.isDirectory()) {
                result.addAll(findClassesWithAnnotation(packageName + "." + file.getName(), annotationClass));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().replace(".class", "");
                try {
                    Class<?> cls = Class.forName(className);
                    if (cls.isAnnotationPresent(annotationClass)) {
                        result.add(cls);
                    }
                } catch (ClassNotFoundException e) {
                    // Ignorer les classes qui ne peuvent pas être chargées
                }
            }
        }
        return result;
    }
    
    /**
     * Affiche toutes les classes contrôleurs trouvées (pour debug)
     * @param packageName Le package à scanner
     */
    public static void printControllerClasses(String packageName) {
        try {
            List<Class<?>> classes = findControllerClasses(packageName);
            System.out.println("Classes annotées avec @Controller dans " + packageName + " :");
            for (Class<?> cls : classes) {
                System.out.println("  - " + cls.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}