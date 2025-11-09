package com.monframework;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

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
        
        // URL racine → 404
        if (path.isEmpty()) {
            System.out.println("❌ URL racine non autorisée - Envoi 404");
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page non trouvée");
            return;
        }
        
        // display.jsp → Laisser Tomcat gérer
        if (path.equals("display.jsp")) {
            System.out.println("📄 display.jsp - Laisser Tomcat gérer");
            return;
        }
        
        // Ressources statiques → Laisser Tomcat gérer
        if (isStaticResource(path)) {
            System.out.println("📁 Ressource statique ignorée: " + path);
            return;
        }
        
        AnnotationScanner.Mapping mapping = scanner.getMapping(path);
        
        if (mapping != null) {
            try {
                System.out.println("🎯 Mapping trouvé: " + mapping.className.getSimpleName() + "." + mapping.method.getName());
                
                Object controller = mapping.className.getDeclaredConstructor().newInstance();
                Method method = mapping.method;
                
                // Exécuter la méthode et récupérer le résultat
                Object result = method.invoke(controller);
                
                // 🔥 AFFICHER DIRECTEMENT LE RÉSULTAT EN HTML
                displayResult(response, mapping.className.getSimpleName(), method.getName(), path, result);
                
            } catch (Exception e) {
                System.out.println("❌ Erreur execution: " + e.getMessage());
                displayError(response, "Erreur: " + e.getMessage(), path);
            }
        } else {
            System.out.println("❌ AUCUN mapping trouvé pour: " + path);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "URL non trouvée: /" + path);
        }
    }
    
    /**
     * 🔥 AFFICHE LE RÉSULTAT D'UNE MÉTHODE EN HTML
     */
    private void displayResult(HttpServletResponse response, String className, String methodName, String url, Object result) 
            throws IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html lang='fr'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='container'>");
        
        if (result instanceof String) {
            String resultString = (String) result;
            out.println("        <div class='result'>");
            out.println("            " + resultString);
            out.println("        </div>");
        }
        
        out.println("        <div class='info'>");
        out.println("            <strong>📊 Informations:</strong><br>");
        out.println("            URL appelée: <strong>/" + url + "</strong><br>");
        out.println("            Classe: <strong>" + className + "</strong><br>");
        out.println("            Méthode: <strong>" + methodName + "</strong>");
        out.println("        </div>");
        
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
    }
    
    /**
     * 🔥 AFFICHE UNE ERREUR EN HTML
     */
    private void displayError(HttpServletResponse response, String error, String url) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><title>Erreur - MonFramework</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 40px; background: #f5f5f5; }");
        out.println(".container { background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
        out.println(".error { background: #ffe6e6; padding: 20px; border-radius: 5px; color: #d00; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='container'>");
        out.println("        <h1>❌ Erreur MonFramework</h1>");
        out.println("        <div class='error'>");
        out.println("            <strong>Erreur:</strong> " + error);
        out.println("        </div>");
        out.println("        <p>URL: <code>/" + url + "</code></p>");
        out.println("        <a href='display.jsp'>← Retour à l'accueil</a>");
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
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