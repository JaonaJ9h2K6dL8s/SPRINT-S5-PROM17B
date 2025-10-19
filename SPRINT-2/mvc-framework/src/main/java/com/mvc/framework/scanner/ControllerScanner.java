package com.mvc.framework.scanner;

import com.mvc.framework.annotation.Controller;
import com.mvc.framework.annotation.Route;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * Scanner pour détecter automatiquement les contrôleurs et leurs routes.
 */
public class ControllerScanner {
    
    private Map<String, RouteInfo> routes = new HashMap<>();
    
    /**
     * Classe pour stocker les informations d'une route
     */
    public static class RouteInfo {
        private Object controllerInstance;
        private Method method;
        private String url;
        private String httpMethod;
        
        public RouteInfo(Object controllerInstance, Method method, String url, String httpMethod) {
            this.controllerInstance = controllerInstance;
            this.method = method;
            this.url = url;
            this.httpMethod = httpMethod;
        }
        
        // Getters
        public Object getControllerInstance() { return controllerInstance; }
        public Method getMethod() { return method; }
        public String getUrl() { return url; }
        public String getHttpMethod() { return httpMethod; }
    }
    
    /**
     * Scanne un package pour trouver tous les contrôleurs
     */
    public void scanPackage(String packageName) {
        try {
            String path = packageName.replace('.', '/');
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL resource = classLoader.getResource(path);
            
            if (resource != null) {
                File directory = new File(resource.getFile());
                if (directory.exists()) {
                    scanDirectory(directory, packageName);
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du scan du package " + packageName + ": " + e.getMessage());
        }
    }
    
    /**
     * Scanne récursivement un répertoire pour trouver les classes
     */
    private void scanDirectory(File directory, String packageName) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    scanDirectory(file, packageName + "." + file.getName());
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                    processClass(className);
                }
            }
        }
    }
    
    /**
     * Traite une classe pour vérifier si c'est un contrôleur
     */
    private void processClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            
            // Vérifier si la classe est annotée avec @Controller
            if (clazz.isAnnotationPresent(Controller.class)) {
                Object controllerInstance = clazz.getDeclaredConstructor().newInstance();
                scanControllerMethods(controllerInstance, clazz);
            }
        } catch (Exception e) {
            // Ignorer les erreurs de chargement de classe
            System.err.println("Impossible de charger la classe " + className + ": " + e.getMessage());
        }
    }
    
    /**
     * Scanne les méthodes d'un contrôleur pour trouver les routes
     */
    private void scanControllerMethods(Object controllerInstance, Class<?> controllerClass) {
        Method[] methods = controllerClass.getDeclaredMethods();
        
        for (Method method : methods) {
            if (method.isAnnotationPresent(Route.class)) {
                Route routeAnnotation = method.getAnnotation(Route.class);
                String url = routeAnnotation.url();
                String httpMethod = routeAnnotation.method();
                
                // Normaliser l'URL (s'assurer qu'elle commence par /)
                if (!url.startsWith("/")) {
                    url = "/" + url;
                }
                
                RouteInfo routeInfo = new RouteInfo(controllerInstance, method, url, httpMethod);
                routes.put(url, routeInfo);
                
                System.out.println("Route enregistrée: " + url + " -> " + 
                                 controllerClass.getSimpleName() + "." + method.getName());
            }
        }
    }
    
    /**
     * Trouve une route correspondant à l'URL donnée
     */
    public RouteInfo findRoute(String url) {
        // Normaliser l'URL
        if (!url.startsWith("/")) {
            url = "/" + url;
        }
        
        return routes.get(url);
    }
    
    /**
     * Retourne toutes les routes enregistrées
     */
    public Map<String, RouteInfo> getAllRoutes() {
        return new HashMap<>(routes);
    }
    
    /**
     * Affiche toutes les routes enregistrées
     */
    public void printRoutes() {
        System.out.println("=== Routes enregistrées ===");
        for (Map.Entry<String, RouteInfo> entry : routes.entrySet()) {
            RouteInfo route = entry.getValue();
            System.out.println(entry.getKey() + " -> " + 
                             route.getControllerInstance().getClass().getSimpleName() + 
                             "." + route.getMethod().getName());
        }
        System.out.println("=========================");
    }
}