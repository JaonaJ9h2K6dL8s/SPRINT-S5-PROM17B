<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Formulaire Élève</title>

</head>
<body>
<div class="container">
    <h1>Formulaire Élève</h1>
    
    <div class="method-select">
        <label>Méthode :</label>
        <select id="method">
            <option value="1">Méthode 1: Objet Eleve</option>
            <option value="2">Méthode 2: RequestParam</option>
        </select>
    </div>
    
    <form id="eleveForm" method="post">
        <div class="form-group">
            <label>Nom :</label>
            <input type="text" name="nom" required>
        </div>
        
        <div class="form-group">
            <label>Âge :</label>
            <input type="number" name="age" min="10" max="20" required>
        </div>
        
        <h3>Note :</h3>
        <div class="form-group">
            <label>Matière :</label>
            <input type="text" name="note.matiere" placeholder="Mathématiques">
        </div>
        
        <div class="form-group">
            <label>Note (sur 20) :</label>
            <input type="number" name="note.valeur" step="0.5" min="0" max="20" placeholder="15.5">
        </div>
        
        <button type="submit">Enregistrer</button>
    </form>
    
    <div style="text-align: center; margin-top: 20px;">
        <a href="/sprint8bis-core/eleve/demo">Voir la démo</a>
    </div>
</div>

<script>
    document.getElementById('eleveForm').addEventListener('submit', function(e) {
        const method = document.getElementById('method').value;
        
        // Toujours créer des champs pour les deux méthodes
        const matiereValue = document.querySelector('[name="note.matiere"]').value;
        const valeurValue = document.querySelector('[name="note.valeur"]').value;
        
        // Créer des champs cachés pour la méthode 2
        if (method === '2') {
            let matiereInput = document.createElement('input');
            matiereInput.type = 'hidden';
            matiereInput.name = 'matiere';
            matiereInput.value = matiereValue;
            this.appendChild(matiereInput);
            
            let valeurInput = document.createElement('input');
            valeurInput.type = 'hidden';
            valeurInput.name = 'valeur';
            valeurInput.value = valeurValue;
            this.appendChild(valeurInput);
            
            this.action = '/sprint8bis-core/eleve/save2';
        } else {
            this.action = '/sprint8bis-core/eleve/save';
        }
    });
</script>
</body>
</html>