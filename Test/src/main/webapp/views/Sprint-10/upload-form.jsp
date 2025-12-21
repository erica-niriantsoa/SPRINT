<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sprint 10 - Upload de Fichiers</title>
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
            border-bottom: 3px solid #4CAF50;
            padding-bottom: 10px;
        }
        h2 {
            color: #555;
            margin-top: 30px;
        }
        .form-section {
            margin-bottom: 30px;
            padding: 20px;
            background: #f9f9f9;
            border-left: 4px solid #4CAF50;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #333;
        }
        input[type="text"],
        input[type="file"],
        textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        textarea {
            resize: vertical;
            min-height: 80px;
        }
        button {
            background-color: #4CAF50;
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }
        button:hover {
            background-color: #45a049;
        }
        .info {
            background-color: #e3f2fd;
            padding: 15px;
            border-left: 4px solid #2196F3;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1> SPRINT 10 - Upload de Fichiers</h1>
        
        <div class="info">
            <strong> Fonctionnalités implémentées :</strong>
            <ul>
                <li>Annotation @FileParam pour injecter les fichiers uploadés</li>
                <li>Extraction automatique des fichiers via getPart()</li>
                <li>Support de multipart/form-data</li>
                <li>Conversion des fichiers en byte[]</li>
                <li>Upload de fichiers uniques ou multiples</li>
            </ul>
        </div>

        <!-- Formulaire d'upload simple -->
        <div class="form-section">
            <h2> Upload d'un fichier simple</h2>
            <form action="<%= request.getContextPath() %>/upload/single" method="POST" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="titre">Titre :</label>
                    <input type="text" id="titre" name="titre" required>
                </div>
                
                <div class="form-group">
                    <label for="description">Description :</label>
                    <textarea id="description" name="description" required></textarea>
                </div>
                
                <div class="form-group">
                    <label for="fichier">Choisir un fichier :</label>
                    <input type="file" id="fichier" name="fichier" required>
                </div>
                
                <button type="submit"> Envoyer le fichier</button>
            </form>
        </div>

        <!-- Formulaire d'upload multiple -->
        <div class="form-section">
            <h2> Upload de plusieurs fichiers</h2>
            <form action="<%= request.getContextPath() %>/upload/multiple" method="POST" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="nom">Nom :</label>
                    <input type="text" id="nom" name="nom" required>
                </div>
                
                <div class="form-group">
                    <label for="prenom">Prénom :</label>
                    <input type="text" id="prenom" name="prenom" required>
                </div>
                
                <div class="form-group">
                    <label for="photo">Photo de profil :</label>
                    <input type="file" id="photo" name="photo" accept="image/*">
                </div>
                
                <div class="form-group">
                    <label for="cv">CV (PDF) :</label>
                    <input type="file" id="cv" name="cv" accept=".pdf">
                </div>
                
                <button type="submit"> Envoyer les fichiers</button>
            </form>
        </div>

        <!-- Test API REST -->
        <div class="form-section">
            <h2> Test API REST - Upload via AJAX</h2>
            <div class="form-group">
                <label for="nomApi">Nom du document :</label>
                <input type="text" id="nomApi" value="Document Test">
            </div>
            
            <div class="form-group">
                <label for="fichierApi">Fichier :</label>
                <input type="file" id="fichierApi">
            </div>
            
            <button onclick="uploadViaApi()"> Upload via API</button>
            
            <div id="apiResult" style="margin-top: 15px; padding: 10px; background: #f0f0f0; border-radius: 4px; display: none;">
                <strong>Résultat API :</strong>
                <pre id="apiResultContent" style="margin-top: 10px; background: white; padding: 10px; border-radius: 4px;"></pre>
            </div>
        </div>
    </div>

    <script>
        function uploadViaApi() {
            const nom = document.getElementById('nomApi').value;
            const fichier = document.getElementById('fichierApi').files[0];
            
            if (!fichier) {
                alert('Veuillez sélectionner un fichier');
                return;
            }
            
            const formData = new FormData();
            formData.append('nom', nom);
            formData.append('fichier', fichier);
            
            fetch('<%= request.getContextPath() %>/api/upload', {
                method: 'POST',
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                document.getElementById('apiResult').style.display = 'block';
                document.getElementById('apiResultContent').textContent = JSON.stringify(data, null, 2);
            })
            .catch(error => {
                alert('Erreur: ' + error);
            });
        }
    </script>
</body>
</html>
