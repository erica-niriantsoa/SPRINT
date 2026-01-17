<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Couleur Enregistrée - Sprint 11</title>
</head>
<body>
    <div class="container">
        <div class="success-icon"></div>
        <h1>Couleur Enregistrée avec Succès !</h1>
        
        <div class="color-preview">
            ${couleur}
        </div>
        
        <div class="message">
            ${message}
            <br><br>
            Votre couleur préférée est maintenant enregistrée dans votre session.
            Elle sera conservée pendant toute votre navigation.
        </div>
        
        <div class="actions">
            <a href="<%= request.getContextPath() %>/session/choix-couleur">🎨 Changer de couleur</a>
            <a href="<%= request.getContextPath() %>/session/afficher" class="secondary">📊 Voir la session</a>
            <a href="<%= request.getContextPath() %>/" class="secondary">🏠 Accueil</a>
        </div>
    </div>
</body>
</html>
