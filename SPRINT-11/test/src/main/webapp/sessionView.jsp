<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Contenu de la Session</title>
</head>
<body>
    <h1>Contenu de la Session</h1>
    
    <%
        com.monframework.SessionMap sessionMap = 
            (com.monframework.SessionMap) request.getAttribute("session");
        
        if (sessionMap != null) {
            Set<String> keys = sessionMap.keySet();
    %>
    
    <table border="1">
        <tr>
            <th>Clé</th>
            <th>Valeur</th>
        </tr>
        <%
            for (String key : keys) {
                Object value = sessionMap.get(key);
        %>
        <tr>
            <td><%= key %></td>
            <td><%= value %></td>
        </tr>
        <%
            }
        %>
    </table>
    
    <p>Nombre de variables: <%= keys.size() %></p>
    
    <%
        } else {
    %>
    <p>La session est vide ou n'existe pas</p>
    <%
        }
    %>
    
    <br>
    <a href="session-form">Retour au menu principal</a>
</body>
</html>