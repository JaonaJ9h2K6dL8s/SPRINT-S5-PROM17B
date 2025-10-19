package com.example.controller;

import com.mvc.framework.annotation.Controller;
import com.mvc.framework.annotation.Route;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Contrôleur d'exemple pour tester le système d'annotations
 */
@Controller
public class HomeController {
    
    @Route(url = "/")
    public String index() {
        return "Bienvenue sur la page d'accueil ! 🏠<br>" +
               "Le système d'annotations fonctionne parfaitement.<br>" +
               "Framework MVC - Sprint 2 activé !";
    }
    
    @Route(url = "/home")
    public String home() {
        return "Page d'accueil alternative 🏡<br>" +
               "URL: /home<br>" +
               "Contrôleur: HomeController<br>" +
               "Méthode: home()";
    }
    
    @Route(url = "/about")
    public String about() {
        return "À propos de notre Framework MVC 📖<br>" +
               "Version: Sprint 2<br>" +
               "Fonctionnalités:<br>" +
               "✅ Annotations @Controller et @Route<br>" +
               "✅ Scanning automatique des contrôleurs<br>" +
               "✅ Réflexion Java pour l'appel dynamique<br>" +
               "✅ Mapping URL vers méthodes";
    }
    
    @Route(url = "/contact")
    public String contact(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return "Page de contact 📧<br>" +
               "Votre navigateur: " + (userAgent != null ? userAgent.substring(0, Math.min(50, userAgent.length())) + "..." : "Inconnu") + "<br>" +
               "IP: " + request.getRemoteAddr() + "<br>" +
               "Méthode HTTP: " + request.getMethod();
    }
    
    @Route(url = "/test")
    public String test(HttpServletRequest request, HttpServletResponse response) {
        return "Test complet du système 🧪<br>" +
               "Paramètres injectés automatiquement:<br>" +
               "✅ HttpServletRequest: " + (request != null ? "OK" : "ERREUR") + "<br>" +
               "✅ HttpServletResponse: " + (response != null ? "OK" : "ERREUR") + "<br>" +
               "Session ID: " + request.getSession().getId() + "<br>" +
               "Timestamp: " + System.currentTimeMillis();
    }
}