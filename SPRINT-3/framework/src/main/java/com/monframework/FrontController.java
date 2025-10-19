package com.monframework;

import java.io.IOException;
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
        
        String pathInfo = request.getPathInfo(); // Pour /app/* 
        String servletPath = request.getServletPath();
        String contextPath = request.getContextPath();
        
        System.out.println("🔍 Requête reçue:");
        System.out.println("   - Context Path: " + contextPath);
        System.out.println("   - Servlet Path: " + servletPath);
        System.out.println("   - Path Info: " + pathInfo);
        System.out.println("   - Request URI: " + request.getRequestURI());
        
        String path = "";
        
        // 🔥 CORRECTION : Récupérer le bon chemin
        if (pathInfo != null) {
            path = pathInfo;
        } else if (servletPath != null && servletPath.startsWith("/app/")) {
            path = servletPath.substring(5); // Enlever "/app/"
        } else {
            path = servletPath;
        }
        
        // Nettoyer le path
        if (path != null && path.startsWith("/")) {
            path = path.substring(1);
        }
        
        System.out.println("🔎 Chemin analysé: '" + path + "'");
        
        // Page d'accueil
        if (path == null || path.isEmpty() || path.equals("display.jsp")) {
            System.out.println("📄 Affichage de display.jsp");
            request.getRequestDispatcher("/display.jsp").forward(request, response);
            return;
        }
        
        AnnotationScanner.Mapping mapping = scanner.getMapping(path);
        
        if (mapping != null) {
            try {
                System.out.println("🎯 Mapping trouvé: " + mapping.className.getSimpleName() + "." + mapping.method.getName());
                
                Object controller = mapping.className.getDeclaredConstructor().newInstance();
                Method method = mapping.method;
                method.invoke(controller);
                
                request.setAttribute("className", mapping.className.getSimpleName());
                request.setAttribute("methodName", method.getName());
                request.setAttribute("url", path);
                
                System.out.println("✅ Méthode exécutée avec succès");
                
            } catch (Exception e) {
                System.out.println("❌ Erreur execution: " + e.getMessage());
                request.setAttribute("error", "Erreur: " + e.getMessage());
            }
        } else {
            System.out.println("❌ AUCUN mapping trouvé pour: " + path);
            request.setAttribute("error", "URL non trouvée: " + path);
        }
        
        request.getRequestDispatcher("/display.jsp").forward(request, response);
    }
}