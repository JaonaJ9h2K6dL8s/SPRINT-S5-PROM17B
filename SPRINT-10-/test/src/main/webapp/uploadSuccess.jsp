<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Upload Réussi</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            max-width: 800px;
            margin: 50px auto;
            padding: 20px;
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            min-height: 100vh;
        }
        
        .container {
            background-color: white;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
            text-align: center;
        }
        
        .icon {
            font-size: 80px;
            margin-bottom: 20px;
        }
        
        .success-icon {
            color: #4CAF50;
        }
        
        .error-icon {
            color: #f44336;
        }
        
        h1 {
            color: #333;
            margin-bottom: 20px;
            font-size: 2em;
        }
        
        .message {
            font-size: 1.2em;
            margin-bottom: 30px;
            color: #666;
            line-height: 1.6;
        }
        
        .file-info {
            background-color: #f8f9fa;
            padding: 25px;
            border-radius: 10px;
            margin: 30px 0;
            text-align: left;
            border-left: 5px solid #4CAF50;
        }
        
        .file-info h3 {
            color: #333;
            margin-top: 0;
            margin-bottom: 15px;
        }
        
        .file-detail {
            display: flex;
            justify-content: space-between;
            padding: 8px 0;
            border-bottom: 1px solid #eee;
        }
        
        .file-detail:last-child {
            border-bottom: none;
        }
        
        .detail-label {
            font-weight: bold;
            color: #555;
        }
        
        .detail-value {
            color: #333;
        }
        
        .nav-links {
            margin-top: 40px;
            display: flex;
            justify-content: center;
            gap: 20px;
            flex-wrap: wrap;
        }
        
        .nav-link {
            padding: 12px 25px;
            text-decoration: none;
            border-radius: 8px;
            font-weight: bold;
            transition: all 0.3s ease;
            display: inline-flex;
            align-items: center;
            gap: 10px;
        }
        
        .upload-another {
            background: linear-gradient(45deg, #667eea, #764ba2);
            color: white;
        }
        
        .view-files {
            background: #4CAF50;
            color: white;
        }
        
        .go-home {
            background: #f8f9fa;
            color: #333;
            border: 2px solid #ddd;
        }
        
        .nav-link:hover {
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            text-decoration: none;
        }
        
        .progress-bar {
            width: 100%;
            height: 20px;
            background: #f0f0f0;
            border-radius: 10px;
            overflow: hidden;
            margin: 20px 0;
        }
        
        .progress-fill {
            height: 100%;
            background: linear-gradient(90deg, #4CAF50, #8BC34A);
            width: 100%;
            animation: progress 1s ease-in-out;
        }
        
        @keyframes progress {
            from { width: 0; }
            to { width: 100%; }
        }
        
        .file-preview {
            max-width: 200px;
            max-height: 200px;
            margin: 20px auto;
            border-radius: 10px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }
        
        @media (max-width: 768px) {
            body {
                margin: 20px auto;
                padding: 10px;
            }
            
            .container {
                padding: 20px;
            }
            
            .nav-links {
                flex-direction: column;
            }
            
            .file-info {
                padding: 15px;
            }
        }
    </style>
    <script>
        // Animation de chargement
        window.onload = function() {
            const progressBar = document.getElementById('progressBar');
            if (progressBar) {
                progressBar.style.width = '100%';
            }
        };
    </script>
</head>
<body>
    <div class="container">
        <c:choose>
            <c:when test="${uploadSuccess}">
                <div class="icon success-icon">✅</div>
                <h1>Upload Réussi !</h1>
                
                <div class="message">
                    Votre fichier a été uploadé avec succès sur le serveur.
                </div>
                
                <div class="progress-bar">
                    <div class="progress-fill" id="progressBar"></div>
                </div>
                
                <div class="file-info">
                    <h3>📋 Détails du fichier</h3>
                    <div class="file-detail">
                        <span class="detail-label">Nom du fichier :</span>
                        <span class="detail-value">${fileName}</span>
                    </div>
                    <div class="file-detail">
                        <span class="detail-label">Taille :</span>
                        <span class="detail-value">${fileSize} bytes</span>
                    </div>
                    <div class="file-detail">
                        <span class="detail-label">Chemin :</span>
                        <span class="detail-value">${filePath}</span>
                    </div>
                    <div class="file-detail">
                        <span class="detail-label">Statut :</span>
                        <span class="detail-value" style="color: #4CAF50; font-weight: bold;">✓ Sauvegardé</span>
                    </div>
                </div>
                
            </c:when>
            <c:otherwise>
                <div class="icon error-icon">❌</div>
                <h1>Échec de l'Upload</h1>
                <div class="message">
                    ${message}
                </div>
            </c:otherwise>
        </c:choose>
        
        <div class="nav-links">
            <a href="upload-form" class="nav-link upload-another">
                📤 Uploader un autre fichier
            </a>
            <a href="list-files" class="nav-link view-files">
                📁 Voir tous les fichiers
            </a>
            <a href="upload-form" class="nav-link go-home">
                🏠 Retour à l'accueil
            </a>
        </div>
    </div>
</body>
</html>