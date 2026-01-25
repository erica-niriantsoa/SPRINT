<%@ page import="framework.security.User" %>
<%@ page import="framework.security.AuthenticationManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) session.getAttribute(AuthenticationManager.SESSION_USER_KEY);
%>
<html>
<head>
    <title>Page protégée - Sprint 11-bis</title>
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
            color: #2196F3;
        }
        .alert {
            background-color: #fff3cd;
            border: 1px solid #ffc107;
            padding: 15px;
            border-radius: 4px;
            margin: 20px 0;
        }
        .user-info {
            background-color: #e3f2fd;
            padding: 15px;
            border-radius: 4px;
            margin: 20px 0;
        }
        a {
            display: inline-block;
            margin: 5px 10px 5px 0;
            padding: 10px 15px;
            background-color: #2196F3;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }
        a:hover {
            background-color: #1976D2;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>🔒 Page Protégée</h1>
        
        <div class="alert">
            <strong>@AuthenticatedOnly</strong> - Cette page nécessite une authentification.
            <br>Si vous voyez cette page, c'est que vous êtes bien connecté !
        </div>
        
        <div class="user-info">
            <h3>Vous êtes connecté en tant que :</h3>
            <p><strong>Utilisateur :</strong> <%= user != null ? user.getUsername() : "N/A" %></p>
            <p><strong>Rôle :</strong> <%= user != null ? user.getRole() : "N/A" %></p>
        </div>
        
        <p>Cette page est accessible à tous les utilisateurs authentifiés, quel que soit leur rôle.</p>
        
        <div style="margin-top: 30px;">
            <a href="chef-only">Page Chef</a>
            <a href="admin-area">Zone Admin</a>
            <a href="public">Page publique</a>
            <a href="logout" style="background-color: #f44336;">Déconnexion</a>
        </div>
    </div>
</body>
</html>
