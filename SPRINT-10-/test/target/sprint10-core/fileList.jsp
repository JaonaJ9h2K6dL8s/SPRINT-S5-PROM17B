<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Liste des Fichiers</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            max-width: 1000px;
            margin: 50px auto;
            padding: 20px;
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
            min-height: 100vh;
        }
        
        .container {
            background-color: white;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        }
        
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            flex-wrap: wrap;
            gap: 20px;
        }
        
        h1 {
            color: #333;
            margin: 0;
            font-size: 2.5em;
            background: linear-gradient(45deg, #667eea, #764ba2);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }
        
        .stats {
            display: flex;
            gap: 15px;
            flex-wrap: wrap;
        }
        
        .stat-box {
            background: #f8f9fa;
            padding: 15px 25px;
            border-radius: 10px;
            text-align: center;
            min-width: 120px;
        }
        
        .stat-number {
            font-size: 2em;
            font-weight: bold;
            color: #667eea;
            display: block;
        }
        
        .stat-label {
            color: #666;
            font-size: 0.9em;
        }
        
        .files-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        
        .files-table th {
            background: linear-gradient(45deg, #667eea, #764ba2);
            color: white;
            padding: 15px;
            text-align: left;
            font-weight: 600;
        }
        
        .files-table td {
            padding: 15px;
            border-bottom: 1px solid #eee;
            color: #555;
        }
        
        .files-table tr:hover {
            background-color: #f8f9fa;
        }
        
        .file-icon {
            color: #667eea;
            font-size: 1.2em;
            margin-right: 10px;
        }
        
        .file-name {
            font-weight: 500;
            color: #333;
        }
        
        .file-size {
            font-family: monospace;
            color: #666;
        }
        
        .file-date {
            color: #888;
            font-size: 0.9em;
        }
        
        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #888;
        }
        
        .empty-icon {
            font-size: 60px;
            margin-bottom: 20px;
            opacity: 0.5;
        }
        
        .empty-state h3 {
            color: #666;
            margin-bottom: 10px;
        }
        
        .empty-state p {
            margin-bottom: 30px;
        }
        
        .actions {
            display: flex;
            gap: 15px;
            margin-top: 30px;
            justify-content: center;
            flex-wrap: wrap;
        }
        
        .action-btn {
            padding: 12px 25px;
            text-decoration: none;
            border-radius: 8px;
            font-weight: bold;
            transition: all 0.3s ease;
            display: inline-flex;
            align-items: center;
            gap: 10px;
        }
        
        .upload-btn {
            background: linear-gradient(45deg, #667eea, #764ba2);
            color: white;
        }
        
        .refresh-btn {
            background: #4CAF50;
            color: white;
        }
        
        .home-btn {
            background: #f8f9fa;
            color: #333;
            border: 2px solid #ddd;
        }
        
        .action-btn:hover {
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            text-decoration: none;
        }
        
        .delete-form {
            display: inline;
        }
        
        .delete-btn {
            background: #f44336;
            color: white;
            border: none;
            padding: 5px 10px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 0.9em;
        }
        
        .delete-btn:hover {
            background: #d32f2f;
        }
        
        .upload-path {
            background: #e8f4fd;
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 20px;
            font-family: monospace;
            color: #2196F3;
            word-break: break-all;
        }
        
        @media (max-width: 768px) {
            body {
                margin: 20px auto;
                padding: 10px;
            }
            
            .container {
                padding: 20px;
            }
            
            .header {
                flex-direction: column;
                text-align: center;
            }
            
            .files-table {
                display: block;
                overflow-x: auto;
            }
            
            .files-table th,
            .files-table td {
                padding: 10px;
            }
            
            .actions {
                flex-direction: column;
            }
        }
    </style>
    <script>
        function confirmDelete(fileName) {
            return confirm("Êtes-vous sûr de vouloir supprimer le fichier \"" + fileName + "\" ?");
        }
        
        function refreshPage() {
            window.location.reload();
        }
    </script>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>📁 Fichiers Uploadés</h1>
            <div class="stats">
                <div class="stat-box">
                    <span class="stat-number">${fileCount}</span>
                    <span class="stat-label">Fichiers</span>
                </div>
                <div class="stat-box">
                    <span class="stat-number">
                        <c:set var="totalSize" value="0" />
                        <c:forEach var="file" items="${files}">
                            <c:set var="totalSize" value="${totalSize + file.size}" />
                        </c:forEach>
                        <c:choose>
                            <c:when test="${totalSize < 1024}">${totalSize} B</c:when>
                            <c:when test="${totalSize < 1048576}">
                                <fmt:formatNumber value="${totalSize / 1024}" maxFractionDigits="1" /> KB
                            </c:when>
                            <c:otherwise>
                                <fmt:formatNumber value="${totalSize / 1048576}" maxFractionDigits="2" /> MB
                            </c:otherwise>
                        </c:choose>
                    </span>
                    <span class="stat-label">Total</span>
                </div>
            </div>
        </div>
        
        <div class="upload-path">
            📁 Dossier d'upload : ${uploadPath}
        </div>
        
        <c:choose>
            <c:when test="${not empty files}">
                <table class="files-table">
                    <thead>
                        <tr>
                            <th>Fichier</th>
                            <th>Taille</th>
                            <th>Date de modification</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="file" items="${files}">
                            <tr>
                                <td>
                                    <span class="file-icon">📄</span>
                                    <span class="file-name">${file.name}</span>
                                </td>
                                <td class="file-size">${file.formattedSize}</td>
                                <td class="file-date">${file.formattedDate}</td>
                                <td>
                                    <form class="delete-form" action="delete-file" method="post" onsubmit="return confirmDelete('${file.name}')">
                                        <input type="hidden" name="fileName" value="${file.name}">
                                        <button type="submit" class="delete-btn">🗑️ Supprimer</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <div class="empty-icon">📁</div>
                    <h3>Aucun fichier uploadé</h3>
                    <p>Le dossier d'upload est vide. Commencez par uploader un fichier.</p>
                </div>
            </c:otherwise>
        </c:choose>
        
        <div class="actions">
            <a href="upload-form" class="action-btn upload-btn">
                📤 Uploader un fichier
            </a>
            <button onclick="refreshPage()" class="action-btn refresh-btn">
                🔄 Actualiser la liste
            </button>
            <a href="upload-form" class="action-btn home-btn">
                🏠 Retour à l'accueil
            </a>
        </div>
    </div>
</body>
</html>