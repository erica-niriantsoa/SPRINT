<%@ page import="framework.security.User" %>
<%@ page import="framework.security.AuthenticationManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) session.getAttribute(AuthenticationManager.SESSION_USER_KEY);
%>
<html>
<head>
    <title>Page Chef - Sprint 11-bis</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 700px;
            margin: 50px auto;
            padding: 20px;
            background-color: #f4f4f4;
        }
        .container {
            background-color: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h1 {
            color: #ff9800;
        }
        .alert {
            background-color: #fff3e0;
            border: 1px solid #ff9800;
            padding: 15px;
            border-radius: 4px;
            margin: 20px 0;
        }
        .user-info {
            background-color: #ffe0b2;
            padding: 15px;
            border-radius: 4px;
            margin: 20px 0;
        }
        a {
            display: inline-block;
            margin: 5px 10px 5px 0;
            padding: 10px 15px;
            background-color: #ff9800;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }
        a:hover {
            background-color: #f57c00;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>👔 Page Réservée aux Chefs</h1>
        
        <div class="alert">
            <strong>@RoleRequired({"chef"})</strong> - Cette page est réservée aux utilisateurs avec le rôle "chef".
            <br>Si vous voyez cette page, c'est que vous avez bien le rôle requis !
        </div>
        
        <div class="user-info">
            <h3>Informations de connexion :</h3>
            <p><strong>Utilisateur :</strong> <%= user != null ? user.getUsername() : "N/A" %></p>
            <p><strong>Rôle :</strong> <%= user != null ? user.getRole() : "N/A" %></p>
        </div>
        
        <p>Cette page contient des fonctionnalités réservées aux chefs. Seuls les utilisateurs avec le rôle "chef" peuvent y accéder.</p>
        
        <h3>Actions disponibles pour les chefs :</h3>
        <ul>
            <li>Gérer les équipes</li>
            <li>Valider les congés</li>
            <li>Consulter les rapports</li>
            <li>Approuver les demandes</li>
        </ul>
        
        <div style="margin-top: 30px;">
            <a href="protected">Page protégée</a>
            <a href="admin-area">Zone Admin</a>
            <a href="public">Page publique</a>
            <a href="logout" style="background-color: #f44336;">Déconnexion</a>
        </div>
    </div>
</body>
</html>
