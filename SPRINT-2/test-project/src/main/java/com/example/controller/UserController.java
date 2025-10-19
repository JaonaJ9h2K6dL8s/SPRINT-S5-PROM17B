package com.example.controller;

import com.mvc.framework.annotation.Controller;
import com.mvc.framework.annotation.Route;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Contrôleur pour la gestion des utilisateurs
 */
@Controller
public class UserController {
    
    @Route(url = "/users")
    public String listUsers() {
        return "Liste des utilisateurs 👥<br>" +
               "1. Admin (admin@example.com)<br>" +
               "2. John Doe (john@example.com)<br>" +
               "3. Jane Smith (jane@example.com)<br>" +
               "<br>Contrôleur: UserController<br>" +
               "Action: Affichage de la liste";
    }
    
    @Route(url = "/users/profile")
    public String userProfile(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        return "Profil utilisateur 👤<br>" +
               "Nom: John Doe<br>" +
               "Email: john@example.com<br>" +
               "Rôle: Utilisateur<br>" +
               "Session: " + sessionId.substring(0, 8) + "...<br>" +
               "<br>Contrôleur: UserController<br>" +
               "Action: Affichage du profil";
    }
    
    @Route(url = "/users/settings")
    public String userSettings() {
        return "Paramètres utilisateur ⚙️<br>" +
               "Langue: Français<br>" +
               "Thème: Clair<br>" +
               "Notifications: Activées<br>" +
               "Confidentialité: Publique<br>" +
               "<br>Contrôleur: UserController<br>" +
               "Action: Gestion des paramètres";
    }
}