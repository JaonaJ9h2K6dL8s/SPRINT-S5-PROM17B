package com.monframework;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontController extends HttpServlet {
    private AnnotationScanner scanner = new AnnotationScanner();
    
    @Override
    public void init() throws ServletException {
        System.out.println("🚀 Initialisation du FrontController...");
        System.out.println("==========================================");
        
        try {
            // ESSAIE LE SCAN AUTOMATIQUE DU PACKAGE
            try {
                scanner.scanPackage("test");
                System.out.println("✅ Scan automatique du package 'test' réussi");
            } catch (Exception e) {
                System.out.println("⚠️ Scan automatique échoué: " + e.getMessage());
                System.out.println("🔄 Tentative de chargement manuel...");
                
                // FALLBACK: Chargement manuel
                try {
                    Class<?> utilisateurClass = Class.forName("test.Utilisateur");
                    scanner.addClass(utilisateurClass);
                    System.out.println("✅ Chargement manuel de test.Utilisateur réussi");
                } catch (ClassNotFoundException cnfe) {
                    System.out.println("❌ Classe test.Utilisateur non trouvée");
                    System.out.println("💡 Vérifie le package et le nom de la classe");
                }
            }
            
            // AFFICHAGE DES MAPPINGS TROUVÉS
            Map<String, AnnotationScanner.Mapping> allMappings = scanner.getAllMappings();
            System.out.println("==========================================");
            System.out.println("🗺️  MAPPINGS DISPONIBLES:");
            if (allMappings.isEmpty()) {
                System.out.println("   ❌ AUCUN MAPPING TROUVÉ");
                System.out.println("   💡 Vérifie les annotations @Url dans tes classes");
            } else {
                for (String url : allMappings.keySet()) {
                    AnnotationScanner.Mapping mapping = allMappings.get(url);
                    System.out.println("   ✅ " + url + " → " + 
                                     mapping.className.getSimpleName() + "." + mapping.method.getName());
                }
            }
            System.out.println("==========================================");
            
        } catch (Exception e) {
            System.out.println("❌ ERREUR CRITIQUE lors de l'initialisation: " + e.getMessage());
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
        System.out.println("\n🔍 NOUVELLE REQUÊTE ==================================");
        System.out.println("📨 Chemin: '" + path + "'");
        System.out.println("🌐 URL: " + request.getRequestURL());
        System.out.println("🔧 Méthode: " + request.getMethod());
        System.out.println("📋 Paramètres: " + request.getParameterMap().keySet());
        
        // AFFICHER LES MAPPINGS DISPONIBLES POUR DÉBOGAGE
        Map<String, AnnotationScanner.Mapping> availableMappings = scanner.getAllMappings();
        System.out.println("🔎 Mappings disponibles: " + availableMappings.keySet());
        
        // GESTION DES RESSOURCES STATIQUES ET PAGES
        if (isStaticResource(path)) {
            System.out.println("📁 Ressource statique ignorée: " + path);
            return;
        }
        
        // PAGE D'ACCUEIL AUTOMATIQUE
        if (path.isEmpty() || "formulaire".equals(path)) {
            System.out.println("📄 Retour vers la page formulaire.jsp");
            request.getRequestDispatcher("/formulaire.jsp").forward(request, response);
            return;
        }
        
        // RECHERCHE DU MAPPING
        AnnotationScanner.Mapping mapping = scanner.getMapping(path);
        
        if (mapping != null) {
            System.out.println("🎯 MAPPING TROUVÉ: " + mapping.className.getSimpleName() + "." + mapping.method.getName());
            
            try {
                // VALIDATION DE LA MÉTHODE (1 SEUL PARAMÈTRE)
                validateSingleParameterMethod(mapping.method, request);
                
                // INSTANCIATION DU CONTRÔLEUR
                Object controller = mapping.className.getDeclaredConstructor().newInstance();
                Method method = mapping.method;
                
                // PRÉPARATION DE L'ARGUMENT
                Object methodArg = prepareSingleArgument(method, request);
                System.out.println("✅ Argument préparé: " + methodArg + " (type: " + methodArg.getClass().getSimpleName() + ")");
                
                // EXÉCUTION DE LA MÉTHODE
                Object result = method.invoke(controller, methodArg);
                System.out.println("✅ Méthode exécutée avec succès");
                
                // GESTION DU RÉSULTAT
                handleResult(result, request, response);
                
            } catch (Exception e) {
                System.out.println("❌ Erreur lors de l'exécution: " + e.getMessage());
                handleError(e, request, response);
            }
        } else {
            System.out.println("❌ AUCUN MAPPING TROUVÉ pour: '" + path + "'");
            System.out.println("💡 Mappings disponibles: " + availableMappings.keySet());
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "URL non trouvée: /" + path);
        }
        
        System.out.println("====================================================\n");
    }
    
    // 🔥 VALIDATION POUR MÉTHODE À 1 ARGUMENT
    private void validateSingleParameterMethod(Method method, HttpServletRequest request) {
        Parameter[] parameters = method.getParameters();
        
        System.out.println("🔧 Validation de la méthode " + method.getName());
        
        // VÉRIFICATION NOMBRE DE PARAMÈTRES
        if (parameters.length != 1) {
            throw new RuntimeException("La méthode " + method.getName() + 
                                     " doit avoir exactement 1 paramètre. Trouvé: " + parameters.length);
        }
        
        // VÉRIFICATION ANNOTATION @RequestParam
        Parameter param = parameters[0];
        RequestParam requestParam = param.getAnnotation(RequestParam.class);
        
        if (requestParam == null) {
            throw new RuntimeException("Le paramètre de " + method.getName() + 
                                     " doit être annoté avec @RequestParam");
        }
        
        String expectedParamName = requestParam.value();
        String receivedValue = request.getParameter(expectedParamName);
        boolean isRequired = requestParam.required();
        
        System.out.println("📋 Paramètre attendu: '" + expectedParamName + "'");
        System.out.println("📦 Valeur reçue: '" + receivedValue + "'");
        System.out.println("🔐 Requis: " + isRequired);
        
        // VALIDATION REQUIRED
        if ((receivedValue == null || receivedValue.trim().isEmpty()) && isRequired) {
            throw new RuntimeException("Paramètre requis manquant: '" + expectedParamName + "'");
        }
        
        // VALIDATION PARAMÈTRES SUPPLÉMENTAIRES (MODE STRICT)
        validateNoExtraParameters(request, expectedParamName);
    }
    
    // 🔥 VALIDATION DES PARAMÈTRES SUPPLÉMENTAIRES
    private void validateNoExtraParameters(HttpServletRequest request, String allowedParam) {
        Map<String, String[]> allParams = request.getParameterMap();
        
        for (String paramName : allParams.keySet()) {
            if (!paramName.equals(allowedParam)) {
                System.out.println("⚠️ Paramètre supplémentaire ignoré: '" + paramName + "'");
                // DÉCOMMENTER POUR MODE STRICT:
                // throw new RuntimeException("Paramètre non autorisé: '" + paramName + "'. Attendu uniquement: '" + allowedParam + "'");
            }
        }
    }
    
    // 🔥 PRÉPARATION D'UN SEUL ARGUMENT
    private Object prepareSingleArgument(Method method, HttpServletRequest request) {
        Parameter param = method.getParameters()[0];
        RequestParam requestParam = param.getAnnotation(RequestParam.class);
        String paramName = requestParam.value();
        String paramValue = request.getParameter(paramName);
        Class<?> targetType = param.getType();
        
        System.out.println("🔄 Conversion: '" + paramValue + "' → " + targetType.getSimpleName());
        
        return convertParameterValue(paramValue, targetType);
    }
    
    // 🔥 CONVERSION DE TYPE
    private Object convertParameterValue(String value, Class<?> targetType) {
        if (value == null) return null;
        
        try {
            if (targetType == String.class) {
                return value;
            } else if (targetType == int.class || targetType == Integer.class) {
                return Integer.parseInt(value);
            } else if (targetType == long.class || targetType == Long.class) {
                return Long.parseLong(value);
            } else if (targetType == boolean.class || targetType == Boolean.class) {
                return Boolean.parseBoolean(value);
            } else if (targetType == double.class || targetType == Double.class) {
                return Double.parseDouble(value);
            } else {
                throw new RuntimeException("Type non supporté: " + targetType.getSimpleName());
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("Conversion impossible de '" + value + "' en " + targetType.getSimpleName());
        }
    }
    
    // 🔥 GESTION DU RÉSULTAT
    private void handleResult(Object result, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if (result instanceof ModelView) {
            ModelView modelView = (ModelView) result;
            String viewPath = modelView.getView();
            
            System.out.println("📊 ModelView reçu - Vue: " + viewPath);
            System.out.println("📦 Données: " + modelView.getData());
            
            // TRANSFERT DES DONNÉES À LA JSP
            for (Map.Entry<String, Object> entry : modelView.getData().entrySet()) {
                request.setAttribute(entry.getKey(), entry.getValue());
                System.out.println("   📍 " + entry.getKey() + " = " + entry.getValue());
            }
            
            request.getRequestDispatcher(viewPath).forward(request, response);
            
        } else if (result instanceof String) {
            // CAS SIMPLE: RETOUR STRING DIRECT
            String resultString = (String) result;
            System.out.println("📝 Retour string direct: " + resultString);
            
            request.setAttribute("message", resultString);
            request.setAttribute("action", "resultat_direct");
            request.getRequestDispatcher("/result.jsp").forward(request, response);
            
        } else if (result == null) {
            // CAS NULL
            System.out.println("⚡ Retour null");
            
            request.setAttribute("message", "Méthode exécutée avec succès (retour null)");
            request.setAttribute("action", "sans_retour");
            request.getRequestDispatcher("/result.jsp").forward(request, response);
            
        } else {
            // AUTRE TYPE D'OBJET
            System.out.println("🔮 Retour objet: " + result.getClass().getSimpleName());
            
            request.setAttribute("message", "Retour: " + result.toString());
            request.setAttribute("action", "objet_retour");
            request.getRequestDispatcher("/result.jsp").forward(request, response);
        }
    }
    
    // 🔥 GESTION DES ERREURS
    private void handleError(Exception e, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String errorMessage = e.getMessage();
        String exceptionType = e.getClass().getSimpleName();
        
        System.out.println("🚨 ERREUR: " + exceptionType + " - " + errorMessage);
        
        request.setAttribute("erreur", errorMessage);
        request.setAttribute("exceptionType", exceptionType);
        request.setAttribute("timestamp", new java.util.Date());
        
        request.getRequestDispatcher("/error.jsp").forward(request, response);
    }
    
    // 🔥 NETTOYAGE DU CHEMIN
    private String getCleanPath(HttpServletRequest request) {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        
        System.out.println("📍 URI original: " + path);
        System.out.println("📍 Contexte: " + contextPath);
        
        if (contextPath != null && !contextPath.isEmpty() && path.startsWith(contextPath)) {
            path = path.substring(contextPath.length());
            System.out.println("📍 URI après contexte: " + path);
        }
        
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        
        System.out.println("📍 Chemin final: '" + path + "'");
        return path;
    }
    
    // 🔥 DÉTECTION RESSOURCES STATIQUES
    private boolean isStaticResource(String path) {
        if (path == null || path.isEmpty()) return false;
        
        boolean isStatic = path.endsWith(".css") || 
                          path.endsWith(".js") ||
                          path.endsWith(".png") || 
                          path.endsWith(".jpg") ||
                          path.endsWith(".gif") ||
                          path.endsWith(".ico") ||
                          path.endsWith(".woff") ||
                          path.endsWith(".woff2") ||
                          path.contains("/WEB-INF/") ||
                          path.contains("/META-INF/");
        
        if (isStatic) {
            System.out.println("📁 Ressource statique détectée: " + path);
        }
        
        return isStatic;
    }
    
    // 🔥 MÉTHODE POUR RÉCUPÉRER LES MAPPINGS (UTILE POUR DÉBOGAGE)
    public Map<String, AnnotationScanner.Mapping> getAllMappings() {
        return scanner.getAllMappings();
    }
}