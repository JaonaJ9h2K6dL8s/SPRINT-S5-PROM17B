package com.monframework;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontController extends HttpServlet {
    private AnnotationScanner scanner = new AnnotationScanner();
    
    @Override
    public void init() throws ServletException {
        System.out.println("🚀 Initialisation du FrontController...");
        try {
            Class<?> utilisateurClass = Class.forName("test.Utilisateur");
            scanner.addClass(utilisateurClass);
            System.out.println("✅ Classe test.Utilisateur chargée");
            
        } catch (Exception e) {
            System.out.println("❌ ERREUR: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response, "GET");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response, "POST");
    }
    
    private void processRequest(HttpServletRequest request, HttpServletResponse response, String httpMethod)
            throws ServletException, IOException {
        
        String path = getCleanPath(request);
        
        // DEBUG DÉTAILLÉ
        System.out.println("=== DEBUG ===");
        System.out.println("🔍 Requête " + httpMethod + " reçue - Chemin: '" + path + "'");
        System.out.println("📋 Tous les paramètres reçus:");
        request.getParameterMap().forEach((key, value) -> {
            System.out.println("   " + key + " = " + Arrays.toString(value));
        });
        System.out.println("=============");
        
        if (path.isEmpty() || "formulaire".equals(path)) {
            request.getRequestDispatcher("formulaire.jsp").forward(request, response);
            return;
        }
        
        AnnotationScanner.Mapping mapping = scanner.getMapping(path, httpMethod);
        
        if (mapping != null) {
            try {
                System.out.println("🎯 Mapping trouvé: " + mapping.className.getSimpleName() + "." + mapping.method.getName() + " (HTTP: " + httpMethod + ")");
                
                Object controller = mapping.className.getDeclaredConstructor().newInstance();
                Method method = mapping.method;
                
                Object[] methodArgs = prepareMethodArguments(method, request);
                Object result = method.invoke(controller, methodArgs);
                
                handleResult(result, request, response);
                
            } catch (Exception e) {
                System.out.println("❌ Erreur: " + e.getMessage());
                handleError(e, request, response);
            }
        } else {
            System.out.println("❌ Aucun mapping trouvé pour: " + path + " avec méthode: " + httpMethod);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "URL non trouvée: /" + path + " (méthode: " + httpMethod + ")");
        }
    }
    
    // 🔥 CORRECTION : Méthode corrigée pour gérer les noms de paramètres
    private Object[] prepareMethodArguments(Method method, HttpServletRequest request) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        
        System.out.println("🔧 Préparation de " + parameters.length + " argument(s) pour " + method.getName());
        
        // 🔥 TABLE DE MAPPING : Définit les noms de paramètres pour chaque méthode
        String[] expectedParamNames = getExpectedParameterNamesForMethod(method.getName());
        
        for (int i = 0; i < parameters.length; i++) {
            String paramName;
            if (expectedParamNames != null && i < expectedParamNames.length) {
                paramName = expectedParamNames[i]; // Utiliser le nom connu depuis la table
            } else {
                paramName = parameters[i].getName(); // Fallback (peut donner arg0, arg1...)
                System.out.println("⚠️  Utilisation du nom générique: " + paramName);
            }
            
            String paramValue = request.getParameter(paramName);
            System.out.println("📋 Paramètre " + i + " '" + paramName + "' = '" + paramValue + "'");
            
            args[i] = convertParameterValue(paramValue, parameters[i].getType());
            System.out.println("✅ Converti en: " + args[i] + " (type: " + parameters[i].getType().getSimpleName() + ")");
        }
        
        return args;
    }
    
    // 🔥 NOUVELLE MÉTHODE : Table de correspondance méthode → noms de paramètres
    private String[] getExpectedParameterNamesForMethod(String methodName) {
        switch (methodName) {
            case "getGetRequet":
                return new String[]{"paramGet", "nomGet", "emailGet"};
            case "postGetRequet":
                return new String[]{"nom", "email"};
            case "inscrire":
                return new String[]{"nom"};
            // Ajoutez vos autres méthodes ici
            default:
                System.out.println("⚠️  Aucune table de paramètres définie pour: " + methodName);
                return null;
        }
    }
    
    private Object convertParameterValue(String value, Class<?> targetType) {
        if (value == null) return null;
        
        try {
            if (targetType == String.class) return value;
            if (targetType == int.class || targetType == Integer.class) return Integer.parseInt(value);
            if (targetType == long.class || targetType == Long.class) return Long.parseLong(value);
            if (targetType == boolean.class || targetType == Boolean.class) return Boolean.parseBoolean(value);
            if (targetType == double.class || targetType == Double.class) return Double.parseDouble(value);
            
            return value;
        } catch (NumberFormatException e) {
            throw new RuntimeException("Conversion impossible: '" + value + "' en " + targetType.getSimpleName());
        }
    }
    
    private void handleResult(Object result, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if (result instanceof ModelView) {
            ModelView modelView = (ModelView) result;
            
            for (Map.Entry<String, Object> entry : modelView.getData().entrySet()) {
                request.setAttribute(entry.getKey(), entry.getValue());
            }
            
            request.getRequestDispatcher(modelView.getView()).forward(request, response);
            
        } else {
            request.setAttribute("message", result != null ? result.toString() : "Méthode exécutée");
            request.getRequestDispatcher("result.jsp").forward(request, response);
        }
    }
    
    private void handleError(Exception e, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("❌ Erreur: " + e.getMessage());
        e.printStackTrace();
        
        request.setAttribute("erreur", e.getMessage());
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }
    
    private String getCleanPath(HttpServletRequest request) {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        
        if (contextPath != null && path.startsWith(contextPath)) {
            path = path.substring(contextPath.length());
        }
        
        return path.startsWith("/") ? path.substring(1) : path;
    }
}