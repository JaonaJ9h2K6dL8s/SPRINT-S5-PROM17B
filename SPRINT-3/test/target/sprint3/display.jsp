<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String className = (String) request.getAttribute("className");
    String methodName = (String) request.getAttribute("methodName"); 
    String url = (String) request.getAttribute("url");
    String error = (String) request.getAttribute("error");
%>
<html>
<head>

</head>
<body>
    <div class="container"> 
        <% if (error != null) { %>
            <div class="error">
                <strong>❌ Erreur:</strong> <%= error %>
            </div>
        <% } else if (className != null && methodName != null) { %>
            <div class="result">
                <p><strong>Class :</strong> <%= className %></p>
                <p><strong>Methode :</strong> <%= methodName %></p>
                <p><strong>URL appelée :</strong> /<%= url %></p>
            </div>
        <% } %>
    </div>
</body>
</html>