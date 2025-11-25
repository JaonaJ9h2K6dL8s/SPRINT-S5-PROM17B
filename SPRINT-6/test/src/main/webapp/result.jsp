<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Résultat</title>
</head>
<body>
    <h1>✅ Résultat</h1>
    
    <h2>${action}</h2>
    <p><strong>Nom:</strong> ${nom != null ? nom : 'Non reçu'}</p>
    <p><strong>User ID:</strong> ${userId != null ? userId : 'Non reçu'}</p>
    <p><strong>Actif:</strong> ${actif != null ? actif : 'Non reçu'}</p>
    <p><strong>Message:</strong> ${message}</p>
    
    <br>
    <a href="formulaire">🔄 Nouveau test</a>
</body>
</html>