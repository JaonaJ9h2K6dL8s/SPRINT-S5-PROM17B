<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Upload de Fichier</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            max-width: 800px;
            margin: 50px auto;
            padding: 20px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }
        
        .container {
            background-color: white;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
        }
        
        h1 {
            color: #333;
            text-align: center;
            margin-bottom: 30px;
            font-size: 2.5em;
            background: linear-gradient(45deg, #667eea, #764ba2);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }
        
        .form-group {
            margin-bottom: 25px;
        }
        
        label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #555;
            font-size: 1.1em;
        }
        
        .file-input-wrapper {
            position: relative;
            overflow: hidden;
            display: inline-block;
            width: 100%;
        }
        
        .file-input-wrapper input[type="file"] {
            position: absolute;
            left: 0;
            top: 0;
            opacity: 0;
            width: 100%;
            height: 100%;
            cursor: pointer;
        }
        
        .file-input-button {
            display: block;
            padding: 15px;
            background: #f8f9fa;
            border: 2px dashed #667eea;
            border-radius: 10px;
            text-align: center;
            color: #667eea;
            font-weight: bold;
            transition: all 0.3s ease;
        }
        
        .file-input-button:hover {
            background: #667eea;
            color: white;
        }
        
        .selected-file {
            margin-top: 10px;
            padding: 10px;
            background: #e8f4fd;
            border-radius: 5px;
            color: #2196F3;
            display: none;
        }
        
        input[type="submit"] {
            background: linear-gradient(45deg, #667eea, #764ba2);
            color: white;
            padding: 15px 30px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 18px;
            font-weight: bold;
            width: 100%;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }
        
        input[type="submit"]:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        
        input[type="submit"]:disabled {
            background: #cccccc;
            cursor: not-allowed;
            transform: none;
        }
        
        .nav-links {
            margin-top: 30px;
            text-align: center;
            display: flex;
            justify-content: center;
            gap: 15px;
            flex-wrap: wrap;
        }
        
        .nav-link {
            padding: 10px 20px;
            background: #f8f9fa;
            color: #667eea;
            text-decoration: none;
            border-radius: 8px;
            font-weight: bold;
            transition: all 0.3s ease;
        }
        
        .nav-link:hover {
            background: #667eea;
            color: white;
            text-decoration: none;
            transform: translateY(-2px);
        }
        
        .info-box {
            background: #e8f4fd;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 25px;
            border-left: 4px solid #2196F3;
        }
        
        .info-box h3 {
            color: #2196F3;
            margin-top: 0;
        }
        
        @media (max-width: 768px) {
            body {
                margin: 20px auto;
                padding: 10px;
            }
            
            .container {
                padding: 20px;
            }
            
            h1 {
                font-size: 2em;
            }
            
            .nav-links {
                flex-direction: column;
            }
        }
    </style>
    <script>
        function updateFileName(input) {
            const fileName = input.files[0] ? input.files[0].name : 'Aucun fichier sélectionné';
            document.getElementById('selectedFileName').textContent = fileName;
            document.getElementById('selectedFile').style.display = 'block';
            
            // Activer le bouton submit si un fichier est sélectionné
            document.getElementById('submitBtn').disabled = input.files.length === 0;
        }
        
        function validateForm() {
            const fileInput = document.getElementById('fichier');
            if (fileInput.files.length === 0) {
                alert('Veuillez sélectionner un fichier à uploader.');
                return false;
            }
            
            const file = fileInput.files[0];
            const maxSize = 10 * 1024 * 1024; // 10MB
            
            if (file.size > maxSize) {
                alert('Le fichier est trop volumineux. Taille maximum: 10MB');
                return false;
            }
            
            return true;
        }
    </script>
</head>
<body>
    <div class="container">
        <h1>📤 Upload de Fichier</h1>
        
        <div class="info-box">
            <h3>📋 Instructions</h3>
            <p>• Sélectionnez un fichier à uploader (max 10MB)</p>
            <p>• Formats supportés: tous les types de fichiers</p>
            <p>• Les fichiers sont stockés dans le dossier /uploads</p>
        </div>
        
        <form action="upload" method="post" enctype="multipart/form-data" onsubmit="return validateForm()">
            <div class="form-group">
                <label for="fichier">Sélectionnez un fichier :</label>
                <div class="file-input-wrapper">
                    <div class="file-input-button">
                        <span id="fileButtonText">📁 Cliquez pour choisir un fichier</span>
                        <input type="file" id="fichier" name="fichier" required 
                               onchange="updateFileName(this)">
                    </div>
                </div>
                <div class="selected-file" id="selectedFile">
                    Fichier sélectionné : <strong id="selectedFileName">Aucun fichier sélectionné</strong>
                </div>
            </div>
            
            <input type="submit" id="submitBtn" value="📤 Uploader le fichier" disabled>
        </form>
        
        <div class="nav-links">
            <a href="list-files" class="nav-link">📁 Voir les fichiers uploadés</a>
            <a href="upload-form" class="nav-link">🔄 Nouvel upload</a>
        </div>
    </div>
</body>
</html>