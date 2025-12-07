<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Formulaire d'Inscription</title>
</head>
<body>
    <h2>Formulaire d'Inscription</h2>
    
    <p>Ce formulaire envoie à l'URL <strong>/inscription</strong></p>
    
    <form action="inscription" method="POST">
        <div>
            <label for="nom">Nom :</label>
            <input type="text" id="nom" name="nom" required>
        </div>
        
        <div>
            <label>Matières (choix multiple) :</label><br>
            <input type="checkbox" id="matiere1" name="matiere" value="Mathématiques">
            <label for="matiere1">Mathématiques</label><br>
            
            <input type="checkbox" id="matiere2" name="matiere" value="Informatique">
            <label for="matiere2">Informatique</label><br>
            
            <input type="checkbox" id="matiere3" name="matiere" value="Physique">
            <label for="matiere3">Physique</label><br>
            
            <input type="checkbox" id="matiere4" name="matiere" value="Chimie">
            <label for="matiere4">Chimie</label><br>
            
            <input type="checkbox" id="matiere5" name="matiere" value="Français">
            <label for="matiere5">Français</label><br>
            
            <input type="checkbox" id="matiere6" name="matiere" value="Anglais">
            <label for="matiere6">Anglais</label>
        </div> 
        <button type="submit">Soumettre</button>
    </form>
</body>
</html>