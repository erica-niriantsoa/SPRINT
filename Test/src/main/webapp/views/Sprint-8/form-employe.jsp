<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SPRINT 8-BIS - Injection d'objet</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 40px 20px;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .container {
            max-width: 600px;
            width: 100%;
        }
        
        h1 {
            color: white;
            text-align: center;
            margin-bottom: 30px;
            font-size: 2em;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.2);
        }
        
        .form-card {
            background: white;
            border-radius: 15px;
            padding: 35px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.3);
        }
        
        h2 {
            color: #667eea;
            margin-bottom: 10px;
            font-size: 1.5em;
            text-align: center;
        }
        
        .subtitle {
            color: #666;
            font-size: 0.9em;
            margin-bottom: 25px;
            text-align: center;
            font-style: italic;
        }
        
        .form-group {
            margin-bottom: 20px;
        }
        
        label {
            display: block;
            font-weight: 600;
            color: #333;
            margin-bottom: 8px;
            font-size: 0.95em;
        }
        
        input[type="text"],
        input[type="number"] {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 1em;
            transition: all 0.3s ease;
        }
        
        input[type="text"]:focus,
        input[type="number"]:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        
        .radio-group {
            display: flex;
            gap: 30px;
            margin-top: 8px;
        }
        
        .radio-option {
            display: flex;
            align-items: center;
            gap: 8px;
            cursor: pointer;
        }
        
        input[type="radio"] {
            width: 20px;
            height: 20px;
            cursor: pointer;
            accent-color: #667eea;
        }
        
        .radio-option label {
            cursor: pointer;
            margin: 0;
            font-weight: 500;
        }
        
        button {
            width: 100%;
            padding: 15px;
            border: none;
            border-radius: 8px;
            font-size: 1.1em;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            margin-top: 25px;
            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
        }
        
        button:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(102, 126, 234, 0.6);
        }
        
        button:active {
            transform: translateY(0);
        }
        
        .info-box {
            background: #f0f7ff;
            border-left: 4px solid #667eea;
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
        }
        
        .info-box p {
            margin: 5px 0;
            font-size: 0.9em;
            color: #555;
        }
        
        .info-box strong {
            color: #667eea;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>SPRINT 8-BIS</h1>

        <div class="form-card">
            <h2>Injection d'Objet Personnalise</h2>
            <p class="subtitle">Le framework remplit automatiquement l'objet Employe</p>
            
            
            <form action="/Test/save-employe" method="post">
                <div class="form-group">
                    <label for="nom">Nom de l'employe</label>
                    <input type="text" id="nom" name="nom" value="Jean Dupont" required>
                </div>
                
                <div class="form-group">
                    <label for="age">Âge (Integer)</label>
                    <input type="number" id="age" name="age" value="30" required>
                </div>
                
                <div class="form-group">
                    <label for="salaire">Salaire (Double)</label>
                    <input type="text" id="salaire" name="salaire" value="2500.50" 
                           pattern="[0-9]+(\.[0-9]+)?" required>
                </div>
                
                <div class="form-group">
                    <label>Statut (Boolean)</label>
                    <div class="radio-group">
                        <div class="radio-option">
                            <input type="radio" id="actif_true" name="actif" value="true" checked>
                            <label for="actif_true">Actif</label>
                        </div>
                        <div class="radio-option">
                            <input type="radio" id="actif_false" name="actif" value="false">
                            <label for="actif_false">Inactif</label>
                        </div>
                    </div>
                </div>
                
                <button type="submit">Enregistrer l'Employe</button>
            </form>
        </div>
    </div>
</body>
</html>
