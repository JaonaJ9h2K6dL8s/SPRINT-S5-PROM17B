package com.monframework;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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
        processRequest(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String path = getCleanPath(request);
        System.out.println("🔍 Requête reçue - Chemin: '" + path + "'");
        System.out.println("📋 Paramètres reçus: " + request.getParameterMap().keySet());
        
        if (path.isEmpty() || "formulaire".equals(path)) {
            request.getRequestDispatcher("formulaire.jsp").forward(request, response);
            return;
        }
        
        AnnotationScanner.Mapping mapping = scanner.getMapping(path);
        
        if (mapping != null) {
            try {
                System.out.println("🎯 Mapping trouvé: " + mapping.className.getSimpleName() + "." + mapping.method.getName());
                
                Object controller = mapping.className.getDeclaredConstructor().newInstance();
                Method method = mapping.method;
                
                // 🔥 CORRECTION : Validation STRICTE du mapping
                validateStrictParameterMapping(method, request, path);
                
                Object[] methodArgs = prepareMethodArguments(method, request);
                Object result = method.invoke(controller, methodArgs);
                
                handleResult(result, request, response);
                
            } catch (Exception e) {
                System.out.println("❌ Erreur: " + e.getMessage());
                handleError(e, request, response);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "URL non trouvée: /" + path);
        }
    }
    
    // 🔥 NOUVELLE MÉTHODE : VALIDATION STRICTE PAR NOM DE MÉTHODE
    private void validateStrictParameterMapping(Method method, HttpServletRequest request, String url) {
        String methodName = method.getName();
        Map<String, String[]> requestParams = request.getParameterMap();
        
        System.out.println("🔍 Validation stricte pour: " + methodName);
        
        // 🔥 TABLE DE CORRESPONDANCE MÉTHODE → PARAMÈTRE ATTENDU
        String expectedParam = getExpectedParameterForMethod(methodName);
        
        if (expectedParam != null) {
            String receivedValue = request.getParameter(expectedParam);
            System.out.println("📋 Paramètre attendu: '" + expectedParam + "' = '" + receivedValue + "'");
            
            // Vérifier si le bon paramètre est présent
            if (receivedValue == null) {
                throw new RuntimeException("❌ Mapping incorrect! La méthode '" + methodName + 
                                         "' attend le paramètre '" + expectedParam + 
                                         "' mais a reçu: " + requestParams.keySet());
            }
            
            // Vérifier s'il y a des paramètres supplémentaires non attendus
            if (requestParams.size() > 1 || !requestParams.containsKey(expectedParam)) {
                System.out.println("⚠️ Paramètres supplémentaires détectés: " + requestParams.keySet());
                // Pour être strict, décommente la ligne suivante:
                // throw new RuntimeException("Paramètres non autorisés: " + requestParams.keySet() + ". Attendu: " + expectedParam);
            }
        } else {
            System.out.println("⚠️ Aucune règle de mapping pour: " + methodName);
        }
    }
    
    // 🔥 TABLE DE MAPPING : Méthode → Paramètre attendu
    private String getExpectedParameterForMethod(String methodName) {
        switch (methodName) {
            case "inscrire": return "nom";
            case "voirProfil": return "userId"; 
            case "changerStatut": return "actif";
            // Ajoute tes méthodes ici
            default: return null;
        }
    }
    
    private Object[] prepareMethodArguments(Method method, HttpServletRequest request) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        
        System.out.println("🔧 Préparation de " + parameters.length + " argument(s)");
        
        if (parameters.length == 1) {
            String methodName = method.getName();
            String expectedParam = getExpectedParameterForMethod(methodName);
            
            if (expectedParam != null) {
                String paramValue = request.getParameter(expectedParam);
                System.out.println("📋 Utilisation du paramètre: '" + expectedParam + "' = '" + paramValue + "'");
                
                args[0] = convertParameterValue(paramValue, parameters[0].getType());
                System.out.println("✅ Converti en: " + args[0] + " (type: " + parameters[0].getType().getSimpleName() + ")");
            }
        }
        
        return args;
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