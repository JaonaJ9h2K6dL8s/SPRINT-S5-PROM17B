<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>500 - Erreur Serveur | MVC Framework</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #dc3545 0%, #fd7e14 100%);
            min-height: 100vh;
            margin: 0;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }
        
        .error-container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 40px rgba(0,0,0,0.1);
            padding: 40px;
            text-align: center;
            max-width: 700px;
            width: 100%;
        }
        
        .error-code {
            font-size: 8em;
            font-weight: bold;
            background: linear-gradient(135deg, #dc3545 0%, #fd7e14 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
            margin: 0;
            line-height: 1;
        }
        
        .error-title {
            font-size: 2em;
            color: #333;
            margin: 20px 0 10px 0;
        }
        
        .error-message {
            color: #666;
            font-size: 1.2em;
            margin-bottom: 30px;
        }
        
        .error-details {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 20px;
            margin: 20px 0;
            text-align: left;
            border-left: 5px solid #dc3545;
        }
        
        .stack-trace {
            background: #2d3748;
            color: #e2e8f0;
            padding: 15px;
            border-radius: 5px;
            font-family: 'Courier New', monospace;
            font-size: 0.9em;
            max-height: 200px;
            overflow-y: auto;
            margin-top: 10px;
        }
        
        .back-button {
            background: linear-gradient(135deg, #dc3545 0%, #fd7e14 100%);
            color: white;
            text-decoration: none;
            padding: 15px 30px;
            border-radius: 10px;
            font-weight: 600;
            display: inline-block;
            transition: transform 0.3s ease;
            margin: 10px;
        }
        
        .back-button:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(220, 53, 69, 0.3);
        }
        
        .emoji {
            font-size: 4em;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="emoji">⚠️</div>
        <div class="error-code">500</div>
        <h1 class="error-title">Erreur Serveur Interne</h1>
        <p class="error-message">
            Une erreur inattendue s'est produite sur le serveur. Nos équipes ont été notifiées.
        </p>
        
        <div class="error-details">
            <h3>🔧 Détails Techniques</h3>
            <p><strong>URL Demandée :</strong> <%= request.getRequestURL() %></p>
            <p><strong>Méthode :</strong> <%= request.getMethod() %></p>
            <p><strong>Timestamp :</strong> <%= new java.util.Date() %></p>
            <p><strong>Session ID :</strong> <%= session.getId() %></p>
            
            <% if (exception != null) { %>
                <p><strong>Exception :</strong> <%= exception.getClass().getSimpleName() %></p>
                <p><strong>Message :</strong> <%= exception.getMessage() %></p>
                
                <div class="stack-trace">
                    <strong>Stack Trace :</strong><br>
                    <%
                        java.io.StringWriter sw = new java.io.StringWriter();
                        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
                        exception.printStackTrace(pw);
                        String stackTrace = sw.toString();
                        // Limiter l'affichage à 10 lignes
                        String[] lines = stackTrace.split("\n");
                        for (int i = 0; i < Math.min(lines.length, 10); i++) {
                            out.println(lines[i] + "<br>");
                        }
                        if (lines.length > 10) {
                            out.println("... (" + (lines.length - 10) + " lignes supplémentaires)");
                        }
                    %>
                </div>
            <% } %>
        </div>
        
        <a href="<%= request.getContextPath() %>/" class="back-button">
            🏠 Retour à l'Accueil
        </a>
        
        <a href="javascript:history.back()" class="back-button">
            ⬅️ Page Précédente
        </a>
    </div>
</body>
</html>