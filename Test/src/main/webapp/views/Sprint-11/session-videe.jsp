<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Session Vidée - Sprint 11</title>
</head>
<body>
    <div class="container">
        <div class="icon">🗑️</div>
        <h1>Session Vidée</h1>
        
        <div class="message">
            ${message}
            <br><br>
            Toutes les données de votre session ont été supprimées.
            Vous pouvez recommencer avec une session vierge.
        </div>
        
        <div class="actions">
            <a href="<%= request.getContextPath() %>/session/choix-couleur">Choisir une couleur</a>
            <a href="<%= request.getContextPath() %>/session/afficher" class="secondary"> Voir la session</a>
            <a href="<%= request.getContextPath() %>/" class="secondary">Accueil</a>
        </div>
    </div>
</body>
</html>
