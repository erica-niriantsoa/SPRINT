<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Liste des departements</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        ul { list-style-type: square; }
        .info { color: #666; font-style: italic; }
    </style>
</head>
<body>
    <h1><%= request.getAttribute("titre") %></h1>
    <p class="info">Nombre de départements: <%= request.getAttribute("nombreDepts") %></p>
    
    <ul>
    <%
        List<String> departements = (List<String>) request.getAttribute("departements");
        if (departements != null) {
            for (String dept : departements) {
    %>
        <li><%= dept %></li>
    <%
            }
        } else {
    %>
        <li>Aucun département trouvé</li>
    <%
        }
    %>
    </ul>
    
    <p><a href="/Test/">Retour à l'accueil</a></p>
</body>
</html>