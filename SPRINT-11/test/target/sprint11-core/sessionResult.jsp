<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Résultat de l'opération</title>
</head>
<body>
    <h1>Résultat de l'opération</h1>
    
    <%
        String operation = (String) request.getAttribute("operation");
        String key = (String) request.getAttribute("key");
        String value = (String) request.getAttribute("value");
        String oldValue = (String) request.getAttribute("oldValue");
        String newValue = (String) request.getAttribute("newValue");
        
        com.monframework.SessionMap sessionMap = 
            (com.monframework.SessionMap) request.getAttribute("session");
    %>
    
    <h2>Opération: <%= operation %></h2>
    
    <%
        if (operation.equals("Ajout")) {
    %>
    <p>Variable ajoutée:</p>
    <p>Clé: <%= key %></p>
    <p>Valeur: <%= value %></p>
    <%
        } else if (operation.equals("Consultation")) {
    %>
    <p>Variable consultée:</p>
    <p>Clé: <%= key %></p>
    <p>Valeur: <%= value %></p>
    <%
        } else if (operation.equals("Modification")) {
    %>
    <p>Variable modifiée:</p>
    <p>Clé: <%= key %></p>
    <p>Ancienne valeur: <%= oldValue %></p>
    <p>Nouvelle valeur: <%= newValue %></p>
    <%
        } else if (operation.equals("Suppression")) {
    %>
    <p>Variable supprimée:</p>
    <p>Clé: <%= key %></p>
    <p>Valeur supprimée: <%= value %></p>
    <%
        } else if (operation.equals("Nettoyage complet")) {
    %>
    <p>Session nettoyée</p>
    <%
        } else if (operation.equals("Invalidation")) {
    %>
    <p>Session invalidée</p>
    <%
        }
    %>
    
    <h3>Contenu actuel de la session:</h3>
    <%
        if (sessionMap != null) {
            Set<String> keys = sessionMap.keySet();
    %>
    <ul>
        <%
            for (String k : keys) {
                Object v = sessionMap.get(k);
        %>
        <li><%= k %> = <%= v %></li>
        <%
            }
        %>
    </ul>
    <p>Total: <%= keys.size() %> variable(s)</p>
    <%
        } else {
    %>
    <p>Session vide</p>
    <%
        }
    %>
    
    <br>
    <a href="session-form">Retour au menu principal</a>
    <br>
    <a href="session-show">Voir le contenu complet</a>
</body>
</html>