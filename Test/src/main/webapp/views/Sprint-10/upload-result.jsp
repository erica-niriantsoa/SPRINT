<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Résultat Upload</title>
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
        .error {
            background-color: #f8d7da;
            color: #721c24;
            padding: 15px;
            border-radius: 4px;
            border-left: 4px solid #dc3545;
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
            width: 150px;
        }
        .value {
            color: #333;
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
        <h1> Résultat de l'Upload</h1>
        
        <% Boolean success = (Boolean) request.getAttribute("success"); %>
        <% if (success != null && success) { %>
            <div class="success">
                 <%= request.getAttribute("message") %>
            </div>
            
            <div class="info-box">
                <h3> Informations reçues :</h3>
                
                <div class="info-row">
                    <span class="label">Titre :</span>
                    <span class="value"><%= request.getAttribute("titre") %></span>
                </div>
                
                <div class="info-row">
                    <span class="label">Description :</span>
                    <span class="value"><%= request.getAttribute("description") %></span>
                </div>
                
                <div class="info-row">
                    <span class="label">Taille du fichier :</span>
                    <span class="value"><%= request.getAttribute("fileSize") %> octets</span>
                </div>
                
                <div class="info-row">
                    <span class="label">Taille (KB) :</span>
                    <span class="value"><%= String.format("%.2f", request.getAttribute("fileSizeKB")) %> KB</span>
                </div>
                
                <% if (request.getAttribute("savedPath") != null) { %>
                <div class="info-row">
                    <span class="label"> Fichier sauvegardé :</span>
                    <span class="value"><%= request.getAttribute("fileName") %></span>
                </div>
                
                <div class="info-row">
                    <span class="label"> Emplacement :</span>
                    <span class="value" style="font-size: 11px; word-break: break-all;"><%= request.getAttribute("savedPath") %></span>
                </div>
                <% } %>
            </div>
        <% } else { %>
            <div class="error">
                 <%= request.getAttribute("message") %>
            </div>
        <% } %>
        
        <a href="<%= request.getContextPath() %>/upload" class="btn"> Retour au formulaire</a>
    </div>
</body>
</html>
