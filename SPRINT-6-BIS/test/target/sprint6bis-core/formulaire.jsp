<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Formulaire de Test</title>
</head>
<body>
    <h1>🔍 Test des Paramètres @RequestParam</h1>
    
    <!-- FORMULAIRES GÉNÉRÉS AUTOMATIQUEMENT -->
    <div style="border: 1px solid #ccc; padding: 15px; margin: 10px;">
        <h2>📝 Inscription (nom)</h2>
        <form action="inscription" method="post">
            <input type="text" name="nom" placeholder="Votre nom" required>
            <input type="submit" value="Tester Inscription">
        </form>
    </div>
    
    <div style="border: 1px solid #ccc; padding: 15px; margin: 10px;">
        <h2>👤 Profil (userId)</h2>
        <form action="profil" method="post">
            <input type="number" name="userId" placeholder="ID utilisateur" required>
            <input type="submit" value="Tester Profil">
        </form>
    </div>
    
    <h2>Test 3: Paramètre optionnel "age"</h2>
    <form action="profil" method="post">
        <label for="age">Age (optionnel):</label>
        <input type="number" id="age" name="age" placeholder="Votre age">
        <br><br>
        <input type="submit" value="Tester age optionnel">
    </form>
    
</body>
</html>