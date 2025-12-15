package test;

import java.util.Map;

import com.monframework.Json;
import com.monframework.Url;

public class Utilisateur {

    @Url("api/utilisateurs")
    @Json
    public Object getUtilisateursJson() {
        // Création de la structure JSON standardisée
        Map<String, Object> response = new java.util.HashMap<>();
        
        response.put("status", "success");
        response.put("code", 200);
        
        // Exemple de données (à remplacer par tes vraies données)
        Map<String, Object> userData = new java.util.HashMap<>();
        userData.put("id", 1);
        userData.put("name", "Jean");
        userData.put("email", "jean@example.com");
        
        response.put("data", userData);
        
        return response;
    }

    @Url("api/utilisateurs/liste")
    @Json
    public Object getListeUtilisateursJson() {
        // Création de la structure JSON standardisée pour une liste
        Map<String, Object> response = new java.util.HashMap<>();
        
        response.put("status", "success");
        response.put("code", 200);
        
        // Création d'une liste d'utilisateurs exemple
        java.util.List<Map<String, Object>> usersList = new java.util.ArrayList<>();
        
        Map<String, Object> user1 = new java.util.HashMap<>();
        user1.put("id", 1);
        user1.put("name", "Jean");
        
        Map<String, Object> user2 = new java.util.HashMap<>();
        user2.put("id", 2);
        user2.put("name", "Marie");
        
        usersList.add(user1);
        usersList.add(user2);
        
        response.put("result", usersList);  // "result" au lieu de "data" pour une liste
        response.put("count", usersList.size());
        
        return response;
    }
}