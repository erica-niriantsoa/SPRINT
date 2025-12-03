<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SPRINT 8 - Formulaire</title>
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
            max-width: 500px;
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
            margin-bottom: 25px;
            font-size: 1.3em;
            text-align: center;
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
        
        input[type="text"] {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 1em;
            transition: all 0.3s ease;
        }
        
        input[type="text"]:focus {
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
    </style>
</head>
<body>
    <div class="container">
        <h1>SPRINT 8 - Test Conversion Types</h1>

        <div class="form-card">
            <h2>Formulaire de Test Multi-Types</h2>
            <p style="color: #666; font-size: 0.9em; margin-bottom: 20px; text-align: center;">
                Testez la différence entre Map&lt;String, Object&gt; et Map&lt;String, String&gt;
            </p>
            
            <form id="testForm" method="get">
                <div class="form-group">
                    <label for="nom">Nom (String)</label>
                    <input type="text" id="nom" name="nom" value="Erica" required>
                </div>
                
                <div class="form-group">
                    <label for="age">Age (Integer)</label>
                    <input type="number" id="age" name="age" value="25" required>
                </div>
                
                <div class="form-group">
                    <label for="note">Note (Double)</label>
                    <input type="text" id="note" name="note" value="15.5" pattern="[0-9]+(\.[0-9]+)?" required>
                </div>
                
                <div class="form-group">
                    <label>Actif (Boolean)</label>
                    <div class="radio-group">
                        <div class="radio-option">
                            <input type="radio" id="actif_true" name="actif" value="true" checked>
                            <label for="actif_true">Oui (true)</label>
                        </div>
                        <div class="radio-option">
                            <input type="radio" id="actif_false" name="actif" value="false">
                            <label for="actif_false">Non (false)</label>
                        </div>
                    </div>
                </div>
                
                <div class="form-group">
                    <label>Genre (String)</label>
                    <div class="radio-group">
                        <div class="radio-option">
                            <input type="radio" id="homme" name="genre" value="Homme">
                            <label for="homme">Homme</label>
                        </div>
                        <div class="radio-option">
                            <input type="radio" id="femme" name="genre" value="Femme" checked>
                            <label for="femme">Femme</label>
                        </div>
                    </div>
                </div>
                
                <button type="button" onclick="submitToObject()" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
                    Map&lt;String, Object&gt;
                </button>
                
                <button type="button" onclick="submitToString()" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); margin-top: 10px;">
                     Map&lt;String, String&gt;
                </button>
                
                <button type="button" onclick="submitToInteger()" style="background: linear-gradient(135deg, #fa709a 0%, #fee140 100%); margin-top: 10px;">
                     Map&lt;String, Integer&gt;
                </button>
            </form>
        </div>
        
        <script>
            function submitToObject() {
                document.getElementById('testForm').action = '/Test/test-object';
                document.getElementById('testForm').submit();
            }
            
            function submitToString() {
                document.getElementById('testForm').action = '/Test/test-string';
                document.getElementById('testForm').submit();
            }
            
            function submitToInteger() {
                document.getElementById('testForm').action = '/Test/test-integer';
                document.getElementById('testForm').submit();
            }
        </script>
    </div>
</body>
</html>