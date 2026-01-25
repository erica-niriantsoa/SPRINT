<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Connexion - Sprint 11-bis</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 500px;
            margin: 50px auto;
            padding: 20px;
            background-color: #f4f4f4;
        }
        .login-container {
            background-color: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            text-align: center;
        }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            color: #555;
            font-weight: bold;
        }
        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        button {
            width: 100%;
            padding: 12px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }
        button:hover {
            background-color: #45a049;
        }
        .error {
            color: #d32f2f;
            background-color: #ffebee;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        .info {
            background-color: #e3f2fd;
            padding: 15px;
            border-radius: 4px;
            margin-top: 20px;
        }
        .info h3 {
            margin-top: 0;
            color: #1976d2;
        }
        .user-list {
            font-size: 14px;
        }
        .user-list li {
            margin: 5px 0;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <h1>Connexion</h1>
        
        <% if (request.getAttribute("error") != null) { %>
            <div class="error">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>
        
        <form action="login" method="post">
            <div class="form-group">
                <label for="username">Nom d'utilisateur :</label>
                <input type="text" id="username" name="username" required>
            </div>
            
            <div class="form-group">
                <label for="password">Mot de passe :</label>
                <input type="password" id="password" name="password" required>
            </div>
            
            <button type="submit">Se connecter</button>
        </form>
        
        <div class="info">
            <h3>Comptes de test disponibles :</h3>
            <ul class="user-list">
                <li><strong>admin</strong> / admin123 (rôle: admin)</li>
                <li><strong>chef</strong> / chef123 (rôle: chef)</li>
                <li><strong>employe</strong> / employe123 (rôle: employe)</li>
                <li><strong>user</strong> / user123 (rôle: user)</li>
            </ul>
        </div>
        
        <div style="margin-top: 20px; text-align: center;">
            <a href="public">Page publique (sans authentification)</a>
        </div>
    </div>
</body>
</html>
