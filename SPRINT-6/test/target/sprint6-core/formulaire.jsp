<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Formulaire Sans @RequestParam</title>
</head>
<body>
    
    <h2>📝 Test Inscription</h2>
    <form action="inscription" method="post">
        <input type="text" name="vody" placeholder="Votre nom" required>
        <br><br>
        <input type="submit" value="Tester inscription">
    </form>
</body>
</html>