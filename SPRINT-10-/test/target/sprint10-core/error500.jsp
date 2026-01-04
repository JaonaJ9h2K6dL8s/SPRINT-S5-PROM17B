<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>500 - Erreur Serveur</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            padding: 50px;
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            color: white;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .error-container {
            background: rgba(255, 255, 255, 0.1);
            padding: 40px;
            border-radius: 15px;
            backdrop-filter: blur(10px);
        }
        
        h1 {
            font-size: 5em;
            margin: 0;
        }
        
        h2 {
            margin-top: 0;
            margin-bottom: 30px;
        }
        
        a {
            color: white;
            text-decoration: underline;
            font-size: 1.2em;
        }
        
        .error-details {
            background: rgba(0, 0, 0, 0.2);
            padding: 15px;
            border-radius: 8px;
            margin: 20px 0;
            text-align: left;
            font-family: monospace;
            max-width: 600px;
            overflow-x: auto;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <h1>500</h1>
        <h2>Erreur interne du serveur</h2>
        <p>Une erreur s'est produite lors du traitement de votre requête.</p>
        
        <div class="error-details">
            <strong>Message d'erreur :</strong><br>
            <%= exception != null ? exception.getMessage() : "Aucun détail disponible" %>
        </div>
        
        <a href="${pageContext.request.contextPath}/">← Retour à l'accueil</a>
    </div>
</body>
</html>