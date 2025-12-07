<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Erreur</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        .error { border: 2px solid #f44336; padding: 20px; background: #fff5f5; }
        .details { background: #ffeaea; padding: 15px; margin: 10px 0; }
        .back-btn { padding: 10px 15px; background: #f44336; color: white; text-decoration: none; border-radius: 4px; }
        .solution { background: #fff3cd; padding: 15px; margin: 10px 0; border-left: 4px solid #ffc107; }
    </style>
</head>
<body>
    <h1>❌ Erreur Détectée</h1>
    
    <div class="error">
        <h2>🚫 ${exceptionType != null ? exceptionType : 'Erreur'}</h2>
        
        <div class="details">
            <h3>📋 Message d'erreur:</h3>
            <p><strong>${erreur}</strong></p>
        </div>
        
        <div class="solution">
            <h3>💡 Solution possible:</h3>
            <%
                String error = (String) request.getAttribute("erreur");
                if (error != null) {
                    if (error.contains("Paramètre requis")) {
                        out.println("<p>Vérifiez que vous avez bien rempli le champ requis dans le formulaire.</p>");
                    } else if (error.contains("Conversion impossible")) {
                        out.println("<p>Le type de données saisi ne correspond pas à ce qui est attendu.</p>");
                    } else if (error.contains("non autorisé")) {
                        out.println("<p>Vous avez envoyé un paramètre non attendu pour cette action.</p>");
                    } else if (error.contains("exactement 1 paramètre")) {
                        out.println("<p>La méthode doit avoir exactement un paramètre annoté avec @RequestParam.</p>");
                    } else {
                        out.println("<p>Vérifiez les données saisies et réessayez.</p>");
                    }
                }
            %>
        </div>
    </div>
    
    <br>
    <a href="formulaire" class="back-btn">🔄 Retour aux Tests</a>
    <a href="${pageContext.request.contextPath}/" class="back-btn">🏠 Accueil</a>
    
    <!-- Debug info -->
    <div style="margin-top: 20px; font-size: 12px; color: #666;">
        <h4>🐛 Informations techniques:</h4>
        <p>URL: ${pageContext.request.requestURI}</p>
        <p>Méthode: ${pageContext.request.method}</p>
        <p>Erreur: ${erreur}</p>
    </div>
</body>
</html>