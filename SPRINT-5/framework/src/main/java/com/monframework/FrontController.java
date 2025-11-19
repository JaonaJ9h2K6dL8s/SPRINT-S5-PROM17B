package com.monframework;

import java.io.IOException;
import java.lang.reflect.Method;
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
            System.out.println("✅ Classe test.Utilisateur chargée avec succès");
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
    
    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String path = getCleanPath(request);
        System.out.println("🔍 Requête reçue - Chemin: '" + path + "'");
        
        // ... (gestion des URLs racine et ressources) ...
        
        AnnotationScanner.Mapping mapping = scanner.getMapping(path);
        
        if (mapping != null) {
            try {
                System.out.println("🎯 Mapping trouvé: " + mapping.className.getSimpleName() + "." + mapping.method.getName());
                
                Object controller = mapping.className.getDeclaredConstructor().newInstance();
                Method method = mapping.method;
                
                // 🔥 EXÉCUTER LA MÉTHODE ET RÉCUPÉRER LE RÉSULTAT
                Object result = method.invoke(controller);
                
                // 🔥 TRAITEMENT DU RETOUR ModelView
                if (result instanceof ModelView) {
                    ModelView modelView = (ModelView) result;
                    System.out.println("✅ ModelView reçu - Vue: " + modelView.getView());
                    
                    // ✅ AJOUTER CETTE LIGNE - Passer les données à la requête
                    for (Map.Entry<String, Object> entry : modelView.getData().entrySet()) {
                        request.setAttribute(entry.getKey(), entry.getValue());
                    }
                    
                    request.getRequestDispatcher(modelView.getView()).forward(request, response);
                } else if (result instanceof String) {
                    // 🔥 COMPATIBILITÉ : Si retour String direct
                    String resultString = (String) result;
                    
                    // CORRECTION: Le constructeur ModelView(String, String) n'existe pas
                    // On utilise le constructeur disponible
                    ModelView modelView = new ModelView("/result.jsp");
                    request.setAttribute("data", resultString); // Stocker le string comme donnée
                    request.getRequestDispatcher(modelView.getView()).forward(request, response);
                    
                } else {
                    // 🔥 Retour sans données
                    ModelView modelView = new ModelView("/result.jsp");
                    request.getRequestDispatcher(modelView.getView()).forward(request, response);
                }
                
            } catch (Exception e) {
                System.out.println("❌ Erreur execution: " + e.getMessage());
                e.printStackTrace(); // Ajout pour mieux déboguer
                
                // CORRECTION: Même problème de constructeur
                ModelView errorView = new ModelView("/result.jsp");
                request.setAttribute("data", "Erreur: " + e.getMessage());
                request.getRequestDispatcher(errorView.getView()).forward(request, response);
            }
        } else {
            System.out.println("❌ AUCUN mapping trouvé pour: " + path);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "URL non trouvée: /" + path);
        }
    }
    

    private String getCleanPath(HttpServletRequest request) {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        
        if (contextPath != null && !contextPath.isEmpty() && path.startsWith(contextPath)) {
            path = path.substring(contextPath.length());
        }
        
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        
        return path;
    }
    
    private boolean isStaticResource(String path) {
        if (path == null || path.isEmpty()) return false;
        
        return path.endsWith(".css") || 
               path.endsWith(".js") ||
               path.endsWith(".png") || 
               path.endsWith(".jpg") ||
               path.endsWith(".gif") ||
               path.endsWith(".ico") ||
               path.endsWith(".woff") ||
               path.endsWith(".woff2") ||
               path.contains("/WEB-INF/") ||
               path.contains("/META-INF/");
    }
}