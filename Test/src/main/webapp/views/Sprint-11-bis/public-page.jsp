<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Page Publique - Sprint 11-bis</title>
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
        }
        .alert {
            background-color: #e8f5e9;
            border: 1px solid #4CAF50;
            padding: 15px;
            border-radius: 4px;
            margin: 20px 0;
        }
        a {
            display: inline-block;
            margin: 5px 10px 5px 0;
            padding: 10px 15px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }
        a:hover {
            background-color: #45a049;
        }
        .login-link {
            background-color: #2196F3 !important;
        }
        .login-link:hover {
            background-color: #1976D2 !important;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>🌍 Page Publique</h1>
        
        <div class="alert">
            <strong>Aucune annotation de sécurité</strong> - Cette page est accessible à tous, sans authentification.
        </div>
        
        <p>Cette page publique est accessible à tous les visiteurs, qu'ils soient connectés ou non.</p>
        
        <p>Le système de sécurité du Sprint 11-bis permet de :</p>
        <ul>
            <li><strong>@AuthenticatedOnly</strong> : Restreindre l'accès aux utilisateurs authentifiés</li>
            <li><strong>@RoleRequired({"role1", "role2"})</strong> : Restreindre l'accès à des rôles spécifiques</li>
            <li>Laisser les pages sans annotation accessibles à tous (comme celle-ci)</li>
        </ul>
        
        <h3>Testez le système de sécurité :</h3>
        <div style="margin-top: 30px;">
            <a href="login" class="login-link">Se connecter</a>
            <a href="protected">Page protégée</a>
            <a href="chef-only">Page Chef</a>
            <a href="admin-area">Zone Admin</a>
        </div>
    </div>
</body>
</html>
