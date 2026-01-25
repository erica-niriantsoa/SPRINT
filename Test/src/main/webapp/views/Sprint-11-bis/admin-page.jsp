<%@ page import="framework.security.User" %>
<%@ page import="framework.security.AuthenticationManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) session.getAttribute(AuthenticationManager.SESSION_USER_KEY);
%>
<html>
<head>
    <title>Zone Admin - Sprint 11-bis</title>
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
            color: #9c27b0;
        }
        .alert {
            background-color: #f3e5f5;
            border: 1px solid #9c27b0;
            padding: 15px;
            border-radius: 4px;
            margin: 20px 0;
        }
        .user-info {
            background-color: #e1bee7;
            padding: 15px;
            border-radius: 4px;
            margin: 20px 0;
        }
        a {
            display: inline-block;
            margin: 5px 10px 5px 0;
            padding: 10px 15px;
            background-color: #9c27b0;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }
        a:hover {
            background-color: #7b1fa2;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>⚙️ Zone Administration</h1>
        
        <div class="alert">
            <strong>@RoleRequired({"chef", "admin"})</strong> - Cette page est accessible aux rôles "chef" et "admin".
            <br>Si vous voyez cette page, vous avez l'un des rôles autorisés !
        </div>
        
        <div class="user-info">
            <h3>Informations de connexion :</h3>
            <p><strong>Utilisateur :</strong> <%= user != null ? user.getUsername() : "N/A" %></p>
            <p><strong>Rôle :</strong> <%= user != null ? user.getRole() : "N/A" %></p>
        </div>
        
        <p>Cette zone administrative permet de gérer les paramètres avancés du système. Elle est accessible uniquement aux chefs et administrateurs.</p>
        
        <h3>Fonctionnalités administratives :</h3>
        <ul>
            <li>Gestion des utilisateurs</li>
            <li>Configuration du système</li>
            <li>Consultation des logs</li>
            <li>Paramètres de sécurité</li>
            <li>Statistiques avancées</li>
        </ul>
        
        <div style="margin-top: 30px;">
            <a href="protected">Page protégée</a>
            <a href="chef-only">Page Chef</a>
            <a href="public">Page publique</a>
            <a href="logout" style="background-color: #f44336;">Déconnexion</a>
        </div>
    </div>
</body>
</html>
