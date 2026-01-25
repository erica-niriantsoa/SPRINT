<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Déconnexion réussie - Sprint 11-bis</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 500px;
            margin: 50px auto;
            padding: 20px;
            background-color: #f4f4f4;
        }
        .container {
            background-color: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            text-align: center;
        }
        h1 {
            color: #4CAF50;
        }
        p {
            font-size: 18px;
            margin: 20px 0;
        }
        a {
            display: inline-block;
            margin: 10px;
            padding: 12px 24px;
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
        <h1>✓ Déconnexion réussie</h1>
        <p>Vous avez été déconnecté avec succès.</p>
        <p>Votre session a été supprimée.</p>
        
        <div style="margin-top: 30px;">
            <a href="login">Se reconnecter</a>
            <a href="public">Page publique</a>
        </div>
    </div>
</body>
</html>
