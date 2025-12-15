<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Résultat Élève</title>
</head>
<body>
<div class="container">
    
    <div class="info">
        <h3>Message :</h3>
        <p>${message}</p>
    </div>
    
    <div class="info">
        <h3>Élève :</h3>
        <div class="field">
            <label>Nom :</label>
            ${eleve.nom}
        </div>
        <div class="field">
            <label>Âge :</label>
            ${eleve.age} ans
        </div>
        
        <c:if test="${not empty eleve.note}">
        <h3>Note :</h3>
        <div class="field">
            <label>Matière :</label>
            ${eleve.note.matiere}
        </div>
        <div class="field">
            <label>Note :</label>
            ${eleve.note.valeur}/20
        </div>
        </c:if>
    </div>
    
    <div style="text-align: center; margin-top: 30px;">
        <a href="/sprint8bis-core/eleve/form" class="back-btn">← Nouvel élève</a>
    </div>

</div>
</body>
</html>