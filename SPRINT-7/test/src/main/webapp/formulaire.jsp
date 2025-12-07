<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<body>

    <h2>Formulaire GET</h2>
    <form action="getgetrequet" method="GET">
        <div>
            <label>Paramètre GET:</label>
            <input type="text" name="paramGet" value="${paramGet != null ? paramGet : 'valeur_get'}">
        </div>
        <div>
            <label>Nom GET:</label>
            <input type="text" name="nomGet" value="${nomGet != null ? nomGet : ''}">
        </div>
        <div>
            <label>Email GET:</label>
            <input type="email" name="emailGet" value="${emailGet != null ? emailGet : ''}">
        </div>
        <button type="submit">Envoyer en GET</button>
    </form>
    
    <c:if test="${donneesGet != null}">
        <div>
            <h3>Réponse GET:</h3>
            <p>${donneesGet}</p>
            <p>Timestamp GET: ${timestampGet}</p>
        </div>
    </c:if>
    
    <hr>
    
    <!-- Formulaire POST -->
    <h2>Formulaire POST</h2>
    <form action="getgetrequet" method="POST">
        <div>
            <label>Nom POST:</label>
            <input type="text" name="nom" value="${nom != null ? nom : ''}">
        </div>
        <div>
            <label>Email POST:</label>
            <input type="email" name="email" value="${email != null ? email : ''}">
        </div>
        <button type="submit">Envoyer en POST</button>
    </form>
    
    <!-- Réponse POST -->
    <c:if test="${messagePost != null}">
        <div>
            <h3>Réponse POST:</h3>
            <p>${messagePost}</p>
            <p>Timestamp POST: ${timestampPost}</p>
        </div>
    </c:if>
    
    <hr>
</body>
</html>