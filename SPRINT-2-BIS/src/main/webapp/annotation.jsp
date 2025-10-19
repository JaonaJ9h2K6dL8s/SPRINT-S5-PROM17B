<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Framework Sprint 2-bis - Test des Annotations Avancées</title>
    <style>
        body { 
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
            margin: 0; 
            padding: 20px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }
        .container { 
            background: rgba(255,255,255,0.95); 
            padding: 40px; 
            border-radius: 15px; 
            box-shadow: 0 8px 32px rgba(0,0,0,0.1);
            backdrop-filter: blur(10px);
            max-width: 1200px;
            margin: 0 auto;
        }
        h1 { 
            color: #2c3e50; 
            border-bottom: 3px solid #3498db; 
            padding-bottom: 15px; 
            text-align: center;
            margin-bottom: 30px;
        }
        h2 {
            color: #34495e;
            margin-top: 30px;
            border-left: 4px solid #e74c3c;
            padding-left: 15px;
        }
        .controller-info { 
            background: linear-gradient(135deg, #e8f4fd 0%, #f0f8ff 100%); 
            padding: 20px; 
            border-left: 5px solid #3498db; 
            margin: 20px 0; 
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        .method-info { 
            background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%); 
            padding: 15px; 
            border-left: 4px solid #28a745; 
            margin: 10px 0 10px 20px; 
            border-radius: 6px;
        }
        .controller-name { 
            font-weight: bold; 
            color: #2980b9; 
            font-size: 1.2em;
        }
        .method-name { 
            font-weight: bold; 
            color: #27ae60; 
        }
        .url-mapping { 
            color: #e74c3c; 
            font-family: 'Courier New', monospace; 
            background: #f8f9fa;
            padding: 2px 6px;
            border-radius: 3px;
        }
        .stats {
            display: flex;
            justify-content: space-around;
            margin: 30px 0;
        }
        .stat-box {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 20px;
            border-radius: 10px;
            text-align: center;
            min-width: 150px;
        }
        .stat-number {
            font-size: 2em;
            font-weight: bold;
        }
        .test-links {
            background: #fff3cd;
            border: 1px solid #ffeaa7;
            border-radius: 8px;
            padding: 20px;
            margin: 20px 0;
        }
        .test-links a {
            display: inline-block;
            margin: 5px 10px;
            padding: 8px 15px;
            background: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            transition: background 0.3s;
        }
        .test-links a:hover {
            background: #0056b3;
        }
    </style>
</head>
<body>
    <%@ page import="java.lang.reflect.Method" %>
    <%@ page import="java.util.List" %>
    <%@ page import="java.util.ArrayList" %>
    <%@ page import="project.controller.*" %>
    <%@ page import="project.controllerBe.*" %>
    <%@ page import="project.controllerKely.*" %>
    <%@ page import="jframework.annotation.Controller" %>
    <%@ page import="jframework.annotation.Url" %>

    <div class="container">
        <h1>🚀 Framework Sprint 2-bis - Scan Avancé des Annotations</h1>
        
        <p style="text-align: center; font-size: 1.1em; color: #666;">
            Cette page démontre les capacités avancées du framework Sprint 2-bis :<br>
            <strong>Auto-découverte des contrôleurs</strong> avec @Controller et <strong>routage intelligent</strong> avec @Url
        </p>

        <%
            // Liste des classes à scanner
            Class<?>[] controllerClasses = {
                project.controller.Personne.class,
                project.controllerBe.Voiture.class,
                project.controllerKely.Materiaux.class
            };
            
            int totalControllers = 0;
            int totalMethods = 0;
            List<String> allUrls = new ArrayList<>();
        %>

        <div class="stats">
            <div class="stat-box">
                <div class="stat-number"><%= controllerClasses.length %></div>
                <div>Contrôleurs</div>
            </div>
            <div class="stat-box">
                <div class="stat-number" id="methodCount">0</div>
                <div>Méthodes @Url</div>
            </div>
            <div class="stat-box">
                <div class="stat-number">3</div>
                <div>Packages</div>
            </div>
        </div>

        <h2>📋 Contrôleurs Découverts</h2>
        
        <%
            for (Class<?> clazz : controllerClasses) {
                boolean isController = clazz.isAnnotationPresent(Controller.class);
                totalControllers++;
        %>
        
        <div class="controller-info">
            <div class="controller-name">
                <%= isController ? "🎯" : "📦" %> <%= clazz.getSimpleName() %>
                <span style="color: #666; font-size: 0.9em;">(<%= clazz.getPackage().getName() %>)</span>
                <%= isController ? "<span style='color: #28a745; font-size: 0.9em;'>@Controller</span>" : "<span style='color: #ffc107; font-size: 0.9em;'>Compatibilité</span>" %>
            </div>
            
            <p style="margin: 10px 0; color: #666;">
                <%= isController ? "Contrôleur auto-découvert avec l'annotation @Controller" : "Contrôleur en mode compatibilité (sans @Controller)" %>
            </p>
            
            <%
                Method[] methods = clazz.getDeclaredMethods();
                boolean hasUrlMethods = false;
                
                for (Method method : methods) {
                    if (method.isAnnotationPresent(Url.class)) {
                        hasUrlMethods = true;
                        totalMethods++;
                        Url urlAnnotation = method.getAnnotation(Url.class);
                        String url = urlAnnotation.value();
                        allUrls.add(url);
            %>
            
            <div class="method-info">
                <span class="method-name">🔗 <%= method.getName() %>()</span><br>
                <strong>URL mappée :</strong> <span class="url-mapping"><%= url %></span><br>
                <strong>Type de retour :</strong> <code><%= method.getReturnType().getSimpleName() %></code>
            </div>
            
            <%
                    }
                }
                
                if (!hasUrlMethods) {
            %>
            <p style="color: #999; font-style: italic; margin-left: 20px;">Aucune méthode annotée avec @Url trouvée.</p>
            <%
                }
            %>
        </div>
        
        <%
            }
        %>

        <script>
            document.getElementById('methodCount').textContent = '<%= totalMethods %>';
        </script>

        <div class="test-links">
            <h3>🧪 Liens de Test</h3>
            <p>Cliquez sur les liens ci-dessous pour tester le routage automatique :</p>
            <%
                for (String url : allUrls) {
            %>
            <a href="<%= request.getContextPath() + url %>" target="_blank"><%= url %></a>
            <%
                }
            %>
        </div>

        <h2>📊 Résumé des Fonctionnalités Sprint 2-bis</h2>
        <div style="background: #f8f9fa; padding: 20px; border-radius: 8px; border-left: 5px solid #17a2b8;">
            <ul style="margin: 0; padding-left: 20px;">
                <li><strong>Auto-découverte des contrôleurs</strong> : Les classes annotées avec @Controller sont automatiquement détectées</li>
                <li><strong>Routage intelligent</strong> : Les méthodes annotées avec @Url sont automatiquement mappées</li>
                <li><strong>Support multi-packages</strong> : Scan dans différents packages (controller, controllerBe, controllerKely)</li>
                <li><strong>Compatibilité ascendante</strong> : Support des contrôleurs sans @Controller</li>
                <li><strong>Interface améliorée</strong> : Affichage moderne avec retour de valeurs des méthodes</li>
            </ul>
        </div>

        <p style="text-align: center; margin-top: 30px; color: #666;">
            <em>Framework Sprint 2-bis - Développement par sprints itératifs</em>
        </p>
    </div>
</body>
</html>