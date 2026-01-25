<%@ page import="framework.security.User" %>
<%@ page import="framework.security.AuthenticationManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) session.getAttribute(AuthenticationManager.SESSION_USER_KEY);
%>
<html>
<head>
    <title>Connexion réussie - Sprint 11-bis</title>
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
            color: #4CAF50;
            text-align: center;
        }
        .user-info {
            background-color: #e8f5e9;
            padding: 20px;
            border-radius: 4px;
            margin: 20px 0;
        }
        .links {
            margin-top: 30px;
        }
        .links a {
            display: block;
            padding: 12px;
            margin: 10px 0;
            background-color: #2196F3;
            color: white;
            text-decoration: none;
            text-align: center;
            border-radius: 4px;
        }
        .links a:hover {
            background-color: #1976D2;
        }
        .logout-link {
            background-color: #f44336 !important;
        }
        .logout-link:hover {
            background-color: #d32f2f !important;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>✓ Connexion réussie !</h1>
        
        <div class="user-info">
            <h2>Informations de votre compte :</h2>
            <p><strong>Nom d'utilisateur :</strong> <%= user != null ? user.getUsername() : "N/A" %></p>
            <p><strong>Rôle :</strong> <%= user != null ? user.getRole() : "N/A" %></p>
        </div>
        
        <div class="links">
            <h3>Pages à tester :</h3>
            <a href="protected">Page protégée (authentification requise)</a>
            <a href="chef-only">Page Chef uniquement (rôle: chef)</a>
            <a href="admin-area">Zone Admin (rôle: chef ou admin)</a>
            <a href="public">Page publique</a>
            <a href="api/current-user">API - Utilisateur actuel (JSON)</a>
            <a href="logout" class="logout-link">Se déconnecter</a>
        </div>
    </div>
</body>
</html>
