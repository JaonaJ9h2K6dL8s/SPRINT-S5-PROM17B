<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MVC Framework - JSP Test Page</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            margin: 0;
            padding: 20px;
        }
        
        .container {
            max-width: 1000px;
            margin: 0 auto;
            background: white;
            border-radius: 15px;
            box-shadow: 0 20px 40px rgba(0,0,0,0.1);
            overflow: hidden;
        }
        
        .header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 30px;
            text-align: center;
        }
        
        .header h1 {
            margin: 0;
            font-size: 2.5em;
        }
        
        .content {
            padding: 30px;
        }
        
        .info-section {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 20px;
            margin: 20px 0;
            border-left: 5px solid #667eea;
        }
        
        .jsp-info {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin: 20px 0;
        }
        
        .info-card {
            background: #e3f2fd;
            border-radius: 8px;
            padding: 15px;
            border: 1px solid #2196f3;
        }
        
        .info-card h3 {
            color: #1976d2;
            margin-top: 0;
        }
        
        .test-links {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 15px;
            margin: 20px 0;
        }
        
        .test-link {
            background: linear-gradient(135deg, #28a745, #20c997);
            color: white;
            text-decoration: none;
            padding: 15px;
            border-radius: 8px;
            text-align: center;
            font-weight: 600;
            transition: transform 0.3s ease;
        }
        
        .test-link:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(40, 167, 69, 0.3);
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>🚀 MVC Framework</h1>
            <p>Page JSP de Test - Sprint 1</p>
        </div>
        
        <div class="content">
            <div class="info-section">
                <h2>📊 Informations JSP</h2>
                <div class="jsp-info">
                    <div class="info-card">
                        <h3>Date/Heure Actuelle</h3>
                        <p><fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd/MM/yyyy HH:mm:ss"/></p>
                    </div>
                    
                    <div class="info-card">
                        <h3>Session ID</h3>
                        <p><%= session.getId() %></p>
                    </div>
                    
                    <div class="info-card">
                        <h3>Nouvelle Session</h3>
                        <p><%= session.isNew() ? "Oui" : "Non" %></p>
                    </div>
                    
                    <div class="info-card">
                        <h3>Context Path</h3>
                        <p><%= request.getContextPath() %></p>
                    </div>
                    
                    <div class="info-card">
                        <h3>Servlet Path</h3>
                        <p><%= request.getServletPath() %></p>
                    </div>
                    
                    <div class="info-card">
                        <h3>Server Info</h3>
                        <p><%= application.getServerInfo() %></p>
                    </div>
                </div>
            </div>
            
            <div class="info-section">
                <h2>🧪 Tests Disponibles</h2>
                <p>Cliquez sur les liens ci-dessous pour tester différentes fonctionnalités :</p>
                
                <div class="test-links">
                    <a href="<%= request.getContextPath() %>/test/jsp?action=get&source=jsp" class="test-link">
                        Test GET depuis JSP
                    </a>
                    
                    <a href="<%= request.getContextPath() %>/api/json?format=json" class="test-link">
                        Test API JSON
                    </a>
                    
                    <a href="<%= request.getContextPath() %>/user/profile/123" class="test-link">
                        Test URL avec Paramètres
                    </a>
                    
                    <a href="<%= request.getContextPath() %>/admin/dashboard" class="test-link">
                        Test Route Admin
                    </a>
                </div>
            </div>
            
            <div class="info-section">
                <h2>🔧 Variables d'Environnement</h2>
                <div class="jsp-info">
                    <div class="info-card">
                        <h3>Java Version</h3>
                        <p><%= System.getProperty("java.version") %></p>
                    </div>
                    
                    <div class="info-card">
                        <h3>OS Name</h3>
                        <p><%= System.getProperty("os.name") %></p>
                    </div>
                    
                    <div class="info-card">
                        <h3>User Dir</h3>
                        <p><%= System.getProperty("user.dir") %></p>
                    </div>
                </div>
            </div>
            
            <div class="info-section">
                <h2>📝 Formulaire de Test JSP</h2>
                <form method="POST" action="<%= request.getContextPath() %>/jsp/submit" style="max-width: 500px;">
                    <div style="margin-bottom: 15px;">
                        <label for="jspName" style="display: block; margin-bottom: 5px; font-weight: 600;">Nom :</label>
                        <input type="text" id="jspName" name="name" value="Test JSP" 
                               style="width: 100%; padding: 10px; border: 2px solid #e9ecef; border-radius: 5px;">
                    </div>
                    
                    <div style="margin-bottom: 15px;">
                        <label for="jspAction" style="display: block; margin-bottom: 5px; font-weight: 600;">Action :</label>
                        <select id="jspAction" name="action" 
                                style="width: 100%; padding: 10px; border: 2px solid #e9ecef; border-radius: 5px;">
                            <option value="create">Créer</option>
                            <option value="update">Mettre à jour</option>
                            <option value="delete">Supprimer</option>
                        </select>
                    </div>
                    
                    <button type="submit" 
                            style="background: linear-gradient(135deg, #007bff, #6610f2); color: white; border: none; padding: 12px 25px; border-radius: 5px; font-weight: 600; cursor: pointer;">
                        Envoyer depuis JSP
                    </button>
                </form>
            </div>
            
            <div class="info-section">
                <h2>ℹ️ Informations Techniques</h2>
                <ul style="list-style-type: none; padding: 0;">
                    <li style="margin: 10px 0; padding-left: 25px; position: relative;">
                        <span style="position: absolute; left: 0;">✅</span>
                        <strong>JSP Version :</strong> 2.3.3
                    </li>
                    <li style="margin: 10px 0; padding-left: 25px; position: relative;">
                        <span style="position: absolute; left: 0;">✅</span>
                        <strong>JSTL Version :</strong> 1.2
                    </li>
                    <li style="margin: 10px 0; padding-left: 25px; position: relative;">
                        <span style="position: absolute; left: 0;">✅</span>
                        <strong>Servlet API :</strong> 4.0.1
                    </li>
                    <li style="margin: 10px 0; padding-left: 25px; position: relative;">
                        <span style="position: absolute; left: 0;">✅</span>
                        <strong>Encodage :</strong> UTF-8
                    </li>
                </ul>
            </div>
        </div>
    </div>
</body>
</html>