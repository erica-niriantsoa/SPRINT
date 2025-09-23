<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>MiniFramework - Accueil</title>
</head>
<body>
    <h1>Bienvenue dans MiniFramework</h1>

    <p>
        <%-- Affiche le chemin demandé par l'utilisateur --%>
        <c:out value="${requestedPath}" />
    </p>

    <hr/>

    <%-- Exemple simple de routing interne dans JSP --%>
    <c:choose>
        <c:when test="${requestedPath == '/home'}">
            <p>Vous êtes sur la page d'accueil.</p>
        </c:when>
        <c:when test="${requestedPath == '/about'}">
            <p>Voici la page à propos.</p>
        </c:when>
        <c:otherwise>
            <p>Page non spécifiée. Vous êtes sur : <c:out value="${requestedPath}"/></p>
        </c:otherwise>
    </c:choose>
</body>
</html>
