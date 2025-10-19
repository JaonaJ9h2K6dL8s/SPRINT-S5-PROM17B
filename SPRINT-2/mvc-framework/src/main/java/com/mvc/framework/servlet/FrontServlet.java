package com.mvc.framework.servlet;

import com.mvc.framework.scanner.ControllerScanner;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Map;

/**
 * FrontServlet - Implémentation du pattern Front Controller
 * 
 * Ce servlet intercepte toutes les requêtes HTTP et affiche leurs informations
 * pour le débogage et le développement du framework MVC.
 * 
 * @author MVC Framework Team
 * @version 1.0.0
 */
@WebServlet(name = "FrontServlet", urlPatterns = "/*", loadOnStartup = 1)
public class FrontServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private boolean debugMode = true;
    private ControllerScanner controllerScanner;
    
    @Override
    public void init() throws ServletException {
        super.init();
        
        // Initialiser le scanner de contrôleurs
        controllerScanner = new ControllerScanner();
        
        // Scanner les packages pour trouver les contrôleurs
        // On scanne le package du projet test
        String basePackage = getServletContext().getInitParameter("base-package");
        if (basePackage == null) {
            // Package par défaut si non spécifié
            basePackage = "com.example.controller";
        }
        
        log("Scanning package: " + basePackage);
        controllerScanner.scanPackage(basePackage);
        
        // Afficher les routes trouvées
        controllerScanner.printRoutes();
        
        log("FrontServlet initialisé - Mode Debug: " + debugMode);
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
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response, "PUT");
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response, "DELETE");
    }
    
    /**
     * Traite toutes les requêtes HTTP et affiche leurs informations
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response, String method) 
            throws ServletException, IOException {
        
        // Configuration de la réponse
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        // Obtenir l'URL de la requête
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String url = requestURI.substring(contextPath.length());
        
        // Log de la requête si en mode debug
        if (debugMode) {
            logRequestInfo(request, method);
            log("URL demandée: " + url);
        }
        
        try (PrintWriter out = response.getWriter()) {
            // Chercher une route correspondante
            ControllerScanner.RouteInfo route = controllerScanner.findRoute(url);
            
            if (route != null) {
                // Route trouvée, appeler la méthode du contrôleur
                try {
                    Object result = invokeControllerMethod(route, request, response);
                    
                    // Afficher le résultat
                    if (result != null) {
                        out.println("<!DOCTYPE html>");
                        out.println("<html lang='fr'>");
                        out.println("<head>");
                        out.println("    <meta charset='UTF-8'>");
                        out.println("    <title>MVC Framework - Sprint 2</title>");
                        out.println("    <style>");
                        out.println("        body { font-family: Arial, sans-serif; margin: 40px; background-color: #f5f5f5; }");
                        out.println("        .container { max-width: 800px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
                        out.println("        .header { color: #333; border-bottom: 2px solid #4CAF50; padding-bottom: 10px; margin-bottom: 20px; }");
                        out.println("        .result { background: #e8f5e8; padding: 20px; border-radius: 5px; border-left: 4px solid #4CAF50; }");
                        out.println("        .info { color: #666; font-size: 0.9em; margin-top: 20px; }");
                        out.println("    </style>");
                        out.println("</head>");
                        out.println("<body>");
                        out.println("    <div class='container'>");
                        out.println("        <h1 class='header'>🚀 MVC Framework - Sprint 2</h1>");
                        out.println("        <div class='result'>");
                        out.println("            <h2>Résultat du contrôleur:</h2>");
                        out.println("            <p>" + result.toString() + "</p>");
                        out.println("        </div>");
                        out.println("        <div class='info'>");
                        out.println("            <p><strong>URL:</strong> " + url + "</p>");
                        out.println("            <p><strong>Contrôleur:</strong> " + route.getControllerInstance().getClass().getSimpleName() + "</p>");
                        out.println("            <p><strong>Méthode:</strong> " + route.getMethod().getName() + "</p>");
                        out.println("            <p><strong>Méthode HTTP:</strong> " + method + "</p>");
                        out.println("        </div>");
                        out.println("    </div>");
                        out.println("</body>");
                        out.println("</html>");
                    }
                } catch (Exception e) {
                    // Erreur lors de l'appel du contrôleur
                    generateErrorPage(out, url, e);
                }
            } else {
                // Aucune route trouvée
                if (debugMode) {
                    // En mode debug, afficher la page de debug
                    generateDebugPage(out, request, method);
                } else {
                    // En mode production, afficher une erreur 404
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    generate404Page(out, url);
                }
            }
        }
    }
    
    /**
     * Invoque la méthode du contrôleur via réflexion
     */
    private Object invokeControllerMethod(ControllerScanner.RouteInfo route, 
                                        HttpServletRequest request, 
                                        HttpServletResponse response) throws Exception {
        Method method = route.getMethod();
        Object controller = route.getControllerInstance();
        
        // Déterminer les paramètres de la méthode
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];
        
        // Injecter les paramètres appropriés
        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i] == HttpServletRequest.class) {
                parameters[i] = request;
            } else if (parameterTypes[i] == HttpServletResponse.class) {
                parameters[i] = response;
            } else {
                // Pour d'autres types, on peut ajouter d'autres injections plus tard
                parameters[i] = null;
            }
        }
        
        // Rendre la méthode accessible si elle est privée
        method.setAccessible(true);
        
        // Invoquer la méthode
        return method.invoke(controller, parameters);
    }
    
    /**
     * Génère une page d'erreur
     */
    private void generateErrorPage(PrintWriter out, String url, Exception e) {
        out.println("<!DOCTYPE html>");
        out.println("<html lang='fr'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <title>Erreur - MVC Framework</title>");
        out.println("    <style>");
        out.println("        body { font-family: Arial, sans-serif; margin: 40px; background-color: #f5f5f5; }");
        out.println("        .container { max-width: 800px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
        out.println("        .error { background: #ffe6e6; padding: 20px; border-radius: 5px; border-left: 4px solid #ff4444; }");
        out.println("        .error h2 { color: #cc0000; margin-top: 0; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='container'>");
        out.println("        <div class='error'>");
        out.println("            <h2>❌ Erreur lors de l'exécution</h2>");
        out.println("            <p><strong>URL:</strong> " + url + "</p>");
        out.println("            <p><strong>Erreur:</strong> " + e.getMessage() + "</p>");
        out.println("            <p><strong>Type:</strong> " + e.getClass().getSimpleName() + "</p>");
        out.println("        </div>");
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
    }
    
    /**
     * Génère une page 404
     */
    private void generate404Page(PrintWriter out, String url) {
        out.println("<!DOCTYPE html>");
        out.println("<html lang='fr'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <title>404 - Page non trouvée</title>");
        out.println("    <style>");
        out.println("        body { font-family: Arial, sans-serif; margin: 40px; background-color: #f5f5f5; }");
        out.println("        .container { max-width: 800px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
        out.println("        .not-found { background: #fff3cd; padding: 20px; border-radius: 5px; border-left: 4px solid #ffc107; }");
        out.println("        .not-found h2 { color: #856404; margin-top: 0; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='container'>");
        out.println("        <div class='not-found'>");
        out.println("            <h2>🔍 Page non trouvée</h2>");
        out.println("            <p>L'URL <strong>" + url + "</strong> n'a pas été trouvée.</p>");
        out.println("            <p>Aucune route correspondante n'a été définie dans les contrôleurs.</p>");
        out.println("        </div>");
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
    }
    
    /**
     * Génère une page HTML affichant les informations de la requête
     */
    private void generateDebugPage(PrintWriter out, HttpServletRequest request, String method) {
        out.println("<!DOCTYPE html>");
        out.println("<html lang='fr'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>MVC Framework - Debug Mode</title>");
        out.println("    <style>");
        out.println("        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5; }");
        out.println("        .container { max-width: 1200px; margin: 0 auto; background: white; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
        out.println("        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; border-radius: 8px 8px 0 0; }");
        out.println("        .header h1 { margin: 0; font-size: 2.5em; }");
        out.println("        .header p { margin: 10px 0 0 0; opacity: 0.9; }");
        out.println("        .content { padding: 30px; }");
        out.println("        .section { margin-bottom: 30px; }");
        out.println("        .section h2 { color: #333; border-bottom: 2px solid #667eea; padding-bottom: 10px; }");
        out.println("        .info-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 20px; }");
        out.println("        .info-card { background: #f8f9fa; border: 1px solid #e9ecef; border-radius: 6px; padding: 20px; }");
        out.println("        .info-card h3 { margin-top: 0; color: #495057; }");
        out.println("        .param-table, .header-table { width: 100%; border-collapse: collapse; margin-top: 10px; }");
        out.println("        .param-table th, .param-table td, .header-table th, .header-table td { padding: 8px 12px; text-align: left; border-bottom: 1px solid #dee2e6; }");
        out.println("        .param-table th, .header-table th { background-color: #e9ecef; font-weight: 600; }");
        out.println("        .method-badge { display: inline-block; padding: 4px 12px; border-radius: 20px; font-weight: bold; font-size: 0.9em; }");
        out.println("        .method-get { background-color: #d4edda; color: #155724; }");
        out.println("        .method-post { background-color: #d1ecf1; color: #0c5460; }");
        out.println("        .method-put { background-color: #fff3cd; color: #856404; }");
        out.println("        .method-delete { background-color: #f8d7da; color: #721c24; }");
        out.println("        .timestamp { color: #6c757d; font-size: 0.9em; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        
        out.println("    <div class='container'>");
        out.println("        <div class='header'>");
        out.println("            <h1>🚀 MVC Framework</h1>");
        out.println("            <p>Front Controller - Mode Debug Activé</p>");
        out.println("        </div>");
        
        out.println("        <div class='content'>");
        
        // Informations générales de la requête
        out.println("            <div class='section'>");
        out.println("                <h2>📋 Informations de la Requête</h2>");
        out.println("                <div class='info-grid'>");
        
        out.println("                    <div class='info-card'>");
        out.println("                        <h3>Méthode HTTP</h3>");
        out.println("                        <span class='method-badge method-" + method.toLowerCase() + "'>" + method + "</span>");
        out.println("                    </div>");
        
        out.println("                    <div class='info-card'>");
        out.println("                        <h3>URL Demandée</h3>");
        out.println("                        <strong>" + request.getRequestURL() + "</strong>");
        out.println("                    </div>");
        
        out.println("                    <div class='info-card'>");
        out.println("                        <h3>URI</h3>");
        out.println("                        <code>" + request.getRequestURI() + "</code>");
        out.println("                    </div>");
        
        out.println("                    <div class='info-card'>");
        out.println("                        <h3>Query String</h3>");
        out.println("                        <code>" + (request.getQueryString() != null ? request.getQueryString() : "Aucune") + "</code>");
        out.println("                    </div>");
        
        out.println("                    <div class='info-card'>");
        out.println("                        <h3>Protocole</h3>");
        out.println("                        <code>" + request.getProtocol() + "</code>");
        out.println("                    </div>");
        
        out.println("                    <div class='info-card'>");
        out.println("                        <h3>Adresse IP Client</h3>");
        out.println("                        <code>" + request.getRemoteAddr() + "</code>");
        out.println("                    </div>");
        
        out.println("                </div>");
        out.println("            </div>");
        
        // Paramètres de la requête
        out.println("            <div class='section'>");
        out.println("                <h2>🔧 Paramètres de la Requête</h2>");
        Map<String, String[]> parameters = request.getParameterMap();
        if (parameters.isEmpty()) {
            out.println("                <p><em>Aucun paramètre trouvé</em></p>");
        } else {
            out.println("                <table class='param-table'>");
            out.println("                    <thead>");
            out.println("                        <tr><th>Nom</th><th>Valeur(s)</th></tr>");
            out.println("                    </thead>");
            out.println("                    <tbody>");
            for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
                out.println("                        <tr>");
                out.println("                            <td><strong>" + entry.getKey() + "</strong></td>");
                out.println("                            <td>" + String.join(", ", entry.getValue()) + "</td>");
                out.println("                        </tr>");
            }
            out.println("                    </tbody>");
            out.println("                </table>");
        }
        out.println("            </div>");
        
        // Headers HTTP
        out.println("            <div class='section'>");
        out.println("                <h2>📡 Headers HTTP</h2>");
        out.println("                <table class='header-table'>");
        out.println("                    <thead>");
        out.println("                        <tr><th>Header</th><th>Valeur</th></tr>");
        out.println("                    </thead>");
        out.println("                    <tbody>");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            out.println("                        <tr>");
            out.println("                            <td><strong>" + headerName + "</strong></td>");
            out.println("                            <td>" + headerValue + "</td>");
            out.println("                        </tr>");
        }
        out.println("                    </tbody>");
        out.println("                </table>");
        out.println("            </div>");
        
        // Informations de session
        out.println("            <div class='section'>");
        out.println("                <h2>🔐 Informations de Session</h2>");
        out.println("                <div class='info-grid'>");
        
        out.println("                    <div class='info-card'>");
        out.println("                        <h3>ID de Session</h3>");
        out.println("                        <code>" + request.getSession().getId() + "</code>");
        out.println("                    </div>");
        
        out.println("                    <div class='info-card'>");
        out.println("                        <h3>Nouvelle Session</h3>");
        out.println("                        <code>" + request.getSession().isNew() + "</code>");
        out.println("                    </div>");
        
        out.println("                </div>");
        out.println("            </div>");
        
        // Timestamp
        out.println("            <div class='section'>");
        out.println("                <p class='timestamp'>⏰ Requête traitée le : " + new java.util.Date() + "</p>");
        out.println("            </div>");
        
        out.println("        </div>");
        out.println("    </div>");
        
        out.println("</body>");
        out.println("</html>");
    }
    
    /**
     * Log les informations de la requête dans les logs du serveur
     */
    private void logRequestInfo(HttpServletRequest request, String method) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("=== REQUÊTE HTTP ===\n");
        logMessage.append("Méthode: ").append(method).append("\n");
        logMessage.append("URL: ").append(request.getRequestURL()).append("\n");
        logMessage.append("URI: ").append(request.getRequestURI()).append("\n");
        logMessage.append("Query String: ").append(request.getQueryString()).append("\n");
        logMessage.append("IP Client: ").append(request.getRemoteAddr()).append("\n");
        logMessage.append("User-Agent: ").append(request.getHeader("User-Agent")).append("\n");
        logMessage.append("==================");
        
        log(logMessage.toString());
    }
    
    @Override
    public void destroy() {
        log("FrontServlet détruit");
        super.destroy();
    }
}