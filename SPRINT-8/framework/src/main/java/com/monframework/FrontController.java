package com.monframework;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
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
        System.out.println("📋 Paramètres reçus: " + request.getParameterMap());
        
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
                
                Object[] methodArgs = prepareMethodArguments(method, request);
                Object result = method.invoke(controller, methodArgs);
                
                handleResult(result, request, response);
                
            } catch (Exception e) {
                System.out.println("❌ Erreur: " + e.getMessage());
                e.printStackTrace();
                handleError(e, request, response);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "URL non trouvée: /" + path);
        }
    }
    
    private Object[] prepareMethodArguments(Method method, HttpServletRequest request) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        
        System.out.println("🔧 Préparation de " + parameters.length + " argument(s) pour méthode: " + method.getName());
        
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            Class<?> paramType = param.getType();
            
            System.out.println("  📌 Paramètre " + i + ": " + param.getName() + " (type: " + paramType.getSimpleName() + ")");
            
            if (paramType.equals(Map.class)) {
                // 🔥 CRÉATION DU MAP AVEC TOUS LES PARAMÈTRES
                Map<String, Object> paramMap = createParameterMap(request);
                args[i] = paramMap;
                System.out.println("    ✅ Création d'un Map avec " + paramMap.size() + " entrées: " + paramMap);
                
            } else if (paramType.equals(String.class)) {
                // Pour les paramètres String simples
                String paramName = param.getName();
                String[] values = request.getParameterValues(paramName);
                
                if (values != null && values.length == 1) {
                    args[i] = values[0];
                    System.out.println("    ✅ String '" + paramName + "': '" + args[i] + "'");
                } else if (values != null && values.length > 1) {
                    // Si plusieurs valeurs, on prend la première ou on les concatène
                    args[i] = String.join(", ", values);
                    System.out.println("    ✅ String multiple '" + paramName + "': '" + args[i] + "'");
                } else {
                    args[i] = null;
                    System.out.println("    ⚠️ Paramètre '" + paramName + "' non trouvé");
                }
                
            } else if (paramType.equals(String[].class)) {
                // Pour les tableaux de String (comme les checkboxes)
                String paramName = param.getName();
                String[] values = request.getParameterValues(paramName);
                args[i] = values;
                System.out.println("    ✅ String[] '" + paramName + "': " + 
                                 (values != null ? java.util.Arrays.toString(values) : "null"));
                
            } else if (paramType.equals(int.class) || paramType.equals(Integer.class)) {
                String paramName = param.getName();
                String value = request.getParameter(paramName);
                args[i] = convertToInt(value);
                System.out.println("    ✅ int '" + paramName + "': " + args[i]);
                
            } else if (paramType.equals(double.class) || paramType.equals(Double.class)) {
                String paramName = param.getName();
                String value = request.getParameter(paramName);
                args[i] = convertToDouble(value);
                System.out.println("    ✅ double '" + paramName + "': " + args[i]);
                
            } else if (paramType.equals(boolean.class) || paramType.equals(Boolean.class)) {
                String paramName = param.getName();
                String value = request.getParameter(paramName);
                args[i] = convertToBoolean(value);
                System.out.println("    ✅ boolean '" + paramName + "': " + args[i]);
                
            } else {
                System.out.println("    ⚠️ Type non géré: " + paramType.getName());
                args[i] = null;
            }
        }
        
        return args;
    }
    
    // 🔥 MÉTHODE POUR CRÉER UN MAP AVEC TOUS LES PARAMÈTRES
    private Map<String, Object> createParameterMap(HttpServletRequest request) {
        Map<String, Object> paramMap = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        
        for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
            String paramName = entry.getKey();
            String[] values = entry.getValue();
            
            // 🔥 GESTION SPÉCIALE POUR LES CHECKBOXES ET PARAMÈTRES MULTIPLES
            if (values != null) {
                if (values.length == 1) {
                    // Un seul valeur -> String simple
                    paramMap.put(paramName, values[0]);
                } else if (values.length > 1) {
                    // Plusieurs valeurs -> Garder le tableau
                    // Ceci est important pour les checkboxes avec même nom
                    paramMap.put(paramName, values);
                } else {
                    paramMap.put(paramName, "");
                }
            } else {
                paramMap.put(paramName, "");
            }
        }
        
        return paramMap;
    }
    
    private Integer convertToInt(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.out.println("    ⚠️ Conversion int échouée pour: '" + value + "'");
            return 0;
        }
    }
    
    private Double convertToDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            System.out.println("    ⚠️ Conversion double échouée pour: '" + value + "'");
            return 0.0;
        }
    }
    
    private Boolean convertToBoolean(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        return Boolean.parseBoolean(value);
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