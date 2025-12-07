<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Résultat</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        .success { border: 2px solid #4CAF50; padding: 20px; background: #f9fff9; }
        .info { border: 1px solid #2196F3; padding: 15px; background: #f3f9ff; margin: 10px 0; }
        .data { background: #f5f5f5; padding: 10px; margin: 5px 0; }
        .back-btn { padding: 10px 15px; background: #4CAF50; color: white; text-decoration: none; border-radius: 4px; }
    </style>
</head>
<body>
    <h1>🎯 Résultat de l'Action</h1>
    
    <div class="success">
        <h2>✅ ${action != null ? action : 'Opération'} Réussie</h2>
        
        <div class="info">
            <h3>📊 Données Reçues:</h3>
            
            <!-- Affichage dynamique de toutes les données -->
            <div class="data">
                <strong>Action:</strong> ${action != null ? action : 'Non spécifiée'}<br>
                <strong>Nom:</strong> ${nom != null ? nom : 'Non spécifié'}<br>
                <strong>User ID:</strong> ${userId != null ? userId : 'Non spécifié'}<br>
                <strong>Mot-clé:</strong> ${keyword != null ? keyword : 'Non spécifié'}<br>
                <strong>Statut actif:</strong> ${actif != null ? actif : 'Non spécifié'}<br>
                <strong>Message:</strong> ${message != null ? message : 'Aucun message'}<br>
                <strong>Status:</strong> ${status != null ? status : 'Succès'}
            </div>
        </div>
        
        <div class="info">
            <h3>🔍 Détails de la Requête:</h3>
            <div class="data">
                <strong>URL:</strong> ${pageContext.request.requestURI}<br>
                <strong>Méthode:</strong> ${pageContext.request.method}<br>
                <strong>Timestamp:</strong> <%= new java.util.Date() %>
            </div>
        </div>
    </div>
    
    <br>
    <a href="formulaire" class="back-btn">🔄 Nouveau Test</a>
    <a href="${pageContext.request.contextPath}/" class="back-btn">🏠 Accueil</a>
    
    <!-- Debug info -->
    <div style="margin-top: 20px; font-size: 12px; color: #666;">
        <h4>🐛 Debug Information:</h4>
        <p>Paramètres: ${pageContext.request.queryString}</p>
        <p>Attributs disponibles: 
            <%
                java.util.Enumeration<String> attrs = request.getAttributeNames();
                while (attrs.hasMoreElements()) {
                    String attr = attrs.nextElement();
                    out.print(attr + " ");
                }
            %>
        </p>
    </div>
</body>
</html>