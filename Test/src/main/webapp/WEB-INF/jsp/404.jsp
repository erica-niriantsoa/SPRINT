<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>MiniFramework - Page introuvable</title>
</head>
<body>
    <h1>404 - Page non trouvée</h1>
    <p>La page que vous avez demandée n'existe pas.</p>
    <p>Chemin demandé : ${requestedPath}</p>
    <hr/>
    <p><a href="<c:out value='${pageContext.request.contextPath}'/>">Retour à l'accueil</a></p>
</body>
</html>
