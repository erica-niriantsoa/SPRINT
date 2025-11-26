<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Profil Etudiant - Sprint 6</title>
    <style>
        body { 
            font-family: Arial, sans-serif; 
            margin: 40px;
            background: #f5f5f5;
        }
        .profil-card {
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            max-width: 600px;
        }
        h1 { color: #2c3e50; }
        .info-row {
            margin: 15px 0;
            padding: 10px;
            background: #ecf0f1;
            border-radius: 4px;
        }
        .label { font-weight: bold; color: #34495e; }
        ul { list-style-type: none; padding: 0; }
        li { 
            padding: 8px;
            margin: 5px 0;
            background: #3498db;
            color: white;
            border-radius: 4px;
        }
        .badge {
            background: #27ae60;
            color: white;
            padding: 5px 10px;
            border-radius: 3px;
            font-size: 12px;
        }
    </style>
</head>
<body>
    <div class="profil-card">
        <h1>Profil Etudiant <span class="badge">SPRINT 6</span></h1>
        
        <div class="info-row">
            <span class="label">ID:</span> 
            <%= request.getAttribute("etudiantId") %>
        </div>
        
        <div class="info-row">
            <span class="label">Nom:</span> 
            <%= request.getAttribute("nom") %>
        </div>
        
        <div class="info-row">
            <span class="label">Email:</span> 
            <%= request.getAttribute("email") %>
        </div>
        
        <h2>Matieres inscrites:</h2>
        <ul>
        <%
            List<String> matieres = (List<String>) request.getAttribute("matieres");
            if (matieres != null) {
                for (String matiere : matieres) {
        %>
            <li><%= matiere %></li>
        <%
                }
            }
        %>
        </ul>
        
        <p><a href="/Test/">Retour a l'accueil</a></p>
    </div>
</body>
</html>