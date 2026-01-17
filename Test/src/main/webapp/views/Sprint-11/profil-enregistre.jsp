<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Profil Enregistré - Sprint 11</title>
</head>
<body>
    <div class="container">
        <h1> Profil Enregistré avec Succès</h1>
        
        <div class="profile-card">
            <h2> Votre Profil</h2>
            <div class="profile-item">
                <label>Nom :</label>
                <span>${nom}</span>
            </div>
            <div class="profile-item">
                <label>Âge :</label>
                <span>${age} ans</span>
            </div>
            <div class="profile-item">
                <label>Couleur préférée :</label>
                <span class="color-badge"></span>
                <span>${couleur}</span>
            </div>
        </div>
        
        <p style="text-align: center; color: #666;">
            Ces informations sont maintenant stockées dans votre session et 
            seront disponibles pendant toute votre navigation.
        </p>
        
        <div class="actions">
            <a href="<%= request.getContextPath() %>/session/afficher">📊 Voir la session</a>
            <a href="<%= request.getContextPath() %>/session/choix-couleur" class="secondary">🎨 Changer la couleur</a>
            <a href="<%= request.getContextPath() %>/" class="secondary">🏠 Accueil</a>
        </div>
    </div>
</body>
</html>
