<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Résultat Upload Multiple</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 800px;
            margin: 50px auto;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .container {
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
        }
        .success {
            background-color: #d4edda;
            color: #155724;
            padding: 15px;
            border-radius: 4px;
            border-left: 4px solid #28a745;
            margin: 20px 0;
        }
        .info-box {
            background-color: #f9f9f9;
            padding: 20px;
            border-radius: 4px;
            margin: 20px 0;
        }
        .info-row {
            padding: 10px 0;
            border-bottom: 1px solid #ddd;
        }
        .info-row:last-child {
            border-bottom: none;
        }
        .label {
            font-weight: bold;
            color: #555;
            display: inline-block;
            width: 180px;
        }
        .value {
            color: #333;
        }
        .file-section {
            background-color: #e8f4f8;
            padding: 15px;
            border-radius: 4px;
            margin: 10px 0;
            border-left: 4px solid #17a2b8;
        }
        .btn {
            display: inline-block;
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            margin-top: 20px;
        }
        .btn:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1> Résultat Upload Multiple</h1>
        
        <div class="success">
             <%= request.getAttribute("message") %>
        </div>
        
        <div class="info-box">
            <h3> Informations personnelles :</h3>
            
            <div class="info-row">
                <span class="label">Nom :</span>
                <span class="value"><%= request.getAttribute("nom") %></span>
            </div>
            
            <div class="info-row">
                <span class="label">Prénom :</span>
                <span class="value"><%= request.getAttribute("prenom") %></span>
            </div>
        </div>
        
        <% if (request.getAttribute("photoSize") != null) { %>
            <div class="file-section">
                <h4> Photo de profil</h4>
                <div class="info-row">
                    <span class="label">Taille :</span>
                    <span class="value"><%= request.getAttribute("photoSize") %> octets</span>
                </div>
                <div class="info-row">
                    <span class="label">Taille (KB) :</span>
                    <span class="value"><%= String.format("%.2f", request.getAttribute("photoSizeKB")) %> KB</span>
                </div>
                <% if (request.getAttribute("photoPath") != null) { %>
                <div class="info-row">
                    <span class="label"> Fichier sauvegardé :</span>
                    <span class="value"><%= request.getAttribute("photoFileName") %></span>
                </div>
                <% } %>
            </div>
        <% } else { %>
            <div class="file-section">
                <h4> Photo de profil</h4>
                <p>Aucune photo uploadée</p>
            </div>
        <% } %>
        
        <% if (request.getAttribute("cvSize") != null) { %>
            <div class="file-section">
                <h4> CV (PDF)</h4>
                <div class="info-row">
                    <span class="label">Taille :</span>
                    <span class="value"><%= request.getAttribute("cvSize") %> octets</span>
                </div>
                <div class="info-row">
                    <span class="label">Taille (KB) :</span>
                    <span class="value"><%= String.format("%.2f", request.getAttribute("cvSizeKB")) %> KB</span>
                </div>
                <% if (request.getAttribute("cvPath") != null) { %>
                <div class="info-row">
                    <span class="label"> Fichier sauvegardé :</span>
                    <span class="value"><%= request.getAttribute("cvFileName") %></span>
                </div>
                <% } %>
            </div>
        <% } else { %>
            <div class="file-section">
                <h4> CV (PDF)</h4>
                <p>Aucun CV uploadé</p>
            </div>
        <% } %>
        
        <% if (request.getAttribute("savedLocation") != null) { %>
        <div class="info-box" style="margin-top: 20px; background-color: #e7f3ff;">
            <h4> Emplacement des fichiers :</h4>
            <p style="font-size: 12px; word-break: break-all; color: #555;">
                <%= request.getAttribute("savedLocation") %>
            </p>
        </div>
        <% } %>
        
        <a href="<%= request.getContextPath() %>/upload" class="btn"> Retour au formulaire</a>
    </div>
</body>
</html>
