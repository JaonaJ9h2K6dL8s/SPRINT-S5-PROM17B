<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>404 - Page non trouvée</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            padding: 50px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
    </style>
</head>
<body>
    <div class="error-container">
        <h1>404</h1>
        <h2>Page non trouvée</h2>
        <p>La page que vous cherchez n'existe pas ou a été déplacée.</p>
        <a href="${pageContext.request.contextPath}/">← Retour à l'accueil</a>
    </div>
</body>
</html>