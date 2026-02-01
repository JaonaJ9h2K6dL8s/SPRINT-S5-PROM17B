<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Gestion des Sessions</title>
</head>
<body>
    <h1>Gestion des Variables de Session</h1>
    
    <h2>Session Actuelle</h2>
    <p><a href="session-show">Voir le contenu complet de la session</a></p>
    
    <h2>Opérations disponibles :</h2>
    
    <h3>1. Ajouter une variable</h3>
    <form action="session-add" method="GET">
        Clé: <input type="text" name="key" required><br>
        Valeur: <input type="text" name="value" required><br>
        <input type="submit" value="Ajouter">
    </form>
    
    <h3>2. Consulter une variable</h3>
    <form action="session-get" method="GET">
        Clé: <input type="text" name="key" required><br>
        <input type="submit" value="Consulter">
    </form>
    
    <h3>3. Modifier une variable</h3>
    <form action="session-update" method="GET">
        Clé: <input type="text" name="key" required><br>
        Nouvelle valeur: <input type="text" name="value" required><br>
        <input type="submit" value="Modifier">
    </form>
    
    <h3>4. Supprimer une variable</h3>
    <form action="session-remove" method="GET">
        Clé: <input type="text" name="key" required><br>
        <input type="submit" value="Supprimer">
    </form>
    
    <h3>5. Opérations globales</h3>
    <p><a href="session-clear">Nettoyer toute la session</a></p>
    <p><a href="session-invalidate">Invalidate la session</a></p>
</body>
</html>